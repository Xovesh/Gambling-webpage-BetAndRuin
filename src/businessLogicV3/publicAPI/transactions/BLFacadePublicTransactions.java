package businessLogicV3.publicAPI.transactions;

import java.util.List;

import domainV2.Transaction;

public interface BLFacadePublicTransactions {
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
	public List<Transaction> getLastTransactions(String username, String token, int amount);
}
