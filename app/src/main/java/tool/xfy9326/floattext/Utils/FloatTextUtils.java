package tool.xfy9326.floattext.Utils;

import java.util.*;

public class FloatTextUtils
{
	private ArrayList<String> TextShow = new ArrayList<String>();
    private ArrayList<Integer> ColorShow = new ArrayList<Integer>();
    private ArrayList<Float> SizeShow = new ArrayList<Float>();
    private ArrayList<Boolean> ThickShow = new ArrayList<Boolean>();
    private ArrayList<Boolean> ShowFloat = new ArrayList<Boolean>();
    private ArrayList<String> Position = new ArrayList<String>();
    private ArrayList<Boolean> LockPosition = new ArrayList<Boolean>();
    private ArrayList<Boolean> TextTop = new ArrayList<Boolean>();
    private ArrayList<Boolean> AutoTop = new ArrayList<Boolean>();
    private ArrayList<Boolean> TextMove = new ArrayList<Boolean>();
    private ArrayList<Integer> TextSpeed = new ArrayList<Integer>();
    private ArrayList<Boolean> TextShadow = new ArrayList<Boolean>();
    private ArrayList<Float> TextShadowX = new ArrayList<Float>();
    private ArrayList<Float> TextShadowY = new ArrayList<Float>();
    private ArrayList<Float> TextShadowRadius = new ArrayList<Float>();
    private ArrayList<Integer> TextShadowColor = new ArrayList<Integer>();
    private ArrayList<Integer> BackgroundColor = new ArrayList<Integer>();
	private ArrayList<Boolean> FloatSize = new ArrayList<Boolean>();
	private ArrayList<Float> FloatLong = new ArrayList<Float>();
	private ArrayList<Float> FloatWide = new ArrayList<Float>();

	public void setTextShadowRadius(ArrayList<Float> textShadowRadius)
	{
		TextShadowRadius = textShadowRadius;
	}

	public ArrayList<Float> getTextShadowRadius()
	{
		return TextShadowRadius;
	}

	public void setTextShadowColor(ArrayList<Integer> textShadowColor)
	{
		TextShadowColor = textShadowColor;
	}

	public ArrayList<Integer> getTextShadowColor()
	{
		return TextShadowColor;
	}

	public void setBackgroundColor(ArrayList<Integer> backgroundColor)
	{
		BackgroundColor = backgroundColor;
	}

	public ArrayList<Integer> getBackgroundColor()
	{
		return BackgroundColor;
	}

	public void setFloatSize(ArrayList<Boolean> floatSize)
	{
		FloatSize = floatSize;
	}

	public ArrayList<Boolean> getFloatSize()
	{
		return FloatSize;
	}

	public void setFloatLong(ArrayList<Float> floatLong)
	{
		FloatLong = floatLong;
	}

	public ArrayList<Float> getFloatLong()
	{
		return FloatLong;
	}

	public void setFloatWide(ArrayList<Float> floatWide)
	{
		FloatWide = floatWide;
	}

	public ArrayList<Float> getFloatWide()
	{
		return FloatWide;
	}

	public void setTextShadowX(ArrayList<Float> textShadowX)
	{
		TextShadowX = textShadowX;
	}

	public ArrayList<Float> getTextShadowX()
	{
		return TextShadowX;
	}

	public void setTextShadowY(ArrayList<Float> textShadowY)
	{
		TextShadowY = textShadowY;
	}

	public ArrayList<Float> getTextShadowY()
	{
		return TextShadowY;
	}

	public void setTextSpeed(ArrayList<Integer> textSpeed)
	{
		TextSpeed = textSpeed;
	}

	public ArrayList<Integer> getTextSpeed()
	{
		return TextSpeed;
	}

	public void setTextMove(ArrayList<Boolean> textMove)
	{
		TextMove = textMove;
	}

	public ArrayList<Boolean> getTextMove()
	{
		return TextMove;
	}

	public void setTextTop(ArrayList<Boolean> textTop)
	{
		TextTop = textTop;
	}

	public ArrayList<Boolean> getTextTop()
	{
		return TextTop;
	}

	public void setLockPosition(ArrayList<Boolean> lockPosition)
	{
		LockPosition = lockPosition;
	}

	public ArrayList<Boolean> getLockPosition()
	{
		return LockPosition;
	}

	public void setAutoTop(ArrayList<Boolean> autoTop)
	{
		AutoTop = autoTop;
	}

	public ArrayList<Boolean> getAutoTop()
	{
		return AutoTop;
	}

	public void setTextShadow(ArrayList<Boolean> textShadow)
	{
		TextShadow = textShadow;
	}

	public ArrayList<Boolean> getTextShadow()
	{
		return TextShadow;
	}

	public void setPosition(ArrayList<String> position)
	{
		Position = position;
	}

	public ArrayList<String> getPosition()
	{
		return Position;
	}

	public void setThickShow(ArrayList<Boolean> thickShow)
	{
		ThickShow = thickShow;
	}

	public ArrayList<Boolean> getThickShow()
	{
		return ThickShow;
	}

	public void setShowFloat(ArrayList<Boolean> showFloat)
	{
		ShowFloat = showFloat;
	}

	public ArrayList<Boolean> getShowFloat()
	{
		return ShowFloat;
	}

	public void setSizeShow(ArrayList<Float> sizeShow)
	{
		SizeShow = sizeShow;
	}

	public ArrayList<Float> getSizeShow()
	{
		return SizeShow;
	}

	public void setColorShow(ArrayList<Integer> colorShow)
	{
		ColorShow = colorShow;
	}

	public ArrayList<Integer> getColorShow()
	{
		return ColorShow;
	}

	public void setTextShow(ArrayList<String> textShow)
	{
		TextShow = textShow;
	}

	public ArrayList<String> getTextShow()
	{
		return TextShow;
	}
	
	public void addDatas (String text, int color, float size, boolean thick, boolean show, String position, boolean lp, boolean tp, boolean ap, boolean tm, int sp, boolean sha, float shax, float shay, float shad, int bac, int tsc, boolean fs, float fl, float fw)
    {
        TextShow.add(text);
        ColorShow.add(color);
        SizeShow.add(size);
        ThickShow.add(thick);
        ShowFloat.add(show);
        Position.add(position);
        LockPosition.add(lp);
        TextTop.add(tp);
        AutoTop.add(ap);
        TextMove.add(tm);
        TextSpeed.add(sp);
        TextShadow.add(sha);
        TextShadowX.add(shax);
        TextShadowY.add(shay);
        TextShadowRadius.add(shad);
        BackgroundColor.add(bac);
        TextShadowColor.add(tsc);
		FloatSize.add(fs);
		FloatLong.add(fl);
		FloatWide.add(fw);
    }
	
	public void replaceDatas (ArrayList<String> text, ArrayList<Integer> color, ArrayList<Float> size, ArrayList<Boolean> thick, ArrayList<Boolean> show, ArrayList<String> position, ArrayList<Boolean> lp, ArrayList<Boolean> tp, ArrayList<Boolean> ap, ArrayList<Boolean> tm, ArrayList<Integer> sp, ArrayList<Boolean> sha, ArrayList<Float> shax, ArrayList<Float> shay, ArrayList<Float> shad, ArrayList<Integer> bac, ArrayList<Integer> tsc, ArrayList<Boolean> fs, ArrayList<Float> fl, ArrayList<Float> fw)
    {
        TextShow = text;
        ColorShow = color;
        SizeShow = size;
        ThickShow = thick;
        ShowFloat = show;
        Position = position;
        LockPosition = lp;
        TextTop = tp;
        AutoTop = ap;
        TextMove = tm;
        TextSpeed = sp;
        TextShadow = sha;
        TextShadowX = shax;
        TextShadowY = shay;
        TextShadowRadius = shad;
        BackgroundColor = bac;
        TextShadowColor = tsc;
		FloatSize = fs;
		FloatLong = fl;
		FloatWide = fw;
    }

    public void removeDatas (int i)
    {
        TextShow.remove(i);
        ColorShow.remove(i);
        SizeShow.remove(i);
        ThickShow.remove(i);
		ShowFloat.remove(i);
        Position.remove(i);
        LockPosition.remove(i);
        TextTop.remove(i);
        AutoTop.remove(i);
        TextMove.remove(i);
        TextSpeed.remove(i);
        TextShadow.remove(i);
        TextShadowX.remove(i);
        TextShadowY.remove(i);
        TextShadowRadius.remove(i);
        BackgroundColor.remove(i);
        TextShadowColor.remove(i);
		FloatSize.remove(i);
		FloatLong.remove(i);
		FloatWide.remove(i);
    }
	
	public void setDatas (int i, String text, int color, float size, boolean thick, boolean show, String position, boolean lp, boolean tp, boolean ap, boolean tm, int sp, boolean sha, float shax, float shay, float shad, int bac, int tsc, boolean fs, float fl, float fw)
    {
        TextShow.set(i, text);
        ColorShow.set(i, color);
        SizeShow.set(i, size);
        ThickShow.set(i, thick);
        ShowFloat.set(i, show);
        Position.set(i, position);
        LockPosition.set(i, lp);
        TextTop.set(i, tp);
        AutoTop.set(i, ap);
        TextMove.set(i, tm);
        TextSpeed.set(i, sp);
        TextShadow.set(i, sha);
        TextShadowX.set(i, shax);
        TextShadowY.set(i, shay);
        TextShadowRadius.set(i, shad);
        BackgroundColor.set(i, bac);
        TextShadowColor.set(i, tsc);
		FloatSize.set(i, fs);
		FloatLong.set(i, fl);
		FloatWide.set(i, fw);
    }
}
