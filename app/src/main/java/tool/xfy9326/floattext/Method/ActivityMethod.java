package tool.xfy9326.floattext.Method;

import android.content.*;
import android.content.pm.*;
import android.net.*;
import java.util.*;

import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
import java.io.File;

public class ActivityMethod
{
	//文件夹大小获取
	public static long getFolderSize(File file)
	{  
        long size = 0;  
        try
		{
			File[] fileList = file.listFiles();   
			for (int i = 0; i < fileList.length; i++)   
			{   
			    if (fileList[i].isDirectory())   
			    {   
			        size += getFolderSize(fileList[i]);
			    }
				else
				{   
			        size += fileList[i].length();
			    }   
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}   
        return size;  
    }  
	
	//文件大小格式
	public static String formatSize(Context ctx, String target_size)
	{
		if (target_size != null)
		{
			return Formatter.formatFileSize(ctx, Long.valueOf(target_size));
		}
		else
		{
			return "0B";
		}
    }
	
	//获取后缀名
	public static String getExtraName(String filename)
    { 
        if ((filename != null) && (filename.length() > 0))
        { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot > -1) && (dot < (filename.length() - 1)))
            { 
                return filename.substring(dot + 1); 
            } 
        } 
        return "No_Name"; 
    }
	
	//包名排序
	public static List<PackageInfo> orderPackageList(final Context ctx, List<PackageInfo> list)
	{
		Collections.sort(list, new Comparator<PackageInfo>() {
                @Override
                public int compare(PackageInfo o1, PackageInfo o2)
                {
					String str1 = o1.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
					String str2 = o2.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
                    return str1.compareTo(str2);
                }
            });
		return list;
	}
	
	//辅助服务是否打开
	public static boolean isAccessibilitySettingsOn(Context context)
	{
        int accessibilityEnabled = 0;
        try
		{
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        }
		catch (Settings.SettingNotFoundException e)
		{
			e.printStackTrace();
        }
        if (accessibilityEnabled == 1)
		{
            String services = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null)
			{
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }

	//通知监听是否打开
	public static boolean isNotificationListenerEnabled(Context ctx)
	{  
		String pkgName = ctx.getPackageName();  
		final String flat = Settings.Secure.getString(ctx.getContentResolver(), "enabled_notification_listeners");  
		if (!TextUtils.isEmpty(flat))
		{  
			final String[] names = flat.split(":");  
			for (int i = 0; i < names.length; i++)
			{  
				final ComponentName cn = ComponentName.unflattenFromString(names[i]);  
				if (cn != null)
				{  
					if (TextUtils.equals(pkgName, cn.getPackageName()))
					{  
						return true;  
					}  
				}  
			}  
		}  
		return false;  
	}

	//网络是否可用
	public static boolean isNetworkAvailable(Context ctx)
	{
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();  
		if (networkInfo == null || !networkInfo.isAvailable())  
		{  
			return false;
		}  
		else   
		{  
			return true;
		}
	}

	//获取版本名
    public static String getVersionName(Context context)
    {
        return getPackageInfo(context).versionName;
    }

	//获取版本号
    public static int getVersionCode(Context context) 
    { 
        return getPackageInfo(context).versionCode; 
    }

	private static PackageInfo getPackageInfo(Context context)
    { 
        PackageInfo pi = null;  
        try
        {
            PackageManager pm = context.getPackageManager(); pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);  return pi; }
        catch (Exception e)
        { 
            e.printStackTrace();
        } 
        return pi; 
    }
}
