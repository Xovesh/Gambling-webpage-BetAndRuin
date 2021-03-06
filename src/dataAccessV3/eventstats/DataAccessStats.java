package dataAccessV3.eventstats;

import java.util.Calendar;
import java.util.List;

import domainV2.event.TemporalStats;

public interface DataAccessStats {
	/**
	 * This function adds a new eventStats
	 * 
	 * @param id id of the event
	 */
	public void addEventData(long id);

	/**
	 * This function updates the stats of a event
	 * 
	 * @param id id of the event
	 */
	public void updateDataWithEvent(long id);

	/**
	 * This function gets the total bets of a event
	 * 
	 * @param id id of the event
	 * @return the total bets by id
	 */
	public long getTotalBetByID(long id);

	/**
	 * This function gets the total profit of a event
	 * 
	 * @param id id of the event
	 * @return the total profit by id
	 */
	public long getTotalProfitByID(long id);

	/**
	 * This function gets the total winrate of a event
	 * 
	 * @param id id of the event
	 * @return the average winrate by id
	 */
	public float getAverageUserWinrateByID(long id);

	/**
	 * This function gets the total profit by date
	 * 
	 * @param date date
	 * @return the total profit by date
	 */
	public long getTotalProfitByDate(Calendar date);

	/**
	 * This function gets the total bet by date
	 * 
	 * @param date date
	 * @return the total bets by date
	 */
	public long getTotalBetByDate(Calendar date);

	/**
	 * This function gets the average win rate by date
	 * 
	 * @param date date
	 * @return the average winrate by date
	 */
	public float getAverageUserWinrateByDate(Calendar date);

	/**
	 * This function updates the history of the actual date
	 * 
	 * @param betAmount amount of points to add
	 */
	public void updateHistoryStats(long betAmount);

	/**
	 * This function returns the stats by date
	 * 
	 * @param date date
	 * @return temporal stats of the date
	 */
	public TemporalStats getStatsByDate(Calendar date);

	/**
	 * This function update the bets of a event
	 * 
	 * @param questionID id of the question
	 * @param betAmount  amount of points to increase
	 */
	public void updateWithBet(long questionID, long betAmount);

	/**
	 * This function returns the total coins bet
	 * 
	 * @return total amount of coins bet in total
	 */
	public long totalCoinsBet();

	/**
	 * This function returns the last N stats
	 * 
	 * @param n amount of stats
	 * @return list with temporal stats
	 */
	public List<TemporalStats> getNStats(int n);
}
