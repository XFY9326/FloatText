package tool.xfy9326.floattext.View;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.preference.*;
import android.support.v7.widget.*;
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

/*
管理列表操作
*/

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder>
{
    private static int FLOAT_RESULT_CODE = 0;
    private Context context;
    private Activity activity;
    private ArrayList<String> textshow;
    private ArrayList<FloatTextView> floatdata;
    private ArrayList<WindowManager.LayoutParams> floatlayout;
    private ArrayList<FloatLinearLayout> linearlayout;
    private ArrayList<Boolean> FloatShow;
    private SharedPreferences sp = null;
    private Typeface typeface = null;

    public ListViewAdapter(Activity activity, ArrayList<String> textshow)
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

	//设置显示状态的数据
    public void setTextShow(ArrayList<String> ts)
	{
        this.textshow = ts;
    }

    @Override
    public int getItemCount()
	{
        return textshow.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.floatmanage_listview, p1, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder view, int p2)
	{
        final int index = view.getLayoutPosition();
        final App utils = ((App) context.getApplicationContext());
        ArrayList<Boolean> Show = utils.getShowFloat();
        if (Show.size() != textshow.size())
		{
            FloatManageMethod.restartApplication(context, context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()));
        }
        String listtext = textshow.get(index);
		//保持显示的文字和悬浮窗内样式一致
        TextReshow(index, utils, view, Show.get(index), listtext);
		//单行显示
        view.textView.setSingleLine(utils.getListTextHide());
		//锁定图标设置
		LockViewSet(index, view);
		//点击文字隐藏和显示悬浮窗
        view.textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					TextViewSet(index, utils, view);
				}
			});

		//锁定监听设置
        view.lock_button.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					FloatWinLock(context, index, view.lock_button);
					System.out.println("ListViewAdapter: " + context);
				}
			});
		//删除按钮监听
        view.del_button.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					DelViewSet(index);
				}
			});
		//编辑按钮监听
        view.edit_button.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					EditViewSet(index);
				}
			});
    }
	
	private void TextReshow(int index, App utils, ViewHolder view, boolean show, String listtext)
	{
		if (!show)
		{
            SpannableString str = new SpannableString(listtext);
            str.setSpan(new StrikethroughSpan(), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.textView.setText(str);
        }
		else
		{
            view.textView.setText(listtext);
        }
        if (typeface != null)
		{
            view.textView.setTypeface(typeface);
        }
        if (utils.getTextShadow().get(index))
		{
            view.textView.setShadowLayer(utils.getTextShadowRadius().get(index), utils.getTextShadowX().get(index), utils.getTextShadowY().get(index), utils.getTextShadowColor().get(index));
        }
		else
		{
			view.textView.setShadowLayer(0, 0, 0, 0);
		}
        view.textView.getPaint().setFakeBoldText(utils.getThickData().get(index));
        view.textView.setTextColor(utils.getColorData().get(index));
        view.textView.setEllipsize(TextUtils.TruncateAt.END);
	}
	
	private void LockViewSet(int index, ViewHolder view)
	{
		ArrayList<Boolean> lock = ((App) context.getApplicationContext()).getLockPosition();
        if (lock.get(index))
		{
            view.lock_button.setBackgroundResource(R.drawable.ic_lock);
        }
		else
		{
            view.lock_button.setBackgroundResource(R.drawable.ic_lock_unlocked);
        }
	}
	
	private void EditViewSet(int index)
	{
		if (!sp.getBoolean("WinOnlyShowInHome", false))
		{
			App utils = ((App) context.getApplicationContext());
			floatdata = utils.getFloatView();
			floatlayout = utils.getFloatLayout();
			FloatWinEdit(activity, index);
			notifyItemChanged(index);
		}
		else
		{
			FloatManage.snackshow((Activity) context, context.getString(R.string.float_can_not_edit));
		}
	}
	
	private void DelViewSet(final int index)
	{
		SharedPreferences spdata = PreferenceManager.getDefaultSharedPreferences(context);
		boolean show = spdata.getBoolean("FloatDeleteAlert", false);
		if (show)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(context)
				.setTitle(R.string.ask_for_delete)
				.setMessage(R.string.ask_for_delete_alert)
				.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface p1, int p2)
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
	
	private void TextViewSet(int index, App utils, ViewHolder view)
	{
		ArrayList<Boolean> showFloat = ((App) context.getApplicationContext()).getShowFloat();
		ArrayList<String> TextShow = ((App) context.getApplicationContext()).getTextData();
		boolean iShowFloat = showFloat.get(index);
		String iTextShow = TextShow.get(index);

		iShowFloat = !showFloat.set(index, !iShowFloat);
		FloatData dat = new FloatData(context);
		dat.savedata();

		FloatLinearLayout floatLinearLayout = utils.getFloatlinearlayout().get(index);
		floatLinearLayout.setShowState(iShowFloat);

		WindowManager.LayoutParams wmParams = utils.getFloatLayout().get(index);
		WindowManager wm = utils.getFloatwinmanager();

		if (!iShowFloat)
		{
			SpannableString str = new SpannableString(iTextShow);
			str.setSpan(new StrikethroughSpan(), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			view.textView.setText(str);
		}
		else
		{
			view.textView.setText(iTextShow);
		}

		wm.updateViewLayout(floatLinearLayout, wmParams);
	}

	//控件获取
    public static class ViewHolder extends RecyclerView.ViewHolder
	{
        public TextView textView;
        public Button del_button;
        public Button edit_button;
        public Button lock_button;

        public ViewHolder(View view)
		{
            super(view);
            textView = (TextView) view.findViewById(R.id.textview_titleshow_floatmanage);
            del_button = (Button) view.findViewById(R.id.button_delete_floatmanage);
            edit_button = (Button) view.findViewById(R.id.button_edit_floatmanage);
            lock_button = (Button) view.findViewById(R.id.button_lock_floatmanage);
        }
    }

	//控制删除悬浮窗的显示
    private void delwin(int index)
	{
        if (FloatWinDelete(index))
		{
            FloatManage.snackshow((Activity) context, context.getString(R.string.delete_text_ok));
        }
		else
		{
            FloatManage.snackshow((Activity) context, context.getString(R.string.float_list_del_err));
        }
    }

	//锁定悬浮窗
    private void FloatWinLock(Context context, final int index, Button lock_button)
	{
		App utils = ((App) context.getApplicationContext());
        ArrayList<Boolean> lock = utils.getLockPosition();
        ArrayList<String> position = utils.getPosition();
        linearlayout = utils.getFloatlinearlayout();
        FloatLinearLayout fll = linearlayout.get(index);
        if (fll.getPositionLocked())
		{
            fll.setPositionLocked(false);
            lock.set(index, false);
            lock_button.setBackgroundResource(R.drawable.ic_lock_unlocked);
            FloatManage.snackshow((Activity) context, context.getString(R.string.text_unlock));
        }
		else
		{
            fll.setPositionLocked(true);
            lock.set(index, true);
            position.set(index, fll.getPosition());
            lock_button.setBackgroundResource(R.drawable.ic_lock);
            FloatManage.snackshow((Activity) context, context.getString(R.string.text_lock));
        }
        FloatData dat = new FloatData(context);
        dat.savedata();
    }

	//删除悬浮窗
    private boolean FloatWinDelete(final int index)
	{
        App utils = ((App) context.getApplicationContext());
        WindowManager wm = utils.getFloatwinmanager();
        floatdata = utils.getFloatView();
        floatlayout = utils.getFloatLayout();
        linearlayout = utils.getFloatlinearlayout();
        FloatShow = utils.getShowFloat();
        if ((int) floatdata.size() - 1 < index || floatlayout.size() != linearlayout.size())
		{
            return false;
        }
		else
		{
            if (FloatShow.get(index))
			{
                wm.removeView(linearlayout.get(index));
            }
            floatdata.remove(index);
            floatlayout.remove(index);
            linearlayout.remove(index);
            textshow.remove(index);
            FloatTextUtils textutils = utils.getTextutil();
			textutils.removeDatas(index);
			utils.setTextutil(textutils);
            FloatData dat = new FloatData(context);
            dat.savedata();
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount());
            return true;
        }
    }

    /*
	 进入编辑窗口前需要保存数据
     */
	
    private void FloatWinEdit(Activity act, int i)
	{
        Intent intent = new Intent(context, FloatTextSetting.class);
        intent.putExtra("EditMode", true);
        intent.putExtra("EditID", i);
        act.startActivityForResult(intent, FLOAT_RESULT_CODE);
    }

}
