package tool.xfy9326.floattext.Service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.Utils.StaticString;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class FloatNotificationListenerService extends NotificationListenerService {
    private String notify;
    private String pkg;
    private App utils;

    @Override
    public void onCreate() {
        utils = ((App) getApplicationContext());
        super.onCreate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (utils.StartShowWin) {
            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString(Notification.EXTRA_TITLE);
            String text = extras.getString(Notification.EXTRA_TEXT);
            String sum_text = extras.getString(Notification.EXTRA_SUMMARY_TEXT);
            String sub_text = extras.getString(Notification.EXTRA_SUB_TEXT);
            if (title != null) {
                notify = title + (text == null ? "" : ":" + text) + (sum_text == null ? "" : " " + sum_text) + (sub_text == null ? "" : " " + sub_text);
                pkg = sbn.getPackageName();
                sendmes();
            }
        }
        super.onNotificationPosted(sbn);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    private void sendmes() {
        Intent intent = new Intent();
        intent.setAction(StaticString.TEXT_ADVANCE_UPDATE_ACTION);
        intent.putExtra("NotifyMes", notify);
        intent.putExtra("NotifyPkg", pkg);
        sendBroadcast(intent);
    }

}
