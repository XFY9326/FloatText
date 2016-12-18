package tool.xfy9326.floattext.Method;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import tool.xfy9326.floattext.FloatManage;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Service.FloatTextUpdateService;
import tool.xfy9326.floattext.Service.FloatWindowStayAliveService;
import tool.xfy9326.floattext.Setting.FloatTextSetting;
import tool.xfy9326.floattext.Setting.FloatWebSetting;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.Utils.FloatData;
import tool.xfy9326.floattext.View.FloatLinearLayout;
import tool.xfy9326.floattext.View.FloatTextView;

public class FloatManageMethod
{
    public static AlertDialog setLoadingDialog (Context ctx)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);  
        View layout = inflater.inflate(R.layout.dialog_loading, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx)
            .setView(layout)
            .setCancelable(false);
        AlertDialog ag = dialog.create();
        return ag;
    }

    public static boolean importtxt (Context ctx, String path)
    {
        File file = new File(path);
        App utils = (App)ctx.getApplicationContext();
        String[] lines = IOMethod.readfile(file);
        ArrayList<String> fixline = new ArrayList<String>();
        for (int i = 0;i < lines.length;i++)
        {
            if (!lines[i].toString().replaceAll("\\s+", "").equalsIgnoreCase(""))
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
            FloatData dat = new FloatData();
            dat.savedata(ctx);
            dat.getSaveArrayData(ctx);
            dat.savedata(ctx);
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void exporttxt (Context ctx)
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
            Toast.makeText(ctx, R.string.text_export_error, Toast.LENGTH_SHORT).show();
        }
    }

    public static void closeAllWin (Context ctx)
    {
        WindowManager wm =((App)ctx.getApplicationContext()).getFloatwinmanager();
        ArrayList<FloatLinearLayout> layout =  ((App)ctx.getApplicationContext()).getFloatlinearlayout();
        ArrayList<Boolean> show =  ((App)ctx.getApplicationContext()).getShowFloat();
        for (int i = 0;i < layout.size();i++)
        {
            if (show.get(i))
            {
                wm.removeView(layout.get(i));
            }
        }
    }

    public static Thread PrepareSave (Activity ctx, Handler han)
    {
        if (((App)ctx.getApplicationContext()).getTextData().size() > 0)
        {
            if (Build.VERSION.SDK_INT >= 23)
            {
                if (!Settings.canDrawOverlays(ctx))
                {
                    FloatManageMethod.askforpermission(ctx, FloatManage.RESHOW_PERMISSION_RESULT_CODE);
                }
                else
                {
                    FloatManageMethod.delayaskforpermission(ctx, FloatManage.RESHOW_PERMISSION_RESULT_CODE);
                    return FloatManageMethod.Reshow(ctx, han);
                }
            }
            else
            {
                return FloatManageMethod.Reshow(ctx, han);
            }
        }
        return null;
    }

    public static void CloseApp (final Activity ctx)
    {
        AlertDialog.Builder exit = new AlertDialog.Builder(ctx)
            .setTitle(R.string.exit_title)
            .setMessage(R.string.exit_msg)
            .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                public void onClick (DialogInterface p1, int p2)
                {
                    FloatManageMethod.stopservice(ctx);
                    FloatManageMethod.closeAllWin(ctx);
                    ctx.finish();
                    System.exit(0);
                }
            })
            .setNegativeButton(R.string.cancel, null)
            .setNeutralButton(R.string.back_to_launcher, new DialogInterface.OnClickListener(){
                public void onClick (DialogInterface p1, int p2)
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
                        ctx.finish();
                    }
                }
            });
        exit.show();
    }

    public static void setWinManager (Context ctx)
    {
        WindowManager wm = (WindowManager)ctx.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        ((App)ctx.getApplicationContext()).setFloatwinmanager(wm);
    }

    public static void first_ask_for_premission (final Activity ctx)
    {
        SharedPreferences setdata = ctx.getSharedPreferences("ApplicationSettings", Activity.MODE_WORLD_READABLE);
        SharedPreferences.Editor setedit = setdata.edit();
        if (!setdata.getBoolean("FirstUse_AskForPremission", false))
        {
            AlertDialog.Builder asp = new AlertDialog.Builder(ctx)
                .setTitle(R.string.ask_for_premission)
                .setMessage(R.string.ask_for_premission_alert)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                    public void onClick (DialogInterface d, int i)
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

    public static void preparefolder ()
    {
        String typeface_path = Environment.getExternalStorageDirectory().toString() + "/FloatText/TTFs";
        File typeface = new File(typeface_path);
        if (!typeface.exists())
        {
            typeface.mkdirs();
        }
    }

    public static void delayaskforpermission (final Activity act, final int code)
    {
        new Handler().postDelayed(new Runnable(){  
                public void run ()
                {
                    if (Build.VERSION.SDK_INT >= 23)
                    {
                        if (!Settings.canDrawOverlays(act))
                        {
                            askforpermission(act, code);
                        }
                    }
                }  
            }, 2000); 
    }

    public static void askforpermission (final Activity act, int code)
    {
        final int askcode = code;
        final Activity activity = act;
        final Context context = act;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context)
            .setTitle(R.string.ask_for_premission)
            .setMessage(act.getString(R.string.ask_for_premisdion_msg) + act.getString(R.string.reshow_msg))
            .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                public void onClick (DialogInterface p1, int p2)
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    activity.startActivityForResult(intent, askcode);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                public void onClick (DialogInterface p1, int p2)
                {
                    act.finish();
                    System.exit(0);
                }
            })
            .setCancelable(false);
        dialog.show();
    }

    public static Thread getSaveData (final Activity ctx, final App utils, final SharedPreferences spdata, final Handler han)
    {
        Thread thread = new Thread() {
            public void run ()
            {
                FloatData dat = new FloatData();
                dat.getSaveArrayData(ctx);
                utils.setMovingMethod(spdata.getBoolean("TextMovingMethod", false));
                utils.setStayAliveService(spdata.getBoolean("StayAliveService", false));
                utils.setDynamicNumService(spdata.getBoolean("DynamicNumService", false));
                utils.setDevelopMode(spdata.getBoolean("DevelopMode", false));
                utils.setHtmlMode(spdata.getBoolean("HtmlMode", true));
                utils.setListTextHide(spdata.getBoolean("ListTextHide", false));
                han.obtainMessage(0).sendToTarget();
            }};
        return thread;
    }

    public static Thread Reshow (final Activity ctx, final Handler han)
    {
        Thread thread = new Thread() {
			public void run ()
            {
                final App utils = ((App)ctx.getApplicationContext());
                final ArrayList<String> Text = utils.getTextData();
                final ArrayList<Float> Size = utils.getSizeData();
                final ArrayList<Integer> Color = utils.getColorData();
                final ArrayList<Boolean> Thick = utils.getThickData();
                final ArrayList<Boolean> Show = utils.getShowFloat();
                final ArrayList<String> Position = utils.getPosition();
                final ArrayList<Boolean> Lock = utils.getLockPosition();
                final ArrayList<Boolean> Top = utils.getTextTop();
                final ArrayList<Boolean> AutoTop = utils.getAutoTop();
                final ArrayList<Boolean> Move = utils.getTextMove();
                final ArrayList<Integer> Speed = utils.getTextSpeed();
                final ArrayList<Boolean> Shadow = utils.getTextShadow();
                final ArrayList<Float> ShadowX = utils.getTextShadowX();
                final ArrayList<Float> ShadowY = utils.getTextShadowY();
                final ArrayList<Float> ShadowRadius = utils.getTextShadowRadius();
                final ArrayList<Integer> BackgroundColor = utils.getBackgroundColor();
                final ArrayList<Integer> ShadowColor = utils.getTextShadowColor();
                ctx.runOnUiThread(new Runnable(){
                        @Override
                        public void run ()
                        {
                            if (Text.size() != 0 && Size.size() != 0 && Color.size() != 0 && Thick.size() != 0)
                            {
                                WindowManager wm = utils.getFloatwinmanager();
                                for (int i = 0;i < Text.size();i++)
                                {
                                    FloatTextView fv = FloatTextSettingMethod.CreateFloatView(ctx, Text.get(i), Size.get(i), Color.get(i), Thick.get(i), Speed.get(i), i, Shadow.get(i), ShadowX.get(i), ShadowY.get(i), ShadowRadius.get(i), ShadowColor.get(i));
                                    FloatLinearLayout fll = FloatTextSettingMethod.CreateLayout(ctx, i);
                                    fll.changeShowState(Show.get(i));
                                    String[] ptemp = new String[]{"100", "150"};
                                    if (Lock.get(i))
                                    {
                                        ptemp = Position.get(i).toString().split("_");
                                        fll.setAddPosition(Float.parseFloat(ptemp[0]), Float.parseFloat(ptemp[1]));
                                    }
                                    WindowManager.LayoutParams layout = FloatTextSettingMethod.CreateFloatLayout(ctx, wm, fv, fll, Show.get(i), Float.parseFloat(ptemp[0]), Float.parseFloat(ptemp[1]), Top.get(i), Move.get(i), BackgroundColor.get(i));
                                    fll.setFloatLayoutParams(layout);
                                    fll.setPositionLocked(Lock.get(i));
                                    fll.setTop(AutoTop.get(i));
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
                            utils.getListviewadapter().notifyDataSetChanged();
                        }
                    }
                );
                if (han != null)
                {
                    han.obtainMessage(1).sendToTarget();
                }
            }
        };
        return thread;
    }

    public static void floattext_typeface_check (Context ctx)
    {
        SharedPreferences setdata = ctx.getSharedPreferences("ApplicationSettings", Activity.MODE_WORLD_READABLE);
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
        Toast.makeText(ctx, R.string.text_typeface_err, Toast.LENGTH_SHORT).show();
    }

    public static void LanguageInit (Activity ctx)
    {
        SharedPreferences setdata = ctx.getSharedPreferences("ApplicationSettings", Activity.MODE_WORLD_READABLE);
        int lan = setdata.getInt("Language", 0);
        ((App)ctx.getApplicationContext()).setLanguage(lan);
        LanguageSet(ctx, lan);
    }

    public static void LanguageSet (Activity ctx, int i)
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

    public static void startservice (Activity ctx)
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
        }
    }

    public static void stopservice (Activity ctx)
    {
        Intent service = new Intent(ctx, FloatWindowStayAliveService.class);
        ctx.stopService(service);
        Intent floatservice = new Intent(ctx, FloatTextUpdateService.class);
        ctx.stopService(floatservice);
    }

    public static void addFloatWindow (final Activity ctx, final ArrayList<String> FloatDataName)
    {
        if (((App)ctx.getApplicationContext()).getDevelopMode())
        {
            String[] type = ctx.getResources().getStringArray(R.array.floatmanage_choose);
            AlertDialog.Builder choose = new AlertDialog.Builder(ctx)
                .setTitle(R.string.choose_float_type)
                .setItems(type, new DialogInterface.OnClickListener(){
                    public void onClick (DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {
                            Intent intent = new Intent(ctx, FloatTextSetting.class);
                            intent.putExtra("EditID", FloatDataName.size());
                            ctx.startActivityForResult(intent, FloatManage.FLOATTEXT_RESULT_CODE);
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
            ctx.startActivityForResult(intent, FloatManage.FLOATTEXT_RESULT_CODE);
        }
    }

}
