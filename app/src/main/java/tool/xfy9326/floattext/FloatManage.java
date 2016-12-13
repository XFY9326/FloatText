package tool.xfy9326.floattext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import tool.xfy9326.floattext.Activity.AboutActivity;
import tool.xfy9326.floattext.Activity.GlobalSetActivity;
import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.Utils.FloatData;
import tool.xfy9326.floattext.View.FloatTextView;
import tool.xfy9326.floattext.View.ListViewAdapter;

public class FloatManage extends Activity
{
    public static int FLOATTEXT_RESULT_CODE = 0;
    public static int RESHOW_PERMISSION_RESULT_CODE = 2;
    public static int FLOATSET_RESULT_CODE = 3;
    private ListView listview = null;
    private ListViewAdapter listadapter = null;
    private SharedPreferences spdata;
    private SharedPreferences.Editor spedit;
    private ArrayList<FloatTextView> FloatViewData;
    private ArrayList<String> FloatDataName;
    private LayoutAnimationController lac;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FloatManageMethod.LanguageInit(this);
        setContentView(R.layout.activity_float_manage);
        SetAll(this);
        ListViewSet();
    }

    public void SetAll (Activity ctx)
    {
        FloatManageMethod.preparefolder();
        FloatManageMethod.setWinManager(ctx);
        FloatManageMethod.floattext_typeface_check(ctx);
        getSaveData(ctx);
        FloatManageMethod.startservice(ctx);
        FloatManageMethod.first_ask_for_premission(ctx);
    }

    private void getSaveData (Context ctx)
    {
        App utils = ((App)ctx.getApplicationContext());
        spdata = PreferenceManager.getDefaultSharedPreferences(ctx);
        spedit = spdata.edit();
        FloatManageMethod.getSaveData(ctx, utils, spdata);
        FloatDataName = utils.getFloatText();
        FloatViewData = utils.getFloatView();
        if (utils.FloatWinReshow)
        {
            PrepareSave(ctx);
            utils.setFloatReshow(false);
        }
    }

    private void PrepareSave (Context ctx)
    {
        if (((App)ctx.getApplicationContext()).getTextData().size() > 0)
        {
            if (Build.VERSION.SDK_INT >= 23)
            {
                if (!Settings.canDrawOverlays(ctx))
                {
                    FloatManageMethod.askforpermission((Activity)ctx, RESHOW_PERMISSION_RESULT_CODE);
                }
                else
                {
                    FloatManageMethod.delayaskforpermission((Activity)ctx, RESHOW_PERMISSION_RESULT_CODE);
                    FloatManageMethod.Reshow(ctx);
                }
            }
            else
            {
                FloatManageMethod.Reshow(ctx);
            }
        }
    }

    private void ListViewSet ()
    {
        listview = (ListView) findViewById(R.id.listview_floatmanage);
        listview.setEmptyView(findViewById(R.id.textview_floatmanage_empty));
        listadapter = new ListViewAdapter(this, FloatDataName);
        listview.setAdapter(listadapter);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.listview_anim_layout);
        lac = new LayoutAnimationController(animation);
        lac.setDelay(0.3f);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        listview.setLayoutAnimation(lac);
        ((App)getApplicationContext()).setListviewadapter(listadapter);
    }

    @Override 
    public boolean onCreateOptionsMenu (Menu menu)
    {  
        MenuInflater inflater = getMenuInflater();  
        inflater.inflate(R.menu.opinion_menu, menu);
        inflater.inflate(R.menu.floatmanage_action_bar, menu);
        return super.onCreateOptionsMenu(menu);  
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_about:
                Intent aboutintent = new Intent(this, AboutActivity.class);
                startActivity(aboutintent);
                break;
            case R.id.menu_set:
                Intent setintent = new Intent(this, GlobalSetActivity.class);
                startActivityForResult(setintent, FLOATSET_RESULT_CODE);
                break;
            case R.id.add_win:
                FloatManageMethod.addFloatWindow(this, FloatDataName);
                break;
            case R.id.menu_exit:
                onKeyDown(KeyEvent.KEYCODE_BACK, null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if (requestCode == FLOATTEXT_RESULT_CODE)
        {
            App utils = ((App)getApplicationContext());
            FloatDataName = utils.getFloatText();
            FloatViewData = utils.getFloatView();
            ListViewSet();
            if (FloatDataName.size() > 0)
            {
                lac.start();
            }
            FloatData dat = new FloatData();
            dat.savedata(this);
        }
        else if (requestCode == RESHOW_PERMISSION_RESULT_CODE)
        {
            if (Build.VERSION.SDK_INT >= 23)
            {
                if (Settings.canDrawOverlays(this))
                {
                    FloatManageMethod.Reshow(this);
                }
                else
                {
                    Toast.makeText(this, R.string.premission_ask_failed, Toast.LENGTH_SHORT).show();
                }
            }
        }
        listadapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            AlertDialog.Builder exit = new AlertDialog.Builder(this)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_msg)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                    public void onClick (DialogInterface p1, int p2)
                    {
                        FloatManageMethod.stopservice(FloatManage.this);
                        FloatManageMethod.closeAllWin(FloatManage.this);
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setNeutralButton(R.string.back_to_launcher, new DialogInterface.OnClickListener(){
                    public void onClick (DialogInterface p1, int p2)
                    {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(FloatManage.this);
                        if (sp.getBoolean("WinOnlyShowInHome", false))
                        {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                        }
                        else
                        {
                            finish();
                        }
                    }
                });
            exit.show();
        }
        return false;
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        App utils = ((App)getApplicationContext());
        spdata = PreferenceManager.getDefaultSharedPreferences(this);
        spedit = spdata.edit();
        FloatDataName = utils.getFloatText();
        FloatViewData = utils.getFloatView();
        ListViewSet();
    }

    @Override
    protected void onDestroy ()
    {
        System.gc();
        super.onDestroy();
    }

}
