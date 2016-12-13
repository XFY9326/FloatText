package tool.xfy9326.floattext.Setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.Toast;
import tool.xfy9326.floattext.Method.FloatTextSettingMethod;
import tool.xfy9326.floattext.Method.FloatWebSettingMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.View.FloatLinearLayout;
import tool.xfy9326.floattext.View.FloatWebView;

public class FloatWebSetting extends PreferenceActivity
{
    private static final int REQUEST_CODE = 1;
    private String WebUrl;
    private int webheight;
    private int webwidth;
    private View toolbar;
    private FloatWebView webview;
    private boolean savewin = false;
    private FloatLinearLayout linearlayout;
    private SharedPreferences spdata;
    private SharedPreferences.Editor spedit;
    private WindowManager wm = null;
    private WindowManager.LayoutParams wmParams = null;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        spdata = PreferenceManager.getDefaultSharedPreferences(this);
        spedit = spdata.edit();
        wm = ((App)getApplicationContext()).getFloatwinmanager();
        addPreferencesFromResource(R.xml.floatweb_settings);
        preferenceset();
        defaultkeyget();
        prepareshow();
    }


    private void defaultkeyget ()
    {
        webwidth = Integer.parseInt(spdata.getString("WebWidth", "500"));
        webheight = Integer.parseInt(spdata.getString("WebHeight", "800"));
        WebUrl = spdata.getString("WebUrl", "http://www.baidu.com/");
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
        webview = FloatWebSettingMethod.CreateFloatWebView(this, WebUrl);
        linearlayout = FloatTextSettingMethod.CreateLayout(this, -1);
        LayoutInflater inflater = LayoutInflater.from(this);
        toolbar = inflater.inflate(R.layout.webview_toolbar, null);
        toolbar_set(this, webview, toolbar);
        wmParams = FloatWebSettingMethod.CreateFloatLayout(this, wm, webview, toolbar, linearlayout, 150, 150, true, webwidth, webheight);
        webview.reload();
    }

    private void toolbar_set (Context ctx, final FloatWebView webview, final View view)
    {
        Button hide = (Button) view.findViewById(R.id.webview_hide);
        Button previous = (Button) view.findViewById(R.id.webview_previous);
        Button next = (Button) view.findViewById(R.id.webview_next);
        Button reload = (Button) view.findViewById(R.id.webview_reload);
        Button close = (Button) view.findViewById(R.id.webview_close);
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
                    stopshow();
                    FloatWebSetting.this.finish();
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
    }

    private void backbigwin (View v, FloatLinearLayout layout)
    {
        layout.removeView(v);
        layout.addView(toolbar);
        layout.addView(webview);
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
    }

    private void stopshow ()
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
            }
            wm = null;
            wmParams = null;
            linearlayout = null;
        }
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
                    Toast.makeText(this, R.string.premission_ask_failed, Toast.LENGTH_SHORT).show();
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
            case R.id.save_win:
                savewin = true;
                this.finish();
                Toast.makeText(this, R.string.save_text_ok, Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete_win:
                this.finish();
                Toast.makeText(this, R.string.delete_text_ok, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy ()
    {
        if (!savewin)
        {
            stopshow();
        }
        super.onDestroy();
    }

}
