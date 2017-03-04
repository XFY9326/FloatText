package tool.xfy9326.floattext.Service;

import android.service.notification.*;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import tool.xfy9326.floattext.Utils.StaticString;

public class FloatNotificationListenerService extends NotificationListenerService
{
	private String notify;
	private String pkg;

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn)
	{
		Bundle extras = sbn.getNotification().extras;
		String title = extras.getString(Notification.EXTRA_TITLE);
		String text = extras.getString(Notification.EXTRA_TEXT);
		String sum_text = extras.getString(Notification.EXTRA_SUMMARY_TEXT);
		String sub_text = extras.getString(Notification.EXTRA_SUB_TEXT);
		if (title != null)
		{
			notify = title  + (text == null ? "" : ":" + text) + (sum_text == null ? "" : " " + sum_text) + (sub_text == null ? "" : " " + sub_text);
			pkg = sbn.getPackageName().toString();
			sendmes();
		}
		super.onNotificationPosted(sbn);
	}

	private void sendmes()
	{
		Intent intent = new Intent();
		intent.setAction(StaticString.TEXT_ADVANCE_UPDATE_ACTION);
		intent.putExtra("NotifyMes", notify);
		intent.putExtra("NotifyPkg", pkg);
		sendBroadcast(intent);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
