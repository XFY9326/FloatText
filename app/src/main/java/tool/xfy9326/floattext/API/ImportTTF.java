package tool.xfy9326.floattext.API;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.R;

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
                            if (CopyFile(ttf, ttf_c))
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

    private boolean CopyFile (File fromFile, File toFile)
    {
        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


}
