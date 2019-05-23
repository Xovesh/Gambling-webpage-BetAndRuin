package domainV2.bet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import domainV2.User;
import domainV2.question.Question;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BetSuperClass {
	@Id
	@GeneratedValue
	protected int id;
	@Id
	protected String username;
	protected long betAmount;
	protected String prediction;
	protected User user;
	protected boolean isWon;
	protected float payoff;
	@OneToOne(fetch = FetchType.EAGER)
	protected Question question;
	protected long finalPay;

	public BetSuperClass(Question questionAssigned, long betAmount, String prediction, User user) {
		question = questionAssigned;
		this.betAmount = betAmount;
		this.prediction = prediction;
		this.user = user;
		username = user.getUsername();
		question.addBetAmount(betAmount);
		payoff = question.getPayoff(prediction);
	}

	public void update(String result) {
		System.out.print("\tPrediction was: " + ((prediction == null) ? "null" : prediction) + " Bet amount: "
				+ betAmount + " Payoff: " + question.getPayoff(prediction));
		if (result.equals(prediction)) {
			isWon = true;
			question.removeProfit(betReturn());
			question.addWin();
			finalPay = betReturn();
			// add money to user
			// user.addWalletFunds(betReturn());
			System.out.print("---User won " + betReturn());
		}
		System.out.print("\n");
	}

	public long betReturn() {
		return (long) (betAmount * question.getPayoff(prediction));
	}

	public long getFinalPay() {
		return finalPay;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public long getBetAmount() {
		return betAmount;
	}

	public String getPrediction() {
		return prediction;
	}

	public User getUser() {
		return user;
	}

	public boolean isWon() {
		return isWon;
	}

	public float getPayoff() {
		return payoff;
	}

	public Question getQuestion() {
		return question;
	}

}
