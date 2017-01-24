package tool.xfy9326.floattext.Setting;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.provider.*;
import android.support.v7.app.*;
import android.view.*;
import android.view.View.*;
import android.view.WindowManager.*;
import android.view.inputmethod.*;
import android.webkit.*;
import android.widget.*;
import android.widget.TextView.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Activity.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;
import tool.xfy9326.floattext.View.*;

import android.view.View.OnClickListener;

public class FloatWebSetting extends AppCompatPreferenceActivity
{
    private static final int REQUEST_CODE = 1;
    private String WebUrl;
    private int webheight;
    private int webwidth;
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

    @Override
    protected void onCreate (Bundle savedInstanceState)
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
    }

	private void sethome ()
	{
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
		{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
	}

    private void wmcheck ()
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

    private void defaultkeyget ()
    {
        webwidth = Integer.parseInt(spdata.getString("WebWidth", "600"));
        webheight = Integer.parseInt(spdata.getString("WebHeight", "800"));
        WebUrl = spdata.getString("WebUrl", "https://xfy9326.github.io/FloatText/");
    }

    private void preferenceset ()
    {
        EditTextPreference url = (EditTextPreference) findPreference("WebUrl");
        url.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    WebUrl = (String)p2;
                    updateview();
                    return true;
                }
            });
        EditTextPreference width = (EditTextPreference) findPreference("WebWidth");
        width.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    webwidth = Integer.parseInt(p2.toString());
                    updateview();
                    return true;
                }
            });
        EditTextPreference height = (EditTextPreference) findPreference("WebHeight");
        height.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    webheight = Integer.parseInt(p2.toString());
                    updateview();
                    return true;
                }
            });
        Preference reload = findPreference("WebReload");
        reload.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference p1)
                {
                    webview.reload();
                    return true;
                }
            });
    }

    private void prepareshow ()
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

    private void CreateView ()
    {
		LayoutInflater inflater = LayoutInflater.from(this);
        toolbar = inflater.inflate(R.layout.webview_toolbar, null);
		urltext = (EditText) toolbar.findViewById(R.id.webviewtoolbar_url);
		urltext.clearFocus();
		urlloading = (ProgressBar) toolbar.findViewById(R.id.webviewtoolbar_loading);
		urlloading.setVisibility(View.GONE);
		webview = FloatWebSettingMethod.CreateFloatWebView(this, WebUrl);
		webview.setWebViewClient(new WebViewClient(){
                public boolean shouldOverrideUrlLoading (WebView view, String wurl)
                {
					WebUrl = wurl;
                    view.loadUrl(wurl);
					urltext.setText(wurl);
                    return true;
                }

				public void onPageStarted (WebView view, String purl, Bitmap ic)
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
        wmParams = FloatWebSettingMethod.CreateFloatLayout(this, wm, webview, toolbar, linearlayout, 150, 150, true, webwidth, webheight);
    }

    private void toolbar_set (Context ctx, final WebView webview, final View view)
    {
        Button hide = (Button) view.findViewById(R.id.webview_hide);
        Button previous = (Button) view.findViewById(R.id.webview_previous);
        Button next = (Button) view.findViewById(R.id.webview_next);
        Button reload = (Button) view.findViewById(R.id.webview_reload);
        Button close = (Button) view.findViewById(R.id.webview_close);
		Button urlenter = (Button) view.findViewById(R.id.webviewtoolbar_enter);
		urltext.setOnEditorActionListener(new OnEditorActionListener(){
				public boolean onEditorAction (TextView v, int actionId, KeyEvent event)
				{
					if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE)
					{
						webview.loadUrl(FloatWebSettingMethod.urlfix(v.getText().toString()));
					}
					return true;
				}
			});
        hide.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    setsmallwin(wm, view, linearlayout);
                }
            });
        previous.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    webview.goBack();
                }
            });
        next.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    webview.goForward();
                }
            });
        reload.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    webview.reload();
                }
            });
        close.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    stopshow(FloatWebSetting.this);
                    FloatWebSetting.this.finish();
                }
            });
		urlenter.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    webview.loadUrl(FloatWebSettingMethod.urlfix(urltext.getText().toString()));
                }
            });
    }

    private void setsmallwin (WindowManager wm, View view, final FloatLinearLayout layout)
    {
        layout.removeView(webview);
        layout.removeView(view);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View toolbar_hide = inflater.inflate(R.layout.webview_toolbar_hide, null);
        Button back = (Button) toolbar_hide.findViewById(R.id.webview_show);
        back.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    backbigwin(toolbar_hide, layout);
                }
            });
        layout.addView(toolbar_hide);
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
		updateview();
    }

    private void backbigwin (View v, FloatLinearLayout layout)
    {
        layout.removeView(v);
        layout.addView(toolbar);
        layout.addView(webview);
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		updateview();
    }

    private void updateview ()
    {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.width = TableRow.LayoutParams.WRAP_CONTENT;
        params.height = TableRow.LayoutParams.WRAP_CONTENT;
        params.width = webwidth;
        params.height = webheight;
        webview.setLayoutParams(params);
        webview.loadUrl(WebUrl);
		wm.updateViewLayout(linearlayout, wmParams);
    }

    private void stopshow (Context ctx)
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

	private void setbackresult (int i)
	{
		Intent intent = new Intent();
		intent.putExtra("RESULT", i);
		setResult(FloatManage.FLOATTEXT_RESULT_CODE, intent);
	}

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
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
    public boolean onCreateOptionsMenu (Menu menu)
    {  
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floatsetting_action_bar, menu);
        return super.onCreateOptionsMenu(menu);  
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId())
        {
			case android.R.id.home:
				onKeyDown(KeyEvent.KEYCODE_BACK, null);
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
    protected void onDestroy ()
    {
        if (!savewin)
        {
            stopshow(this);
        }
        super.onDestroy();
    }

}
