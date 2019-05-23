package domainV2.question;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import domainV2.bet.BetSuperClass;
import domainV2.util.Translation;
import domainV2.util.validation.text.GenericTextValidatorNumericSequence;

@Entity
public class Question {
	// Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ID;
	// Base functionality
	private String result;
	private QuestionMode mode;
	private long parentEventId;
	private long minimumBetAmount = 100;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<BetSuperClass> bets = new LinkedList<BetSuperClass>();
	// Valid Set Functionality
	private HashMap<String, Float> validSet = new HashMap<String, Float>();
	@SuppressWarnings("unused")
	private float defaultChance;
	// Numeric Functionality
	private static GenericTextValidatorNumericSequence numEval = new GenericTextValidatorNumericSequence();
	@SuppressWarnings("unused")
	private float avgScore, maxScoreRange;
	@SuppressWarnings("unused")
	private int answerLength = 0;
	private int[] target = { 3, 2 };
	private int expectedMax = 7;
	private float maxPayout = 20;

	// Translation
	@Embedded
	public Translation translation;
	// Stats
	private long profit = 0;
	private long betAmount = 0;
	private int userWins = 0;

	// Observable Interface

	public void notifyObservers() {
		System.out.println("Notification sent");
		for (BetSuperClass b : bets)
			b.update(result);
	}

	public void addObserver(BetSuperClass b) {
		bets.add(b);
	}

	public void removeObserver(BetSuperClass b) {
		bets.remove(b);
	}

	// Constructor
	public Question(Translation questionText, long eventId, QuestionMode mode, int[] target, int maxExpectedScore) {
		translation = questionText;
		this.mode = mode;
		this.target = target;
		expectedMax = maxExpectedScore;
		parentEventId = eventId;
	}

	// Base functionality methods
	public void answer(String answer) throws InvalidAnswerValueException {
		switch (mode) {
		case UseValidSet:
			if (validSet.containsKey(answer)) {
				result = answer;
			} else
				throw new InvalidAnswerValueException();
			break;
		case UseNumericValue:
			if (numEval.validate(answer)) {
				result = answer;
			} else
				throw new InvalidAnswerValueException();
		}
	}

	public String getAnswer() {
		return result;
	}

	public float getAvgScore() {
		return avgScore;
	}

	public float getMaxScoreRange() {
		return expectedMax;
	}

	public QuestionMode getQuestionMode() {
		return mode;
	}

	public boolean isAnswered() {
		return result != null;
	}

	public float getPayoff(String prediction) {
		switch (mode) {
		case UseValidSet:
			return validSet.get(prediction);
		case UseNumericValue:
			return computeDistancetoNormal(prediction);
		default:
			return 1f;
		}
	}

	public void setAnswerLength(int n) {
		answerLength = n;
	}

	public int getAnswerLength() {
		return target.length;
	}

	public long getMinimumBetAmount() {
		return minimumBetAmount;
	}

	public void setMinimumBetAmount(long minimumBetAmount) {
		this.minimumBetAmount = minimumBetAmount;
	}

	public long getId() {
		return ID;
	}

	public List<BetSuperClass> getBets() {
		return bets;
	}

	public long getParentEventId() {
		return parentEventId;
	}

	public void setParentEventId(long parentEventId) {
		this.parentEventId = parentEventId;
	}

	public int[] getExpectedResult() {
		return target;
	}

	// Statistics related methods
	// Data recollection
	public void addBetAmount(long val) {
		betAmount += val;
		profit = betAmount;
	}

	public void removeProfit(long val) {
		profit -= val;
	}

	public void addWin() {
		userWins += 1;
	}

	// Statistics retrieval
	public long getBetAmount() {
		return betAmount;
	}

	public long getAverageBetAmount() {
		return betAmount / bets.size();
	}

	public long getProfit() {
		return profit;
	}

	public long getAverageProfit() {
		return profit / bets.size();
	}

	public double getUserWinrate() {
		return userWins / (float) bets.size();
	}

	public String getStatSummary() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);
		return "Total bet amount: " + getBetAmount() + " Average bet amount: " + getAverageBetAmount()
				+ "\nObtained profit: " + getProfit() + " Average profit per bet: " + getAverageProfit()
				+ "\nUser Winrate: " + df.format(getUserWinrate() * 100) + "%\n";
	}

	// Valid set of answers related methods
	public void addToSet(String item, float payout) {
		validSet.put(item, payout);
	}

	public void removeFromSet(String item) {
		validSet.remove(item);
	}

	public List<String> getValidSet() {
		return new LinkedList<String>(validSet.keySet());
	}

	// Numeric Score Utility Methods

	public float computeDistancetoNormal(String prediction) {
		@SuppressWarnings("unused")
		float minReturn = 0.01f, maxReturn = 20f;
		String[] teamScores = prediction.split(",");
		int[] values = new int[teamScores.length];
		for (int i = 0; i < teamScores.length; i++) {
			values[i] = (Integer.parseInt(teamScores[i]));
		}
		return getPayout(values, 100);
	}

	float getPayout(int[] answer, long betAmount) {
		float finalPayout = 1.2f;
		float localPayout = maxPayout / target.length;
		for (int i = 0; i < target.length; i++) {
			float distance = Math.abs(target[i] - answer[i]);
			float maxLocalDistance = Math.max(Math.abs(expectedMax - target[i]), 0 - target[i]);
			finalPayout += getPayoutFromDistance(distance, localPayout, maxLocalDistance);
		}
		// float chances = (1 / finalPayout) * 100;
		float payoutQuantity = (float) (((0.9 * betAmount) * (finalPayout - 1)) + betAmount);
		return payoutQuantity / betAmount;

	}

	float getPayoutFromDistance(float distance, float localPayout, float maxDistance) {
		float result = (float) ((Math.sinh(((distance / maxDistance) * 3) - 1.5) + 2)) * (distance / maxDistance) * 4;
		// float result = (float) Math.cosh(distance);
		return (result > localPayout) ? localPayout : result;
	}

	@SuppressWarnings("unused")
	private double convertBetweenConversion(double oldBottom, double oldTop, double newBottom, double newTop,
			double value) {
		return (value - oldBottom) / (oldTop - oldBottom) * (newTop - newBottom) + newBottom;
	}
}
