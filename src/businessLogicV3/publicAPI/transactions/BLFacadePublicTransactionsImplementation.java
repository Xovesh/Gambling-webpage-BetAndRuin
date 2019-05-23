package businessLogicV3.publicAPI.transactions;

import java.util.List;

import businessLogicV3.privateAPI.BLFacadePrivate;
import domainV2.Transaction;

public class BLFacadePublicTransactionsImplementation implements BLFacadePublicTransactions{

	private BLFacadePrivate privateAPI;
	public BLFacadePublicTransactionsImplementation(BLFacadePrivate privateAPI) {
		this.privateAPI = privateAPI;
	}
	
	/**
	 * This method returns the last amount transactions
	 * 
	 * @param username
	 *            of the user
	 * @param token
	 *            of the user
	 * @param amount
	 *            of transactions to be retrieved
	 * @return List with the last amount transactions
	 */
	@Override
	public List<Transaction> getLastTransactions(String username, String token, int amount) {
		if (privateAPI.getUsers().verifyToken(username, token)) {
			return privateAPI.getTransactions().getLastTransactions(username, amount);
		} else {
			return null;
		}
	}
}
