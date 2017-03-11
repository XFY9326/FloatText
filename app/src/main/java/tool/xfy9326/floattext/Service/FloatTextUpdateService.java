package tool.xfy9326.floattext.Service;

import android.content.*;
import android.hardware.*;
import android.os.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import tool.xfy9326.floattext.Utils.*;

import android.app.Service;
import tool.xfy9326.floattext.Method.FloatServiceMethod;
import tool.xfy9326.floattext.R;

public class FloatTextUpdateService extends Service
{
	private static String[] LIST;
	private static int[] INFO;
	private boolean FloatScreenState = true;
	private int DynamicReloadTime = 1000;
    private Timer timer = null;
    private SimpleDateFormat sdf12 = null;
    private SimpleDateFormat sdf24 = null;
    private SimpleDateFormat sdf_clock_12 = null;
    private SimpleDateFormat sdf_clock_24 = null;
    private SimpleDateFormat sdf_date = null;
	private SimpleDateFormat sdf_week = null;
	private SimpleDateFormat sdf_sec = null;
	private String time0;
	private String time1;
	private String time2;
	private String time3;
	private String time4;
	private String time5;
	private String netspeed;
	private String wifisignal;
	private String orientation;
    private Intent timeIntent = null;
    private boolean timer_run = false;
	private boolean nousual_dynamicword = false;
    private boolean high_cpu_use_dynamicword = false;
	private boolean time_dynamicword = false;
	private Bundle bundle = null;
    private String cpurate;
    private String meminfo;
    private float lastTotalRxBytes = 0;
    private float lastTotalTxBytes = 0;
    private long lastTimeStamp = 0;
    private String localip;
	private String clipstr;
    private BatteryReceiver breceiver = new BatteryReceiver();
	private AdvanceTextReceiver atr = new AdvanceTextReceiver();
	private int HighCpu_DoubleWaitTime = 0;
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
	private String notifypkg;

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
		DynamicModeSet();
    }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (intent != null)
		{
			if (intent.getBooleanExtra("RELOAD", false))
			{
				DynamicModeSet();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private String[] SetPostKey()
	{
		String[] PostList = new String[27];
		PostList[0] = time0;
		PostList[1] = time1;
		PostList[2] = time2;
		PostList[3] = time3;
		PostList[4] = time4;
		PostList[5] = cpurate;
		PostList[6] = netspeed;
		PostList[7] = meminfo;
		PostList[8] = localip;
		PostList[9] = battery_percent;
		PostList[10] = sensorlight;
		PostList[11] = sensorgravity;
		PostList[12] = sensorpressure;
		PostList[13] = cputemperature;
		PostList[14] = sensorproximity;
		PostList[15] = sensorstep + "";
		PostList[16] = clipstr;
		PostList[17] = currentactivity;
		PostList[18] = notifymes;
		PostList[19] = toasts;
		PostList[20] = week;
		PostList[21] = "None";
		PostList[22] = time5;
		PostList[23] = orientation;
		PostList[24] = "None";
		PostList[25] = notifypkg;
		PostList[26] = wifisignal;
		return PostList;
	}

	private void setDefaultKey()
	{
		String str = getString(R.string.dynamic_word_empty);
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
			week = 
			time5 =
			netspeed =
			orientation =
			clipstr =
			wifisignal =
			notifypkg = str;
	}

    private void init()
    {
		IntentFilter adtfilter = new IntentFilter();
		adtfilter.addAction(Intent.ACTION_SCREEN_ON);
		adtfilter.addAction(Intent.ACTION_SCREEN_OFF);
		adtfilter.addAction(StaticString.TEXT_ADVANCE_UPDATE_ACTION);
		registerReceiver(atr, adtfilter);
        sdf12 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf_clock_12 = new SimpleDateFormat("hh:mm:ss");
        sdf_clock_24 = new SimpleDateFormat("HH:mm:ss");
        sdf_date = new SimpleDateFormat("yyyy-MM-dd");
		sdf_week = new SimpleDateFormat("E");
		sdf_sec = new SimpleDateFormat("ss");
        timeIntent = new Intent();
		timeIntent.setAction(StaticString.TEXT_UPDATE_ACTION);
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
		SetReloadTime();
    }

    private void sendBroadcast()
    {
		bundle = new Bundle();
        bundle.putStringArray("LIST", LIST);
		bundle.putIntArray("INFO", INFO);
		bundle.putStringArray("DATA", SetPostKey());
        timeIntent.putExtras(bundle);
        sendBroadcast(timeIntent);
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
		Date d = new Date();
		time0 = sdf12.format(d);
		time1 = sdf24.format(d);
		time2 = sdf_clock_12.format(d);
		time3 = sdf_clock_24.format(d);
		time4 = sdf_date.format(d);
		week = sdf_week.format(d);
		time5 = sdf_sec.format(d);
	}

    private void getNetSpeed()
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
        netspeed = FloatServiceMethod.netspeedset(downspeed) + "↓ " + FloatServiceMethod.netspeedset(upspeed) + "↑";
    }

	private void DynamicModeSet()
	{
		FloatWinModeGet(this);
		if (FloatScreenState)
		{
			timeropen();
		}
		else if (!FloatScreenState && timer_run)
		{
			timerclose();
		}
		if (!sensor_use_dynamic_word && register_sensor)
		{
			msensor.unregisterListener(sreceiver);
			register_sensor = false;
		}
	}

	private void timeropen()
	{
		sensorregister();
		if (!timer_run)
		{
			timer_run = true;
			registerReceiver(breceiver, battery_filter);
			timercommon();
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
					if (nousual_dynamicword)
					{
						getNetSpeed();
						clipstr = getClip();
						localip = FloatServiceMethod.getIP(FloatTextUpdateService.this);
						wifisignal = FloatServiceMethod.getWifiSignal(FloatTextUpdateService.this);
					}
					if (high_cpu_use_dynamicword)
					{
						if (HighCpu_DoubleWaitTime >= 2)
						{
							cpurate = FloatServiceMethod.getProcessCpuRate() + "%";
							meminfo = FloatServiceMethod.getMeminfo(FloatTextUpdateService.this);
							HighCpu_DoubleWaitTime = 0;
						}
						HighCpu_DoubleWaitTime ++;
					}
					orientation = FloatServiceMethod.judgeOrientation(FloatTextUpdateService.this);
					sendBroadcast();
				}
			}, 150, DynamicReloadTime);
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
		if (timer != null)
		{
			timer.cancel();
			timer = null;
			unregisterReceiver(breceiver);
			msensor.unregisterListener(sreceiver);
			register_sensor = false;
		}
	}
	
	private void SetReloadTime()
	{
		SharedPreferences setdata = getSharedPreferences("ApplicationSettings", MODE_PRIVATE);
		DynamicReloadTime = setdata.getInt("DynamicReloadTime", 1000);
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
        if (timer_run)
        {
            timer.cancel();
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

	//动态变量使用检测
    private void FloatWinModeGet(Context ctx)
    {
        ArrayList<String> list = ((App)ctx.getApplicationContext()).getFloatText();
		String str = list.toString();
		str = str.substring(1, str.length() - 1).trim();
		if (FloatServiceMethod.hasWord(str, new String[]{"NetSpeed", "ClipBoard", "WifiSignal", "LocalIP"}))
		{
			nousual_dynamicword = true;
		}
		else
		{
			nousual_dynamicword = false;
		}
		if (FloatServiceMethod.hasWord(str, new String[]{"SystemTime", "Date", "Clock", "Week", "Second"}))
		{
			time_dynamicword = true;
		}
		else
		{
			time_dynamicword = false;
		}
		if (FloatServiceMethod.hasWord(str, new String[]{"Sensor"}))
		{
			sensor_use_dynamic_word = true;
		}
		else
		{
			sensor_use_dynamic_word = false;
		}
		if (FloatServiceMethod.hasWord(str, new String[]{"CPURate", "MemRate"}))
		{
			high_cpu_use_dynamicword = true;
		}
		else
		{
			high_cpu_use_dynamicword = false;
		}
    }

	private class AdvanceTextReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context p1, Intent p2)
		{
			String action = p2.getAction();
			if (action == Intent.ACTION_SCREEN_ON)
			{
				FloatScreenState = true;
				DynamicModeSet();
			}
			else if (action == Intent.ACTION_SCREEN_OFF)
			{
				FloatScreenState = false;
				DynamicModeSet();
			}
			else if (action == StaticString.TEXT_ADVANCE_UPDATE_ACTION)
			{
				currentactivity = FloatServiceMethod.fixnull(p2.getStringExtra("CurrentActivity"), currentactivity);
				toasts = FloatServiceMethod.fixnull(p2.getStringExtra("Toasts"), toasts);
				notifymes = FloatServiceMethod.fixnull(p2.getStringExtra("NotifyMes"), notifymes);
				notifypkg = FloatServiceMethod.fixnull(p2.getStringExtra("NotifyPkg"), notifypkg);
			}
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
		private DecimalFormat df = new DecimalFormat("#0.00");

        @Override
        public void onSensorChanged(SensorEvent p1)
        {
            if (p1.sensor.getType() == Sensor.TYPE_TEMPERATURE)
            {
                cputemperature = df.format((float)Math.round(p1.values[0] * 100) / 100) + "℃";
            }
            else if (p1.sensor.getType() == Sensor.TYPE_LIGHT)
            {
                sensorlight = df.format((float)Math.round(p1.values[0] * 100) / 100) + "lux";
            }
            else if (p1.sensor.getType() == Sensor.TYPE_GRAVITY)
            {
                sensorgravity = "X:" + df.format((float)Math.round(p1.values[0] * 100) / 100) + "m/s² Y:" + df.format((float)Math.round(p1.values[1] * 100) / 100) + "m/s² Z:" + df.format((float)Math.round(p1.values[2] * 100) / 100) + "m/s²";
            }
            else if (p1.sensor.getType() == Sensor.TYPE_PRESSURE)
            {
                sensorpressure = df.format((float)Math.round(p1.values[0] * 10000) / 100) + "Pa";
            }
            else if (p1.sensor.getType() == Sensor.TYPE_PROXIMITY)
            {
                sensorproximity = df.format((float)Math.round(p1.values[0] * 100) / 100) + "cm";
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
