package com.systex.msg.practice.domain.train.outbound;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeDate {

	private Integer year;
	private String month;
	private Integer monthValue;
	private Integer dayOfMonth;
	private String dayOfWeek;
	private String era;
	private Integer dayOfYear;
	private Boolean leapYear;
	private Map<String,String>chronology;
	public TakeDate(LocalDate takeDate) {

		year = takeDate.getYear();
		month = takeDate.getMonth().getDisplayName(TextStyle.FULL,Locale.ENGLISH).toUpperCase();
		monthValue = takeDate.getMonthValue();
		dayOfMonth = takeDate.getDayOfMonth();
		dayOfWeek = takeDate.getDayOfWeek().getDisplayName(TextStyle.FULL,Locale.getDefault()).toUpperCase();
		era = takeDate.getEra().toString();
		dayOfYear = takeDate.getDayOfYear();
		leapYear = takeDate.isLeapYear();
		chronology = new HashMap<String,String>();
		chronology.put("id",takeDate.getChronology().getId());
		chronology.put("calendarType",takeDate.getChronology().getCalendarType());
	
	}
}
