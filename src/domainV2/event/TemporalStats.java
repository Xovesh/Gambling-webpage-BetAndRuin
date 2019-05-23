package domainV2.event;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TemporalStats {
	@Temporal(TemporalType.DATE)
	private Calendar date;
	private long bets = 0;
	private long coins = 0;

	public TemporalStats() {
		date = new GregorianCalendar();
	}
	
	public TemporalStats(Calendar date, long bets, long coins) {
		this.date = date;
		this.bets = bets;
		this.coins = coins;
	}

	public void addBet(long coinAmount) {
		bets++;
		coins += coinAmount;
	}

	public Calendar getDate() {
		return date;
	}

	public long getBets() {
		return bets;
	}

	public long getCoins() {
		return coins;
	}
}