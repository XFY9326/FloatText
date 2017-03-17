package tool.xfy9326.floattext.CrashReport;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import tool.xfy9326.floattext.Method.IOMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Utils.StaticNum;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private Map<String, String> infos = new HashMap<String, String>();
    private String Log = "";
    private String Info = "";
    private String mainclassname;
    private String mail;
    private String AppName;
	private boolean outputerror;
	private String outputpath;

    private CrashHandler() {}

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context, String AppName, String mainclassname, String mail) {
        mContext = context;
        this.mail = mail;
        this.AppName = AppName;
        this.mainclassname = mainclassname;
		this.outputerror = false;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

	public void setOutPutError(boolean output, String path) {
		outputerror = output;
		outputpath = path;
	}

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
			if (outputerror) {
				ErrorOutPut(ex);
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
			if (!outputerror) {
				Intent ui = new Intent(mContext, CrashHandlerUI.class);
				ui.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ui.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				ui.putExtra("CrashLog", Log);
				ui.putExtra("DeviceInfo", Info);
				ui.putExtra("ClassName", mainclassname);
				ui.putExtra("Mail", mail);
				ui.putExtra("AppName", AppName);
				mContext.startActivity(ui);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
    }

	private void ErrorOutPut(final Throwable ex) {
		String FileName = AppName + "-CrashLog_" + stampToDate(new Date().getTime() + "");
		String error = ExToString(ex);
		IOMethod.writefile(outputpath + FileName + ".txt", error);
	}

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, R.string.crashreport_toast_info, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        collectDeviceInfo(mContext);
        saveCrashInfo(mContext, ex);
        return true;
    }

	private String getAppInfo(Context ctx) {
		try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
				String installTime = stampToDate(pi.firstInstallTime + "");
				String lastupdatetime = stampToDate(pi.lastUpdateTime + "");
				String errortime = stampToDate(new Date().getTime() + "");
                String result = "VersionName = " + versionName + "\n" + "VersionCode = " + versionCode + "\n" + "ErrorTime = " + errortime + "\n" + "InstallTime = " + installTime + "\n" + "LastUpdateTime = " + lastupdatetime;
				result += "\n" + "FloatDataVersion = " + StaticNum.FloatDataVersion + "\n" + "DynamicListVersion = " + StaticNum.DYNAMIC_LIST_VERSION;
				result += "\n" + "SDK = " + Build.VERSION.SDK + "(" + Build.VERSION.SDK_INT + ")";
				return result;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
		return "Unknown Application Version";
	}

    private void collectDeviceInfo(Context ctx) {
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName().toString(), field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	private String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    private void saveCrashInfo(Context ctx, Throwable ex) {
        String str = "";
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			if (key.contains("java.lang.String") || value.contains("java.lang.String") || key.equalsIgnoreCase("UNKNOWN") || value.equalsIgnoreCase("unknown")) {
				continue;
			} else {

            	str += key + " = " + value + " \n";
			}
        }
		str = getAppInfo(ctx) + "\n\n" + str;
        String result = ExToString(ex);
        Info = str;
        Log = result;
    }

	private String ExToString(Throwable ex) {
		Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
	}

}
