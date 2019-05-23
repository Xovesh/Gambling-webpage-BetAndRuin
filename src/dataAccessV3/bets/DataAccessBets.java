package dataAccessV3.bets;

import java.util.List;

import domainV2.User;
import domainV2.bet.BetSuperClass;

public interface DataAccessBets {
	/**
	 * This function stores a new bet
	 * 
	 * @param questionID id of the question
	 * @param betAmount  amount of points to bet
	 * @param prediction prediction of the user
	 * @param user2 user which is assigned the bet
	 */
	public void storeNewBet(long questionID, long betAmount, String prediction, User user2);

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
	 * @param amount        number of bets to retrieve
	 * @return list with the last n events of the user
	 */
	public List<BetSuperClass> getLastNBetsByUser(String username, long amount);
}
