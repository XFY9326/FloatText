package tool.xfy9326.floattext.Activity;

import android.app.*;
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
        TextView str = (TextView) findViewById(R.id.textview_license);
        String result="";
        try
        {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("LICENSE.txt"));
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
