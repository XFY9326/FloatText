package tool.xfy9326.floattext.Activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import tool.xfy9326.floattext.*;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

public class LicenceActivity extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);
		Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tb);
		sethome();
		String path = "LICENSE_CN.txt";
		SharedPreferences setdata = getSharedPreferences("ApplicationSettings", Activity.MODE_PRIVATE);
        int lan = setdata.getInt("Language", 0);
		switch (lan)
		{
            case 1:
                path = "LICENSE_CN.txt";
                break;
            case 2:
                path = "LICENSE_CN.txt";
                break;
            case 3:
                path = "LICENSE_EN.txt";
                break;
		}
        TextView str = (TextView) findViewById(R.id.textview_license);
        String result="";
        try
        {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(path));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            while ((line = bufReader.readLine()) != null)
            {
                result += line + "\n";
            }
        }
        catch (IOException e)
        {
            result = "No Found";
        }
        str.setText(result);
    }

	private void sethome ()
	{
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
		{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
