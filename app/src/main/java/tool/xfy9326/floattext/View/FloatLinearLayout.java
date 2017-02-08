package tool.xfy9326.floattext.View;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Utils.*;

/*
悬浮窗线性布局
主要用于进行窗体操作
比如锁定，移动，显示在通知栏等
*/

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
	//长按处理
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
                        if (FLOAT_ID < lock.size() && lockposition == false)
                        {
                            setPositionLocked(true);
                            lock.set(FLOAT_ID, true);
                            position.set(FLOAT_ID, getPosition());
                            FloatData dat = new FloatData(ctx);
                            dat.savedata();
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

	//窗体移动
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

	//长按判断
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

	//窗口在通知栏上显示
    public void setTop (boolean bool)
    {
        this.top = bool;
    }

	//设置初始位置
    public void setAddPosition (float sx, float sy)
    {
        this.px = sx;
        this.py = sy;
        this.LastX = sx;
        this.LastY = sy;
    }

	//设置窗体状态
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

	//修正改变窗体状态
    public void changeShowState (boolean bool)
    {
        this.ShowState = bool;
    }

	//设置窗体布局
    public void setFloatLayoutParams (WindowManager.LayoutParams wmLayoutParams)
    {
        this.wmParams = wmLayoutParams;
    }

	//设置可触摸
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

	//设置默认Flag
    public void setLayout_default_flags (int layout_default_flags)
    {
        this.layout_default_flags = layout_default_flags;
    }

	//锁定窗口
    public void setPositionLocked (boolean bool)
    {
        this.lockposition = bool;
        setTouchable(wmParams, !bool);
    }

	//获取锁定状态
    public boolean getPositionLocked ()
    {
        return lockposition;
    }

	//更新位置
    private void updateViewPosition ()
    {
        LastX = x - mTouchX;
        LastY = y - mTouchY;
        wmParams.x = (int)LastX;
        wmParams.y = (int)LastY;
        wm.updateViewLayout(this, wmParams);
    }

	//获取位置
    public String getPosition ()
    {
        LastX = wmParams.x;
        LastY = wmParams.y;
        return ((LastX) + "_" + (LastY)).toString();
    }

}
