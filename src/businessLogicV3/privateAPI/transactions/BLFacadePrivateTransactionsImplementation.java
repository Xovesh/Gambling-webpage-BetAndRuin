package businessLogicV3.privateAPI.transactions;

import java.util.List;

import businessLogicV3.privateAPI.BLFacadePrivate;
import dataAccessV3.DataAccess;
import domainV2.Transaction;

public class BLFacadePrivateTransactionsImplementation implements BLFacadePrivateTransactions {
	private DataAccess dataManager;
	public BLFacadePrivateTransactionsImplementation(DataAccess dataManager, BLFacadePrivate privateAPI) {
		this.dataManager = dataManager;
	}

	/**
	 * This function creates a new transaction
	 * 
	 * @param username of the user
	 * @param amount   that has been increased or decreased
	 * @param type     of the transaction in/out
	 * @return id of the transaction
	 */
	@Override
	public int newTransaction(String username, long amount, String type, String message) {

		int tsID = dataManager.getDataAccessTransactions().newTransaction(username, amount, type, message);

		return tsID;

	}

	/**
	 * This method returns the last amount transactions
	 * 
	 * @param username of the user
	 * @param amount   of transactions to be retrieved
	 * @return List with the last amount transactions
	 */
	@Override
	public List<Transaction> getLastTransactions(String username, int amount) {

		List<Transaction> ts = dataManager.getDataAccessTransactions().getLastTransactions(username, amount);

		return ts;

	}
}
