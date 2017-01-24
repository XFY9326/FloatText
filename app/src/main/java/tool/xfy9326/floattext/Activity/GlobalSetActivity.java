package tool.xfy9326.floattext.Activity;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.preference.*;
import android.provider.*;
import android.support.v7.app.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Service.*;
import tool.xfy9326.floattext.Utils.*;
import tool.xfy9326.floattext.View.*;

import android.app.AlertDialog;
import android.support.v7.app.ActionBar;
import tool.xfy9326.floattext.R;

public class GlobalSetActivity extends AppCompatPreferenceActivity
{
    private String default_typeface;
    private int typeface_choice;
    private int language_choice;
	private static int ADVANCE_TEXT_SET = 1;
	private static int ADVANCE_TEXT_NOTIFICATION_SET = 2;
	private static int FLOAT_TEXT_GET_TYPEFACE_PERMISSION = 3;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.global_settings);
		sethome();
        setDataToApp();
    }

	private void sethome ()
	{
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
		{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
        SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
        default_typeface = setdata.getString("DefaultTTFName", "Default");
        if (default_typeface.equalsIgnoreCase("Default"))
        {
            default_typeface = getString(R.string.text_default_typeface);
        }
        typeface.setSummary(getString(R.string.xml_global_text_typeface_summary) + default_typeface);
        typeface.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference pre)
                {
					if (Build.VERSION.SDK_INT > 22)
					{
						if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
						{
							requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, FLOAT_TEXT_GET_TYPEFACE_PERMISSION);
						}
						else
						{
							getTypeFace(pre);
						}
					}
					else
					{
						getTypeFace(pre);
					}
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
                                SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
                                setdata.edit().putInt("Language", language_choice).commit();
                                FloatManageMethod.LanguageSet(GlobalSetActivity.this, language_choice);
                                pre.setSummary(getString(R.string.xml_global_language_sum) + lan_list[language_choice]);
                                FloatManageMethod.restartApplication(GlobalSetActivity.this);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                            public void onClick (DialogInterface d, int i)
                            {
                                SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
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
						FloatManageMethod.setWinManager(GlobalSetActivity.this);
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
					Intent asservice = new Intent(GlobalSetActivity.this, FloatAdvanceTextUpdateService.class);
					Intent notifyservice = new Intent(GlobalSetActivity.this, FloatNotificationListenerService.class);
                    if ((boolean)p2)
                    {
                        startService(service);
						startService(asservice);
						startService(notifyservice);
                    }
                    else
                    {
                        stopService(service);
						stopService(asservice);
						stopService(notifyservice);
                    }
                    return true;
                }
            });
		Preference adts = findPreference("AdvanceTextService");
		setADTsum(adts);
		adts.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
			{
				public boolean onPreferenceClick (Preference p)
				{
					Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
					startActivityForResult(intent, ADVANCE_TEXT_SET);
					return true;
				}
			});
		Preference nous = findPreference("NotificationListenerService");
		if (Build.VERSION.SDK_INT < 18)
		{
			nous.setEnabled(false);
		}
		else
		{
			setNOSsum(nous);
			nous.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
					public boolean onPreferenceClick (Preference p)
					{
						if (Build.VERSION.SDK_INT >= 18)
						{
							Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
							startActivityForResult(intent, ADVANCE_TEXT_NOTIFICATION_SET);
						}
						return true;
					}
				});
		}
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

	private void getTypeFace (final Preference pre)
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
					SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
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
					FloatManageMethod.restartApplication(GlobalSetActivity.this);
				}
			})
			.setNegativeButton(R.string.cancel, null);
		pathselect.show();
	}

	public static boolean isAccessibilitySettingsOn (Context context)
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

	public static boolean isNotificationListenerEnabled (Context ctx)
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

	private void setADTsum (Preference p)
	{
		if (isAccessibilitySettingsOn(this))
		{
			p.setSummary(getString(R.string.status) + getString(R.string.on) + "\n" + getString(R.string.xml_global_service_advancetext_sum));
		}
		else
		{
			FloatManageMethod.setWinManager(this);
			p.setSummary(getString(R.string.status) + getString(R.string.off) + "\n" + getString(R.string.xml_global_service_advancetext_sum));
		}
	}

	private void setNOSsum (Preference p)
	{
		if (isNotificationListenerEnabled(this))
		{
			p.setSummary(getString(R.string.status) + getString(R.string.on) + "\n" + getString(R.string.xml_global_service_notificationtext_sum));
		}
		else
		{
			p.setSummary(getString(R.string.status) + getString(R.string.off) + "\n" + getString(R.string.xml_global_service_notificationtext_sum));
		}
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

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data)
	{
		if (requestCode == FLOAT_TEXT_GET_TYPEFACE_PERMISSION)
		{
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
			{
				getTypeFace(findPreference("TextTypeface"));
			}
		}
		if (requestCode == ADVANCE_TEXT_SET)
		{
			Preference adts = findPreference("AdvanceTextService");
			setADTsum(adts);
		}
		else if (requestCode == ADVANCE_TEXT_NOTIFICATION_SET)
		{
			Preference nous = findPreference("NotificationListenerService");
			setNOSsum(nous);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
