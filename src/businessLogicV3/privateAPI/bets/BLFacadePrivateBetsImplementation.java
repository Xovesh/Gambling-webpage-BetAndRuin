package businessLogicV3.privateAPI.bets;

import java.util.List;

import businessLogicV3.privateAPI.BLFacadePrivate;
import dataAccessV3.DataAccess;
import domainV2.bet.BetSuperClass;

public class BLFacadePrivateBetsImplementation implements BLFacadePrivateBets {

	private DataAccess dataManager;
	private BLFacadePrivate privateAPI;

	public BLFacadePrivateBetsImplementation(DataAccess dataManager, BLFacadePrivate privateAPI) {
		this.dataManager = dataManager;
		this.privateAPI = privateAPI;
	}

	/**
	 * This function stores a new bet
	 * 
	 * @param questionID id of the question
	 * @param betAmount  amount of points to bet
	 * @param prediction prediction of the user
	 * @param username   of the user
	 */
	@Override
	public void storeNewBet(long questionID, long betAmount, String prediction, String username) {
		if (hasAlreadyBet(username, questionID)) {
			return;
		} else {
			dataManager.getDataAccessBets().storeNewBet(questionID, betAmount, prediction,
					dataManager.getDataAccessUsers().getUserByUsername(username));
			privateAPI.getUsers().updateUser(username);
		}
	}

	/**
	 * This function returns true if a user has already bet in a question
	 * 
	 * @param username of the user
	 * @param id       of the question
	 * @return true if if a user has already bet in a question
	 */
	@Override
	public boolean hasAlreadyBet(String username, long id) {
		List<BetSuperClass> lB = dataManager.getDataAccessBets().getBetsByUser(username);
		for (BetSuperClass b : lB)
			if (b.getQuestion().getId() == id)
				return true;
		return false;
	}

	/**
	 * This function returns all the bets by username
	 * 
	 * @param username of the user
	 * @return list with all the bets of a user
	 */
	@Override
	public List<BetSuperClass> getBetsByUser(String username) {
		return dataManager.getDataAccessBets().getBetsByUser(username);
	}

	/**
	 * This function returns the last N bets by user
	 * 
	 * @param username of the user
	 * @param n        number of bets to retrieve
	 * @return list with the last n events of the user
	 */
	@Override
	public List<BetSuperClass> getLastNBetsByUser(String username, int n) {
		return dataManager.getDataAccessBets().getLastNBetsByUser(username, n);
	}
}
