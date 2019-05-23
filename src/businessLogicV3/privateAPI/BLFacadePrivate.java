package businessLogicV3.privateAPI;

import businessLogicV3.privateAPI.bets.BLFacadePrivateBets;
import businessLogicV3.privateAPI.bets.BLFacadePrivateBetsImplementation;
import businessLogicV3.privateAPI.email.BLFacadePrivateEmail;
import businessLogicV3.privateAPI.email.BLFacadePrivateEmailImplementation;
import businessLogicV3.privateAPI.events.BLFacadePrivateEvents;
import businessLogicV3.privateAPI.events.BLFacadePrivateEventsImplementation;
import businessLogicV3.privateAPI.eventstats.BLFacadePrivateEventStats;
import businessLogicV3.privateAPI.eventstats.BLFacadePrivateEventStatsImplementation;
import businessLogicV3.privateAPI.news.BLFacadePrivateNews;
import businessLogicV3.privateAPI.news.BLFacadePrivateNewsImplementation;
import businessLogicV3.privateAPI.other.BLFacadePrivateOthers;
import businessLogicV3.privateAPI.other.BLFacadePrivateOthersImplementation;
import businessLogicV3.privateAPI.questions.BLFacadePrivateQuestions;
import businessLogicV3.privateAPI.questions.BLFacadePrivateQuestionsImplementation;
import businessLogicV3.privateAPI.transactions.BLFacadePrivateTransactions;
import businessLogicV3.privateAPI.transactions.BLFacadePrivateTransactionsImplementation;
import businessLogicV3.privateAPI.users.BLFacadePrivateUsers;
import businessLogicV3.privateAPI.users.BLFacadePrivateUsersImplementation;
import dataAccessV3.DataAccess;

public class BLFacadePrivate {
	public DataAccess dataManager;
	public BLFacadePrivateBets bets;
	public BLFacadePrivateEmail email;
	public BLFacadePrivateEvents events;
	public BLFacadePrivateNews news;
	public BLFacadePrivateOthers others;
	public BLFacadePrivateTransactions transactions;
	public BLFacadePrivateUsers users;
	public BLFacadePrivateQuestions questions;
	public BLFacadePrivateEventStats stats;

	public BLFacadePrivate() {
		this.dataManager = new DataAccess(false);
		this.bets = new BLFacadePrivateBetsImplementation(dataManager, this);
		this.email = new BLFacadePrivateEmailImplementation(dataManager, this);
		this.events = new BLFacadePrivateEventsImplementation(dataManager, this);
		this.news = new BLFacadePrivateNewsImplementation(dataManager, this);
		this.others = new BLFacadePrivateOthersImplementation(dataManager, this);
		this.transactions = new BLFacadePrivateTransactionsImplementation(dataManager,this);
		this.users = new BLFacadePrivateUsersImplementation(dataManager, this);
		this.questions = new BLFacadePrivateQuestionsImplementation(dataManager, this);
		this.stats = new BLFacadePrivateEventStatsImplementation(dataManager, this);
	}

	public DataAccess getDataManager() {
		return dataManager;
	}

	public BLFacadePrivateBets getBets() {
		return bets;
	}

	public BLFacadePrivateEmail getEmail() {
		return email;
	}

	public BLFacadePrivateEvents getEvents() {
		return events;
	}

	public BLFacadePrivateNews getNews() {
		return news;
	}

	public BLFacadePrivateOthers getOthers() {
		return others;
	}

	public BLFacadePrivateTransactions getTransactions() {
		return transactions;
	}

	public BLFacadePrivateUsers getUsers() {
		return users;
	}

	public BLFacadePrivateQuestions getQuestions() {
		return questions;
	}

	public BLFacadePrivateEventStats getStats() {
		return stats;
	}
}
