package lib.xfy9326.fileselector;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<String> Filename = new ArrayList<String>();
    private ArrayList<String> Filedata = new ArrayList<String>();
    private String Path;
    private String[] TypeList;
    private int[] IconList;

    public ListAdapter (Context context)
    {
        this.context = context;
        setFileIconData();
    }

    public void setListData (String path, ArrayList<String> name, ArrayList<String> data)
    {
        this.Filename = name;
        this.Filedata = data;
        this.Path = path;
    }

    @Override
    public View getView (int p1, View p2, ViewGroup p3)
    {
        final int index = p1;
        View view = p2;
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fileselector_list_layout, null);
        }
        final TextView name = (TextView) view.findViewById(R.id.fileselector_filelist_filename);
        name.setText(Filename.get(index));
        final TextView data = (TextView) view.findViewById(R.id.fileselector_filelist_filedata);
        data.setText(Filedata.get(index));
        final ImageView icon = (ImageView) view.findViewById(R.id.fileselector_filelist_icon);
        iconset(icon, index);
        return view;
    }

    private void iconset (ImageView icon, int index)
    {
        File file = new File(Path + "/" + Filename.get(index));
        String name = Filename.get(index);
        if (file.isDirectory())
        {
            if(name.equalsIgnoreCase("Download"))
            {
                icon.setImageResource(R.drawable.ic_folder_download);
            }
            else if(name.equalsIgnoreCase("DCIM") || name.equalsIgnoreCase("Pictures") || name.equalsIgnoreCase("Camera") || name.equalsIgnoreCase("ScreenShots"))
            {
                icon.setImageResource(R.drawable.ic_folder_image);
            }
            else
            {
                icon.setImageResource(R.drawable.ic_folder);
            }
        }
        else
        {
            if (name.substring(0, 1).equalsIgnoreCase("."))
            {
                icon.setImageResource(R.drawable.ic_file_hidden);
            }
            else
            {
                boolean found = false;
                String extraname = getExtraName(name);
                if (extraname.equalsIgnoreCase("No_Name"))
                {
                    icon.setImageResource(R.drawable.ic_file_delimited);
                }
                else
                {
                    for (int i = 0;i < TypeList.length;i++)
                    {
                        if (extraname.equalsIgnoreCase(TypeList[i]))
                        {
                            icon.setImageResource(IconList[i]);
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                    {
                        icon.setImageResource(R.drawable.ic_file);
                    }
                }
            }
        }
        if (name.equalsIgnoreCase("/..."))
        {
            icon.setImageResource(R.drawable.ic_folder_outline);
        }
    }

    @Override
    public long getItemId (int p1)
    {
        return p1;
    }

    @Override
    public Object getItem (int p1)
    {
        return Filename.get(p1);
    }

    @Override
    public int getCount ()
    {
        return Filename.size();
    }

    private void setFileIconData ()
    {
        TypeList = context.getResources().getStringArray(R.array.fileselect_types);
        IconList = context.getResources().getIntArray(R.array.fileselect_icons);
        for (int i = 0;i < IconList.length;i++)
        {
            switch (IconList[i])
            {
                case 1:
                    IconList[i] = R.drawable.ic_file_image;
                    break;
                case 2:
                    IconList[i] = R.drawable.ic_file_music;
                    break;
                case 3:
                    IconList[i] = R.drawable.ic_file_video;
                    break;
                case 4:
                    IconList[i] = R.drawable.ic_file_document;
                    break;
                case 5:
                    IconList[i] = R.drawable.ic_file_xml;
                    break;
                case 6:
                    IconList[i] = R.drawable.ic_file_pdf;
                    break;
                case 7:
                    IconList[i] = R.drawable.ic_file_word;
                    break;
                case 8:
                    IconList[i] = R.drawable.ic_file_excel;
                    break;
                case 9:
                    IconList[i] = R.drawable.ic_file_powerpoint;
                    break;
                case 10:
                    IconList[i] = R.drawable.ic_android;
                    break;
                case 11:
                    IconList[i] = R.drawable.ic_zip_box;
                    break;
            }

        }
    }

    private String getExtraName (String filename)
    { 
        if ((filename != null) && (filename.length() > 0))
        { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot > -1) && (dot < (filename.length() - 1)))
            { 
                return filename.substring(dot + 1); 
            } 
        } 
        return "No_Name"; 
    }

}
