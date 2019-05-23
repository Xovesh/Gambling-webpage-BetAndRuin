package businessLogicV3.privateAPI.eventstats;

import java.util.Calendar;
import java.util.List;

import businessLogicV3.privateAPI.BLFacadePrivate;
import dataAccessV3.DataAccess;
import domainV2.event.TemporalStats;

public class BLFacadePrivateEventStatsImplementation implements BLFacadePrivateEventStats {
	private DataAccess dataManager;

	public BLFacadePrivateEventStatsImplementation(DataAccess dataManager, BLFacadePrivate privateAPI) {
		this.dataManager = dataManager;

	}

	/**
	 * This function adds a new eventStats
	 * 
	 * @param id id of the event
	 */
	@Override
	public void addEventData(long id) {
		dataManager.getDataAccessStats().addEventData(id);
	}

	/**
	 * This function updates the stats of a event
	 * 
	 * @param id id of the event
	 */
	@Override
	public void updateDataWithEvent(long id) {
		dataManager.getDataAccessStats().updateDataWithEvent(id);
	}

	/**
	 * This function gets the total bets of a event
	 * 
	 * @param id id of the event
	 * @return the total bets by id
	 */
	@Override
	public long getTotalBetByID(long id) {
		return dataManager.getDataAccessStats().getTotalBetByID(id);
	}

	/**
	 * This function gets the total profit of a event
	 * 
	 * @param id id of the event
	 * @return the total profit by id
	 */
	@Override
	public long getTotalProfitByID(long id) {
		return dataManager.getDataAccessStats().getTotalProfitByID(id);
	}

	/**
	 * This function gets the total winrate of a event
	 * 
	 * @param id id of the event
	 * @return the average winrate by id
	 */
	@Override
	public float getAverageUserWinrateByID(long id) {
		return dataManager.getDataAccessStats().getAverageUserWinrateByID(id);
	}

	/**
	 * This function gets the total profit by date
	 * 
	 * @param date date
	 * @return the total profit by date
	 */
	@Override
	public long getTotalProfitByDate(Calendar date) {
		return dataManager.getDataAccessStats().getTotalProfitByDate(date);
	}

	/**
	 * This function gets the total bet by date
	 * 
	 * @param date date
	 * @return the total bets by date
	 */
	@Override
	public long getTotalBetByDate(Calendar date) {
		return dataManager.getDataAccessStats().getTotalBetByDate(date);
	}

	/**
	 * This function gets the average win rate by date
	 * 
	 * @param date date
	 * @return the average winrate by date
	 */
	@Override
	public float getAverageUserWinrateByDate(Calendar date) {
		return dataManager.getDataAccessStats().getAverageUserWinrateByDate(date);
	}

	/**
	 * This function returns the stats by date
	 * 
	 * @param date date
	 * @return temporal stats of the date
	 */
	@Override
	public TemporalStats getStatsByDate(Calendar date) {
		return dataManager.getDataAccessStats().getStatsByDate(date);
	}

	/**
	 * This function return the all time coins
	 * 
	 * @return the coins spent in all the history of the application
	 */
	@Override
	public long getAllTimeCoins() {
		return dataManager.getDataAccessStats().totalCoinsBet();
	}

	/**
	 * This function returns the last N stats
	 * 
	 * @param n amount of stats
	 * @return list with temporal stats
	 */
	@Override
	public List<TemporalStats> getNStats(int n) {
		return dataManager.getDataAccessStats().getNStats(n);
	}
}
