package tool.xfy9326.floattext.Receiver;

import android.content.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;
import tool.xfy9326.floattext.View.*;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;
import java.util.ArrayList;
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
		ArrayList<String> Text = textutils.getTextShow();
		ArrayList<Float> Size = textutils.getSizeShow();
		ArrayList<Integer> Color = textutils.getColorShow();
		ArrayList<Boolean> Thick = textutils.getThickShow();
		ArrayList<Boolean> Show = textutils.getShowFloat();
		ArrayList<String> Position = textutils.getPosition();
		ArrayList<Boolean> Lock = textutils.getLockPosition();
		ArrayList<Boolean> Top = textutils.getTextTop();
		ArrayList<Boolean> AutoTop = textutils.getAutoTop();
		ArrayList<Boolean> Move = textutils.getTextMove();
		ArrayList<Integer> Speed = textutils.getTextSpeed();
		ArrayList<Boolean> Shadow = textutils.getTextShadow();
		ArrayList<Float> ShadowX = textutils.getTextShadowX();
		ArrayList<Float> ShadowY = textutils.getTextShadowY();
		ArrayList<Float> ShadowRadius = textutils.getTextShadowRadius();
		ArrayList<Integer> BackgroundColor = textutils.getBackgroundColor();
		ArrayList<Integer> ShadowColor = textutils.getTextShadowColor();
		ArrayList<Boolean> FloatSize = textutils.getFloatSize();
		ArrayList<Float> FloatLong = textutils.getFloatLong();
		ArrayList<Float> FloatWide = textutils.getFloatWide();
		if (Text.size() != 0 && Size.size() != 0 && Color.size() != 0 && Thick.size() != 0)
		{
			WindowManager wm = utils.getFloatwinmanager();
			for (int i = 0;i < Text.size();i++)
			{
				FloatTextView fv = FloatTextSettingMethod.CreateFloatView(ctx, Text.get(i), Size.get(i), Color.get(i), Thick.get(i), Speed.get(i), i, Shadow.get(i), ShadowX.get(i), ShadowY.get(i), ShadowRadius.get(i), ShadowColor.get(i));
				FloatLinearLayout fll = FloatTextSettingMethod.CreateLayout(ctx, i);
				fll.changeShowState(Show.get(i));
				String[] ptemp = new String[]{"100", "150"};
				if (Lock.get(i))
				{
					ptemp = Position.get(i).toString().split("_");
					fll.setAddPosition(Float.parseFloat(ptemp[0]), Float.parseFloat(ptemp[1]));
				}
				WindowManager.LayoutParams layout = FloatTextSettingMethod.CreateFloatLayout(ctx, wm, fv, fll, Show.get(i), Float.parseFloat(ptemp[0]), Float.parseFloat(ptemp[1]), Top.get(i), Move.get(i), BackgroundColor.get(i), FloatSize.get(i), FloatLong.get(i), FloatWide.get(i));
				fll.setFloatLayoutParams(layout);
				fll.setPositionLocked(Lock.get(i));
				fll.setTop(AutoTop.get(i));
				if (utils.getMovingMethod())
				{
					fv.setMoving(Move.get(i), 0);
				}
				else
				{
					fv.setMoving(Move.get(i), 1);
					if (Move.get(i))
					{
						fll.setShowState(false);
						fll.setShowState(true);
					}
				}
				FloatTextSettingMethod.savedata(ctx, fv, fll, Text.get(i), layout);
			}
		}
	}

}
