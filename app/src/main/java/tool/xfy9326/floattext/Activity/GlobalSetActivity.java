package tool.xfy9326.floattext.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Service.FloatTextUpdateService;
import tool.xfy9326.floattext.Service.FloatWindowStayAliveService;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.View.FloatLinearLayout;
import tool.xfy9326.floattext.View.ListViewAdapter;

public class GlobalSetActivity extends PreferenceActivity
{
    private String default_typeface;
    private int typeface_choice;
    private int language_choice;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.global_settings);
        setDataToApp();
    }

    private void setDataToApp ()
    {
        CheckBoxPreference movemethod = (CheckBoxPreference) findPreference("TextMovingMethod");
        movemethod.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    ((App)getApplicationContext()).setMovingMethod((boolean)p2);
                    Toast.makeText(GlobalSetActivity.this, R.string.restart_to_apply, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        Preference typeface = findPreference("TextTypeface");
        SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_WORLD_READABLE);
        default_typeface = setdata.getString("DefaultTTFName", "Default");
        if (default_typeface.equalsIgnoreCase("Default"))
        {
            default_typeface = getString(R.string.text_default_typeface);
        }
        typeface.setSummary(getString(R.string.xml_global_text_typeface_summary) + default_typeface);
        typeface.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (final Preference pre)
                {
                    File path = new File(Environment.getExternalStorageDirectory().toString() + "/FloatText/TTFs");
                    if (!path.exists())
                    {
                        path.mkdirs();
                    }
                    File[] files = path.listFiles();
                    int defaultchoice = 0;
                    ArrayList<String> ttfs = new ArrayList<String>();
                    for (int i = 0; i < files.length;i++)
                    {
                        String str = files[i].getName().toString();
                        String extra = getExtraName(str);
                        if (extra.equalsIgnoreCase("ttf"))
                        {
                            String realname = str.substring(0, str.length() - 4);
                            ttfs.add(realname);
                            if (realname.equalsIgnoreCase(default_typeface))
                            {
                                defaultchoice = i + 1;
                            }
                        }
                    }
                    typeface_choice = defaultchoice;
                    ttfs.add(0, getString(R.string.text_default_typeface));
                    final String[] ttfname = new String[ttfs.size()];
                    for (int i = 0; i < ttfs.size(); i++)
                    {
                        ttfname[i] = ttfs.get(i);
                    }
                    AlertDialog.Builder pathselect = new AlertDialog.Builder(GlobalSetActivity.this)
                        .setTitle(R.string.text_choose_typeface)
                        .setSingleChoiceItems(ttfname, defaultchoice, new DialogInterface.OnClickListener(){
                            public void onClick (DialogInterface p1, int p2)
                            {
                                typeface_choice = p2;
                            }
                        })
                        .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                            public void onClick (DialogInterface p1, int p2)
                            {
                                SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_WORLD_READABLE);
                                if (typeface_choice == 0)
                                {
                                    setdata.edit().putString("DefaultTTFName", "Default").commit();
                                    pre.setSummary(getString(R.string.xml_global_text_typeface_summary) + getString(R.string.text_default_typeface));
                                    default_typeface = "Default";
                                }
                                else
                                {
                                    setdata.edit().putString("DefaultTTFName", ttfname[typeface_choice]).commit();
                                    pre.setSummary(getString(R.string.xml_global_text_typeface_summary) + ttfname[typeface_choice]);
                                    default_typeface = ttfname[typeface_choice];
                                }
                                Toast.makeText(GlobalSetActivity.this, R.string.restart_to_apply, Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null);
                    pathselect.show();
                    return true;
                }
            });
        Preference language = findPreference("Language");
        final String[] lan_list = getResources().getStringArray(R.array.language_list);
        language_choice = setdata.getInt("Language", 0);
        language.setSummary(getString(R.string.xml_global_language_sum) + lan_list[language_choice]);
        language.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (final Preference pre)
                {
                    AlertDialog.Builder lan = new AlertDialog.Builder(GlobalSetActivity.this)
                        .setTitle(R.string.xml_global_language)
                        .setSingleChoiceItems(lan_list, language_choice, new DialogInterface.OnClickListener(){
                            public void onClick (DialogInterface dialog, int which)
                            {
                                language_choice = which;
                            }
                        })
                        .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                            public void onClick (DialogInterface p1, int p2)
                            {
                                ((App)getApplicationContext()).setLanguage(language_choice);
                                SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_WORLD_READABLE);
                                setdata.edit().putInt("Language", language_choice).commit();
                                FloatManageMethod.LanguageSet(GlobalSetActivity.this, language_choice);
                                pre.setSummary(getString(R.string.xml_global_language_sum) + lan_list[language_choice]);
                                restartApplication();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                            public void onClick (DialogInterface d, int i)
                            {
                                SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_WORLD_READABLE);
                                language_choice = setdata.getInt("Language", 0);
                            }
                        });
                    lan.show();
                    return true;
                }
            });
        CheckBoxPreference stayalive = (CheckBoxPreference) findPreference("StayAliveService");
        stayalive.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    ((App)getApplicationContext()).setStayAliveService((boolean)p2);
                    Intent service = new Intent(GlobalSetActivity.this, FloatWindowStayAliveService.class);
                    if ((boolean)p2)
                    {
                        startService(service);
                    }
                    else
                    {
                        stopService(service);
                    }
                    return true;
                }
            });
        CheckBoxPreference dynamicnum = (CheckBoxPreference) findPreference("DynamicNumService");
        dynamicnum.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    ((App)getApplicationContext()).setDynamicNumService((boolean)p2);
                    Intent service = new Intent(GlobalSetActivity.this, FloatTextUpdateService.class);
                    if ((boolean)p2)
                    {
                        startService(service);
                    }
                    else
                    {
                        stopService(service);
                    }
                    return true;
                }
            });
        CheckBoxPreference develop = (CheckBoxPreference) findPreference("DevelopMode");
        develop.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    ((App)getApplicationContext()).setDevelopMode((boolean)p2);
                    return true;
                }
            });
        CheckBoxPreference html = (CheckBoxPreference) findPreference("HtmlMode");
        html.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    ((App)getApplicationContext()).setHtmlMode((boolean)p2);
                    Toast.makeText(GlobalSetActivity.this, R.string.restart_to_apply, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        CheckBoxPreference hidetext = (CheckBoxPreference) findPreference("ListTextHide");
        hidetext.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    ((App)getApplicationContext()).setListTextHide((boolean)p2);
                    return true;
                }
            });
        CheckBoxPreference onlyshowinhome = (CheckBoxPreference)findPreference("WinOnlyShowInHome");
        onlyshowinhome.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    if (!(boolean)p2)
                    {
                        App utils = ((App)getApplicationContext());
                        ArrayList<FloatLinearLayout> layout = utils.getFloatlinearlayout();
                        ArrayList<Boolean> show = utils.getShowFloat();
                        ListViewAdapter adp = utils.getListviewadapter();
                        for (int i = 0;i < layout.size(); i++)
                        {
                            layout.get(i).setShowState(true);
                            show.set(i, true);
                        }
                        adp.notifyDataSetChanged();
                    }
                    return true;
                }
            });
    }

    private void restartApplication ()
    {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        System.exit(0);
    }

    private String getExtraName (String filename)
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
}
