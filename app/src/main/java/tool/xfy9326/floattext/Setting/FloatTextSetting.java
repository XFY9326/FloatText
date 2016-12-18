package tool.xfy9326.floattext.Setting;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;
import net.margaritov.preference.colorpicker.ColorPickerPreference;
import tool.xfy9326.floattext.Method.FloatTextSettingMethod;
import tool.xfy9326.floattext.R;
import tool.xfy9326.floattext.Utils.App;
import tool.xfy9326.floattext.View.FloatLinearLayout;
import tool.xfy9326.floattext.View.FloatTextView;

public class FloatTextSetting extends PreferenceActivity
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
    private boolean LockPosition;
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
    private String TextTransparency;
    private String WinTransparency;
    private int TextShadowColor;
    private String TextShadowTransparency;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        spdata = PreferenceManager.getDefaultSharedPreferences(this);
        spedit = spdata.edit();
        wm = ((App)getApplicationContext()).getFloatwinmanager();
        wmcheck();
        setkeys();
        addPreferencesFromResource(R.xml.floattext_settings);
        buttonset();
        if (!EditMode)
        {
            prepareshow();
        }
        else
        {
            setTitle(R.string.float_edit_title);
        }
    }

    private void wmcheck ()
    {
        if (wm == null)
        {
            wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            ((App)getApplicationContext()).setFloatwinmanager(wm);
        }
        if (wm == null)
        {
            throw new Error(new NullPointerException());
        }
    }
    
    private void setkeys ()
    {
        Intent intent = getIntent();
        EditMode = intent.getBooleanExtra("EditMode", false);
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

    private void editkeyget (int i)
    {
        App utils = ((App)getApplicationContext());
        wmParams = utils.getFloatLayout().get(i);
        floatview = utils.getFloatView().get(i);
        linearlayout = utils.getFloatlinearlayout().get(i);
        LockPosition = utils.getLockPosition().get(i);
        TextShow = utils.getTextData().get(i);
        AutoTop = utils.getAutoTop().get(i);
        TextMove = utils.getTextMove().get(i);
        FloatShow = utils.getShowFloat().get(i);
        TextTop = utils.getTextTop().get(i);
        TextThick = utils.getThickData().get(i);
        TextSize = utils.getSizeData().get(i);
        TextSpeed = utils.getTextSpeed().get(i);
        TextColor = utils.getColorData().get(i);
        TextShadow = utils.getTextShadow().get(i);
        TextShadowX = utils.getTextShadowX().get(i);
        TextShadowY = utils.getTextShadowY().get(i);
        TextShadowRadius = utils.getTextShadowRadius().get(i);
        BackgroundColor = utils.getBackgroundColor().get(i);
        TextShadowColor = utils.getTextShadowColor().get(i);
    }

    private void editkeyset ()
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
        spedit.commit();
    }

    private void defaultkeyget ()
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
    }

    private void buttonset ()
    {
        Preference tips = findPreference("tips");
        String[] tiparr = getResources().getStringArray(R.array.floatsetting_tips);
        Random random = new Random();
        int tipnum = random.nextInt(tiparr.length - 1);
        tips.setSummary(tiparr[tipnum]);
        Preference textshow = findPreference("TextShow");
        textshow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference p)
                {
                    LayoutInflater inflater = LayoutInflater.from(FloatTextSetting.this);  
                    View layout = inflater.inflate(R.layout.dialog_text_edit, null);
                    final AutoCompleteTextView atv = (AutoCompleteTextView) layout.findViewById(R.id.textview_addnewtext);
                    atv.setText(spdata.getString("TextShow", getString(R.string.default_text)));
                    if (((App)getApplicationContext()).DynamicNumService)
                    {
                        final String[] words = getResources().getStringArray(R.array.floatsetting_dynamic_list);
                        for (int i = 0;i < words.length;i++)
                        {
                            words[i] = "<" + words[i] + ">";
                        }
                        ArrayAdapter<String> av = new ArrayAdapter<String>(FloatTextSetting.this, android.R.layout.simple_dropdown_item_1line, words);
                        atv.setAdapter(av);
                    }
                    AlertDialog.Builder textedit = new AlertDialog.Builder(FloatTextSetting.this)
                        .setTitle(R.string.xml_set_textedit_title)
                        .setView(layout)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                            public void onClick (DialogInterface d , int i)
                            {
                                String text = atv.getText().toString();
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
                            }
                        });
                    textedit.show();
                    return true;
                }
            });
        Preference textsize = findPreference("TextSize");
        textsize.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference p1)
                {
                    LayoutInflater inflater = LayoutInflater.from(FloatTextSetting.this);  
                    View layout = inflater.inflate(R.layout.dialog_textsize_edit, null);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(FloatTextSetting.this);
                    dialog.setTitle(R.string.text_size_set);
                    final TextView text = (TextView) layout.findViewById(R.id.textview_textsize_now);
                    SeekBar bar = (SeekBar) layout.findViewById(R.id.seekbar_textsize);
                    text.setText(getString(R.string.text_size_now) + "：" + TextSize.intValue());
                    bar.setMax(100);
                    bar.setProgress(TextSize.intValue());
                    bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
                            public void onStartTrackingTouch (SeekBar bar)
                            {}
                            public void onStopTrackingTouch (SeekBar bar)
                            {}
                            public void onProgressChanged (SeekBar bar , int i , boolean state)
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
                    return true;
                }
            });
        ColorPickerPreference textcolor = (ColorPickerPreference) findPreference("ColorPicker");
        textcolor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    TextColor = p2;
                    spedit.putInt("ColorPicker", p2);
                    spedit.commit();
                    updateview();
                    return true;
                }
            });
        textcolor.setHexValueEnabled(true);
        Preference texcolor = findPreference("TextColorEnd");
        texcolor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference p)
                {
                    LayoutInflater inflater = LayoutInflater.from(FloatTextSetting.this);  
                    View layout = inflater.inflate(R.layout.dialog_textcolor_transparency_edit, null);
                    final TextView tt = (TextView) layout.findViewById(R.id.textview_texttransparency_now);
                    final SeekBar st = (SeekBar) layout.findViewById(R.id.seekbar_texttransparency);
                    st.setMax(Integer.parseInt("FF", 16));
                    final String cor = FloatTextSettingMethod.IntColortoHex(TextColor);
                    if (cor.length() > 7)
                    {
                        String nowend = cor.substring(1, 3);
                        TextTransparency = nowend;
                        st.setProgress(Integer.parseInt(nowend, 16));
                    }
                    else
                    {
                        st.setProgress(Integer.parseInt("FF", 16));
                        TextTransparency = "FF";
                    }
                    tt.setText(getString(R.string.text_edit_color_transparency_now) + Integer.parseInt(TextTransparency, 16));
                    st.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
                            public void onStartTrackingTouch (SeekBar bar)
                            {}
                            public void onStopTrackingTouch (SeekBar bar)
                            {}
                            public void onProgressChanged (SeekBar bar , int i , boolean state)
                            {
                                TextTransparency = Integer.toHexString(i) + "";
                                if (i < 16)
                                {
                                    TextTransparency = "0" + TextTransparency;
                                }
                                tt.setText(getString(R.string.text_edit_color_transparency_now) + Integer.parseInt(TextTransparency, 16));
                                if (cor.length() > 7)
                                {
                                    String corn = "#" + TextTransparency + cor.substring(3);
                                    TextColor = Color.parseColor(corn);
                                }
                                else
                                {
                                    String corn = "#" + TextTransparency + cor.substring(1);
                                    TextColor = Color.parseColor(corn);
                                }
                                spedit.putInt("ColorPicker", TextColor);
                                spedit.commit();
                                updateview();
                            }
                        });
                    AlertDialog.Builder tex = new AlertDialog.Builder(FloatTextSetting.this);
                    tex.setTitle(R.string.text_edit_color_transparency);
                    tex.setView(layout);
                    tex.setPositiveButton(R.string.close, null);
                    tex.show();
                    return true;
                }
            });
        CheckBoxPreference textthick = (CheckBoxPreference) findPreference("TextThick");
        textthick.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    TextThick = (Boolean)p2;
                    updateview();
                    return true;
                }
            });
        CheckBoxPreference texttop = (CheckBoxPreference) findPreference("TextTop");
        texttop.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    TextTop = (Boolean)p2;
                    updateview();
                    return true;
                }
            });
        Preference floatshow = findPreference("FloatShow");
        floatshow.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    FloatShow = (Boolean)p2;
                    updateview();
                    return true;
                }
            });
        if (!spdata.getBoolean("WinOnlyShowInHome", false) || !EditMode)
        {
            floatshow.setEnabled(true);
        }
        else
        {
            floatshow.setEnabled(false);
        }
        Preference shadow = findPreference("TextShadow");
        shadow.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference p1)
                {
                    LayoutInflater inflater = LayoutInflater.from(FloatTextSetting.this);  
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
                            public void onCheckedChanged (CompoundButton b, boolean c)
                            {
                                TextShadow = c;
                                spedit.putBoolean("TextShadow", TextShadow);
                                spedit.commit();
                                updateview();
                            }
                        });
                    bx.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
                            public void onStartTrackingTouch (SeekBar bar)
                            {}
                            public void onStopTrackingTouch (SeekBar bar)
                            {
                                spedit.putFloat("TextShadowX", TextShadowX);
                                spedit.commit();
                            }
                            public void onProgressChanged (SeekBar bar , int i , boolean state)
                            {
                                TextShadowX = i;
                                sx.setText(getString(R.string.xml_set_text_shadow_dx) + (int)TextShadowX);
                                updateview();
                            }
                        });
                    by.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
                            public void onStartTrackingTouch (SeekBar bar)
                            {}
                            public void onStopTrackingTouch (SeekBar bar)
                            {
                                spedit.putFloat("TextShadowY", TextShadowY);
                                spedit.commit();
                            }
                            public void onProgressChanged (SeekBar bar , int i , boolean state)
                            {
                                TextShadowY = i;
                                sy.setText(getString(R.string.xml_set_text_shadow_dy) + (int)TextShadowY);
                                updateview();
                            }
                        });
                    br.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
                            public void onStartTrackingTouch (SeekBar bar)
                            {}
                            public void onStopTrackingTouch (SeekBar bar)
                            {
                                spedit.putFloat("TextShadowRadius", TextShadowRadius);
                                spedit.commit();
                            }
                            public void onProgressChanged (SeekBar bar , int i , boolean state)
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
                    return true;
                }
            });
        ColorPickerPreference textshadowcolor = (ColorPickerPreference) findPreference("TextShadowColor");
        textshadowcolor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    TextShadowColor = p2;
                    spedit.putInt("TextShadowColor", p2);
                    spedit.commit();
                    updateview();
                    return true;
                }
            });
        textshadowcolor.setHexValueEnabled(true);
        Preference texshadowcolor = findPreference("TextShadowColorEnd");
        texshadowcolor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference p)
                {
                    LayoutInflater inflater = LayoutInflater.from(FloatTextSetting.this);  
                    View layout = inflater.inflate(R.layout.dialog_textcolor_transparency_edit, null);
                    final TextView tt = (TextView) layout.findViewById(R.id.textview_texttransparency_now);
                    final SeekBar st = (SeekBar) layout.findViewById(R.id.seekbar_texttransparency);
                    st.setMax(Integer.parseInt("FF", 16) - 1);
                    final String cor = FloatTextSettingMethod.IntColortoHex(TextShadowColor);
                    if (cor.length() > 7)
                    {
                        String nowend = cor.substring(1, 3);
                        TextShadowTransparency = nowend;
                        st.setProgress(Integer.parseInt(nowend, 16));
                    }
                    else
                    {
                        st.setProgress(Integer.parseInt("FF", 16));
                        TextShadowTransparency = "FF";
                    }
                    tt.setText(getString(R.string.text_edit_color_transparency_now) + Integer.parseInt(TextShadowTransparency, 16));
                    st.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
                            public void onStartTrackingTouch (SeekBar bar)
                            {}
                            public void onStopTrackingTouch (SeekBar bar)
                            {}
                            public void onProgressChanged (SeekBar bar , int i , boolean state)
                            {
                                TextShadowTransparency = Integer.toHexString(i) + "";
                                if (i < 16)
                                {
                                    TextShadowTransparency = "0" + TextShadowTransparency;
                                }
                                tt.setText(getString(R.string.text_edit_color_transparency_now) + Integer.parseInt(TextShadowTransparency, 16));
                                if (cor.length() > 7)
                                {
                                    String corn = "#" + TextShadowTransparency + cor.substring(3);
                                    TextShadowColor = Color.parseColor(corn);
                                }
                                else
                                {
                                    String corn = "#" + TextShadowTransparency + cor.substring(1);
                                    TextShadowColor = Color.parseColor(corn);
                                }
                                spedit.putInt("TextShadowColor", TextShadowColor);
                                spedit.commit();
                                updateview();
                            }
                        });
                    AlertDialog.Builder tex = new AlertDialog.Builder(FloatTextSetting.this);
                    tex.setTitle(R.string.text_edit_color_transparency);
                    tex.setView(layout);
                    tex.setPositiveButton(R.string.close, null);
                    tex.show();
                    return true;
                }
            });
        CheckBoxPreference textmove = (CheckBoxPreference) findPreference("TextMove");
        if (((App)getApplicationContext()).getMovingMethod())
        {
            textmove.setSummaryOn(R.string.text_move_on_sum);
        }
        textmove.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    TextMove = (Boolean)p2;
                    updateview();
                    return true;
                }
            });
        Preference textspeed = findPreference("TextSpeed");
        textspeed.setEnabled(((App)getApplicationContext()).getMovingMethod());
        textspeed.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference p1)
                {
                    LayoutInflater inflater = LayoutInflater.from(FloatTextSetting.this);  
                    View layout = inflater.inflate(R.layout.dialog_textspeed_edit, null);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(FloatTextSetting.this);
                    dialog.setTitle(R.string.text_speed_set);
                    final TextView text = (TextView) layout.findViewById(R.id.textview_textspeed_now);
                    SeekBar bar = (SeekBar) layout.findViewById(R.id.seekbar_textspeed);
                    text.setText(getString(R.string.text_speed_now) + "：" + TextSpeed);
                    bar.setMax(10);
                    bar.setProgress(TextSpeed);
                    bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
                            public void onStartTrackingTouch (SeekBar bar)
                            {}
                            public void onStopTrackingTouch (SeekBar bar)
                            {
                                spedit.putInt("TextSpeed", TextSpeed);
                                spedit.commit();
                            }
                            public void onProgressChanged (SeekBar bar , int i , boolean state)
                            {
                                TextSpeed = i;
                                text.setText(getString(R.string.text_speed_now) + "：" + TextSpeed);
                                updateview();
                            }
                        });
                    dialog.setView(layout);
                    dialog.setPositiveButton(R.string.close, null);
                    dialog.show();
                    return true;
                }
            });
        ColorPickerPreference baccolor = (ColorPickerPreference) findPreference("BackgroundColor");
        baccolor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p1, Object p2)
                {
                    BackgroundColor = p2;
                    spedit.putInt("BackgroundColor", p2);
                    spedit.commit();
                    updateview();
                    return true;
                }
            });
        baccolor.setHexValueEnabled(true);
        Preference baccolorTransparency = findPreference("BackgroundColorEnd");
        baccolorTransparency.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference p)
                {
                    LayoutInflater inflater = LayoutInflater.from(FloatTextSetting.this);  
                    View layout = inflater.inflate(R.layout.dialog_textcolor_transparency_edit, null);
                    final TextView tt = (TextView) layout.findViewById(R.id.textview_texttransparency_now);
                    final SeekBar st = (SeekBar) layout.findViewById(R.id.seekbar_texttransparency);
                    st.setMax(Integer.parseInt("FF", 16));
                    final String cor = FloatTextSettingMethod.IntColortoHex(BackgroundColor);
                    if (cor.length() > 7)
                    {
                        String nowend = cor.substring(1, 3);
                        WinTransparency = nowend;
                        st.setProgress(Integer.parseInt(nowend, 16));
                    }
                    else
                    {
                        st.setProgress(Integer.parseInt("FF", 16));
                        WinTransparency = "FF";
                    }
                    tt.setText(getString(R.string.text_edit_color_transparency_now) + Integer.parseInt(WinTransparency, 16));
                    st.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
                            public void onStartTrackingTouch (SeekBar bar)
                            {}
                            public void onStopTrackingTouch (SeekBar bar)
                            {}
                            public void onProgressChanged (SeekBar bar , int i , boolean state)
                            {
                                WinTransparency = Integer.toHexString(i) + "";
                                if (i < 16)
                                {
                                    WinTransparency = "0" + WinTransparency;
                                }
                                tt.setText(getString(R.string.text_edit_color_transparency_now) + Integer.parseInt(WinTransparency, 16));
                                if (cor.length() > 7)
                                {
                                    String corn = "#" + WinTransparency + cor.substring(3);
                                    BackgroundColor = Color.parseColor(corn);
                                }
                                else
                                {
                                    String corn = "#" + WinTransparency + cor.substring(1);
                                    BackgroundColor = Color.parseColor(corn);
                                }
                                spedit.putInt("BackgroundColor", BackgroundColor);
                                spedit.commit();
                                updateview();
                            }
                        });
                    AlertDialog.Builder tex = new AlertDialog.Builder(FloatTextSetting.this);
                    tex.setTitle(R.string.text_edit_color_transparency);
                    tex.setView(layout);
                    tex.setPositiveButton(R.string.close, null);
                    tex.show();
                    return true;
                }
            });
        CheckBoxPreference autotop = (CheckBoxPreference) findPreference("TextAutoTop");
        autotop.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange (Preference p, Object v)
                {
                    AutoTop = (Boolean)v;
                    updateview();
                    return true;
                }
            });
        Preference dynamiclist = findPreference("DynamicList");
        dynamiclist.setEnabled(((App)getApplicationContext()).DynamicNumService);
        dynamiclist.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference p)
                {
                    final String[] dynamiclist = getResources().getStringArray(R.array.floatsetting_dynamic_list);
                    String[] dynamicname = getResources().getStringArray(R.array.floatsetting_dynamic_name);
                    String[] result = new String[dynamiclist.length + 1];
                    for (int i = 0;i < dynamiclist.length;i++)
                    {
                        result[i] = "<" + dynamiclist[i] + ">" + "\n" + dynamicname[i];
                    }
                    result[dynamiclist.length] = getString(R.string.dynamic_num_tip);
                    AlertDialog.Builder list = new AlertDialog.Builder(FloatTextSetting.this)
                        .setTitle(R.string.dynamic_list_title)
                        .setItems(result, new DialogInterface.OnClickListener(){
                            public void onClick (DialogInterface d, int i)
                            {
                                if (i != dynamiclist.length)
                                {
                                    ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                                    if (((App)getApplicationContext()).HtmlMode)
                                    {
                                        clip.setText("#" + dynamiclist[i] + "#");
                                    }
                                    else
                                    {
                                        clip.setText("<" + dynamiclist[i] + ">");
                                    }
                                    Toast.makeText(FloatTextSetting.this, R.string.copy_ok, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    list.show();
                    return true;
                }
            });
        Preference floatmove = findPreference("FloatMove");
        floatmove.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                public boolean onPreferenceClick (Preference p)
                {
                    LayoutInflater inflater = LayoutInflater.from(FloatTextSetting.this);  
                    View layout = inflater.inflate(R.layout.dialog_floatmove, null);
                    move_x = (TextView) layout.findViewById(R.id.textview_floatmove_x);
                    move_y = (TextView) layout.findViewById(R.id.textview_floatmove_y);
                    move_x.setText(String.valueOf(wmParams.x));
                    move_y.setText(String.valueOf(wmParams.y));
                    final Handler handler = new Handler(){
                        public void handleMessage (Message msg)
                        {
                            switch (msg.what)
                            {
                                case 0:
                                    wmParams.x = wmParams.x - 1;
                                    break;
                                case 1:
                                    wmParams.x = wmParams.x + 1;
                                    break;
                                case 2:
                                    wmParams.y = wmParams.y - 1;
                                    break;
                                case 3:
                                    wmParams.y = wmParams.y + 1;
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
                    return true;
                }
            });
    }

    private void prepareshow ()
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

    private void updateview ()
    {
        updatefloatview(FloatShow, TextShow, TextSize, TextColor, TextThick, TextTop, AutoTop, TextMove, TextSpeed, TextShadow, TextShadowX, TextShadowY, TextShadowRadius, BackgroundColor, TextShadowColor);
    }

    private void startshow ()
    {
        floatview = FloatTextSettingMethod.CreateFloatView(this, TextShow, TextSize, TextColor, TextThick, TextSpeed, EditID, TextShadow, TextShadowX, TextShadowY, TextShadowRadius, TextShadowColor);
        linearlayout = FloatTextSettingMethod.CreateLayout(this, EditID);
        wmParams = FloatTextSettingMethod.CreateFloatLayout(this, wm, floatview, linearlayout, FloatShow, TextTop, TextMove, BackgroundColor);
    }

    private void stopshow ()
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

    private void updatefloatview (boolean show, String Text, Float Size, int Paint, boolean Thick, boolean TextTop, boolean autotop, boolean move, int speed, boolean shadow, float shadowx, float shadowy, float shadowradius, int bac, int shadowcolor)
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
                floattext.set(EditID, Text.toString());
                utils.setFloatText(floattext);
            }
            floatview.setText(Text.toString());
            floatview.setTextSize(Size);
            floatview.setTextColor(Paint);
            floatview.setShadow(shadow, shadowx, shadowy, shadowradius, shadowcolor);
            linearlayout.setFloatLayoutParams(wmParams);
            if (((App)getApplicationContext()).getMovingMethod())
            {
                floatview.setMoving(move, 0);
            }
            else
            {
                floatview.setMoving(move, 1);
                if (move)
                {
                    linearlayout.setShowState(false);
                    linearlayout.setShowState(true);
                }
            }
            floatview.setMoveSpeed(speed);
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
                linearlayout.setTop(autotop);
                wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            }
            else
            {
                linearlayout.setTop(true);
                wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
            }
            linearlayout.setLayout_default_flags(wmParams.flags);
            linearlayout.setShowState(show);
            linearlayout.setBackgroundColor(bac);
            if (EditMode)
            {
                ArrayList<Boolean> fs = utils.getShowFloat();
                fs.set(EditID, show);
                utils.setShowFloat(fs);
            }
            wm.updateViewLayout(linearlayout, wmParams);
        }
    }

    private void saveall (Context ctx, FloatTextView fv, FloatLinearLayout fll, String text, WindowManager.LayoutParams layout, boolean savedetails, boolean show, String position, boolean texttop, boolean autotop, boolean textmove, int speed)
    {
        App utils = ((App)ctx.getApplicationContext());
        FloatTextSettingMethod.savedata(ctx, fv, fll, text, layout);
        if (!texttop)
        {
            autotop = true;
        }
        if (savedetails)
        {
            utils.addDatas(TextShow, TextColor, TextSize, TextThick, show, position, false, texttop, autotop, textmove, speed, TextShadow, TextShadowX, TextShadowY, TextShadowRadius, BackgroundColor, TextShadowColor);
        }
    }

    private void setall (int i)
    {
        Position = linearlayout.getPosition();
        App utils = ((App)getApplicationContext());
        utils.setDatas(i, floatview, linearlayout, wmParams, TextShow, TextColor, TextSize, TextThick, FloatShow, Position, LockPosition, TextTop, AutoTop, TextMove, TextSpeed, TextShadow, TextShadowX, TextShadowY, TextShadowRadius, BackgroundColor, TextShadowColor);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
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
                    Toast.makeText(this, R.string.premission_ask_failed, Toast.LENGTH_SHORT).show();
                    this.finish();
                }
            }
        }
    }

    @Override 
    public boolean onCreateOptionsMenu (Menu menu)
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
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.save_win:
                Position = linearlayout.getPosition();
                if (EditMode)
                {
                    setall(EditID);
                }
                else
                {
                    saveall(this, floatview, linearlayout, spdata.getString("TextShow", getString(R.string.default_text)), wmParams, true, FloatShow, Position, TextTop, AutoTop, TextMove, TextSpeed);
                }
                FloatWinSaved = true;
                this.finish();
                Toast.makeText(this, R.string.save_text_ok, Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete_win:
                if (!EditMode)
                {
                    stopshow();
                    Toast.makeText(this, R.string.delete_text_ok, Toast.LENGTH_SHORT).show();
                }
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
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
                        public void onClick (DialogInterface p1, int p2)
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
        return false;
    }

    @Override
    protected void onDestroy ()
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
