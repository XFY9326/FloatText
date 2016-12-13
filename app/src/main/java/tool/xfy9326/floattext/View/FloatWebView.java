package tool.xfy9326.floattext.View;

import android.content.Context;
import android.webkit.WebView;

public class FloatWebView extends WebView
{
    public FloatWebView (Context context)
    {
        super(context);
    }

    @Override
    public void onPause ()
    {
        super.onPause();
        clearFormData();
        clearHistory();
        clearCache(true);
        clearDisappearingChildren();
        clearMatches();
        clearFocus();
        clearSslPreferences();
        clearView();
    }

}
