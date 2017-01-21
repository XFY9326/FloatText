package tool.xfy9326.floattext.Activity;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.preference.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import tool.xfy9326.floattext.*;

import android.app.AlertDialog;
import android.support.v7.app.ActionBar;

public class AboutActivity extends AppCompatPreferenceActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_about);
		sethome();
        preset();
    }
	
	private void sethome ()
	{
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null)
		{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
	}

    private void preset ()
    {
		Preference pst = findPreference("ThirdCode");
		pst.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				public boolean onPreferenceClick (Preference p)
				{
					AlertDialog.Builder list = new AlertDialog.Builder(AboutActivity.this)
						.setTitle(R.string.xml_about_thirdcode)
						.setMessage(R.string.thirdcode_use);
					list.show();
					return true;
				}
			});
        Preference version = findPreference("Version");
        version.setSummary(getVersionName(this) + "(" + getVersionCode(this) + ")");
        version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                public boolean onPreferenceClick (Preference p)
                {
                    Toast.makeText(AboutActivity.this, "╭( ′• o •′ )╭☞就是这个人！", Toast.LENGTH_SHORT).show();
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
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
}
