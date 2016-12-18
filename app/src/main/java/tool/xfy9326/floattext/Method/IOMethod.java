package tool.xfy9326.floattext.Method;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class IOMethod
{
    public static String[] readfile (File file)
    {
        ArrayList<String> output = new ArrayList<String>();
        try
        {
            InputStream file_stream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(file_stream)); 
            String line = ""; 
            while ((line = reader.readLine()) != null)
            { 
                output.add(line);
            }
            reader.close();
            String[] result = output.toArray(new String[output.size()]);
            return result;
        }
        catch (IOException e)
        {
            output.add("Failed");
            String[] result = output.toArray(new String[output.size()]);
            return result;
        }
    }

    public static boolean writefile (String path, String data)
    {
        try
        {
            File file = new File(path);
            pathset(path);
            byte[] Bytes = new String(data).getBytes();
            if (file.exists())
            {
                if (file.isFile())
                {
                    OutputStream writer = new FileOutputStream(file);
                    writer.write(Bytes);
                    writer.close();
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                file.createNewFile();
                OutputStream writer = new FileOutputStream(file);
                writer.write(Bytes);
                writer.close();
                return true;
            }
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public static void pathset (String path)
    {
        String[] dirs = path.split("/");
        String pth = "";
        for (int i = 0;i < dirs.length;i++)
        {
            if (i != dirs.length - 1)
            {
                pth += "/" + dirs[i];
            }
        }
        File dir = new File(pth);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
    }
}
