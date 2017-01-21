package tool.xfy9326.floattext.Method;

import android.content.*;
import android.os.*;
import android.view.*;
import java.util.*;
import java.util.regex.*;
import tool.xfy9326.floattext.Utils.*;
import tool.xfy9326.floattext.View.*;

public class DynamicWordUpdateMethod
{
    private Context context;
    private ArrayList<FloatTextView> floatview = new ArrayList<FloatTextView>();
    private ArrayList<String> floattext = new ArrayList<String>();
    private ArrayList<WindowManager.LayoutParams> floatlayout = new ArrayList<WindowManager.LayoutParams>();
    private ArrayList<FloatLinearLayout> linearlayout = new ArrayList<FloatLinearLayout>();
    private ArrayList<Boolean> ShowFloat = new ArrayList<Boolean>();
    private WindowManager wm;

    public DynamicWordUpdateMethod (Context ctx)
    {
        this.context = ctx;
    }

    public void UpdateText (Intent intent)
    {
        Bundle bundle = intent.getExtras();
		String[] list = bundle.getStringArray("LIST");
		String[] data = bundle.getStringArray("DATA");
		boolean[] info = bundle.getBooleanArray("INFO");
        App utils = ((App)context.getApplicationContext());
        wm = utils.getFloatwinmanager();
        floatview = utils.getFloatView();
        floattext = utils.getFloatText();
        ShowFloat = utils.getShowFloat();
        linearlayout = utils.getFloatlinearlayout();
        floatlayout = utils.getFloatLayout();
        for (int i = 0; i < floattext.size(); i++)
        {
            if (ShowFloat.get(i))
            {
                String str = floattext.get(i);
                for (int a = 0;a < list.length;a++)
				{
					str = updatetext(str, list[a].toString(), data[a].toString(), info[a]);
				}
                if (floattext.get(i) != str)
                {
                    floatview.get(i).setText(str);
					wm.updateViewLayout(linearlayout.get(i), floatlayout.get(i));
                }
            }
        }
    }

    private String updatetext (String str, String tag, String change, boolean reg)
    {
        if (!change.equals("NULL"))
        {
            String tag2 = tag;
            tag = "<" + tag + ">";
            str = search(str, tag, change, reg);
            tag2 = "#" + tag2 + "#";
            str = search(str, tag2, change, reg);
        }
        return str;
    }

    private String search (String str, String tag, String change, boolean reg)
    {
        if (reg)
        {
            Pattern pat = Pattern.compile(tag);
            Matcher mat = pat.matcher(str);
            while (mat.find())
            {
                String get = mat.group(0).toString();
				str = str.replace(get, FloatServiceMethod.datecount(context, (get.substring(11, get.length() - 1))));
            }
        }
        else
        {
            if (str.contains(tag))
            {
                str = str.replace(tag, change);
            }
        }
        return str;
    }
}
