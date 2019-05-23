package dataAccessV3.transactions;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dataAccessV3.DataAccess;
import domainV2.Transaction;

public class DataAccessTransactionsImplementation implements DataAccessTransactions {
	private EntityManager db;

	public DataAccessTransactionsImplementation(EntityManager db, DataAccess dataManager) {
		this.db = db;
	}

	/**
	 * This function creates a new transaction
	 * 
	 * @param username of the user
	 * @param amount   that has been increased or decreased
	 * @param type     of the transaction in/out
	 * @param message  message of the transaction
	 * @return id of the transaction
	 */
	@Override
	public int newTransaction(String username, long amount, String type, String message) {
		try {
			db.getTransaction().begin();
			Transaction trans = new Transaction(username, amount, type, message);
			db.persist(trans);
			db.getTransaction().commit();
			return trans.getId();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
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
		try {
			List<Transaction> res = new LinkedList<Transaction>();
			TypedQuery<Transaction> query = db.createQuery(
					"SELECT ts FROM Transaction ts WHERE ts.username = ?1 ORDER BY ts.date DESC", Transaction.class);
			query.setParameter(1, username);
			List<Transaction> tsList = query.getResultList();
			int i = 1;
			for (Transaction ts : tsList) {
				res.add(ts);
				if (i == amount) {
					break;
				}
				i++;
			}
			return res;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

}
