package tool.xfy9326.floattext.Activity;

import android.os.*;

import android.app.Activity;
import tool.xfy9326.floattext.Method.QuickStartMethod;
import tool.xfy9326.floattext.Utils.App;

//安卓N快捷方式处理

public class ShortCutActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
		{
			if (!((App)getApplicationContext()).StartShowWin)
			{
				QuickStartMethod.Launch(this);
			}
		}
		finish();
	}

}
