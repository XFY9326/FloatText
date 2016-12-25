package tool.xfy9326.floattext.Receiver;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import java.util.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;
import tool.xfy9326.floattext.View.*;

public class FloatTextUpdateReceiver extends BroadcastReceiver
{
    private String TextAction = FloatServiceMethod.TEXT_UPDATE_ACTION;
    private String StateAction = FloatServiceMethod.TEXT_STATE_UPDATE_ACTION;

    @Override
    public void onReceive (Context p1, Intent p2)
    {
        String action = p2.getAction();
        if (action == TextAction)
        {
			Log.d("FT", "Receive Update Package");
            DynamicWordUpdateMethod updater = new DynamicWordUpdateMethod(p1);
            updater.UpdateText(p2);
        }
        else if (action == StateAction)
        {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(p1);
            if (sp.getBoolean("WinOnlyShowInHome", false))
            {
                Bundle bundle = p2.getExtras();
                App utils = ((App)p1.getApplicationContext());
                ArrayList<FloatLinearLayout> layout = utils.getFloatlinearlayout();
                ArrayList<Boolean> show = utils.getShowFloat();
                ListViewAdapter adp = utils.getListviewadapter();
                if (bundle.getBoolean("Float_InHome"))
                {
                    for (int i = 0;i < layout.size(); i++)
                    {
                        layout.get(i).setShowState(true);
                        show.set(i, true);
                    }
                }
                else
                {
                    for (int i = 0;i < layout.size(); i++)
                    {
                        layout.get(i).setShowState(false);
                        show.set(i, false);
                    }
                }
                adp.notifyDataSetChanged();
            }
        }
    }

}
