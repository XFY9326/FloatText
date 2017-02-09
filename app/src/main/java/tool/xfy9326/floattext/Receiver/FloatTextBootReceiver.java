package tool.xfy9326.floattext.Receiver;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.preference.*;
import android.provider.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;
import tool.xfy9326.floattext.View.*;

import tool.xfy9326.floattext.R;

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
		utils.setFilterApplication(FloatData.StringToStringArrayList(setdata.getString("Filter_Application", "[]")));
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
		ArrayList<String> Text = utils.getTextData();
		ArrayList<Float> Size = utils.getSizeData();
		ArrayList<Integer> Color = utils.getColorData();
		ArrayList<Boolean> Thick = utils.getThickData();
		ArrayList<Boolean> Show = utils.getShowFloat();
		ArrayList<String> Position = utils.getPosition();
		ArrayList<Boolean> Lock = utils.getLockPosition();
		ArrayList<Boolean> Top = utils.getTextTop();
		ArrayList<Boolean> AutoTop = utils.getAutoTop();
		ArrayList<Boolean> Move = utils.getTextMove();
		ArrayList<Integer> Speed = utils.getTextSpeed();
		ArrayList<Boolean> Shadow = utils.getTextShadow();
		ArrayList<Float> ShadowX = utils.getTextShadowX();
		ArrayList<Float> ShadowY = utils.getTextShadowY();
		ArrayList<Float> ShadowRadius = utils.getTextShadowRadius();
		ArrayList<Integer> BackgroundColor = utils.getBackgroundColor();
		ArrayList<Integer> ShadowColor = utils.getTextShadowColor();
		ArrayList<Boolean> FloatSize = utils.getFloatSize();
		ArrayList<Float> FloatLong = utils.getFloatLong();
		ArrayList<Float> FloatWide = utils.getFloatWide();
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
