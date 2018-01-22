package com.wudaosoft.commons.mvc.formatter;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.format.Formatter;

import com.wudaosoft.commons.mvc.exception.ServiceException;

public class TimestampFormatter implements Formatter<Date> {

	@Override
	public String print(Date object, Locale locale) {
		return null;
	}

	@Override
	public Date parse(String text, Locale locale) {

		if (Pattern.compile("[0-9]{10}").matcher(text).matches()) {
			text += "000";
		} else if (!Pattern.compile("[0-9]{13}").matcher(text).matches()) {
			ServiceException.throwParameterException();
		}

		try {
			return timestampToDate(Long.parseLong(text), locale);
		} catch (Exception e) {
			throw new ServiceException(100, "Invalid parameter.");
		}
	}
	
	private Date timestampToDate(long timestamp, Locale locale) {
		Calendar ca = Calendar.getInstance(locale);
		ca.setTimeInMillis(timestamp);
		return ca.getTime();
	}
}