package dataAccessV3.users;

import java.util.List;

import domainV2.User;

public interface DataAccessUsers {
	/**
	 * Returns all the users in the database
	 * 
	 * @return List all the users in the database null if nothing found or error
	 */
	public List<User> retrieveAllUsers();

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
	 * This method returns all the banned users in the database
	 * 
	 * @return List all banned users null if nothing found or error
	 */
	public List<User> retrieveAllBannedUsers();

	/**
	 * This function returns the user that match with a given id
	 * 
	 * @param id of the user
	 * @return User that match the Id null if nothing found or error
	 */
	public User searchUserById(int id);

	/**
	 * This method registers a new user in the database
	 * 
	 * @param username    of the member
	 * @param password    of the member
	 * @param year        when the member bore
	 * @param month       when the member bore
	 * @param day         when the member bore
	 * @param sex         of the member
	 * @param phoneNumber of the member
	 * @param email       of the member
	 * @param name        of the member
	 * @param surname     of the member
	 * @param country     of the member
	 * @param city        of the member
	 * @param address     of the member
	 * @param ip_Address  of the member
	 * @return error code depending of what happened 0-successfully registered
	 *         1-username already in use 2-email already in use 3-username and
	 *         email already in use -1-Error in the database
	 *
	 * 
	 */
	public int storeNewUser(String username, String password, int year, int month, int day, String sex,
			String phoneNumber, String email, String name, String surname, String country, String city, String address,
			String ip_Address);

	/**
	 * This method checks if the email exists in the database
	 * 
	 * @param email to be checked
	 * @return boolean true if the email exists or an error happened
	 */
	public boolean emailExist(String email);

	/**
	 * This method authenticates a user if the password and username match in the
	 * database
	 * 
	 * @param username of the member
	 * @param password of the member
	 * @return boolean True if the username and password match in the database
	 */
	public boolean authentication(String username, String password);

	/**
	 * This method checks if the user exists in the database
	 * 
	 * @param username to be checked
	 * @return true if the user exists or an error happened
	 */
	public boolean userExist(String username);

	/**
	 * This method removes a user from the database
	 * 
	 * @param username to be removed
	 * @return true if user has been removed
	 * 
	 */
	public boolean removeUser(String username);

	/**
	 * This method retrieves a user object given his username
	 * 
	 * @param username of the member
	 * @return user object if successfully found
	 */
	public User getUserByUsername(String username);

	/**
	 * This method updates the last login of a given username
	 * 
	 * @param username of the member
	 * @return boolean true if updated successfully
	 */
	public boolean updateLastLogin(String username);

	/**
	 * This method updates the email of a given username
	 * 
	 * @param username which email is going to be changed
	 * @param newEmail to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - email already in use 3 - email is the same -1 - Error in the
	 *         database
	 */
	public int updateEmail(String username, String newEmail);

	/**
	 * This method updates the password of a given username
	 * 
	 * @param username    which password is going to be changed
	 * @param newPassword that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - password is the same -1 - Error in the database
	 */
	public int updatePassword(String username, String newPassword);

	/**
	 * This method updates the phone number of a given username
	 * 
	 * @param username which phone number is going to be changed
	 * @param newPhoneNumber that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - phoneNumber is the same -1 - Error in the database
	 */
	public int updatePhoneNumber(String username, String newPhoneNumber);

	/**
	 * This method updates the name of a given username
	 * 
	 * @param username which name is going to be changed
	 * @param newName  that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - name is the same -1 - Error in the database
	 */
	public int updateName(String username, String newName);

	/**
	 * This method updates the surname of a given username
	 * 
	 * @param username   which surname is going to be changed
	 * @param newSurname that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - surname is the same -1 - Error in the database
	 */
	public int updateSurname(String username, String newSurname);

	/**
	 * This method updates the country of a given username
	 * 
	 * @param username   which country is going to be changed
	 * @param newCountry that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - country is the same -1 - Error in the database
	 */
	public int updateCountry(String username, String newCountry);

	/**
	 * This method updates the city of a given username
	 * 
	 * @param username which city is going to be changed
	 * @param newCity  that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - city is the same -1 - Error in the database
	 */
	public int updateCity(String username, String newCity);

	/**
	 * This method updates the address of a given username
	 * 
	 * @param username   which address is going to be changed
	 * @param newAddress that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - address is the same
	 */
	public int updateAddress(String username, String newAddress);

	/**
	 * THIS FUNCTION ADDS FUNDS TO THE WALLET n - actual funds k - funds to add
	 * POSITIVE NUMBER RETURN OF THIS FUNCTION n+k
	 * 
	 * @param username which funds are going to be added
	 * @param newFunds    that are going to be added POSITIVE NUMBER
	 * @param message message to add to the transaction
	 * @return true if added successfully
	 */
	public boolean increaseWalletFunds(String username, long newFunds, String message);

	/**
	 * * THIS FUNCTION SUBSTRACT FUNDS TO THE WALLET n - actual funds k - funds to
	 * substract POSITIVE NUMBER RETURN OF THIS FUNCTION n-k
	 * 
	 * @param username which funds are going to be added
	 * @param newFunds    that are going to be substracted POSITIVE NUMBER
	 * @param message message to add to the transaction
	 * @return true if substracted successfully
	 */
	public boolean removeWalletFunds(String username, long newFunds, String message);

	/**
	 * * THIS FUNCTION UPDATES FUNDS TO THE WALLET n - actual funds s - new funds
	 * POSITIVE NUMBER RETURN OF THIS FUNCTION s
	 * 
	 * @param username which funds are going to be added
	 * @param newFunds    that are going to be changed POSITIVE NUMBER
	 * @return true if changed successfully
	 */
	public boolean updateWalletFunds(String username, long newFunds);

	/**
	 * This function updates the ipaddress of a given username
	 * 
	 * @param username     which ipAddress is going to be added
	 * @param newIpAddress that is going to be updated
	 * @return true if changed succesfully
	 */
	public boolean updateIpAddress(String username, String newIpAddress);

	/**
	 * This function updates the favourite sports a given username
	 * 
	 * @param username           which favourite sports is going to be update
	 * @param newFavouriteSports new List of favourite sports that is going to be
	 *                           replaced
	 * @return true if changed successfully
	 */
	public boolean updateFavouriteSports(String username, List<String> newFavouriteSports);

	/**
	 * This function updates the favorite teams given username
	 * 
	 * @param username          which favorite teams is going to be update
	 * @param newFavouriteTeams new List of favorite teams that is going to be
	 *                          replaced
	 * @return true if changed successfully
	 */
	public boolean updateFavouriteTeams(String username, List<String> newFavouriteTeams);

	/**
	 * This method updates the user rank
	 * 
	 * @param username which user rank is going to be changed
	 * @param newRank  of the user
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - rank is the same -1 - Error in the database
	 */
	public int updateUserRank(String username, int newRank);

	/**
	 * This method updates the user sex
	 * 
	 * @param username which user sex is going to be changed
	 * @param sex   of the user
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - sex is the same -1 - Error in the database
	 */
	public int updateSex(String username, String sex);

	/**
	 * This method returns the quantity of users in the database
	 * 
	 * @return number of users in the database
	 */
	public int usersQuantity();

	/**
	 * This method returns the next id that proceed to a given id
	 * 
	 * @param id   of the user before
	 * @param type BANNED OR ALL
	 * @return id of the next user
	 */
	public int idAfterIdUsers(int id, String type);

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
	 * This function returns n ids before the given id
	 * 
	 * @param id     of the user
	 * @param amount number of users to retrieve
	 * @param type   BANNED OR ALL
	 * @return id of n before
	 */
	public int nIdsBeforeUsers(int id, int amount, String type);

	/**
	 * This method retrieves all users until a given id
	 * 
	 * @param untilTo until which id we want to retrieve
	 * @param type    BANNED OR ALL
	 * @return List of users until untilTo
	 */
	public List<User> untilGivenIdUsers(int untilTo, String type);
}
