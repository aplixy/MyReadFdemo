package com.xinyu.xylibrary.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {


    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";
    public final static String YYYY_MM = "yyyy-MM";
    public final static String YYYYMMDD = "yyyyMMdd";
    public final static String MM_DD_HH_MM = "MM-dd HH:mm";
    public final static String MM_DD = "MM-dd";
    public final static String HH_MM_SS = "HH:mm:ss";
    public final static String YYYYSMMSDD = "yyyy/MM/dd";
    public final static String YYYYDMMSDD = "yyyy.MM.dd";

    public static String mmddFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            return getFormatter(MM_DD).format(date);

        }
    }

    /***
     * 返回 2015/10/15格式的日期
     *
     * @param milliseconds
     * @return
     */
    public static String yyyymmdd(Long milliseconds) {
        Date date = new Date(milliseconds);
        return getFormatter(YYYYSMMSDD).format(date);
    }

    /***
     * 返回 2015.10.15格式的日期
     *
     * @param milliseconds
     * @return
     */
    public static String yyyymmddByDot(Long milliseconds) {
        Date date = new Date(milliseconds);
        return getFormatter(YYYYDMMSDD).format(date);
    }

    public static String yyyymmddhhmmssFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            return getFormatter(YYYY_MM_DD_HH_MM_SS).format(date);
        }
    }

    public static String yyyymmddhhmmssFormat(Long timestamp) {
        if (timestamp == 0L) {
            return "";
        } else {
            return getFormatter(YYYY_MM_DD_HH_MM_SS).format(timestamp);
        }
    }

    public static String hhmmssFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            return getFormatter(HH_MM_SS).format(date);
        }
    }


    public static String yyyymmddhhmmFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            return getFormatter(YYYY_MM_DD_HH_MM).format(date);
        }
    }

    public static String yyyymmddhhmmFormat(Long timestamp) {
        if (timestamp == 0L) {
            return "";
        } else {
            return getFormatter(YYYY_MM_DD_HH_MM).format(timestamp);
        }
    }

    public static String mmddhhmmFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            return getFormatter(MM_DD_HH_MM).format(date);

        }
    }

    public static String mmddhhmmFormat(long timestamp) {
        if (timestamp == 0L) {
            return "";
        } else {
            return getFormatter(MM_DD_HH_MM).format(timestamp);

        }
    }

    public static String yyyymmddNoSpaceFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            return getFormatter(YYYYMMDD).format(date);

        }
    }

    public static String yyyymmddFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            return getFormatter(YYYY_MM_DD).format(date);

        }
    }

    public static String yyyymmddFormat(long timestamp) {
        if (timestamp == 0L) {
            return "";
        } else {
            return getFormatter(YYYY_MM_DD).format(timestamp);

        }
    }

    public static String yyyymmFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            return getFormatter(YYYY_MM).format(date);

        }
    }

    public static Date formateDateStryyyymmdd(String dateStr) {

        SimpleDateFormat formatter = getFormatter(YYYY_MM_DD);
        Date date = null;
        try {
            date = formatter.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date formateDateStryyyymmddhhmm(String dateStr) {

        SimpleDateFormat formatter = getFormatter(YYYY_MM_DD_HH_MM);
        Date date = null;
        try {
            date = formatter.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 例如:2016年6月
     *
     * @param time
     * @return
     */
    public static final String formatYYYYMMChinese(long time) {
        SimpleDateFormat formatter = getFormatter("yyyy年MM月");
        return formatter.format(time);
    }

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getFormatter(String parttern) {
        return new SimpleDateFormat(parttern);
    }

    /**
     * 计算几分钟前工具
     */
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    // public static void main(String[] args) throws ParseException {
    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
    // Date date = format.parse("2013-11-11 18:35:35");
    // System.out.println(format(date));
    // }

    /**
     * 格式化显示时间
     *
     * @param deltaDate 时间间隔
     * @return
     */
    @Deprecated
    public static String format(Date deltaDate) {
        // long delta = new Date().getTime() - date.getTime();
        long delta = deltaDate.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    /**
     * 格式化信息流中要显示的时间
     *
     * @param time 要格式化的时间，单位为毫秒
     * @return
     */
    public static String formatTimelineTime(long time) {
        return format(new Date(new Date().getTime() - time));
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


    private static final String[] MONTHS = {
            "一月",
            "二月",
            "三月",
            "四月",
            "五月",
            "六月",
            "七月",
            "八月",
            "九月",
            "十月",
            "十一月",
            "十二月",
    };

    public static String getDayText(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        return calendar.get(Calendar.DAY_OF_MONTH) + "";
    }

    public static String getMonthText(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));

        return MONTHS[calendar.get(Calendar.MONTH)];
    }

    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        return isSameDay(new Date(), date);
    }

    /**
     * 是否是同一天
     *
     * @param src
     * @param dest
     * @return
     */
    public static boolean isSameDay(Date src, Date dest) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(src);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dest);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }
	
	public static int dayOffset(long millis) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		int offset = 0;
		try {
			offset = (int) ((millis - sdf.parse(sdf.format(new Date())).getTime()) / 86400000);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return offset;
	}
}
