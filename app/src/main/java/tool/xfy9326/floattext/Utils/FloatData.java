package tool.xfy9326.floattext.Utils;

import android.app.*;
import android.content.*;
import android.util.*;
import java.util.*;
import tool.xfy9326.floattext.*;

public class FloatData
{
	private Context ctx;
    private int DataNum = 0;
    public static int FloatDataVersion = 2;
	private SharedPreferences spdatat;
	private SharedPreferences.Editor speditt;
	private SharedPreferences spdata;
	private SharedPreferences.Editor spedit;

	public FloatData (Context ctx)
	{
		this.ctx = ctx;
		spdatat = ctx.getSharedPreferences("FloatTextList", Activity.MODE_PRIVATE);
        speditt = spdatat.edit();
        spdata = ctx.getSharedPreferences("FloatShowList", Activity.MODE_PRIVATE);
        spedit = spdata.edit();
	}
	
    public void savedata ()
    {
        SafeGuard.isSignatureAvailable(ctx);
        SafeGuard.isPackageNameAvailable(ctx);
        App utils = ((App)ctx.getApplicationContext());
        spedit.putInt("Version", FloatDataVersion);
        speditt.putString("TextArray", TextArr_encode(utils.getTextData()).toString());
        spedit.putString("ColorArray", utils.getColorData().toString());
        spedit.putString("ThickArray", utils.getThickData().toString());
        spedit.putString("SizeArray", utils.getSizeData().toString());
        spedit.putString("ShowArray", utils.getShowFloat().toString());
        spedit.putString("LockArray", utils.getLockPosition().toString());
        spedit.putString("PositionArray", utils.getPosition().toString());
        spedit.putString("TopArray", utils.getTextTop().toString());
        spedit.putString("AutoTopArray", utils.getAutoTop().toString());
        spedit.putString("MoveArray", utils.getTextMove().toString());
        spedit.putString("SpeedArray", utils.getTextSpeed().toString());
        spedit.putString("ShadowArray", utils.getTextShadow().toString());
        spedit.putString("ShadowXArray", utils.getTextShadowX().toString());
        spedit.putString("ShadowYArray", utils.getTextShadowY().toString());
        spedit.putString("ShadowRadiusArray", utils.getTextShadowRadius().toString());
        spedit.putString("BackgroundColorArray", utils.getBackgroundColor().toString());
        spedit.putString("TextShadowColorArray", utils.getTextShadowColor().toString());
		spedit.putString("FloatSizeArray", utils.getFloatSize().toString());
		spedit.putString("FloatLongArray", utils.getFloatLong().toString());
		spedit.putString("FloatWideArray", utils.getFloatWide().toString());
        spedit.apply();
		speditt.apply();
    }

    public void getSaveArrayData ()
    {
        App utils = ((App)ctx.getApplicationContext());
        int version = spdata.getInt("Version", 0);
        String text = spdatat.getString("TextArray", "[]");
        ArrayList<String> textarr = new ArrayList<String>();
        if (version < 1)
        {
            textarr.addAll(StringToStringArrayList(text));
			speditt.putString("TextArray", TextArr_encode(textarr).toString());
			speditt.commit();
            updateVersion(1);
        }
		textarr.addAll(TextArr_decode(StringToStringArrayList(text)));
		if (version < 2)
		{
			String text_v = spdata.getString("TextArray", "[]");
			textarr.clear();
			if (version < 1)
			{
				textarr.addAll(StringToStringArrayList(text_v));
			}
			textarr.addAll(TextArr_decode(StringToStringArrayList(text_v)));
			spedit.remove("TextArray");
			speditt.putString("TextArray", TextArr_encode(textarr).toString());
			spedit.commit();
			speditt.commit();
			updateVersion(2);
		}
        DataNum = textarr.size();
        ArrayList<Float> size = NewFloatKey(spdata.getString("SizeArray", "[]"), "20.0");
        ArrayList<Integer> color = NewIntegerKey(spdata.getString("ColorArray", "[]"), "-61441");
        ArrayList<Boolean> thick = NewBooleanKey(spdata.getString("ThickArray", "[]"), "false");
        ArrayList<Boolean> show = NewBooleanKey(spdata.getString("ShowArray", "[]"), "true");
        ArrayList<Boolean> lock = NewBooleanKey(spdata.getString("LockArray", "[]"), "false");
        ArrayList<String> position = NewStringKey(spdata.getString("PositionArray", "[]"), "50_50");
        ArrayList<Boolean> top = NewBooleanKey(spdata.getString("TopArray", "[]"), "false");
        ArrayList<Boolean> autotop = NewBooleanKey(spdata.getString("AutoTopArray", "[]"), "true");
        ArrayList<Boolean> move = NewBooleanKey(spdata.getString("MoveArray", "[]"), "false");
        ArrayList<Integer> speed = NewIntegerKey(spdata.getString("SpeedArray", "[]"), "5");
        ArrayList<Boolean> shadow = NewBooleanKey(spdata.getString("ShadowArray", "[]"), "false");
        ArrayList<Float> shadowx = NewFloatKey(spdata.getString("ShadowXArray", "[]"), "10.0");
        ArrayList<Float> shadowy = NewFloatKey(spdata.getString("ShadowYArray", "[]"), "10.0");
        ArrayList<Float> shadowradius = NewFloatKey(spdata.getString("ShadowRadiusArray", "[]"), "5.0");
        ArrayList<Integer> backgroundcolor = NewIntegerKey(spdata.getString("BackgroundColorArray", "[]"), "16777215");
        ArrayList<Integer> textshadowcolor = NewIntegerKey(spdata.getString("TextShadowColorArray", "[]"), "1660944384");
		ArrayList<Boolean> floatsize = NewBooleanKey(spdata.getString("FloatSizeArray", "[]"), "false");
		ArrayList<Float> floatlong = NewFloatKey(spdata.getString("FloatLongArray", "[]"), "100");
		ArrayList<Float> floatwide = NewFloatKey(spdata.getString("FloatWideArray", "[]"), "100");
        utils.replaceDatas(textarr, color, size, thick, show, position, lock, top, autotop, move, speed, shadow, shadowx, shadowy, shadowradius, backgroundcolor, textshadowcolor, floatsize, floatlong, floatwide);
    }
	
	private void updateVersion (int i)
	{
		spedit.putInt("Version", i);
		spedit.commit();
	}

    private static ArrayList<String> TextArr_decode (ArrayList<String> str)
    {
        ArrayList<String> output = new ArrayList<String>();
        output.addAll(str);
        if (str.size() > 0)
        {
            for (int i = 0;i < str.size();i++)
            {
                String result = new String(Base64.decode(str.get(i).getBytes(), Base64.NO_WRAP));
                output.set(i, result);
            }
        }
        return output;
    }

    private static ArrayList<String> TextArr_encode (ArrayList<String> str)
    {
        ArrayList<String> output = new ArrayList<String>();
        output.addAll(str);
        if (str.size() > 0)
        {
            for (int i = 0;i < str.size();i++)
            {
                String result = Base64.encodeToString(str.get(i).getBytes(), Base64.NO_WRAP);
                output.set(i, result);
            }
        }
        return output;
    }

    private ArrayList<String> NewStringKey (String fix, String def)
    {
        fix = NewKey(fix, def);
        ArrayList<String> res = StringToStringArrayList(fix);
        return FixKey(res, def);
    }

    private ArrayList<Integer> NewIntegerKey (String fix, String def)
    {
        fix = NewKey(fix, def);
        ArrayList<Integer> res = StringToIntegerArrayList(fix);
        return FixKey(res, Integer.valueOf(def));
    }

    private ArrayList<Float> NewFloatKey (String fix, String def)
    {
        fix = NewKey(fix, def);
        ArrayList<Float> res = StringToFloatArrayList(fix);
        return FixKey(res, Float.valueOf(def));
    }

    private ArrayList<Boolean> NewBooleanKey (String fix, String def)
    {
        fix = NewKey(fix, def);
        ArrayList<Boolean> res = StringToBooleanArrayList(fix);
        return FixKey(res, Boolean.valueOf(def));
    }

    private String NewKey (String fix, String def)
    {
        if (fix.equalsIgnoreCase("[]") && DataNum != 0)
        {
            ArrayList<String> str = new ArrayList<String>();
            for (int i = 0;i < DataNum;i++)
            {
                str.add(def);
            }
            fix = str.toString();
        }
        return fix;
    }

    private ArrayList<String> FixKey (ArrayList<String> str, String def)
    {
        while (str.size() < DataNum)
        {
            str.add(def);
        }
        return str;
    }

    private ArrayList<Float> FixKey (ArrayList<Float> str, Float def)
    {
        while (str.size() < DataNum)
        {
            str.add(def);
        }
        return str;
    }

    private ArrayList<Integer> FixKey (ArrayList<Integer> str, Integer def)
    {
        while (str.size() < DataNum)
        {
            str.add(def);
        }
        return str;
    }

    private ArrayList<Boolean> FixKey (ArrayList<Boolean> str, Boolean def)
    {
        while (str.size() < DataNum)
        {
            str.add(def);
        }
        return str;
    }

    private final static ArrayList<String> StringToStringArrayList (String str)
    {
        ArrayList<String> arr = new ArrayList<String>();
        if (str.contains("[") && str.length() >= 3)
        {
            str = str.substring(1, str.length() - 1);
            if (str.contains(","))
            {
                String[] temp = str.split(",");
                for (int i = 0;i < temp.length;i++)
                {
                    if (i != 0)
                    {
                        temp[i] = temp[i].substring(1, temp[i].length());
                    }
                    arr.add(temp[i].toString());
                }
            }
            else
            {
                arr.add(str.toString());
            }
        }
        return arr;
    }

    private final static ArrayList<Float> StringToFloatArrayList (String str)
    {
        ArrayList<Float> arr = new ArrayList<Float>();
        if (str.contains("[") && str.length() >= 3)
        {
            str = str.substring(1, str.length() - 1);
            if (str.contains(","))
            {
                String[] temp = str.split(",");
                for (int i = 0;i < temp.length;i++)
                {
                    temp[i] = temp[i].replaceAll("\\s+", "");
                    arr.add(Float.parseFloat(temp[i]));
                }
            }
            else
            {
                arr.add(Float.parseFloat(str));
            }
        }
        return arr;
    }

    private final static ArrayList<Integer> StringToIntegerArrayList (String str)
    {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        if (str.contains("[") && str.length() >= 3)
        {
            str = str.substring(1, str.length() - 1);
            if (str.contains(","))
            {
                String[] temp = str.split(",");
                for (int i = 0;i < temp.length;i++)
                {
                    temp[i] = temp[i].replaceAll("\\s+", "");
                    arr.add(Integer.parseInt(temp[i]));
                }
            }
            else
            {
                arr.add(Integer.parseInt(str));
            }
        }
        return arr;
    }

    private final static ArrayList<Boolean> StringToBooleanArrayList (String str)
    {
        ArrayList<Boolean> arr = new ArrayList<Boolean>();
        if (str.contains("[") && str.length() >= 3)
        {
            str = str.substring(1, str.length() - 1);
            if (str.contains(","))
            {
                String[] temp = str.split(",");
                for (int i = 0;i < temp.length;i++)
                {
                    temp[i] = temp[i].replaceAll("\\s+", "");
                    arr.add(Boolean.parseBoolean(temp[i]));
                }
            }
            else
            {
                arr.add(Boolean.parseBoolean(str));
            }
        }
        return arr;
    }
}
