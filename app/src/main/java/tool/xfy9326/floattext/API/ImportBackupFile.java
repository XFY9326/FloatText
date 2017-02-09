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
import tool.xfy9326.floattext.Utils.*;

import tool.xfy9326.floattext.R;

public class ImportBackupFile extends AppCompatActivity
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
		FloatData fd = new FloatData(this);
		fd.savedata();
		FilePath = ImportMethod.FilePathGet(this, FLOAT_TEXT_PERMISSION);
		setView();
	}

	private void setView()
    {
		if (!FilePath.equals(""))
		{
			Button importfile = (Button) findViewById(R.id.button_importttf);
			TextView filename = (TextView) findViewById(R.id.textview_selectttf);
			final File bak = new File(FilePath);
			filename.setText(bak.getName());
			importfile.setOnClickListener(new OnClickListener(){
					public void onClick(View v)
					{
						backupset(bak);
					}
				});
		}
    }

	private void backupset(File bak)
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
			ImportMethod.Show(ImportBackupFile.this, R.string.recover_failed);
			ImportBackupFile.this.finish();
		}
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
