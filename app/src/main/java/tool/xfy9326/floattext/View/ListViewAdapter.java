package tool.xfy9326.floattext.View;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import tool.xfy9326.floattext.FloatManage;
import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.Method.FloatServiceMethod;
import tool.xfy9326.floattext.Method.FloatTextSettingMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Setting.FloatTextSetting;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.Utils.FloatData;
import tool.xfy9326.floattext.Utils.FloatTextUtils;

/*
 管理列表操作
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private ArrayList<String> textshow;
    private ArrayList<FloatTextView> floatdata;
    private ArrayList<WindowManager.LayoutParams> floatlayout;
    private Typeface typeface = null;

    public ListViewAdapter(Activity activity, ArrayList<String> textshow) {
        this.context = activity;
        this.activity = activity;
        this.textshow = textshow;
        String ttf = FloatTextSettingMethod.typeface_fix(context);
        if (ttf.equalsIgnoreCase("Default")) {
            typeface = null;
        } else {
            typeface = Typeface.createFromFile(ttf);
        }
    }

    //设置显示状态的数据
    public void setTextShow(ArrayList<String> ts) {
        this.textshow = ts;
    }

    @Override
    public int getItemCount() {
        return textshow.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.floatmanage_listview, p1, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder view, int p2) {
        final int index = view.getLayoutPosition();
        final App utils = ((App) context.getApplicationContext());
        ArrayList<Boolean> Show = utils.getTextutil().getShowFloat();
        if (Show.size() != textshow.size()) {
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
            public void onClick(View v) {
                FloatManageMethod.ShoworHideWin(context, index);
            }
        });

        //锁定监听设置
        view.lock_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (FloatManageMethod.LockorUnlockWin(context, index)) {
                    FloatManage.snackshow((Activity) context, context.getString(R.string.text_lock));
                } else {
                    FloatManage.snackshow((Activity) context, context.getString(R.string.text_unlock));
                }
            }
        });
        //删除按钮监听
        view.del_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DelViewSet(index);
            }
        });
        //编辑按钮监听
        view.edit_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                EditViewSet(index);
            }
        });
    }

    private void TextReshow(int index, App utils, ViewHolder view, boolean show, String listtext) {
        FloatTextUtils textutils = utils.getTextutil();
        if (!show) {
            SpannableString str = new SpannableString(listtext);
            str.setSpan(new StrikethroughSpan(), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.textView.setText(str);
        } else {
            view.textView.setText(listtext);
        }
        if (typeface != null) {
            view.textView.setTypeface(typeface);
        }
        if (textutils.getTextShadow().get(index)) {
            view.textView.setShadowLayer(textutils.getTextShadowRadius().get(index), textutils.getTextShadowX().get(index), textutils.getTextShadowY().get(index), textutils.getTextShadowColor().get(index));
        } else {
            view.textView.setShadowLayer(0, 0, 0, 0);
        }
        view.textView.getPaint().setFakeBoldText(utils.getTextutil().getThickShow().get(index));
        view.textView.setTextColor(utils.getTextutil().getColorShow().get(index));
        view.textView.setEllipsize(TextUtils.TruncateAt.END);
    }

    private void LockViewSet(int index, ViewHolder view) {
        ArrayList<Boolean> lock = ((App) context.getApplicationContext()).getTextutil().getLockPosition();
        if (lock.get(index)) {
            view.lock_button.setBackgroundResource(R.drawable.ic_lock);
        } else {
            view.lock_button.setBackgroundResource(R.drawable.ic_lock_unlocked);
        }
    }

    private void EditViewSet(int index) {
        App utils = ((App) context.getApplicationContext());
        floatdata = utils.getFrameutil().getFloatview();
        floatlayout = utils.getFrameutil().getFloatlayout();
        FloatWinEdit(activity, index);
        notifyItemChanged(index);
    }

    private void DelViewSet(final int index) {
        SharedPreferences spdata = PreferenceManager.getDefaultSharedPreferences(context);
        boolean show = spdata.getBoolean("FloatDeleteAlert", false);
        if (show) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.ask_for_delete)
                    .setMessage(R.string.ask_for_delete_alert)
                    .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface p1, int p2) {
                            delwin(index);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null);
            dialog.show();
        } else {
            delwin(index);
        }
    }

    //控制删除悬浮窗的显示
    private void delwin(int index) {
        if (FloatWinDelete(index)) {
            FloatManage.snackshow((Activity) context, context.getString(R.string.delete_text_ok));
        } else {
            FloatManage.snackshow((Activity) context, context.getString(R.string.float_list_del_err));
        }
        FloatManageMethod.UpdateNotificationCount(context);
    }

    //删除悬浮窗
    private boolean FloatWinDelete(final int index) {
        App utils = ((App) context.getApplicationContext());
        FloatTextUtils textutils = utils.getTextutil();
        WindowManager wm = utils.getFloatwinmanager();
        floatdata = utils.getFrameutil().getFloatview();
        floatlayout = utils.getFrameutil().getFloatlayout();
        ArrayList<FloatLinearLayout> linearlayout = utils.getFrameutil().getFloatlinearlayout();
        ArrayList<Boolean> floatShow = textutils.getShowFloat();
        if (floatdata.size() - 1 < index || floatlayout.size() != linearlayout.size()) {
            return false;
        } else {
            if (floatShow.get(index)) {
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
            FloatServiceMethod.ReloadDynamicUse(context);
            return true;
        }
    }

    //改变悬浮窗内置序号(删除后调用)
    private void FloatIDChange(int delid) {
        App utils = (App) context.getApplicationContext();
        ArrayList<FloatLinearLayout> fll = utils.getFrameutil().getFloatlinearlayout();
        if (delid < fll.size()) {
            for (int i = delid; i < fll.size(); i++) {
                int id = fll.get(i).getFloatID();
                fll.get(i).setFloatID(id - 1);
            }
        }
    }

    private void FloatWinEdit(Activity act, int i) {
        Intent intent = new Intent(context, FloatTextSetting.class);
        intent.putExtra("EditMode", true);
        intent.putExtra("EditID", i);
        int FLOAT_RESULT_CODE = 0;
        act.startActivityForResult(intent, FLOAT_RESULT_CODE);
    }

    /*
     进入编辑窗口前需要保存数据
     */

    //控件获取
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public final Button del_button;
        public final Button edit_button;
        public final Button lock_button;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textview_titleshow_floatmanage);
            del_button = view.findViewById(R.id.button_delete_floatmanage);
            edit_button = view.findViewById(R.id.button_edit_floatmanage);
            lock_button = view.findViewById(R.id.button_lock_floatmanage);
        }
    }

}
