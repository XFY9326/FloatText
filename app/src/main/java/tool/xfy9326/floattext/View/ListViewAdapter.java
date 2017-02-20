package tool.xfy9326.floattext.View;

import android.app.*;
import android.content.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;

import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.StrikethroughSpan;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import tool.xfy9326.floattext.Setting.FloatTextSetting;

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
        ArrayList<Boolean> Show = utils.getTextutil().getShowFloat();
        if (Show.size() != textshow.size())
		{
            FloatManageMethod.restartApplication(context, context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()));
        }
        String listtext = textshow.get(index);
		//保持显示的文字和悬浮窗内样式一致
        TextReshow(index, utils, view, Show.get(index), listtext);
		//单行显示
        view.textView.setSingleLine(utils.ListTextHide);
		//锁定图标设置
		LockViewSet(index, view);
		//点击文字隐藏和显示悬浮窗
        view.textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					FloatManageMethod.ShoworHideWin(context, index);
				}
			});

		//锁定监听设置
        view.lock_button.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					if (FloatManageMethod.LockorUnlockWin(context, index))
					{
						FloatManage.snackshow((Activity) context, context.getString(R.string.text_lock));
					}
					else
					{
						FloatManage.snackshow((Activity) context, context.getString(R.string.text_unlock));
					}
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
		FloatTextUtils textutils = utils.getTextutil();
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
        if (textutils.getTextShadow().get(index))
		{
            view.textView.setShadowLayer(textutils.getTextShadowRadius().get(index), textutils.getTextShadowX().get(index), textutils.getTextShadowY().get(index), textutils.getTextShadowColor().get(index));
        }
		else
		{
			view.textView.setShadowLayer(0, 0, 0, 0);
		}
        view.textView.getPaint().setFakeBoldText(utils.getTextutil().getThickShow().get(index));
        view.textView.setTextColor(utils.getTextutil().getColorShow().get(index));
        view.textView.setEllipsize(TextUtils.TruncateAt.END);
	}

	private void LockViewSet(int index, ViewHolder view)
	{
		ArrayList<Boolean> lock = ((App) context.getApplicationContext()).getTextutil().getLockPosition();
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
			floatdata = utils.getFrameutil().getFloatview();
			floatlayout = utils.getFrameutil().getFloatlayout();
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
		FloatManageMethod.UpdateNotificationCount(context);
    }

	//删除悬浮窗
    private boolean FloatWinDelete(final int index)
	{
        App utils = ((App) context.getApplicationContext());
		FloatTextUtils textutils = utils.getTextutil();
        WindowManager wm = utils.getFloatwinmanager();
        floatdata = utils.getFrameutil().getFloatview();
        floatlayout = utils.getFrameutil().getFloatlayout();
        linearlayout = utils.getFrameutil().getFloatlinearlayout();
        FloatShow = textutils.getShowFloat();
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
			textutils.removeDatas(index);
			utils.setTextutil(textutils);
			FloatIDChange(index);
            FloatData dat = new FloatData(context);
            dat.savedata();
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount());
            return true;
        }
    }
	
	//改变悬浮窗内置序号(删除后调用)
	private void FloatIDChange(int delid)
	{
		App utils = (App) context.getApplicationContext();
		ArrayList<FloatLinearLayout> fll = utils.getFrameutil().getFloatlinearlayout();
		if (delid < fll.size())
		{
			for (int i = delid; i < fll.size(); i++)
			{
				int id = fll.get(i).getFloatID();
				fll.get(i).setFloatID(id - 1);
			}
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
