package com.kan.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GenUtils {
	
	public static final String C_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	
	public static Date toDate(String date, String format) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.parse(date);
	}
	
	public static Date toDate(String date) {
		try {
			return toDate(date, C_DATE_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date addHoursToDate(Date date, double hrs) {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.MINUTE, (int) (hrs*60));
	    return cal.getTime();
	}
	
	public static boolean isDateBetween(Date dt, Date startDate, Date endDate) {
		return (dt.compareTo(startDate) >= 0 && dt.compareTo(endDate) <= 0);
	}
}
