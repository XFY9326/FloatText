package tool.xfy9326.floattext.Setting;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import tool.xfy9326.floattext.*;
import tool.xfy9326.floattext.Method.*;
import tool.xfy9326.floattext.Utils.*;
import tool.xfy9326.floattext.View.*;

import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import net.margaritov.preference.colorpicker.ColorPickerPreference;
import tool.xfy9326.floattext.Activity.AppCompatPreferenceActivity;

/*
 文字悬浮窗设置界面
 */

public class FloatTextSetting extends AppCompatPreferenceActivity
{
    private static final int REQUEST_CODE = 1;
    private boolean EditMode;
    private int EditID;
    private FloatLinearLayout linearlayout;
    private WindowManager wm = null;
    private WindowManager.LayoutParams wmParams = null;
    private FloatTextView floatview = null;
    private String TextShow = "";
    private Float TextSize;
    private int TextColor;
    private boolean TextThick;
    private SharedPreferences spdata;
    private SharedPreferences.Editor spedit;
    private String Position = "";
    private boolean FloatWinSaved = false;
    private boolean TextTop;
    private boolean AutoTop;
    private boolean TextMove;
    private int TextSpeed;
    private boolean FloatShow;
    private TextView move_x;
    private TextView move_y;
    private boolean TextShadow;
    private float TextShadowX;
    private float TextShadowY;
    private float TextShadowRadius;
    private int BackgroundColor;
    private int TextShadowColor;
	private boolean FloatSize;
	private float FloatLong;
	private float FloatWide;
	private boolean NotifyControl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(FloatTextSetting.this);
        spdata = PreferenceManager.getDefaultSharedPreferences(this);
        spedit = spdata.edit();
        wm = ((App)getApplicationContext()).getFloatwinmanager();
        wmcheck();
        setkeys();
        addPreferencesFromResource(R.xml.floattext_settings);
        sethome();
		FloatTextViewSet(inflater);
		FloatWinViewSet(inflater);
        if (!EditMode)
        {
            prepareshow();
        }
        else
        {
            setTitle(R.string.float_edit_title);
        }
		SafeGuard.isSignatureAvailable(this, true);
        SafeGuard.isPackageNameAvailable(this, true);
    }

	//ToolBar设置
	private void sethome()
	{
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
		{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
	}

	//WindowManager为null错误检测
    private void wmcheck()
    {
        if (wm == null)
        {
            wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            ((App)getApplicationContext()).setFloatwinmanager(wm);
        }
        if (wm == null)
        {
            throw new Error(new NullPointerException("Window Manager No Found"));
        }
    }

	//设置所有可用数据
    private void setkeys()
    {
        Intent intent = getIntent();
		//编辑模式
        EditMode = intent.getBooleanExtra("EditMode", false);
		//编辑代号
        EditID = intent.getIntExtra("EditID", 0);
        if (EditMode)
        {
            editkeyget(EditID);
            editkeyset();
        }
        else
        {
            defaultkeyget();
        }
    }

	//获取编辑悬浮窗的数据，进行覆盖
    private void editkeyget(int i)
    {
        App utils = ((App)getApplicationContext());
		FloatFrameUtils frameutils = utils.getFrameutil();
		if (frameutils.getFloatview().size() < i + 1)
		{
			FloatManageMethod.restartApplication(this, getPackageManager().getLaunchIntentForPackage(getPackageName()));
		}
		FloatTextUtils textutils = utils.getTextutil();
        wmParams = frameutils.getFloatlayout().get(i);
        floatview = frameutils.getFloatview().get(i);
        linearlayout = frameutils.getFloatlinearlayout().get(i);
        TextShow = textutils.getTextShow().get(i);
        AutoTop = textutils.getAutoTop().get(i);
        TextMove = textutils.getTextMove().get(i);
        FloatShow = textutils.getShowFloat().get(i);
        TextTop = textutils.getTextTop().get(i);
        TextThick = textutils.getThickShow().get(i);
        TextSize = textutils.getSizeShow().get(i);
        TextSpeed = textutils.getTextSpeed().get(i);
        TextColor = textutils.getColorShow().get(i);
        TextShadow = textutils.getTextShadow().get(i);
        TextShadowX = textutils.getTextShadowX().get(i);
        TextShadowY = textutils.getTextShadowY().get(i);
        TextShadowRadius = textutils.getTextShadowRadius().get(i);
        BackgroundColor = textutils.getBackgroundColor().get(i);
        TextShadowColor = textutils.getTextShadowColor().get(i);
		FloatSize = textutils.getFloatSize().get(i);
		FloatLong = textutils.getFloatLong().get(i);
		FloatWide = textutils.getFloatWide().get(i);
		NotifyControl = textutils.getNotifyControl().get(i);
    }

	//设置编辑悬浮窗的数据为默认数据
    private void editkeyset()
    {
        spedit.putString("TextShow", TextShow);
        spedit.putBoolean("TextAutoTop", AutoTop);
        spedit.putBoolean("TextMove", TextMove);
        spedit.putBoolean("FloatShow", FloatShow);
        spedit.putBoolean("TextTop", TextTop);
        spedit.putBoolean("TextThick", TextThick);
        spedit.putFloat("TextSize", TextSize);
        spedit.putInt("TextSpeed", TextSpeed);
        spedit.putInt("ColorPicker", TextColor);
        spedit.putBoolean("TextShadow", TextShadow);
        spedit.putFloat("TextShadowX", TextShadowX);
        spedit.putFloat("TextShadowY", TextShadowY);
        spedit.putFloat("TextShadowRadius", TextShadowRadius);
        spedit.putInt("BackgroundColor", BackgroundColor);
        spedit.putInt("TextShadowColor", TextShadowColor);
		spedit.putBoolean("FloatSize", FloatSize);
		spedit.putFloat("FloatTextLong", FloatLong);
		spedit.putFloat("FloatTextWide", FloatWide);
		spedit.putBoolean("NotifyControl", NotifyControl);
        spedit.commit();
    }

	//默认数据获取
    private void defaultkeyget()
    {
        TextShow = spdata.getString("TextShow", getString(R.string.default_text));
        AutoTop = spdata.getBoolean("TextAutoTop", false);
        TextMove = spdata.getBoolean("TextMove", false);
        FloatShow = true;
        spedit.putBoolean("FloatShow", FloatShow);
        spedit.commit();
        TextTop = spdata.getBoolean("TextTop", false);
        TextThick = spdata.getBoolean("TextThick", false);
        TextSize = spdata.getFloat("TextSize", 20f);
        TextSpeed = spdata.getInt("TextSpeed", 5);
        TextColor = spdata.getInt("ColorPicker", -61441);
        EditID = ((App)getApplicationContext()).getFloatText().size();
        TextShadow = spdata.getBoolean("TextShadow", false);
        TextShadowX = spdata.getFloat("TextShadowX", 10f);
        TextShadowY = spdata.getFloat("TextShadowY", 10f);
        TextShadowRadius = spdata.getFloat("TextShadowRadius", 5f);
        BackgroundColor = spdata.getInt("BackgroundColor", 0x00ffffff);
        TextShadowColor = spdata.getInt("TextShadowColor", 0x99000000);
		FloatSize = spdata.getBoolean("FloatSize", false);
		FloatLong = spdata.getFloat("FloatTextLong", FloatWebSettingMethod.getWinDefaultHeight(wm));
		FloatWide = spdata.getFloat("FloatTextWide", FloatWebSettingMethod.getWinDefaultWidth(wm));
		NotifyControl = spdata.getBoolean("NotifyControl", true);
    }

	//界面所有操作设置，操作后必须更新视图
    private void FloatTextViewSet(final LayoutInflater inflater)
    {
		//小提示
        Preference tips = findPreference("tips");
        String[] tiparr = getResources().getStringArray(R.array.floatsetting_tips);
        Random random = new Random();
        int tipnum = random.nextInt(tiparr.length - 1);
        tips.setSummary(tiparr[tipnum]);
		//悬浮窗文字设置
        Preference textshow = findPreference("TextShow");
        textshow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference p)
                {  
                    FloatTextShowSet(inflater);
                    return true;
                }
            });
		//文字大小设置
        Preference textsize = findPreference("TextSize");
        textsize.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference p1)
                {  
                    TextSizeSet(inflater);
                    return true;
                }
            });
		//文字颜色设置
        ColorPickerPreference textcolor = (ColorPickerPreference) findPreference("ColorPicker");
        textcolor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    TextColor = p2;
                    spedit.putInt("ColorPicker", p2);
                    spedit.commit();
                    updateview();
                    return true;
                }
            });
        textcolor.setHexValueEnabled(true);
		textcolor.setAlphaSliderEnabled(true);
		//粗体设置
        CheckBoxPreference textthick = (CheckBoxPreference) findPreference("TextThick");
        textthick.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    TextThick = (Boolean)p2;
                    updateview();
                    return true;
                }
            });
		//文字阴影设置
        Preference shadow = findPreference("TextShadow");
        shadow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference p1)
                {  
                    TextShadowSet(inflater);
                    return true;
                }
            });
		//文字阴影颜色设置
        ColorPickerPreference textshadowcolor = (ColorPickerPreference) findPreference("TextShadowColor");
        textshadowcolor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    TextShadowColor = p2;
                    spedit.putInt("TextShadowColor", p2);
                    spedit.commit();
                    updateview();
                    return true;
                }
            });
        textshadowcolor.setHexValueEnabled(true);
		textshadowcolor.setAlphaSliderEnabled(true);
		//跑马灯开关
        CheckBoxPreference textmove = (CheckBoxPreference) findPreference("TextMove");
        if (((App)getApplicationContext()).getMovingMethod())
        {
            textmove.setSummaryOn(R.string.text_move_on_sum);
        }
        textmove.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    TextMove = (Boolean)p2;
                    updateview();
                    return true;
                }
            });
		//跑马灯速度设置
        Preference textspeed = findPreference("TextSpeed");
        textspeed.setEnabled(((App)getApplicationContext()).getMovingMethod());
        textspeed.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference p1)
                {
                    TextSpeedSet(inflater);
                    return true;
                }
            });
    }

	private void FloatWinViewSet(final LayoutInflater inflater)
	{
		//通知栏控制
		CheckBoxPreference notifycontrol = (CheckBoxPreference) findPreference("NotifyControl");
        notifycontrol.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p, Object v)
                {
                    NotifyControl = v;
                    return true;
                }
            });
		//背景颜色设置
        ColorPickerPreference baccolor = (ColorPickerPreference) findPreference("BackgroundColor");
        baccolor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    BackgroundColor = p2;
                    spedit.putInt("BackgroundColor", p2);
                    spedit.commit();
                    updateview();
                    return true;
                }
            });
        baccolor.setHexValueEnabled(true);
		baccolor.setAlphaSliderEnabled(true);
		//自动吸顶
        CheckBoxPreference autotop = (CheckBoxPreference) findPreference("TextAutoTop");
        autotop.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p, Object v)
                {
                    AutoTop = (Boolean)v;
                    updateview();
                    return true;
                }
            });
		//悬浮窗可以显示在通知栏
        CheckBoxPreference texttop = (CheckBoxPreference) findPreference("TextTop");
        texttop.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    TextTop = (Boolean)p2;
                    updateview();
                    return true;
                }
            });
		//是否显示悬浮窗后
        Preference floatshow = findPreference("FloatShow");
        floatshow.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference p1, Object p2)
                {
                    FloatShow = (Boolean)p2;
                    updateview();
                    return true;
                }
            });
		//悬浮窗微调
        Preference floatmove = findPreference("FloatMove");
        floatmove.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference p)
                {  
                    FloatMoveSet(inflater);
                    return true;
                }
            });
		//悬浮窗大小自定义开关
		CheckBoxPreference floatsize = (CheckBoxPreference) findPreference("FloatSize");
		floatsize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
				public boolean onPreferenceChange(Preference p, Object o)
				{
					FloatSize = o;
					updateview();
					return true;
				}
			});
		//悬浮窗X轴设置
		Preference floatwide = findPreference("FloatWide");
        floatwide.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference p)
                {  
                    FloatWideSet(inflater);
					return true;
				}
			});
		//悬浮窗Y轴设置
		Preference floatlong = findPreference("FloatLong");
        floatlong.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick(Preference p)
                {  
                    FloatLongSet(inflater);
					return true;
				}
			});
	}

	private void TextSizeSet(LayoutInflater inflater)
	{
		View layout = inflater.inflate(R.layout.dialog_textsize_edit, null);
		AlertDialog.Builder dialog = new AlertDialog.Builder(FloatTextSetting.this);
		dialog.setTitle(R.string.text_size_set);
		final TextView text = (TextView) layout.findViewById(R.id.textview_textsize_now);
		final SeekBar bar = (SeekBar) layout.findViewById(R.id.seekbar_textsize);
		text.setText(getString(R.string.text_size_now) + "：" + TextSize.intValue());
		final DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		Button minus = (Button) layout.findViewById(R.id.textsize_button_minus);
		Button plus = (Button) layout.findViewById(R.id.textsize_button_plus);
		minus.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					if (TextSize > 0)
					{
						TextSize--;
						spedit.putFloat("TextSize", TextSize);
						spedit.commit();
						bar.setProgress(TextSize.intValue());
						text.setText(getString(R.string.text_size_now) + "：" + TextSize.intValue());
						updateview();
					}
				}
			});
		plus.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					if (TextSize < (int)(dm.widthPixels / dm.scaledDensity + 0.5f))
					{
						TextSize++;
						spedit.putFloat("TextSize", TextSize);
						spedit.commit();
						bar.setProgress(TextSize.intValue());
						text.setText(getString(R.string.text_size_now) + "：" + TextSize.intValue());
						updateview();
					}
				}
			});
		bar.setMax((int)(dm.widthPixels / dm.scaledDensity + 0.5f));
		bar.setProgress(TextSize.intValue());
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				public void onStartTrackingTouch(SeekBar bar)
				{}
				public void onStopTrackingTouch(SeekBar bar)
				{}
				public void onProgressChanged(SeekBar bar , int i , boolean state)
				{
					TextSize = Float.parseFloat(String.valueOf(i));
					text.setText(getString(R.string.text_size_now) + "：" + TextSize.intValue());
					spedit.putFloat("TextSize", TextSize);
					spedit.commit();
					updateview();
				}
			});
		dialog.setView(layout);
		dialog.setPositiveButton(R.string.close, null);
		dialog.show();
	}

	private void TextSpeedSet(LayoutInflater inflater)
	{
		View layout = inflater.inflate(R.layout.dialog_textspeed_edit, null);
		AlertDialog.Builder dialog = new AlertDialog.Builder(FloatTextSetting.this);
		dialog.setTitle(R.string.text_speed_set);
		final TextView text = (TextView) layout.findViewById(R.id.textview_textspeed_now);
		SeekBar bar = (SeekBar) layout.findViewById(R.id.seekbar_textspeed);
		text.setText(getString(R.string.text_speed_now) + "：" + TextSpeed);
		bar.setMax(10);
		bar.setProgress(TextSpeed);
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				public void onStartTrackingTouch(SeekBar bar)
				{}
				public void onStopTrackingTouch(SeekBar bar)
				{
					spedit.putInt("TextSpeed", TextSpeed);
					spedit.commit();
				}
				public void onProgressChanged(SeekBar bar , int i , boolean state)
				{
					TextSpeed = i;
					text.setText(getString(R.string.text_speed_now) + "：" + TextSpeed);
					updateview();
				}
			});
		dialog.setView(layout);
		dialog.setPositiveButton(R.string.close, null);
		dialog.show();
	}

	private void FloatLongSet(LayoutInflater inflater)
	{
		final DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		View layout = inflater.inflate(R.layout.dialog_floatsize_edit, null);
		AlertDialog.Builder dialog = new AlertDialog.Builder(FloatTextSetting.this);
		dialog.setTitle(R.string.xml_set_win_long);
		final TextView text = (TextView) layout.findViewById(R.id.textview_size_now);
		final SeekBar bar = (SeekBar) layout.findViewById(R.id.seekbar_size);
		text.setText(getString(R.string.xml_set_win_long) + "：" + FloatLong);
		Button minus = (Button) layout.findViewById(R.id.floatsize_button_minus);
		Button plus = (Button) layout.findViewById(R.id.floatsize_button_plus);
		minus.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					if (FloatLong > 0)
					{
						FloatLong--;
						spedit.putFloat("FloatTextLong", FloatLong);
						spedit.commit();
						bar.setProgress((int)FloatLong);
						text.setText(getString(R.string.xml_set_win_long) + "：" + FloatLong);
						updateview();
					}
				}
			});
		plus.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					if (FloatLong < dm.heightPixels)
					{
						FloatLong++;
						spedit.putFloat("FloatTextLong", FloatLong);
						spedit.commit();
						bar.setProgress((int)FloatLong);
						text.setText(getString(R.string.xml_set_win_long) + "：" + FloatLong);
						updateview();
					}
				}
			});
		bar.setMax((int)dm.heightPixels);
		bar.setProgress((int)FloatLong);
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				public void onStartTrackingTouch(SeekBar bar)
				{}
				public void onStopTrackingTouch(SeekBar bar)
				{
					spedit.putFloat("FloatTextLong", FloatLong);
					spedit.commit();
				}
				public void onProgressChanged(SeekBar bar , int i , boolean state)
				{
					FloatLong = i;
					text.setText(getString(R.string.xml_set_win_long) + "：" + FloatLong);
					updateview();
				}
			});
		dialog.setView(layout);
		dialog.setPositiveButton(R.string.close, null);
		dialog.show();
	}

	private void FloatWideSet(LayoutInflater inflater)
	{
		final DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		View layout = inflater.inflate(R.layout.dialog_floatsize_edit, null);
		AlertDialog.Builder dialog = new AlertDialog.Builder(FloatTextSetting.this);
		dialog.setTitle(R.string.xml_set_win_wide);
		final TextView text = (TextView) layout.findViewById(R.id.textview_size_now);
		final SeekBar bar = (SeekBar) layout.findViewById(R.id.seekbar_size);
		text.setText(getString(R.string.xml_set_win_wide) + "：" + FloatWide);
		Button minus = (Button) layout.findViewById(R.id.floatsize_button_minus);
		Button plus = (Button) layout.findViewById(R.id.floatsize_button_plus);
		minus.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					if (FloatWide > 0)
					{
						FloatWide--;
						spedit.putFloat("FloatTextWide", FloatWide);
						spedit.commit();
						bar.setProgress((int)FloatWide);
						text.setText(getString(R.string.xml_set_win_wide) + "：" + FloatWide);
						updateview();
					}
				}
			});
		plus.setOnClickListener(new OnClickListener(){
				public void onClick(View v)
				{
					if (FloatWide < dm.widthPixels)
					{
						FloatWide++;
						spedit.putFloat("FloatTextWide", FloatWide);
						spedit.commit();
						bar.setProgress((int)FloatWide);
						text.setText(getString(R.string.xml_set_win_wide) + "：" + FloatWide);
						updateview();
					}
				}
			});
		bar.setMax((int)dm.widthPixels);
		bar.setProgress((int)FloatWide);
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				public void onStartTrackingTouch(SeekBar bar)
				{}
				public void onStopTrackingTouch(SeekBar bar)
				{
					spedit.putFloat("FloatTextWide", FloatWide);
					spedit.commit();
				}
				public void onProgressChanged(SeekBar bar , int i , boolean state)
				{
					FloatWide = i;
					text.setText(getString(R.string.xml_set_win_wide) + "：" + FloatWide);
					updateview();
				}
			});
		dialog.setView(layout);
		dialog.setPositiveButton(R.string.close, null);
		dialog.show();
	}

	private void FloatTextShowSet(LayoutInflater inflater)
	{
		View layout = inflater.inflate(R.layout.dialog_text_edit, null);
		final EditText atv = (EditText) layout.findViewById(R.id.textview_addnewtext);
		//自动清空输入
		if (spdata.getBoolean("TextAutoClear", false))
		{
			atv.setText("");
		}
		else
		{
			atv.setText(spdata.getString("TextShow", getString(R.string.default_text)));
		}
		//动态变量列表显示
		final ListView lv = (ListView) layout.findViewById(R.id.listview_textedit);
		final LinearLayout ll = (LinearLayout) layout.findViewById(R.id.layout_textedit);
		if (((App)getApplicationContext()).DynamicNumService)
		{
			final String[] dynamiclist = getResources().getStringArray(R.array.floatsetting_dynamic_list);
			String[] dynamicname = getResources().getStringArray(R.array.floatsetting_dynamic_name);
			String[] result = new String[dynamiclist.length];
			for (int i = 0;i < dynamiclist.length;i++)
			{
				result[i] = "<" + dynamiclist[i] + ">" + "\n" + dynamicname[i];
			}
			ArrayAdapter<String> av = new ArrayAdapter<String>(FloatTextSetting.this, android.R.layout.simple_list_item_1, result);
			lv.setAdapter(av);
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
					public void onItemClick(AdapterView<?> adapter, View v, int i, long l)
					{
						atv.getText().insert(atv.getSelectionStart() , "<" + dynamiclist[i] + ">");
					}
				});
		}
		else
		{
			ll.setVisibility(View.GONE);
		}
		AlertDialog.Builder textedit = new AlertDialog.Builder(FloatTextSetting.this);
		textedit.setTitle(R.string.xml_set_textedit_title)
			.setView(layout)
			.setNegativeButton(R.string.cancel, null)
			.setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d , int i)
				{
					String text = atv.getText().toString();
					//空文本检测
					if (text.replaceAll("\\s+", "").equalsIgnoreCase(""))
					{
						Toast.makeText(FloatTextSetting.this, R.string.text_error, Toast.LENGTH_SHORT).show();
					}
					else
					{
						TextShow = text;
						spedit.putString("TextShow", text);
						spedit.commit();
						updateview();
					}
					FloatServiceMethod.ReloadDynamicUse(FloatTextSetting.this);
				}
			});
		textedit.show();
	}

	private void FloatMoveSet(LayoutInflater inflater)
	{
		View layout = inflater.inflate(R.layout.dialog_floatmove, null);
		move_x = (TextView) layout.findViewById(R.id.textview_floatmove_x);
		move_y = (TextView) layout.findViewById(R.id.textview_floatmove_y);
		move_x.setText(String.valueOf(wmParams.x));
		move_y.setText(String.valueOf(wmParams.y));
		final Handler handler = new Handler(){
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
					case 0:
						wmParams.x--;
						break;
					case 1:
						wmParams.x++;
						break;
					case 2:
						wmParams.y--;
						break;
					case 3:
						wmParams.y++;
						break;
				}
				updateview();
			}
		};
		FloatTextSettingMethod method = new FloatTextSettingMethod();
		final Button control_left = (Button) layout.findViewById(R.id.button_floatmove_left);
		control_left.setOnTouchListener(method.ButtonOnLongRepeatClickListener(0, handler));
		final Button control_right = (Button) layout.findViewById(R.id.button_floatmove_right);
		control_right.setOnTouchListener(method.ButtonOnLongRepeatClickListener(1, handler));
		final Button control_up = (Button) layout.findViewById(R.id.button_floatmove_up);
		control_up.setOnTouchListener(method.ButtonOnLongRepeatClickListener(2, handler));
		final Button control_down = (Button) layout.findViewById(R.id.button_floatmove_down);
		control_down.setOnTouchListener(method.ButtonOnLongRepeatClickListener(3, handler));
		AlertDialog.Builder move = new AlertDialog.Builder(FloatTextSetting.this)
			.setTitle(R.string.float_move_title)
			.setView(layout);
		move.show();
	}

	private void TextShadowSet(LayoutInflater inflater)
	{
		View layout = inflater.inflate(R.layout.dialog_textshadow_edit, null);
		final Switch ss = (Switch) layout.findViewById(R.id.switch_textshadow);
		final TextView sx = (TextView) layout.findViewById(R.id.textview_shadowDx);
		final TextView sy = (TextView) layout.findViewById(R.id.textview_shadowDy);
		final TextView sr = (TextView) layout.findViewById(R.id.textview_radius);
		final SeekBar bx = (SeekBar) layout.findViewById(R.id.seekbar_shadowDx);
		final SeekBar by = (SeekBar) layout.findViewById(R.id.seekbar_shadowDy);
		final SeekBar br = (SeekBar) layout.findViewById(R.id.seekbar_radius);
		bx.setMax(30);
		by.setMax(30);
		br.setMax(25);
		ss.setChecked(TextShadow);
		bx.setProgress((int)TextShadowX);
		by.setProgress((int)TextShadowY);
		br.setProgress((int)TextShadowRadius);
		sx.setText(getString(R.string.xml_set_text_shadow_dx) + (int)TextShadowX);
		sy.setText(getString(R.string.xml_set_text_shadow_dy) + (int)TextShadowY);
		sr.setText(getString(R.string.xml_set_text_shadow_radius) + (int)TextShadowRadius);
		ss.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton b, boolean c)
				{
					TextShadow = c;
					spedit.putBoolean("TextShadow", TextShadow);
					spedit.commit();
					updateview();
				}
			});
		bx.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				public void onStartTrackingTouch(SeekBar bar)
				{}
				public void onStopTrackingTouch(SeekBar bar)
				{
					spedit.putFloat("TextShadowX", TextShadowX);
					spedit.commit();
				}
				public void onProgressChanged(SeekBar bar , int i , boolean state)
				{
					TextShadowX = i;
					sx.setText(getString(R.string.xml_set_text_shadow_dx) + (int)TextShadowX);
					updateview();
				}
			});
		by.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				public void onStartTrackingTouch(SeekBar bar)
				{}
				public void onStopTrackingTouch(SeekBar bar)
				{
					spedit.putFloat("TextShadowY", TextShadowY);
					spedit.commit();
				}
				public void onProgressChanged(SeekBar bar , int i , boolean state)
				{
					TextShadowY = i;
					sy.setText(getString(R.string.xml_set_text_shadow_dy) + (int)TextShadowY);
					updateview();
				}
			});
		br.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				public void onStartTrackingTouch(SeekBar bar)
				{}
				public void onStopTrackingTouch(SeekBar bar)
				{
					spedit.putFloat("TextShadowRadius", TextShadowRadius);
					spedit.commit();
				}
				public void onProgressChanged(SeekBar bar , int i , boolean state)
				{
					TextShadowRadius = i;
					sr.setText(getString(R.string.xml_set_text_shadow_radius) + (int)TextShadowRadius);
					updateview();
				}
			});
		AlertDialog.Builder dialog = new AlertDialog.Builder(FloatTextSetting.this);
		dialog.setTitle(R.string.xml_set_text_shadow);
		dialog.setPositiveButton(R.string.close, null);
		dialog.setView(layout);
		dialog.show();
	}

	//权限检测
    private void prepareshow()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (!Settings.canDrawOverlays(this))
            {
                FloatTextSettingMethod.askforpermission(this, REQUEST_CODE);
            }
            else
            {
                startshow();
            }
        }
        else
        {
            startshow();
        }
    }

	//创建新悬浮窗
    private void startshow()
    {
        floatview = FloatTextSettingMethod.CreateFloatView(this, TextShow, TextSize, TextColor, TextThick, TextSpeed, EditID, TextShadow, TextShadowX, TextShadowY, TextShadowRadius, TextShadowColor);
        linearlayout = FloatTextSettingMethod.CreateLayout(this, EditID);
        wmParams = FloatTextSettingMethod.CreateFloatLayout(this, wm, floatview, linearlayout, FloatShow, TextTop, TextMove, BackgroundColor, FloatSize, FloatLong, FloatWide);
    }

	//停止显示
    private void stopshow()
    {
        if (wm != null)
        {
            if (linearlayout != null)
            {
                wm.removeView(linearlayout);
            }
            wm = null;
            wmParams = null;
            floatview = null;
            linearlayout = null;
        }
    }

	//更新视图
    private void updateview()
    {
        App utils = ((App)getApplicationContext());
        if (wm != null)
        {
            if (move_x != null && move_y != null)
            {
                move_x.setText(String.valueOf(wmParams.x));
                move_y.setText(String.valueOf(wmParams.y));
            }
            if (EditMode)
            {
                ArrayList<String> floattext = utils.getFloatText();
                floattext.set(EditID, TextShow.toString());
                utils.setFloatText(floattext);
            }
            floatview.setText(TextShow.toString());
            floatview.setTextSize(TextSize);
            floatview.setTextColor(TextColor);
            floatview.setShadow(TextShadow, TextShadowX, TextShadowY, TextShadowRadius, TextShadowColor);
            linearlayout.setFloatLayoutParams(wmParams);
            if (utils.getMovingMethod())
            {
                floatview.setMoving(TextMove, 0);
            }
            else
            {
                floatview.setMoving(TextMove, 1);
                if (TextMove)
                {
                    linearlayout.setShowState(false);
                    linearlayout.setShowState(true);
                }
            }
            floatview.setMoveSpeed(TextSpeed);
            if (TextThick)
            {
                floatview.getPaint().setFakeBoldText(true);
            }
            else
            {
                floatview.getPaint().setFakeBoldText(false);
            }
            if (TextTop)
            {
                linearlayout.setTop(AutoTop);
                wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            }
            else
            {
                linearlayout.setTop(true);
                wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
            }
            linearlayout.setLayout_default_flags(wmParams.flags);
            linearlayout.setShowState(FloatShow);
            linearlayout.setBackgroundColor(BackgroundColor);
			if (FloatSize)
			{
				wmParams.width = (int)FloatWide;
				wmParams.height = (int)FloatLong;
			}
			else
			{
				wmParams.width = LayoutParams.WRAP_CONTENT;
				wmParams.height = LayoutParams.WRAP_CONTENT;
			}
            if (EditMode)
            {
                ArrayList<Boolean> sf = utils.getTextutil().getShowFloat();
				//Flag更新覆盖后重设
				linearlayout.setTouchable(wmParams, !linearlayout.getPositionLocked());
                sf.set(EditID, FloatShow);
                utils.getTextutil().setShowFloat(sf);
            }
            wm.updateViewLayout(linearlayout, wmParams);
        }
    }

	//保存所有数据
    private void saveall(String text, boolean savedetails)
    {
        App utils = ((App)getApplicationContext());
        FloatTextSettingMethod.savedata(this, floatview, linearlayout, text, wmParams);
        if (!TextTop)
        {
            AutoTop = true;
        }
        if (savedetails)
        {
            FloatTextUtils textutils = utils.getTextutil();
			textutils.addDatas(TextShow, TextColor, TextSize, TextThick, FloatShow, Position, linearlayout.getPositionLocked(), TextTop, AutoTop, TextMove, TextSpeed, TextShadow, TextShadowX, TextShadowY, TextShadowRadius, BackgroundColor, TextShadowColor, FloatSize, FloatLong, FloatWide, NotifyControl);
			utils.setTextutil(textutils);
        }
    }

	//设置数据
    private void setall(int i)
    {
        Position = linearlayout.getPosition();
        App utils = ((App)getApplicationContext());
		FloatTextUtils textutils = utils.getTextutil();
		FloatFrameUtils frameutils = utils.getFrameutil();
        frameutils.setDatas(i, floatview, linearlayout, wmParams, TextShow);
		textutils.setDatas(i, TextShow, TextColor, TextSize, TextThick, FloatShow, Position, linearlayout.getPositionLocked(), TextTop, AutoTop, TextMove, TextSpeed, TextShadow, TextShadowX, TextShadowY, TextShadowRadius, BackgroundColor, TextShadowColor, FloatSize, FloatLong, FloatWide, NotifyControl);
		utils.setTextutil(textutils);
		utils.setFrameutil(frameutils);
    }

	//返回消息
	private void setbackresult(int i)
	{
		Intent intent = new Intent();
		intent.putExtra("RESULT", i);
		intent.putExtra("POSITION", EditID);
		intent.putExtra("EDITMODE", EditMode);
		setResult(StaticNum.FLOATTEXT_RESULT_CODE, intent);
	}

	//返回事件处理
	private void backpressed()
	{
		if (EditMode)
		{
			Toast.makeText(this, R.string.set_save, Toast.LENGTH_SHORT).show();
		}
		else
		{
			AlertDialog.Builder exit = new AlertDialog.Builder(this)
				.setTitle(R.string.exit_text_add)
				.setMessage(R.string.exit_text_add_alert)
				.setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface p1, int p2)
					{
						if (!FloatWinSaved && FloatShow)
						{
							stopshow();
						}
						FloatTextSetting.this.finish();
					}
				})
				.setNegativeButton(R.string.cancel, null);
			exit.show();
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE)
        {
            if (Build.VERSION.SDK_INT >= 23)
            {
                if (Settings.canDrawOverlays(this))
                {
                    prepareshow();
                }
                else
                {
                    setbackresult(3);
                    this.finish();
                }
            }
        }
    }

    @Override 
    public boolean onCreateOptionsMenu(Menu menu)
    {  
        MenuInflater inflater = getMenuInflater();
        if (EditMode)
        {
            inflater.inflate(R.menu.floatsetting_action_bar_editmode, menu);
        }
        else
        {
            inflater.inflate(R.menu.floatsetting_action_bar, menu);
        }
        return super.onCreateOptionsMenu(menu);  
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
			case android.R.id.home:
				backpressed();
				break;
            case R.id.save_win:
				if (!FloatWinSaved)
				{
					Position = linearlayout.getPosition();
					if (EditMode)
					{
						setall(EditID);
					}
					else
					{
						saveall(spdata.getString("TextShow", getString(R.string.default_text)), true);
					}
					FloatWinSaved = true;
					setbackresult(1);
					this.finish();
				}
                break;
            case R.id.delete_win:
                if (!EditMode)
                {
                    stopshow();
                    setbackresult(2);
                }
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
		{
            backpressed();
        }
        return false;
    }

    @Override
    protected void onDestroy()
    {
        if (!FloatWinSaved && FloatShow)
        {
            if (!EditMode)
            {
                stopshow();
            }
        }
        System.gc();
        super.onDestroy();
    }

}
