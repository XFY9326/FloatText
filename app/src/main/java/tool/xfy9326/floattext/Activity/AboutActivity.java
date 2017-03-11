package tool.xfy9326.floattext.Activity;

import android.support.v7.app.*;
import tool.xfy9326.floattext.Tool.*;

import android.os.Bundle;
import android.preference.Preference;
import android.view.MenuItem;
import android.widget.Toast;
import tool.xfy9326.floattext.Method.ActivityMethod;
import tool.xfy9326.floattext.R;

public class AboutActivity extends AppCompatPreferenceActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_about);
		sethome();
        preset();
    }

	private void sethome()
	{
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
		{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
	}

    private void preset()
    {
		//版本号
        Preference version = findPreference("Version");
        version.setSummary(ActivityMethod.getVersionName(this) + " (" + ActivityMethod.getVersionCode(this) + ")");
        version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                public boolean onPreferenceClick(Preference p)
                {
                    Toast.makeText(AboutActivity.this, "(ノ=Д=)ノ┻━┻", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
		//更新检测
		Preference checkupdate = findPreference("CheckUpdate");
		checkupdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference pre)
                {
					if (ActivityMethod.isNetworkAvailable(AboutActivity.this))
					{
						GithubUpdateCheck gu = new GithubUpdateCheck(AboutActivity.this);
						gu.setProjectData("XFY9326", "FloatText");
						gu.setMarketDownload(true, "http://www.coolapk.com/apk/tool.xfy9326.floattext");
						gu.showUpdateInfoDialog(true);
					}
					else
					{
						Toast.makeText(AboutActivity.this, R.string.updater_nointernet, Toast.LENGTH_SHORT).show();
					}
                    return true;
                }
            });
		//捐赠
		Preference donate = findPreference("Donate");
        donate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference pre)
                {
                    DonateList dl = new DonateList(AboutActivity.this);
					dl.show();
                    return true;
                }
            });
		//感谢列表
        Preference thanklist = findPreference("ThankList");
        thanklist.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference pre)
                {
                    AlertDialog.Builder list = new AlertDialog.Builder(AboutActivity.this)
                        .setTitle(R.string.xml_about_thanklist)
                        .setMessage(R.string.thanklist)
						.setPositiveButton(R.string.done, null);
                    list.show();
                    return true;
                }
            });
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
