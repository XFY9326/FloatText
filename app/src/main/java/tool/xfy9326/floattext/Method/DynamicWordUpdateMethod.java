package tool.xfy9326.floattext.Method;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.View.FloatLinearLayout;
import tool.xfy9326.floattext.View.FloatTextView;

public class DynamicWordUpdateMethod
{
    private String DATA_NULL;
    private Context context;
    private ArrayList<FloatTextView> floatview = new ArrayList<FloatTextView>();
    private ArrayList<String> floattext = new ArrayList<String>();
    private ArrayList<WindowManager.LayoutParams> floatlayout = new ArrayList<WindowManager.LayoutParams>();
    private ArrayList<FloatLinearLayout> linearlayout = new ArrayList<FloatLinearLayout>();
    private ArrayList<Boolean> ShowFloat = new ArrayList<Boolean>();
    private WindowManager wm;
    private boolean htmlmode = true;

    public DynamicWordUpdateMethod (Context ctx)
    {
        this.context = ctx;
        this.DATA_NULL = "DATA_RECEIVE_NULL";
    }

    public void UpdateText (Intent intent)
    {
        Bundle bundle = intent.getExtras();
        String systemtime = bundle.getString("SystemTime", DATA_NULL);
        String systemtime_24 = bundle.getString("SystemTime_24", DATA_NULL);
        String clock = bundle.getString("Clock", DATA_NULL);
        String clock_24 = bundle.getString("Clock_24", DATA_NULL);
        String date = bundle.getString("Date", DATA_NULL);
        String cpurate = bundle.getString("CPURate", DATA_NULL);
        String netspeed = bundle.getString("NetSpeed", DATA_NULL);
        String meminfo = bundle.getString("MemInfo", DATA_NULL);
        String localip = bundle.getString("LocalIP", DATA_NULL);
        String battery = bundle.getString("Battery", DATA_NULL);
        String cputemp = bundle.getString("Sensor_CPUTemperature", DATA_NULL);
        String light = bundle.getString("Sensor_Light", DATA_NULL);
        String gravity = bundle.getString("Sensor_Gravity", DATA_NULL);
        String pressure = bundle.getString("Sensor_Pressure", DATA_NULL);
        String proximity = bundle.getString("Sensor_Proximity", DATA_NULL);
        String step = bundle.getString("Sensor_Step", DATA_NULL);
        String clip = bundle.getString("ClipBoard", DATA_NULL);

        App utils = ((App)context.getApplicationContext());
        htmlmode = utils.HtmlMode;
        wm = utils.getFloatwinmanager();
        floatview = utils.getFloatView();
        floattext = utils.getFloatText();
        ShowFloat = utils.getShowFloat();
        linearlayout = utils.getFloatlinearlayout();
        floatlayout = utils.getFloatLayout();
        for (int i = 0; i < floattext.size(); i++)
        {
            if (ShowFloat.get(i))
            {
                String str = floattext.get(i);
                str = updatetext(str, "SystemTime", systemtime, false);
                str = updatetext(str, "SystemTime_24", systemtime_24, false);
                str = updatetext(str, "Clock", clock, false);
                str = updatetext(str, "Clock_24", clock_24, false);
                str = updatetext(str, "Date", date, false);
                str = updatetext(str, "CPURate", cpurate, false);
                str = updatetext(str, "NetSpeed", netspeed, false);
                str = updatetext(str, "MemRate", meminfo, false);
                str = updatetext(str, "LocalIP", localip, false);
                str = updatetext(str, "Battery", battery, false);
                str = updatetext(str, "Sensor_Light", light, false);
                str = updatetext(str, "Sensor_Gravity", gravity, false);
                str = updatetext(str, "Sensor_Pressure", pressure, false);
                str = updatetext(str, "Sensor_CPUTemperature", cputemp, false);
                str = updatetext(str, "Sensor_Proximity", proximity, false);
                str = updatetext(str, "Sensor_Step", step, false);
                str = updatetext(str, "ClipBoard", clip, false);
                str = updatetext(str, "(DateCount_)(.*?)", "NULL", true);
                if (floattext.get(i) != str)
                {
                    floatview.get(i).setText(str);
                    wm.updateViewLayout(linearlayout.get(i), floatlayout.get(i));
                }
            }
        }
    }

    private String updatetext (String str, String tag, String change, boolean reg)
    {
        if (!change.equalsIgnoreCase(DATA_NULL))
        {
            String tag2 = tag;
            tag = "<" + tag + ">";
            str = search(str, tag, change, reg);
            tag2 = "#" + tag2 + "#";
            str = search(str, tag2, change, reg);
        }
        return str;
    }

    private String search (String str, String tag, String change, boolean reg)
    {
        if (reg)
        {
            Pattern pat = Pattern.compile(tag);
            Matcher mat = pat.matcher(str);
            while (mat.find())
            {
                String get = mat.group(0).toString();
                if (str.length() >= 11)
                {
                    str = str.replace(get, FloatServiceMethod.datecount(context, (get.substring(11, get.length() - 1))));
                }
            }
        }
        else
        {
            if (str.indexOf(tag) >= 0)
            {
                str = str.replace(tag, change);
            }
        }
        return str;
    }
}
