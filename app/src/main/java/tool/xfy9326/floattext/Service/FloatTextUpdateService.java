package tool.xfy9326.floattext.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tool.xfy9326.floattext.Method.FloatServiceMethod;
import tool.xfy9326.floattext.Utils.App;

public class FloatTextUpdateService extends Service
{
    private Timer timer = null;
    private Timer timer_f = null;
    private Timer timer_s = null;
    private Timer timer_a = null;
    private SimpleDateFormat sdf12 = null;
    private SimpleDateFormat sdf24 = null;
    private SimpleDateFormat sdf_clock_12 = null;
    private SimpleDateFormat sdf_clock_24 = null;
    private SimpleDateFormat sdf_date = null;
    private Intent timeIntent = null;
    private boolean timer_run = false;
    private boolean high_cpu_use_dynamicword = false;
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
    private String sensorproximity = "";
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

    private void init ()
    {
        timer_a = new Timer();
        sdf12 = new SimpleDateFormat("yyyy-MM-dd " + "hh:mm:ss");
        sdf24 = new SimpleDateFormat("yyyy-MM-dd " + "HH:mm:ss");
        sdf_clock_12 = new SimpleDateFormat("hh:mm:ss");
        sdf_clock_24 = new SimpleDateFormat("HH:mm:ss");
        sdf_date = new SimpleDateFormat("yyyy-MM-dd");
        timeIntent = new Intent();
		bundle = new Bundle();
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
    }

    private void sendBroadcast ()
    {
        bundle.putString("SystemTime", getTime(0));
        bundle.putString("SystemTime_24", getTime(1));
        bundle.putString("Clock", getTime(2));
        bundle.putString("Clock_24", getTime(3));
        bundle.putString("Date", getTime(4));
        bundle.putString("CPURate", cpurate);
        bundle.putString("NetSpeed", getNetSpeed());
        bundle.putString("MemInfo", meminfo);
        bundle.putString("LocalIP", localip);
        bundle.putString("Battery", battery_percent);
        bundle.putString("Sensor_CPUTemperature", cputemperature);
        bundle.putString("Sensor_Light", sensorlight);
        bundle.putString("Sensor_Gravity", sensorgravity);
        bundle.putString("Sensor_Pressure", sensorpressure);
        bundle.putString("Sensor_Proximity", sensorproximity);
        bundle.putString("Sensor_Step", sensorstep + "");
        bundle.putString("ClipBoard", clip.getText() + "");
        timeIntent.putExtras(bundle);
        timeIntent.setAction(FloatServiceMethod.TEXT_UPDATE_ACTION);
        sendBroadcast(timeIntent);
	}

    private String getTime (int i)
    {
        String str = "";
        switch (i)
        {
            case 0:
                str = sdf12.format(new Date());
                break;
            case 1:
                str = sdf24.format(new Date());
                break;
            case 2:
                str = sdf_clock_12.format(new Date());
                break;
            case 3:
                str = sdf_clock_24.format(new Date());
                break;
            case 4:
                str = sdf_date.format(new Date());
                break;
            default:
                str = sdf12.format(new Date());
        }
        return str;
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
                    if (hasFloatWin(FloatTextUpdateService.this) && !timer_run && FloatServiceMethod.isScreenOn(FloatTextUpdateService.this))
                    {
                        timer_run = true;
                        timer = new Timer();
                        timer_f = new Timer();
                        timer_s = new Timer();
                        registerReceiver(breceiver, battery_filter);
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
                        timer.schedule(new TimerTask()
                            {
                                @Override
                                public void run ()
                                {
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
                            }, 200, 2500);
                    }
                    else if (!hasFloatWin(FloatTextUpdateService.this) && timer_run || !FloatServiceMethod.isScreenOn(FloatTextUpdateService.this) && timer_run)
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
            }, 100, 3000);
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
                register_sensor = false;
                msensor.unregisterListener(sreceiver);
            }
        }
        super.onDestroy();
    }

    private boolean hasFloatWin (Context ctx)
    {
        boolean highcpudynamicset = false;
        boolean sensordynamicset = false;
        Pattern pat = Pattern.compile("<(.*?)>");
        Pattern pat2 = Pattern.compile("#(.*?)#");
        ArrayList<String> list = ((App)ctx.getApplicationContext()).getFloatText();
        boolean dynamicnum = false;
        if (list.size() > 0)
        {
            for (int i = 0;i < list.size();i++)
            {
                String str = list.get(i);
                Matcher mat = pat.matcher(str);
                Matcher mat2 = pat2.matcher(str);
                if (mat.find() || mat2.find())
                {
                    if (!highcpudynamicset)
                    {
                        if (hasWord(str, "CPURate") || hasWord(str, "MemRate"))
                        {
                            high_cpu_use_dynamicword = true;
                            highcpudynamicset = true;
                        }
                        else
                        {
                            high_cpu_use_dynamicword = false;
                        }
                    }
                    if (!sensordynamicset)
                    {
                        if (hasWord(str, "Sensor_"))
                        {
                            sensor_use_dynamic_word = true;
                            sensordynamicset = true;
                        }
                        else
                        {
                            sensor_use_dynamic_word = false;
                        }
                    }
                    dynamicnum = true;
                    break;
                }
            }
        }
        return dynamicnum;
    }

    private boolean hasWord (String all, String part)
    {
        return all.indexOf(part) >= 0;
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
