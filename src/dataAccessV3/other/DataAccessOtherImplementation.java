package dataAccessV3.other;

import java.util.Calendar;

import javax.persistence.EntityManager;

import dataAccessV3.DataAccess;
import domainV2.event.TemporalStats;

public class DataAccessOtherImplementation implements DataAccessOther {

	private EntityManager db;

	public DataAccessOtherImplementation(EntityManager db, DataAccess dataManager) {
		this.db = db;
	}

	/**
	 * This function creates a new Date object with a given year month and day and.
	 * The hour is set to 00:00:00;
	 * 
	 * @param year  year
	 * @param month month
	 * @param day   day
	 * @return Date type object
	 */
	@Override
	public Calendar newDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	/**
	 * This function creates some fake stats to populate the database
	 * 
	 * @param date  date of the calendar
	 * @param bets  number of bets of the day
	 * @param coins bet in that day
	 */
	@Override
	public void fakeStats(Calendar date, long bets, long coins) {
		db.getTransaction().begin();
		TemporalStats stats = new TemporalStats(date, bets, coins);
		db.persist(stats);
		db.getTransaction().commit();
	}

}
