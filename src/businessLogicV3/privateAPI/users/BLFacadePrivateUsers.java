package businessLogicV3.privateAPI.users;

import java.util.Hashtable;
import java.util.List;
import java.util.Queue;

import domainV2.User;

public interface BLFacadePrivateUsers {
	/**
	 * This method returns a hashtable with the online users
	 * 
	 * @return hashtable with the online users
	 */
	public Hashtable<String, User> getActiveUsers();

	/**
	 * This method return the tokens of all users
	 * 
	 * @return hashtable with the tokens of each user
	 */
	public Hashtable<String, String> getUsersTokens();

	// ------------ Users -----------
	/**
	 * This method verifies the credentials of an user
	 * 
	 * @param user     which is going to be verified
	 * @param password of the user
	 * @return token that is going that is going to be used to verify the user
	 */
	public String verifyAuthentication(User user, String password);

	/**
	 * This method verifies the token to allow access
	 * 
	 * @param username of the user
	 * @param token    the one that use the user to verify himself
	 * @return true if the user use that token
	 */
	public boolean verifyToken(String username, String token);

	/**
	 * This method updates the last login of a given username
	 * 
	 * @param username of the member
	 * @return boolean true if updated successfully
	 */
	public boolean updateLastLogin(String username);

	/**
	 * This method retrieves a user given his username
	 * 
	 * @param username of the member
	 * @return user object
	 */
	public User retrieveUser(String username);

	/**
	 * This method updates a active user
	 * 
	 * @param username of the user to be updated
	 * @return true if the user has been updated successfully
	 */
	public boolean updateUser(String username);

	/**
	 * This method logOut a username from the server
	 * 
	 * @param username of the user that is going to be log out
	 * @return true if logout successfully
	 */
	public boolean logOut(String username);

	/**
	 * This method updates the user rank
	 * 
	 * @param username which user rank is going to be changed
	 * @param newRank  of the user
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist 2
	 *         - rank is the same -1 - Error in the database
	 */
	public int updateUserRank(String username, int newRank);

	/**
	 * This method returns the rank of a user given the username and the token
	 * 
	 * @param username of the user
	 * @param token    that identifies the user
	 * @return the user rank or -1 if something is wrong
	 */
	public int checkUserRank(String username, String token);

	/**
	 * THIS FUNCTION ADDS FUNDS TO THE WALLET n - actual funds k - funds to add
	 * POSITIVE NUMBER RETURN OF THIS FUNCTION n+k
	 * 
	 * @param username which funds are going to be added
	 * @param funds    that are going to be added POSITIVE NUMBER
	 * @param message  message of the transaction
	 * @return true if increased successfully
	 */
	public boolean increaseWalletFunds(String username, long funds, String message);

	/**
	 * * THIS FUNCTION SUBSTRACT FUNDS TO THE WALLET n - actual funds k - funds to
	 * substract POSITIVE NUMBER RETURN OF THIS FUNCTION n-k
	 * 
	 * @param username which funds are going to be added
	 * @param funds    that are going to be substracted POSITIVE NUMBER
	 * @param message  message of the transaction
	 * @return true if substracted successfully
	 */
	public boolean removeWalletFunds(String username, long funds, String message);

	/**
	 * * THIS FUNCTION UPDATES FUNDS TO THE WALLET n - actual funds s - new funds
	 * POSITIVE NUMBER RETURN OF THIS FUNCTION s
	 * 
	 * @param username which funds are going to be added
	 * @param funds    that are going to be changed POSITIVE NUMBER
	 * @param message  message of the transaction
	 * @return true if changed successfully
	 */
	public boolean updateWalletFunds(String username, long funds, String message);

	/**
	 * This method retrives all users from the database
	 * 
	 * @return list with all users in the database
	 */
	public List<User> retrieveAllUsers();

	/**
	 * This function returns the user that match with a given id
	 * 
	 * @param id of the user
	 * @return User that match the Id
	 */
	public User searchUserById(Integer id);

	/**
	 * This method retrieves a ACTIVE user given his username
	 * 
	 * @param username of the member
	 * @return user object
	 */
	public User retrieveActiveUser(String username);

	/**
	 * This method updates all the parameters of a user
	 * 
	 * @param username        which values are going to be changed
	 * @param sex             that is going to be changed
	 * @param phone           that is going to be changed
	 * @param email           that is going to be changed
	 * @param name            that is going to be changed
	 * @param surname         that is going to be changed
	 * @param country         that is going to be changed
	 * @param city            that is going to be changed
	 * @param address         that is going to be changed
	 * @param favouriteSports that are going to be changed
	 * @param favouriteTeams  that are going to be changed
	 * @return List of integers with an error for all of them; -5 if nothing has
	 *         changed
	 */
	public List<Integer> updateUserData(String username, String sex, String phone, String email, String name,
			String surname, String country, String city, String address, List<String> favouriteSports,
			List<String> favouriteTeams);

	/**
	 * This method removes a user from the database
	 * 
	 * @param username to be removed
	 * @return true if LogOut successfully
	 */
	public boolean removeUser(String username);

	/**
	 * This method updates a password of a user
	 * 
	 * @param username  which password is going to be updated
	 * @param password  to change
	 * @param password2 repeat the password from before
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist 2
	 *         - password is the same -1 - Error in the database -2 - The password
	 *         does not match with the repeated password
	 */
	public int updatePassword(String username, String password, String password2);

	/**
	 * This method retrieves all the banned users
	 * 
	 * @return list with all the banned users
	 */
	public List<User> retrieveAllBannedUsers();

	/**
	 * This method retrieves the last connected users
	 * 
	 * @return Queue with the latest connected users
	 */
	public Queue<User> retrieveLastConnectedUsers();

	/**
	 * This function returns a list with all the connected users
	 * 
	 * @return list with the connected users
	 */
	public List<User> retrieveConnectedUsers();

	/**
	 * This method returns the next id that proceed to a given id
	 * 
	 * @param id   of the user before
	 * @param type BANNED OR ALL
	 * @return id of the next user
	 */
	public int idAfterIdUsers(int id, String type);

	/**
	 * This method returns the quantity of users in the database
	 * 
	 * @return number of users in the database
	 */
	public int usersQuantity();

	/**
	 * This method retrieves all users given a id "from" to start from
	 * 
	 * @param from   id from we want to start to add users to the list
	 * @param amount of users to be in the list
	 * @return List of amount users that start from from
	 */
	public List<User> retrieveAllUsersFrom(int from, int amount);

	/**
	 * This method retrieves all banned users given a id "from" to start from
	 * 
	 * @param from   id from we want to start to add users to the list
	 * @param amount of users to be in the list
	 * @return List of amount users that start from from
	 */
	public List<User> retrieveAllBannedUsersFrom(int from, int amount);

	/**
	 * This function returns the id of the first user in the database
	 * 
	 * @param type BANNED OR ALL
	 * @return id of the first User
	 */
	public int firstUserId(String type);

	/**
	 * This function returns the id of the last user registered
	 * 
	 * @param type BANNED OR ALL
	 * @return the if of the last user Registered
	 */
	public int lastUserId(String type);

	/**
	 * This method retrieves all users until a given id
	 * 
	 * @param untilTo until which id we want to retrieve
	 * @param type    BANNED OR ALL
	 * @return List of users until untilTo
	 */
	public List<User> untilGivenIdUsers(int untilTo, String type);

	/**
	 * This function returns n ids before the given id
	 * 
	 * @param id     of the user
	 * @param amount of users
	 * @param type   BANNED OR ALL
	 * @return id of n before
	 */
	public int nIdsBeforeUsers(int id, int amount, String type);

}
