package tool.xfy9326.floattext.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;
import tool.xfy9326.floattext.Utils.App;
import android.graphics.Color;

public class FloatTextView extends TextView implements Runnable
{
    private Context ctx;
    private WindowManager wm;
    private int currentScrollX;
    private boolean isStop = true; 
    private int textWidth; 
    private boolean isMeasure = false; 
    private int movespeed = 5;
    private int ID = 0;
    private boolean htmlmode = true;

    public FloatTextView (Context context, boolean htmlmode)
    {
        super(context);
        this.ctx = context.getApplicationContext();
        this.htmlmode = htmlmode;
        this.wm = ((App)context.getApplicationContext()).getFloatwinmanager();
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    @Override
    public void setText (CharSequence text, TextView.BufferType type)
    {
        text = htmlcodefix(text);
        super.setText(text, type);
        getTextWidth();
        invalidate();
    }

    private CharSequence htmlcodefix (CharSequence text)
    {
        if (htmlmode)
        {
            if (text.toString().matches("^#(\\[HTML\\])"))
            {
                String str = text.toString().replace("#[HTML]", "");
                text = Html.fromHtml(str);
            }
        }
        return text;
    }

    public void setMoveSpeed (int i)
    {
        this.movespeed = i;
        invalidate();
    }

    @Override 
    protected void onDraw (Canvas canvas)
    { 
        super.onDraw(canvas); 
        if (!isMeasure)
        {
            getTextWidth(); 
            isMeasure = true; 
        }
        invalidate();
    }

    private void getTextWidth ()
    { 
        Paint paint = this.getPaint(); 
        String str = this.getText().toString(); 
        textWidth = (int) paint.measureText(str); 
    }

    @Override 
    public void run ()
    { 
        currentScrollX += movespeed;
        scrollTo(currentScrollX, 0); 
        if (isStop)
        { 
            return; 
        } 
        if (getScrollX() >= textWidth)
        { 
            scrollTo(textWidth, 0);
            currentScrollX = -wm.getDefaultDisplay().getWidth();
        }
        postDelayed(this, 10);
        invalidate();
    } 

    public void startScroll ()
    {
        this.removeCallbacks(this);
        post(this);
        isStop = false;
        invalidate();
    } 

    public void stopScroll ()
    {
        scrollTo(textWidth, 0);
        currentScrollX = 0;
        isStop = true;
        invalidate();
    }

    public void setID (int i)
    {
        ID = i;
    }

    public void setTypefaceFile (String ttf)
    {
        if (!ttf.equalsIgnoreCase("Default"))
        {
            Typeface tf = Typeface.createFromFile(ttf);
            setTypeface(tf);
        }
    }
    
    public void setShadow(boolean shadow, float x, float y, float r, int color)
    {
        if(shadow)
        {
            setShadowLayer(r, x, y, color);
        }
        else
        {
            setShadowLayer(0, 0, 0, color);
        }
    }

    public void setMoving (boolean bool, int mode)
    {
        switch (mode)
        {
            case 0:
                if (bool)
                {
                    if (isStop)
                    {
                        setSingleLine(true);
                        startScroll();
                    }
                }
                else
                {
                    if (!isStop)
                    {
                        stopScroll();
                        setSingleLine(false);
                    }
                }
                break;
            case 1:
                if (bool)
                {
                    setSingleLine(true);
                    setFocusable(true);
                    setFocusableInTouchMode(true);
                    setMarqueeRepeatLimit(-1);
                    setEllipsize(TextUtils.TruncateAt.MARQUEE);
                }
                else
                {
                    setSingleLine(false);
                    setFocusable(false);
                    setFocusableInTouchMode(false);
                    setMarqueeRepeatLimit(0);
                }
                break;
        }
    }
    
}
