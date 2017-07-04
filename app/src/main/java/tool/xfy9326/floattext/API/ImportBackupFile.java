package tool.xfy9326.floattext.API;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import tool.xfy9326.floattext.Method.FloatManageMethod;
import tool.xfy9326.floattext.Method.ImportMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Utils.FloatData;

//备份文件导入

public class ImportBackupFile extends AppCompatActivity {
    private static final int FLOAT_TEXT_PERMISSION = 1;
    private String FilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImportMethod.ViewSet(this);
        setAll();
    }

    private void setAll() {
        FloatData fd = new FloatData(this);
        fd.savedata();
        FilePath = ImportMethod.FilePathGet(this, FLOAT_TEXT_PERMISSION);
        setView();
    }

    private void setView() {
        if (!FilePath.equals("")) {
            Button importfile = (Button) findViewById(R.id.button_importttf);
            TextView filename = (TextView) findViewById(R.id.textview_selectttf);
            final File bak = new File(FilePath);
            filename.setText(bak.getName());
            importfile.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    backupset(bak);
                }
            });
        }
    }

    private void backupset(File bak) {
        if (bak.exists()) {
            FloatData fd = new FloatData(ImportBackupFile.this);
            if (fd.InputData(FilePath)) {
                final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intent.putExtra("RecoverText", 1);
                FloatManageMethod.restartApplication(ImportBackupFile.this, intent);
            }
        } else {
            ImportMethod.Show(ImportBackupFile.this, R.string.recover_failed);
            ImportBackupFile.this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FLOAT_TEXT_PERMISSION) {
            if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                FilePath = ImportMethod.getIntentData(this);
                setView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
