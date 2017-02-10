package tool.xfy9326.floattext.Utils;

import android.app.*;
import android.view.*;
import java.util.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.CrashReport.*;
import tool.xfy9326.floattext.View.*;

/*
 数据缓存
 */

public class App extends Application
{
	private FloatTextUtils textutil;
	private FloatFrameUtils frameutil;
    private ListViewAdapter listviewadapter = null;
    private WindowManager floatwinmanager = null;
    public boolean MovingMethod = false;
    public boolean FloatWinReshow = true;
    public boolean StayAliveService = true;
    public boolean DynamicNumService = false;
    public boolean DevelopMode = false;
    public boolean HtmlMode = false;
    public boolean ListTextHide = false;
    public boolean GetSave = false;

    @Override
    public void onCreate()
    {
        super.onCreate();
        init();
		//错误报告
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this, "FloatText", "tool.xfy9326.floattext.FloatManage", "1069665464@qq.com");
    }

    private void init()
    {
		textutil = new FloatTextUtils();
		frameutil = new FloatFrameUtils();
        SafeGuard.isSignatureAvailable(this);
        SafeGuard.isPackageNameAvailable(this);
    }

    public void setGetSave(boolean b)
    {
        this.GetSave = b;
    }

    public void setListviewadapter(ListViewAdapter listviewadapter)
    {
        this.listviewadapter = listviewadapter;
    }

    public ListViewAdapter getListviewadapter()
    {
        return listviewadapter;
    }

    public void setFloatwinmanager(WindowManager floatwinmanager)
    {
        this.floatwinmanager = floatwinmanager;
    }

    public WindowManager getFloatwinmanager()
    {
        return floatwinmanager;
    }

    public void setListTextHide(boolean listTextHide)
    {
        ListTextHide = listTextHide;
    }

    public boolean getListTextHide()
    {
        return ListTextHide;
    }

    public void setHtmlMode(boolean htmlMode)
    {
        HtmlMode = htmlMode;
    }

    public boolean getHtmlMode()
    {
        return HtmlMode;
    }

    public void setDevelopMode(boolean developMode)
    {
        DevelopMode = developMode;
    }

    public boolean getDevelopMode()
    {
        return DevelopMode;
    }

    public void setDynamicNumService(boolean DynamicNumService)
    {
        this.DynamicNumService = DynamicNumService;
    }

    public boolean getDynamicNumService()
    {
        return DynamicNumService;
    }

    public void setStayAliveService(boolean stayAliveService)
    {
        StayAliveService = stayAliveService;
    }

    public boolean getStayAliveService()
    {
        return StayAliveService;
    }

    public void setMovingMethod(boolean movingMethod)
    {
        MovingMethod = movingMethod;
    }

    public boolean getMovingMethod()
    {
        return MovingMethod;
    }

    public void setFloatReshow(boolean bool)
    {
        FloatWinReshow = bool;
    }

    public ArrayList<String> getFloatText()
    {
        return frameutil.getFloattext();
    }

    public void setFloatText(ArrayList<String> floattext)
    {
        frameutil.setFloattext(floattext);
    }

    public ArrayList<String> getTextData()
    {
        return textutil.getTextShow();
    }

	public void setTextutil(FloatTextUtils textutil)
	{
		this.textutil = textutil;
	}

	public FloatTextUtils getTextutil()
	{
		return textutil;
	}
	
	public void setFrameutil(FloatFrameUtils frameutil)
	{
		this.frameutil = frameutil;
	}

	public FloatFrameUtils getFrameutil()
	{
		return frameutil;
	}

    public void setDatas(int i, FloatTextView fv, FloatLinearLayout fll, WindowManager.LayoutParams layout, String text, int color, float size, boolean thick, boolean show, String position, boolean lp, boolean tp, boolean ap, boolean tm, int sp, boolean sha, float shax, float shay, float shad, int bac, int tsc, boolean fs, float fl, float fw)
    {
        frameutil.setDatas(i, fv, fll, layout, text);
		textutil.setDatas(i, text, color, size, thick, show, position, lp, tp, ap, tm, sp, sha, shax, shay, shad, bac, tsc, fs, fl, fw);
    }

}
