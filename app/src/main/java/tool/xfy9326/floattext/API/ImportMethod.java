package tool.xfy9326.floattext.API;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;

public class ImportMethod
{
	public static String getIntentData(Activity ctx)
    {
        String path = null;
        Intent intent = ctx.getIntent();
        String action = intent.getAction();
        if (intent.ACTION_VIEW.equals(action))
        { 
            Uri uri = intent.getData();
            path = uri.getPath().toString();
        }
        return path;
    }
	
	public static String FilePathGet(Activity ctx, int requestcode)
	{
		if (Build.VERSION.SDK_INT > 22)
		{
			if (ctx.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
			{
				ctx.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestcode);
				return "";
			}
			else
			{
				return ImportMethod.getIntentData(ctx);
			}
		}
		else
		{
			return ImportMethod.getIntentData(ctx);
		}
	}
}
