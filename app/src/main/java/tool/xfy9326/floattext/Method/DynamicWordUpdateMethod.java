package tool.xfy9326.floattext.Method;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Tool.DateCounter;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.View.FloatLinearLayout;
import tool.xfy9326.floattext.View.FloatTextView;

/*
 本方法用于动态变量的更新
 */

public class DynamicWordUpdateMethod {
    private Context context;
    private ArrayList<FloatTextView> floatview = new ArrayList<FloatTextView>();
    private ArrayList<String> floattext = new ArrayList<String>();
    private ArrayList<WindowManager.LayoutParams> floatlayout = new ArrayList<WindowManager.LayoutParams>();
    private ArrayList<FloatLinearLayout> linearlayout = new ArrayList<FloatLinearLayout>();
    private ArrayList<Boolean> ShowFloat = new ArrayList<Boolean>();
    private WindowManager wm;
	private String[] filtertext = new String[]{"NULL"};
	private boolean textfilter;
	private boolean addonmode = false;

    public DynamicWordUpdateMethod(Context ctx, boolean addonmode) {
        this.context = ctx;
		this.addonmode = addonmode;
    }

    public void UpdateText(Intent intent) {
		App utils = ((App)context.getApplicationContext());
		textfilter = utils.TextFilter;
		wm = utils.getFloatwinmanager();
		floatview = utils.getFrameutil().getFloatview();
		floattext = utils.getFrameutil().getFloattext();
		ShowFloat = utils.getTextutil().getShowFloat();
		linearlayout = utils.getFrameutil().getFloatlinearlayout();
		floatlayout = utils.getFrameutil().getFloatlayout();
		if (addonmode) {
			String[] keys = intent.getStringArrayExtra("KEY");
			String[] datas = intent.getStringArrayExtra("DATA");
			if (keys != null && datas != null && keys.length == datas.length) {
				for (int i = 0; i < floattext.size(); i++) {
					UpdateEachText(i, keys, datas, null);
				}
			}
		} else {
			Bundle bundle = intent.getExtras();
			String[] list = bundle.getStringArray("LIST");
			String[] data = bundle.getStringArray("DATA");
			int[] info = bundle.getIntArray("INFO");
			for (int i = 0; i < floattext.size(); i++) {
				UpdateEachText(i, list, data, info);
			}
		}
		System.gc();
    }

	private void UpdateEachText(int i, String[] list, String[] data, int[] info) {
		if (ShowFloat.get(i)) {
			String str = floattext.get(i);
			if (info == null) {
				for (int a = 0;a < list.length;a++) {
					str = updatetext(str, list[a].toString(), data[a].toString(), 0);
				}
			} else {
				for (int a = 0;a < list.length;a++) {
					str = updatetext(str, list[a].toString(), data[a].toString(), info[a]);
				}
			}
			if (filtertext.length == 2) {
				str = FilterText(str, filtertext[0], filtertext[1]);
			}
			if (!str.equalsIgnoreCase("FilterText_No_Found") && floattext.get(i) != str && wm != null) {
				floatview.get(i).setText(str);
				try {
					wm.updateViewLayout(linearlayout.get(i), floatlayout.get(i));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}

    private String updatetext(String str, String tag, String change, int reg) {
        if (!change.equals("NULL")) {
            str = search(str, "<" + tag + ">", change, reg);
            str = search(str, "#" + tag + "#", change, reg);
			if (textfilter && filtertext.length == 1) {
				filtertext = FilterTextCheck(str, "[" + tag + "]", change);
			}
        }
        return str;
    }

    private String search(String str, String tag, String change, int reg) {
        if (reg == 0) {
            if (str.contains(tag)) {
                str = str.replace(tag, change);
            }
        } else {
            Pattern pat = Pattern.compile(tag);
            Matcher mat = pat.matcher(str);
			String replaceString = "";
			try {
				while (mat.find()) {
					String get = mat.group(0).toString();
					if (reg == 1) {
						replaceString = DateCounter.Count(context, (get.substring(11, get.length() - 1)), false);
					} else if (reg == 2) {
						replaceString = DateCounter.Count(context, (get.substring(14, get.length() - 1)), true);
					}
					str = str.replace(get, replaceString);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return str;
    }

	private String[] FilterTextCheck(String str, String tag, String change) {
		try {
			if (str.toString().substring(0, tag.length()).equalsIgnoreCase(tag)) {
				return new String[]{tag, change};
			}
			return new String[]{"NULL"};
		} catch (Exception e) {
			return new String[]{"NULL"};
		}
	}

	private String FilterText(String str_def, String tag, String change) {
		try {
			if (change.equals(context.getString(R.string.dynamic_word_empty))) {
				return change;
			}
			String str = str_def.substring(tag.length());
			String[] text;
			if (str.contains(";")) {
				text = str.split(";");
			} else {
				text = new String[1];
				text[0] = str;
			}
			String def = null;
			boolean found = false;
			for (String line : text) {
				int split = line.lastIndexOf("|");
				if (split >= 0) {
					String regstr = line.substring(0, split);
					String textstr = line.substring(split + 1);
					if (regstr.equalsIgnoreCase("Default")) {
						def = textstr.toString();
						continue;
					} else if (change.equals(regstr)) {
						str_def = textstr.toString();
						found = true;
						break;
					}
					if (!found && change.matches(regstr.toString())) {
						str_def = textstr.toString();
						found = true;
						break;
					}
				}
			}
			if (!found) {
				if (def != null) {
					str_def = def;
				} else {
					str_def = "FilterText_No_Found";
				}
			}
			if (str_def.trim().equalsIgnoreCase("Empty")) {
				str_def = "";
			}
			return str_def;
		} catch (Exception e) {
			return str_def;
		}
	}
}
