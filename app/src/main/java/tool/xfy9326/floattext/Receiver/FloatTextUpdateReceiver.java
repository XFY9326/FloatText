package tool.xfy9326.floattext.Receiver;

import android.content.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;

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
		else if (action == StaticString.DYNAMIC_WORD_ADDON_ACTION)
		{
			if (p2 != null)
			{
				DynamicWordUpdateMethod updater = new DynamicWordUpdateMethod(p1, true);
				updater.UpdateText(p2);
			}
		}
    }

	//应用显示过滤
	private void ActivityChange(Context p1, Intent p2)
	{
		String actname = p2.getStringExtra("CurrentActivity");
		if (actname.substring(0, 7).equalsIgnoreCase("android") || actname.contains(p1.getPackageName()))
		{
			return;
		}
		App utils = ((App)p1.getApplicationContext());
		if (utils.StartShowWin)
		{
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
			if (PreferenceManager.getDefaultSharedPreferences(p1).getBoolean("WinFilterMode", false))
			{
				findact = !findact;
			}
			if (findact)
			{
				FloatManageMethod.ShoworHideAllWin(p1, false, true);
			}
			else
			{
				FloatManageMethod.ShoworHideAllWin(p1, true, true);
			}
		}
	}

}
