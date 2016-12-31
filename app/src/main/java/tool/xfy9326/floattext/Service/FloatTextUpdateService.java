package tool.xfy9326.floattext.Service;

import android.app.*;
import android.content.*;
import android.hardware.*;
import android.os.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;

public class FloatTextUpdateService extends Service
{
	private String[] LIST;
	private boolean[] INFO;
    private Timer timer = null;
    private Timer timer_f = null;
    private Timer timer_s = null;
    private Timer timer_a = null;
    private SimpleDateFormat sdf12 = null;
    private SimpleDateFormat sdf24 = null;
    private SimpleDateFormat sdf_clock_12 = null;
    private SimpleDateFormat sdf_clock_24 = null;
    private SimpleDateFormat sdf_date = null;
	private String time0 = "0";
	private String time1 = "0";
	private String time2 = "0";
	private String time3 = "0";
	private String time4 = "0";
    private Intent timeIntent = null;
    private boolean timer_run = false;
    private boolean high_cpu_use_dynamicword = false;
	private boolean time_dynamicword = false;
	private Bundle bundle = null;
    private String cpurate = "0%";
    private String meminfo = "0%";
    private float lastTotalRxBytes = 0;
    private float lastTotalTxBytes = 0;
    private long lastTimeStamp = 0;
    private String localip = "0.0.0.0";
    private BatteryReceiver breceiver = new BatteryReceiver();
    private IntentFilter battery_filter = null;
    private String battery_percent = "0%";
    private boolean sensor_use_dynamic_word = false;
    private SensorReceiver sreceiver = new SensorReceiver();
    private SensorManager msensor = null;
    private Sensor sensor_tem = null;
    private String cputemperature = "0℃";
    private Sensor sensor_light = null;
    private String sensorlight = "0lux";
    private Sensor sensor_gravity = null;
    private String sensorgravity = "X:0m/s² Y:0m/s² Z:0m/s²";
    private Sensor sensor_pressure = null;
    private String sensorpressure = "0Pa";
    private Sensor sensor_proximity = null;
    private String sensorproximity = "0cm";
    private Sensor sensor_step = null;
    private int sensorstep = 0;
    private ClipboardManager clip = null;
    private boolean register_sensor = false;

    @Override
    public IBinder onBind (Intent p1)
    {
        return null;
    }

    @Override
    public void onCreate ()
    {
        super.onCreate();
        init();
        timerset();
    }

	private String[] SetPostKey ()
	{
		String[] PostList = new String[18];
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
		PostList[16] = clip.getText().toString();
		PostList[17] = "None";
		return PostList;
	}

    private void init ()
    {
        timer_a = new Timer();
        sdf12 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf_clock_12 = new SimpleDateFormat("hh:mm:ss");
        sdf_clock_24 = new SimpleDateFormat("HH:mm:ss");
        sdf_date = new SimpleDateFormat("yyyy-MM-dd");
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
        msensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_tem = msensor.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        sensor_light = msensor.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor_gravity = msensor.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensor_pressure = msensor.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensor_proximity = msensor.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensor_step = msensor.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		if (clip.getText() == null)
		{
			clip.setText("");
		}
		SetListKeys();
    }

    private void sendBroadcast ()
    {
		bundle = new Bundle();
        bundle.putStringArray("LIST", LIST);
		bundle.putBooleanArray("INFO", INFO);
		bundle.putStringArray("DATA", SetPostKey());
        timeIntent.putExtras(bundle);
        sendBroadcast(timeIntent);
	}

    private void getTime ()
    {
		time0 = sdf12.format(new Date());
		time1 = sdf24.format(new Date());
		time2 = sdf_clock_12.format(new Date());
		time3 = sdf_clock_24.format(new Date());
		time4 = sdf_date.format(new Date());
	}

    private String getNetSpeed ()
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

    private void timerset ()
    {
        timer_a.schedule(new TimerTask()
            {
                @Override
                public void run ()
                {
					boolean FloatWinMode = hasFloatWin(FloatTextUpdateService.this);
					boolean FloatScreenMode = FloatServiceMethod.isScreenOn(FloatTextUpdateService.this);
                    if (FloatWinMode && FloatScreenMode)
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
						if (!timer_run)
						{
							timer_run = true;
							timer = new Timer();
							timer_f = new Timer();
							timer_s = new Timer();
							registerReceiver(breceiver, battery_filter);
							timer.schedule(new TimerTask()
								{
									@Override
									public void run ()
									{
										if (time_dynamicword)
										{
											getTime();
										}
										sendBroadcast();
									}
								}, 150, 1000);
							timer_s.schedule(new TimerTask()
								{
									@Override
									public void run ()
									{
										localip = FloatServiceMethod.getIP(FloatTextUpdateService.this);
									}
								}, 300, 5000);
							timer_f.schedule(new TimerTask()
								{
									@Override
									public void run ()
									{
										if (high_cpu_use_dynamicword)
										{
											cpurate = FloatServiceMethod.getProcessCpuRate() + "%";
											meminfo = FloatServiceMethod.getMeminfo(FloatTextUpdateService.this);
										}
									}
								}, 200, 2000);
						}
                    }
                    else if (!FloatWinMode && timer_run || !FloatScreenMode && timer_run)
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
                    if (!sensor_use_dynamic_word && register_sensor)
                    {
                        msensor.unregisterListener(sreceiver);
                        register_sensor = false;
                    }
                }
            }, 100, 3500);
    }

	private void SetListKeys ()
	{
		SharedPreferences sp = FloatServiceMethod.setUpdateList(this);
		LIST = FloatServiceMethod.StringtoStringArray(sp.getString("LIST", "[]"));
		INFO = FloatServiceMethod.StringtoBooleanArray(sp.getString("INFO", "[]"));
	}

    @Override
    public void onDestroy ()
    {
        timer_a.cancel();
        if (timer_run)
        {
            timer.cancel();
            timer_f.cancel();
            timer_s.cancel();
            unregisterReceiver(breceiver);
            if (sensor_use_dynamic_word && register_sensor)
            {
                msensor.unregisterListener(sreceiver);
				register_sensor = false;
            }
        }
        super.onDestroy();
    }

    private boolean hasFloatWin (Context ctx)
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
                        if (hasWord(str, "SystemTime") || hasWord(str, "Date") || hasWord(str, "Clock"))
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
                        if (hasWord(str, "Sensor_"))
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
                        if (hasWord(str, "CPURate") || hasWord(str, "MemRate"))
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

    private boolean hasWord (String all, String part)
    {
        return all.contains(part);
    }

    private class BatteryReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive (Context context, Intent intent)
        {
            int current = intent.getExtras().getInt("level");
            int total = intent.getExtras().getInt("scale");
            battery_percent = (current * 100 / total) + "%";
        }
    }

    private class SensorReceiver implements SensorEventListener
    {
        @Override
        public void onSensorChanged (SensorEvent p1)
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
        public void onAccuracyChanged (Sensor p1, int p2)
        {}

    }
}