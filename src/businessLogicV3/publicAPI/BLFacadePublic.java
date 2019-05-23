package businessLogicV3.publicAPI;

import businessLogicV3.privateAPI.BLFacadePrivate;
import businessLogicV3.publicAPI.bets.*;
import businessLogicV3.publicAPI.email.*;
import businessLogicV3.publicAPI.events.*;
import businessLogicV3.publicAPI.news.*;
import businessLogicV3.publicAPI.other.*;
import businessLogicV3.publicAPI.questions.*;
import businessLogicV3.publicAPI.transactions.*;
import businessLogicV3.publicAPI.users.*;

public class BLFacadePublic {
	public BLFacadePublicBets bets;
	public BLFacadePublicEmail email;
	public BLFacadePublicEvents events;
	public BLFacadePublicNews news;
	public BLFacadePublicOther others;
	public BLFacadePublicTransactions transactions;
	public BLFacadePublicUsers users;
	public BLFacadePublicQuestions questions;
	public BLFacadePrivate privateAPI = new BLFacadePrivate();

	public BLFacadePublic() {
		this.privateAPI = new BLFacadePrivate();
		this.bets = new BLFacadePublicBetsImplementation(privateAPI);
		this.email = new BLFacadePublicEmailImplementation(privateAPI);
		this.events = new BLFacadePublicEventsImplementation(privateAPI);
		this.news = new BLFacadePublicNewsImplementation(privateAPI);
		this.others = new BLFacadePublicOtherImplementation(privateAPI);
		this.transactions = new BLFacadePublicTransactionsImplementation(privateAPI);
		this.users = new BLFacadePublicUsersImplementation(privateAPI);
		this.questions = new BLFacadePublicQuestionsImplementation(privateAPI);
	}

	public BLFacadePublicBets getBets() {
		return bets;
	}

	public BLFacadePublicEmail getEmail() {
		return email;
	}

	public BLFacadePublicEvents getEvents(){
		return events;
	}

	public BLFacadePublicNews getNews() {
		return news;
	}

	public BLFacadePublicOther getOthers() {
		return others;
	}

	public BLFacadePublicTransactions getTransactions() {
		return transactions;
	}

	public BLFacadePublicUsers getUsers() {
		return users;
	}

	public BLFacadePublicQuestions getQuestions() {
		return questions;
	}

	public BLFacadePrivate getPrivateAPI(String username, String token) {
		if (privateAPI.getUsers().verifyToken(username, token)) {
			return privateAPI;
		}else {
			return null;
		}
	}
	
	// ------remove at the end of the project -----
	public BLFacadePrivate getPrivateAPI() {
		return privateAPI;
	}
	
	

}
