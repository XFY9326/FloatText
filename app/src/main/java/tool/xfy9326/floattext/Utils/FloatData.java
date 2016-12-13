package tool.xfy9326.floattext.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import java.util.ArrayList;

public class FloatData
{
    private int DataNum = 0;
    private static int FloatDataVersion = 1;

    public void savedata (Context ctx)
    {
        SharedPreferences spdata = ctx.getSharedPreferences("FloatShowList", Activity.MODE_WORLD_READABLE);
        SharedPreferences.Editor spedit = spdata.edit();
        App utils = ((App)ctx.getApplicationContext());
        spedit.putInt("Version", FloatDataVersion);
        spedit.putString("TextArray", TextArr_encode(utils.getTextData()).toString());
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
        spedit.apply();
    }

    public void getSaveArrayData (Context ctx)
    {
        App utils = ((App)ctx.getApplicationContext());
        SharedPreferences sp = ctx.getSharedPreferences("FloatShowList", Activity.MODE_WORLD_READABLE);
        int version = sp.getInt("Version", 0);
        String text = sp.getString("TextArray", "[]");
        ArrayList<String> textarr = new ArrayList<String>();
        if (version < 1)
        {
            SharedPreferences.Editor spedit = sp.edit();
            textarr = StringToStringArrayList(text);
            spedit.putInt("Version", 1);
            spedit.putString("TextArray", TextArr_encode(textarr).toString());
            spedit.commit();
        }
        else
        {
            textarr = TextArr_decode(StringToStringArrayList(text));
        }
        DataNum = textarr.size();
        String size = NewKey(sp.getString("SizeArray", "[]"), "20.0");
        String color = NewKey(sp.getString("ColorArray", "[]"), "-61441");
        String thick = NewKey(sp.getString("ThickArray", "[]"), "false");
        String show = NewKey(sp.getString("ShowArray", "[]"), "true");
        String lock = NewKey(sp.getString("LockArray", "[]"), "false");
        String position = NewKey(sp.getString("PositionArray", "[]"), "50_50");
        String top = NewKey(sp.getString("TopArray", "[]"), "false");
        String autotop = NewKey(sp.getString("AutoTopArray", "[]"), "true");
        String move = NewKey(sp.getString("MoveArray", "[]"), "false");
        String speed = NewKey(sp.getString("SpeedArray", "[]"), "5");
        String shadow = NewKey(sp.getString("ShadowArray", "[]"), "false");
        String shadowx = NewKey(sp.getString("ShadowXArray", "[]"), "10.0");
        String shadowy = NewKey(sp.getString("ShadowYArray", "[]"), "10.0");
        String shadowradius = NewKey(sp.getString("ShadowRadiusArray", "[]"), "5.0");
        String backgroundcolor = NewKey(sp.getString("BackgroundColorArray", "[]"), "16777215");
        String textshadowcolor = NewKey(sp.getString("TextShadowColorArray", "[]"), "1660944384");
        utils.replaceDatas(textarr, StringToIntegerArrayList(color), StringToFloatArrayList(size), StringToBooleanArrayList(thick), StringToBooleanArrayList(show), StringToStringArrayList(position), StringToBooleanArrayList(lock), StringToBooleanArrayList(top), StringToBooleanArrayList(autotop), StringToBooleanArrayList(move), StringToIntegerArrayList(speed), StringToBooleanArrayList(shadow), StringToFloatArrayList(shadowx), StringToFloatArrayList(shadowy), StringToFloatArrayList(shadowradius), StringToIntegerArrayList(backgroundcolor), StringToIntegerArrayList(textshadowcolor));
    }

    private ArrayList<String> TextArr_decode (ArrayList<String> str)
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

    private ArrayList<String> TextArr_encode (ArrayList<String> str)
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

    private ArrayList<String> StringToStringArrayList (String str)
    {
        ArrayList<String> arr = new ArrayList<String>();
        if (str.indexOf("[") >= 0 && str.length() >= 3)
        {
            str = str.substring(1, str.length() - 1);
            if (str.indexOf(",") >= 0)
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

    private ArrayList<Float> StringToFloatArrayList (String str)
    {
        ArrayList<Float> arr = new ArrayList<Float>();
        if (str.indexOf("[") >= 0 && str.length() >= 3)
        {
            str = str.substring(1, str.length() - 1);
            if (str.indexOf(",") >= 0)
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

    private ArrayList<Integer> StringToIntegerArrayList (String str)
    {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        if (str.indexOf("[") >= 0 && str.length() >= 3)
        {
            str = str.substring(1, str.length() - 1);
            if (str.indexOf(",") >= 0)
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

    private ArrayList<Boolean> StringToBooleanArrayList (String str)
    {
        ArrayList<Boolean> arr = new ArrayList<Boolean>();
        if (str.indexOf("[") >= 0 && str.length() >= 3)
        {
            str = str.substring(1, str.length() - 1);
            if (str.indexOf(",") >= 0)
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
