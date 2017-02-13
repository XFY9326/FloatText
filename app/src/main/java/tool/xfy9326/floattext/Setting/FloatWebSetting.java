package tool.xfy9326.floattext.Setting;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v7.app.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;

import android.graphics.Bitmap;
import android.provider.Settings;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;
import tool.xfy9326.floattext.Activity.AppCompatPreferenceActivity;
import tool.xfy9326.floattext.View.FloatLinearLayout;

public class FloatWebSetting extends AppCompatPreferenceActivity
{
    private static final int REQUEST_CODE = 1;
    private String WebUrl;
    private View toolbar;
    private WebView webview;
    private boolean savewin = false;
    private FloatLinearLayout linearlayout;
    private SharedPreferences spdata;
    private SharedPreferences.Editor spedit;
    private WindowManager wm = null;
    private WindowManager.LayoutParams wmParams = null;
	private EditText urltext;
	private ProgressBar urlloading;
	private float FloatLong;
	private float FloatWide;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        spdata = PreferenceManager.getDefaultSharedPreferences(this);
        spedit = spdata.edit();
        wm = ((App)getApplicationContext()).getFloatwinmanager();
        wmcheck();
        addPreferencesFromResource(R.xml.floatweb_settings);
		sethome();
        preferenceset();
        defaultkeyget();
        prepareshow();
		SafeGuard.isSignatureAvailable(this);
        SafeGuard.isPackageNameAvailable(this);
    }

	private void sethome()
	{
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
		{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
	}

    private void wmcheck()
    {
        if (wm == null)
        {
            wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            ((App)getApplicationContext()).setFloatwinmanager(wm);
        }
        if (wm == null)
        {
            throw new Error(new NullPointerException());
        }
    }

    private void defaultkeyget()
    {
        FloatWide = Integer.parseInt(spdata.getString("WebWidth", "600"));
        FloatLong = Integer.parseInt(spdata.getString("WebHeight", "800"));
        WebUrl = spdata.getString("WebUrl", "https://xfy9326.github.io/FloatText/");
    }

    private void preferenceset()
    {
        EditTextPreference url = (EditTextPreference) findPreference("WebUrl");
        url.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    WebUrl = (String)p2;
                    updateview();
                    return true;
                }
            });
        Preference width = findPreference("WebWidth");
        width.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference p1)
                {
					WidthViewSet();
                    return true;
                }
            });
        Preference height = findPreference("WebHeight");
        height.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference p1)
                {
					HeightViewSet();
                    return true;
                }
            });
        Preference reload = findPreference("WebReload");
        reload.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference p1)
                {
                    webview.reload();
                    return true;
                }
            });
    }

	private void WidthViewSet()
	{
		LayoutInflater inflater = LayoutInflater.from(FloatWebSetting.this);  
		View layout = inflater.inflate(R.layout.dialog_floatsize_edit, null);
		AlertDialog.Builder dialog = new AlertDialog.Builder(FloatWebSetting.this);
		dialog.setTitle(R.string.xml_web_win_width);
		final TextView text = (TextView) layout.findViewById(R.id.textview_size_now);
		final SeekBar bar = (SeekBar) layout.findViewById(R.id.seekbar_size);
		text.setText(getString(R.string.xml_set_win_wide) + "：" + FloatWide);
		Button minus = (Button) layout.findViewById(R.id.floatsize_button_minus);
		Button plus = (Button) layout.findViewById(R.id.floatsize_button_plus);
		minus.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					if (FloatWide > 0)
					{
						FloatWide--;
						spedit.putFloat("FloatWide", FloatWide);
						spedit.commit();
						bar.setProgress((int)FloatWide);
						text.setText(getString(R.string.xml_set_win_wide) + "：" + FloatWide);
						updateview();
					}
				}
			});
		plus.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					if (FloatWide < wm.getDefaultDisplay().getHeight())
					{
						FloatWide++;
						spedit.putFloat("FloatWide", FloatWide);
						spedit.commit();
						bar.setProgress((int)FloatWide);
						text.setText(getString(R.string.xml_set_win_wide) + "：" + FloatWide);
						updateview();
					}
				}
			});
		bar.setMax(wm.getDefaultDisplay().getHeight());
		bar.setProgress((int)FloatWide);
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				public void onStartTrackingTouch(SeekBar bar)
				{}
				public void onStopTrackingTouch(SeekBar bar)
				{}
				public void onProgressChanged(SeekBar bar , int i , boolean state)
				{
					FloatWide = i;
					text.setText(getString(R.string.xml_set_win_wide) + "：" + FloatWide);
					updateview();
				}
			});
		dialog.setView(layout);
		dialog.setPositiveButton(R.string.close, null);
		dialog.show();
	}

	private void HeightViewSet()
	{
		LayoutInflater inflater = LayoutInflater.from(FloatWebSetting.this);  
		View layout = inflater.inflate(R.layout.dialog_floatsize_edit, null);
		AlertDialog.Builder dialog = new AlertDialog.Builder(FloatWebSetting.this);
		dialog.setTitle(R.string.xml_web_win_height);
		final TextView text = (TextView) layout.findViewById(R.id.textview_size_now);
		final SeekBar bar = (SeekBar) layout.findViewById(R.id.seekbar_size);
		text.setText(getString(R.string.xml_set_win_long) + "：" + FloatLong);
		Button minus = (Button) layout.findViewById(R.id.floatsize_button_minus);
		Button plus = (Button) layout.findViewById(R.id.floatsize_button_plus);
		minus.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					if (FloatLong > 0)
					{
						FloatLong--;
						spedit.putFloat("FloatLong", FloatLong);
						spedit.commit();
						bar.setProgress((int)FloatLong);
						text.setText(getString(R.string.xml_set_win_long) + "：" + FloatLong);
						updateview();
					}
				}
			});
		plus.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					if (FloatLong < wm.getDefaultDisplay().getWidth())
					{
						FloatLong++;
						spedit.putFloat("FloatLong", FloatLong);
						spedit.commit();
						bar.setProgress((int)FloatLong);
						text.setText(getString(R.string.xml_set_win_long) + "：" + FloatLong);
						updateview();
					}
				}
			});
		bar.setMax(wm.getDefaultDisplay().getWidth());
		bar.setProgress((int)FloatLong);
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				public void onStartTrackingTouch(SeekBar bar)
				{}
				public void onStopTrackingTouch(SeekBar bar)
				{}
				public void onProgressChanged(SeekBar bar , int i , boolean state)
				{
					FloatLong = i;
					text.setText(getString(R.string.xml_set_win_long) + "：" + FloatLong);
					updateview();
				}
			});
		dialog.setView(layout);
		dialog.setPositiveButton(R.string.close, null);
		dialog.show();
	}

    private void prepareshow()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (!Settings.canDrawOverlays(this))
            {
                FloatTextSettingMethod.askforpermission(this, REQUEST_CODE);
            }
            else
            {
                CreateView();
            }
        }
        else
        {
            CreateView();
        }
    }

    private void CreateView()
    {
		LayoutInflater inflater = LayoutInflater.from(this);
        toolbar = inflater.inflate(R.layout.webview_toolbar, null);
		urltext = (EditText) toolbar.findViewById(R.id.webviewtoolbar_url);
		urltext.clearFocus();
		urlloading = (ProgressBar) toolbar.findViewById(R.id.webviewtoolbar_loading);
		urlloading.setVisibility(View.GONE);
		webview = FloatWebSettingMethod.CreateFloatWebView(this, WebUrl);
		webview.setWebViewClient(new WebViewClient(){
                public boolean shouldOverrideUrlLoading(WebView view, String wurl)
                {
					WebUrl = wurl;
                    view.loadUrl(wurl);
					urltext.setText(wurl);
                    return true;
                }

				public void onPageStarted(WebView view, String purl, Bitmap ic)
				{
					urltext.setText(purl);
					urlloading.setVisibility(View.VISIBLE);
					super.onPageStarted(view, purl, ic);
				}

				public void onPageFinished(WebView view, String furl)
				{
					urlloading.setVisibility(View.GONE);
					super.onPageFinished(view, furl);
				}
            });
        linearlayout = FloatTextSettingMethod.CreateLayout(this, -1);
        toolbar_set(this, webview, toolbar);
        wmParams = FloatWebSettingMethod.CreateFloatLayout(this, wm, webview, toolbar, linearlayout, 150, 150, true, (int)FloatWide, (int)FloatLong);
    }

    private void toolbar_set(Context ctx, final WebView webview, final View view)
    {
        Button hide = (Button) view.findViewById(R.id.webview_hide);
        Button previous = (Button) view.findViewById(R.id.webview_previous);
        Button next = (Button) view.findViewById(R.id.webview_next);
        Button reload = (Button) view.findViewById(R.id.webview_reload);
        Button close = (Button) view.findViewById(R.id.webview_close);
		Button urlenter = (Button) view.findViewById(R.id.webviewtoolbar_enter);
		urltext.setOnEditorActionListener(new OnEditorActionListener(){
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
				{
					if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE)
					{
						webview.loadUrl(FloatWebSettingMethod.urlfix(v.getText().toString()));
					}
					return true;
				}
			});
        hide.setOnClickListener(new OnClickListener(){
                public void onClick(View v)
                {
                    setsmallwin(wm, view, linearlayout);
                }
            });
        previous.setOnClickListener(new OnClickListener(){
                public void onClick(View v)
                {
                    webview.goBack();
                }
            });
        next.setOnClickListener(new OnClickListener(){
                public void onClick(View v)
                {
                    webview.goForward();
                }
            });
        reload.setOnClickListener(new OnClickListener(){
                public void onClick(View v)
                {
                    webview.reload();
                }
            });
        close.setOnClickListener(new OnClickListener(){
                public void onClick(View v)
                {
                    stopshow(FloatWebSetting.this);
                    FloatWebSetting.this.finish();
                }
            });
		urlenter.setOnClickListener(new OnClickListener(){
                public void onClick(View v)
                {
                    webview.loadUrl(FloatWebSettingMethod.urlfix(urltext.getText().toString()));
                }
            });
    }

    private void setsmallwin(WindowManager wm, View view, final FloatLinearLayout layout)
    {
        layout.removeView(webview);
        layout.removeView(view);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View toolbar_hide = inflater.inflate(R.layout.webview_toolbar_hide, null);
        Button back = (Button) toolbar_hide.findViewById(R.id.webview_show);
        back.setOnClickListener(new OnClickListener(){
                public void onClick(View v)
                {
                    backbigwin(toolbar_hide, layout);
                }
            });
        layout.addView(toolbar_hide);
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		updateview();
    }

    private void backbigwin(View v, FloatLinearLayout layout)
    {
        layout.removeView(v);
        layout.addView(toolbar);
        layout.addView(webview);
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		updateview();
    }

    private void updateview()
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.width = TableRow.LayoutParams.WRAP_CONTENT;
        params.height = TableRow.LayoutParams.WRAP_CONTENT;
        params.width = (int)FloatWide;
        params.height = (int)FloatLong;
        webview.setLayoutParams(params);
        webview.loadUrl(WebUrl);
		wm.updateViewLayout(linearlayout, wmParams);
    }

    private void stopshow(Context ctx)
    {
        if (wm != null)
        {
            if (linearlayout != null)
            {
                wm.removeView(linearlayout);
            }
            if (webview != null)
            {
                webview.clearFormData();
                webview.clearHistory();
                webview.clearDisappearingChildren();
                webview.clearCache(true);
                webview.clearFormData();
                webview.clearView();
                webview.clearMatches();
                webview.clearFocus();
                webview.clearAnimation();
                webview.clearSslPreferences();
                webview.destroy();
				ctx.getCacheDir().delete();
            }
            wm = null;
            wmParams = null;
            linearlayout = null;
        }
    }

	private void setbackresult(int i)
	{
		Intent intent = new Intent();
		intent.putExtra("RESULT", i);
		setResult(StaticNum.FLOATTEXT_RESULT_CODE, intent);
	}

	//返回事件处理
	private void backpressed()
	{
		AlertDialog.Builder exit = new AlertDialog.Builder(this)
			.setTitle(R.string.exit_text_add)
			.setMessage(R.string.exit_text_add_alert)
			.setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface p1, int p2)
				{
					setbackresult(2);
					FloatWebSetting.this.finish();
				}
			})
			.setNegativeButton(R.string.cancel, null);
		exit.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			backpressed();
		}
		return super.onKeyDown(keyCode, event);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE)
        {
            if (Build.VERSION.SDK_INT >= 23)
            {
                if (Settings.canDrawOverlays(this))
                {
                    prepareshow();
                }
                else
                {
                    setbackresult(3);
                    this.finish();
                }
            }
        }
    }

    @Override 
    public boolean onCreateOptionsMenu(Menu menu)
    {  
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floatsetting_action_bar, menu);
        return super.onCreateOptionsMenu(menu);  
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
			case android.R.id.home:
				backpressed();
				break;
            case R.id.save_win:
                savewin = true;
				setbackresult(1);
                this.finish();
                break;
            case R.id.delete_win:
				setbackresult(2);
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        if (!savewin)
        {
            stopshow(this);
        }
        super.onDestroy();
    }

}
