package tool.xfy9326.floattext.Service;

import android.app.*;
import android.content.*;
import android.os.*;
import android.service.notification.*;
import tool.xfy9326.floattext.Method.*;

public class FloatNotificationListenerService extends NotificationListenerService
{
	private String notify;

	@Override
	public void onCreate ()
	{
		super.onCreate();
	}

	@Override
	public void onNotificationPosted (StatusBarNotification sbn)
	{
		Bundle extras = sbn.getNotification().extras;
		String title = extras.getString(Notification.EXTRA_TITLE);
		String text = extras.getCharSequence(Notification.EXTRA_TEXT).toString();
		notify = title  + ":" + text;
		sendmes();
		super.onNotificationPosted(sbn);
	}

	private void sendmes ()
	{
		Intent intent = new Intent();
		intent.setAction(FloatServiceMethod.TEXT_ADVANCE_UPDATE_ACTION);
		intent.putExtra("NotifyMes", notify);
		sendBroadcast(intent);
	}

	@Override
	public void onDestroy ()
	{
		super.onDestroy();
	}
}
