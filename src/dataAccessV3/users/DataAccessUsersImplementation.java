package dataAccessV3.users;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import dataAccessV3.DataAccess;
import domainV2.User;


public class DataAccessUsersImplementation implements DataAccessUsers{
	private EntityManager db;
	private DataAccess dataManager;
	
	public DataAccessUsersImplementation(EntityManager db,DataAccess dataManager) {
		this.db = db;
		this.dataManager = dataManager;
	}
	

	/**
	 * Returns all the users in the database
	 * 
	 * @return List all the users in the database null if nothing found or error
	 */
	@Override
	public List<User> retrieveAllUsers() {
		List<User> users = null;
		try {
			TypedQuery<User> q2 = db.createQuery("SELECT us FROM User us", User.class);
			users = q2.getResultList();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return users;
	}

	/**
	 * This method retrieves all users given a id "from" to start from
	 * 
	 * @param from
	 *            id from we want to start to add users to the list
	 * @param amount
	 *            of users to be in the list
	 * @return List of amount users that start from from
	 */
	@Override
	public List<User> retrieveAllUsersFrom(int from, int amount) {
		TypedQuery<User> query = db.createQuery("SELECT us FROM User us WHERE us.id >= ?1", User.class);
		query.setParameter(1, from);
		try {
			List<User> res = new LinkedList<User>();
			List<User> helper = query.getResultList();
			int j = 1;
			for (User users : helper) {
				res.add(users);
				if (j == amount) {
					break;
				}
				j++;
			}
			return res;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

	/**
	 * This method retrieves all banned users given a id "from" to start from
	 * 
	 * @param from
	 *            id from we want to start to add users to the list
	 * @param amount
	 *            of users to be in the list
	 * @return List of amount users that start from from
	 */
	@Override
	public List<User> retrieveAllBannedUsersFrom(int from, int amount) {
		TypedQuery<User> query = db.createQuery("SELECT us FROM User us WHERE us.id >= ?1 AND us.userRank = ?2",
				User.class);
		query.setParameter(1, from);
		query.setParameter(2, 0);
		try {
			List<User> res = new LinkedList<User>();
			List<User> helper = query.getResultList();
			int j = 1;
			for (User users : helper) {
				res.add(users);
				if (j == amount) {
					break;
				}
				j++;
			}
			return res;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

	/**
	 * This method returns all the banned users in the database
	 * 
	 * @return List all banned users null if nothing found or error
	 */
	@Override
	public List<User> retrieveAllBannedUsers() {
		List<User> users = null;
		try {
			TypedQuery<User> q2 = db.createQuery("SELECT us FROM User us WHERE us.userRank = ?1", User.class);
			q2.setParameter(1, 0);
			users = q2.getResultList();

		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return users;
	}

	/**
	 * This function returns the user that match with a given id
	 * 
	 * @param id
	 *            of the user
	 * @return User that match the Id null if nothing found or error
	 */
	@Override
	public User searchUserById(int id) {
		User users = null;
		try {
			TypedQuery<User> q2 = db.createQuery("SELECT us FROM User us WHERE us.id = ?1", User.class);
			q2.setParameter(1, id);
			users = q2.getSingleResult();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return users;
	}

	/**
	 * This method registers a new user in the database
	 * 
	 * @param username
	 *            of the member
	 * @param password
	 *            of the member
	 * @param year
	 *            when the member bore
	 * @param month
	 *            when the member bore
	 * @param day
	 *            when the member bore
	 * @param sex
	 *            of the member
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
	 *         email already in use -1-Error in the database
	 *
	 * 
	 */
	@Override
	public int storeNewUser(String username, String password, int year, int month, int day, String sex,
			String phoneNumber, String email, String name, String surname, String country, String city, String address,
			String ip_Address) {
		try {
			boolean userNotAvailable = userExist(username);
			boolean emailNotAvailable = emailExist(email);
			if (userNotAvailable && emailNotAvailable) {
				return 3;
			} else if (emailNotAvailable) {
				return 2;
			} else if (userNotAvailable) {
				return 1;
			}

			db.getTransaction().begin();
			User newUser = new User(username, password, dataManager.getDataAccessOther().newDate(year, month, day), phoneNumber, email, name, surname,
					country, city, address, ip_Address, sex);
			db.persist(newUser);
			db.getTransaction().commit();
			System.out.println(newUser + " has been saved");
			return 0;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}

	}

	/**
	 * This method checks if the email exists in the database
	 * 
	 * @param email
	 *            to be checked
	 * @return boolean true if the email exists or an error happened
	 */
	@Override
	public boolean emailExist(String email) {
		try {
			TypedQuery<User> q2 = db.createQuery("SELECT us FROM User us WHERE us.email = ?1", User.class);
			q2.setParameter(1, email);
			User users = null;
			try {
				users = q2.getSingleResult();
			} catch (Exception e) {

			}

			if (users == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return true;
		}
	}

	/**
	 * This method authenticates a user if the password and username match in the
	 * database
	 * 
	 * @param username
	 *            of the member
	 * @param password
	 *            of the member
	 * @return boolean True if the username and password match in the database
	 */
	@Override
	public boolean authentication(String username, String password) {
		try {
			TypedQuery<User> q2 = db.createQuery("SELECT us FROM User us WHERE us.username = ?1", User.class);
			q2.setParameter(1, username);
			User users = null;
			try {
				users = q2.getSingleResult();
			} catch (Exception e) {

			}
			if (users == null) {
				return false;
			} else if (users.getPassword().equals(password)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This method checks if the user exists in the database
	 * 
	 * @param username
	 *            to be checked
	 * @return true if the user exists or an error happened
	 */
	@Override
	public boolean userExist(String username) {
		try {
			TypedQuery<User> q2 = db.createQuery("SELECT us FROM User us WHERE us.username = ?1", User.class);
			q2.setParameter(1, username);
			User users = null;
			try {
				users = q2.getSingleResult();
			} catch (Exception e) {

			}
			if (users == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return true;
		}
	}

	/**
	 * This method removes a user from the database
	 * 
	 * @param username
	 *            to be removed
	 * @return true if user has been removed
	 * 
	 */
	@Override
	public boolean removeUser(String username) {
		try {
			if (userExist(username)) {
				db.getTransaction().begin();
				Query q4 = db.createQuery("DELETE FROM User us WHERE us.username = ?1", User.class);
				q4.setParameter(1, username);
				q4.executeUpdate();
				db.getTransaction().commit();
				System.out.println(username + " has been removed");
				return true;
			} else {
				System.out.println(username + " is not in the database");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This method retrieves a user object given his username
	 * 
	 * @param username
	 *            of the member
	 * @return user object if successfully found
	 */
	@Override
	public User getUserByUsername(String username) {
		User users = null;
		try {
			TypedQuery<User> q2 = db.createQuery("SELECT us FROM User us WHERE us.username = ?1", User.class);
			q2.setParameter(1, username);
			try {
				users = q2.getSingleResult();
			} catch (Exception e) {

			}
			if (users == null) {
				return null;
			} else {
				return users;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return users;
		}
	}

	/**
	 * This method updates the last login of a given username
	 * 
	 * @param username
	 *            of the member
	 * @return boolean true if updated successfully
	 */
	@Override
	public boolean updateLastLogin(String username) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return false;
			} else {
				db.getTransaction().begin();
				user.updateLastLogin();
				db.getTransaction().commit();
				System.out.println(username + " last login has been updated");
				return true;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This method updates the email of a given username
	 * 
	 * @param username
	 *            which email is going to be changed
	 * @param newEmail
	 *            to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - email already in use 3 - email is the same -1 - Error in the
	 *         database
	 */
	@Override
	public int updateEmail(String username, String newEmail) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return 1;
			} else {
				if (emailExist(newEmail)) {
					System.out.println("Email is already in use");
					return 2;
				} else {
					if (user.getEmail().equals(newEmail)) {
						System.out.println("The email cant be the same");
						return 3;
					} else {
						db.getTransaction().begin();
						user.setEmail(newEmail);
						db.getTransaction().commit();
						System.out.println(username + " email has been updated");
						return 0;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
	}

	/**
	 * This method updates the password of a given username
	 * 
	 * @param username
	 *            which password is going to be changed
	 * @param newPassword
	 *            that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - password is the same -1 - Error in the database
	 */
	@Override
	public int updatePassword(String username, String newPassword) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return 1;
			} else {
				if (user.getPassword().equals(newPassword)) {
					System.out.println("The password cant be the same");
					return 2;
				} else {
					db.getTransaction().begin();
					user.setPassword(newPassword);
					db.getTransaction().commit();
					System.out.println(username + " password has been updated");
					return 0;
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
	}

	/**
	 * This method updates the phone number of a given username
	 * 
	 * @param username
	 *            which phone number is going to be changed
	 * @param newPhoneNumber
	 *            that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - phoneNumber is the same -1 - Error in the database
	 */
	@Override
	public int updatePhoneNumber(String username, String newPhoneNumber) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return 1;
			} else {
				if (user.getPhoneNumber().equals(newPhoneNumber)) {
					System.out.println("The password cant be the same");
					return 2;
				} else {
					db.getTransaction().begin();
					user.setPhoneNumber(newPhoneNumber);
					db.getTransaction().commit();
					System.out.println(username + " phone number has been updated");
					return 0;
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
	}

	/**
	 * This method updates the name of a given username
	 * 
	 * @param username
	 *            which name is going to be changed
	 * @param newName
	 *            that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - name is the same -1 - Error in the database
	 */
	@Override
	public int updateName(String username, String newName) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return 1;
			} else {
				if (user.getName().equals(newName)) {
					System.out.println("The name cant be the same");
					return 2;
				} else {
					db.getTransaction().begin();
					user.setName(newName);
					db.getTransaction().commit();
					System.out.println(username + " name has been updated");
					return 0;
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
	}

	/**
	 * This method updates the surname of a given username
	 * 
	 * @param username
	 *            which surname is going to be changed
	 * @param newSurname
	 *            that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - surname is the same -1 - Error in the database
	 */
	@Override
	public int updateSurname(String username, String newSurname) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return 1;
			} else {
				if (user.getSurname().equals(newSurname)) {
					System.out.println("The surname cant be the same");
					return 2;
				} else {
					db.getTransaction().begin();
					user.setSurname(newSurname);
					db.getTransaction().commit();
					System.out.println(username + " surname has been updated");
					return 0;
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
	}

	/**
	 * This method updates the country of a given username
	 * 
	 * @param username
	 *            which country is going to be changed
	 * @param newCountry
	 *            that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - country is the same -1 - Error in the database
	 */
	@Override
	public int updateCountry(String username, String newCountry) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return 1;
			} else {
				if (user.getCountry().equals(newCountry)) {
					System.out.println("The country cant be the same");
					return 2;
				} else {
					db.getTransaction().begin();
					user.setCountry(newCountry);
					db.getTransaction().commit();
					System.out.println(username + " country has been updated");
					return 0;
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
	}

	/**
	 * This method updates the city of a given username
	 * 
	 * @param username
	 *            which city is going to be changed
	 * @param newCity
	 *            that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - city is the same -1 - Error in the database
	 */
	@Override
	public int updateCity(String username, String newCity) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return 1;
			} else {
				if (user.getCity().equals(newCity)) {
					System.out.println("The city cant be the same");
					return 2;
				} else {
					db.getTransaction().begin();
					user.setCity(newCity);
					db.getTransaction().commit();
					System.out.println(username + " city has been updated");
					return 0;
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}

	}

	/**
	 * This method updates the address of a given username
	 * 
	 * @param username
	 *            which address is going to be changed
	 * @param newAddress
	 *            that is going to be changed
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - address is the same
	 */
	@Override
	public int updateAddress(String username, String newAddress) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return 1;
			} else {
				if (user.getAddress().equals(newAddress)) {
					System.out.println("The address cant be the same");
					return 2;
				} else {
					db.getTransaction().begin();
					user.setAddress(newAddress);
					db.getTransaction().commit();
					System.out.println(username + " address has been updated");
					return 0;
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
	}

	/**
	 * THIS FUNCTION ADDS FUNDS TO THE WALLET n - actual funds k - funds to add
	 * POSITIVE NUMBER RETURN OF THIS FUNCTION n+k
	 * 
	 * @param username
	 *            which funds are going to be added
	 * @param newFunds
	 *            that are going to be added POSITIVE NUMBER
	 * @return true if added successfully
	 */
	@Override
	public boolean increaseWalletFunds(String username, long newFunds, String message) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return false;
			} else {
				db.getTransaction().begin();
				user.addWalletFunds(newFunds);
				db.getTransaction().commit();
				System.out.println("INCREASE WALLET:" + username);
				dataManager.getDataAccessTransactions().newTransaction(username, newFunds, "IN", message);
				System.out.println(username + " funds has been updated");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * * THIS FUNCTION SUBSTRACT FUNDS TO THE WALLET n - actual funds k - funds to
	 * substract POSITIVE NUMBER RETURN OF THIS FUNCTION n-k
	 * 
	 * @param username
	 *            which funds are going to be added
	 * @param newFunds
	 *            that are going to be substracted POSITIVE NUMBER
	 * @return true if substracted successfully
	 */
	@Override
	public boolean removeWalletFunds(String username, long newFunds, String message) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return false;
			} else {
				db.getTransaction().begin();
				boolean removeSuccessfully = user.removeWalletFunds(newFunds);
				db.getTransaction().commit();
				if (removeSuccessfully) {
					dataManager.getDataAccessTransactions().newTransaction(username, newFunds, "OUT", message);
					return true;
				} else {
					System.out.println(username + " has not enough funds");
					return false;
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * * THIS FUNCTION UPDATES FUNDS TO THE WALLET n - actual funds s - new funds
	 * POSITIVE NUMBER RETURN OF THIS FUNCTION s
	 * 
	 * @param username
	 *            which funds are going to be added
	 * @param newFunds
	 *            that are going to be changed POSITIVE NUMBER
	 * @return true if changed successfully
	 */
	@Override
	public boolean updateWalletFunds(String username, long newFunds) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return false;
			} else {
				db.getTransaction().begin();
				user.setWalletFunds(newFunds);
				dataManager.getDataAccessTransactions().newTransaction(username, newFunds, "IN", "WALLET FUNDS UPDATED BY AN ADMINISTRATOR");
				db.getTransaction().commit();
				System.out.println(username + " funds has been updated");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This function updates the ipaddress of a given username
	 * 
	 * @param username
	 *            which ipAddress is going to be added
	 * @param newIpAddress
	 *            that is going to be updated
	 * @return true if changed succesfully
	 */
	@Override
	public boolean updateIpAddress(String username, String newIpAddress) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return false;
			} else {
				db.getTransaction().begin();
				user.setIp_Address(newIpAddress);
				db.getTransaction().commit();
				System.out.println(username + " ip address has been updated");
				return true;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This function updates the favourite sports a given username
	 * 
	 * @param username
	 *            which favourite sports is going to be update
	 * @param newFavouriteSports
	 *            new List of favourite sports that is going to be replaced
	 * @return true if changed successfully
	 */
	@Override
	public boolean updateFavouriteSports(String username, List<String> newFavouriteSports) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return false;
			} else {
				db.getTransaction().begin();
				user.setFavouriteSports(newFavouriteSports);
				db.getTransaction().commit();
				System.out.println(username + " favourite sports has been updated");
				return true;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This function updates the favorite teams given username
	 * 
	 * @param username
	 *            which favorite teams is going to be update
	 * @param newFavouriteTeams
	 *            new List of favorite teams that is going to be replaced
	 * @return true if changed successfully
	 */
	@Override
	public boolean updateFavouriteTeams(String username, List<String> newFavouriteTeams) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return false;
			} else {
				db.getTransaction().begin();
				user.setFavouriteTeams(newFavouriteTeams);
				db.getTransaction().commit();
				System.out.println(username + " favourite teams has been updated");
				return true;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This method updates the user rank
	 * 
	 * @param username
	 *            which user rank is going to be changed
	 * @param newRank
	 *            of the user
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - rank is the same -1 - Error in the database
	 */
	@Override
	public int updateUserRank(String username, int newRank) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return 1;
			} else {
				if (user.getUserRank() == newRank) {
					System.out.println("The new rank cant be the same");
					return 2;
				} else {
					db.getTransaction().begin();
					user.setUserRank(newRank);
					db.getTransaction().commit();
					System.out.println(username + " rank has been updated");
					return 0;
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
	}

	/**
	 * This method updates the user sex
	 * 
	 * @param username
	 *            which user sex is going to be changed
	 * @param sex
	 *            of the user
	 * @return integer errorcode 0 - successfully changed 1 - user does not exist
	 *         2 - sex is the same -1 - Error in the database
	 */
	@Override
	public int updateSex(String username, String sex) {
		try {
			User user = getUserByUsername(username);
			if (user == null) {
				System.out.println("Username " + user + " is not in the db");
				return 1;
			} else {
				if (user.getSex().equals(sex)) {
					System.out.println("The sex cant be the same");
					return 2;
				} else {
					db.getTransaction().begin();
					user.setSex(sex);
					db.getTransaction().commit();
					System.out.println(username + " sex has been updated");
					return 0;
				}
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
	}

	/**
	 * This method returns the quantity of users in the database
	 * 
	 * @return number of users in the database
	 */
	@Override
	public int usersQuantity() {
		List<User> userList = retrieveAllUsers();
		if (userList != null) {
			return userList.size();
		} else {
			return -1;
		}
	}

	/**
	 * This method returns the next id that proceed to a given id
	 * 
	 * @param id
	 *            of the user before
	 * @param type
	 *            BANNED OR ALL
	 * @return id of the next user
	 */
	@Override
	public int idAfterIdUsers(int id, String type) {
		List<User> users = null;
		if (type.equals("ALL")) {
			users = retrieveAllUsersFrom(id, 2);
		} else if (type.equals("BANNED")) {
			users = retrieveAllBannedUsersFrom(id, 2);
		} else {
			return -1;
		}
		if (users.size() >= 2) {
			return users.get(1).getId();
		} else {
			return -1;
		}
	}

	/**
	 * This function returns the id of the first user in the database
	 * 
	 * @param type
	 *            BANNED OR ALL
	 * @return id of the first User
	 */
	@Override
	public int firstUserId(String type) {
		List<User> userList = null;
		if (type.equals("ALL")) {
			userList = retrieveAllUsersFrom(1, 2);
		} else if (type.equals("BANNED")) {
			userList = retrieveAllBannedUsersFrom(1, 2);
		} else {
			return -1;
		}
		if (userList != null && userList.size() > 0) {
			return userList.get(0).getId();
		} else {
			return -1;
		}
	}

	/**
	 * This function returns the id of the last user registered
	 * 
	 * @param type
	 *            BANNED OR ALL
	 * @return the if of the last user Registered
	 */
	@Override
	public int lastUserId(String type) {
		List<User> userList = null;
		if (type.equals("ALL")) {
			userList = retrieveAllUsers();
		} else if (type.equals("BANNED")) {
			userList = retrieveAllBannedUsers();
		} else {
			return -1;
		}
		if (userList != null) {
			return userList.get(userList.size() - 1).getId();
		} else {
			return -1;
		}
	}

	/**
	 * This function returns n ids before the given id
	 * 
	 * @param id
	 *            of the user
	 * @param amount number of user to retrieve
	 * @param type
	 *            BANNED OR ALL
	 * @return id of n before
	 */
	@Override
	public int nIdsBeforeUsers(int id, int amount, String type) {
		List<User> userList = untilGivenIdUsers(id, type);
		if (id <= 0) {
			return -1;
		}
		if (userList != null && userList.size() > 0) {
			if (userList.size() <= amount) {
				return userList.get(0).getId();
			} else {
				return userList.get(userList.size() - amount).getId();
			}
		} else {
			return -1;
		}
	}

	/**
	 * This method retrieves all users until a given id
	 * 
	 * @param untilTo
	 *            until which id we want to retrieve
	 * @param type
	 *            BANNED OR ALL
	 * @return List of users until untilTo
	 */
	@Override
	public List<User> untilGivenIdUsers(int untilTo, String type) {
		TypedQuery<User> query = null;
		if (type.equals("ALL")) {
			query = db.createQuery("SELECT us FROM User us WHERE us.id < ?1", User.class);
			query.setParameter(1, untilTo);
		} else if (type.equals("BANNED")) {
			query = db.createQuery("SELECT us FROM User us WHERE us.id < ?1 AND us.userRank=0", User.class);
			query.setParameter(1, untilTo);
		} else {
			return null;
		}

		try {
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

	
	
}
