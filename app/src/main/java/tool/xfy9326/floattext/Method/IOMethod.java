package tool.xfy9326.floattext.Method;

import java.io.*;
import java.util.*;

public class IOMethod
{
	public static boolean CopyFile (File fromFile, File toFile)
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
