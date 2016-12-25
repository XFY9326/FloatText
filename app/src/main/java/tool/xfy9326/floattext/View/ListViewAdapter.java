package tool.xfy9326.floattext.View;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.preference.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Setting.*;
import tool.xfy9326.floattext.Utils.*;

public class ListViewAdapter extends BaseAdapter
{
    private static int FLOAT_RESULT_CODE = 0;
    private Context context;
    private Activity activity;
    private ArrayList<String> textshow;
    private ArrayList<FloatTextView> floatdata;
    private ArrayList<WindowManager.LayoutParams> floatlayout;
    private ArrayList<FloatLinearLayout> linearlayout;
    private SharedPreferences sp = null;
    private Typeface typeface = null;

    public ListViewAdapter (Activity activity, ArrayList<String> textshow)
    {
        this.context = activity;
        this.activity = activity;
        this.textshow = textshow;
        sp = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        String ttf = FloatTextSettingMethod.typeface_fix(context);
        if (ttf.equalsIgnoreCase("Default"))
        {
            typeface = null;
        }
        else
        {
            typeface = Typeface.createFromFile(ttf);
        }
    }

    public void setTextShow (ArrayList<String> ts)
    {
        this.textshow = ts;
    }

    @Override
    public int getCount ()
    {
        return textshow.size();
    }

    @Override
    public View getView (int p1, View p2, ViewGroup p3)
    {
        final int index = p1;
        View view = p2;
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.floatmanage_listview, null);
        }
        final TextView textView = (TextView) view.findViewById(R.id.textview_titleshow_floatmanage);
        App utils = ((App)context.getApplicationContext());
        ArrayList<Boolean> Show = utils.getShowFloat();
		if(Show.size() != textshow.size())
		{
			restoredata();
		}
        String listtext = textshow.get(p1);
        if (!Show.get(index))
        {
            SpannableString str = new SpannableString(listtext);
            str.setSpan(new StrikethroughSpan(), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(str);
        }
        else
        {
            textView.setText(listtext);
        }
        if (typeface != null)
        {
            textView.setTypeface(typeface);
        }
        if (utils.getTextShadow().get(index))
        {
            textView.setShadowLayer(utils.getTextShadowRadius().get(index), utils.getTextShadowX().get(index), utils.getTextShadowY().get(index), utils.getTextShadowColor().get(index));
        }
        textView.getPaint().setFakeBoldText(utils.getThickData().get(index));
        textView.setTextColor(utils.getColorData().get(index));
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setSingleLine(utils.getListTextHide());
        final Button del_button = (Button) view.findViewById(R.id.button_delete_floatmanage);
        final Button edit_button = (Button) view.findViewById(R.id.button_edit_floatmanage);
        final Button lock_button = (Button) view.findViewById(R.id.button_lock_floatmanage);
        ArrayList<Boolean> lock = ((App)context.getApplicationContext()).getLockPosition();
        if (lock.get(index))
        {
            lock_button.setBackgroundResource(R.drawable.ic_lock);
        }
        else
        {
            lock_button.setBackgroundResource(R.drawable.ic_lock_unlocked);
        }
        lock_button.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    FloatWinLock(context, index, lock_button);
                }
            });
        del_button.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    SharedPreferences spdata = PreferenceManager.getDefaultSharedPreferences(context);
                    boolean show = spdata.getBoolean("FloatDeleteAlert", false);
                    if (show)
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                            .setTitle(R.string.ask_for_delete)
                            .setMessage(R.string.ask_for_delete_alert)
                            .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                                public void onClick (DialogInterface p1, int p2)
                                {
                                    delwin(index);
                                }
                            })
                            .setNegativeButton(R.string.cancel, null);
                        dialog.show();
                    }
                    else
                    {
                        delwin(index);
                    }
                }
            });
        edit_button.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    if (!sp.getBoolean("WinOnlyShowInHome", false))
                    {
                        App utils = ((App)context.getApplicationContext());
                        floatdata = utils.getFloatView();
                        floatlayout = utils.getFloatLayout();
                        FloatWinEdit(activity, index);
                        notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(context, R.string.float_can_not_edit, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        return view;
    }

    @Override
    public long getItemId (int p1)
    {
        return p1;
    }

    @Override
    public Object getItem (int p1)
    {
        return textshow.get(p1);
    }

    private void delwin (int index)
    {
        if (!FloatWinDelete(index))
        {
            restoredata();
            if (!FloatWinDelete(index))
            {
                Toast.makeText(context, R.string.float_list_del_err, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, R.string.delete_text_ok, Toast.LENGTH_SHORT).show();
        }
    }

    private void FloatWinLock (Context context, int index, Button lock_button)
    {
        ArrayList<Boolean> lock = ((App)context.getApplicationContext()).getLockPosition();
        ArrayList<String> position = ((App)context.getApplicationContext()).getPosition();
        linearlayout = ((App)context.getApplicationContext()).getFloatlinearlayout();
        FloatLinearLayout fll = linearlayout.get(index);
        if (fll.getPositionLocked())
        {
            fll.setPositionLocked(false);
            lock.set(index, false);
            lock_button.setBackgroundResource(R.drawable.ic_lock_unlocked);
            Toast.makeText(context, R.string.text_unlock, Toast.LENGTH_SHORT).show();
        }
        else
        {
            fll.setPositionLocked(true);
            lock.set(index, true);
            position.set(index, fll.getPosition());
            lock_button.setBackgroundResource(R.drawable.ic_lock);
            Toast.makeText(context, R.string.text_lock, Toast.LENGTH_SHORT).show();
        }
        FloatData dat = new FloatData();
        dat.savedata(context);
    }

    private boolean FloatWinDelete (int index)
    {
        App utils = ((App)context.getApplicationContext());
        WindowManager wm = utils.getFloatwinmanager();
        floatdata = utils.getFloatView();
        floatlayout = utils.getFloatLayout();
        linearlayout = utils.getFloatlinearlayout();
        if (utils.getShowFloat().get(index))
        {
            wm.removeView(linearlayout.get(index));
        }
        if (floatdata.size() < index + 1 || floatlayout.size() != linearlayout.size())
        {
            return false;
        }
        else
        {
            floatdata.remove(index);
            floatlayout.remove(index);
            linearlayout.remove(index);
            utils.setFloatLayout(floatlayout);
            utils.setFloatView(floatdata);
            utils.setFloatlinearlayout(linearlayout);
            utils.removeDatas(index);
            FloatData dat = new FloatData();
            dat.savedata(context);
            textshow.remove(index);
            notifyDataSetChanged();
            return true;
        }
    }

    private void restoredata ()
    {
        FloatData dat = new FloatData();
        dat.getSaveArrayData(context);
		textshow.clear();
		textshow.addAll(((App)context.getApplicationContext()).getFloatText());
        notifyDataSetChanged();
    }

    private void FloatWinEdit (Activity act, int i)
    {
        Intent intent = new Intent(context, FloatTextSetting.class);
        intent.putExtra("EditMode", true);
        intent.putExtra("EditID", i);
        act.startActivityForResult(intent, FLOAT_RESULT_CODE);
    }

}
