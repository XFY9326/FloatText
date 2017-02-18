package tool.xfy9326.floattext.Receiver;

import android.content.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;

import android.os.Bundle;
import android.preference.PreferenceManager;
import java.util.ArrayList;

public class FloatTextUpdateReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context p1, Intent p2)
    {
        String action = p2.getAction();
        if (action == StaticString.TEXT_UPDATE_ACTION)
        {
            DynamicWordUpdateMethod updater = new DynamicWordUpdateMethod(p1, false);
            updater.UpdateText(p2);
        }
		else if (action == StaticString.ACTIVITY_CHANGE_ACTION)
		{
			ActivityChange(p1, p2);
		}
        else if (action == StaticString.TEXT_STATE_UPDATE_ACTION)
        {
            StateChange(p1, p2);
        }
		else if (action == StaticString.DYNAMIC_WORD_ADDON_ACTION)
		{
			if (p2 != null)
			{
				DynamicWordUpdateMethod updater = new DynamicWordUpdateMethod(p1, true);
				updater.UpdateText(p2);
			}
		}
    }

	private void StateChange(Context p1, Intent p2)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(p1);
		if (sp.getBoolean("WinOnlyShowInHome", false))
		{
			Bundle bundle = p2.getExtras();
			if (bundle.getBoolean("Float_InHome"))
			{
				FloatManageMethod.ShoworHideAllWin(p1, true, false);
			}
			else
			{
				FloatManageMethod.ShoworHideAllWin(p1, false, false);
			}
		}
	}

	private void ActivityChange(Context p1, Intent p2)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(p1);
		if (!sp.getBoolean("WinOnlyShowInHome", false))
		{
			String actname = p2.getStringExtra("CurrentActivity");
			App utils = ((App)p1.getApplicationContext());
			ArrayList<String> fa = utils.getFrameutil().getFilterApplication();
			boolean findact = false;
			for (int a = 0;a < fa.size();a++)
			{
				if (actname.contains(fa.get(a).toString()))
				{
					findact = true;
					break;
				}
			}
			if (findact)
			{
				FloatManageMethod.ShoworHideAllWin(p1, false, false);
			}
			else
			{
				FloatManageMethod.ShoworHideAllWin(p1, true, false);
			}
		}
	}

}
