package domainV2;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String username;
	private long amount;
	private String type;
	private Calendar date;
	private String message;

	public Transaction( String username, long amount, String type, String message) {
		this.username = username;
		this.amount = amount;
		this.type = type;
		this.date = new GregorianCalendar();
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public long getAmount() {
		return amount;
	}

	public String getType() {
		return type;
	}

	public Calendar getDate() {
		return date;
	}
	
	public String getMessage() {
		return message;
	}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", username=" + username + ", amount=" + amount + ", type=" + type + ", date="
				+ date + "]";
	}
	
	
	
	
}
