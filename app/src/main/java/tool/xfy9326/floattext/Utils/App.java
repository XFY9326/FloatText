package tool.xfy9326.floattext.Utils;

import android.app.*;
import android.view.*;
import java.util.*;
import lib.xfy9326.crashreport.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.View.*;

public class App extends Application
{
    private ArrayList<FloatTextView> floatview = new ArrayList<FloatTextView>();
    private ArrayList<String> floattext = new ArrayList<String>();
    private ArrayList<WindowManager.LayoutParams> floatlayout = new ArrayList<WindowManager.LayoutParams>();
    private ArrayList<FloatLinearLayout> floatlinearlayout = new ArrayList<FloatLinearLayout>();
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
    public void onCreate ()
    {
        super.onCreate();
        init();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this, "FloatText", "tool.xfy9326.floattext.FloatManage", "1069665464@qq.com");
    }
    
    private void init()
    {
        SafeGuard.isSignatureAvailable(this);
        SafeGuard.isPackageNameAvailable(this);
    }
	
	public ArrayList<Float> getFloatWide ()
	{
		return FloatWide;
	}

	public ArrayList<Float> getFloatLong ()
	{
		return FloatLong;
	}

	public ArrayList<Boolean> getFloatSize ()
	{
		return FloatSize;
	}
    
    public void setGetSave (boolean b)
    {
        this.GetSave = b;
    }

    public ArrayList<Integer> getTextShadowColor ()
    {
        return TextShadowColor;
    }

    public ArrayList<Integer> getBackgroundColor ()
    {
        return BackgroundColor;
    }

    public ArrayList<Float> getTextShadowRadius ()
    {
        return TextShadowRadius;
    }

    public ArrayList<Float> getTextShadowY ()
    {
        return TextShadowY;
    }

    public ArrayList<Float> getTextShadowX ()
    {
        return TextShadowX;
    }

    public ArrayList<Boolean> getTextShadow ()
    {
        return TextShadow;
    }

    public void setListviewadapter (ListViewAdapter listviewadapter)
    {
        this.listviewadapter = listviewadapter;
    }

    public ListViewAdapter getListviewadapter ()
    {
        return listviewadapter;
    }

    public void setFloatwinmanager (WindowManager floatwinmanager)
    {
        this.floatwinmanager = floatwinmanager;
    }

    public WindowManager getFloatwinmanager ()
    {
        return floatwinmanager;
    }

    public void setListTextHide (boolean listTextHide)
    {
        ListTextHide = listTextHide;
    }

    public boolean getListTextHide ()
    {
        return ListTextHide;
    }

    public void setHtmlMode (boolean htmlMode)
    {
        HtmlMode = htmlMode;
    }

    public boolean getHtmlMode ()
    {
        return HtmlMode;
    }

    public void setDevelopMode (boolean developMode)
    {
        DevelopMode = developMode;
    }

    public boolean getDevelopMode ()
    {
        return DevelopMode;
    }

    public void setFloatlinearlayout (ArrayList<FloatLinearLayout> floatlinearlayout)
    {
        this.floatlinearlayout = floatlinearlayout;
    }

    public ArrayList<FloatLinearLayout> getFloatlinearlayout ()
    {
        return floatlinearlayout;
    }

    public void setDynamicNumService (boolean DynamicNumService)
    {
        this.DynamicNumService = DynamicNumService;
    }

    public boolean getDynamicNumService ()
    {
        return DynamicNumService;
    }

    public void setStayAliveService (boolean stayAliveService)
    {
        StayAliveService = stayAliveService;
    }

    public boolean getStayAliveService ()
    {
        return StayAliveService;
    }

    public void setMovingMethod (boolean movingMethod)
    {
        MovingMethod = movingMethod;
    }

    public boolean getMovingMethod ()
    {
        return MovingMethod;
    }

    public ArrayList<Integer> getTextSpeed ()
    {
        return TextSpeed;
    }

    public ArrayList<Boolean> getTextMove ()
    {
        return TextMove;
    }

    public ArrayList<Boolean> getAutoTop ()
    {
        return AutoTop;
    }

    public ArrayList<Boolean> getTextTop ()
    {
        return TextTop;
    }

    public ArrayList<Boolean> getLockPosition ()
    {
        return LockPosition;
    }

    public ArrayList<String> getPosition ()
    {
        return Position;
    }

    public void setShowFloat (ArrayList<Boolean> showfloat)
    {
        this.ShowFloat = showfloat;
    }

    public ArrayList<Boolean> getShowFloat ()
    {
        return ShowFloat;
    }

    public void setFloatReshow (boolean bool)
    {
        FloatWinReshow = bool;
    }

    public ArrayList<FloatTextView> getFloatView ()
    {
        return this.floatview;
    }

    public void setFloatView (ArrayList<FloatTextView> floatview)
    {
        this.floatview = floatview;
    }

    public ArrayList<String> getFloatText ()
    {
        return this.floattext;
    }

    public void setFloatText (ArrayList<String> floattext)
    {
        this.floattext = floattext;
    }

    public ArrayList<WindowManager.LayoutParams> getFloatLayout ()
    {
        return this.floatlayout;
    }

    public void setFloatLayout (ArrayList<WindowManager.LayoutParams> floatlayout)
    {
        this.floatlayout = floatlayout;
    }

    public void setTextData (int i, String text)
    {
        TextShow.set(i, text);
    }

    public ArrayList<String> getTextData ()
    {
        return TextShow;
    }

    public ArrayList<Float> getSizeData ()
    {
        return SizeShow;
    }

    public ArrayList<Integer> getColorData ()
    {
        return ColorShow;
    }

    public ArrayList<Boolean> getThickData ()
    {
        return ThickShow;
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

    public void setDatas (int i, FloatTextView fv, FloatLinearLayout fll, WindowManager.LayoutParams layout, String text, int color, float size, boolean thick, boolean show, String position, boolean lp, boolean tp, boolean ap, boolean tm, int sp, boolean sha, float shax, float shay, float shad, int bac, int tsc, boolean fs, float fl, float fw)
    {
        floatview.set(i, fv);
        floatlinearlayout.set(i, fll);
        floatlayout.set(i, layout);
        TextShow.set(i, text);
        floattext.set(i, text);
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

}
