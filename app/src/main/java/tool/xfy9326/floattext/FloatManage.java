package tool.xfy9326.floattext;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import tool.xfy9326.floattext.Activity.*;
import tool.xfy9326.floattext.Utils.*;
import tool.xfy9326.floattext.View.*;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Tool.FormatArrayList;

public class FloatManage extends AppCompatActivity
{
    private AdvanceRecyclerView listview = null;
    private ListViewAdapter listadapter = null;
    private SharedPreferences spdata;
    private SharedPreferences.Editor spedit;
    private ArrayList<String> FloatDataName;
    private AlertDialog ag_loading;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		FloatManageMethod.RootTask(this);
        FloatManageMethod.LanguageInit(this);
        setContentView(R.layout.activity_float_manage);
		contentset();
        ag_loading = FloatManageMethod.setLoadingDialog(this);
		setHandle();
        SetAll(this);
    }

	private void ListViewSet()
    {
        App utils = (App)getApplicationContext();
        FloatDataName = utils.getFloatText();
        listview = (AdvanceRecyclerView) findViewById(R.id.listview_floatmanage);
		listview.setHasFixedSize(true);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
		listview.setLayoutManager(mLayoutManager);
        listview.setEmptyView(findViewById(R.id.textview_floatmanage_empty));
        listadapter = new ListViewAdapter(this, FloatDataName);
        listview.setAdapter(listadapter);
        utils.setListviewadapter(listadapter);
    }

	private void contentset()
	{
		Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tb);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatbutton);
		fab.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					FloatManageMethod.addFloatWindow(FloatManage.this, FloatDataName);
				}
			});
		final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
		NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
		getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, tb, R.string.on, R.string.off);
		mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
		if (navigationView != null)
		{
			navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(MenuItem item) 
					{
						mDrawerLayout.closeDrawers();
						DrawerSet(mDrawerLayout, item.getItemId());
						return false;
					}
				});
		}
	}

	private void DrawerSet(DrawerLayout mDrawerLayout, int item)
	{
		switch (item)
		{
			case R.id.menu_import:
				FloatManageMethod.TextFileSolve(FloatManage.this, 1, StaticNum.FLOAT_TEXT_IMPORT_PERMISSION);
				break;
			case R.id.menu_export:
				FloatManageMethod.TextFileSolve(FloatManage.this, 0, StaticNum.FLOAT_TEXT_EXPORT_PERMISSION);
				break;
			case R.id.menu_wordlist:
				FloatManageMethod.showDList(this);
				break;
			case R.id.menu_about:
				Intent aboutintent = new Intent(FloatManage.this, AboutActivity.class);
				startActivity(aboutintent);
				break;
			case R.id.menu_set:
				Intent setintent = new Intent(FloatManage.this, GlobalSetActivity.class);
				startActivityForResult(setintent, StaticNum.FLOATSET_RESULT_CODE);
				break;
			case R.id.menu_back:
				if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
				{
					mDrawerLayout.closeDrawer(GravityCompat.START);
				}
				FloatManageMethod.RunInBack(FloatManage.this);
				break;
			case R.id.menu_exit:
				FloatManageMethod.ShutSown(FloatManage.this);
				break;
		}
	}

    private void SetAll(final Activity ctx)
    {
        FloatStart(ctx);
        Intent intent = getIntent();
        int importresult = intent.getIntExtra("ImportText", 0);
		int recoverresult = intent.getIntExtra("RecoverText", 0);
        if (importresult == 1)
        {
            snackshow(ctx, getString(R.string.text_import_success));
        }
        else if (importresult == 2)
        {
            snackshow(ctx, getString(R.string.text_import_error));
        }
		if (recoverresult == 1)
		{
			snackshow(ctx, getString(R.string.recover_success));
		}
    }

	private void FloatStart(final Activity ctx)
	{
		App utils = (App)getApplicationContext();
		SharedPreferences setdata = ctx.getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
		utils.getFrameutil().setFilterApplication(FormatArrayList.StringToStringArrayList(setdata.getString("Filter_Application", "[]")));
        spdata = PreferenceManager.getDefaultSharedPreferences(ctx);
        spedit = spdata.edit();
        if (!utils.GetSave)
        {
            runOnUiThread(new Runnable(){
                    public void run()
                    {
                        ag_loading.show();
                    }
                });
            Thread t = FloatManageMethod.getSaveData(ctx, utils, spdata, mHandler);
            FloatManageMethod.preparefolder();
            FloatManageMethod.setWinManager(FloatManage.this);
			t.start();
            utils.setGetSave(true);
        }
        else
        {
            ListViewSet();
			FloatManageMethod.startservice(FloatManage.this);
        }
	}

    private void closeag()
    {
        FloatManage.this.runOnUiThread(new Runnable(){
                public void run()
                {
                    ag_loading.dismiss();
                }
            });
    }

    private void importtxt(Intent data)
    {
        final String Path = data.getStringExtra("FilePath");
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        Thread thread = new Thread(new Runnable(){
                public void run()
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
		FloatManageMethod.restartApplication(this, intent);
    }

	private void setHandle()
	{
		mHandler = new Handler() {
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
					case 0:
						if (PermissionCheck(true))
						{
							FloatRecover(FloatManage.this);
						}
						break;
					case 1:
						closeag();
						break;
				}
			}};
	}

	private void FloatRecover(Activity ctx)
	{
		boolean close_ag = false;
		App utils = ((App)ctx.getApplicationContext());
		Thread r = FloatManageMethod.PrepareSave(ctx, mHandler);
		FloatManageMethod.floattext_typeface_check(ctx, true);
		if (r != null)
		{
			if (utils.FloatWinReshow)
			{
				r.start();
				close_ag = true;
				utils.setFloatReshow(false);
			}
		}
		FloatManageMethod.startservice(ctx);
		FloatManageMethod.first_ask_for_premission(ctx);
		ListViewSet();
		if (!close_ag)
		{
			closeag();
		}
	}

	private boolean PermissionCheck(boolean request)
	{
		if (Build.VERSION.SDK_INT >= 23)
		{
			int result = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			if (result == PackageManager.PERMISSION_GRANTED)
			{
				return true;
			}
			else
			{
				if (request)
				{
					requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, StaticNum.FLOAT_TEXT_SYSTEM_PERMISSION);
				}
				return false;
			}
		}
		return true;
	}

	private void  AlertTextShow(Intent data)
	{
		if (data != null)
		{
			int s = data.getIntExtra("RESULT", 0);
			int p = data.getIntExtra("POSITION", 0);
			boolean e = data.getBooleanExtra("EDITMODE", false);
			switch (s)
			{
				case 1:
					new Handler().postDelayed(new Runnable(){   
							public void run()
							{   
								snackshow(FloatManage.this, getString(R.string.save_text_ok));
							}   
						}, 300);   
					break;
				case 2:
					new Handler().postDelayed(new Runnable(){   
							public void run()
							{   
								snackshow(FloatManage.this, getString(R.string.delete_text_ok));
							}   
						}, 300);   
					break;
				case 3:
					new Handler().postDelayed(new Runnable(){   
							public void run()
							{   
								snackshow(FloatManage.this, getString(R.string.premission_ask_failed));
							}   
						}, 300);   
					break;
			}
			FloatDataName = ((App)getApplicationContext()).getFloatText();
			if (e)
			{
				listadapter.notifyItemChanged(p);
			}
			else
			{
				listadapter.notifyItemInserted(p);
				listadapter.notifyItemRangeChanged(p, listadapter.getItemCount());
			}
		}
		FloatData dat = new FloatData(this);
		dat.savedata();
	}

	private void ReshowPermisdionGot()
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
				snackshow(this, getString(R.string.premission_ask_failed));
			}
		}
		listadapter.notifyDataSetChanged();
	}

	public static void snackshow(Activity ctx, String str)
	{
		CoordinatorLayout cl = (CoordinatorLayout) ctx.findViewById(R.id.FloatManage_MainLayout);
		Snackbar sb = Snackbar.make(cl, str, Snackbar.LENGTH_SHORT);
		sb.show();
		System.gc();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if (requestCode == StaticNum.FLOAT_TEXT_SYSTEM_PERMISSION)
		{
			if (PermissionCheck(false))
			{
				FloatRecover(this);
			}
		}
		else if (requestCode == StaticNum.FLOAT_TEXT_IMPORT_PERMISSION)
		{
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
			{
				FloatManageMethod.selectFile(this);
			}
		}
		else if (requestCode == StaticNum.FLOAT_TEXT_EXPORT_PERMISSION)
		{
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
			{
				FloatManageMethod.exporttxt(this);
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
		if (listadapter == null)
		{
			listadapter = ((App)getApplicationContext()).getListviewadapter();
		}
        if (requestCode == StaticNum.FLOATTEXT_RESULT_CODE)
        {
			AlertTextShow(data);
        }
        else if (requestCode == StaticNum.RESHOW_PERMISSION_RESULT_CODE)
        {
            ReshowPermisdionGot();
        }
        else if (requestCode == StaticNum.FLOAT_TEXT_IMPORT_CODE)
        {
            if (data != null)
            {
                importtxt(data);
				listadapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
			if (drawer.isDrawerOpen(GravityCompat.START))
			{
				drawer.closeDrawer(GravityCompat.START);
			}
			else
			{
				FloatManageMethod.CloseApp(this);
			}
        }
		else if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
			if (drawer.isDrawerOpen(GravityCompat.START))
			{
				drawer.closeDrawer(GravityCompat.START);
			}
			else
			{
				drawer.openDrawer(GravityCompat.START);
			}
		}
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        spdata = PreferenceManager.getDefaultSharedPreferences(this);
        spedit = spdata.edit();
        ListViewSet();
    }

    @Override
    protected void onDestroy()
    {
        System.gc();
        super.onDestroy();
    }

}
