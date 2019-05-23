package businessLogicV3.publicAPI.users;

import java.util.List;
import domainV2.User;

public interface BLFacadePublicUsers {
	/**
	 * This method authenticates a user if the password and username match in the
	 * database
	 * 
	 * @param username
	 *            of the member
	 * @param password
	 *            of the member
	 * @return String which is going to be used to make verifications instead of the
	 *         password
	 */

	public String authentication(String username, String password);

	/**
	 * This method registers a new user in the database
	 * 
	 * @param username
	 *            of the member
	 * @param password
	 *            of the member
	 * @param password2 repetion of the password
	 * @param year
	 *            when the person has bore
	 * @param month
	 *            when the person has bore
	 * @param day
	 *            when the person has bore
	 * @param sex
	 *            of the person
	 * @param phoneNumber
	 *            of the member
	 * @param email
	 *            of the member
	 * @param name
	 *            of the member
	 * @param surname
	 *            of the member
	 * @param country
	 *            of the member
	 * @param city
	 *            of the member
	 * @param address
	 *            of the member
	 * @param ip_Address
	 *            of the member
	 * @return error code depending of what happened 0-successfully registered
	 *         1-username already in use 2-email already in use 3-username and
	 *         email already in use -1-Error in the database -2 - username not
	 *         allowed -5 - Password does not match -6 - UnderAge
	 */
	public int registration(String username, String password, String password2, int year, int month, int day,
			String sex, String phoneNumber, String email, String name, String surname, String country, String city,
			String address, String ip_Address);
	
	/**
	 * This method updates the password of a given username
	 * 
	 * @param username
	 *            which password is going to be changed
	 * @param token
	 *            that is going to be used to verify the username
	 * @param password new password to change
	 * @param password2 repetea the new password
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - password is the same -1- Error in the database -2 - User and
	 *         token does not match -3 - The password does not match with the
	 *         second
	 * Please see updateUserData
	 */
	public int updatePassword(String username, String token, String password, String password2);
	
	/**
	 * This method removes a user from the database
	 * 
	 * @param username
	 *            to be removed
	 * @param token
	 *            that is going to be used to verify the username
	 * @return true if user existed and has been removed
	 */
	public boolean removeUser(String username, String token);

	/**
	 * This method logOut username from the server
	 * 
	 * @param username
	 *            that is going to log out
	 * @param token
	 *            of the username to verify the username
	 * @return true if successfully logOut
	 */
	public boolean logOut(String username, String token);

	/**
	 * This method updates all the parameters of a user
	 * 
	 * @param username
	 *            which values are going to be changed
	 * @param token
	 *            that identifies the username
	 * @param sex
	 *            that is going to be changed
	 * @param phone
	 *            that is going to be changed
	 * @param email
	 *            that is going to be changed
	 * @param name
	 *            that is going to be changed
	 * @param surname
	 *            that is going to be changed
	 * @param country
	 *            that is going to be changed
	 * @param city
	 *            that is going to be changed
	 * @param address
	 *            that is going to be changed
	 * @param favouriteSports
	 *            that are going to be changed
	 * @param favouriteTeams
	 *            that are going to be changed
	 * @return List of integers with an error for all of them; -5 if nothing has
	 *         changed
	 */
	public List<Integer> updateUserData(String username, String token, String sex, String phone, String email,
			String name, String surname, String country, String city, String address, List<String> favouriteSports,
			List<String> favouriteTeams);

	/**
	 * @param username
	 *            of the user which data is going to be retrieved
	 * @param token
	 *            that identifies that user
	 * @return User object with his information
	 */
	public User retrieveData(String username, String token);

	/**
	 * This method checks if the email exists in the database
	 * 
	 * @param email
	 *            to be checked
	 * @return true if the email exists,
	 */
	public boolean emailExist(String email);

	/**
	 * This method checks if the username exists in the database
	 * 
	 * @param username
	 *            to be checked
	 * @return true if the username exists
	 */
	public boolean userExist(String username);

	/**
	 * This method returns the rank of the user given
	 * 
	 * @param username
	 *            which rank is going to be checked
	 * @param token
	 *            that identifies the user
	 * @return the user rank or -1 if something is went wrong
	 */
	public int checkUserRank(String username, String token);

	/**
	 * This method checks if an user is actives
	 * 
	 * @param username
	 *            that is going to be checked
	 * @param token
	 *            that identifies the user
	 * @return true if is online
	 */
	public boolean activeUser(String username, String token);

	/**
	 * This method increase the funds of a username
	 * 
	 * @param username
	 *            which funds are going to be increase
	 * @param token
	 *            that identifies the user
	 * @param amount
	 *            that is going to be increase
	 * @param message message to add to the transaction
	 * @return true if changed successfully
	 */
	public boolean increaseFunds(String username, String token, long amount, String message);

	/**
	 * This method decrease the funds of a username
	 * 
	 * @param username
	 *            which funds are going to be increase
	 * @param token
	 *            that identifies the user
	 * @param amount
	 *            that is going to be decreased
	 * @param message message to add to the transaction
	 * @return true if changed successfully
	 */
	public boolean decreaseFunds(String username, String token, long amount, String message);
}
