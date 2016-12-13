package tool.xfy9326.floattext.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.Utils.FloatData;

public class FloatLinearLayout extends LinearLayout
{
    private Context ctx;
    private int FLOAT_ID = -1;
    private boolean lockposition = false;
    private boolean top = false;
    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;
    private boolean ShowState = true;
    private int layout_default_flags;
    private float mStartX;
    private float mStartY;
    private float mTouchX;
    private float mTouchY;
    private float LastX;
    private float LastY;
    private float x;
    private float y;
    private float px = 0;
    private float py = 0;
    private int statusBarHeight = 0;
    private long startlongclicktime = 0;
    private long nowlongclicktime = 0;
    private double longclicksecond = 1;
    private boolean longclickmove = false;
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    if (FLOAT_ID != -1)
                    {
                        ArrayList<Boolean> lock = ((App)ctx.getApplicationContext()).getLockPosition();
                        ArrayList<String> position = ((App)ctx.getApplicationContext()).getPosition();
                        if (FLOAT_ID < lock.size())
                        {
                            setPositionLocked(true);
                            lock.set(FLOAT_ID, true);
                            position.set(FLOAT_ID, getPosition());
                            FloatData dat = new FloatData();
                            dat.savedata(ctx);
                            ((App)ctx.getApplicationContext()).getListviewadapter().notifyDataSetChanged();
                            Toast.makeText(ctx, R.string.text_lock, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
	};

    public FloatLinearLayout (Context context, int ID)
    {
        super(context);
        this.ctx = context.getApplicationContext();
        this.FLOAT_ID = ID;
        this.wm = ((App)context.getApplicationContext()).getFloatwinmanager();
    }

    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
        if (!lockposition)
        {
            x = event.getRawX() + px;
            if (top)
            {
                Rect frame = new Rect(); 
                getWindowVisibleDisplayFrame(frame);
                statusBarHeight = frame.top;
                y = event.getRawY() + py - statusBarHeight;
            }
            else
            {
                y = event.getRawY() + py;
            }
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    mStartX = x;
                    mStartY = y;
                    mTouchX = event.getX() + px;
                    mTouchY = event.getY() + py;
                    setlongclick();
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateViewPosition();
                    if (Math.abs(x - mStartX) > 10 || Math.abs(y - mStartY) > 10)
                    {
                        longclickmove = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    longclickmove = true;
                    startlongclicktime = 0;
                    nowlongclicktime = 0;
                    updateViewPosition();
                    mTouchX = mTouchY = 0;
                    break;
            }
        }
        return true;
    }

    private void setlongclick ()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (sp.getBoolean("WinLongClickLock", true))
        {
            longclickmove = false;
            startlongclicktime = System.currentTimeMillis();
            Thread longclick = new Thread(){
                @Override
                public void run ()
                {
                    super.run();
                    while (!longclickmove)
                    {
                        try
                        {
                            nowlongclicktime = System.currentTimeMillis();
                            if (nowlongclicktime - startlongclicktime >= longclicksecond * 1000)
                            {
                                mHandler.obtainMessage(0).sendToTarget();
                                longclickmove = true;
                                startlongclicktime = 0;
                                nowlongclicktime = 0;
                            }
                            else
                            {
                                Thread.sleep(100);
                            }
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            };
            longclick.start();
        }
    }

    public void setTop (boolean bool)
    {
        this.top = bool;
    }

    public void setAddPosition (float sx, float sy)
    {
        this.px = sx;
        this.py = sy;
        this.LastX = sx;
        this.LastY = sy;
    }

    public void setShowState (boolean bool)
    {
        if (bool)
        {
            if (!ShowState)
            {
                wm.addView(this, wmParams);
                ShowState = true;
            }
        }
        else
        {
            if (ShowState)
            {
                wm.removeView(this);
                ShowState = false;
            }
        }
    }

    public void changeShowState (boolean bool)
    {
        this.ShowState = bool;
    }

    public void setFloatLayoutParams (WindowManager.LayoutParams wmLayoutParams)
    {
        this.wmParams = wmLayoutParams;
    }

    public void setTouchable (WindowManager.LayoutParams layout, boolean bool)
    {
        if (bool)
        {
            layout.flags = layout_default_flags;
        }
        else
        {
            layout.flags += WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        }
        if (ShowState)
        {
            wm.updateViewLayout(this, layout);
        }
    }

    public void setLayout_default_flags (int layout_default_flags)
    {
        this.layout_default_flags = layout_default_flags;
    }

    public void setPositionLocked (boolean bool)
    {
        this.lockposition = bool;
        setTouchable(wmParams, !bool);
    }

    public boolean getPositionLocked ()
    {
        return lockposition;
    }

    private void updateViewPosition ()
    {
        LastX = x - mTouchX;
        LastY = y - mTouchY;
        wmParams.x = (int)LastX;
        wmParams.y = (int)LastY;
        wm.updateViewLayout(this, wmParams);
    }

    public String getPosition ()
    {
        LastX = wmParams.x;
        LastY = wmParams.y;
        return ((LastX) + "_" + (LastY)).toString();
    }

}
