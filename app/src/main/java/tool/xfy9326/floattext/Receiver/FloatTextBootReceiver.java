package tool.xfy9326.floattext.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import tool.xfy9326.floattext.Method.QuickStartMethod;

public class FloatTextBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent p2) {
		//开机自启动
		String action = p2.getAction();
		SharedPreferences spdata = PreferenceManager.getDefaultSharedPreferences(ctx);
		if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
			if (spdata.getBoolean("FloatAutoBoot", false)) {
				QuickStartMethod.Launch(ctx);
			}
		}
	}

}
