package lib.xfy9326.crashreport;

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
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler
{
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private Map<String, String> infos = new HashMap<String, String>();
    private String Log = "";
    private String Info = "";
    private String mainclassname;
    private String mail;
    private String AppName;
    
    private CrashHandler()
    {}

    public static CrashHandler getInstance()
    {
        return INSTANCE;
    }

    public void init(Context context,String AppName,String mainclassname,String mail)
    {
        mContext = context;
        this.mail = mail;
        this.AppName = AppName;
        this.mainclassname = mainclassname;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && mDefaultHandler != null)
        {
            mDefaultHandler.uncaughtException(thread, ex);
        }
        else
        {
            try
            {
                Thread.sleep(1500);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
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

    private boolean handleException(Throwable ex)
    {
        if (ex == null)
        {
            return false;
        }
        new Thread() {
            @Override
            public void run()
            {
                Looper.prepare();
                Toast.makeText(mContext, R.string.crashreport_toast_info, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        collectDeviceInfo(mContext);
        saveCrashInfo(ex);
        return true;
    }

    public void collectDeviceInfo(Context ctx)
    {
        try
        {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),PackageManager.GET_ACTIVITIES);
            if (pi != null)
            {
                String versionName = pi.versionName == null ? "null"
                    : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void saveCrashInfo(Throwable ex)
    {

        String str = "";
        for (Map.Entry<String, String> entry : infos.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            str += key + "=" + value + "\n";
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null)
        {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        Info = str;
        Log = result;
    }

}
