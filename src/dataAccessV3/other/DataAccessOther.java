package dataAccessV3.other;

import java.util.Calendar;

public interface DataAccessOther {
	/**
	 * This function creates a new Date object with a given year month and day and.
	 * The hour is set to 00:00:00;
	 * 
	 * @param year  year
	 * @param month month
	 * @param day   day
	 * @return Date type object
	 */
	public Calendar newDate(int year, int month, int day);
	
	/**
	 * This function creates some fake stats to populate the database
	 * 
	 * @param date  date of the calendar
	 * @param bets  number of bets of the day
	 * @param coins bet in that day
	 */
	public void fakeStats(Calendar date, long bets, long coins);
}
