package tool.xfy9326.floattext.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Objects;

import tool.xfy9326.floattext.Method.DynamicWordUpdateMethod;
import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.Utils.StaticString;

public class FloatTextUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context p1, Intent p2) {
        String action = p2.getAction();
        if (Objects.equals(action, StaticString.TEXT_UPDATE_ACTION)) {
            DynamicWordUpdateMethod updater = new DynamicWordUpdateMethod(p1, false);
            updater.UpdateText(p2);
        } else if (Objects.equals(action, StaticString.ACTIVITY_CHANGE_ACTION)) {
            ActivityChange(p1, p2);
        } else if (Objects.equals(action, StaticString.DYNAMIC_WORD_ADDON_ACTION)) {
            DynamicWordUpdateMethod updater = new DynamicWordUpdateMethod(p1, true);
            updater.UpdateText(p2);
        }
    }

    //应用显示过滤
    private void ActivityChange(Context p1, Intent p2) {
        String actname = p2.getStringExtra("CurrentActivity");
        if (actname.substring(0, 7).equalsIgnoreCase("android") || actname.contains(p1.getPackageName())) {
            return;
        }
        App utils = ((App) p1.getApplicationContext());
        if (utils.StartShowWin) {
            ArrayList<String> fa = utils.getFrameutil().getFilterApplication();
            boolean findact = false;
            for (int a = 0; a < fa.size(); a++) {
                if (actname.contains(fa.get(a))) {
                    findact = true;
                    break;
                }
            }
            if (PreferenceManager.getDefaultSharedPreferences(p1).getBoolean("WinFilterMode", false)) {
                findact = !findact;
            }
            if (findact) {
                FloatManageMethod.ShoworHideAllWin(p1, false, true);
            } else {
                FloatManageMethod.ShoworHideAllWin(p1, true, true);
            }
        }
    }

}
