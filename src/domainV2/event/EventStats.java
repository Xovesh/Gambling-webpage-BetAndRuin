package domainV2.event;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import domainV2.question.Question;

@Entity
public class EventStats {
	@Id
	private long ID;
	@SuppressWarnings("unused")
	private int questionAmount;

	@Temporal(TemporalType.DATE)
	private Calendar date;
	private long totalBet = 0, totalProfit = 0;
	private float averageUserWinrate = 0;

	public EventStats(long id) {
		ID = id;
	}

	public long getTotalBet() {
		return totalBet;
	}

	public long getTotalProfit() {
		return totalProfit;
	}

	public float getUserWinrate() {
		return averageUserWinrate;
	}

	public void UpdateDataWithEvent(Event e) {
		date = new GregorianCalendar();
		questionAmount = e.getAllQuestions().size();
		long totalBetAmount = 0, betWins = 0;
		for (Question q : e.getAllQuestions()) {
			totalProfit += q.getProfit();
			totalBetAmount = q.getBets().size();
			betWins += q.getBets().size() * q.getUserWinrate();
		}
		if (totalBetAmount == 0) {
			averageUserWinrate = 0;
		} else {
			averageUserWinrate = betWins / totalBetAmount;
		}
	}

	public void UpdateDataWithBet(long betAmount) {
		totalBet += betAmount;
	}

}