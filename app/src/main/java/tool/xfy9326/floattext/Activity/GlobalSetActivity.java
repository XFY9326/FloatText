package tool.xfy9326.floattext.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import tool.xfy9326.floattext.FileSelector.SelectFile;
import tool.xfy9326.floattext.Method.ActivityMethod;
import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.Method.IOMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Service.FloatAdvanceTextUpdateService;
import tool.xfy9326.floattext.Service.FloatNotificationListenerService;
import tool.xfy9326.floattext.Service.FloatTextUpdateService;
import tool.xfy9326.floattext.Service.FloatWindowStayAliveService;
import tool.xfy9326.floattext.Tool.FormatArrayList;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.Utils.FloatData;
import tool.xfy9326.floattext.Utils.StaticNum;

public class GlobalSetActivity extends AppCompatPreferenceActivity {
    private String default_typeface;
    private int typeface_choice, language_choice;
	private String[] AppNames, PkgNames;
	private boolean[] AppState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.global_settings);
		sethome();
        ViewSet();
		ServiceViewSet();
		DataViewSet();
    }

	//工具栏和返回按键设置
	private void sethome() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
	}

    private void ViewSet() {
		//通知栏控制
		CheckBoxPreference notify = (CheckBoxPreference) findPreference("FloatNotification");
        notify.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2) {
                    StayAliveSet(false);
					StayAliveSet(true);
                    return true;
                }
            });
		//跑马灯模式
        CheckBoxPreference movemethod = (CheckBoxPreference) findPreference("TextMovingMethod");
        movemethod.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2) {
                    ((App)getApplicationContext()).setMovingMethod((boolean)p2);
                    Toast.makeText(GlobalSetActivity.this, R.string.restart_to_apply, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
		//字体
        Preference typeface = findPreference("TextTypeface");
        final SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
        default_typeface = setdata.getString("DefaultTTFName", "Default");
        if (default_typeface.equalsIgnoreCase("Default")) {
            default_typeface = getString(R.string.text_default_typeface);
        }
        typeface.setSummary(getString(R.string.xml_global_text_typeface_summary) + default_typeface);
        typeface.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference pre) {
					DataAction(0, pre, StaticNum.FLOAT_TEXT_GET_TYPEFACE_PERMISSION);
                    return true;
                }
            });
		//语言
        Preference language = findPreference("Language");
        final String[] lan_list = getResources().getStringArray(R.array.language_list);
        language_choice = setdata.getInt("Language", 0);
        language.setSummary(getString(R.string.xml_global_language_sum) + lan_list[language_choice]);
        language.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference pre) {
                    LanguageSet(pre, lan_list);
                    return true;
                }
            });
		//动态变量刷新时间
		Preference dynamictime = findPreference("DynamicTime");
		dynamictime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				public boolean onPreferenceClick(Preference p) {
					DynamicTimeSet(setdata);
					return true;
				}
			});
		//实验性功能
        CheckBoxPreference develop = (CheckBoxPreference) findPreference("DevelopMode");
        develop.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2) {
                    ((App)getApplicationContext()).setDevelopMode((boolean)p2);
                    return true;
                }
            });
		//HTML代码功能
        CheckBoxPreference html = (CheckBoxPreference) findPreference("HtmlMode");
        html.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2) {
                    ((App)getApplicationContext()).setHtmlMode((boolean)p2);
                    Toast.makeText(GlobalSetActivity.this, R.string.restart_to_apply, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
		//管理列表单行文字
        CheckBoxPreference hidetext = (CheckBoxPreference) findPreference("ListTextHide");
        hidetext.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2) {
                    ((App)getApplicationContext()).setListTextHide((boolean)p2);
                    return true;
                }
            });
		//窗口过滤器
		Preference filter = findPreference("WinFilter");
		filter.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				public boolean onPreferenceClick(Preference p) {
					FilterSet(setdata);
					return true;
				}
			});
		//文字过滤器
		CheckBoxPreference textfilter = (CheckBoxPreference) findPreference("TextFilter");
        textfilter.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2) {
                    ((App)getApplicationContext()).setTextFilter((boolean)p2);
                    return true;
                }
            });
		//文字过滤器帮助
		Preference textfilterhelp = findPreference("TextFilterHelp");
		textfilterhelp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				public boolean onPreferenceClick(Preference p) {
					TextFilterHelpGet(GlobalSetActivity.this);
					return true;
				}
			});
		//通知栏图标设置
		CheckBoxPreference notifyicon = (CheckBoxPreference) findPreference("FloatNotificationIcon");
        notifyicon.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2) {
                    StayAliveSet(false);
					StayAliveSet(true);
                    return true;
                }
            });
    }
	private void ServiceViewSet() {
		//进程守护服务
		CheckBoxPreference stayalive = (CheckBoxPreference) findPreference("StayAliveService");
        stayalive.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2) {
                    StayAliveSet((boolean)p2);
                    return true;
                }
            });
		//动态变量服务
        CheckBoxPreference dynamicnum = (CheckBoxPreference) findPreference("DynamicNumService");
        dynamicnum.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2) {
                    DymanicSet((boolean)p2);
                    return true;
                }
            });
		//高级功能服务
		Preference adts = findPreference("AdvanceTextService");
		setADTsum(adts);
		adts.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
			{
				public boolean onPreferenceClick(Preference p) {
					Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
					startActivityForResult(intent, StaticNum.ADVANCE_TEXT_SET);
					return true;
				}
			});
		//通知监听服务
		Preference nous = findPreference("NotificationListenerService");
		if (Build.VERSION.SDK_INT < 18) {
			nous.setEnabled(false);
		} else {
			setNOSsum(nous);
			nous.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
					public boolean onPreferenceClick(Preference p) {
						if (Build.VERSION.SDK_INT >= 18) {
							Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
							startActivityForResult(intent, StaticNum.ADVANCE_TEXT_NOTIFICATION_SET);
						}
						return true;
					}
				});
		}
		//自定义动态变量
		Preference dynamicwordaddon = findPreference("DynamicWordAddon");
		dynamicwordaddon.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				public boolean onPreferenceClick(Preference p) {
					DynamicAddonHelpGet(GlobalSetActivity.this);
					return true;
				}
			});
	}

	private void DynamicTimeSet(final SharedPreferences sp) {
		int num = sp.getInt("DynamicReloadTime", 1000);
		LayoutInflater inflater = LayoutInflater.from(GlobalSetActivity.this);
		View view = inflater.inflate(R.layout.dialog_text, null);
		AlertDialog.Builder set = new AlertDialog.Builder(this);
		set.setTitle(R.string.xml_global_dynamicword_reload_time);
		final EditText et = (EditText) view.findViewById(R.id.dialog_text_edittext);
		et.setText(String.valueOf(num));
		et.setKeyListener(new DigitsKeyListener(false, true));
		set.setPositiveButton(R.string.done, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface d, int i) {
					String str = et.getText().toString();
					if (!str.isEmpty()) {
						int get = Integer.valueOf(str);
						if (get < 500) {
							Toast.makeText(GlobalSetActivity.this, R.string.num_err, Toast.LENGTH_SHORT).show();
						} else {
							sp.edit().putInt("DynamicReloadTime", get).commit();
							Toast.makeText(GlobalSetActivity.this, R.string.restart_to_apply, Toast.LENGTH_LONG).show();
						}
					}
				}
			});
		set.setNegativeButton(R.string.cancel, null);
		set.setView(view);
		set.show();
	}

	private void DataViewSet() {
		Preference backup = findPreference("DataBackup");
		backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				public boolean onPreferenceClick(Preference p) {
					DataAction(2, p, StaticNum.FLOAT_TEXT_GET_BACKUP_PERMISSION);
					return true;
				}
			});
		Preference recover = findPreference("DataRecover");
		recover.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
				public boolean onPreferenceClick(Preference p) {
					DataAction(1, p, StaticNum.FLOAT_TEXT_GET_RECOVER_PERMISSION);
					return true;
				}
			});
	}

	private void DynamicAddonHelpGet(Context ctx) {
		String[] paths = new String[]{"HELPS/Dynamic_Words_Addon_cn.txt", "HELPS/Dynamic_Words_Addon_tw.txt", "HELPS/Dynamic_Words_Addon_en.txt"};
		String path = MultiLanguageSet(ctx, paths);
		String str = IOMethod.readAssets(ctx, path);
		SetHelpDialog(ctx, R.string.xml_global_service_dynamicaddon, str);
	}

	private void TextFilterHelpGet(Context ctx) {
		String[] paths = new String[]{"HELPS/TextFilter_cn.txt", "HELPS/TextFilter_tw.txt", "HELPS/TextFilter_en.txt"};
		String path = MultiLanguageSet(ctx, paths);
		String str = IOMethod.readAssets(ctx, path);
		SetHelpDialog(ctx, R.string.xml_global_text_filter, str);
	}

	private void SetHelpDialog(Context ctx, int title, String str) {
		AlertDialog.Builder help = new AlertDialog.Builder(ctx);
		help.setTitle(title);
		help.setMessage(str);
		help.setPositiveButton(R.string.done, null);
		help.show();
	}

	//语言设置
	private String MultiLanguageSet(Context ctx, String[] arr) {
		String path = arr[2];
		String locale = ctx.getResources().getConfiguration().locale.getCountry();
		if (locale.equals(Locale.SIMPLIFIED_CHINESE.getCountry())) {
			path = arr[0];
		} else if (locale.equals(Locale.TAIWAN.getCountry())) {
			path = arr[1];
		} else if (locale.equals(Locale.ENGLISH.getCountry())) {
			path = arr[2];
		}
		return path;
	}

	private void DataAction(int type, Preference pre, int requestcode) {
		if (Build.VERSION.SDK_INT > 22) {
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestcode);
			} else {
				if (type == 0) {
					getTypeFace(pre);
				} else if (type == 1) {
					recoverdata(null);
				} else if (type == 2) {
					backupdata();
				}
			}
		} else {
			if (type == 0) {
				getTypeFace(pre);
			} else if (type == 1) {
				recoverdata(null);
			} else if (type == 2) {
				backupdata();
			}
		}
	}

	private void StayAliveSet(boolean b) {
		((App)getApplicationContext()).setStayAliveService(b);
		Intent service = new Intent(GlobalSetActivity.this, FloatWindowStayAliveService.class);
		if (b) {
			startService(service);
		} else {
			FloatManageMethod.setWinManager(GlobalSetActivity.this);
			stopService(service);
		}
	}

	private void DymanicSet(boolean b) {
		((App)getApplicationContext()).setDynamicNumService(b);
		Intent service = new Intent(GlobalSetActivity.this, FloatTextUpdateService.class);
		Intent asservice = new Intent(GlobalSetActivity.this, FloatAdvanceTextUpdateService.class);
		Intent notifyservice = new Intent(GlobalSetActivity.this, FloatNotificationListenerService.class);
		if (b) {
			startService(service);
			startService(asservice);
			startService(notifyservice);
		} else {
			stopService(service);
			stopService(asservice);
			stopService(notifyservice);
		}
	}

	private void FilterSet(final SharedPreferences setdata) {
		final ArrayList<String> FilterApplication = FormatArrayList.StringToStringArrayList(setdata.getString("Filter_Application", "[]"));
		getAppInfo(GlobalSetActivity.this, FilterApplication);
		AlertDialog.Builder alert = new AlertDialog.Builder(GlobalSetActivity.this)
			.setTitle(R.string.xml_global_win_filter)
			.setMultiChoiceItems(AppNames, AppState, new DialogInterface.OnMultiChoiceClickListener(){
				public void onClick(DialogInterface d, int i, boolean b) {
					AppState[i] = b;
				}
			})
			.setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d, int i) {
					FilterApplication.clear();
					for (int a = 0; a < AppState.length; a ++) {
						if (AppState[a]) {
							FilterApplication.add(PkgNames[a]);
						}
					}
					((App)getApplicationContext()).getFrameutil().setFilterApplication(FilterApplication);
					SharedPreferences.Editor ed = setdata.edit();
					ed.putString("Filter_Application", FilterApplication.toString());
					ed.commit();
				}
			})
			.setNegativeButton(R.string.cancel, null);
		alert.show();
	}

	private void LanguageSet(final Preference pre, final String[] lan_list) {
		AlertDialog.Builder lan = new AlertDialog.Builder(GlobalSetActivity.this)
			.setTitle(R.string.xml_global_language)
			.setSingleChoiceItems(lan_list, language_choice, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					language_choice = which;
				}
			})
			.setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface p1, int p2) {
					SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
					setdata.edit().putInt("Language", language_choice).commit();
					FloatManageMethod.LanguageSet(GlobalSetActivity.this, language_choice);
					pre.setSummary(getString(R.string.xml_global_language_sum) + lan_list[language_choice]);
					FloatManageMethod.restartApplication(GlobalSetActivity.this, getPackageManager().getLaunchIntentForPackage(getPackageName()));
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d, int i) {
					SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
					language_choice = setdata.getInt("Language", 0);
				}
			});
		lan.show();
	}

	private void recoverdata(String path) {
		if (path == null) {
			SelectFile sf = new SelectFile(StaticNum.FLOAT_TEXT_SELECT_RECOVER_FILE, SelectFile.TYPE_ChooseFile);
			sf.setFileType("ftbak");
			sf.start(GlobalSetActivity.this);
		} else {
			FloatData fd = new FloatData(GlobalSetActivity.this);
			if (fd.InputData(path)) {
				final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
				intent.putExtra("RecoverText", 1);
				FloatManageMethod.restartApplication(GlobalSetActivity.this, intent);
			} else {
				Toast.makeText(GlobalSetActivity.this, R.string.recover_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void backupdata() {
		if (((App)getApplicationContext()).getFloatText().size() == 0) {
			Toast.makeText(GlobalSetActivity.this, R.string.backup_nofound, Toast.LENGTH_SHORT).show();
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String path = Environment.getExternalStorageDirectory().toString() + "/FloatText/Backup/FloatText>" + sdf.format(new Date()) + ".ftbak";
			FloatData fd = new FloatData(GlobalSetActivity.this);
			if (fd.OutputData(path, ActivityMethod.getVersionCode(GlobalSetActivity.this))) {
				Toast.makeText(GlobalSetActivity.this, getString(R.string.backup_success) + path, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(GlobalSetActivity.this, R.string.backup_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void getTypeFace(final Preference pre) {
		File path = new File(Environment.getExternalStorageDirectory().toString() + "/FloatText/TTFs");
		if (!path.exists()) {
			path.mkdirs();
		}
		File[] files = path.listFiles();
		int defaultchoice = 0;
		ArrayList<String> ttfs = new ArrayList<String>();
		for (int i = 0; i < files.length;i++) {
			String str = files[i].getName().toString();
			String extra = ActivityMethod.getExtraName(str);
			if (extra.equalsIgnoreCase("ttf")) {
				String realname = str.substring(0, str.length() - 4);
				ttfs.add(realname);
				if (realname.equalsIgnoreCase(default_typeface)) {
					defaultchoice = i + 1;
				}
			}
		}
		typeface_choice = defaultchoice;
		ttfs.add(0, getString(R.string.text_default_typeface));
		final String[] ttfname = new String[ttfs.size()];
		for (int i = 0; i < ttfs.size(); i++) {
			ttfname[i] = ttfs.get(i);
		}
		AlertDialog.Builder pathselect = new AlertDialog.Builder(GlobalSetActivity.this)
			.setTitle(R.string.text_choose_typeface)
			.setSingleChoiceItems(ttfname, defaultchoice, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface p1, int p2) {
					typeface_choice = p2;
				}
			})
			.setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface p1, int p2) {
					SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
					if (typeface_choice == 0) {
						setdata.edit().putString("DefaultTTFName", "Default").commit();
						pre.setSummary(getString(R.string.xml_global_text_typeface_summary) + getString(R.string.text_default_typeface));
						default_typeface = "Default";
					} else {
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

	private void setADTsum(Preference p) {
		Preference filter = findPreference("WinFilterSwitch");
		if (ActivityMethod.isAccessibilitySettingsOn(this)) {
			filter.setEnabled(true);
			p.setSummary(getString(R.string.status) + getString(R.string.on) + "\n" + getString(R.string.xml_global_service_advancetext_sum));
		} else {
			filter.setEnabled(false);
			FloatManageMethod.setWinManager(this);
			p.setSummary(getString(R.string.status) + getString(R.string.off) + "\n" + getString(R.string.xml_global_service_advancetext_sum));
		}
	}

	private void setNOSsum(Preference p) {
		if (ActivityMethod.isNotificationListenerEnabled(this)) {
			p.setSummary(getString(R.string.status) + getString(R.string.on) + "\n" + getString(R.string.xml_global_service_notificationtext_sum));
		} else {
			p.setSummary(getString(R.string.status) + getString(R.string.off) + "\n" + getString(R.string.xml_global_service_notificationtext_sum));
		}
	}

	private void getAppInfo(Context ctx, ArrayList<String> PkgHave) {
		PackageManager pm = ctx.getPackageManager();
		List<PackageInfo> info = pm.getInstalledPackages(0);
		String FloatTextPkgName = ctx.getPackageName();
		ActivityMethod.orderPackageList(ctx, info);
		AppNames = new String[info.size() - 1];
		PkgNames = new String[info.size() - 1];
		AppState = new boolean[info.size() - 1];
		int countnum = 0;
		for (int i = 0; i < info.size();i++) {
			String pkgname = info.get(i).packageName;
			if (!pkgname.equalsIgnoreCase(FloatTextPkgName)) {
				AppNames[countnum] = info.get(i).applicationInfo.loadLabel(ctx.getPackageManager()).toString();
				PkgNames[countnum] = pkgname;
				if (PkgHave.contains(pkgname)) {
					AppState[countnum] = true;
				} else {
					AppState[countnum] = false;
				}
				countnum++;
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == StaticNum.FLOAT_TEXT_GET_TYPEFACE_PERMISSION) {
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				getTypeFace(findPreference("TextTypeface"));
			} else {
				Toast.makeText(this, R.string.premission_error, Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == StaticNum.FLOAT_TEXT_GET_BACKUP_PERMISSION) {
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				backupdata();
			} else {
				Toast.makeText(this, R.string.premission_error, Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == StaticNum.FLOAT_TEXT_GET_RECOVER_PERMISSION) {
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				recoverdata(null);
			} else {
				Toast.makeText(this, R.string.premission_error, Toast.LENGTH_SHORT).show();
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == StaticNum.ADVANCE_TEXT_SET) {
			Preference adts = findPreference("AdvanceTextService");
			setADTsum(adts);
		} else if (requestCode == StaticNum.ADVANCE_TEXT_NOTIFICATION_SET) {
			Preference nous = findPreference("NotificationListenerService");
			setNOSsum(nous);
		} else if (requestCode == StaticNum.FLOAT_TEXT_SELECT_RECOVER_FILE) {
			if (data != null) {
				String str = data.getStringExtra("FilePath");
				recoverdata(str);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
