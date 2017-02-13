package tool.xfy9326.floattext.Method;

import android.content.*;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import tool.xfy9326.floattext.R;

public class ImportMethod
{
	public static void Show(Context ctx, int str)
	{
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
	}

	public static void ViewSet(AppCompatActivity ctx)
	{
		ctx.setContentView(R.layout.activity_api_import);
		Toolbar tb = (Toolbar) ctx.findViewById(R.id.toolbar);
		ctx.setSupportActionBar(tb);
	}

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
