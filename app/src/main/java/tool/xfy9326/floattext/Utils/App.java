package tool.xfy9326.floattext.Utils;

import android.app.Application;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.view.WindowManager;
import android.widget.RemoteViews;

import java.util.ArrayList;

import tool.xfy9326.floattext.CrashReport.CrashHandler;
import tool.xfy9326.floattext.SafeGuard;
import tool.xfy9326.floattext.View.ListViewAdapter;

/*
 数据缓存
 */

public class App extends Application {
    public boolean FloatWinReshow = true;
    public boolean DynamicNumService = false;
    public boolean HtmlMode = false;
    public boolean ListTextHide = false;
    public boolean GetSave = false;
    public boolean TextFilter = false;
    public boolean StartShowWin = false;
    private boolean MovingMethod = false;
    private boolean StayAliveService = true;
    private boolean DevelopMode = false;
    private FloatTextUtils textutil;
    private FloatFrameUtils frameutil;
    private ListViewAdapter listviewadapter = null;
    private WindowManager floatwinmanager = null;
    private NotificationCompat.Builder notification = null;
    private RemoteViews remoteview = null;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashCatch();
        init();
    }

    private void CrashCatch() {
        //错误报告
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this, "FloatText", "tool.xfy9326.floattext.FloatManage", "1069665464@qq.com");
        crashHandler.setOutPutError(false, Environment.getExternalStorageDirectory().getAbsolutePath() + "/FloatText/CrashLog/");
    }

    private void init() {
        textutil = new FloatTextUtils();
        frameutil = new FloatFrameUtils();
        SafeGuard.isSignatureAvailable(this, true);
        SafeGuard.isPackageNameAvailable(this, true);
    }

    public RemoteViews getRemoteview() {
        return remoteview;
    }

    public void setRemoteview(RemoteViews remoteview) {
        this.remoteview = remoteview;
    }

    public NotificationCompat.Builder getNotification() {
        return notification;
    }

    public void setNotification(NotificationCompat.Builder notification) {
        this.notification = notification;
    }

    public void setStartShowWin(boolean startShowWin) {
        StartShowWin = startShowWin;
    }

    public void setTextFilter(boolean textFilter) {
        TextFilter = textFilter;
    }

    public void setGetSave(boolean b) {
        this.GetSave = b;
    }

    public ListViewAdapter getListviewadapter() {
        return listviewadapter;
    }

    public void setListviewadapter(ListViewAdapter listviewadapter) {
        this.listviewadapter = listviewadapter;
    }

    public WindowManager getFloatwinmanager() {
        return floatwinmanager;
    }

    public void setFloatwinmanager(WindowManager floatwinmanager) {
        this.floatwinmanager = floatwinmanager;
    }

    public void setListTextHide(boolean listTextHide) {
        ListTextHide = listTextHide;
    }

    public boolean getHtmlMode() {
        return HtmlMode;
    }

    public void setHtmlMode(boolean htmlMode) {
        HtmlMode = htmlMode;
    }

    public boolean getDevelopMode() {
        return DevelopMode;
    }

    public void setDevelopMode(boolean developMode) {
        DevelopMode = developMode;
    }

    public boolean getDynamicNumService() {
        return DynamicNumService;
    }

    public void setDynamicNumService(boolean DynamicNumService) {
        this.DynamicNumService = DynamicNumService;
    }

    public boolean getStayAliveService() {
        return StayAliveService;
    }

    public void setStayAliveService(boolean stayAliveService) {
        StayAliveService = stayAliveService;
    }

    public boolean getMovingMethod() {
        return MovingMethod;
    }

    public void setMovingMethod(boolean movingMethod) {
        MovingMethod = movingMethod;
    }

    public void setFloatReshow(boolean bool) {
        FloatWinReshow = bool;
    }

    public ArrayList<String> getFloatText() {
        return frameutil.getFloattext();
    }

    public void setFloatText(ArrayList<String> floattext) {
        frameutil.setFloattext(floattext);
    }

    public ArrayList<String> getTextData() {
        return textutil.getTextShow();
    }

    public FloatTextUtils getTextutil() {
        return textutil;
    }

    public void setTextutil(FloatTextUtils textutil) {
        this.textutil = textutil;
    }

    public FloatFrameUtils getFrameutil() {
        return frameutil;
    }

    public void setFrameutil(FloatFrameUtils frameutil) {
        this.frameutil = frameutil;
    }

}
