package tool.xfy9326.floattext.Method;

import android.animation.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.WindowManager.*;
import android.webkit.*;
import android.widget.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.View.*;

public class FloatWebSettingMethod
{
	
	public static String urlfix (String str)
	{
		if (!str.contains("://"))
		{
			str = "http://" + str;
		}
		return str;
	}
	
    public static WebView CreateFloatWebView (Context ctx, String url, final EditText et, final ProgressBar loading)
    {
        String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/FloatText/WebCache/";
        WebView webview = new WebView(ctx);
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
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.setWebViewClient(new WebViewClient(){
                public boolean shouldOverrideUrlLoading (WebView view, String wurl)
                {
                    view.loadUrl(wurl);
					et.setText(wurl);
                    return true;
                }
				
				public void onPageStarted (WebView view, String purl, Bitmap ic)
				{
					et.setText(purl);
					loading.setVisibility(View.VISIBLE);
					super.onPageStarted(view, purl, ic);
				}
				
				public void onPageFinished(WebView view, String furl)
				{
					loading.setVisibility(View.GONE);
					super.onPageFinished(view, furl);
				}
            });
        webview.loadUrl(url);
        return webview;
    }

    public static WindowManager.LayoutParams CreateFloatLayout (final Context ctx, WindowManager wm, WebView fwv, View tview, FloatLinearLayout layout, float px, float py, boolean show, int width, int height)
    {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = (int)px;
        wmParams.y = (int)py;
        wmParams.format = PixelFormat.TRANSLUCENT;
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;
		wmParams.windowAnimations = R.style.floatwin_anim;
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
