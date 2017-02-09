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
import java.text.*;
import java.util.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.FileSelector.*;
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
	private String[] AppNames;
	private String[] PkgNames;
	private boolean[] AppState;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.global_settings);
		sethome();
        ViewSet();
		ServiceViewSet();
		DataViewSet();
    }

	private void sethome()
	{
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
		{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
	}

    private void ViewSet()
    {
        CheckBoxPreference movemethod = (CheckBoxPreference) findPreference("TextMovingMethod");
        movemethod.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    ((App)getApplicationContext()).setMovingMethod((boolean)p2);
                    Toast.makeText(GlobalSetActivity.this, R.string.restart_to_apply, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        Preference typeface = findPreference("TextTypeface");
        final SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
        default_typeface = setdata.getString("DefaultTTFName", "Default");
        if (default_typeface.equalsIgnoreCase("Default"))
        {
            default_typeface = getString(R.string.text_default_typeface);
        }
        typeface.setSummary(getString(R.string.xml_global_text_typeface_summary) + default_typeface);
        typeface.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference pre)
                {
					DataAction(0, pre, StaticNum.FLOAT_TEXT_GET_TYPEFACE_PERMISSION);
                    return true;
                }
            });
        Preference language = findPreference("Language");
        final String[] lan_list = getResources().getStringArray(R.array.language_list);
        language_choice = setdata.getInt("Language", 0);
        language.setSummary(getString(R.string.xml_global_language_sum) + lan_list[language_choice]);
        language.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference pre)
                {
                    LanguageSet(pre, lan_list);
                    return true;
                }
            });
        CheckBoxPreference develop = (CheckBoxPreference) findPreference("DevelopMode");
        develop.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    ((App)getApplicationContext()).setDevelopMode((boolean)p2);
                    return true;
                }
            });
        CheckBoxPreference html = (CheckBoxPreference) findPreference("HtmlMode");
        html.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    ((App)getApplicationContext()).setHtmlMode((boolean)p2);
                    Toast.makeText(GlobalSetActivity.this, R.string.restart_to_apply, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        CheckBoxPreference hidetext = (CheckBoxPreference) findPreference("ListTextHide");
        hidetext.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    ((App)getApplicationContext()).setListTextHide((boolean)p2);
                    return true;
                }
            });
        CheckBoxPreference onlyshowinhome = (CheckBoxPreference)findPreference("WinOnlyShowInHome");
        onlyshowinhome.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    HomeSet((boolean)p2);
                    return true;
                }
            });
		Preference filter = findPreference("WinFilter");
		filter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				public boolean onPreferenceClick(Preference p)
				{
					FilterSet(setdata);
					return true;
				}
			});

    }
	private void ServiceViewSet()
	{
		CheckBoxPreference stayalive = (CheckBoxPreference) findPreference("StayAliveService");
        stayalive.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    StayAliveSet((boolean)p2);
                    return true;
                }
            });
        CheckBoxPreference dynamicnum = (CheckBoxPreference) findPreference("DynamicNumService");
        dynamicnum.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    DymanicSet((boolean)p2);
                    return true;
                }
            });
		Preference adts = findPreference("AdvanceTextService");
		setADTsum(adts);
		adts.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
			{
				public boolean onPreferenceClick(Preference p)
				{
					Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
					startActivityForResult(intent,StaticNum.ADVANCE_TEXT_SET);
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
					public boolean onPreferenceClick(Preference p)
					{
						if (Build.VERSION.SDK_INT >= 18)
						{
							Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
							startActivityForResult(intent, StaticNum.ADVANCE_TEXT_NOTIFICATION_SET);
						}
						return true;
					}
				});
		}
	}

	private void DataViewSet()
	{
		Preference backup = findPreference("DataBackup");
		backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				public boolean onPreferenceClick(Preference p)
				{
					DataAction(2, p, StaticNum.FLOAT_TEXT_GET_BACKUP_PERMISSION);
					return true;
				}
			});
		Preference recover = findPreference("DataRecover");
		recover.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				public boolean onPreferenceClick(Preference p)
				{
					DataAction(1, p, StaticNum.FLOAT_TEXT_GET_RECOVER_PERMISSION);
					return true;
				}
			});
	}

	private void DataAction(int type, Preference pre, int requestcode)
	{
		if (Build.VERSION.SDK_INT > 22)
		{
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
			{
				requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestcode);
			}
			else
			{
				if (type == 0)
				{
					getTypeFace(pre);
				}
				else if (type == 1)
				{
					recoverdata(null);
				}
				else if (type == 2)
				{
					backupdata();
				}
			}
		}
		else
		{
			if (type == 0)
			{
				getTypeFace(pre);
			}
			else if (type == 1)
			{
				recoverdata(null);
			}
			else if (type == 2)
			{
				backupdata();
			}
		}
	}

	private void HomeSet(boolean b)
	{
		if (!b)
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
	}

	private void StayAliveSet(boolean b)
	{
		((App)getApplicationContext()).setStayAliveService(b);
		Intent service = new Intent(GlobalSetActivity.this, FloatWindowStayAliveService.class);
		if (b)
		{
			startService(service);
		}
		else
		{
			FloatManageMethod.setWinManager(GlobalSetActivity.this);
			stopService(service);
		}
	}

	private void DymanicSet(boolean b)
	{
		((App)getApplicationContext()).setDynamicNumService(b);
		Intent service = new Intent(GlobalSetActivity.this, FloatTextUpdateService.class);
		Intent asservice = new Intent(GlobalSetActivity.this, FloatAdvanceTextUpdateService.class);
		Intent notifyservice = new Intent(GlobalSetActivity.this, FloatNotificationListenerService.class);
		if (b)
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
	}

	private void FilterSet(final SharedPreferences setdata)
	{
		final ArrayList<String> FilterApplication = FloatData.StringToStringArrayList(setdata.getString("Filter_Application", "[]"));
		getAppInfo(GlobalSetActivity.this, FilterApplication);
		AlertDialog.Builder alert = new AlertDialog.Builder(GlobalSetActivity.this)
			.setTitle(R.string.xml_global_win_filter)
			.setMultiChoiceItems(AppNames, AppState, new DialogInterface.OnMultiChoiceClickListener(){
				public void onClick(DialogInterface d, int i, boolean b)
				{
					AppState[i] = b;
				}
			})
			.setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d, int i)
				{
					FilterApplication.clear();
					for (int a = 0; a < AppState.length; a ++)
					{
						if (AppState[a])
						{
							FilterApplication.add(PkgNames[a]);
						}
					}
					((App)getApplicationContext()).setFilterApplication(FilterApplication);
					SharedPreferences.Editor ed = setdata.edit();
					ed.putString("Filter_Application", FilterApplication.toString());
					ed.commit();
				}
			})
			.setNegativeButton(R.string.cancel, null);
		alert.show();
	}

	private void LanguageSet(final Preference pre, final String[] lan_list)
	{
		AlertDialog.Builder lan = new AlertDialog.Builder(GlobalSetActivity.this)
			.setTitle(R.string.xml_global_language)
			.setSingleChoiceItems(lan_list, language_choice, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which)
				{
					language_choice = which;
				}
			})
			.setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface p1, int p2)
				{
					SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
					setdata.edit().putInt("Language", language_choice).commit();
					FloatManageMethod.LanguageSet(GlobalSetActivity.this, language_choice);
					pre.setSummary(getString(R.string.xml_global_language_sum) + lan_list[language_choice]);
					FloatManageMethod.restartApplication(GlobalSetActivity.this, getPackageManager().getLaunchIntentForPackage(getPackageName()));
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d, int i)
				{
					SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
					language_choice = setdata.getInt("Language", 0);
				}
			});
		lan.show();
	}

	private void recoverdata(String path)
	{
		if (path == null)
		{
			SelectFile sf = new SelectFile(StaticNum.FLOAT_TEXT_SELECT_RECOVER_FILE, SelectFile.TYPE_ChooseFile);
			sf.setFileType("ftbak");
			sf.start(GlobalSetActivity.this);
		}
		else
		{
			FloatData fd = new FloatData(GlobalSetActivity.this);
			if (fd.InputData(path))
			{
				final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
				intent.putExtra("RecoverText", 1);
				FloatManageMethod.restartApplication(GlobalSetActivity.this, intent);
			}
			else
			{
				Toast.makeText(GlobalSetActivity.this, R.string.recover_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void backupdata()
	{
		if (((App)getApplicationContext()).getFloatText().size() == 0)
		{
			Toast.makeText(GlobalSetActivity.this, R.string.backup_nofound, Toast.LENGTH_SHORT).show();
		}
		else
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String path = Environment.getExternalStorageDirectory().toString() + "/FloatText/Backup/FloatText>" + sdf.format(new Date()) + ".ftbak";
			FloatData fd = new FloatData(GlobalSetActivity.this);
			if (fd.OutputData(path, AboutActivity.getVersionCode(GlobalSetActivity.this)))
			{
				Toast.makeText(GlobalSetActivity.this, getString(R.string.backup_success) + path, Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(GlobalSetActivity.this, R.string.backup_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void getTypeFace(final Preference pre)
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
				public void onClick(DialogInterface p1, int p2)
				{
					typeface_choice = p2;
				}
			})
			.setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface p1, int p2)
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
					FloatManageMethod.restartApplication(GlobalSetActivity.this, getPackageManager().getLaunchIntentForPackage(getPackageName()));
				}
			})
			.setNegativeButton(R.string.cancel, null);
		pathselect.show();
	}

	public static boolean isAccessibilitySettingsOn(Context context)
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

	public static boolean isNotificationListenerEnabled(Context ctx)
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

    private static String getExtraName(String filename)
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

	private void setADTsum(Preference p)
	{
		Preference filter = findPreference("WinFilterSwitch");
		if (isAccessibilitySettingsOn(this))
		{
			filter.setEnabled(true);
			p.setSummary(getString(R.string.status) + getString(R.string.on) + "\n" + getString(R.string.xml_global_service_advancetext_sum));
		}
		else
		{
			filter.setEnabled(false);
			FloatManageMethod.setWinManager(this);
			p.setSummary(getString(R.string.status) + getString(R.string.off) + "\n" + getString(R.string.xml_global_service_advancetext_sum));
		}
	}

	private void setNOSsum(Preference p)
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

	private void getAppInfo(Context ctx, ArrayList<String> PkgHave)
	{
		List<String> homes = FloatServiceMethod.getHomes(ctx);
		PackageManager pm = ctx.getPackageManager();
		List<PackageInfo> info = pm.getInstalledPackages(0);
		String FloatTextPkgName = ctx.getPackageName();
		orderList(ctx, info);
		int num = info.size() - homes.size() - 1;
		AppNames = new String[num];
		PkgNames = new String[num];
		AppState = new boolean[num];
		int countnum = 0;
		for (int i = 0; i < info.size();i++)
		{
			String pkgname = info.get(i).packageName;
			if (!homes.contains(pkgname) && !pkgname.equalsIgnoreCase(FloatTextPkgName))
			{
				AppNames[countnum] = info.get(i).applicationInfo.loadLabel(ctx.getPackageManager()).toString();
				PkgNames[countnum] = pkgname;
				if (PkgHave.contains(pkgname))
				{
					AppState[countnum] = true;
				}
				else
				{
					AppState[countnum] = false;
				}
				countnum++;
			}
		}
	}

	private static List<PackageInfo> orderList(final Context ctx, List<PackageInfo> list)
	{
		Collections.sort(list, new Comparator<PackageInfo>() {
                @Override
                public int compare(PackageInfo o1, PackageInfo o2)
                {
					String str1 = o1.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
					String str2 = o2.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
                    return str1.compareTo(str2);
                }
            });
		return list;
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

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if (requestCode == StaticNum.FLOAT_TEXT_GET_TYPEFACE_PERMISSION)
		{
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
			{
				getTypeFace(findPreference("TextTypeface"));
			}
		}
		else if (requestCode == StaticNum.FLOAT_TEXT_GET_BACKUP_PERMISSION)
		{
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
			{
				backupdata();
			}
		}
		else if (requestCode == StaticNum.FLOAT_TEXT_GET_RECOVER_PERMISSION)
		{
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
			{
				recoverdata(null);
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == StaticNum.ADVANCE_TEXT_SET)
		{
			Preference adts = findPreference("AdvanceTextService");
			setADTsum(adts);
		}
		else if (requestCode == StaticNum.ADVANCE_TEXT_NOTIFICATION_SET)
		{
			Preference nous = findPreference("NotificationListenerService");
			setNOSsum(nous);
		}
		else if (requestCode == StaticNum.FLOAT_TEXT_SELECT_RECOVER_FILE)
		{
			if (data != null)
			{
				String str = data.getStringExtra("FilePath");
				recoverdata(str);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
