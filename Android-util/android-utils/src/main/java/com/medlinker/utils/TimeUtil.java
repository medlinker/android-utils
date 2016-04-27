package com.medlinker.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author <a href="mailto:tql@medlinker.net">tqlmorepassion</a>
 * @version 1.0
 * @description 时间工具类
 * @time 2016/4/26 20:19
 */
public class TimeUtil {

    /**
     * 时间格式示例
     */
    public interface PatternStr {
        String HHmm = "HH:mm";
        String yyMMdd = "yyyy.MM.dd";
        String MMddHHmm = "MM-dd HH:mm";
        String yyMMddHHmm = "yy.MM.dd HH:mm";
        String yyyyMMddHHmm = "yyyy.MM.dd  HH:mm";
        String yyyyMMddHHmmss = "yyyy.MM.dd  HH:mm:ss";
        String yyyyMM = "yyyy.MM";
        String yyyyMMdd = "yyyy-MM-dd";
    }

    /**
     * 格式化时间 毫秒
     * @param milliSecond
     * @return
     */
    public static String formatTimeByPattern(long milliSecond, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(milliSecond));
    }

    /**
     * 格式化时间  string
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     * @param strDate
     * @return
     */
    public static Date formatTimeByPattern(String strDate, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(strDate, new ParsePosition(0));
    }

    /**
     * 得到当前时间的凌晨时间
     * 2016-4-27 00：00:00
     *
     * @param milliSecond
     * @return
     */
    public static long getZeroTime(long milliSecond) {
        String nowDate = formatTimeByPattern(milliSecond, PatternStr.yyyyMMdd);
        String strdate = nowDate + " 00:00:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date newdate = format.parse(strdate);
            return newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
    }

    /**
     * 智能时间显示格式
     * 规则：当天则显示为 09:43 昨天显示：昨天  否则：2015.09.20
     *
     * @param milliSecond
     * @return
     */
    public static String smartMilliToDate(long milliSecond) {
        Calendar mCalendar = Calendar.getInstance();
        long oneDayMilli = 24 * 60 * 60 * 1000;
        long hour = mCalendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000;
        long minute = mCalendar.get(Calendar.MINUTE) * 60 * 1000;
        long second = mCalendar.get(Calendar.SECOND) * 1000;
        long milli = mCalendar.get(Calendar.MILLISECOND);
        long milliBeyondZeroClock = hour + minute + second + milli;//当前时间距离当天凌晨的毫秒数
        long difference = System.currentTimeMillis() - milliSecond;
        if (difference < milliBeyondZeroClock) {
            //当天
            return formatTimeByPattern(milliSecond, PatternStr.HHmm);
        } else if (difference > milliBeyondZeroClock && difference < milliBeyondZeroClock + oneDayMilli) {
            //昨天
            return "昨天";
        } else {
            //昨天以前
            return formatTimeByPattern(milliSecond, PatternStr.yyMMdd);
        }
    }

    /**
     * 得到当前时间的偏移量，
     *
     * @param d   当前时间
     * @param day 偏移的值，正数为几天后，负数为几天前
     * @return
     */
    public static Date getDateBeforeOrAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 获取持续时间：格式为40'30''
     *
     * @param seconds 秒
     * @return String
     */
    public static String getDuration(int seconds) {
        StringBuilder builder = new StringBuilder();
        if (seconds <= 60) {
            String secondsStr = seconds > 10 ? String.valueOf(seconds) : ("0" + seconds);
            builder.append(secondsStr).append("''");
        } else {
            int minutes = seconds / 60;
            int subSeconds = seconds % 60;

            String minutesStr = seconds > 10 ? String.valueOf(minutes) : ("0" + minutes);
            String subSecondsStr = subSeconds > 10 ? String.valueOf(subSeconds) : ("0" + subSeconds);
            builder.append(minutesStr).append("'").append(subSecondsStr).append("''");
        }
        return builder.toString();
    }

    /**
     * 是否同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(long date1, long date2) {
        long days1 = date1 / (1000 * 60 * 60 * 24);
        long days2 = date2 / (1000 * 60 * 60 * 24);
        return days1 == days2;
    }

    /**
     * 得到现在时间
     *
     * @return
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        return currentTime;
    }

    /**
     * 提取一个月中的最后一天
     *
     * @param day
     * @return
     */
    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }

    /**
     * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
     */
    public static String getTwoHour(String st1, String st2) {
        String[] kk = null;
        String[] jj = null;
        kk = st1.split(":");
        jj = st2.split(":");
        if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
            return "0";
        else {
            double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60;
            double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60;
            if ((y - u) > 0)
                return y - u + "";
            else
                return "0";
        }
    }

    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 判断是否润年
     *
     * @param ddate
     * @return
     */
    public static boolean isLeapYear(String ddate) {

        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        Date d = formatTimeByPattern(ddate, PatternStr.yyyyMMdd);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(Calendar.YEAR);
        if ((year % 400) == 0)
            return true;
        else if ((year % 4) == 0) {
            if ((year % 100) == 0)
                return false;
            else
                return true;
        } else
            return false;
    }

    /**
     * 判断二个时间是否在同一个周
     */
    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    /**
     * 获取一个月的最后一天
     */
    public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
        String str = dat.substring(0, 8);
        String month = dat.substring(5, 7);
        int mon = Integer.parseInt(month);
        if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
            str += "31";
        } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
            str += "30";
        } else {
            if (isLeapYear(dat)) {
                str += "29";
            } else {
                str += "28";
            }
        }
        return str;
    }
}
