package tool.xfy9326.floattext.Activity;

import android.content.*;
import android.support.v7.app.*;

import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.view.MenuItem;
import android.widget.Toast;
import tool.xfy9326.floattext.Method.ActivityMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Tool.GithubUpdateCheck;

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
        Preference version = findPreference("Version");
        version.setSummary(ActivityMethod.getVersionName(this) + " (" + ActivityMethod.getVersionCode(this) + ")");
        version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                public boolean onPreferenceClick(Preference p)
                {
                    Toast.makeText(AboutActivity.this, "(ง •̀_•́)ง", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
		Preference checkupdate = findPreference("CheckUpdate");
		checkupdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference pre)
                {
					if (ActivityMethod.isNetworkAvailable(AboutActivity.this))
					{
						GithubUpdateCheck gu = new GithubUpdateCheck(AboutActivity.this);
						gu.setProjectData("XFY9326", "FloatText");
						gu.prepare();
						gu.showDialog(true);
					}
					else
					{
						Toast.makeText(AboutActivity.this, R.string.updater_nointernet, Toast.LENGTH_SHORT).show();
					}
                    return true;
                }
            });
		Preference donate = findPreference("Donate");
        donate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference pre)
                {
                    DonateDialogShow();
                    return true;
                }
            });
        Preference thanklist = findPreference("ThankList");
        thanklist.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference pre)
                {
                    AlertDialog.Builder list = new AlertDialog.Builder(AboutActivity.this)
                        .setTitle(R.string.xml_about_thanklist)
                        .setMessage(R.string.thanklist);
                    list.show();
                    return true;
                }
            });
    }

	private void DonateDialogShow()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(AboutActivity.this)
			.setTitle(R.string.xml_about_donate)
			.setItems(R.array.donate_list, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d, int i)
				{
					String[] urls = getPurchaseURL();
					Uri url = Uri.parse(urls[i].toString().trim());
					Intent intent = new Intent(Intent.ACTION_VIEW, url);
					startActivity(intent);
				}
			})
			.setNegativeButton(R.string.cancel, null);
		dialog.show();
	}

	private String[] getPurchaseURL()
	{
		String[] url = new String[3];
		url[0] = "https://raw.githubusercontent.com/XFY9326/FloatText/gh-pages/Datas/DONATE/AliPay.png";
		url[1] = "https://raw.githubusercontent.com/XFY9326/FloatText/gh-pages/Datas/DONATE/WeChat.png";
		url[2] = "https://raw.githubusercontent.com/XFY9326/FloatText/gh-pages/Datas/DONATE/QQ.png";
		return url;
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
