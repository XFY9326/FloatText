package tool.xfy9326.floattext.Activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import tool.xfy9326.floattext.*;

public class LicenceActivity extends Activity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);
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

}
