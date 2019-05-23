package dataAccessV3.email;

import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dataAccessV3.DataAccess;
import domainV2.RecoverPassword;


public class DataAccessEmailImplementation implements DataAccessEmail{
	private EntityManager db;
	
	public DataAccessEmailImplementation(EntityManager db,  DataAccess dataManager) {
		this.db = db;
	}
	
	/**
	 * This functions add a new request to reset the password
	 * 
	 * @param username
	 *            of the member
	 * @param email
	 *            of the member
	 * @param token
	 *            code to verify
	 * @return true if successfully added
	 */
	@Override
	public boolean passwordReset(String username, String email, String token, String recoveryLink) {
		try {
			TypedQuery<RecoverPassword> query = null;
			query = db.createQuery("SELECT us FROM RecoverPassword us WHERE us.username = ?1", RecoverPassword.class);
			query.setParameter(1, username);
			List<RecoverPassword> data = query.getResultList();
			if (!(data.size() == 0)) {
				RecoverPassword check = data.get(data.size() - 1);
				long diffInMillies = new GregorianCalendar().getTimeInMillis()
						- check.getExpiration().getTimeInMillis();
				if (diffInMillies <= 600000) {
					return false;
				}
			}
			db.getTransaction().begin();
			RecoverPassword passwordReset = new RecoverPassword(username, email, token, recoveryLink);
			db.persist(passwordReset);
			db.getTransaction().commit();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This function check if the token sent to the email equals to the stored one
	 * 
	 * @param username
	 *            that request the update
	 * @param token
	 *            to compare with the database
	 * @return true if the token equals to the token in the database
	 */
	@Override
	public boolean checkPasswordReset(String username, String token) {
		try {
			TypedQuery<RecoverPassword> query = null;
			query = db.createQuery("SELECT us FROM RecoverPassword us WHERE us.username = ?1", RecoverPassword.class);
			query.setParameter(1, username);
			List<RecoverPassword> data = query.getResultList();
			if (data.size() == 0) {
				return false;
			}
			RecoverPassword check = data.get(data.size() - 1);
			if (!check.isUsed()) {
				if (check.getToken().equals(token)) {
					long diffInMillies = new GregorianCalendar().getTimeInMillis()
							- check.getExpiration().getTimeInMillis();
					if (diffInMillies <= 600000) {
						db.getTransaction().begin();
						check.setUsed(true);
						db.getTransaction().commit();
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This method returns the user with the recoveryPasswordLink
	 * 
	 * @param passwordLink
	 *            that identifies the user
	 * @return the username that match the passwordLink
	 */
	@Override
	public String getUsernameVerifyLink(String passwordLink) {
		TypedQuery<RecoverPassword> query = null;
		query = db.createQuery("SELECT us FROM RecoverPassword us WHERE us.recoveryPasswordLink = ?1",
				RecoverPassword.class);
		query.setParameter(1, passwordLink);
		List<RecoverPassword> userList = query.getResultList();
		if (userList.size() == 0) {
			return "";
		} else {
			return userList.get(userList.size() - 1).getUsername();
		}
	}
	
	
	
}
