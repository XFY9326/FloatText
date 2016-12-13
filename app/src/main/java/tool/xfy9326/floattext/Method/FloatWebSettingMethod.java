package tool.xfy9326.floattext.Method;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.TableRow;
import tool.xfy9326.floattext.View.FloatLinearLayout;
import tool.xfy9326.floattext.View.FloatWebView;

public class FloatWebSettingMethod
{
    public static FloatWebView CreateFloatWebView (Context ctx, String url)
    {
        String cachePath = Environment.getExternalStorageDirectory().getPath().toString() + "/FloatText/webcache/";
        FloatWebView webview = new FloatWebView(ctx);
        webview.setVerticalScrollbarOverlay(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setAppCachePath(cachePath);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(5 * 1024 * 1024);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.setWebViewClient(new WebViewClient(){
                public boolean shouldOverrideUrlLoading (FloatWebView view, String url)
                {
                    view.loadUrl(url);
                    return true;
                }
            });
        webview.loadUrl(url);
        return webview;
    }

    public static WindowManager.LayoutParams CreateFloatLayout (final Context ctx, WindowManager wm, FloatWebView fwv, View tview, FloatLinearLayout layout, float px, float py, boolean show, int width, int height)
    {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = (int)px;
        wmParams.y = (int)py;
        wmParams.format = PixelFormat.TRANSLUCENT;
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.width = width;
        params.height = height;
        fwv.setLayoutParams(params);
        layout.setLayoutTransition(new LayoutTransition());
        layout.setBackgroundColor(Color.parseColor("#303F9F"));
        layout.setOrientation(FloatLinearLayout.VERTICAL);
        layout.setPadding(0, 50, 0, 0);
        layout.setLayout_default_flags(wmParams.flags);
        layout.setTop(true);
        layout.setAddPosition(px, py);
        layout.setFloatLayoutParams(wmParams);
        layout.changeShowState(show);
        layout.addView(tview);
        layout.addView(fwv);
        if (show)
        {
            wm.addView(layout, wmParams);
            fwv.reload();
        }
        return wmParams;
    }

}
