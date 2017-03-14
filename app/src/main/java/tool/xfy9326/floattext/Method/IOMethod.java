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

public class IOMethod {
	//读取资源文件
	public static String readAssets(Context ctx, String path) {
		String result="";
        try {
            InputStreamReader inputReader = new InputStreamReader(ctx.getResources().getAssets().open(path));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            while ((line = bufReader.readLine()) != null) {
                result += line + "\n";
            }
        } catch (IOException e) {
            result = "No Found";
			e.printStackTrace();
        }
		return result;
	}

	//复制文件
	public static boolean CopyFile(File fromFile, File toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	//读取文件
    public static String[] readfile(File file) {
        ArrayList<String> output = new ArrayList<String>();
        try {
            InputStream file_stream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(file_stream)); 
            String line = ""; 
            while ((line = reader.readLine()) != null) { 
                output.add(line);
            }
            reader.close();
            String[] result = output.toArray(new String[output.size()]);
            return result;
        } catch (IOException e) {
            output.add("Failed");
            String[] result = output.toArray(new String[output.size()]);
            return result;
        }
    }

	//写入文件
    public static boolean writefile(String path, String data) {
        try {
            File file = new File(path);
            pathset(path);
            byte[] Bytes = new String(data).getBytes();
            if (file.exists()) {
                if (file.isFile()) {
                    OutputStream writer = new FileOutputStream(file);
                    writer.write(Bytes);
                    writer.close();
                    return true;
                } else {
                    return false;
                }
            } else {
                file.createNewFile();
                OutputStream writer = new FileOutputStream(file);
                writer.write(Bytes);
                writer.close();
                return true;
            }
        } catch (IOException e) {
			e.printStackTrace();
            return false;
        }
    }

	//设置文件夹
    public static void pathset(String path) {
        String[] dirs = path.split("/");
        String pth = "";
        for (int i = 0;i < dirs.length;i++) {
            if (i != dirs.length - 1) {
                pth += "/" + dirs[i];
            }
        }
        File dir = new File(pth);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
