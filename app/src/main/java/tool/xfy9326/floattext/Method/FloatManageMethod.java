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
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
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

    public static void getSaveData (Context ctx, App utils, SharedPreferences spdata)
    {
        FloatData dat = new FloatData();
        dat.getSaveArrayData(ctx);
        utils.setMovingMethod(spdata.getBoolean("TextMovingMethod", false));
        utils.setStayAliveService(spdata.getBoolean("StayAliveService", false));
        utils.setDynamicNumService(spdata.getBoolean("DynamicNumService", false));
        utils.setDevelopMode(spdata.getBoolean("DevelopMode", false));
        utils.setHtmlMode(spdata.getBoolean("HtmlMode", true));
        utils.setListTextHide(spdata.getBoolean("ListTextHide", false));
    }

    public static void Reshow (Context ctx)
    {
        App utils = ((App)ctx.getApplicationContext());
        ArrayList<String> Text = utils.getTextData();
        ArrayList<Float> Size = utils.getSizeData();
        ArrayList<Integer> Color = utils.getColorData();
        ArrayList<Boolean> Thick = utils.getThickData();
        ArrayList<Boolean> Show = utils.getShowFloat();
        ArrayList<String> Position = utils.getPosition();
        ArrayList<Boolean> Lock = utils.getLockPosition();
        ArrayList<Boolean> Top = utils.getTextTop();
        ArrayList<Boolean> AutoTop = utils.getAutoTop();
        ArrayList<Boolean> Move = utils.getTextMove();
        ArrayList<Integer> Speed = utils.getTextSpeed();
        ArrayList<Boolean> Shadow = utils.getTextShadow();
        ArrayList<Float> ShadowX = utils.getTextShadowX();
        ArrayList<Float> ShadowY = utils.getTextShadowY();
        ArrayList<Float> ShadowRadius = utils.getTextShadowRadius();
        ArrayList<Integer> BackgroundColor = utils.getBackgroundColor();
        ArrayList<Integer> ShadowColor = utils.getTextShadowColor();
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
