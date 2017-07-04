package tool.xfy9326.floattext.Activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import tool.xfy9326.floattext.Method.ActivityMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Tool.DonateList;
import tool.xfy9326.floattext.Tool.GithubUpdateCheck;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();
        sethome();
    }

    private void sethome() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PrefFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.activity_about);
            preset();
        }

        private void preset() {
            //版本号
            Preference version = findPreference("Version");
            version.setSummary(ActivityMethod.getVersionName(getActivity()) + " (" + ActivityMethod.getVersionCode(getActivity()) + ")");
            version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference p) {
                    Toast.makeText(getActivity(), "(ノ=Д=)ノ┻━┻", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            //更新检测
            Preference checkupdate = findPreference("CheckUpdate");
            checkupdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pre) {
                    if (ActivityMethod.isNetworkAvailable(getActivity())) {
                        GithubUpdateCheck gu = new GithubUpdateCheck(getActivity());
                        gu.setProjectData("XFY9326", "FloatText");
                        gu.setMarketDownload(true, "http://www.coolapk.com/apk/tool.xfy9326.floattext");
                        gu.showUpdateInfoDialog(true);
                    } else {
                        Toast.makeText(getActivity(), R.string.updater_nointernet, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            //捐赠
            Preference donate = findPreference("Donate");
            donate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pre) {
                    DonateList dl = new DonateList(getActivity());
                    dl.show();
                    return true;
                }
            });
            //感谢列表
            Preference thanklist = findPreference("ThankList");
            thanklist.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pre) {
                    AlertDialog.Builder list = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.xml_about_thanklist)
                            .setMessage(R.string.thanklist)
                            .setPositiveButton(R.string.done, null);
                    list.show();
                    return true;
                }
            });
        }
    }

}
