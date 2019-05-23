package businessLogicV3.privateAPI.users;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import businessLogicV3.privateAPI.BLFacadePrivate;
import dataAccessV3.DataAccess;
import domainV2.User;

public class BLFacadePrivateUsersImplementation implements BLFacadePrivateUsers {
	private Hashtable<String, User> activeUsers; // username User
	private Hashtable<String, String> usersTokens; // username token
	private Queue<User> lastConnectedUsers;
	private DataAccess dataManager;
	private BLFacadePrivate privateAPI;

	public BLFacadePrivateUsersImplementation(DataAccess dataManager, BLFacadePrivate privateAPI) {
		this.activeUsers = new Hashtable<String, User>();
		this.usersTokens = new Hashtable<String, String>();
		this.lastConnectedUsers = new LinkedList<User>();
		this.dataManager = dataManager;
		this.privateAPI = privateAPI;
	}

	/**
	 * This method returns a hashtable with the online users
	 * 
	 * @return hashtable with the online users
	 */
	@Override
	public Hashtable<String, User> getActiveUsers() {
		return activeUsers;
	}

	/**
	 * This method return the tokens of all users
	 * 
	 * @return hashtable with the tokens of each user
	 */
	@Override
	public Hashtable<String, String> getUsersTokens() {
		return usersTokens;
	}

	/**
	 * This method verifies the credentials of an user
	 * 
	 * @param user     which is going to be verified
	 * @param password of the user
	 * @return token that is going that is going to be used to verify the user
	 */
	@Override
	public String verifyAuthentication(User user, String password) {
		String token = "";
		if (user.getUserRank() == 0) {
			return "";
		}
		if (user.getPassword().equals(privateAPI.getOthers().encrypt(password))) {
			token = privateAPI.getOthers().createToken(10);
			this.activeUsers.put(user.getUsername(), user);
			this.usersTokens.put(user.getUsername(), privateAPI.getOthers().encrypt(token));
			updateLastLogin(user.getUsername());
			if (this.lastConnectedUsers.size() == 200) {
				this.lastConnectedUsers.remove();
				this.lastConnectedUsers.add(user);
			} else {
				this.lastConnectedUsers.add(user);
			}
			return token;
		} else {
			return "";
		}
	}

	/**
	 * This method verifies the token to allow access
	 * 
	 * @param username of the user
	 * @param token    the one that use the user to verify himself
	 * @return true if the user use that token
	 */
	@Override
	public boolean verifyToken(String username, String token) {
		if (this.usersTokens.containsKey(username)) {
			if (this.usersTokens.get(username).equals(privateAPI.getOthers().encrypt(token))) {
				if (this.activeUsers.get(username).getUserRank() == 0) {
					logOut(username);
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * This method updates the last login of a given username
	 * 
	 * @param username of the member
	 * @return boolean true if updated successfully
	 */

	@Override
	public boolean updateLastLogin(String username) {
		boolean error = dataManager.getDataAccessUsers().updateLastLogin(username);
		return error;
	}

	/**
	 * This method retrieves a ACTIVE user given his username
	 * 
	 * @param username of the member
	 * @return user object
	 */
	@Override
	public User retrieveActiveUser(String username) {
		return this.activeUsers.get(username);
	}

	/**
	 * This method retrieves a user given his username
	 * 
	 * @param username of the member
	 * @return user object
	 */
	@Override
	public User retrieveUser(String username) {

		User res = dataManager.getDataAccessUsers().getUserByUsername(username);

		return res;
	}

	/**
	 * This method updates a active user
	 * 
	 * @param username of the user to be updated
	 * @return true if the user has been updated successfully
	 */
	@Override
	public boolean updateUser(String username) {
		if (this.activeUsers.containsKey(username)) {
			this.activeUsers.replace(username, retrieveUser(username));
			return true;
		}
		return false;
	}

	/**
	 * This method logOut a username from the server
	 * 
	 * @param username of the user that is going to be log out
	 * @return true if logout successfully
	 */
	@Override
	public boolean logOut(String username) {
		if (activeUsers.containsKey(username)) {
			if (usersTokens.containsKey(username)) {
				this.activeUsers.remove(username);
				this.usersTokens.remove(username);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/**
	 * This method updates the user rank
	 * 
	 * @param username which user rank is going to be changed
	 * @param newRank  of the user
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist 2
	 *         - rank is the same -1 - Error in the database
	 */
	@Override
	public int updateUserRank(String username, int newRank) {

		int error = dataManager.getDataAccessUsers().updateUserRank(username, newRank);

		updateUser(username);
		return error;

	}

	/**
	 * This method returns the rank of a user given the username and the token
	 * 
	 * @param username of the user
	 * @param token    that identifies the user
	 * @return the user rank or -1 if something is wrong
	 */
	@Override
	public int checkUserRank(String username, String token) {
		if (verifyToken(username, token)) {
			return this.activeUsers.get(username).getUserRank();
		} else {
			return -1;
		}

	}

	/**
	 * THIS FUNCTION ADDS FUNDS TO THE WALLET n - actual funds k - funds to add
	 * POSITIVE NUMBER RETURN OF THIS FUNCTION n+k
	 * 
	 * @param username which funds are going to be added
	 * @param funds    that are going to be added POSITIVE NUMBER
	 * @return true if increased successfully
	 */
	@Override
	public boolean increaseWalletFunds(String username, long funds, String message) {

		boolean error = dataManager.getDataAccessUsers().increaseWalletFunds(username, funds, message);

		if (error) {
			updateUser(username);
		}
		return error;

	}

	/**
	 * * THIS FUNCTION SUBSTRACT FUNDS TO THE WALLET n - actual funds k - funds to
	 * substract POSITIVE NUMBER RETURN OF THIS FUNCTION n-k
	 * 
	 * @param username which funds are going to be added
	 * @param funds    that are going to be substracted POSITIVE NUMBER
	 * @return true if substracted successfully
	 */
	@Override
	public boolean removeWalletFunds(String username, long funds, String message) {

		boolean error = dataManager.getDataAccessUsers().removeWalletFunds(username, funds, message);

		if (error) {
			updateUser(username);
		}
		return error;
	}

	/**
	 * * THIS FUNCTION UPDATES FUNDS TO THE WALLET n - actual funds s - new funds
	 * POSITIVE NUMBER RETURN OF THIS FUNCTION s
	 * 
	 * @param username which funds are going to be added
	 * @param funds    that are going to be changed POSITIVE NUMBER
	 * @return true if changed successfully
	 */
	@Override
	public boolean updateWalletFunds(String username, long funds, String message) {

		boolean error = dataManager.getDataAccessUsers().updateWalletFunds(username, funds);

		if (error) {
			updateUser(username);
		}
		return error;

	}

	/**
	 * This method retrives all users from the database
	 * 
	 * @return list with all users in the database
	 */
	@Override
	public List<User> retrieveAllUsers() {
		List<User> result;

		result = dataManager.getDataAccessUsers().retrieveAllUsers();

		return result;
	}

	/**
	 * This function returns the user that match with a given id
	 * 
	 * @param id of the user
	 * @return User that match the Id
	 */
	@Override
	public User searchUserById(Integer id) {

		User res = dataManager.getDataAccessUsers().searchUserById(id.intValue());

		return res;
	}

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
	@Override
	public List<Integer> updateUserData(String username, String sex, String phone, String email, String name,
			String surname, String country, String city, String address, List<String> favouriteSports,
			List<String> favouriteTeams) {

		List<Integer> error = new LinkedList<Integer>();

		User userData = retrieveUser(username);

		if (!sex.equals(userData.getSex())) {
			error.add(dataManager.getDataAccessUsers().updateSex(username, sex));
		} else {
			error.add(-5);
		}

		if (!phone.equals(userData.getPhoneNumber())) {
			error.add(dataManager.getDataAccessUsers().updatePhoneNumber(username, phone));
		} else {
			error.add(-5);
		}

		if (!email.equals(userData.getEmail())) {
			error.add(dataManager.getDataAccessUsers().updateEmail(username, email));
		} else {
			error.add(-5);
		}

		if (!name.equals(userData.getName())) {
			error.add(dataManager.getDataAccessUsers().updateName(username, name));
		} else {
			error.add(-5);
		}

		if (!surname.equals(userData.getSurname())) {
			error.add(dataManager.getDataAccessUsers().updateSurname(username, surname));
		} else {
			error.add(-5);
		}

		if (!country.equals(userData.getCountry())) {
			error.add(dataManager.getDataAccessUsers().updateCountry(username, country));
		} else {
			error.add(-5);
		}

		if (!city.equals(userData.getCity())) {
			error.add(dataManager.getDataAccessUsers().updateCity(username, city));
		} else {
			error.add(-5);
		}

		if (!address.equals(userData.getAddress())) {
			error.add(dataManager.getDataAccessUsers().updateAddress(username, address));
		} else {
			error.add(-5);
		}

		if (favouriteSports != null) {
			boolean error1 = dataManager.getDataAccessUsers().updateFavouriteSports(username, favouriteSports);
			if (error1) {
				error.add(0);
			} else {
				error.add(1);
			}
		} else {
			error.add(-5);
		}

		if (favouriteTeams != null) {
			boolean error2 = dataManager.getDataAccessUsers().updateFavouriteTeams(username, favouriteTeams);
			if (error2) {
				error.add(0);
			} else {
				error.add(1);
			}
		} else {
			error.add(-5);
		}

		updateUser(username);
		return error;

	}

	/**
	 * This method removes a user from the database
	 * 
	 * @param username to be removed
	 * @return true if LogOut successfully
	 */
	@Override
	public boolean removeUser(String username) {

		boolean error = dataManager.getDataAccessUsers().removeUser(username);

		if (error) {
			error = logOut(username);
		}
		return error;

	}

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

	@Override
	public int updatePassword(String username, String password, String password2) {
		if (!password.equals(password2)) {
			return -2;
		}

		int error = dataManager.getDataAccessUsers().updatePassword(username, privateAPI.getOthers().encrypt(password));

		updateUser(username);
		return error;

	}

	/**
	 * This method retrieves all the banned users
	 * 
	 * @return list with all the banned users
	 */
	@Override
	public List<User> retrieveAllBannedUsers() {
		List<User> res = null;

		res = dataManager.getDataAccessUsers().retrieveAllBannedUsers();

		return res;
	}

	/**
	 * This method retrieves the last connected users
	 * 
	 * @return Queue with the latest connected users
	 */
	@Override
	public Queue<User> retrieveLastConnectedUsers() {
		return this.lastConnectedUsers;
	}

	/**
	 * This function returns a list with all the connected users
	 * 
	 * @return list with the connected users
	 */
	@Override
	public List<User> retrieveConnectedUsers() {
		return new LinkedList<User>(this.activeUsers.values());
	}

	/**
	 * This method returns the next id that proceed to a given id
	 * 
	 * @param id   of the user before
	 * @param type BANNED OR ALL
	 * @return id of the next user
	 */
	@Override
	public int idAfterIdUsers(int id, String type) {
		int res = -1;

		res = dataManager.getDataAccessUsers().idAfterIdUsers(id, type);

		return res;
	}

	/**
	 * This method returns the quantity of users in the database
	 * 
	 * @return number of users in the database
	 */
	@Override
	public int usersQuantity() {
		int res = 0;

		res = dataManager.getDataAccessUsers().usersQuantity();

		return res;
	}

	/**
	 * This method retrieves all users given a id "from" to start from
	 * 
	 * @param from   id from we want to start to add users to the list
	 * @param amount of users to be in the list
	 * @return List of amount users that start from from
	 */
	@Override
	public List<User> retrieveAllUsersFrom(int from, int amount) {
		List<User> res = null;

		res = dataManager.getDataAccessUsers().retrieveAllUsersFrom(from, amount);

		return res;
	}

	/**
	 * This method retrieves all banned users given a id "from" to start from
	 * 
	 * @param from   id from we want to start to add users to the list
	 * @param amount of users to be in the list
	 * @return List of amount users that start from from
	 */
	@Override
	public List<User> retrieveAllBannedUsersFrom(int from, int amount) {
		List<User> res = null;

		res = dataManager.getDataAccessUsers().retrieveAllBannedUsersFrom(from, amount);

		return res;
	}

	/**
	 * This function returns the id of the first user in the database
	 * 
	 * @param type BANNED OR ALL
	 * @return id of the first User
	 */
	@Override
	public int firstUserId(String type) {
		int res = 0;

		res = dataManager.getDataAccessUsers().firstUserId(type);

		return res;
	}

	/**
	 * This function returns the id of the last user registered
	 * 
	 * @param type BANNED OR ALL
	 * @return the if of the last user Registered
	 */
	@Override
	public int lastUserId(String type) {
		int res = 0;

		res = dataManager.getDataAccessUsers().lastUserId(type);

		return res;
	}

	/**
	 * This method retrieves all users until a given id
	 * 
	 * @param untilTo until which id we want to retrieve
	 * @param type    BANNED OR ALL
	 * @return List of users until untilTo
	 */
	@Override
	public List<User> untilGivenIdUsers(int untilTo, String type) {
		List<User> res = null;

		res = dataManager.getDataAccessUsers().untilGivenIdUsers(untilTo, type);

		return res;
	}

	/**
	 * This function returns n ids before the given id
	 * 
	 * @param id     of the user
	 * @param amount of users
	 * @return id of n before
	 */
	@Override
	public int nIdsBeforeUsers(int id, int amount, String type) {
		int res = 0;
		res = dataManager.getDataAccessUsers().nIdsBeforeUsers(id, amount, type);
		return res;
	}
}
