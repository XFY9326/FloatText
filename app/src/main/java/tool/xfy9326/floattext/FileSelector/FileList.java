package tool.xfy9326.floattext.FileSelector;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import tool.xfy9326.floattext.Method.ActivityMethod;
import tool.xfy9326.floattext.R;

public class FileList extends AppCompatActivity {
    private String Path;
    private String LastPath;
    private String StartPath;
    private String FileType;
    private int resultcode;
    private int choosetype;
    private ArrayList<String> Filename;
    private ArrayList<String> Filedata;
    private ArrayList<String> Filesize;
    private ListAdapter listadapter;
    private boolean backtofront = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileselector_select_layout);
        actionbarset();
        intentdataget();
        DataSet(Path);
        adapterset();
        ViewSet();
    }

    private void actionbarset() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        sethome();
    }

    private void sethome() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void adapterset() {
        listadapter = new ListAdapter(this);
        listadapter.setListData(Path, Filename, Filedata, Filesize);
    }

    private void intentdataget() {
        Intent intent = this.getIntent();
        Path = intent.getStringExtra("DefaultPath");
        FileType = intent.getStringExtra("FileType");
        resultcode = intent.getIntExtra("ResultCode", RESULT_OK);
        choosetype = intent.getIntExtra("ChooseType", SelectFile.TYPE_ChooseFile);
    }

    private void ViewSet() {
        ListView filelist = (ListView) findViewById(R.id.fileselector_listview_file);
        filelist.setAdapter(listadapter);
        filelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adv, View v, int li, long l) {
                LastPath = Path;
                if (li == 0) {
                    if (!Path.equalsIgnoreCase("/")) {
                        backtofront = true;
                        File file = new File(Path);
                        Path = file.getParent();
                        File_Selected(Path);
                    } else {
                        backtofront = false;
                        Path += "/" + listadapter.getItem(li);
                        File_Selected(Path);
                    }
                } else {
                    backtofront = false;
                    Path += "/" + listadapter.getItem(li);
                    File_Selected(Path);
                }
            }
        });
        filelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adv, View v, int li, long l) {
                editfolder(Path + "/" + listadapter.getItem(li));
                return true;
            }
        });
    }

    private void DataSet(String str) {
        File file = new File(str);
        Path = file.getAbsolutePath();
        StartPath = Path;
        if (file.exists() && file.isDirectory() && file.listFiles() != null) {
            File_GetList(file);
        } else {
            Show(R.string.fileselect_defaultpath_error);
        }
    }

    private void File_GetList(File file) {
        List<File> filelist = orderByName(file);
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> size = new ArrayList<>();
        if (filelist == null) {
            if (!Path.equalsIgnoreCase("/")) {
                size.add("");
                name.add("/...");
                data.add(file.getAbsolutePath());
            }
        } else {
            for (int i = 0; i < filelist.size(); i++) {
                if (FileType == null) {
                    size.add(filelist.get(i).isDirectory() ? "" : ActivityMethod.formatSize(this, String.valueOf(filelist.get(i).length())));
                    name.add(filelist.get(i).getName());
                    long time = filelist.get(i).lastModified();
                    String currenttime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", getResources().getConfiguration().locale).format(new Date(time));
                    data.add(currenttime);
                } else {
                    if (filelist.get(i).isDirectory() || getExtraName(filelist.get(i).getName()).equalsIgnoreCase(FileType)) {
                        size.add(filelist.get(i).isDirectory() ? "" : ActivityMethod.formatSize(this, String.valueOf(filelist.get(i).length())));
                        name.add(filelist.get(i).getName());
                        long time = filelist.get(i).lastModified();
                        String currenttime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", getResources().getConfiguration().locale).format(new Date(time));
                        data.add(currenttime);
                    }
                }
            }
            if (!Path.equalsIgnoreCase("/")) {
                size.add(0, "");
                name.add(0, "/...");
                data.add(0, file.getAbsolutePath());
            }
        }
        Filesize = size;
        Filename = name;
        Filedata = data;
    }

    private String getExtraName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "No_Name";
    }

    private void File_Selected(String str) {
        File file = new File(str);
        if (!file.exists()) {
            Show(R.string.fileselect_file_nofound);
            return;
        }
        if (choosetype == SelectFile.TYPE_ChooseFile) {
            filechoose(file);
        } else if (choosetype == SelectFile.TYPE_ChooseFolder) {
            folderchoose(file);
        }
    }

    private void filechoose(File file) {
        if (file.isDirectory()) {
            if (file.canRead()) {
                updateview();
            } else {
                if (backtofront) {
                    Path = LastPath;
                } else {
                    Path = file.getParent();
                }
                Show(R.string.fileselect_file_nopremission);
            }
        } else if (file.isFile()) {
            Path = file.getParent();
            returnpath(file.getAbsolutePath());
        }
    }

    private void folderchoose(final File file) {
        if (file.isDirectory()) {
            if (file.canRead()) {
                if (backtofront) {
                    updateview();
                } else {
                    AlertDialog.Builder edit = new AlertDialog.Builder(this);
                    String[] choice = getResources().getStringArray(R.array.fileselect_folder_edit);
                    edit.setItems(choice, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    updateview();
                                    break;
                                case 1:
                                    returnpath(file.getAbsolutePath());
                                    break;
                            }
                        }
                    });
                    edit.show();
                }
            } else {
                if (backtofront) {
                    Path = LastPath;
                } else {
                    Path = file.getParent();
                }
                Show(R.string.fileselect_file_nopremission);
            }
        }
    }

    private void returnpath(String str) {
        Intent intent = new Intent();
        intent.putExtra("FilePath", str);
        setResult(resultcode, intent);
        finish();
    }

    private List<File> orderByName(File file) {
        List<File> filelist = Arrays.asList(file.listFiles());
        Collections.sort(filelist, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile()) {
                    return -1;
                }
                if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        return filelist;
    }

    private void Show(int id) {
        Toast.makeText(this, getString(id), Toast.LENGTH_SHORT).show();
    }

    private void editfolder(final String path) {
        final File file = new File(path);
        AlertDialog.Builder edit = new AlertDialog.Builder(this);
        if (file.canRead()) {
            String[] choice = getResources().getStringArray(R.array.fileselect_file_edit);
            if (choosetype == SelectFile.TYPE_ChooseFile) {
                String[] choice_tmp = new String[choice.length - 1];
                System.arraycopy(choice, 1, choice_tmp, 0, choice.length - 1);
                edit.setItems(choice_tmp, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deletefile(file);
                                updateview();
                                break;
                            case 1:
                                renamefile(file);
                                break;
                        }
                    }
                });
                edit.show();
            } else if (choosetype == SelectFile.TYPE_ChooseFolder) {
                edit.setItems(choice, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                File_Selected(path);
                                break;
                            case 1:
                                deletefile(file);
                                updateview();
                                break;
                            case 2:
                                renamefile(file);
                                break;
                        }
                    }
                });
                edit.show();
            }
        } else {
            Show(R.string.fileselect_file_nopremission);
        }
    }

    private void deletefile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deletefile(f);
            }
            file.delete();
        }
    }

    private void renamefile(final File oldFile) {
        final EditText filename = new EditText(this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.fileselect_rename)
                .setView(filename)
                .setPositiveButton(R.string.fileselect_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        File newFile = new File(oldFile.getParent() + "/" + filename.getText().toString());
                        if (newFile.exists()) {
                            Show(R.string.fileselect_file_rename_found);
                        } else {
                            oldFile.renameTo(newFile);
                            if (!newFile.exists()) {
                                Show(R.string.fileselect_file_rename_error);
                            } else {
                                Show(R.string.fileselect_file_rename_ok);
                            }
                        }
                        updateview();
                    }
                })
                .setNegativeButton(R.string.fileselect_cancel, null);
        dialog.show();
    }

    private void addfolder() {
        final EditText foldername = new EditText(this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.fileselect_folder_new)
                .setView(foldername)
                .setPositiveButton(R.string.fileselect_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        String path = Path + "/" + foldername.getText().toString();
                        File file = new File(path);
                        if (file.exists()) {
                            Show(R.string.fileselect_file_rename_found);
                        } else {
                            file.mkdirs();
                            if (!file.exists()) {
                                Show(R.string.fileselect_file_rename_error);
                            } else {
                                Show(R.string.fileselect_folder_new_ok);
                            }
                        }
                        updateview();
                    }
                })
                .setNegativeButton(R.string.fileselect_cancel, null);
        dialog.show();
    }

    private void updateview() {
        File_GetList(new File(Path));
        listadapter.setListData(Path, Filename, Filedata, Filesize);
        listadapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fileselector_actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Path.equalsIgnoreCase("/") || StartPath.equalsIgnoreCase(Path)) {
                finish();
            } else {
                backtofront = true;
                File file = new File(Path);
                Path = file.getParent();
                File_Selected(Path);
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.fileselector_add_folder) {
            addfolder();
        }
        return super.onOptionsItemSelected(item);
    }

}
