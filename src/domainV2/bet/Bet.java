package domainV2.bet;

import javax.persistence.Entity;

import domainV2.User;
import domainV2.question.Question;

@Entity
public class Bet extends BetSuperClass {

	public Bet(Question questionAssigned, long betAmount, String prediction, User user) {
		super(questionAssigned, betAmount, prediction, user);
	}

}
