package tool.xfy9326.floattext.Utils;

import android.app.*;
import android.content.*;
import android.util.*;
import java.io.*;
import java.util.*;
import org.json.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Tool.*;

/*
 数据操作
 */

public class FloatData
{
	private Context ctx;
    private int DataNum = 0;
	private SharedPreferences spdatat;
	private SharedPreferences.Editor speditt;
	private SharedPreferences spdata;
	private SharedPreferences.Editor spedit;
	private App utils;

	public FloatData(Context ctx)
	{
		this.ctx = ctx;
		utils = ((App)ctx.getApplicationContext());
		spdatat = ctx.getSharedPreferences("FloatTextList", Activity.MODE_PRIVATE);
        speditt = spdatat.edit();
        spdata = ctx.getSharedPreferences("FloatShowList", Activity.MODE_PRIVATE);
        spedit = spdata.edit();
	}

	//保存
    public void savedata()
    {
		FloatTextUtils textutils = utils.getTextutil();
        spedit.putInt("Version", StaticNum.FloatDataVersion);
        speditt.putString("TextArray", TextArr_encode(textutils.getTextShow()).toString());
        spedit.putString("ColorArray", textutils.getColorShow().toString());
        spedit.putString("ThickArray", textutils.getThickShow().toString());
        spedit.putString("SizeArray", textutils.getSizeShow().toString());
        spedit.putString("ShowArray", textutils.getShowFloat().toString());
        spedit.putString("LockArray", textutils.getLockPosition().toString());
        spedit.putString("PositionArray", textutils.getPosition().toString());
        spedit.putString("TopArray", textutils.getTextTop().toString());
        spedit.putString("AutoTopArray", textutils.getAutoTop().toString());
        spedit.putString("MoveArray", textutils.getTextMove().toString());
        spedit.putString("SpeedArray", textutils.getTextSpeed().toString());
        spedit.putString("ShadowArray", textutils.getTextShadow().toString());
        spedit.putString("ShadowXArray", textutils.getTextShadowX().toString());
        spedit.putString("ShadowYArray", textutils.getTextShadowY().toString());
        spedit.putString("ShadowRadiusArray", textutils.getTextShadowRadius().toString());
        spedit.putString("BackgroundColorArray", textutils.getBackgroundColor().toString());
        spedit.putString("TextShadowColorArray", textutils.getTextShadowColor().toString());
		spedit.putString("FloatSizeArray", textutils.getFloatSize().toString());
		spedit.putString("FloatLongArray", textutils.getFloatLong().toString());
		spedit.putString("FloatWideArray", textutils.getFloatWide().toString());
        spedit.apply();
		speditt.apply();
    }

	//获取
    public void getSaveArrayData()
    {
        int version = spdata.getInt("Version", 0);
        ArrayList<String> textarr = new ArrayList<String>();
        VersionFix_1(version, textarr);
		VersionFix_2(version, textarr);
        DataNum = textarr.size();
		FloatTextUtils textutils = utils.getTextutil();
		textutils.setTextShow(textarr);
		textutils.setSizeShow(NewFloatKey(spdata.getString("SizeArray", "[]"), "20.0"));
        textutils.setColorShow(NewIntegerKey(spdata.getString("ColorArray", "[]"), "-61441"));
        textutils.setThickShow(NewBooleanKey(spdata.getString("ThickArray", "[]"), "false"));
        textutils.setShowFloat(NewBooleanKey(spdata.getString("ShowArray", "[]"), "true"));
        textutils.setLockPosition(NewBooleanKey(spdata.getString("LockArray", "[]"), "false"));
        textutils.setPosition(NewStringKey(spdata.getString("PositionArray", "[]"), "50_50"));
        textutils.setTextTop(NewBooleanKey(spdata.getString("TopArray", "[]"), "false"));
        textutils.setAutoTop(NewBooleanKey(spdata.getString("AutoTopArray", "[]"), "true"));
        textutils.setTextMove(NewBooleanKey(spdata.getString("MoveArray", "[]"), "false"));
        textutils.setTextSpeed(NewIntegerKey(spdata.getString("SpeedArray", "[]"), "5"));
        textutils.setTextShadow(NewBooleanKey(spdata.getString("ShadowArray", "[]"), "false"));
        textutils.setTextShadowX(NewFloatKey(spdata.getString("ShadowXArray", "[]"), "10.0"));
        textutils.setTextShadowY(NewFloatKey(spdata.getString("ShadowYArray", "[]"), "10.0"));
        textutils.setTextShadowRadius(NewFloatKey(spdata.getString("ShadowRadiusArray", "[]"), "5.0"));
        textutils.setBackgroundColor(NewIntegerKey(spdata.getString("BackgroundColorArray", "[]"), "16777215"));
        textutils.setTextShadowColor(NewIntegerKey(spdata.getString("TextShadowColorArray", "[]"), "1660944384"));
		textutils.setFloatSize(NewBooleanKey(spdata.getString("FloatSizeArray", "[]"), "false"));
		textutils.setFloatLong(NewFloatKey(spdata.getString("FloatLongArray", "[]"), "100"));
		textutils.setFloatWide(NewFloatKey(spdata.getString("FloatWideArray", "[]"), "100"));
        utils.setTextutil(textutils);
    }

	private void VersionFix_1(int version, ArrayList<String> textarr)
	{
		String text = spdatat.getString("TextArray", "[]");
		if (version < 1)
        {
            textarr.addAll(FormatArrayList.StringToStringArrayList(text));
			speditt.putString("TextArray", TextArr_encode(textarr).toString());
			speditt.commit();
            updateVersion(1);
        }
		else
		{
			textarr.addAll(TextArr_decode(FormatArrayList.StringToStringArrayList(text)));
		}
	}

	private void VersionFix_2(int version, ArrayList<String> textarr)
	{
		if (version < 2)
		{
			String text_v = spdata.getString("TextArray", "[]");
			textarr.clear();
			if (version < 1)
			{
				textarr.addAll(FormatArrayList.StringToStringArrayList(text_v));
			}
			textarr.addAll(TextArr_decode(FormatArrayList.StringToStringArrayList(text_v)));
			spedit.remove("TextArray");
			speditt.putString("TextArray", TextArr_encode(textarr).toString());
			spedit.commit();
			speditt.commit();
			updateVersion(2);
		}
	}

	//输出
	public boolean OutputData(String path, int VersionCode)
	{
		String jsonresult = "";
		JSONObject mainobject = new JSONObject();
		JSONObject dataobject = new JSONObject();
		JSONObject textobject = new JSONObject();
		try
		{
			textobject.put("TextArray", spdatat.getString("TextArray", "[]"));

			xmltojson(dataobject, "SizeArray");
			xmltojson(dataobject, "ColorArray");
			xmltojson(dataobject, "ThickArray");
			xmltojson(dataobject, "ShowArray");
			xmltojson(dataobject, "LockArray");
			xmltojson(dataobject, "PositionArray");
			xmltojson(dataobject, "TopArray");
			xmltojson(dataobject, "AutoTopArray");
			xmltojson(dataobject, "MoveArray");
			xmltojson(dataobject, "SpeedArray");
			xmltojson(dataobject, "ShadowArray");
			xmltojson(dataobject, "ShadowXArray");
			xmltojson(dataobject, "ShadowYArray");
			xmltojson(dataobject, "ShadowRadiusArray");
			xmltojson(dataobject, "BackgroundColorArray");
			xmltojson(dataobject, "TextShadowColorArray");
			xmltojson(dataobject, "FloatSizeArray");
			xmltojson(dataobject, "FloatLongArray");
			xmltojson(dataobject, "FloatWideArray");

			mainobject.put("FloatText_Version", VersionCode);
			mainobject.put("Data_Version", StaticNum.FloatDataVersion);
			mainobject.put("Text", textobject);
			mainobject.put("Data", dataobject);

			jsonresult = mainobject.toString();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return false;
		}
		if (!IOMethod.writefile(path, jsonresult))
		{
			return false;
		}
		return true;
	}

	//导入
	public boolean InputData(String path)
	{
		File bak = new File(path);
		if (!bak.exists() && bak.isDirectory())
		{
			return false;
		}
		else
		{
			String[] data = IOMethod.readfile(bak);
			String str = "";
			for (int i = 0;i < data.length;i++)
			{
				str += data[i];
			}
			if (!str.equalsIgnoreCase("Failed"))
			{
				return InputDataAction(str);
			}
			else
			{
				return false;
			}
		}
	}

	private boolean InputDataAction(String str)
	{
		if (str != "" && !str.startsWith("error"))
		{
			try
			{
				JSONObject mainobject = new JSONObject(str);
				//int FloatText_Version = mainobject.getInt("FloatText_Version");
				//int Data_Version = mainobject.getInt("Data_Version");
				JSONObject dataobject = mainobject.getJSONObject("Data");
				JSONObject textobject = mainobject.getJSONObject("Text");

				String text = textobject.getString("TextArray");
				String oldtext = spdatat.getString("TextArray", "[]");
				if (oldtext.equalsIgnoreCase("[]"))
				{
					oldtext = text;
				}
				else
				{
					oldtext = CombineArrayString(oldtext, text);
				}
				speditt.putString("TextArray", oldtext);

				savetofile(dataobject);
				
				spedit.commit();
				speditt.commit();
				return true;
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	private void savetofile(JSONObject dataobject) throws JSONException
	{
		Iterator it = dataobject.keys();
		while (it.hasNext())
		{
			String key = it.next().toString();
			if (spdata.contains(key))
			{
				String old = spdata.getString(key, "[]");
				String get = dataobject.getString(key);
				if (old.equalsIgnoreCase("[]"))
				{
					old = get;
				}
				else
				{
					old = CombineArrayString(old, get);
				}
				spedit.putString(key, old);
			}
		}
	}

	private String CombineArrayString(String a1, String a2)
	{
		if (a1.length() == 2)
		{
			return a2;
		}
		String str = a1.substring(0, a1.length() - 1) + ", " + a2.substring(1, a2.length());
		return str;
	}

	private JSONObject xmltojson(JSONObject obj, String name) throws JSONException
	{
		obj.put(name, spdata.getString(name, "[]"));
		return obj;
	}

	private void updateVersion(int i)
	{
		spedit.putInt("Version", i);
		spedit.commit();
	}

    private static ArrayList<String> TextArr_decode(ArrayList<String> str)
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

    private static ArrayList<String> TextArr_encode(ArrayList<String> str)
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

    private ArrayList<String> NewStringKey(String fix, String def)
    {
        fix = NewKey(fix, def);
        ArrayList<String> res = FormatArrayList.StringToStringArrayList(fix);
        return FixKey(res, def);
    }

    private ArrayList<Integer> NewIntegerKey(String fix, String def)
    {
        fix = NewKey(fix, def);
        ArrayList<Integer> res = FormatArrayList.StringToIntegerArrayList(fix);
        return FixKey(res, Integer.valueOf(def));
    }

    private ArrayList<Float> NewFloatKey(String fix, String def)
    {
        fix = NewKey(fix, def);
        ArrayList<Float> res = FormatArrayList.StringToFloatArrayList(fix);
        return FixKey(res, Float.valueOf(def));
    }

    private ArrayList<Boolean> NewBooleanKey(String fix, String def)
    {
        fix = NewKey(fix, def);
        ArrayList<Boolean> res = FormatArrayList.StringToBooleanArrayList(fix);
        return FixKey(res, Boolean.valueOf(def));
    }

    private String NewKey(String fix, String def)
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

    private ArrayList<String> FixKey(ArrayList<String> str, String def)
    {
        while (str.size() < DataNum)
        {
            str.add(def);
        }
        return str;
    }

    private ArrayList<Float> FixKey(ArrayList<Float> str, Float def)
    {
        while (str.size() < DataNum)
        {
            str.add(def);
        }
        return str;
    }

    private ArrayList<Integer> FixKey(ArrayList<Integer> str, Integer def)
    {
        while (str.size() < DataNum)
        {
            str.add(def);
        }
        return str;
    }

    private ArrayList<Boolean> FixKey(ArrayList<Boolean> str, Boolean def)
    {
        while (str.size() < DataNum)
        {
            str.add(def);
        }
        return str;
    }
    
}
