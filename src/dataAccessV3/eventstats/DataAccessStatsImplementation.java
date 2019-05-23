package dataAccessV3.eventstats;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import dataAccessV3.DataAccess;
import domainV2.event.Event;
import domainV2.event.EventStats;
import domainV2.event.TemporalStats;
import domainV2.question.Question;

public class DataAccessStatsImplementation implements DataAccessStats {
	private EntityManager db;
	private DataAccess dataManager;

	public DataAccessStatsImplementation(EntityManager db, DataAccess dataManager) {
		this.db = db;
		this.dataManager = dataManager;
	}

	/**
	 * This function adds a new eventStats
	 * 
	 * @param id id of the event
	 */
	@Override
	public void addEventData(long id) {
		EventStats newES = new EventStats(id);
		db.getTransaction().begin();
		db.persist(newES);
		db.getTransaction().commit();
	}

	/**
	 * This function updates the stats of a event
	 * 
	 * @param id id of the event
	 */
	@Override
	public void updateDataWithEvent(long id) {
		Event e = dataManager.getDataAccessEvents().getEventByID(id);
		EventStats stats;
		try {
			TypedQuery<EventStats> q2 = db.createQuery("SELECT us FROM EventStats us WHERE us.ID = ?1",
					EventStats.class);
			q2.setParameter(1, id);
			stats = q2.getSingleResult();
			db.getTransaction().begin();
			stats.UpdateDataWithEvent(e);
			db.getTransaction().commit();
		} catch (Exception ex) {
			db.getTransaction().rollback();
			ex.printStackTrace();
			System.out.println("Upps, Something happened in the database");
		}

	}

	/**
	 * This function gets the total bets of a event
	 * 
	 * @param id id of the event
	 * @return the total bets by id
	 */
	@Override
	public long getTotalBetByID(long id) {
		EventStats stats;
		try {
			TypedQuery<EventStats> q2 = db.createQuery("SELECT us FROM EventStats us WHERE us.ID = ?1",
					EventStats.class);
			q2.setParameter(1, id);
			stats = q2.getSingleResult();
			return stats.getTotalBet();
		} catch (javax.persistence.NoResultException e) {
			System.out.println("Data not created yet, using default");
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * This function gets the total profit of a event
	 * 
	 * @param id id of the event
	 * @return the total profit by id
	 */
	@Override
	public long getTotalProfitByID(long id) {
		EventStats stats;
		try {
			TypedQuery<EventStats> q2 = db.createQuery("SELECT us FROM EventStats us WHERE us.ID = ?1",
					EventStats.class);
			q2.setParameter(1, id);
			stats = q2.getSingleResult();
			return stats.getTotalProfit();
		} catch (javax.persistence.NoResultException e) {
			System.out.println("Data not created yet, using default");
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return 0;
	}

	/**
	 * This function gets the total winrate of a event
	 * 
	 * @param id id of the event
	 * @return the average winrate by id
	 */
	@Override
	public float getAverageUserWinrateByID(long id) {
		EventStats stats;
		try {
			TypedQuery<EventStats> q2 = db.createQuery("SELECT us FROM EventStats us WHERE us.ID = ?1",
					EventStats.class);
			q2.setParameter(1, id);
			stats = q2.getSingleResult();
			return stats.getUserWinrate();
		} catch (javax.persistence.NoResultException e) {
			System.out.println("Data not created yet, using default");
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return 0;
	}

	/**
	 * This function gets the total profit by date
	 * 
	 * @param date date
	 * @return the total profit by date
	 */
	@Override
	public long getTotalProfitByDate(Calendar date) {
		List<EventStats> stats;
		long result = 0;
		try {
			TypedQuery<EventStats> q2 = db.createQuery("SELECT us FROM EventStats us WHERE date = ?1",
					EventStats.class);
			q2.setParameter(1, date, TemporalType.DATE);
			stats = q2.getResultList();
			for (EventStats es : stats) {
				result += es.getTotalProfit();
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return result;
	}

	/**
	 * This function gets the total bets by date
	 * 
	 * @param date date
	 * @return the total bets by date
	 */
	@Override
	public long getTotalBetByDate(Calendar date) {
		List<EventStats> stats;
		long result = 0;
		try {
			TypedQuery<EventStats> q2 = db.createQuery("SELECT us FROM EventStats us WHERE date = ?1",
					EventStats.class);
			q2.setParameter(1, date, TemporalType.DATE);
			stats = q2.getResultList();
			for (EventStats es : stats) {
				result += es.getTotalBet();
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return result;
	}

	/**
	 * This function update the bets of a event
	 * 
	 * @param questionID id of the question
	 * @param betAmount  amount of points to increase
	 */
	@Override
	public void updateWithBet(long questionID, long betAmount) {
		Question q = dataManager.getDataAccessQuestions().getQuestionById(questionID);
		EventStats stats;
		try {
			TypedQuery<EventStats> q2 = db.createQuery("SELECT us FROM EventStats us WHERE us.ID = ?1",
					EventStats.class);
			q2.setParameter(1, q.getParentEventId());
			stats = q2.getSingleResult();
			db.getTransaction().begin();
			stats.UpdateDataWithBet(betAmount);
			db.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
	}
	/**
	 * This function gets the average win rate by date
	 * 
	 * @param date date
	 * @return the average winrate by date
	 */
	@Override
	public float getAverageUserWinrateByDate(Calendar date) {
		List<EventStats> stats;
		float result = 0;
		try {
			TypedQuery<EventStats> q2 = db.createQuery("SELECT us FROM EventStats us WHERE date = ?1",
					EventStats.class);
			q2.setParameter(1, date, TemporalType.DATE);
			stats = q2.getResultList();
			for (EventStats es : stats) {
				result += es.getUserWinrate();
			}
			result = result / stats.size();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return result;
	}
	/**
	 * This function updates the history of the actual date
	 * 
	 * @param betAmount amount of points to add
	 */
	@Override
	public void updateHistoryStats(long betAmount) {
		TemporalStats stats;
		try {
			TypedQuery<TemporalStats> q2 = db.createQuery("SELECT us FROM TemporalStats us WHERE us.date = ?1",
					TemporalStats.class);
			q2.setParameter(1, new GregorianCalendar(), TemporalType.DATE);
			stats = q2.getSingleResult();
			db.getTransaction().begin();
			stats.addBet(betAmount);
			db.getTransaction().commit();
		} catch (javax.persistence.NoResultException e) {
			System.out.println("Data not found, creating . . .");
			stats = new TemporalStats();
			stats.addBet(betAmount);
			System.out.println(stats.getBets() + " " + stats.getCoins() + " " + stats.getDate().getTime().toString());
			db.getTransaction().begin();
			db.persist(stats);
			db.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database updateHistoryStats");
		}
	}
	/**
	 * This function returns the stats by date
	 * 
	 * @param date date
	 * @return temporal stats of the date
	 */
	@Override
	public TemporalStats getStatsByDate(Calendar date) {
		TemporalStats stats = new TemporalStats();
		try {
			TypedQuery<TemporalStats> q2 = db.createQuery("SELECT us FROM TemporalStats us WHERE us.date = ?1",
					TemporalStats.class);
			q2.setParameter(1, date, TemporalType.DATE);
			stats = q2.getSingleResult();
			return stats;
		} catch (javax.persistence.NoResultException e) {
			System.out.println("Data not found");
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database getStatsByDate");
			e.printStackTrace();
		}
		return stats;
	}
	/**
	 * This function returns the last N stats
	 * 
	 * @param n amount of stats
	 * @return list with temporal stats
	 */
	@Override
	public List<TemporalStats> getNStats(int n) {
		List<TemporalStats> stats = new LinkedList<>();
		List<TemporalStats> result = new LinkedList<>();
		try {
			TypedQuery<TemporalStats> q2 = db.createQuery("SELECT us FROM TemporalStats us ORDER BY us.date DESC",
					TemporalStats.class);
			stats = q2.getResultList();
			if(!(stats.get(0).getDate().getTime().toString().substring(0, 10).equals(new GregorianCalendar().getTime().toString().substring(0, 10)))) {
				result.add(new TemporalStats());
			}
			for(int i=0; i<n;i++) {
				result.add(stats.get(i));
			}
			return result;
		} catch (javax.persistence.NoResultException e) {
			System.out.println("Data not found");
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database getStatsByDate");
			e.printStackTrace();
		}
		return stats;
	}
	/**
	 * This function returns the total coins bet
	 * 
	 * @return total amount of coins bet in total
	 */
	@Override
	public long totalCoinsBet() {
		long output = 0;
		List<TemporalStats> stats = new LinkedList<>();
		try {
			TypedQuery<TemporalStats> q2 = db.createQuery("SELECT us FROM TemporalStats us", TemporalStats.class);
			stats = q2.getResultList();
			for (TemporalStats t : stats) {
				output += t.getCoins();
			}
		} catch (javax.persistence.NoResultException e) {
			System.out.println("Data not found");
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database getStatsByDate");
			e.printStackTrace();
		}
		return output;
	}
}
