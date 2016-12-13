package tool.xfy9326.floattext.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;
import tool.xfy9326.floattext.R;

public class AboutActivity extends PreferenceActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_about);
        preset();
    }

    private void preset ()
    {
        Preference version = findPreference("Version");
        version.setSummary(getVersionName(this) + "(" + getVersionCode(this) + ")");
        version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                public boolean onPreferenceClick (Preference p)
                {
                    Toast.makeText(AboutActivity.this, "| ू•ૅ㉨•́)ᵎᵎᵎ 我被发现了！", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        Preference thanklist = findPreference("ThankList");
        thanklist.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference pre)
                {
                    AlertDialog.Builder list = new AlertDialog.Builder(AboutActivity.this)
                        .setTitle(R.string.xml_about_thanklist)
                        .setMessage(R.string.thanklist);
                    list.show();
                    return true;
                }
            });
    }

    public static String getVersionName (Context context)
    {
        return getPackageInfo(context).versionName;
    }

    public static int getVersionCode (Context context) 
    { 
        return getPackageInfo(context).versionCode; 
    }

    private static PackageInfo getPackageInfo (Context context)
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
