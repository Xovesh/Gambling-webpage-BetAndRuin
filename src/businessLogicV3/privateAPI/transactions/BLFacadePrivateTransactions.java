package businessLogicV3.privateAPI.transactions;

import java.util.List;

import domainV2.Transaction;

public interface BLFacadePrivateTransactions {
	/**
	 * This function creates a new transaction
	 * 
	 * @param username
	 *            of the user
	 * @param amount
	 *            that has been increased or decreased
	 * @param type
	 *            of the transaction in/out
	 * @param message message for the transaction
	 * @return id of the transaction
	 */
	public int newTransaction(String username, long amount, String type, String message);

	/**
	 * This method returns the last amount transactions
	 * 
	 * @param username
	 *            of the user
	 * @param amount
	 *            of transactions to be retrieved
	 * @return List with the last amount transactions
	 */
	public List<Transaction> getLastTransactions(String username, int amount);
}
