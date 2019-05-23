package dataAccessV3;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import configuration.ConfigXML;
import dataAccessV3.bets.DataAccessBets;
import dataAccessV3.bets.DataAccessBetsImplementation;
import dataAccessV3.email.DataAccessEmail;
import dataAccessV3.email.DataAccessEmailImplementation;
import dataAccessV3.events.DataAccessEvents;
import dataAccessV3.events.DataAccessEventsImplementation;
import dataAccessV3.eventstats.DataAccessStats;
import dataAccessV3.eventstats.DataAccessStatsImplementation;
import dataAccessV3.news.DataAccessNews;
import dataAccessV3.news.DataAccessNewsImplementation;
import dataAccessV3.other.DataAccessOther;
import dataAccessV3.other.DataAccessOtherImplementation;
import dataAccessV3.questions.DataAccessQuestions;
import dataAccessV3.questions.DataAccessQuestionsImplementation;
import dataAccessV3.transactions.DataAccessTransactions;
import dataAccessV3.transactions.DataAccessTransactionsImplementation;
import dataAccessV3.users.DataAccessUsers;
import dataAccessV3.users.DataAccessUsersImplementation;

public class DataAccess {
	protected static EntityManager db;
	protected static EntityManagerFactory emf;
	ConfigXML c;

	public DataAccessBets dataAccessBets;
	public DataAccessEmail dataAccessEmail;
	public DataAccessEvents dataAccessEvents;
	public DataAccessNews dataAccessNews;
	public DataAccessOther dataAccessOther;
	public DataAccessQuestions dataAccessQuestions;
	public DataAccessTransactions dataAccessTransactions;
	public DataAccessUsers dataAccessUsers;
	public DataAccessStats dataAccessStats;

	public DataAccess(boolean initializeMode) {
		c = ConfigXML.getInstance();
		String fileName = c.getDbFilename();
		if (initializeMode)
			fileName = fileName + ";drop";

		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());

			emf = Persistence.createEntityManagerFactory(
					"objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, properties);

			db = emf.createEntityManager();
		}

		this.dataAccessBets = new DataAccessBetsImplementation(db, this);
		this.dataAccessEmail = new DataAccessEmailImplementation(db, this);
		this.dataAccessEvents = new DataAccessEventsImplementation(db, this);
		this.dataAccessNews = new DataAccessNewsImplementation(db, this);
		this.dataAccessOther = new DataAccessOtherImplementation(db, this);
		this.dataAccessQuestions = new DataAccessQuestionsImplementation(db, this);
		this.dataAccessTransactions = new DataAccessTransactionsImplementation(db, this);
		this.dataAccessUsers = new DataAccessUsersImplementation(db, this);
		this.dataAccessStats = new DataAccessStatsImplementation(db, this);
	}

	public DataAccess() {
		new DataAccess(false);
	}

	/**
	 * This function closes the database
	 */
	public void close() {
		db.close();
	}

	public DataAccessBets getDataAccessBets() {
		return dataAccessBets;
	}

	public DataAccessEmail getDataAccessEmail() {
		return dataAccessEmail;
	}

	public DataAccessEvents getDataAccessEvents() {
		return dataAccessEvents;
	}

	public DataAccessNews getDataAccessNews() {
		return dataAccessNews;
	}

	public DataAccessOther getDataAccessOther() {
		return dataAccessOther;
	}

	public DataAccessQuestions getDataAccessQuestions() {
		return dataAccessQuestions;
	}

	public DataAccessTransactions getDataAccessTransactions() {
		return dataAccessTransactions;
	}

	public DataAccessUsers getDataAccessUsers() {
		return dataAccessUsers;
	}

	public DataAccessStats getDataAccessStats() {
		return dataAccessStats;
	}

}
