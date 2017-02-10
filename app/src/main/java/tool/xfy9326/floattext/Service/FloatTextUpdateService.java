package tool.xfy9326.floattext.Service;

import android.app.*;
import android.content.*;
import android.hardware.*;
import android.os.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;

public class FloatTextUpdateService extends Service
{
	private static String[] LIST;
	private static int[] INFO;
    private Timer timer = null;
    private Timer timer_f = null;
    private Timer timer_s = null;
    private Timer timer_a = null;
    private SimpleDateFormat sdf12 = null;
    private SimpleDateFormat sdf24 = null;
    private SimpleDateFormat sdf_clock_12 = null;
    private SimpleDateFormat sdf_clock_24 = null;
    private SimpleDateFormat sdf_date = null;
	private SimpleDateFormat sdf_week = null;
	private String time0;
	private String time1;
	private String time2;
	private String time3;
	private String time4;
    private Intent timeIntent = null;
    private boolean timer_run = false;
    private boolean high_cpu_use_dynamicword = false;
	private boolean time_dynamicword = false;
	private Bundle bundle = null;
    private String cpurate;
    private String meminfo;
    private float lastTotalRxBytes = 0;
    private float lastTotalTxBytes = 0;
    private long lastTimeStamp = 0;
    private String localip;
    private BatteryReceiver breceiver = new BatteryReceiver();
	private AdvanceTextReceiver atr = new AdvanceTextReceiver();
    private IntentFilter battery_filter = null;
    private String battery_percent;
    private boolean sensor_use_dynamic_word = false;
    private SensorReceiver sreceiver = new SensorReceiver();
    private SensorManager msensor = null;
    private Sensor sensor_tem = null;
    private String cputemperature;
    private Sensor sensor_light = null;
    private String sensorlight;
    private Sensor sensor_gravity = null;
    private String sensorgravity;
    private Sensor sensor_pressure = null;
    private String sensorpressure;
    private Sensor sensor_proximity = null;
    private String sensorproximity;
    private Sensor sensor_step = null;
    private int sensorstep = 0;
    private ClipboardManager clip = null;
    private boolean register_sensor = false;
	private String currentactivity;
	private String notifymes;
	private String toasts;
	private String week;

    @Override
    public IBinder onBind(Intent p1)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        init();
        timerset();
    }

	private String[] SetPostKey()
	{
		String[] PostList = new String[22];
		PostList[0] = time0;
		PostList[1] = time1;
		PostList[2] = time2;
		PostList[3] = time3;
		PostList[4] = time4;
		PostList[5] = cpurate;
		PostList[6] = getNetSpeed();
		PostList[7] = meminfo;
		PostList[8] = localip;
		PostList[9] = battery_percent;
		PostList[10] = sensorlight;
		PostList[11] = sensorgravity;
		PostList[12] = sensorpressure;
		PostList[13] = cputemperature;
		PostList[14] = sensorproximity;
		PostList[15] = sensorstep + "";
		PostList[16] = getClip();
		PostList[17] = currentactivity;
		PostList[18] = notifymes;
		PostList[19] = toasts;
		PostList[20] = week;
		PostList[21] = "None";
		return PostList;
	}

	private void setDefaultKey()
	{
		String str = getString(R.string.loading);
		time0 = 
			time1 = 
			time2 = 
			time3 = 
			time4 = 
			cpurate = 
			meminfo = 
			localip = 
			battery_percent = 
			cputemperature = 
			sensorlight = 
			sensorgravity = 
			sensorpressure = 
			sensorproximity = 
			currentactivity =
			notifymes = 
			toasts =
			week = str;
	}

    private void init()
    {
		IntentFilter filter = new IntentFilter();
		filter.addAction(FloatServiceMethod.TEXT_ADVANCE_UPDATE_ACTION);
		registerReceiver(atr, filter);
        timer_a = new Timer();
        sdf12 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf_clock_12 = new SimpleDateFormat("hh:mm:ss");
        sdf_clock_24 = new SimpleDateFormat("HH:mm:ss");
        sdf_date = new SimpleDateFormat("yyyy-MM-dd");
		sdf_week = new SimpleDateFormat("E");
        timeIntent = new Intent();
		timeIntent.setAction(FloatServiceMethod.TEXT_UPDATE_ACTION);
		if (Build.VERSION.SDK_INT >= 12)
		{
			timeIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		}
        lastTotalRxBytes = FloatServiceMethod.getTotalRxBytes(this);
        lastTotalTxBytes = FloatServiceMethod.getTotalTxBytes(this);
        lastTimeStamp = System.currentTimeMillis();
        battery_filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        msensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_tem = msensor.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        sensor_light = msensor.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor_gravity = msensor.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensor_pressure = msensor.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensor_proximity = msensor.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensor_step = msensor.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		setDefaultKey();
		SetListKeys(this);
    }

    private void sendBroadcast()
    {
		bundle = new Bundle();
        bundle.putStringArray("LIST", LIST);
		bundle.putIntArray("INFO", INFO);
		bundle.putStringArray("DATA", SetPostKey());
        timeIntent.putExtras(bundle);
        sendBroadcast(timeIntent);
		System.gc();
	}

	private String getClip()
	{
		if (clip != null)
		{
			if (clip.hasPrimaryClip())
			{
				ClipData cd = clip.getPrimaryClip();
				CharSequence cq = cd.getItemAt(0).getText();
				if (cq != null)
				{
					return cq.toString();
				}
			}
		}
		String str = getString(R.string.loading);
		return  str;
	}

    private void getTime()
    {
		time0 = sdf12.format(new Date());
		time1 = sdf24.format(new Date());
		time2 = sdf_clock_12.format(new Date());
		time3 = sdf_clock_24.format(new Date());
		time4 = sdf_date.format(new Date());
		week = sdf_week.format(new Date());
	}

    private String getNetSpeed()
    {
        float nowTotalRxBytes = FloatServiceMethod.getTotalRxBytes(this);
        float nowTotalTxBytes = FloatServiceMethod.getTotalTxBytes(this);
        long nowTimeStamp = System.currentTimeMillis();
        float upspeed = ((nowTotalTxBytes - lastTotalTxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));
        float downspeed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        lastTotalTxBytes = nowTotalTxBytes;
        if (FloatServiceMethod.isVpnUsed())
        {
            downspeed = downspeed / 2;
            upspeed = upspeed / 2;
        }
        return FloatServiceMethod.netspeedset(downspeed) + "↓" + FloatServiceMethod.netspeedset(upspeed) + "↑";
    }

    private void timerset()
    {
        timer_a.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
					boolean FloatWinMode = hasFloatWin(FloatTextUpdateService.this);
					boolean FloatScreenMode = FloatServiceMethod.isScreenOn(FloatTextUpdateService.this);
                    if (FloatWinMode && FloatScreenMode)
                    {
						timeropen();
                    }
                    else if (!FloatWinMode && timer_run || !FloatScreenMode && timer_run)
                    {
                        timerclose();
                    }
                    if (!sensor_use_dynamic_word && register_sensor)
                    {
                        msensor.unregisterListener(sreceiver);
                        register_sensor = false;
                    }
                }
            }, 100, 3500);
    }

	private void timeropen()
	{
		sensorregister();
		if (!timer_run)
		{
			timer_run = true;
			registerReceiver(breceiver, battery_filter);
			timercommon();
			timerslow();
			timerfew();
		}
	}

	private void timercommon()
	{
		timer = new Timer();
		timer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					if (time_dynamicword)
					{
						getTime();
					}
					sendBroadcast();
				}
			}, 150, 1000);
	}

	private void timerslow()
	{
		timer_s = new Timer();
		timer_s.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					localip = FloatServiceMethod.getIP(FloatTextUpdateService.this);
				}
			}, 300, 5000);
	}

	private void timerfew()
	{
		timer_f = new Timer();
		timer_f.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					if (high_cpu_use_dynamicword)
					{
						cpurate = FloatServiceMethod.getProcessCpuRate() + "%";
						meminfo = FloatServiceMethod.getMeminfo(FloatTextUpdateService.this);
					}
				}
			}, 200, 2000);
	}

	private void sensorregister()
	{
		if (sensor_use_dynamic_word && !register_sensor)
		{
			register_sensor = true;
			msensor.registerListener(sreceiver, sensor_tem, SensorManager.SENSOR_DELAY_UI);
			msensor.registerListener(sreceiver, sensor_light, SensorManager.SENSOR_DELAY_UI);
			msensor.registerListener(sreceiver, sensor_gravity, SensorManager.SENSOR_DELAY_UI);
			msensor.registerListener(sreceiver, sensor_step, SensorManager.SENSOR_DELAY_UI);
			msensor.registerListener(sreceiver, sensor_pressure, SensorManager.SENSOR_DELAY_UI);
			msensor.registerListener(sreceiver, sensor_proximity, SensorManager.SENSOR_DELAY_UI);
		}
	}

	private void timerclose()
	{
		timer_run = false;
		if (timer != null && timer_f != null && timer_s != null)
		{
			timer.cancel();
			timer_f.cancel();
			timer_s.cancel();
			timer = null;
			timer_f = null;
			timer_s = null;
			unregisterReceiver(breceiver);
			msensor.unregisterListener(sreceiver);
			register_sensor = false;
		}
	}

	private static void SetListKeys(Context ctx)
	{
		SharedPreferences sp = FloatServiceMethod.setUpdateList(ctx);
		LIST = FloatServiceMethod.StringtoStringArray(sp.getString("LIST", "[]"));
		INFO = FloatServiceMethod.StringtoIntegerArray(sp.getString("INFO", "[]"));
	}

    @Override
    public void onDestroy()
    {
        timer_a.cancel();
        if (timer_run)
        {
            timer.cancel();
            timer_f.cancel();
            timer_s.cancel();
			unregisterReceiver(atr);
            unregisterReceiver(breceiver);
            if (sensor_use_dynamic_word && register_sensor)
            {
                msensor.unregisterListener(sreceiver);
				register_sensor = false;
            }
        }
        super.onDestroy();
    }

    private boolean hasFloatWin(Context ctx)
    {
        boolean highcpudynamicset = false;
        boolean sensordynamicset = false;
		boolean timedynamicset = false;
        Pattern pat = Pattern.compile("<(.*?)>");
        Pattern pat2 = Pattern.compile("#(.*?)#");
        ArrayList<String> list = ((App)ctx.getApplicationContext()).getFloatText();
        boolean dynamicnum = false;
        if (list.size() > 0)
        {
            for (int i = 0;i < list.size();i++)
            {
                String str = list.get(i).toString();
                Matcher mat = pat.matcher(str);
                Matcher mat2 = pat2.matcher(str);
                if (mat.find() || mat2.find())
                {
					dynamicnum = true;
					if (!timedynamicset)
                    {
                        if (FloatServiceMethod.hasWord(str, "SystemTime") || FloatServiceMethod.hasWord(str, "Date") || FloatServiceMethod.hasWord(str, "Clock") || FloatServiceMethod.hasWord(str, "Week"))
                        {
                            time_dynamicword = true;
                            timedynamicset = true;
							continue;
                        }
                        else
                        {
                            time_dynamicword = false;
                        }
                    }
                    if (!sensordynamicset)
                    {
                        if (FloatServiceMethod.hasWord(str, "Sensor_"))
                        {
                            sensor_use_dynamic_word = true;
                            sensordynamicset = true;
							continue;
                        }
                        else
                        {
                            sensor_use_dynamic_word = false;
                        }
                    }
					if (!highcpudynamicset)
                    {
                        if (FloatServiceMethod.hasWord(str, "CPURate") || FloatServiceMethod.hasWord(str, "MemRate"))
                        {
                            high_cpu_use_dynamicword = true;
                            highcpudynamicset = true;
							continue;
                        }
                        else
                        {
                            high_cpu_use_dynamicword = false;
                        }
                    }
                }
            }
        }
        return dynamicnum;
    }

	private class AdvanceTextReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context p1, Intent p2)
		{
			currentactivity = FloatServiceMethod.fixnull(p2.getStringExtra("CurrentActivity"), currentactivity);
			toasts = FloatServiceMethod.fixnull(p2.getStringExtra("Toasts"), toasts);
			notifymes = FloatServiceMethod.fixnull(p2.getStringExtra("NotifyMes"), notifymes);
		}
	}

    private class BatteryReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int current = intent.getExtras().getInt("level");
            int total = intent.getExtras().getInt("scale");
            battery_percent = (current * 100 / total) + "%";
        }
    }

    private class SensorReceiver implements SensorEventListener
    {
        @Override
        public void onSensorChanged(SensorEvent p1)
        {
            if (p1.sensor.getType() == Sensor.TYPE_TEMPERATURE)
            {
                cputemperature = (float)Math.round(p1.values[0] * 100) / 100 + "℃";
            }
            else if (p1.sensor.getType() == Sensor.TYPE_LIGHT)
            {
                sensorlight = (float)Math.round(p1.values[0] * 100) / 100 + "lux";
            }
            else if (p1.sensor.getType() == Sensor.TYPE_GRAVITY)
            {
                sensorgravity = "X:" + (float)Math.round(p1.values[0] * 100) / 100 + "m/s² Y:" + (float)Math.round(p1.values[1] * 100) / 100 + "m/s² Z:" + (float)Math.round(p1.values[2] * 100) / 100 + "m/s²";
            }
            else if (p1.sensor.getType() == Sensor.TYPE_PRESSURE)
            {
                sensorpressure = (float)Math.round(p1.values[0] * 10000) / 100 + "Pa";
            }
            else if (p1.sensor.getType() == Sensor.TYPE_PROXIMITY)
            {
                sensorproximity = (float)Math.round(p1.values[0] * 100) / 100 + "cm";
            }
            else if (p1.sensor.getType() == Sensor.TYPE_STEP_DETECTOR)
            {
                sensorstep ++;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor p1, int p2)
        {}

    }
}
