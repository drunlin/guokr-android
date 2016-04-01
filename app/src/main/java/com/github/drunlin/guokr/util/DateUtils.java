package com.github.drunlin.guokr.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author drunlin@outlook.com
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {
    /**
     * 格式显示的时间。
     * @param date
     * @param currentTime
     * @return
     */
    public static String format(Date date, Calendar currentTime) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar yesterday = (Calendar) currentTime.clone();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
        String format = timeFormatter.format(date);
        if (calendar.get(Calendar.YEAR) == currentTime.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == currentTime.get(Calendar.DAY_OF_YEAR)) {
            return "今天 " + format;
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return  "昨天 " + format;
        } else {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(date);
        }
    }

    /**
     * 格式显示的时间。
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, Calendar.getInstance());
    }

    /**
     * 格式显示的时间。
     * @param date yyyy-MM-dd HH:mm 格式的时间
     * @return
     */
    public static String format(String date) {
        try {
            return format(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
