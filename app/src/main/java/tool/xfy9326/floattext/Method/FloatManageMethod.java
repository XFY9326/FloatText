package tool.xfy9326.floattext.Method;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import android.support.design.widget.*;
import android.view.*;
import java.util.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Service.*;
import tool.xfy9326.floattext.Setting.*;
import tool.xfy9326.floattext.Utils.*;
import tool.xfy9326.floattext.View.*;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import tool.xfy9326.floattext.Activity.GlobalSetActivity;
import tool.xfy9326.floattext.FileSelector.SelectFile;

public class FloatManageMethod
{
	public static boolean waitdoubleclick = false;
	public static Handler waithandle;
	public static Runnable waitrun;

	//导入或导出的权限检查
	public static void TextFileSolve(Activity ctx, int type, int requestcode)
	{
		if (Build.VERSION.SDK_INT > 22)
		{
			if (ctx.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
			{
				ctx.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestcode);
			}
			else
			{
				if (type == 0)
				{
					FloatManageMethod.exporttxt(ctx);
				}
				else if (type == 1)
				{
					FloatManageMethod.selectFile(ctx);
				}
			}
		}
		else
		{
			if (type == 0)
			{
				FloatManageMethod.exporttxt(ctx);
			}
			else if (type == 1)
			{
				FloatManageMethod.selectFile(ctx);
			}
		}
	}

	//根Activity判断
	public static void RootTask(Activity act)
	{
		if (!act.isTaskRoot())
		{
            act.finish();
        }
	}

	//重启应用
	public static void restartApplication(Context ctx, Intent intent)
	{
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		if (Build.VERSION.SDK_INT >= 21)
		{
			ctx.startActivity(intent);
		}
		else
		{
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			ctx.startActivity(intent);
		}
		System.exit(0);
	}

	//设置加载窗口
    public static AlertDialog setLoadingDialog(Context ctx)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);  
        View layout = inflater.inflate(R.layout.dialog_loading, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx)
            .setView(layout)
            .setCancelable(false);
        AlertDialog ag = dialog.create();
        return ag;
    }

	//导入文本
    public static boolean importtxt(Context ctx, String path)
    {
        File file = new File(path);
        App utils = (App)ctx.getApplicationContext();
        String[] lines = IOMethod.readfile(file);
        ArrayList<String> fixline = new ArrayList<String>();
        for (int i = 0;i < lines.length;i++)
        {
            if (!lines.toString().isEmpty() && !lines[i].toString().replaceAll("\\s+", "").equalsIgnoreCase("") && !lines[i].toString().replace(" ", "").replace("\n", "").equals(""))
            {
                fixline.add(lines[i]);
            }
        }
        if (fixline.size() != 0 && fixline.size() < 100)
        {
            ArrayList<String> data = utils.getTextData();
            for (int a = 0;a < fixline.size();a++)
            {
                data.add(fixline.get(a).toString());
            }
            closeAllWin(ctx);
            FloatData dat = new FloatData(ctx);
            dat.savedata();
            dat.getSaveArrayData();
            dat.savedata();
            return true;
        }
        else
        {
            return false;
        }
    }

	//输出文本
    public static void exporttxt(Context ctx)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String path = Environment.getExternalStorageDirectory().toString() + "/FloatText/Export/FloatText>" + sdf.format(new Date()) + ".txt";
        String str = "";
        ArrayList<String> str_arr = ((App)ctx.getApplicationContext()).getTextData();
        for (int i = 0;i < str_arr.size();i++)
        {
            str += str_arr.get(i).toString() + "\n";
        }
        if (!str.replace("\n", "").replaceAll("\\s+", "").equalsIgnoreCase(""))
        {
            IOMethod.writefile(path, str);
            Toast.makeText(ctx, ctx.getString(R.string.text_export_success) + path, Toast.LENGTH_LONG).show();
        }
        else
        {
            FloatManage.snackshow((Activity)ctx, ctx.getString(R.string.text_export_error));
        }
    }

	//选择文件
	public static void selectFile(Activity ctx)
	{
		Toast.makeText(ctx, R.string.text_import_notice, Toast.LENGTH_LONG).show();
		SelectFile sf = new SelectFile(StaticNum.FLOAT_TEXT_IMPORT_CODE, SelectFile.TYPE_ChooseFile);
		sf.start(ctx);
	}

	//关闭所有窗口
    public static void closeAllWin(Context ctx)
    {
        WindowManager wm =((App)ctx.getApplicationContext()).getFloatwinmanager();
        ArrayList<FloatLinearLayout> layout =  ((App)ctx.getApplicationContext()).getFrameutil().getFloatlinearlayout();
        ArrayList<Boolean> show =  ((App)ctx.getApplicationContext()).getTextutil().getShowFloat();
        for (int i = 0;i < layout.size();i++)
        {
            if (show.get(i))
            {
                wm.removeView(layout.get(i));
            }
        }
    }

	//动态变量列表
	public static void showDList(final Activity ctx)
	{
		if (((App)ctx.getApplicationContext()).DynamicNumService)
		{
			final String[] dynamiclist = ctx.getResources().getStringArray(R.array.floatsetting_dynamic_list);
			String[] dynamicname = ctx.getResources().getStringArray(R.array.floatsetting_dynamic_name);
			String[] result = new String[dynamiclist.length + 1];
			for (int i = 0;i < dynamiclist.length;i++)
			{
				result[i] = "<" + dynamiclist[i] + ">" + "\n" + dynamicname[i];
			}
			result[dynamiclist.length] = ctx.getString(R.string.dynamic_num_tip);
			AlertDialog.Builder list = new AlertDialog.Builder(ctx)
				.setTitle(R.string.dynamic_list_title)
				.setItems(result, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface d, int i)
					{
						if (i != dynamiclist.length)
						{
							ClipboardManager clip = (ClipboardManager)ctx.getSystemService(Context.CLIPBOARD_SERVICE);
							if (((App)ctx.getApplicationContext()).HtmlMode)
							{
								clip.setText("#" + dynamiclist[i] + "#");
							}
							else
							{
								clip.setText("<" + dynamiclist[i] + ">");
							}
							Toast.makeText(ctx, R.string.copy_ok, Toast.LENGTH_SHORT).show();
						}
					}
				});
			list.show();
		}
		else
		{
			FloatManage.snackshow(ctx, ctx.getString(R.string.dynamicservice_no_open));
		}
	}

	//权限提示
	public static void notifypermission(final Activity ctx)
	{
		if (Build.VERSION.SDK_INT >= 23)
		{
			if (!Settings.canDrawOverlays(ctx))
			{
				ctx.runOnUiThread(new Runnable(){
						public void run()
						{
							CoordinatorLayout cl = (CoordinatorLayout) ctx.findViewById(R.id.FloatManage_MainLayout);
							Snackbar.make(cl, R.string.no_premission, Snackbar.LENGTH_SHORT).setAction(R.string.get_premission, new OnClickListener(){
									public void onClick(View v)
									{
										Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
										intent.setData(Uri.parse("package:" + ctx.getPackageName()));
										ctx.startActivity(intent);
									}
								}).setActionTextColor(Color.RED).show();

						}
					});
			}
		}
	}

	//权限判断与悬浮窗重现
    public static Thread PrepareSave(Activity ctx, Handler han)
    {
		if (Build.VERSION.SDK_INT >= 23)
		{
			if (!Settings.canDrawOverlays(ctx))
			{
				FloatManageMethod.askforpermission(ctx, StaticNum.RESHOW_PERMISSION_RESULT_CODE);
			}
			else
			{
				FloatManageMethod.delayaskforpermission(ctx);
				if (((App)ctx.getApplicationContext()).getTextData().size() > 0)
				{
					return FloatManageMethod.Reshow(ctx, han);
				}
				else
				{
					FloatData fd = new FloatData(ctx);
					fd.savedata();
				}
			}
		}
		else
		{
			if (((App)ctx.getApplicationContext()).getTextData().size() > 0)
			{
                return FloatManageMethod.Reshow(ctx, han);
            }
			else
			{
				FloatData fd = new FloatData(ctx);
				fd.savedata();
			}
        }
        return null;
    }

	//关闭应用
	public static void ShutSown(Activity ctx)
	{
		FloatManageMethod.stopservice(ctx);
		App utils = (App)ctx.getApplicationContext();
		utils.setGetSave(false);
		utils.setFloatReshow(true);
		FloatManageMethod.closeAllWin(ctx);
		ctx.finish();
		System.gc();
		new Handler().postDelayed(new Runnable(){  
                public void run()
                {
                    System.exit(0);
                }  
            }, 100); 
	}

	//后台运行应用
	public static void RunInBack(Activity ctx)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		if (sp.getBoolean("WinOnlyShowInHome", false))
		{
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			ctx.startActivity(intent);
		}
		else
		{
			ctx.moveTaskToBack(true);
		}
	}

	//关闭应用控制(双击)
    public static void CloseApp(final Activity ctx)
    {
		if (waitdoubleclick)
		{
			ShutSown(ctx);
		}
		else
		{
			CoordinatorLayout cl = (CoordinatorLayout) ctx.findViewById(R.id.FloatManage_MainLayout);
			Snackbar.make(cl, R.string.exit_title, Snackbar.LENGTH_LONG).setAction(R.string.back_to_launcher, new OnClickListener(){
					public void onClick(View v)
					{
						RunInBack(ctx);
					}
				}).setCallback(new Snackbar.Callback(){
					@Override
					public void onDismissed(Snackbar bar, int i)
					{
						waitdoubleclick = false;
						if (waithandle != null && waitrun != null)
						{
							waithandle.removeCallbacks(waitrun);
						}
						super.onDismissed(bar , i);
					}
				}).
				setActionTextColor(Color.RED).show();
			waitdoubleclick = true;
			waithandle = new Handler();
			waitrun = new Runnable(){   
				public void run()
				{
					waitdoubleclick = false;
					waithandle = null;
					waitrun = null;
				}
			};
			waithandle.postDelayed(waitrun, 2200);
		}
    }

	//设置WindowManager
    public static void setWinManager(Context ctx)
    {
        WindowManager wm = (WindowManager)ctx.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        ((App)ctx.getApplicationContext()).setFloatwinmanager(wm);
    }

	//首次请求权限
    public static void first_ask_for_premission(final Context ctx)
    {
        SharedPreferences setdata = ctx.getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
        SharedPreferences.Editor setedit = setdata.edit();
        if (!setdata.getBoolean("FirstUse_AskForPremission", false))
        {
            AlertDialog.Builder asp = new AlertDialog.Builder(ctx)
                .setTitle(R.string.ask_for_premission)
                .setMessage(R.string.ask_for_premission_alert)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface d, int i)
                    {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + ctx.getPackageName()));
                        ctx.startActivity(intent);
                    }
                });
            asp.show();
            setedit.putBoolean("FirstUse_AskForPremission", true);
            setedit.commit();
        }
    }

	//准备文件夹
    public static void preparefolder()
    {
        String typeface_path = Environment.getExternalStorageDirectory().toString() + "/FloatText/TTFs";
        File typeface = new File(typeface_path);
        if (!typeface.exists())
        {
            typeface.mkdirs();
        }
    }

	//延迟请求权限
    public static void delayaskforpermission(final Activity act)
    {
        new Handler().postDelayed(new Runnable(){  
                public void run()
                {
                    FloatManageMethod.notifypermission(act);
                }  
            }, 2000); 
    }

	//请求权限
    public static void askforpermission(final Activity act, int code)
    {
        final int askcode = code;
        final Activity activity = act;
        final Context context = act;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
            .setTitle(R.string.ask_for_premission)
            .setMessage(act.getString(R.string.ask_for_premisdion_msg) + act.getString(R.string.reshow_msg))
            .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface p1, int p2)
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    activity.startActivityForResult(intent, askcode);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface p1, int p2)
                {
                    act.finish();
                    System.exit(0);
                }
            })
            .setCancelable(false);
        dialog.show();
    }

	//读取并且设置应用所有数据
    public static Thread getSaveData(final Context ctx, final App utils, final SharedPreferences spdata, final Handler han)
    {
        Thread thread = new Thread() {
            public void run()
            {
                FloatData dat = new FloatData(ctx);
                dat.getSaveArrayData();
                utils.setMovingMethod(spdata.getBoolean("TextMovingMethod", false));
                utils.setStayAliveService(spdata.getBoolean("StayAliveService", false));
                utils.setDynamicNumService(spdata.getBoolean("DynamicNumService", false));
                utils.setDevelopMode(spdata.getBoolean("DevelopMode", false));
                utils.setHtmlMode(spdata.getBoolean("HtmlMode", true));
                utils.setListTextHide(spdata.getBoolean("ListTextHide", false));
				utils.setTextFilter(spdata.getBoolean("TextFilter", false));
                han.obtainMessage(0).sendToTarget();
            }};
        return thread;
    }

	//重现悬浮窗
    public static Thread Reshow(final Activity ctx, final Handler han)
    {
        Thread thread = new Thread() {
			public void run()
            {
                final App utils = (App)ctx.getApplicationContext();
				final FloatTextUtils textutils = utils.getTextutil();
				ctx.runOnUiThread(new Runnable(){
						public void run()
						{
							Reshow_Create(ctx, utils, textutils, textutils.getTextShow(), textutils.getShowFloat(), textutils.getLockPosition(), textutils.getPosition(), textutils.getTextMove());
							utils.getListviewadapter().notifyDataSetChanged();
						}
					});
                if (han != null)
                {
                    han.obtainMessage(1).sendToTarget();
                }
            }
        };
        return thread;
    }

	public static void Reshow_Create(Context ctx, App utils, FloatTextUtils textutils, ArrayList<String> Text, ArrayList<Boolean> Show, ArrayList<Boolean> Lock, ArrayList<String> Position, ArrayList<Boolean> Move)
	{
		if (Text.size() != 0)
		{
			WindowManager wm = utils.getFloatwinmanager();
			for (int i = 0;i < Text.size();i++)
			{
				FloatTextView fv = FloatTextSettingMethod.CreateFloatView(ctx, textutils, i);
				FloatLinearLayout fll = FloatTextSettingMethod.CreateLayout(ctx, i);
				fll.changeShowState(Show.get(i));
				String[] ptemp = new String[]{"100", "150"};
				if (Lock.get(i))
				{
					ptemp = Position.get(i).toString().split("_");
					fll.setAddPosition(Float.parseFloat(ptemp[0]), Float.parseFloat(ptemp[1]));
				}
				WindowManager.LayoutParams layout = FloatTextSettingMethod.CreateFloatLayout(ctx, wm, fv, fll, Float.parseFloat(ptemp[0]), Float.parseFloat(ptemp[1]), textutils, i);
				fll.setFloatLayoutParams(layout);
				fll.setPositionLocked(Lock.get(i));
				fll.setTop(textutils.getAutoTop().get(i));
				if (utils.getMovingMethod())
				{
					fv.setMoving(Move.get(i), 0);
				}
				else
				{
					fv.setMoving(Move.get(i), 1);
					if (Move.get(i))
					{
						fll.setShowState(false);
						fll.setShowState(true);
					}
				}
				FloatTextSettingMethod.savedata(ctx, fv, fll, Text.get(i), layout);
			}
		}
	}

	//字体文件检测
    public static void floattext_typeface_check(Context ctx, boolean alert)
    {
        SharedPreferences setdata = ctx.getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
        String filename = setdata.getString("DefaultTTFName", "Default");
        if (filename.equalsIgnoreCase("Default"))
        {
            return;
        }
        else
        {
            String typeface_path = Environment.getExternalStorageDirectory().toString() + "/FloatText/TTFs/" + filename + ".ttf";
            String typeface_path2 = Environment.getExternalStorageDirectory().toString() + "/FloatText/TTFs/" + filename + ".TTF";
            File f1 = new File(typeface_path);
            File f2 = new File(typeface_path2);
            if (f1.exists() || f2.exists())
            {
                return;
            }
        }
        setdata.edit().putString("DefaultTTFName", "Default").commit();
		if (alert)
		{
			FloatManage.snackshow((Activity)ctx, ctx.getString(R.string.text_typeface_err));
		}
    }

	//语言设置 数据
    public static void LanguageInit(Activity ctx)
    {
        SharedPreferences setdata = ctx.getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
        int lan = setdata.getInt("Language", 0);
        LanguageSet(ctx, lan);
    }

	//语言设置 分类
    public static void LanguageSet(Activity ctx, int i)
    {
        Resources resource = ctx.getResources();
        Configuration config = resource.getConfiguration();
        switch (i)
        {
            case 0:
                config.locale = Locale.getDefault();
                break;
            case 1:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case 2:
                config.locale = Locale.TAIWAN;
                break;
            case 3:
                config.locale = Locale.ENGLISH;
                break;
        }
        ctx.getBaseContext().getResources().updateConfiguration(config, null);
    }

	//启动服务
    public static void startservice(Context ctx)
    {
        if (((App)ctx.getApplicationContext()).getDynamicNumService())
        {
            Intent floatservice = new Intent(ctx, FloatTextUpdateService.class);
            ctx.startService(floatservice);
        }
        if (((App)ctx.getApplicationContext()).getStayAliveService())
        {
            Intent service = new Intent(ctx, FloatWindowStayAliveService.class);
            ctx.startService(service);
			if (GlobalSetActivity.isAccessibilitySettingsOn(ctx))
			{
				Intent asservice = new Intent(ctx, FloatAdvanceTextUpdateService.class);
				ctx.startService(asservice);
			}
			if (Build.VERSION.SDK_INT > 18)
			{
				if (GlobalSetActivity.isNotificationListenerEnabled(ctx))
				{
					Intent notifyservice = new Intent(ctx, FloatNotificationListenerService.class);
					ctx.startService(notifyservice);
				}
			}
        }
    }

	//关闭服务
    public static void stopservice(Activity ctx)
    {
        Intent service = new Intent(ctx, FloatWindowStayAliveService.class);
        ctx.stopService(service);
        Intent floatservice = new Intent(ctx, FloatTextUpdateService.class);
        ctx.stopService(floatservice);
		Intent asservice = new Intent(ctx, FloatAdvanceTextUpdateService.class);
		ctx.stopService(asservice);
		Intent notifyservice = new Intent(ctx, FloatNotificationListenerService.class);
		ctx.stopService(notifyservice);
    }

	//用户新建悬浮窗时的提示
    public static void addFloatWindow(final Activity ctx, final ArrayList<String> FloatDataName)
    {
        if (((App)ctx.getApplicationContext()).getDevelopMode())
        {
            String[] type = ctx.getResources().getStringArray(R.array.floatmanage_choose);
            AlertDialog.Builder choose = new AlertDialog.Builder(ctx)
                .setTitle(R.string.choose_float_type)
                .setItems(type, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {
                            Intent intent = new Intent(ctx, FloatTextSetting.class);
                            intent.putExtra("EditID", FloatDataName.size());
                            ctx.startActivityForResult(intent, StaticNum.FLOATTEXT_RESULT_CODE);
                        }
                        else if (which == 1)
                        {
                            Intent intent = new Intent(ctx, FloatWebSetting.class);
                            ctx.startActivity(intent);
                        }
                    }
                });
            choose.show();
        }
        else
        {
            Intent intent = new Intent(ctx, FloatTextSetting.class);
            intent.putExtra("EditID", FloatDataName.size());
            ctx.startActivityForResult(intent, StaticNum.FLOATTEXT_RESULT_CODE);
        }
    }

}
