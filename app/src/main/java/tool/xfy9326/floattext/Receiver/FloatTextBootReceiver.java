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

public class FloatTextBootReceiver extends BroadcastReceiver
{
	private SharedPreferences spdata;

	@Override
	public void onReceive(Context ctx, Intent p2)
	{
		String action = p2.getAction();
		if (Intent.ACTION_BOOT_COMPLETED.equals(action))
		{
			spdata = PreferenceManager.getDefaultSharedPreferences(ctx);
			if (spdata.getBoolean("FloatAutoBoot", false))
			{
				if (Build.VERSION.SDK_INT >= 23)
				{
					if (Settings.canDrawOverlays(ctx) && ctx.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
					{
						FloatStart(ctx);
					}
					else
					{
						Toast.makeText(ctx, ctx.getString(R.string.app_name) + ctx.getString(R.string.permission_error), Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					FloatStart(ctx);
				}
			}
		}
	}

	private void FloatStart(final Context ctx)
	{
		final App utils = (App)ctx.getApplicationContext();
		final SharedPreferences setdata = ctx.getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
		FloatData dat = new FloatData(ctx);
		dat.getSaveArrayData();
		utils.setMovingMethod(spdata.getBoolean("TextMovingMethod", false));
		utils.setStayAliveService(spdata.getBoolean("StayAliveService", false));
		utils.setDynamicNumService(spdata.getBoolean("DynamicNumService", false));
		utils.setDevelopMode(spdata.getBoolean("DevelopMode", false));
		utils.setHtmlMode(spdata.getBoolean("HtmlMode", true));
		utils.setListTextHide(spdata.getBoolean("ListTextHide", false));
		utils.getFrameutil().setFilterApplication(FormatArrayList.StringToStringArrayList(setdata.getString("Filter_Application", "[]")));
		if (((App)ctx.getApplicationContext()).getTextData().size() > 0)
		{
			FloatManageMethod.startservice(ctx);
			FloatManageMethod.preparefolder();
			FloatManageMethod.setWinManager(ctx);
			Reshow(ctx);
			utils.setGetSave(true);
		}
	}

	private void Reshow(Context ctx)
	{
		App utils = ((App)ctx.getApplicationContext());
		FloatTextUtils textutils = utils.getTextutil();
		FloatManageMethod.Reshow_Create(ctx, utils, textutils, textutils.getTextShow(), textutils.getShowFloat(), textutils.getLockPosition(), textutils.getPosition(), textutils.getTextMove());
	}

}
