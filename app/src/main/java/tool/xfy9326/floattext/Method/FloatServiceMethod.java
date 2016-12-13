package tool.xfy9326.floattext.Method;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import tool.xfy9326.floattext.R;

public class FloatServiceMethod
{
    public static String TEXT_UPDATE_ACTION = "tool.xfy9326.floattext.Service.FloatTextUpdateService.action.TEXT_UPDATE_ACTION";
    public static String TEXT_STATE_UPDATE_ACTION = "tool.xfy9326.floattext.Service.FloatTextUpdateService.action.TEXT_STATE_UPDATE_ACTION";

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
