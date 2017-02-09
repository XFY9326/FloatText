package tool.xfy9326.floattext.API;

import android.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Method.*;

import tool.xfy9326.floattext.R;

public class ImportTTF extends AppCompatActivity
{
    private String FilePath = null;
	private static int FLOAT_TEXT_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ImportMethod.ViewSet(this);
		setAll();
    }

	private void setAll()
	{
		FloatManageMethod.preparefolder();
		FilePath = ImportMethod.FilePathGet(this, FLOAT_TEXT_PERMISSION);
		setView();
	}

    private void setView()
    {
		if (!FilePath.equals(""))
		{
			Button importfile = (Button) findViewById(R.id.button_importttf);
			TextView filename = (TextView) findViewById(R.id.textview_selectttf);
			final File ttf = new File(FilePath);
			filename.setText(ttf.getName());
			importfile.setOnClickListener(new OnClickListener(){
					public void onClick(View v)
					{
						ttfset(ttf);
					}
				});
		}
    }

	private void ttfset(File ttf)
	{
		if (ttf.exists())
		{
			File ttf_c = new File(Environment.getExternalStorageDirectory().toString() + "/FloatText/TTFs/"  + ttf.getName());
			if (!ttf_c.exists())
			{
				if (IOMethod.CopyFile(ttf, ttf_c))
				{
					ImportMethod.Show(ImportTTF.this, R.string.ttf_import_success);
				}
				else
				{
					ImportMethod.Show(ImportTTF.this, R.string.ttf_import_failed);
				}
			}
			else
			{
				ImportMethod.Show(ImportTTF.this, R.string.ttf_import_exist);
			}
		}
		else
		{
			ImportMethod.Show(ImportTTF.this, R.string.ttf_import_failed);
		}
		ImportTTF.this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == FLOAT_TEXT_PERMISSION)
		{
			if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
			{
				FilePath = ImportMethod.getIntentData(this);
				setView();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
