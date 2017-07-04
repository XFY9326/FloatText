package tool.xfy9326.floattext.FileSelector;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

public class SelectFile {
    public static final int TYPE_ChooseFile = 0;
    public static final int TYPE_ChooseFolder = 1;

    private final String DefaultPath;
    private final int resultcode;
    private final int type;
    private String FileType = null;

    public SelectFile(int resultcode, int choosetype) {
        this.resultcode = resultcode;
        this.type = choosetype;
        this.DefaultPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public SelectFile(int resultcode, int choosetype, String path) {
        this.resultcode = resultcode;
        this.type = choosetype;
        this.DefaultPath = path;
    }

    public void setFileType(String ExtraName) {
        this.FileType = ExtraName;
    }

    public void start(Activity act) {
        Intent intent = new Intent(act, FileList.class);
        intent.putExtra("DefaultPath", DefaultPath);
        intent.putExtra("ResultCode", resultcode);
        intent.putExtra("ChooseType", type);
        intent.putExtra("FileType", FileType);
        act.startActivityForResult(intent, resultcode);
    }
}
