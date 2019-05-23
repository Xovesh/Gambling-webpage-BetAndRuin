package businessLogicV3.privateAPI.bets;

import java.util.List;

import domainV2.bet.BetSuperClass;

public interface BLFacadePrivateBets {

	/**
	 * This function stores a new bet
	 * 
	 * @param questionID id of the question
	 * @param betAmount  amount of points to bet
	 * @param prediction prediction of the user
	 * @param username   of the user
	 */
	public void storeNewBet(long questionID, long betAmount, String prediction, String username);

	/**
	 * This function returns true if a user has already bet in a question
	 * 
	 * @param username of the user
	 * @param id       of the question
	 * @return true if if a user has already bet in a question
	 */
	public boolean hasAlreadyBet(String username, long id);

	/**
	 * This function returns all the bets by username
	 * 
	 * @param username of the user
	 * @return list with all the bets of a user
	 */
	public List<BetSuperClass> getBetsByUser(String username);

	/**
	 * This function returns the last N bets by user
	 * 
	 * @param username of the user
	 * @param n        number of bets to retrieve
	 * @return list with the last n events of the user
	 */
	public List<BetSuperClass> getLastNBetsByUser(String username, int n);
}
