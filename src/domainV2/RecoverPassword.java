package domainV2;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class RecoverPassword {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String username;
	private String email;
	private String token;
	private Calendar expiration;
	private boolean used;
	private String recoveryPasswordLink;
	
	public RecoverPassword(String username, String email, String token, String recoveryPasswordLink) {
		this.username = username;
		this.email = email;
		this.token = token;
		this.expiration = new GregorianCalendar();
		this.used = false;
		this.recoveryPasswordLink = recoveryPasswordLink;
	}

	public int getId() {
		return id;
	}

	public String getRecoveryPasswordLink() {
		return recoveryPasswordLink;
	}

	public String getUsername() {
		return username;
	}


	public String getEmail() {
		return email;
	}

	public String getToken() {
		return token;
	}

	public Calendar getExpiration() {
		return expiration;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
}
