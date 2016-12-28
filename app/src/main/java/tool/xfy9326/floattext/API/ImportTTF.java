package tool.xfy9326.floattext.API;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Method.*;

public class ImportTTF extends Activity
{
    private String FilePath = null;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_importttf);
        FloatManageMethod.preparefolder();
        FilePath = getIntentData();
        setView();
    }

    private String getIntentData ()
    {
        String path = null;
        Intent intent = getIntent();
        String action = intent.getAction();
        if (intent.ACTION_VIEW.equals(action))
        { 
            Uri uri = intent.getData();
            path = uri.getPath().toString();
        }
        return path;
    }

    private void setView ()
    {
        Button importfile = (Button) findViewById(R.id.button_importttf);
        TextView filename = (TextView) findViewById(R.id.textview_selectttf);
        final File ttf = new File(FilePath);
        filename.setText(ttf.getName());
        importfile.setOnClickListener(new OnClickListener(){
                public void onClick (View v)
                {
                    if (ttf.exists())
                    {
                        File ttf_c = new File(Environment.getExternalStorageDirectory().toString() + "/FloatText/TTFs/"  + ttf.getName());
                        if (!ttf_c.exists())
                        {
                            if (IOMethod.CopyFile(ttf, ttf_c))
                            {
                                Toast.makeText(ImportTTF.this, R.string.ttf_import_success, Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(ImportTTF.this, R.string.ttf_import_failed, Toast.LENGTH_SHORT).show();
                            }
                            ImportTTF.this.finish();
                        }
                        else
                        {
                            Toast.makeText(ImportTTF.this, R.string.ttf_import_exist, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(ImportTTF.this, R.string.ttf_import_failed,Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

}
