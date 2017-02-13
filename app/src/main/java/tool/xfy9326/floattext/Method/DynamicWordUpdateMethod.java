package tool.xfy9326.floattext.Method;

import android.content.*;
import java.util.regex.*;
import tool.xfy9326.floattext.View.*;

import android.os.Bundle;
import android.view.WindowManager;
import java.util.ArrayList;
import tool.xfy9326.floattext.Tool.DateCounter;
import tool.xfy9326.floattext.Utils.App;
import android.util.Log;

/*
 本方法用于动态变量的更新
 */

public class DynamicWordUpdateMethod
{
    private Context context;
    private ArrayList<FloatTextView> floatview = new ArrayList<FloatTextView>();
    private ArrayList<String> floattext = new ArrayList<String>();
    private ArrayList<WindowManager.LayoutParams> floatlayout = new ArrayList<WindowManager.LayoutParams>();
    private ArrayList<FloatLinearLayout> linearlayout = new ArrayList<FloatLinearLayout>();
    private ArrayList<Boolean> ShowFloat = new ArrayList<Boolean>();
    private WindowManager wm;
	private String[] filtertext = new String[]{"NULL"};
	private boolean textfilter;

    public DynamicWordUpdateMethod(Context ctx)
    {
        this.context = ctx;
    }

    public void UpdateText(Intent intent)
    {
        Bundle bundle = intent.getExtras();
		String[] list = bundle.getStringArray("LIST");
		String[] data = bundle.getStringArray("DATA");
		int[] info = bundle.getIntArray("INFO");
        App utils = ((App)context.getApplicationContext());
		textfilter = utils.TextFilter;
        wm = utils.getFloatwinmanager();
        floatview = utils.getFrameutil().getFloatview();
        floattext = utils.getFrameutil().getFloattext();
        ShowFloat = utils.getTextutil().getShowFloat();
        linearlayout = utils.getFrameutil().getFloatlinearlayout();
        floatlayout = utils.getFrameutil().getFloatlayout();
        for (int i = 0; i < floattext.size(); i++)
        {
            UpdateEachText(i, list, data, info);
        }
    }

	private void UpdateEachText(int i, String[] list, String[] data, int[] info)
	{
		if (ShowFloat.get(i))
		{
			String str = floattext.get(i);
			for (int a = 0;a < list.length;a++)
			{
				str = updatetext(str, list[a].toString(), data[a].toString(), info[a]);
			}
			if (filtertext.length == 2)
			{
				str = FilterText(str, filtertext[0], filtertext[1]);
			}
			if (floattext.get(i) != str && wm != null)
			{
				floatview.get(i).setText(str);
				try
				{
					wm.updateViewLayout(linearlayout.get(i), floatlayout.get(i));
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

    private String updatetext(String str, String tag, String change, int reg)
    {
        if (!change.equals("NULL"))
        {
			if (textfilter && filtertext.length == 1)
			{
				filtertext = FilterTextCheck(str, "[" + tag + "]", change);
			}
            String tag2 = tag;
            tag = "<" + tag + ">";
            str = search(str, tag, change, reg);
            tag2 = "#" + tag2 + "#";
            str = search(str, tag2, change, reg);
        }
        return str;
    }

    private String search(String str, String tag, String change, int reg)
    {
        if (reg == 0)
		{
            if (str.contains(tag))
            {
                str = str.replace(tag, change);
            }
        }
		else
		{
            Pattern pat = Pattern.compile(tag);
            Matcher mat = pat.matcher(str);
			String replaceString = "";
            while (mat.find())
            {
                String get = mat.group(0).toString();
				if (reg == 1)
				{
					replaceString = DateCounter.Count(context, (get.substring(11, get.length() - 1)));
				}
				str = str.replace(get, replaceString);
            }
        }
        return str;
    }

	private String[] FilterTextCheck(String str, String tag, String change)
	{
		try
		{
			if (str.toString().substring(0, tag.length()).equalsIgnoreCase(tag))
			{
				return new String[]{tag, change};
			}
			return new String[]{"NULL"};
		}
		catch (Exception e)
		{
			return new String[]{"NULL"};
		}
	}

	private String FilterText(String str, String tag, String change)
	{
		try
		{
			str = str.substring(tag.length(), str.length());
			String[] text;
			if (str.contains(";"))
			{
				text = str.split(";");
			}
			else
			{
				text = new String[1];
				text[0] = str;
			}
			String def = null;
			boolean found = false;
			for (String line : text)
			{
				String[] info = line.split("\\|");
				if (!found && change.contains(info[0]))
				{
					str = info[1];
					found = true;
					break;
				}
				if (info[0].equalsIgnoreCase("Default"))
				{
					def = info[1];
				}
			}
			if (!found && def != null)
			{
				str = def;
			}
			return str;
		}
		catch (Exception e)
		{
			return str;
		}
	}
}
