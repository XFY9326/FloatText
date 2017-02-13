package tool.xfy9326.floattext.Activity;

import android.support.v7.app.*;
import android.view.*;
import android.widget.*;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import tool.xfy9326.floattext.Method.IOMethod;
import tool.xfy9326.floattext.R;

public class LicenseActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);
		Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tb);
		sethome();
		String floattext_path = "LICENSES/FloatText_LICENSE.txt";
		String icon_path = "LICENSES/Android-Material-Icons_LICENSE.txt";
		String preference_path = "LICENSES/ColorPickerPreference_LICENSE.txt";
		String and_path = "LICENSES/Android-Support-Library_LICENSE.txt";

        String ft_licence = IOMethod.readAssets(this, floattext_path);
		String ic_licence = IOMethod.readAssets(this, icon_path);
		String pre_licence = IOMethod.readAssets(this, preference_path);
		String and_licence = IOMethod.readAssets(this, and_path);

		String ft_url = "https://github.com/XFY9326/FloatText";
		String ic_url = "https://github.com/MajeurAndroid/Android-Material-Icons";
		String pre_url = "https://github.com/attenzione/android-ColorPickerPreference";
		String and_url = "http://source.android.com/";

        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_license);

		addlicence("FloatText", ft_url, ft_licence, ll);
		addlicence("Android-Material-Icons", ic_url, ic_licence, ll);
		addlicence("ColorPickerPreference", pre_url, pre_licence, ll);
		addlicence("Android-Support-Library", and_url, and_licence, ll);
    }

	private void addlicence(String title, String html, String data, LinearLayout ll)
	{
		LayoutInflater inflater = LayoutInflater.from(LicenseActivity.this);  
		View layout = inflater.inflate(R.layout.layout_each_licence, null);
		TextView tit = (TextView) layout.findViewById(R.id.licence_title);
		tit.setText(title);
		TextView str = (TextView) layout.findViewById(R.id.licence_data);
		str.setText(data);
		TextView url = (TextView) layout.findViewById(R.id.licence_url);
		url.setText(Html.fromHtml("<a href='" + html + "'>" + html + "</a>"));
		url.setMovementMethod(LinkMovementMethod.getInstance());
		ll.addView(layout);
	}

	private void sethome()
	{
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
		{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
