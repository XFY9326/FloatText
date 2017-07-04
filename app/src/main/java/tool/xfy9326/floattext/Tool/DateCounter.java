package tool.xfy9326.floattext.Tool;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tool.xfy9326.floattext.R;

public class DateCounter {
    //日期倒计时计算 usedays正计时
    public static String Count(Context ctx, String date, boolean usedays) {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", ctx.getResources().getConfiguration().locale);
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd-HH-mm", ctx.getResources().getConfiguration().locale);
        DateFormat format3 = new SimpleDateFormat("yyyy-MM-dd-HH", ctx.getResources().getConfiguration().locale);
        DateFormat format4 = new SimpleDateFormat("yyyy-MM-dd", ctx.getResources().getConfiguration().locale);
        long diff;
        diff = FormatCount(format1, date, usedays);
        if (diff >= 0) {
            return DateCount_1(ctx, diff, usedays);
        }
        diff = FormatCount(format2, date, usedays);
        if (diff >= 0) {
            return DateCount_2(ctx, diff, usedays);
        }
        diff = FormatCount(format3, date, usedays);
        if (diff >= 0) {
            return DateCount_3(ctx, diff, usedays);
        }
        diff = FormatCount(format4, date, usedays);
        if (diff >= 0) {
            return DateCount_4(ctx, diff, usedays);
        } else {
            return ctx.getString(R.string.dynamic_date_err);
        }
    }

    private static long FormatCount(DateFormat format, String date, boolean usedays) {
        try {
            Date now = new Date();
            Date set = format.parse(date);
            long diff;
            if (usedays) {
                diff = now.getTime() - set.getTime();
            } else {
                diff = set.getTime() - now.getTime();
            }
            if (diff < 0) {
                diff = 0;
            }
            return diff;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static String DateCount_1(Context ctx, long diff, boolean usedays) {
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
        if (diff > 0) {
            if (days == 0) {
                if (hours == 0) {
                    if (minutes == 0) {
                        return seconds + ctx.getString(R.string.second);
                    } else {
                        return minutes + ctx.getString(R.string.minute) + seconds + ctx.getString(R.string.second);
                    }
                } else {
                    return hours + ctx.getString(R.string.hour) + minutes + ctx.getString(R.string.minute) + seconds + ctx.getString(R.string.second);
                }
            } else {
                return days + ctx.getString(R.string.day) + hours + ctx.getString(R.string.hour) + minutes + ctx.getString(R.string.minute) + seconds + ctx.getString(R.string.second);
            }
        } else {
            if (usedays) {
                return ctx.getString(R.string.dynamic_date_notstart);
            } else {
                return ctx.getString(R.string.dynamic_date_reach);
            }
        }
    }

    private static String DateCount_2(Context ctx, long diff, boolean usedays) {
        long seconds = 0;
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        diff = diff - seconds * 1000;
        if (diff > 0) {
            if (days == 0) {
                if (hours == 0) {
                    return minutes + ctx.getString(R.string.minute);
                } else {
                    return hours + ctx.getString(R.string.hour) + minutes + ctx.getString(R.string.minute);
                }
            } else {
                return days + ctx.getString(R.string.day) + hours + ctx.getString(R.string.hour) + minutes + ctx.getString(R.string.minute);
            }
        } else {
            if (usedays) {
                return ctx.getString(R.string.dynamic_date_notstart);
            } else {
                return ctx.getString(R.string.dynamic_date_reach);
            }
        }
    }

    private static String DateCount_3(Context ctx, long diff, boolean usedays) {
        long minutes = 0;
        long seconds = 0;
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        diff = diff - minutes * 60 * 1000 - seconds * 1000;
        if (diff > 0) {
            if (days == 0) {
                return hours + ctx.getString(R.string.hour);
            } else {
                return days + ctx.getString(R.string.day) + hours + ctx.getString(R.string.hour);
            }
        } else {
            if (usedays) {
                return ctx.getString(R.string.dynamic_date_notstart);
            } else {
                return ctx.getString(R.string.dynamic_date_reach);
            }
        }
    }

    private static String DateCount_4(Context ctx, long diff, boolean usedays) {
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        long days = diff / (1000 * 60 * 60 * 24);
        diff = diff - hours * 60 * 60 * 1000 - minutes * 60 * 1000 - seconds * 1000;
        if (diff > 0) {
            return days + ctx.getString(R.string.day);
        } else {
            if (usedays) {
                return ctx.getString(R.string.dynamic_date_notstart);
            } else {
                return ctx.getString(R.string.dynamic_date_reach);
            }
        }
    }
}
