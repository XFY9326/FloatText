package tool.xfy9326.floattext.Utils;

import tool.xfy9326.floattext.View.*;

import android.view.WindowManager;
import java.util.ArrayList;

public class FloatFrameUtils
{
	private ArrayList<FloatTextView> floatview = new ArrayList<FloatTextView>();
    private ArrayList<String> floattext = new ArrayList<String>();
    private ArrayList<WindowManager.LayoutParams> floatlayout = new ArrayList<WindowManager.LayoutParams>();
    private ArrayList<FloatLinearLayout> floatlinearlayout = new ArrayList<FloatLinearLayout>();
	private ArrayList<String> FilterApplication = new ArrayList<String>();

	public void setFloatview(ArrayList<FloatTextView> floatview)
	{
		this.floatview = floatview;
	}

	public ArrayList<FloatTextView> getFloatview()
	{
		return floatview;
	}

	public void setFloattext(ArrayList<String> floattext)
	{
		this.floattext = floattext;
	}

	public ArrayList<String> getFloattext()
	{
		return floattext;
	}

	public void setFloatlayout(ArrayList<WindowManager.LayoutParams> floatlayout)
	{
		this.floatlayout = floatlayout;
	}

	public ArrayList<WindowManager.LayoutParams> getFloatlayout()
	{
		return floatlayout;
	}

	public void setFloatlinearlayout(ArrayList<FloatLinearLayout> floatlinearlayout)
	{
		this.floatlinearlayout = floatlinearlayout;
	}

	public ArrayList<FloatLinearLayout> getFloatlinearlayout()
	{
		return floatlinearlayout;
	}

	public void setFilterApplication(ArrayList<String> filterApplication)
	{
		FilterApplication = filterApplication;
	}

	public ArrayList<String> getFilterApplication()
	{
		return FilterApplication;
	}

	public void setDatas(int i, FloatTextView fv, FloatLinearLayout fll, WindowManager.LayoutParams layout, String text)
	{
		floatview.set(i, fv);
        floatlinearlayout.set(i, fll);
        floatlayout.set(i, layout);
		floattext.set(i, text);
	}
}
