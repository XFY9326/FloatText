package tool.xfy9326.floattext.Utils;

import android.view.WindowManager;

import java.util.ArrayList;

import tool.xfy9326.floattext.View.FloatLinearLayout;
import tool.xfy9326.floattext.View.FloatTextView;

public class FloatFrameUtils {
    private ArrayList<FloatTextView> floatview = new ArrayList<>();
    private ArrayList<String> floattext = new ArrayList<>();
    private ArrayList<WindowManager.LayoutParams> floatlayout = new ArrayList<>();
    private ArrayList<FloatLinearLayout> floatlinearlayout = new ArrayList<>();
    private ArrayList<String> FilterApplication = new ArrayList<>();

    public ArrayList<FloatTextView> getFloatview() {
        return floatview;
    }

    public void setFloatview(ArrayList<FloatTextView> floatview) {
        this.floatview = floatview;
    }

    public ArrayList<String> getFloattext() {
        return floattext;
    }

    public void setFloattext(ArrayList<String> floattext) {
        this.floattext = floattext;
    }

    public ArrayList<WindowManager.LayoutParams> getFloatlayout() {
        return floatlayout;
    }

    public void setFloatlayout(ArrayList<WindowManager.LayoutParams> floatlayout) {
        this.floatlayout = floatlayout;
    }

    public ArrayList<FloatLinearLayout> getFloatlinearlayout() {
        return floatlinearlayout;
    }

    public void setFloatlinearlayout(ArrayList<FloatLinearLayout> floatlinearlayout) {
        this.floatlinearlayout = floatlinearlayout;
    }

    public ArrayList<String> getFilterApplication() {
        return FilterApplication;
    }

    public void setFilterApplication(ArrayList<String> filterApplication) {
        FilterApplication = filterApplication;
    }

    public void setDatas(int i, FloatTextView fv, FloatLinearLayout fll, WindowManager.LayoutParams layout, String text) {
        floatview.set(i, fv);
        floatlinearlayout.set(i, fll);
        floatlayout.set(i, layout);
        floattext.set(i, text);
    }
}
