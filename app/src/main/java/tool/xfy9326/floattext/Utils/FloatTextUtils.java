package tool.xfy9326.floattext.Utils;

import java.util.ArrayList;

public class FloatTextUtils {
    private ArrayList<String> TextShow = new ArrayList<>();
    private ArrayList<Integer> ColorShow = new ArrayList<>();
    private ArrayList<Float> SizeShow = new ArrayList<>();
    private ArrayList<Boolean> ThickShow = new ArrayList<>();
    private ArrayList<Boolean> ShowFloat = new ArrayList<>();
    private ArrayList<String> Position = new ArrayList<>();
    private ArrayList<Boolean> LockPosition = new ArrayList<>();
    private ArrayList<Boolean> TextTop = new ArrayList<>();
    private ArrayList<Boolean> AutoTop = new ArrayList<>();
    private ArrayList<Boolean> TextMove = new ArrayList<>();
    private ArrayList<Integer> TextSpeed = new ArrayList<>();
    private ArrayList<Boolean> TextShadow = new ArrayList<>();
    private ArrayList<Float> TextShadowX = new ArrayList<>();
    private ArrayList<Float> TextShadowY = new ArrayList<>();
    private ArrayList<Float> TextShadowRadius = new ArrayList<>();
    private ArrayList<Integer> TextShadowColor = new ArrayList<>();
    private ArrayList<Integer> BackgroundColor = new ArrayList<>();
    private ArrayList<Boolean> FloatSize = new ArrayList<>();
    private ArrayList<Float> FloatLong = new ArrayList<>();
    private ArrayList<Float> FloatWide = new ArrayList<>();
    private ArrayList<Boolean> NotifyControl = new ArrayList<>();

    public ArrayList<Boolean> getNotifyControl() {
        return NotifyControl;
    }

    public void setNotifyControl(ArrayList<Boolean> notifyControl) {
        NotifyControl = notifyControl;
    }

    public ArrayList<Float> getTextShadowRadius() {
        return TextShadowRadius;
    }

    public void setTextShadowRadius(ArrayList<Float> textShadowRadius) {
        TextShadowRadius = textShadowRadius;
    }

    public ArrayList<Integer> getTextShadowColor() {
        return TextShadowColor;
    }

    public void setTextShadowColor(ArrayList<Integer> textShadowColor) {
        TextShadowColor = textShadowColor;
    }

    public ArrayList<Integer> getBackgroundColor() {
        return BackgroundColor;
    }

    public void setBackgroundColor(ArrayList<Integer> backgroundColor) {
        BackgroundColor = backgroundColor;
    }

    public ArrayList<Boolean> getFloatSize() {
        return FloatSize;
    }

    public void setFloatSize(ArrayList<Boolean> floatSize) {
        FloatSize = floatSize;
    }

    public ArrayList<Float> getFloatLong() {
        return FloatLong;
    }

    public void setFloatLong(ArrayList<Float> floatLong) {
        FloatLong = floatLong;
    }

    public ArrayList<Float> getFloatWide() {
        return FloatWide;
    }

    public void setFloatWide(ArrayList<Float> floatWide) {
        FloatWide = floatWide;
    }

    public ArrayList<Float> getTextShadowX() {
        return TextShadowX;
    }

    public void setTextShadowX(ArrayList<Float> textShadowX) {
        TextShadowX = textShadowX;
    }

    public ArrayList<Float> getTextShadowY() {
        return TextShadowY;
    }

    public void setTextShadowY(ArrayList<Float> textShadowY) {
        TextShadowY = textShadowY;
    }

    public ArrayList<Integer> getTextSpeed() {
        return TextSpeed;
    }

    public void setTextSpeed(ArrayList<Integer> textSpeed) {
        TextSpeed = textSpeed;
    }

    public ArrayList<Boolean> getTextMove() {
        return TextMove;
    }

    public void setTextMove(ArrayList<Boolean> textMove) {
        TextMove = textMove;
    }

    public ArrayList<Boolean> getTextTop() {
        return TextTop;
    }

    public void setTextTop(ArrayList<Boolean> textTop) {
        TextTop = textTop;
    }

    public ArrayList<Boolean> getLockPosition() {
        return LockPosition;
    }

    public void setLockPosition(ArrayList<Boolean> lockPosition) {
        LockPosition = lockPosition;
    }

    public ArrayList<Boolean> getAutoTop() {
        return AutoTop;
    }

    public void setAutoTop(ArrayList<Boolean> autoTop) {
        AutoTop = autoTop;
    }

    public ArrayList<Boolean> getTextShadow() {
        return TextShadow;
    }

    public void setTextShadow(ArrayList<Boolean> textShadow) {
        TextShadow = textShadow;
    }

    public ArrayList<String> getPosition() {
        return Position;
    }

    public void setPosition(ArrayList<String> position) {
        Position = position;
    }

    public ArrayList<Boolean> getThickShow() {
        return ThickShow;
    }

    public void setThickShow(ArrayList<Boolean> thickShow) {
        ThickShow = thickShow;
    }

    public ArrayList<Boolean> getShowFloat() {
        return ShowFloat;
    }

    public void setShowFloat(ArrayList<Boolean> showFloat) {
        ShowFloat = showFloat;
    }

    public ArrayList<Float> getSizeShow() {
        return SizeShow;
    }

    public void setSizeShow(ArrayList<Float> sizeShow) {
        SizeShow = sizeShow;
    }

    public ArrayList<Integer> getColorShow() {
        return ColorShow;
    }

    public void setColorShow(ArrayList<Integer> colorShow) {
        ColorShow = colorShow;
    }

    public ArrayList<String> getTextShow() {
        return TextShow;
    }

    public void setTextShow(ArrayList<String> textShow) {
        TextShow = textShow;
    }

    public void addDatas(String text, int color, float size, boolean thick, boolean show, String position, boolean lp, boolean tp, boolean ap, boolean tm, int sp, boolean sha, float shax, float shay, float shad, int bac, int tsc, boolean fs, float fl, float fw, boolean nc) {
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
        NotifyControl.add(nc);
    }

    public void replaceDatas(ArrayList<String> text, ArrayList<Integer> color, ArrayList<Float> size, ArrayList<Boolean> thick, ArrayList<Boolean> show, ArrayList<String> position, ArrayList<Boolean> lp, ArrayList<Boolean> tp, ArrayList<Boolean> ap, ArrayList<Boolean> tm, ArrayList<Integer> sp, ArrayList<Boolean> sha, ArrayList<Float> shax, ArrayList<Float> shay, ArrayList<Float> shad, ArrayList<Integer> bac, ArrayList<Integer> tsc, ArrayList<Boolean> fs, ArrayList<Float> fl, ArrayList<Float> fw, ArrayList<Boolean> nc) {
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
        NotifyControl = nc;
    }

    public void removeDatas(int i) {
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
        NotifyControl.remove(i);
    }

    public void setDatas(int i, String text, int color, float size, boolean thick, boolean show, String position, boolean lp, boolean tp, boolean ap, boolean tm, int sp, boolean sha, float shax, float shay, float shad, int bac, int tsc, boolean fs, float fl, float fw, boolean nc) {
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
        NotifyControl.set(i, nc);
    }
}
