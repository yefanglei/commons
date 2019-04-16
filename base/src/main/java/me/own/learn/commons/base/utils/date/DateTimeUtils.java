package me.own.learn.commons.base.utils.date;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by christopher.wang on 2015/9/14.
 */
public class DateTimeUtils {

    /**
     * 根据年/月/日 时/分/秒 格式获取当前时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        try {
            String datestr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date createdDate = sdf.parse(datestr);
            return createdDate;
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 将日期转化格式，如2015年9月14日10:37:56转化为"14/周一"
     *
     * @param cal
     * @return
     */
    public static String getChineseWeekDays(Calendar cal) {
        String ret;
        int int_day_of_month = cal.get(Calendar.DAY_OF_MONTH);
        DecimalFormat df = new DecimalFormat("#00");
        String str_day_of_month = df.format(int_day_of_month);
        int int_day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        String str_day_of_week = "";
        switch (int_day_of_week) {
            case 1:
                str_day_of_week = "周一";
                break;
            case 2:
                str_day_of_week = "周二";
                break;
            case 3:
                str_day_of_week = "周三";
                break;
            case 4:
                str_day_of_week = "周四";
                break;
            case 5:
                str_day_of_week = "周五";
                break;
            case 6:
                str_day_of_week = "周六";
                break;
            case 7:
                str_day_of_week = "周日";
                break;
            default:
                break;
        }
        ret = str_day_of_month + "/" + str_day_of_week;
        return ret;
    }

    /**
     * 加一天
     */
    public static String plusOneDay(String day) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (day.length() != 0 && null != day) {
            try {
                Date dd = df.parse(day);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dd);
                calendar.add(Calendar.DAY_OF_MONTH, 1);// 加一天
                String outd = df.format(calendar.getTime());
                return outd;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 减一天
     */
    public static String minusOneDay(String day) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (day.length() != 0 && null != day) {
            try {
                long dif = df.parse(day).getTime() - 86400 * 1000;// 减一天
                Date date = new Date();
                date.setTime(dif);
                String outd = df.format(date);
                return outd;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 加一天
     */
    public static Date plusOneDay(Date date) {
        if (null != date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, +1);
            date = calendar.getTime();
            return date;
        }
        return null;
    }

    /**
     * 减一天
     */
    public static Date minusOneDay(Date date) {
        if (null != date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            date = calendar.getTime();
            return date;
        }
        return null;
    }

    /**
     * Get first day of next month of the pass in date
     *
     * @return
     */
    public static Date nextMonthFirstDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    /**
     * Get first day of the pass in year. month
     *
     * @return
     */
    public static Date currentMonthFirstDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * Check if current date between start and enddate
     *
     * @param startDate
     * @param endDate
     * @return true if between, otherwise false
     */
    public static boolean between(Date startDate, Date endDate) {
        long curTM = System.currentTimeMillis();
        if (startDate.getTime() <= curTM && endDate.getTime() + 86400000 >= curTM) {
            return true;
        }
        return false;
    }
}



