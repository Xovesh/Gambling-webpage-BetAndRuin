package testsV2;

import java.util.List;

import businessLogicV3.publicAPI.BLFacadePublic;
import domainV2.Transaction;

/**
 * The objective of this class is to test the transactions
 *
 */
public class transactionTest {

	public static void main(String[] args) {
		BLFacadePublic dataManager = new BLFacadePublic();
		String username = "Ccass";
		String s = dataManager.getUsers().authentication("Ccass", "Patata");
		dataManager.getTransactions().getLastTransactions(username, s, 50);
		
		dataManager.getUsers().increaseFunds(username, s, 200, "IN");
		dataManager.getUsers().decreaseFunds(username, s, 100, "OUT");
		List<Transaction> p = dataManager.getTransactions().getLastTransactions(username, s, 50);
		for(Transaction l: p) {
			System.out.println(l.toString());
		}
	}

}
