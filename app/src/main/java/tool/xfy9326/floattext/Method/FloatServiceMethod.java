package tool.xfy9326.floattext.Method;

import android.content.*;
import android.content.pm.*;
import android.net.wifi.*;
import java.io.*;
import java.util.*;

import android.app.ActivityManager;
import android.net.TrafficStats;
import android.os.PowerManager;
import java.net.NetworkInterface;
import tool.xfy9326.floattext.Tool.FormatArrayList;
import tool.xfy9326.floattext.Utils.StaticNum;

public class FloatServiceMethod
{

	//动态变量更新列表
	public static SharedPreferences setUpdateList(Context ctx)
	{
		SharedPreferences list = ctx.getSharedPreferences("DynamicList", ctx.MODE_PRIVATE);
		SharedPreferences.Editor list_editor = list.edit();
		int version = list.getInt("Version", 0);
		if (version != StaticNum.DYNAMIC_LIST_VERSION)
		{
			ArrayList<String> KeyList = new ArrayList<String>();
			ArrayList<Integer> InfoList = new ArrayList<Integer>();
			KeyList.add("SystemTime");
			InfoList.add(0);
			KeyList.add("SystemTime_24");
			InfoList.add(0);
			KeyList.add("Clock");
			InfoList.add(0);
			KeyList.add("Clock_24");
			InfoList.add(0);
			KeyList.add("Date");
			InfoList.add(0);
			KeyList.add("CPURate");
			InfoList.add(0);
			KeyList.add("NetSpeed");
			InfoList.add(0);
			KeyList.add("MemRate");
			InfoList.add(0);
			KeyList.add("LocalIP");
			InfoList.add(0);
			KeyList.add("Battery");
			InfoList.add(0);
			KeyList.add("Sensor_Light");
			InfoList.add(0);
			KeyList.add("Sensor_Gravity");
			InfoList.add(0);
			KeyList.add("Sensor_Pressure");
			InfoList.add(0);
			KeyList.add("Sensor_CPUTemperature");
			InfoList.add(0);
			KeyList.add("Sensor_Proximity");
			InfoList.add(0);
			KeyList.add("Sensor_Step");
			InfoList.add(0);
			KeyList.add("ClipBoard");
			InfoList.add(0);
			KeyList.add("CurrentActivity");
			InfoList.add(0);
			KeyList.add("Notifications");
			InfoList.add(0);
			KeyList.add("Toasts");
			InfoList.add(0);
			KeyList.add("Week");
			InfoList.add(0);
			KeyList.add("(DateCount_)(.*?)");
			InfoList.add(1);
			KeyList.add("Second");
			InfoList.add(0);
			list_editor.putInt("Version", StaticNum.DYNAMIC_LIST_VERSION);
			list_editor.putString("LIST", KeyList.toString());
			list_editor.putString("INFO", InfoList.toString());
			list_editor.commit();
		}
		return list;
	}

	//判断是否有字符
	public static boolean hasWord(String all, String part)
    {
        return all.contains(part);
    }

	public static String[] StringtoStringArray(String str)
	{
		ArrayList<String> arr = FormatArrayList.StringToStringArrayList(str);
        return arr.toArray(new String[arr.size()]);
	}

	public static int[] StringtoIntegerArray(String str)
	{
		ArrayList<Integer> arr = FormatArrayList.StringToIntegerArrayList(str);
        return Itoi(arr.toArray(new Integer[arr.size()]));
	}

	//判断是否在启动器界面
    public static boolean isHome(Context ctx, List<String> homes)
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

	//布尔值对象转换
	public static int[] Itoi(Integer[] B)
	{
		int[] b = new int[B.length];
		for (int i = 0;i < B.length;i++)
		{
			b[i] = B[i].intValue();
		}
		return b;
	}

	//获取启动器包名
    public static List<String> getHomes(Context ctx)
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

	//修复null
	public static String fixnull(String str, String def)
	{
		if (str == null)
		{
			str = def;
		}
		return str;
	}

	//获取网速
    public static long getTotalRxBytes(Context ctx)
    {
        return TrafficStats.getUidRxBytes(ctx.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);
    }

    public static long getTotalTxBytes(Context ctx)
    {
        return TrafficStats.getUidRxBytes(ctx.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalTxBytes() / 1024);
    }

	//获取代理
    public static boolean isVpnUsed()
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

	//网速单位设置
    public static String netspeedset(float speed)
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

	//获取CPU运行率
    public static int getProcessCpuRate()
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

	//获取内存状态
    public static String getMeminfo(Context ctx)
    {
        ActivityManager mam = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        mam.getMemoryInfo(mi);
        float total =  mi.totalMem;
        float use = total - mi.availMem;
        String result = Math.round((double)(use / total) * 100) + "%";
        return result;
    }

	//判断屏幕开启
    public static boolean isScreenOn(Context ctx)
    {
        PowerManager pm = (PowerManager)ctx.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

	//获取IP
    public static String getIP(Context ctx)
    {
        WifiManager wifiService = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
        WifiInfo wifiinfo = wifiService.getConnectionInfo();
        return intToIp(wifiinfo.getIpAddress());
    }

	//int转IP
    public static String intToIp(int i)
    {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

}
