package dataAccessV3.bets;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dataAccessV3.DataAccess;
import domainV2.User;
import domainV2.bet.Bet;
import domainV2.bet.BetSuperClass;
import domainV2.question.Question;

public class DataAccessBetsImplementation implements DataAccessBets {
	private EntityManager db;
	private DataAccess dataManager;

	public DataAccessBetsImplementation(EntityManager db, DataAccess dataManager) {
		this.db = db;
		this.dataManager = dataManager;
	}

	/**
	 * This function stores a new bet
	 * 
	 * @param questionID
	 *            id of the question
	 * @param betAmount
	 *            amount of points to bet
	 * @param prediction
	 *            prediction of the user
	 * @param user2
	 *            of the user
	 */
	@Override
	public void storeNewBet(long questionID, long betAmount, String prediction, User user2) {
		User user = dataManager.getDataAccessUsers().getUserByUsername(user2.getUsername());
		Question q = dataManager.getDataAccessQuestions().getQuestionById(questionID);
		BetSuperClass b = new Bet(q, betAmount, prediction, user);
		db.getTransaction().begin();
		db.persist(b);
		q.addObserver(b);
		db.getTransaction().commit();
		dataManager.getDataAccessUsers().removeWalletFunds(user.getUsername(), betAmount,
				"prediction for question " + q.translation.getDefaultText() + " for event "
						+ dataManager.getDataAccessEvents().getEventByID(q.getParentEventId()).publicEventName);

		// Stats update
		dataManager.getDataAccessStats().updateWithBet(questionID, betAmount);
		dataManager.getDataAccessStats().updateHistoryStats(betAmount);

	}

	/**
	 * This function returns all the bets by username
	 * 
	 * @param username
	 *            of the user
	 * @return list with all the bets of a user
	 */
	@Override
	public List<BetSuperClass> getBetsByUser(String username) {
		List<BetSuperClass> events = null;
		try {
			TypedQuery<BetSuperClass> q2 = db.createQuery("SELECT us FROM BetSuperClass us WHERE us.username = ?1",
					BetSuperClass.class);
			q2.setParameter(1, username);
			events = q2.getResultList();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return events;
	}

	/**
	 * This function returns the last N bets by user
	 * 
	 * @param username
	 *            of the user
	 * @param amount
	 *            number of bets to retrieve
	 * @return list with the last n events of the user
	 */
	@Override
	public List<BetSuperClass> getLastNBetsByUser(String username, long amount) {
		List<BetSuperClass> bets = new LinkedList<BetSuperClass>();
		try {
			TypedQuery<BetSuperClass> q2 = db.createQuery(
					"SELECT us FROM BetSuperClass us WHERE us.username = ?1 ORDER BY us.id DESC", BetSuperClass.class);
			q2.setParameter(1, username);
			bets = new LinkedList<BetSuperClass>();
			List<BetSuperClass> helper = q2.getResultList();
			int j = 1;
			for (BetSuperClass e : helper) {
				bets.add(e);
				if (j == amount) {
					break;
				}
				j++;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return bets;
	}

}
