package tool.xfy9326.floattext.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;
import java.lang.reflect.Method;
import tool.xfy9326.floattext.FloatManage;
import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.Utils.StaticNum;
import tool.xfy9326.floattext.Utils.StaticString;

public class FloatWindowStayAliveService extends Service {
	private ButtonBroadcastReceiver bbr = null;
	private RemoteViews contentview = null;
	private NotificationCompat.Builder notification;

    @Override
    public IBinder onBind(Intent p1) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        create_notification();
		FloatManageMethod.setWinManager(this);
    }

    private void create_notification() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		if (sp.getBoolean("FloatNotification", true)) {
			create_manage_notify(StaticNum.ONGONING_NOTIFICATION_ID);
		} else {
			create_run_notify(StaticNum.ONGONING_NOTIFICATION_ID);
		}

    }

	private void create_run_notify(int id) {
		Notification notify = new Notification();
		startForeground(id , notify);
	}

	private void create_manage_notify(int id) {
		bbr = new ButtonBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(StaticString.NOTIFICATION_BUTTON_ACTION);
		registerReceiver(bbr, filter);

		notification = new NotificationCompat.Builder(this);
		notification.setSmallIcon(R.mipmap.ic_notification);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		if (!sp.getBoolean("FloatNotificationIcon", true))
		{
			notification.setPriority(Notification.PRIORITY_MIN);
		}

		Intent intent = new Intent(this, FloatManage.class);
		PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setContentIntent(pintent);

		contentview = new RemoteViews(getPackageName(), R.layout.remotelayout_notification);

		contentview.setTextViewText(R.id.textview_notification_win_count, getString(R.string.notification_win_count, FloatManageMethod.getWinCount(this)));

		contentview.setImageViewResource(R.id.button_notification_show, R.drawable.ic_eye_off);
		Intent buttonintent_show = new Intent();
		buttonintent_show.setAction(StaticString.NOTIFICATION_BUTTON_ACTION);
		buttonintent_show.putExtra("BUTTON_ID", 0);
		PendingIntent pintent_show = PendingIntent.getBroadcast(this, 0, buttonintent_show, PendingIntent.FLAG_UPDATE_CURRENT);
		contentview.setOnClickPendingIntent(R.id.button_notification_show, pintent_show);

		contentview.setImageViewResource(R.id.button_notification_unlock, R.drawable.ic_notification_lock_outline);
		Intent buttonintent_unlock = new Intent();
		buttonintent_unlock.setAction(StaticString.NOTIFICATION_BUTTON_ACTION);
		buttonintent_unlock.putExtra("BUTTON_ID", 1);
		PendingIntent pintent_unlock = PendingIntent.getBroadcast(this, 1, buttonintent_unlock, PendingIntent.FLAG_UPDATE_CURRENT);
		contentview.setOnClickPendingIntent(R.id.button_notification_unlock, pintent_unlock);

		notification.setContent(contentview);
		((App)getApplicationContext()).setRemoteview(contentview);
		((App)getApplicationContext()).setNotification(notification);
        startForeground(id, notification.build());
	}

    @Override
    public void onDestroy() {
		if (bbr != null) {
			unregisterReceiver(bbr);
		}
        stopForeground(true);
        super.onDestroy();
    }

	private static void collapseStatusBar(Context context) {
		try {
			Object statusBarManager = context.getSystemService("statusbar");
			Method collapse;
			if (Build.VERSION.SDK_INT <= 16) {
				collapse = statusBarManager.getClass().getMethod("collapse");
			} else {
				collapse = statusBarManager.getClass().getMethod("collapsePanels");
			}
			collapse.invoke(statusBarManager);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	private class ButtonBroadcastReceiver extends BroadcastReceiver {
		private boolean WinLock = false;
		private boolean WinShow = true;

		@Override
		public void onReceive(Context p1, Intent p2) {
			if (contentview != null && p2.getAction().equals(StaticString.NOTIFICATION_BUTTON_ACTION)) {
				int buttonid = p2.getIntExtra("BUTTON_ID", -1);
				if (buttonid == 0) {
					FloatManageMethod.ShoworHideAllWin(p1, WinShow, true);
					WinShow = !WinShow;
					if (WinShow) {
						contentview.setImageViewResource(R.id.button_notification_show, R.drawable.ic_eye_off);
					} else {
						contentview.setImageViewResource(R.id.button_notification_show, R.drawable.ic_eye);
					}
				} else if (buttonid == 1) {
					FloatManageMethod.LockorUnlockAllWin(p1, WinLock, true);
					WinLock = !WinLock;
					if (WinLock) {
						contentview.setImageViewResource(R.id.button_notification_unlock, R.drawable.ic_notification_lock_unlocked_outline);
					} else {
						contentview.setImageViewResource(R.id.button_notification_unlock, R.drawable.ic_notification_lock_outline);
					}
				}
				NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				notification.setContent(contentview);
				nm.notify(StaticNum.ONGONING_NOTIFICATION_ID, notification.build());
				collapseStatusBar(p1);
			}
		}
	}

}
