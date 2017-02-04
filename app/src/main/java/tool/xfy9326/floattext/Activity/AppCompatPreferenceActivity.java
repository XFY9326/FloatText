package tool.xfy9326.floattext.Activity;

import android.content.res.*;
import android.os.*;
import android.preference.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;

public abstract class AppCompatPreferenceActivity extends PreferenceActivity
{

    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
	{
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    public ActionBar getSupportActionBar()
	{
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar)
	{
        getDelegate().setSupportActionBar(toolbar);
    }

    @Override
    public MenuInflater getMenuInflater()
	{
        return getDelegate().getMenuInflater();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
	{
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view)
	{
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params)
	{
        getDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params)
	{
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume()
	{
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color)
	{
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
	{
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop()
	{
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy()
	{
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void invalidateOptionsMenu()
	{
        getDelegate().invalidateOptionsMenu();
    }

    private AppCompatDelegate getDelegate()
	{
        if (mDelegate == null)
		{
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
}