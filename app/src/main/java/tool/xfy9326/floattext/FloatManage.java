package tool.xfy9326.floattext;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import android.preference.*;
import android.provider.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import java.util.*;
import lib.xfy9326.fileselector.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Activity.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;
import tool.xfy9326.floattext.View.*;

import tool.xfy9326.floattext.R;

public class FloatManage extends Activity
{
    public static int FLOATTEXT_RESULT_CODE = 0;
    public static int RESHOW_PERMISSION_RESULT_CODE = 2;
    public static int FLOATSET_RESULT_CODE = 3;
    public static int FLOAT_TEXT_IMPORT_CODE = 5;
    private ListView listview = null;
    private ListViewAdapter listadapter = null;
    private SharedPreferences spdata;
    private SharedPreferences.Editor spedit;
    private ArrayList<FloatTextView> FloatViewData;
    private ArrayList<String> FloatDataName;
    private LayoutAnimationController lac;
    private AlertDialog ag_loading;
    private Handler mHandler;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		FloatManageMethod.RootTask(this);
        FloatManageMethod.LanguageInit(this);
        setContentView(R.layout.activity_float_manage);
        ag_loading = FloatManageMethod.setLoadingDialog(this);
		setHandle();
        SetAll(this);
    }

    private void SetAll (final Activity ctx)
    {
        App utils = ((App)getApplicationContext());
        spdata = PreferenceManager.getDefaultSharedPreferences(ctx);
        spedit = spdata.edit();
        if (!utils.GetSave)
        {
            runOnUiThread(new Runnable(){
                    public void run ()
                    {
                        ag_loading.show();
                    }
                });
            Thread t = FloatManageMethod.getSaveData(ctx, utils, spdata, mHandler);
            t.start();
            FloatManageMethod.preparefolder();
            FloatManageMethod.setWinManager(FloatManage.this);
            utils.setGetSave(true);
        }
        else
        {
            ListViewSet();
			FloatManageMethod.startservice(FloatManage.this);
        }
        Intent intent = getIntent();
        int importresult = intent.getIntExtra("ImportText", 0);
        if (importresult == 1)
        {
            Toast.makeText(ctx, R.string.text_import_success, Toast.LENGTH_SHORT).show();
        }
        else if (importresult == 2)
        {
            Toast.makeText(ctx, R.string.text_import_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void ListViewSet ()
    {
        App utils = ((App)getApplicationContext());
        FloatDataName = utils.getFloatText();
        FloatViewData = utils.getFloatView();
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

    private void closeag ()
    {
        FloatManage.this.runOnUiThread(new Runnable(){
                public void run ()
                {
                    ag_loading.dismiss();
                }
            });
    }

    private void importtxt (Intent data)
    {
        final String Path = data.getStringExtra("FilePath");
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Thread thread = new Thread(new Runnable(){
                public void run ()
                {
                    boolean result = FloatManageMethod.importtxt(FloatManage.this, Path);
                    if (result)
                    {
                        intent.putExtra("ImportText", 1);
                    }
                    else
                    {
                        intent.putExtra("ImportText", 2);
                    }
                }
            });
        thread.start();
        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        startActivity(intent);
        finishAndRemoveTask();
		System.exit(0);
    }

	private void setHandle ()
	{
		mHandler = new Handler() {
			public void handleMessage (Message msg)
			{
				switch (msg.what)
				{
					case 0:
						boolean close_ag = false;
						App utils = ((App)getApplicationContext());
						Thread r = FloatManageMethod.PrepareSave(FloatManage.this, mHandler);
						if (r != null)
						{
							if (utils.FloatWinReshow)
							{
								r.start();
								close_ag = true;
								utils.setFloatReshow(false);
							}
						}
						FloatManageMethod.floattext_typeface_check(FloatManage.this);
						FloatManageMethod.startservice(FloatManage.this);
						FloatManageMethod.first_ask_for_premission(FloatManage.this);
						ListViewSet();
						if (!close_ag)
						{
							closeag();
						}
						break;
					case 1:
						closeag();
						break;
				}
			}};
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
            case R.id.menu_import:
                Toast.makeText(this, R.string.text_import_notice, Toast.LENGTH_LONG).show();
                SelectFile sf = new SelectFile(FLOAT_TEXT_IMPORT_CODE, SelectFile.TYPE_ChooseFile);
                sf.start(this);
                break;
            case R.id.menu_export:
                FloatManageMethod.exporttxt(this);
                break;
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
            ListViewSet();
            if (FloatDataName.size() > 0)
            {
                lac.start();
            }
            FloatData dat = new FloatData(this);
            dat.savedata();
        }
        else if (requestCode == RESHOW_PERMISSION_RESULT_CODE)
        {
            if (Build.VERSION.SDK_INT >= 23)
            {
                if (Settings.canDrawOverlays(this))
                {
                    Thread t = FloatManageMethod.Reshow(this, null);
                    t.start();
                }
                else
                {
                    Toast.makeText(this, R.string.premission_ask_failed, Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == FLOAT_TEXT_IMPORT_CODE)
        {
            if (data != null)
            {
                importtxt(data);
            }
        }
		if (listadapter == null)
		{
			listadapter = ((App)getApplicationContext()).getListviewadapter();
		}
        listadapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            FloatManageMethod.CloseApp(this);
        }
        return false;
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        spdata = PreferenceManager.getDefaultSharedPreferences(this);
        spedit = spdata.edit();
        ListViewSet();
    }

    @Override
    protected void onDestroy ()
    {
        System.gc();
        super.onDestroy();
    }

}
