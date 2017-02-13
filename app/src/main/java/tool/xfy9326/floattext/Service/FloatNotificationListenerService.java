package tool.xfy9326.floattext.Service;

import android.service.notification.*;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import tool.xfy9326.floattext.Utils.StaticString;

public class FloatNotificationListenerService extends NotificationListenerService
{
	private String notify;

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
		CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
		if (text != null)
		{
			notify = title  + ":" + text.toString();
			sendmes();
		}
		super.onNotificationPosted(sbn);
	}

	private void sendmes()
	{
		Intent intent = new Intent();
		intent.setAction(StaticString.TEXT_ADVANCE_UPDATE_ACTION);
		intent.putExtra("NotifyMes", notify);
		sendBroadcast(intent);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
