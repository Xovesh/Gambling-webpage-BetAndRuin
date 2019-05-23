package businessLogicV3.publicAPI.bets;

import java.util.List;

import domainV2.bet.BetSuperClass;

public interface BLFacadePublicBets {
	/**
	 * This function returns true if a user has already bet in a question
	 * 
	 * @param username of the user
	 * @param token    token of the user
	 * @param id       of the question
	 * @return true if if a user has already bet in a question
	 */
	public boolean hasAlreadyBet(String username, String token, long id);

	/**
	 * This function stores a new bet
	 * 
	 * @param questionID id of the question
	 * @param token      token of the user
	 * @param betAmount  amount of points to bet
	 * @param prediction prediction of the user
	 * @param username   of the user
	 */
	public void storeNewBet(String token, long questionID, long betAmount, String prediction, String username);

	/**
	 * This function returns all the bets by username
	 * 
	 * @param username of the user
	 * @param token    token of the user
	 * @return list with all the bets of a user
	 */
	public List<BetSuperClass> getBetsByUser(String username, String token);

	/**
	 * This function returns the last N bets by user
	 * 
	 * @param username of the user
	 * @param n        number of bets to retrieve
	 * @param token    token of the user
	 * @return list with the last n events of the user
	 */
	public List<BetSuperClass> getNBetsByUser(String username, String token, int n);
}
