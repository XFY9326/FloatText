package tool.xfy9326.floattext.Method;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityMethod {

    //文件大小格式
    public static String formatSize(Context ctx, String target_size) {
        if (target_size != null) {
            return Formatter.formatFileSize(ctx, Long.valueOf(target_size));
        } else {
            return "0B";
        }
    }

    //获取后缀名
    public static String getExtraName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "No_Name";
    }

    //包名排序
    public static void orderPackageList(final Context ctx, List<PackageInfo> list) {
        Collections.sort(list, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo o1, PackageInfo o2) {
                String str1 = o1.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
                String str2 = o2.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
                return str1.compareTo(str2);
            }
        });
    }

    //辅助服务是否打开
    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }

    //通知监听是否打开
    public static boolean isNotificationListenerEnabled(Context ctx) {
        String pkgName = ctx.getPackageName();
        final String flat = Settings.Secure.getString(ctx.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //网络是否可用
    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return !(networkInfo == null || !networkInfo.isAvailable());
    }

    //获取版本名
    public static String getVersionName(Context context) {
        PackageInfo pinfo = getPackageInfo(context);
        if (pinfo != null) {
            return pinfo.versionName;
        } else {
            return null;
        }
    }

    //获取版本号
    public static int getVersionCode(Context context) {
        PackageInfo pinfo = getPackageInfo(context);
        if (pinfo != null) {
            return pinfo.versionCode;
        } else {
            return 0;
        }
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
