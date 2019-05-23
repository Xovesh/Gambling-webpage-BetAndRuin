package domainV2;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Id
	private String username;
	private String password;
	private Calendar birthDate;
	private String sex;
	private String phoneNumber;
	private String email;
	private String name;
	private String surname;
	private String country;
	private String city;
	private String address;
	private long walletFunds;
	private String ip_Address;
	private Calendar lastLogin;
	private Calendar firstLogin;
	private long totalMoneyBalance;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<String> favouriteSports;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<String> favouriteTeams;
	private int userRank;

	public User(String username, String password, Calendar birthDate, String phoneNumber, String email, String name,
			String surname, String country, String city, String address, String ip_Address, String sex) {

		this.username = username;
		this.password = password;
		this.birthDate = birthDate;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.country = country;
		this.city = city;
		this.address = address;
		this.walletFunds = 0;
		this.ip_Address = ip_Address;
		this.lastLogin = new GregorianCalendar();
		this.firstLogin = new GregorianCalendar();
		this.totalMoneyBalance = 0;
		this.favouriteSports = new LinkedList<String>();
		this.favouriteTeams = new LinkedList<String>();
		this.userRank = 1;
		this.sex = sex;
	}

	public String getUsername() {
		return username;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void updateLastLogin() {
		this.lastLogin = new GregorianCalendar();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Calendar getBirthDate() {
		return birthDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getWalletFunds() {
		return walletFunds;
	}

	/**
	 * This function set the wallets funds
	 * 
	 * @param walletFunds funds to be set
	 */
	public void setWalletFunds(long walletFunds) {
		this.walletFunds = walletFunds;
	}

	/**
	 * This function increase the wallet funds.
	 * 
	 * @param walletFunds
	 *            to be added (POSITIVE NUMBER)
	 */
	public void addWalletFunds(long walletFunds) {
		this.walletFunds += walletFunds;
		this.totalMoneyBalance += walletFunds;
	}

	/**
	 * @param walletFunds
	 *            to be removed (POSITIVE NUMBER)
	 * @return true if successfully removed
	 */
	public boolean removeWalletFunds(long walletFunds) {
		if (this.getWalletFunds() - walletFunds < 0) {
			return false;
		} else {
			this.walletFunds -= walletFunds;
			this.totalMoneyBalance -= walletFunds;
			return true;
		}
	}

	public String getIp_Address() {
		return ip_Address;
	}

	public void setIp_Address(String ip) {
		this.ip_Address = ip;
	}

	public Calendar getLastLogin() {
		return lastLogin;
	}

	public Calendar getFirstLogin() {
		return firstLogin;
	}

	public long getTotalMoneyBalance() {
		return totalMoneyBalance;
	}

	public void setTotalMoneyBalance(long totalMoneyBalance) {
		this.totalMoneyBalance = totalMoneyBalance;
	}

	public List<String> getFavouriteSports() {
		return favouriteSports;
	}

	public void setFavouriteSports(List<String> favouriteSports) {
		this.favouriteSports = favouriteSports;
	}

	public void setFavouriteTeams(List<String> favouriteTeams) {
		this.favouriteTeams = favouriteTeams;
	}

	public List<String> getFavouriteTeams() {
		return favouriteTeams;
	}

	/**
	 * This method returns the List of favourite teams to an String
	 * 
	 * @return String with the favourite teams
	 */
	public String favouriteTeamsToString() {
		String res = "";
		for (String s : this.favouriteTeams) {
			res += s + ";";
		}
		return res;
	}

	/**
	 * This method returns the List of favourite sports to an String
	 * 
	 * @return String with the favourite sports
	 */
	public String favouriteSportsToString() {
		String res = "";
		for (String s : this.favouriteSports) {
			res += s + ";";
		}
		return res;
	}

	public int getUserRank() {
		return userRank;
	}

	public void setUserRank(int userRank) {
		this.userRank = userRank;
	}

	public int getId() {
		return id;
	}

	/**
	 * This method returns the birthday of the user in format yyyy-MM-dd
	 * 
	 * @return String with the format yyyy-MM-dd
	 */
	public String getBirthDateYearFormat() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(this.birthDate.getTime());
		return formatted;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", birthDate="
				+ birthDate.getTime().toString() + ", phoneNumber=" + phoneNumber + ", email=" + email + ", name="
				+ name + ", surname=" + surname + ", country=" + country + ", city=" + city + ", address=" + address
				+ ", walletFunds=" + walletFunds + ", ip_Address=" + ip_Address + ", lastLogin="
				+ lastLogin.getTime().toString() + ", firstLogin=" + firstLogin.getTime().toString()
				+ ", totalMoneyBalance=" + totalMoneyBalance + ", favouriteSports=" + favouriteSports
				+ ", favouriteTeams=" + favouriteTeams + ", userRank=" + userRank + "]";
	}

}
