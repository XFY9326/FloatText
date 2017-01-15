package tool.xfy9326.floattext.Method;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.net.wifi.*;
import android.os.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import tool.xfy9326.floattext.*;

import java.lang.Process;
import java.text.ParseException;

public class FloatServiceMethod
{
    public static String TEXT_UPDATE_ACTION = "tool.xfy9326.floattext.Service.FloatTextUpdateService.action.TEXT_UPDATE_ACTION";
    public static String TEXT_STATE_UPDATE_ACTION = "tool.xfy9326.floattext.Service.FloatTextUpdateService.action.TEXT_STATE_UPDATE_ACTION";
	public static String TEXT_ADVANCE_UPDATE_ACTION = "tool.xfy9326.floattext.Service.FloatAdvanceTextUpdateService.action.TEXT_ADVANCE_UPDATE_ACTION";
	public static int DYNAMIC_LIST_VERSION = 4;

	public static SharedPreferences setUpdateList (Context ctx)
	{
		SharedPreferences list = ctx.getSharedPreferences("DynamicList", ctx.MODE_PRIVATE);
		SharedPreferences.Editor list_editor = list.edit();
		int version = list.getInt("Version", 0);
		if (version != DYNAMIC_LIST_VERSION)
		{
			ArrayList<String> KeyList = new ArrayList<String>();
			ArrayList<Boolean> InfoList = new ArrayList<Boolean>();
			KeyList.add("SystemTime");
			InfoList.add(false);
			KeyList.add("SystemTime_24");
			InfoList.add(false);
			KeyList.add("Clock");
			InfoList.add(false);
			KeyList.add("Clock_24");
			InfoList.add(false);
			KeyList.add("Date");
			InfoList.add(false);
			KeyList.add("CPURate");
			InfoList.add(false);
			KeyList.add("NetSpeed");
			InfoList.add(false);
			KeyList.add("MemRate");
			InfoList.add(false);
			KeyList.add("LocalIP");
			InfoList.add(false);
			KeyList.add("Battery");
			InfoList.add(false);
			KeyList.add("Sensor_Light");
			InfoList.add(false);
			KeyList.add("Sensor_Gravity");
			InfoList.add(false);
			KeyList.add("Sensor_Pressure");
			InfoList.add(false);
			KeyList.add("Sensor_CPUTemperature");
			InfoList.add(false);
			KeyList.add("Sensor_Proximity");
			InfoList.add(false);
			KeyList.add("Sensor_Step");
			InfoList.add(false);
			KeyList.add("ClipBoard");
			InfoList.add(false);
			KeyList.add("CurrentActivity");
			InfoList.add(false);
			KeyList.add("Notifications");
			InfoList.add(false);
			KeyList.add("Toasts");
			InfoList.add(false);
			KeyList.add("(DateCount_)(.*?)");
			InfoList.add(true);
			list_editor.putInt("Version", DYNAMIC_LIST_VERSION);
			list_editor.putString("LIST", KeyList.toString());
			list_editor.putString("INFO", InfoList.toString());
			list_editor.commit();
		}
		return list;
	}

	public static String[] StringtoStringArray (String str)
	{
		ArrayList<String> arr = new ArrayList<String>();
        if (str.contains("[") && str.length() >= 3)
        {
            str = str.substring(1, str.length() - 1);
            if (str.contains(","))
            {
                String[] temp = str.split(",");
                for (int i = 0;i < temp.length;i++)
                {
                    if (i != 0)
                    {
                        temp[i] = temp[i].substring(1, temp[i].length());
                    }
                    arr.add(temp[i].toString());
                }
            }
            else
            {
                arr.add(str.toString());
            }
        }
        return arr.toArray(new String[arr.size()]);
	}

	public static boolean[] StringtoBooleanArray (String str)
	{
		ArrayList<Boolean> arr = new ArrayList<Boolean>();
        if (str.contains("[") && str.length() >= 3)
        {
            str = str.substring(1, str.length() - 1);
            if (str.contains(","))
            {
                String[] temp = str.split(",");
                for (int i = 0;i < temp.length;i++)
                {
                    temp[i] = temp[i].replaceAll("\\s+", "");
                    arr.add(Boolean.parseBoolean(temp[i]));
                }
            }
            else
            {
                arr.add(Boolean.parseBoolean(str));
            }
        }
        return Btob(arr.toArray(new Boolean[arr.size()]));
	}

    public static boolean isHome (Context ctx, List<String> homes)
    {
        ActivityManager mActivityManager = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        String Top = rti.get(0).topActivity.getPackageName();
        if (Top.equalsIgnoreCase("tool.xfy9326.floattext"))
        {
            return false;
        }
        else
        {
            return homes.contains(Top);
        }
    }

	public static boolean[] Btob (Boolean[] B)
	{
		boolean[] b = new boolean[B.length];
		for (int i = 0;i < B.length;i++)
		{
			b[i] = B[i];
		}
		return b;
	}

    public static List<String> getHomes (Context ctx)
    {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = ctx.getPackageManager();
        Intent intent =new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (int i = 0; i < resolveInfo.size() ; i++)
        {
            String str = resolveInfo.get(i).activityInfo.packageName.toString();
            names.add(str);
        }
        return names;
    }

	public static String fixnull (String str, String def)
	{
		if (str == null)
		{
			str = def;
		}
		return str;
	}

    public static long getTotalRxBytes (Context ctx)
    {
        return TrafficStats.getUidRxBytes(ctx.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);
    }

    public static long getTotalTxBytes (Context ctx)
    {
        return TrafficStats.getUidRxBytes(ctx.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalTxBytes() / 1024);
    }

    public static boolean isVpnUsed ()
    {
        try
        {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null)
            {
                for (NetworkInterface intf : Collections.list(niList))
                {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0)
                    {
                        continue;
                    }
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName()))
                    {                        
                        return true;
                    }
                }
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        return false;
	}

    public static String netspeedset (float speed)
    {
        String NS = "KB/s";
        if (speed >= 1024)
        {
            NS = "MB/s";
            speed = speed / 1024;
            if (speed >= 1024)
            {
                NS = "GB/s";
                speed = speed / 1024;
            }
        }
        return Math.round(speed * 100) / 100.00 + NS;
    }

    public static int getProcessCpuRate ()
    {
        int rate = 0;
        try
        {
            String Result;
            Process p = Runtime.getRuntime().exec("top -n 1 -m 1");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((Result = br.readLine()) != null)
            {
                if (Result.trim().length() < 1)
                {
                    continue;
                }
                else
                {
                    String[] CPUusr = Result.split("%");
                    String[] CPUusage = CPUusr[0].split("User");
                    String[] SYSusage = CPUusr[1].split("System");
                    rate = Integer.parseInt(CPUusage[1].trim()) + Integer.parseInt(SYSusage[1].trim());
                    break;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return rate;
    }

    public static String getMeminfo (Context ctx)
    {
        ActivityManager mam = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        mam.getMemoryInfo(mi);
        float total =  mi.totalMem;
        float use = total - mi.availMem;
        String result = Math.round((double)(use / total) * 100) + "%";
        return result;
    }

    public static String datecount (Context ctx, String date)
    {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        DateFormat format3 = new SimpleDateFormat("yyyy-MM-dd-HH");
        DateFormat format4 = new SimpleDateFormat("yyyy-MM-dd");
        String outputdate;
        Date now = new Date();
        Date set = new Date();
        long diff = 0;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        try
        {
            set = format1.parse(date);
            diff = set.getTime() - now.getTime();
            days = diff / (1000 * 60 * 60 * 24);
            hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
            if (diff > 0)
            {
                if (days == 0)
                {
                    outputdate = hours + ctx.getString(R.string.hour) + minutes + ctx.getString(R.string.minute) + seconds + ctx.getString(R.string.second);
                    if (hours == 0)
                    {
                        outputdate = minutes + ctx.getString(R.string.minute) + seconds + ctx.getString(R.string.second);
                        if (minutes == 0)
                        {
                            outputdate = seconds + ctx.getString(R.string.second);
                        }
                    }
                }
                else
                {
                    outputdate = days + ctx.getString(R.string.day) + hours + ctx.getString(R.string.hour) + minutes + ctx.getString(R.string.minute) + seconds + ctx.getString(R.string.second);
                }
            }
            else
            {
                outputdate = ctx.getString(R.string.dynamic_date_reach);
            }
        }
        catch (Exception e1)
        {
            try
            {
                set = format2.parse(date);
                diff = set.getTime() - now.getTime();
                days = diff / (1000 * 60 * 60 * 24);
                hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                diff = diff - seconds * 1000;
                if (diff > 0)
                {
                    if (days == 0)
                    {
                        outputdate = hours + ctx.getString(R.string.hour) + minutes + ctx.getString(R.string.minute);
                        if (hours == 0)
                        {
                            outputdate = minutes + ctx.getString(R.string.minute);
                        }
                    }
                    else
                    {
                        outputdate = days + ctx.getString(R.string.day) + hours + ctx.getString(R.string.hour) + minutes + ctx.getString(R.string.minute);
                    }
                }
                else
                {
                    outputdate = ctx.getString(R.string.dynamic_date_reach);
                }
            }
            catch (ParseException e2)
            {
                try
                {
                    set = format3.parse(date);
                    diff = set.getTime() - now.getTime();
                    days = diff / (1000 * 60 * 60 * 24);
                    hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                    diff = diff - minutes * 60 * 1000 - seconds * 1000;
                    if (diff > 0)
                    {
                        if (days == 0)
                        {
                            outputdate = hours + ctx.getString(R.string.hour);
                        }
                        else
                        {
                            outputdate = days + ctx.getString(R.string.day) + hours + ctx.getString(R.string.hour);
                        }
                    }
                    else
                    {
                        outputdate = ctx.getString(R.string.dynamic_date_reach);
                    }
                }
                catch (ParseException e3)
                {
                    try
                    {
                        set = format4.parse(date);
                        diff = set.getTime() - now.getTime();
                        days = diff / (1000 * 60 * 60 * 24);
                        diff = diff - hours * 60 * 60 * 1000 - minutes * 60 * 1000 - seconds * 1000;
                        if (diff > 0)
                        {
                            outputdate = days + ctx.getString(R.string.day);
                        }
                        else
                        {
                            outputdate = ctx.getString(R.string.dynamic_date_reach);
                        }
                    }
                    catch (ParseException e4)
                    {
                        outputdate = ctx.getString(R.string.dynamic_date_err);
                        e4.printStackTrace();
                    }
                }
            }
        }
        return outputdate;
    }

    public static boolean isScreenOn (Context ctx)
    {
        PowerManager pm = (PowerManager)ctx.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    public static String getIP (Context ctx)
    {
        WifiManager wifiService = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
        WifiInfo wifiinfo = wifiService.getConnectionInfo();
        return intToIp(wifiinfo.getIpAddress());
    }

    public static String intToIp (int i)
    {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

}
