package tool.xfy9326.floattext.Method;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Tool.FormatArrayList;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.Utils.FloatData;
import tool.xfy9326.floattext.Utils.FloatTextUtils;

//迅速启动所有悬浮窗与应用 无须Avtivity

public class QuickStartMethod {
    public static boolean Launch(Context ctx) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(ctx) && ctx.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                FloatStart(ctx);
                return true;
            } else {
                Toast.makeText(ctx, ctx.getString(R.string.app_name) + ctx.getString(R.string.premission_error), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            FloatStart(ctx);
            return true;
        }
    }

    private static void FloatStart(Context ctx) {
        App utils = (App) ctx.getApplicationContext();
        SharedPreferences spdata = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences setdata = ctx.getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
        FloatData dat = new FloatData(ctx);
        dat.getSaveArrayData();
        utils.setMovingMethod(spdata.getBoolean("TextMovingMethod", false));
        utils.setStayAliveService(spdata.getBoolean("StayAliveService", false));
        utils.setDynamicNumService(spdata.getBoolean("DynamicNumService", false));
        utils.setDevelopMode(spdata.getBoolean("DevelopMode", false));
        utils.setHtmlMode(spdata.getBoolean("HtmlMode", true));
        utils.setListTextHide(spdata.getBoolean("ListTextHide", false));
        utils.getFrameutil().setFilterApplication(FormatArrayList.StringToStringArrayList(setdata.getString("Filter_Application", "[]")));
        FloatManageMethod.startservice(ctx);
        FloatManageMethod.preparefolder();
        FloatManageMethod.setWinManager(ctx);
        FloatTextUtils textutils = utils.getTextutil();
        FloatManageMethod.Reshow_Create(ctx, utils, textutils, textutils.getTextShow(), textutils.getShowFloat(), textutils.getLockPosition(), textutils.getPosition(), textutils.getTextMove());
        utils.setGetSave(true);
        utils.setStartShowWin(true);
        FloatServiceMethod.ReloadDynamicUse(ctx);
    }
}
