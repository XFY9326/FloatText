package tool.xfy9326.floattext.Receiver;

import android.content.*;
import tool.xfy9326.floattext.Utils.*;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;
import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Tool.FormatArrayList;
import tool.xfy9326.floattext.Method.QuickStartMethod;

public class FloatTextBootReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context ctx, Intent p2)
	{
		//开机自启动
		String action = p2.getAction();
		SharedPreferences spdata = PreferenceManager.getDefaultSharedPreferences(ctx);
		if (Intent.ACTION_BOOT_COMPLETED.equals(action))
		{
			if (spdata.getBoolean("FloatAutoBoot", false))
			{
				QuickStartMethod.Launch(ctx);
			}
		}
	}

}
