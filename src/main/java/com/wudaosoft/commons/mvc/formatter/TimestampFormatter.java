/**
 *    Copyright 2009-2018 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.wudaosoft.commons.mvc.formatter;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.format.Formatter;

import com.wudaosoft.commons.mvc.exception.ServiceException;

/**
 * 
 * @author Changsoul Wu
 *
 */
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