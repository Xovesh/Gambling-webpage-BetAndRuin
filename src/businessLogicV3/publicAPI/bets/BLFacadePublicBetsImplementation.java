package businessLogicV3.publicAPI.bets;

import java.util.List;

import businessLogicV3.privateAPI.BLFacadePrivate;
import domainV2.bet.BetSuperClass;

public class BLFacadePublicBetsImplementation implements BLFacadePublicBets{
	
	private BLFacadePrivate privateAPI;
	
	
	public BLFacadePublicBetsImplementation(BLFacadePrivate privateAPI) {
		this.privateAPI = privateAPI;
	}
	/**
	 * This function returns true if a user has already bet in a question
	 * 
	 * @param username of the user
	 * @param id       of the question
	 * @return true if if a user has already bet in a question
	 */
	@Override
	public boolean hasAlreadyBet(String username, String token, long id) {
		if (privateAPI.getUsers().verifyToken(username, token))
			return privateAPI.getBets().hasAlreadyBet(username, id);
		return true;
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
	public void storeNewBet(String token, long questionID, long betAmount, String prediction, String username) {
		if (privateAPI.getUsers().verifyToken(username, token))
			privateAPI.getBets().storeNewBet(questionID, betAmount, prediction, username);
	}
	/**
	 * This function returns all the bets by username
	 * 
	 * @param username of the user
	 * @return list with all the bets of a user
	 */
	@Override
	public List<BetSuperClass> getBetsByUser(String username, String token) {
		if (privateAPI.getUsers().verifyToken(username, token))
			return privateAPI.getBets().getBetsByUser(username);
		return null;
	}
	/**
	 * This function returns the last N bets by user
	 * 
	 * @param username of the user
	 * @param n        number of bets to retrieve
	 * @return list with the last n events of the user
	 */
	@Override
	public List<BetSuperClass> getNBetsByUser(String username, String token, int n) {
		if (privateAPI.getUsers().verifyToken(username, token))
			return privateAPI.getBets().getLastNBetsByUser(username, n);
		return null;
	}
}
