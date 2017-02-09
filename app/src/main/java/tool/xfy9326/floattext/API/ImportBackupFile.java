package tool.xfy9326.floattext.API;

import android.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Utils.*;

import android.support.v7.widget.Toolbar;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Method.*;

public class ImportBackupFile extends AppCompatActivity
{
	private String FilePath = null;
	private static int FLOAT_TEXT_PERMISSION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_api_import);
		Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tb);
		setAll();
	}

	private void setAll()
	{
		if (Build.VERSION.SDK_INT > 22)
		{
			if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
			{
				requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, FLOAT_TEXT_PERMISSION);
			}
			else
			{
				FloatData fd = new FloatData(this);
				fd.savedata();
				FilePath = getIntentData();
				setView();
			}
		}
		else
		{
			FloatData fd = new FloatData(this);
			fd.savedata();
			FilePath = getIntentData();
			setView();
		}
	}

	private String getIntentData()
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

	private void setView()
    {
        Button importfile = (Button) findViewById(R.id.button_importttf);
        TextView filename = (TextView) findViewById(R.id.textview_selectttf);
        final File bak = new File(FilePath);
        filename.setText(bak.getName());
        importfile.setOnClickListener(new OnClickListener(){
                public void onClick(View v)
                {
                    if (bak.exists())
                    {
                    	FloatData fd = new FloatData(ImportBackupFile.this);
						if (fd.InputData(FilePath))
						{
							final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
							intent.putExtra("RecoverText", 1);
							FloatManageMethod.restartApplication(ImportBackupFile.this, intent);
						}
					}
                    else
                    {
                        Toast.makeText(ImportBackupFile.this, R.string.recover_failed, Toast.LENGTH_SHORT).show();
						ImportBackupFile.this.finish();
                    }
                }
            });
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == FLOAT_TEXT_PERMISSION)
		{
			FloatData fd = new FloatData(this);
			fd.savedata();
			FilePath = getIntentData();
			setView();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


}
