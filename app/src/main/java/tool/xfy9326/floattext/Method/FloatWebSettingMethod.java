package tool.xfy9326.floattext.Method;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TableRow;

import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.View.FloatLinearLayout;

public class FloatWebSettingMethod {
    //获取默认窗口高度
    public static int getWinDefaultHeight(WindowManager wm) {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels / 3;
    }

    //获取默认窗口宽度
    public static int getWinDefaultWidth(WindowManager wm) {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels / 2;
    }

    //URL修复
    public static String urlfix(String str) {
        if (!str.contains("://")) {
            str = "http://" + str;
        }
        return str;
    }

    //新建Web悬浮窗
    public static WebView CreateFloatWebView(Context ctx, String url) {
        String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FloatText/WebCache/";
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
        webview.loadUrl(url);
        return webview;
    }

    //悬浮窗布局设置
    public static WindowManager.LayoutParams CreateFloatLayout(WindowManager wm, WebView fwv, View tview, FloatLinearLayout layout, float px, float py, boolean show, int width, int height) {
        WindowManager.LayoutParams wmParams = ParamsSet(px, py, fwv, width, height);
        layout.setLayoutTransition(new LayoutTransition());
        layout.setBackgroundColor(Color.parseColor("#303F9F"));
        layout.setOrientation(FloatLinearLayout.VERTICAL);
        layout.setPadding(0, 50, 0, 0);
        layout.setLayout_default_flags(wmParams.flags);
        layout.setTop(true);
        layout.setAddPosition(px, py);
        layout.setFloatLayoutParams(wmParams);
        layout.setAllowlongclick(false);
        layout.changeShowState(show);
        layout.addView(tview);
        layout.addView(fwv);
        if (show) {
            wm.addView(layout, wmParams);
            fwv.reload();
        }
        return wmParams;
    }

    //悬浮窗设置
    private static WindowManager.LayoutParams ParamsSet(float px, float py, WebView fwv, int width, int height) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wmParams.gravity = Gravity.START | Gravity.TOP;
        wmParams.x = (int) px;
        wmParams.y = (int) py;
        wmParams.format = PixelFormat.TRANSLUCENT;
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;
        wmParams.windowAnimations = R.style.floatwin_anim;
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.width = width;
        params.height = height;
        fwv.setLayoutParams(params);
        return wmParams;
    }

}
