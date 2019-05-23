package webServerV3;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import businessLogicV3.privateAPI.BLFacadePrivate;
import businessLogicV3.publicAPI.BLFacadePublic;
import domainV2.News;
import domainV2.Transaction;
import domainV2.User;
import domainV2.bet.BetSuperClass;
import domainV2.event.Event;
import domainV2.event.TemporalStats;
import domainV2.question.Question;
import domainV2.util.Language;

public class RewriteAdminHTML {

	/**
	 * This function rewrites the adminUser page. It loads a list with 200 users to
	 * manage
	 * 
	 * @param from id of the user from where we are going to start viewing them
	 * @param blP  private business logic
	 * @param type BANNED or ALL depeding which type of user we waznt to retrieve
	 * @return HTML code of the page
	 * @throws Exception exception
	 */
	public static String adminUsers(int from, BLFacadePrivate blP, String type) throws Exception {

		File input = new File("./resources/templates2/administration/adminUsers.html");
		List<User> Users = null;
		if (type.equals("ALL")) {
			Users = blP.getUsers().retrieveAllUsersFrom(from, 200);
		} else if (type.equals("BANNED")) {
			Users = blP.getUsers().retrieveAllBannedUsersFrom(from, 200);
		} else {
			throw new Exception();
		}
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			String s = "";
			for (User users : Users) {
				s = s + "<tr><td>" + users.getId() + "</td><td>" + users.getUsername() + "</td><td>" + users.getEmail()
						+ "</td><td>" + users.getName() + "</td><td>" + users.getSurname() + "</td><td>"
						+ users.getPhoneNumber() + "</td><td>" + users.getFirstLogin().getTime().toString()
						+ "</td><td>" + users.getLastLogin().getTime().toString() + "</td><td>" + users.getIp_Address()
						+ "</td><td>" + users.getWalletFunds() + "</td><td>" + users.getUserRank()
						+ "</td><td><form id=\"userDetails\" action=\"/administration/adminuserdata\" method=\"post\">\r\n"
						+ "                            <div class=\"form-group text-left\">\r\n"
						+ "                                <button class=\"btn btn-default btn-lg\" type=\"submit\" id=\"userid\" name=\"id\" value=\""
						+ users.getId() + "\">More Details</button>\r\n" + "                            </div>\r\n"
						+ "                        </form></td></tr>";
			}
			Elements a = doc.select("tbody");
			a.html(s);
			a = doc.select("div#paginationUsers");
			s = "";
			if (Users.size() > 0 && blP.getUsers().usersQuantity() > 200) {
				if (from == blP.getUsers().firstUserId(type)) {
					s = "<nav aria-label=\"navigation-panel \">\r\n" + "<ul class=\"pagination\">\r\n"
							+ "                            <li class=\"page-item\"><a class=\"page-link\" href=\"/administration/users?from="
							+ blP.getUsers().idAfterIdUsers(Users.get(Users.size() - 1).getId(), type) + "&type=" + type
							+ "\">Next Page</a></li></ul></nav>";
				} else if (Users.get(Users.size() - 1).getId() == blP.getUsers().lastUserId(type)) {
					s = "<nav aria-label=\"navigation-panel \">\r\n" + "<ul class=\"pagination\">\r\n"
							+ "                            <li class=\"page-item\"><a class=\"page-link\" href=\"/administration/users?from="
							+ blP.getUsers().nIdsBeforeUsers(from, 200, type) + "&type=" + type
							+ "\">Page Before</a></li>\r\n" + "                            </ul></nav>";
				} else {
					s = "<nav aria-label=\"navigation-panel \">\r\n" + "<ul class=\"pagination\">\r\n"
							+ "                            <li class=\"page-item\"><a class=\"page-link\" href=\"/administration/users?from="
							+ blP.getUsers().nIdsBeforeUsers(from, 200, type) + "&type=" + type
							+ "\">Page Before</a></li>\r\n"
							+ "                            <li class=\"page-item\"><a class=\"page-link\" href=\"/administration/users?from="
							+ blP.getUsers().idAfterIdUsers(Users.get(Users.size() - 1).getId(), type) + "&type=" + type
							+ "\">Next Page</a></li></ul></nav>";
				}
			}
			a.html(s);

			a = doc.select("div#searchUser");

			s = "<form pattern=\"[^&]+\" method=\"get\" action=\"/administration/users\">\r\n"
					+ "                            <div class=\"form-group\" style=\"margin-top:20px;\">\r\n"
					+ "                                <input type=\"text\" name=\"from\" placeholder=\"ID\">\r\n"
					+ "                                <input type=\"hidden\" name=\"type\" value=\"" + type + "\">\r\n"
					+ "                                <button class=\"btn btn-sm btn-success\" type=\"submit\"><i\r\n"
					+ "                                        class=\"glyphicon glyphicon-ok-sign\"></i> Search\r\n"
					+ "                                </button>\r\n" + "                            </div>\r\n"
					+ "                        </form>";

			a.html(s);
			return doc.html();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function updates the adminuserdata page. Shows the information about a
	 * user
	 * 
	 * @param user         user with data we are going to see
	 * @param uBets        list with the bets of the user
	 * @param transactions list with the transactions of the user
	 * @param bl           public business logic
	 * @return HTML code of the page
	 */
	public static String adminUserData(User user, List<BetSuperClass> uBets, List<Transaction> transactions,
			BLFacadePublic bl) {
		File input = new File("./resources/templates2/administration/adminUserData.html");
		System.out.println(user.toString());
		try {
			Document doc = Jsoup.parse(input, "UTF-8");

			String basicinfo = "<h1>" + user.getUsername() + "</h1><h4>" + user.getName() + " " + user.getSurname()
					+ "</h4><span class=\"help-block\">" + user.getCountry() + " " + user.getCity() + "</span>";
			Elements basic = doc.select("div.user-heading");
			basic.html(basicinfo);

			// information
			String allData = "<tr>\r\n" + "                                        <th scope=\"row\">Id</th>\r\n"
					+ "                                        <td>" + user.getId() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Username</th>\r\n"
					+ "                                        <td>" + user.getUsername() + "</td>\r\n"
					+ "                                    </tr><tr>"
					+ "                                        <th scope=\"row\">Birth Date</th>\r\n"
					+ "                                        <td>" + user.getBirthDateYearFormat() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Email</th>\r\n"
					+ "                                        <td>" + user.getEmail() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Phone Number</th>\r\n"
					+ "                                        <td>" + user.getPhoneNumber() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                   <tr>\r\n"
					+ "                                        <th scope=\"row\">Name</th>\r\n"
					+ "                                        <td>" + user.getName() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Surname</th>\r\n"
					+ "                                        <td>" + user.getSurname() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Sex</th>\r\n"
					+ "                                        <td>" + user.getSex() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    </tr>\r\n"
					+ "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Country</th>\r\n"
					+ "                                        <td>" + user.getCountry() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">City</th>\r\n"
					+ "                                        <td>" + user.getCity() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Address</th>\r\n"
					+ "                                        <td>" + user.getAddress() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Wallet Funds</th>\r\n"
					+ "                                        <td>" + user.getWalletFunds() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Ip Address</th>\r\n"
					+ "                                        <td>" + user.getIp_Address() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">First Login</th>\r\n"
					+ "                                        <td>" + user.getFirstLogin().getTime().toString()
					+ "</td>\r\n" + "                                    </tr>\r\n"
					+ "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Last Login</th>\r\n"
					+ "                                        <td>" + user.getLastLogin().getTime().toString()
					+ "</td>\r\n" + "                                    </tr>\r\n"
					+ "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Favourite Sports</th>\r\n"
					+ "                                        <td>" + user.favouriteSportsToString() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Favourite Teams</th>\r\n"
					+ "                                        <td>" + user.favouriteTeamsToString() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">Total Money Balance</th>\r\n"
					+ "                                        <td>" + user.getTotalMoneyBalance() + "</td>\r\n"
					+ "                                    </tr>\r\n" + "                                    <tr>\r\n"
					+ "                                        <th scope=\"row\">User rank</th>\r\n"
					+ "                                        <td>" + user.getUserRank() + "</td>\r\n"
					+ "                                    </tr>";

			Elements data = doc.select("tbody.userinfo");
			data.html(allData);
			// User bets

			String bets = "";
			if (uBets != null) {
				Event ev = null;
				for (BetSuperClass uBet : uBets) {
					ev = bl.getEvents().getEventByID(uBet.getQuestion().getParentEventId());
					bets = bets + "<tr>\r\n" + "                                        <td>" + uBet.getId()
							+ "</td>\r\n" + "                                        <td>" + uBet.getQuestion().getId()
							+ "</td><td>" + ev.publicEventName + "</td><td>"
							+ uBet.getQuestion().translation.getDefaultText() + "</td>\r\n"
							+ "                                        <td>" + uBet.getPrediction() + "</td>\r\n"
							+ "                                        <td>" + uBet.getBetAmount() + "</td>\r\n"
							+ "                                        <td>" + uBet.getPayoff() + "</td>\r\n"
							+ "                                        <td>" + uBet.getFinalPay() + "</td><td>"
							+ uBet.isWon() + "</td><td>" + ev.isResolved() + "</td></tr>";
				}
			}
			Elements bet = doc.select("tbody#bet");
			bet.html(bets);

			// transactions
			String transac = "";
			if (transactions != null) {
				for (Transaction ts : transactions) {

					transac += "<tr>\r\n" + "                                        <td>" + ts.getId() + "</td>\r\n"
							+ "                                        <td>" + ts.getDate().getTime().toString()
							+ "</td>\r\n" + "                                        <td>" + ts.getMessage()
							+ "</td>\r\n" + "                                        <td>" + ts.getAmount()
							+ "</td></tr>";
				}
			}
			Elements trans = doc.select("tbody#Transaction");
			trans.html(transac);

			// updating info
			Elements settings = doc.select("div#settings");
			String rank = "";
			int r = user.getUserRank();
			String rs = user.getSex();
			if (r == 0) {
				rank = "<option selected=\"selected\" value=\"0\">0. Ban</option><option value=\"1\">1. Member</option><option value=\"2\">2. Premium</option><option value=\"3\">3. Admin</option>";
			} else if (r == 1) {
				rank = "<option value=\"0\">0. Ban</option><option selected=\"selected\" value=\"1\">1. Member</option><option value=\"2\">2. Premium</option><option value=\"3\">3. Admin</option>";
			} else if (r == 2) {
				rank = "<option value=\"0\">0. Ban</option><option value=\"1\">1. Member</option><option selected=\"selected\" value=\"2\">2. Premium</option><option value=\"3\">3. Admin</option>";
			} else {
				rank = "<option value=\"0\">0. Ban</option><option value=\"1\">1. Member</option><option value=\"2\">2. Premium</option><option selected=\"selected\" value=\"3\">3. Admin</option>";
			}

			if (rs.equals("Male")) {
				rs = "<option selected=\"selected\" value=\"Male\" name=\"sex\">Male</option><option value=\"Female\" name=\"sex\">Female</option><option name=\"sex\" value=\"Other\">Other</option>";
			} else if (rs.equals("Female")) {
				rs = "<option value=\"Male\" name=\"sex\">Male</option><option selected=\"selected\" name=\"sex\" value=\"Female\">Female</option><option name=\"sex\" value=\"Other\">Other</option>";
			} else {
				rs = "<option value=\"Male\" name=\"sex\">Male</option><option name=\"sex\" value=\"Female\">Female</option><option name=\"sex\" value=\"Other\" selected=\"selected\">Other</option>";
			}
			String userInformation = "<h1>Settings</h1>\r\n" + "                                <hr>\r\n"
					+ "                                <div class=\"row\">\r\n"
					+ "                                    <div class=\"form-body\">\r\n"
					+ "                                        <h3 class=\"form-title text-left\">Change user rank</h3>\r\n"
					+ "                                        <br>\r\n"
					+ "                                        <form pattern=\"[^&]+\" action=\"/administration/adminuserdatarankupdate\" method=\"post\">\r\n"
					+ "                                            <fieldset>\r\n"
					+ "                                                <div class=\"form-group\">\r\n"
					+ "                                                    <div class=\"col-xs-12\">\r\n"
					+ "                                                        <label for=\"rank\">Select list:</label>\r\n"
					+ "                                                        <input type=\"hidden\" id=\"newusername\" name=\"newusername\" value=\""
					+ user.getUsername() + "\">\r\n"
					+ "                                                        <select class=\"form-control\" id=\"rank\" name=\"rank\">\r\n"
					+ rank + "                                                        </select>\r\n"
					+ "                                                    </div>\r\n"
					+ "                                                </div>\r\n"
					+ "                                                <div class=\"form-group\">\r\n"
					+ "                                                    <div class=\"col-xs-12\">\r\n"
					+ "                                                        <br>\r\n"
					+ "                                                        <div class=\"form-group text-left\">\r\n"
					+ "                                                            <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
					+ "                                                                    class=\"glyphicon glyphicon-ok-sign\"></i>Update Rank\r\n"
					+ "                                                            </button>\r\n"
					+ "                                                        </div>\r\n"
					+ "                                                    </div>\r\n"
					+ "                                                </div>\r\n"
					+ "                                            </fieldset>\r\n"
					+ "                                        </form>"
					+ "                                    </div>\r\n" + "                                </div>\r\n"
					+ "                                <div class=\"row\">\r\n"
					+ "                                    <hr>\r\n"
					+ "                                    <h3 class=\"form-title text-left\">Change User Information</h3>\r\n"
					+ "                                    <form pattern=\"[^&]+\" class=\"form\" action=\"/administration/adminuserdataupdate\" method=\"POST\" id=\"updateUserInfo\">\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"first_name\"><h4>First name</h4></label>\r\n"
					+ "                                                <input type=\"text\" class=\"form-control\" name=\"first_name\"\r\n"
					+ "                                                       id=\"first_name\" placeholder=\"First Name\" value=\""
					+ user.getName() + "\">\r\n" + "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"last_name\"><h4>Last name</h4></label>\r\n"
					+ "                                                <input type=\"text\" class=\"form-control\" name=\"last_name\"\r\n"
					+ "                                                       id=\"last_name\"\r\n"
					+ "                                                       placeholder=\"Last name\" value=\""
					+ user.getSurname() + "\">\r\n" + "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"phone\"><h4>Phone Number</h4></label>\r\n"
					+ "                                                <input type=\"text\" class=\"form-control\" name=\"phone\" id=\"phone\"\r\n"
					+ "                                                       placeholder=\"Phone Number\" value=\""
					+ user.getPhoneNumber() + "\">\r\n" + "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"sex\"><h4>Sex</h4></label>\r\n"
					+ "                                                <select class=\"form-control\" id=\"sex\" name=\"sex\">\r\n"
					+ rs + "                                                </select>\r\n"
					+ "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"email\"><h4>Email</h4></label>\r\n"
					+ "                                                <input type=\"email\" class=\"form-control\" name=\"email\" id=\"email\"\r\n"
					+ "                                                       placeholder=\"Email\" value=\""
					+ user.getEmail() + "\">\r\n" + "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"country\"><h4>Country</h4></label>\r\n"
					+ "<input type=\"text\" class=\"form-control\" name=\"country\" id=\"country\"\r\n"
					+ "                                                       placeholder=\"Country\" value=\""
					+ user.getCountry() + "\">" + "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"city\"><h4>City</h4></label>\r\n"
					+ "                                                <input type=\"text\" class=\"form-control\" name=\"city\" id=\"city\"\r\n"
					+ "                                                       placeholder=\"City\" value=\""
					+ user.getCity() + "\">\r\n" + "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"address\"><h4>Address</h4></label>\r\n"
					+ "                                                <input type=\"text\" class=\"form-control\" name=\"address\" id=\"address\"\r\n"
					+ "                                                       placeholder=\"Address\" value=\""
					+ user.getAddress() + "\">\r\n" + "                                            </div>\r\n"
					+ "                                        </div>" + "<div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-12\">\r\n"
					+ "                                                <label for=\"favouriteSports\"><h4>Favourite Sports</h4></label>\r\n"
					+ "                                                <textarea rows=\"5\" style=\"resize: none\" class=\"form-control\" name=\"favouriteSports\" id=\"favouriteSports\"\r\n"
					+ "                                                       placeholder=\"Favourite Sports\">"
					+ user.favouriteSportsToString() + "</textarea>\r\n"
					+ "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-12\">\r\n"
					+ "                                                <label for=\"favouriteTeams\"><h4>Favourite Teams</h4></label>\r\n"
					+ "                                                <textarea rows=\"5\" style=\"resize: none\" class=\"form-control\" name=\"favouriteTeams\" id=\"favouriteTeams\"\r\n"
					+ "                                                       placeholder=\"Favourite Teams\">"
					+ user.favouriteTeamsToString() + "</textarea>\r\n"
					+ "                                            </div>\r\n"
					+ "                                        </div>"
					+ "<input type=\"hidden\" name=\"username\" value=\"" + user.getUsername() + "\">"

					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-12\">\r\n"
					+ "                                                <br>\r\n"
					+ "                                                <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
					+ "                                                        class=\"glyphicon glyphicon-ok-sign\"\r\n"
					+ "                                                        ></i> Update\r\n"
					+ "                                                </button>"
					+ "                                                <button class=\" btn btn-lg\" type=\"reset\"><i\r\n"
					+ "                                                        class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
					+ "                                                </button>\r\n"
					+ "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                    </form>\r\n</div>\r\n" + "<div class=\"row\">\r\n"
					+ "                                    <hr>\r\n"
					+ "                                    <h3 class=\"form-title text-left\">Update Funds</h3>\r\n"
					+ "                                    <form pattern=\"[^&]+\" class=\"form\" action=\"/administration/updatefunds\" method=\"post\" id=\"updateFunds\">\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"funds\"><h4>New Funds</h4></label>\r\n"
					+ "                                                <input type=\"text\" class=\"form-control\" name=\"funds\"\r\n"
					+ "                                                       id=\"funds\" placeholder=\"Update Funds\" value=\""
					+ user.getWalletFunds() + "\">\r\n" + "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                        <input type=\"hidden\" name=\"username\" value=\""
					+ user.getUsername() + "\">\r\n"
					+ "                                        <div class=\" form-group\">\r\n"
					+ "                                            <div class=\"col-xs-12\">\r\n"
					+ "                                                <br>\r\n"
					+ "                                                <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
					+ "                                                        class=\"glyphicon glyphicon-ok-sign\"></i> Update Funds\r\n"
					+ "                                                </button>\r\n"
					+ "                                                <button class=\"btn btn-lg\" type=\"reset\"><i\r\n"
					+ "                                                        class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
					+ "                                                </button>\r\n"
					+ "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                    </form>\r\n" + "                                </div>"
					+ "                                 <div class=\"row\">\r\n"
					+ "                                    <hr>\r\n"
					+ "                                    <h3 class=\"form-title text-left\">Change User Password</h3>\r\n"
					+ "                                    <form pattern=\"[^&]+\" class=\"form\" action=\"/administration/updateadminpassword\" method=\"post\" id=\"updatePassword\">\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"password\"><h4>Password</h4></label>\r\n"
					+ "                                                <input type=\"password\" class=\"form-control\" name=\"password\"\r\n"
					+ "                                                       id=\"password\" placeholder=\"Password\" value=\"\">\r\n"
					+ "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-6\">\r\n"
					+ "                                                <label for=\"password2\"><h4>Repeat Password</h4></label>\r\n"
					+ "                                                <input type=\"password\" class=\"form-control\" name=\"password2\"\r\n"
					+ "                                                       id=\"password2\" placeholder=\"Repeat Password\" value=\"\">\r\n"
					+ "                                            </div>\r\n"
					+ "                                        </div>\r\n" + "<div class=\"form-group\">\r\n"
					+ "                                        <input type=\"hidden\" name=\"username\" value=\""
					+ user.getUsername() + "\">\r\n"
					+ "                                        <div class=\" form-group\">\r\n"
					+ "                                            <div class=\"col-xs-12\">\r\n"
					+ "                                                <br>\r\n"
					+ "                                                <button class=\"btn btn-lg btn-danger\" type=\"submit\"\r\n"
					+ "                                                ><i\r\n"
					+ "                                                        class=\"glyphicon glyphicon-ok-sign\"></i> Update Password\r\n"
					+ "                                                </button>\r\n"
					+ "                                                <button class=\"btn btn-lg\" type=\"reset\"><i\r\n"
					+ "                                                        class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
					+ "                                                </button>\r\n"
					+ "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                    </form>\r\n" + "</div> </div>\r\n"
					+ "                                <div class=\"row\">\r\n"
					+ "                                    <hr>\r\n"
					+ "                                    <h3 class=\"form-title text-left\">Delete User</h3>\r\n"
					+ "                                    <div class=\"form-group\">\r\n"
					+ "                                        <div class=\"col-xs-6\">\r\n"
					+ "                                            <div class=\"form-group text-left\">\r\n"
					+ "                                                <form pattern=\"[^&]+\" method=\"post\" action=\"/administration/adminuserdelete\">\r\n"
					+ "                                                    <input type=\"hidden\" name=\"username\" value=\""
					+ user.getUsername() + "\">\r\n"
					+ "                                                    <button class=\"btn btn-lg btn-danger\" type=\"submit\"><i\r\n"
					+ "                                                            class=\"glyphicon glyphicon-ok-sign\"></i>Delete User\r\n"
					+ "                                                    </button>\r\n"
					+ "                                                </form>\r\n"
					+ "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                    </div>\r\n" + "                                </div>";

			settings.html(userInformation);
			return doc.html();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the main page
	 * 
	 * @param blP private business logic
	 * @return HTML code of the page
	 */
	public static String adminMain(BLFacadePrivate blP) {
		File input = new File("./resources/templates2/administration/adminMain.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			Calendar today = new GregorianCalendar();
			blP.getStats().getStatsByDate(today);

			// INFORMATION
			// GRAPH1
			int betsToday = (int) blP.getStats().getStatsByDate(today).getBets();
			String x1 = "[";
			List<TemporalStats> stats = blP.getStats().getNStats(5);
			List<Long> coins = new LinkedList<>(), bets = new LinkedList<>();
			for (int i = 4; i >= 0; i--) {
				if (i >= stats.size()) {
					coins.add((long) 0);
					bets.add((long) 0);
				} else {
					coins.add(stats.get(i).getCoins());
					bets.add(stats.get(i).getBets());
				}
			}
			Collections.reverse(bets);
			Collections.reverse(coins);
			String y1 = Arrays.toString(bets.toArray());
			try {
				System.out.println(blP.getStats().getNStats(5));
				System.out.println(y1);
				for (int i = 0; i < 5; i++) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DATE, -i);
					x1 += "'" + sdf.format(calendar.getTime()) + "', ";
				}
				x1 = x1.substring(0, x1.length() - 2);
				x1 += "]";
				System.out.println(x1);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// GRAPH2
			int coinsToday = (int) blP.getStats().getStatsByDate(today).getCoins();
			String x2 = x1;
			String y2 = Arrays.toString(coins.toArray());
			// GRAPH 3
			int totalUsers = blP.getUsers().usersQuantity();

			long totalCoinsBet = blP.getStats().getAllTimeCoins();
			// Values info
			Elements values = doc.select("div#betsToday");
			values.html("" + betsToday);
			values = doc.select("div#coinsToday");
			values.html("" + coinsToday);
			values = doc.select("div#totalUsers");
			values.html("" + totalUsers);
			values = doc.select("div#totalCoins");
			values.html("" + totalCoinsBet);

			// graph 1
			Elements graph1 = doc.select("div#myDiv2");
			String g1 = "<script>\r\n" + "                                    var data = [\r\n"
					+ "                                        {\r\n"
					+ "                                            x: " + x1 + ",\r\n"
					+ "                                            y: " + y1 + ",\r\n"
					+ "                                            type: 'scatter'\r\n"
					+ "                                        }\r\n" + "                                    ];\r\n"
					+ "                                    Plotly.newPlot('myDiv2', data, {}, {showSendToCloud: true});\r\n"
					+ "                                </script>";
			graph1.html(g1);
			// graph2
			Elements graph2 = doc.select("div#myDiv3");
			String g2 = "<script>\r\n" + "                                    var data = [\r\n"
					+ "                                        {\r\n"
					+ "                                            x: " + x2 + ",\r\n"
					+ "                                            y: " + y2 + ",\r\n"
					+ "                                            type: 'scatter'\r\n"
					+ "                                        }\r\n" + "                                    ];\r\n"
					+ "                                    Plotly.newPlot('myDiv3', data, {}, {showSendToCloud: true});\r\n"
					+ "                                </script>";
			graph2.html(g2);

			return doc.html();
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * This function rewrites the adminStatistics page. It shows the statistics of
	 * the webpage
	 * 
	 * @param blP private business logic
	 * @return HTML code of the page
	 */
	public static String adminStatistics(BLFacadePrivate blP) {
		File input = new File("./resources/templates2/administration/adminStatistics.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			Calendar today = new GregorianCalendar();
			blP.getStats().getStatsByDate(today);

			// INFORMATION
			// GRAPH1
			List<TemporalStats> stats = blP.getStats().getNStats(30);
			List<Long> coins = new LinkedList<>(), bets = new LinkedList<>();
			for (int i = 30; i >= 0; i--) {
				if (i >= stats.size()) {
					coins.add((long) 0);
					bets.add((long) 0);
				} else {
					coins.add(stats.get(i).getCoins());
					bets.add(stats.get(i).getBets());
				}
			}
			Collections.reverse(bets);
			Collections.reverse(coins);
			String y1 = Arrays.toString(bets.toArray());
			List<Long> profit = new LinkedList<>();
			// DateString generation
			String x1 = "[";
			for (int i = 0; i < 30; i++) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -i);
				profit.add(blP.getStats().getTotalProfitByDate(calendar));
				x1 += "'" + sdf.format(calendar.getTime()) + "', ";
			}
			x1 = x1.substring(0, x1.length() - 2);
			x1 += "]";
			System.out.println(x1);
			// Collections.reverse(profit);
			// INFORMATION

			// GRAPH 1
			// Bets in last 30 days
			System.out.println(y1);
			// GRAPH 2
			// Coins bet in last 30 days
			String x2 = x1;
			String y2 = Arrays.toString(coins.toArray());
			// GRAPH 3
			// Profits in last 30 days
			String x3 = x1;
			String y3 = Arrays.toString(profit.toArray());
			System.out.println(Arrays.toString(profit.toArray()));
			// GRAPH 4
			String x4 = x1;
			String y4 = "[200, 1000, 200]";

			// graph 1
			Elements graph1 = doc.select("div#myDiv1");
			String g1 = "<script>\r\n" + "                                    var data = [\r\n"
					+ "                                        {\r\n"
					+ "                                            x: " + x1 + ",\r\n"
					+ "                                            y: " + y1 + ",\r\n"
					+ "                                            type: 'scatter'\r\n"
					+ "                                        }\r\n" + "                                    ];\r\n"
					+ "                                    Plotly.newPlot('myDiv1', data, {}, {showSendToCloud: true});\r\n"
					+ "                                </script>";
			graph1.html(g1);

			// graph 2
			Elements graph2 = doc.select("div#myDiv2");
			String g2 = "<script>\r\n" + "                                    var data = [\r\n"
					+ "                                        {\r\n"
					+ "                                            x: " + x2 + ",\r\n"
					+ "                                            y: " + y2 + ",\r\n"
					+ "                                            type: 'scatter'\r\n"
					+ "                                        }\r\n" + "                                    ];\r\n"
					+ "                                    Plotly.newPlot('myDiv2', data, {}, {showSendToCloud: true});\r\n"
					+ "                                </script>";
			graph2.html(g2);

			// graph 3
			Elements graph3 = doc.select("div#myDiv3");
			String g3 = "<script>\r\n" + "                                    var data = [\r\n"
					+ "                                        {\r\n"
					+ "                                            x: " + x3 + ",\r\n"
					+ "                                            y: " + y3 + ",\r\n"
					+ "                                            type: 'scatter'\r\n"
					+ "                                        }\r\n" + "                                    ];\r\n"
					+ "                                    Plotly.newPlot('myDiv3', data, {}, {showSendToCloud: true});\r\n"
					+ "                                </script>";
			graph3.html(g3);

			// graph 4
			Elements graph4 = doc.select("div#myDiv4");
			String g4 = "<script>\r\n" + "                                    var data = [\r\n"
					+ "                                        {\r\n"
					+ "                                            x: " + x4 + ",\r\n"
					+ "                                            y: " + y4 + ",\r\n"
					+ "                                            type: 'scatter'\r\n"
					+ "                                        }\r\n" + "                                    ];\r\n"
					+ "                                    Plotly.newPlot('myDiv4', data, {}, {showSendToCloud: true});\r\n"
					+ "                                </script>";
			graph4.html(g4);
			return doc.html();
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * This function rewrites the adminnews page. It shows a list of 200 news
	 * 
	 * @param from     if from which new we start retrieving
	 * @param blP      private business logic
	 * @param language language of the news
	 * @return HTML code of the page
	 */
	public static String adminNews(int from, BLFacadePrivate blP, String language) {
		File input = new File("./resources/templates2/administration/adminNews.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			String s = "";
			List<News> newsList = blP.getNews().retrieveNewsFromTo(from, 200, language);
			for (News news : newsList) {
				s = s + "<tr><td>" + news.getId() + "</td><td>" + news.getLanguage() + "</td><td>"
						+ news.getDate().getTime().toString() + "</td><td>" + news.getHead() + "</td><td>"
						+ news.getContent() + "</td><td>" + news.getPicture()
						+ "</td><td><form pattern=\"[^&]+\" id=\"newsDetails\" action=\"/administration/newsdetails\" method=\"post\">\r\n"
						+ "                            <div class=\"form-group text-left\">\r\n"
						+ "                                <button class=\"btn btn-default btn-lg\" type=\"submit\" id=\"news\" name=\"newsid\" value=\""
						+ news.getId() + "\">More Details</button>\r\n" + "                            </div>\r\n"
						+ "                        </form></td></tr>";
			}

			Elements a = doc.select("tbody");
			a.html(s);

			a = doc.select("form#languageSelection");
			if (language.equals("ES")) {
				s = "<div class=\"col-sm-3 text-center\">\r\n"
						+ "                        <label for=\"language\"><h3>Language:</h3></label>\r\n"
						+ "                        <select id=\"language\" name=\"language\" onchange=\"this.form.submit();\">\r\n"
						+ "                            <option value=\"ALL\">All</option>\r\n"
						+ "                            <option value=\"ES\" selected=\"selected\">Spanish</option>\r\n"
						+ "                            <option value=\"EN\">English</option>\r\n"
						+ "                            <option value=\"EUS\">Basque</option>\r\n"
						+ "                        </select>\r\n" + "                    </div>";
			} else if (language.equals("EN")) {
				s = "<div class=\"col-sm-3 text-center\">\r\n"
						+ "                        <label for=\"language\"><h3>Language:</h3></label>\r\n"
						+ "                        <select id=\"language\" name=\"language\" onchange=\"this.form.submit();\">\r\n"
						+ "                            <option value=\"ALL\">All</option>\r\n"
						+ "                            <option value=\"ES\">Spanish</option>\r\n"
						+ "                            <option value=\"EN\" selected=\"selected\">English</option>\r\n"
						+ "                            <option value=\"EUS\">Basque</option>\r\n"
						+ "                        </select>\r\n" + "                    </div>";
			} else if (language.equals("EUS")) {
				s = "<div class=\"col-sm-3 text-center\">\r\n"
						+ "                        <label for=\"language\"><h3>Language:</h3></label>\r\n"
						+ "                        <select id=\"language\" name=\"language\" onchange=\"this.form.submit();\">\r\n"
						+ "                            <option value=\"ALL\">All</option>\r\n"
						+ "                            <option value=\"ES\">Spanish</option>\r\n"
						+ "                            <option value=\"EN\">English</option>\r\n"
						+ "                            <option value=\"EUS\" selected=\"selected\">Basque</option>\r\n"
						+ "                        </select>\r\n" + "                    </div>";
			} else {
				s = "<div class=\"col-sm-3 text-center\">\r\n"
						+ "                        <label for=\"language\"><h3>Language:</h3></label>\r\n"
						+ "                        <select id=\"language\" name=\"language\" onchange=\"this.form.submit();\">\r\n"
						+ "                            <option value=\"ALL\" selected=\"selected\">All</option>\r\n"
						+ "                            <option value=\"ES\">Spanish</option>\r\n"
						+ "                            <option value=\"EN\">English</option>\r\n"
						+ "                            <option value=\"EUS\">Basque</option>\r\n"
						+ "                        </select>\r\n" + "                    </div>";
			}

			a.html(s);

			a = doc.select("div#paginationUsers");
			s = "<div class=\"col-sm-3 text-right\">";
			if (newsList.size() > 0 && blP.getNews().newsQuantity(language) > 200) {
				if (from == blP.getNews().firstNewsId(language)) {
					s += "<nav aria-label=\"navigation-panel \">\r\n" + "<ul class=\"pagination\">\r\n"
							+ "                            <li class=\"page-item\"><a class=\"page-link\" href=\"/administration/news?from="
							+ blP.getNews().idAfterIdNews(newsList.get(newsList.size() - 1).getId(), language)
							+ "&language=" + language + "\">Next Page</a></li></ul></nav>";
				} else if (newsList.get(newsList.size() - 1).getId() == blP.getNews().lastNewsId(language)) {
					s += "<nav aria-label=\"navigation-panel \">\r\n" + "<ul class=\"pagination\">\r\n"
							+ "                            <li class=\"page-item\"><a class=\"page-link\" href=\"/administration/news?from="
							+ blP.getNews().nIdsBeforeNews(newsList.get(0).getId(), 200, language) + "&language="
							+ language + "\">Page Before</a></li>\r\n" + "                            </ul></nav>";
				} else {
					s += "<nav aria-label=\"navigation-panel \">\r\n" + "<ul class=\"pagination\">\r\n"
							+ "                            <li class=\"page-item\"><a class=\"page-link\" href=\"/administration/news?from="
							+ blP.getNews().nIdsBeforeNews(newsList.get(0).getId(), 200, language) + "&language="
							+ language + "\">Page Before</a></li>\r\n"
							+ "                            <li class=\"page-item\"><a class=\"page-link\" href=\"/administration/news?from="
							+ blP.getNews().idAfterIdNews(newsList.get(newsList.size() - 1).getId(), language)
							+ "&language=" + language + "\">Next Page</a></li></ul></nav>";
				}
			}
			s += "</div>";
			a.html(s);

			return doc.html();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the adminServerStatus page. It shows the online users
	 * and the last logged ones
	 * 
	 * @param connectedUsers list with the connected users
	 * @param latestUsers    queue with the latest users
	 * @return HTML code of the page
	 */
	public static String adminServerStatus(List<User> connectedUsers, Queue<User> latestUsers) {
		File input = new File("./resources/templates2/administration/adminServerStatus.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements connected = doc.select("tbody#connectedusers");
			String s = "";
			for (User us : connectedUsers) {
				s = s + "<tr>\r\n" + "                            <td>" + us.getId() + "</td>\r\n"
						+ "                            <td>" + us.getUsername() + "</td>\r\n"
						+ "                        </tr>";
			}
			connected.html(s);

			s = "";

			for (User us : latestUsers) {
				s = s + "<tr>\r\n" + "                            <td>" + us.getId() + "</td>\r\n"
						+ "                            <td>" + us.getUsername() + "</td>\r\n"
						+ "                        </tr>";
			}

			Elements latest = doc.select("tbody#lastlogins");
			latest.html(s);

			return doc.html();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the adminNewsData page. It shows information about a
	 * news
	 * 
	 * @param news news which data we are going to see
	 * @return HTML code of the page
	 */
	public static String adminNewsData(News news) {
		File input = new File("./resources/templates2/administration/adminNewsData.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements addPreview = doc.select("div#preview");
			String preview = "<div class=\"panel-heading\">\r\n" + "            <h1>Preview</h1>\r\n"
					+ "        </div>\r\n" + "        <div class=\"panel-body\">\r\n"
					+ "            <h2>Language:</h2>\r\n" + "            <br>\r\n" + "            <h4>"
					+ news.getLanguage() + "</h4>\r\n" + "            <hr>\r\n" + "            <h2>Date:</h2>\r\n"
					+ "            <br>\r\n" + "            <h4>" + news.getDate().getTime().toString() + "</h4>\r\n"
					+ "            <hr>\r\n" + "            <h2>Head:</h2>\r\n" + "            <br>\r\n"
					+ "            <h4>" + news.getHead() + "</h4>\r\n" + "            <hr>\r\n"
					+ "            <h2>Content:</h2>\r\n" + "            <br>\r\n" + "            <h4>"
					+ news.getContent() + "</h4>\r\n" + "            <hr>\r\n" + "            <h2>Picture:</h2>\r\n"
					+ "            <br>\r\n" + "            <h4><img alt=\"The image does not exist\" src=\""
					+ news.getPicture() + "\" width=\"50%\" height=\"50%\" >\r\n" + "            </h4>\r\n"
					+ "        </div>";
			addPreview.html(preview);
			Elements addUpdate = doc.select("div#update");

			String updateLanguage = "";

			if (news.getLanguage().equals("ES")) {
				updateLanguage = "<div class=\"form-group\">\r\n" + "                    <div class=\"col-xs-4\">\r\n"
						+ "                        <label for=\"language\"><h4>Language</h4></label>\r\n"
						+ "                        <select class=\"form-control\" name=\"language\" id=\"language\">\r\n"
						+ "                            <option value=\"ES\" selected=\"selected\">Spanish</option>\r\n"
						+ "                            <option value=\"EUS\">Basque</option>\r\n"
						+ "                            <option value=\"EN\">English</option>\r\n"
						+ "                        </select>\r\n" + "                    </div>\r\n"
						+ "                </div>";
			} else if (news.getLanguage().equals("EUS")) {
				updateLanguage = "<div class=\"form-group\">\r\n" + "                    <div class=\"col-xs-4\">\r\n"
						+ "                        <label for=\"language\"><h4>Language</h4></label>\r\n"
						+ "                        <select class=\"form-control\" name=\"language\" id=\"language\">\r\n"
						+ "                            <option value=\"ES\">Spanish</option>\r\n"
						+ "                            <option value=\"EUS\" selected=\"selected\">Basque</option>\r\n"
						+ "                            <option value=\"EN\">English</option>\r\n"
						+ "                        </select>\r\n" + "                    </div>\r\n"
						+ "                </div>";
			} else {
				updateLanguage = "<div class=\"form-group\">\r\n" + "                    <div class=\"col-xs-4\">\r\n"
						+ "                        <label for=\"language\"><h4>Language</h4></label>\r\n"
						+ "                        <select class=\"form-control\" name=\"language\" id=\"language\">\r\n"
						+ "                            <option value=\"ES\">Spanish</option>\r\n"
						+ "                            <option value=\"EUS\">Basque</option>\r\n"
						+ "                            <option value=\"EN\" selected=\"selected\">English</option>\r\n"
						+ "                        </select>\r\n" + "                    </div>\r\n"
						+ "                </div>";
			}
			String update = "<div class=\"panel-heading\">\r\n" + "            <h1>Modify news</h1>\r\n"
					+ "        </div>\r\n" + "        <div class=\"panel-body\">\r\n"
					+ "            <form pattern=\"[^&]+\" action=\"/administration/adminnewsupdate\" method=\"post\">\r\n"
					+ "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-12\">\r\n"
					+ "                        <label for=\"head\"><h4>Head</h4></label>\r\n"
					+ "                        <input required type=\"text\" class=\"form-control\" name=\"head\"\r\n"
					+ "                               id=\"head\" placeholder=\"Head\" value=\"" + news.getHead()
					+ "\">\r\n" + "                    </div>\r\n" + "                </div>\r\n"
					+ "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-12\">\r\n"
					+ "                        <label for=\"content\"><h4>Content</h4></label>\r\n"
					+ "                        <textarea required rows=\"10\" class=\"form-control\" name=\"content\" id=\"content\"\r\n"
					+ "                                  placeholder=\"Content\" style=\"resize: none;\">"
					+ news.getContent() + "</textarea>\r\n" + "                    </div>\r\n"
					+ "                </div>\r\n" + "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-12\">\r\n"
					+ "                        <label for=\"picture\"><h4>Picture link</h4></label>\r\n"
					+ "                        <input required type=\"text\" class=\"form-control\" name=\"picture\" id=\"picture\"\r\n"
					+ "                               placeholder=\"picture\"\r\n"
					+ "                               value=\"" + news.getPicture() + "\">\r\n"
					+ "                    </div></div>" + updateLanguage
					+ "         <input type=\"hidden\" name=\"newsid\" value=\"" + news.getId() + "\">\r\n"
					+ "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-12\">\r\n" + "                        <hr>\r\n"
					+ "                        <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
					+ "                                class=\"glyphicon glyphicon-ok-sign\"></i> Update\r\n"
					+ "                        </button>\r\n"
					+ "                        <button class=\" btn btn-lg\" type=\"reset\"><i\r\n"
					+ "                                class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
					+ "                        </button>\r\n" + "                    </div>\r\n"
					+ "                </div>\r\n" + "            </form>\r\n" + "        </div>";

			addUpdate.html(update);
			Elements addDelete = doc.select("div#delete");
			String delete = "<div class=\"panel-heading\">\r\n" + "            <h1>Delete News</h1>\r\n"
					+ "        </div>\r\n" + "        <div class=\"panel-body\">\r\n"
					+ "            <div class=\"row\">\r\n" + "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-6\">\r\n"
					+ "                        <div class=\"form-group text-left\">\r\n"
					+ "                            <form pattern=\"[^&]+\" method=\"post\" action=\"/administration/adminnewsdelete\">\r\n"
					+ "                                <input type=\"hidden\" name=\"newsid\" value=\"" + news.getId()
					+ "\">\r\n"
					+ "                                <button class=\"btn btn-lg btn-danger\" type=\"submit\"><i\r\n"
					+ "                                        class=\"glyphicon glyphicon-ok-sign\"></i> Delete News\r\n"
					+ "                                </button>\r\n" + "                            </form>\r\n"
					+ "                        </div>\r\n" + "                    </div>\r\n"
					+ "                </div>\r\n" + "            </div>\r\n" + "        </div>";
			addDelete.html(delete);
			return doc.html();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the adminsetting page. It shows basic settings of the
	 * webpage
	 * 
	 * @param spanishFile content of the spanish file
	 * @param englishFile content of the english file
	 * @param basqueFile  content of the basque file
	 * @return HTML code of the page
	 */
	public static String adminSettings(String spanishFile, String englishFile, String basqueFile) {
		File input = new File("./resources/templates2/administration/adminSettings.html");
		try {

			Document doc = Jsoup.parse(input, "UTF-8");

			Elements content = doc.select("form#spanish");
			String spanish = " <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-12\">\r\n"
					+ "                                                <label for=\"contentSpanish\"><h4>Support message</h4></label>\r\n"
					+ "                                                <textarea rows=\"10\" class=\"form-control\" name=\"content\" id=\"contentSpanish\"\r\n"
					+ "                                                placeholder=\"Content\" style=\"resize: none;\">"
					+ spanishFile + "</textarea>\r\n" + "                                            </div>\r\n"
					+ "                                        </div>\r\n"
					+ "                                          <input type=\"hidden\" name=\"language\" value=\"ES\">\r\n"
					+ "                                        <div class=\"form-group\">\r\n"
					+ "                                            <div class=\"col-xs-12\">\r\n"
					+ "                                                <hr>\r\n"
					+ "                                                <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
					+ "                                                    class=\"glyphicon glyphicon-ok-sign\"></i> Update\r\n"
					+ "                                                </button>\r\n"
					+ "                                                <button class=\" btn btn-lg\" type=\"reset\"><i\r\n"
					+ "                                                    class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
					+ "                                                </button>\r\n"
					+ "                                            </div>\r\n"
					+ "                                        </div>";
			content.html(spanish);
			content = doc.select("div#spanishPreview");
			content.html("<div class=\"col-xs-12\"><h4>Preview</h4>" + spanishFile + "</div>");
			content = doc.select("form#english");
			String english = "<div class=\"form-group\">\r\n"
					+ "                                    <div class=\"col-xs-12\">\r\n"
					+ "                                        <label for=\"contentEnglish\"><h4>Support message</h4></label>\r\n"
					+ "                                        <textarea rows=\"10\" class=\"form-control\" name=\"content\" id=\"contentEnglish\"\r\n"
					+ "                                        placeholder=\"Content\" style=\"resize: none;\">"
					+ englishFile + "</textarea>\r\n" + "                                    </div>\r\n"
					+ "                                </div>\r\n"
					+ "                                <input type=\"hidden\" name=\"language\" value=\"EN\">\r\n"
					+ "                                <div class=\"form-group\">\r\n"
					+ "                                    <div class=\"col-xs-12\">\r\n"
					+ "                                        <hr>\r\n"
					+ "                                        <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
					+ "                                            class=\"glyphicon glyphicon-ok-sign\"></i> Update\r\n"
					+ "                                        </button>\r\n"
					+ "                                        <button class=\" btn btn-lg\" type=\"reset\"><i\r\n"
					+ "                                            class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
					+ "                                        </button>\r\n"
					+ "                                    </div>\r\n" + "                                </div>";
			content.html(english);
			content = doc.select("div#englishPreview");
			content.html("<div class=\"col-xs-12\"><h4>Preview</h4>" + englishFile + "</div>");
			content = doc.select("form#basque");
			String basque = "<div class=\"form-group\">\r\n"
					+ "                            <div class=\"col-xs-12\">\r\n"
					+ "                                <label for=\"contentBasque\"><h4>Support message</h4></label>\r\n"
					+ "                                <textarea rows=\"10\" class=\"form-control\" name=\"content\" id=\"contentBasque\"\r\n"
					+ "                                placeholder=\"Content\" style=\"resize: none;\">" + basqueFile
					+ "</textarea>\r\n" + "                            </div>\r\n"
					+ "                        </div>\r\n"
					+ "                              <input type=\"hidden\" name=\"language\" value=\"EUS\">\r\n"
					+ "                        <div class=\"form-group\">\r\n"
					+ "                            <div class=\"col-xs-12\">\r\n"
					+ "                                <hr>\r\n"
					+ "                                <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
					+ "                                    class=\"glyphicon glyphicon-ok-sign\"></i> Update\r\n"
					+ "                                </button>\r\n"
					+ "                                <button class=\" btn btn-lg\" type=\"reset\"><i\r\n"
					+ "                                    class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
					+ "                                </button>\r\n" + "                            </div>\r\n"
					+ "                        </div>";
			content.html(basque);
			content = doc.select("div#basquePreview");
			content.html("<div class=\"col-xs-12\"><h4>Preview</h4>" + basqueFile + "</div>");
			content = doc.select("div#Maintenance");
			String maintenance = "";
			if (SimpleHTTPServer.MAINTENANCE_MODE) {
				maintenance = "<h4>Maintanance mode</h4>\r\n"
						+ "            <h5>The maintanance mode is activated</h5>\r\n"
						+ "            <div class=\"row\">\r\n"
						+ "                <form pattern=\"[^&]+\" method=\"post\" action=\"/administration/maintenancemodechange\">\r\n"
						+ "                    <div class=\"form-group\">\r\n"
						+ "                        <div class=\"col-xs-12\">\r\n"
						+ "                            <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
						+ "                                    class=\"glyphicon glyphicon-ok-sign\"></i> Desactivate\r\n"
						+ "                            </button>\r\n" + "                        </div>\r\n"
						+ "                    </div>\r\n" + "                </form>\r\n" + "            </div>";
			} else {
				maintenance = "<h4>Maintanance mode</h4>\r\n"
						+ "            <h5>The maintanance mode is NOT activated</h5>\r\n"
						+ "            <div class=\"row\">\r\n"
						+ "                <form pattern=\"[^&]+\" method=\"post\" action=\"/administration/maintenancemodechange\">\r\n"
						+ "                    <div class=\"form-group\">\r\n"
						+ "                        <div class=\"col-xs-12\">\r\n"
						+ "                            <button class=\"btn btn-lg btn-danger\" type=\"submit\"><i\r\n"
						+ "                                    class=\"glyphicon glyphicon-ok-sign\"></i> Activate\r\n"
						+ "                            </button>\r\n" + "                        </div>\r\n"
						+ "                    </div>\r\n" + "                </form>\r\n" + "            </div>";
			}

			content.html(maintenance);
			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the adminevents page. It shows the available events
	 * 
	 * @param blP  private business logic
	 * @param type of the events to retrieve
	 * @return HTML code of the page
	 * @throws Exception exception
	 */
	public static String adminEvents(BLFacadePrivate blP, String type) throws Exception {

		File input = new File("./resources/templates2/administration/adminEvents.html");
		List<Event> eventsList = null;

		if (type.equals("RESOLVED")) {
			eventsList = blP.getEvents().getEventByResolutionInRange(true, 1, 1000);
		} else if (type.equals("UNRESOLVED")) {
			eventsList = blP.getEvents().getEventByResolutionInRange(false, 1, 1000);
		} else if (type.equals("ALL")) {
			eventsList = blP.getEvents().getEventByIdRange(0, 1000);
		} else if(type.equals("BYCANBET")){
			eventsList = blP.getEvents().getNEventsByCanBet(1000);
		}else {
			eventsList = null;
		}

		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			String s = "";

			for (Event events : eventsList) {
				s = s + "<tr><td>" + events.getID() + "</td>" + "<td>" + events.publicEventName
						+ "</td>                    <td>" + events.eventDescription.getDefaultText() + "</td>\r\n"
						+ "                    <td>" + events.getTags().toString() + "</td>\r\n"
						+ "                    <td>" + events.deadlineToString() + "</td>\r\n"
						+ "                    <td>" + events.resolveToString() + "</td>\r\n"
						+ "                    <td>" + events.isResolved() + "</td>\r\n"
						+ "                    <td>\r\n"
						+ "                        <form id=\"eventsDetails\" action=\"/administration/adminEventData\" method=\"post\">\r\n"
						+ "                            <div class=\"form-group text-left\">\r\n"
						+ "                                <button class=\"btn btn-default btn-lg\" type=\"submit\" id=\"eventid\" name=\"id\" value=\""
						+ events.getID() + "\">\r\n" + "                                    More Details\r\n"
						+ "                                </button>\r\n" + "                            </div>\r\n"
						+ "                        </form>\r\n" + "                    </td></tr>";
			}

			Elements a = doc.select("tbody");
			a.html(s);
			return doc.html();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the admineventdata page. It shows information about
	 * one event
	 * 
	 * @param ev  event to view
	 * @param blP private business logic
	 * @return HTML code of the page
	 * @throws Exception exception
	 */
	public static String adminViewEvents(Event ev, BLFacadePrivate blP) throws Exception {

		File input = new File("./resources/templates2/administration/adminEventData.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			String s = "<h2>Event ID:</h2>\r\n" + "            <br>\r\n" + "            <h4>" + ev.getID() + "</h4>\r\n"
					+ "            <hr>" + "            <h2>Event Description:</h2>\r\n" + "            <br>\r\n"
					+ "            <h4>" + ev.eventDescription.getDefaultText() + "</h4>\r\n" + "            <hr>\r\n"
					+ "            <h2>Tags</h2>\r\n" + "            <br>\r\n" + "            <h4>"
					+ ev.getTags().toString() + "</h4>\r\n" + "            <hr>\r\n"
					+ "            <h2>DeadLine Date</h2>\r\n" + "            <br>\r\n" + "            <h4>"
					+ ev.deadlineToString() + "</h4>\r\n" + "            <br>\r\n"
					+ "            <h2>Resolved Date</h2>\r\n" + "            <br>\r\n" + "            <h4>"
					+ ev.resolveToString() + "</h4>\r\n" + "            <br>\r\n" + "            <h2>Resolved</h2>\r\n"
					+ "            <br>\r\n" + "            <h4>" + ev.isResolved() + "</h4>"
					+ "<br><h2>Total profit</h2><br><h4>" + blP.getStats().getTotalProfitByID(ev.getID())
					+ "</h4><br><h2>Average User Win rate</h2><br><h4>"
					+ blP.getStats().getAverageUserWinrateByID(ev.getID()) * 100
					+ "%</h4><br><h2>Total bets</h2><br><h4>" + blP.getStats().getTotalBetByID(ev.getID()) + "</h4>";

			Elements a = doc.select("div#previewbody");
			a.html(s);

			s = "";

			for (Question questions : ev.getAllQuestions()) {
				String answer = questions.isAnswered() ? questions.getAnswer() : "Not Finished";
				s = s + "<tr><td>" + questions.getId() + "</td>\r\n" + "                    <td>"
						+ questions.translation.getDefaultText() + "</td>\r\n" + "                    <td>"
						+ questions.getQuestionMode().name() + "</td>\r\n" + "                    <td>"
						+ questions.isAnswered() + "</td>\r\n" + "                    <td>" + answer + "</td>\r\n"
						+ "                    <td>" + questions.getProfit() * 100 + "%</td>\r\n"
						+ "                    <td>" + questions.getBetAmount() + "</td>\r\n" + "<td>"
						+ questions.getUserWinrate() + "</td>" + "                    <td>\r\n"
						+ "                        <form id=\"questionDetails\" action=\"/administration/adminQuestionData\" method=\"post\">\r\n"
						+ "                            <div class=\"form-group text-left\">\r\n"
						+ "  <input type=\"hidden\" name=\"eventName\" value=\""
						+ questions.translation.getDefaultText() + "\">"
						+ "                                <button class=\"btn btn-default btn-lg\" type=\"submit\" id=\"eventid\" name=\"id\" value=\""
						+ ev.getID() + "\">\r\n" + "                                    More Details\r\n"
						+ "                                </button>\r\n" + "                            </div>\r\n"
						+ "                        </form>\r\n" + "                    </td></tr>";
			}

			a = doc.select("tbody#questionstable");
			a.html(s);

			if (!ev.isCancelled() && !ev.isResolved()) {
				s = "<form method=\"post\" action=\"/administration/admincreatequestion\">\r\n"
						+ "                            <input type=\"hidden\" name=\"eventID\" value=\"" + ev.getID()
						+ "\">\r\n"
						+ "                            <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
						+ "                                    class=\"glyphicon glyphicon-ok-sign\"></i> Add new Question\r\n"
						+ "                            </button>\r\n" + "                        </form>";

				a = doc.select("div#createQuestion");
				a.html(s);
			}
			s = "<div class=\"form-group\">\r\n" + "                    <div class=\"col-xs-12\">\r\n"
					+ "                        <label for=\"eventNameEN\"><h4>Event Name</h4></label>\r\n"
					+ "                        <input type=\"text\" class=\"form-control\" name=\"eventNameEN\"\r\n"
					+ "                               id=\"eventNameEN\"  required=\"required\" placeholder=\"Event Name\" value=\""
					+ ev.publicEventName + "\">\r\n" + "                    </div>\r\n" + "                </div>\r\n"
					+ "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-12\">\r\n"
					+ "                        <label for=\"eventDescriptionEN\"><h4>Event Description (English)</h4></label>\r\n"
					+ "                        <textarea rows=\"4\" style=\"resize: none;\" class=\"form-control\" name=\"eventDescriptionEN\"\r\n"
					+ "                                  id=\"eventDescriptionEN\"\r\n"
					+ "                                   required=\"required\" placeholder=\"Event Description\">"
					+ ev.eventDescription.getTranslationText(Language.EN) + "</textarea>\r\n"
					+ "                    </div>\r\n" + "                </div>\r\n"
					+ "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-12\">\r\n"
					+ "                        <label for=\"eventDescriptionES\"><h4>Event Description (Spanish)</h4></label>\r\n"
					+ "                        <textarea rows=\"4\" style=\"resize: none;\" class=\"form-control\" name=\"eventDescriptionES\"\r\n"
					+ "                                  id=\"eventDescriptionES\"\r\n"
					+ "                                  placeholder=\"Event Description\"  required=\"required\">"
					+ ev.eventDescription.getTranslationText(Language.ES) + "</textarea>\r\n"
					+ "                    </div>\r\n" + "                </div>\r\n"
					+ "               <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-12\">\r\n"
					+ "                        <label for=\"eventDescriptionEUS\"><h4>Event Description (Basque)</h4></label>\r\n"
					+ "                        <textarea  required=\"required\" rows=\"4\" style=\"resize: none;\" class=\"form-control\" name=\"eventDescriptionEUS\"\r\n"
					+ "                                  id=\"eventDescriptionEUS\"\r\n"
					+ "                                  placeholder=\"Event Description\">"
					+ ev.eventDescription.getTranslationText(Language.EUS) + "</textarea>\r\n"
					+ "                    </div>\r\n" + "                </div>\r\n"
					+ "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-2\">\r\n"
					+ "                        <label for=\"deadLineDate\"><h4>DeadLine Date</h4></label>\r\n"
					+ "                        <input  required=\"required\" type=\"datetime-local\" class=\"form-control\" name=\"deadLineDate\" id=\"deadLineDate\" value=\""
					+ ev.deadlineToString() + "\">\r\n" + "                    </div>\r\n"
					+ "                </div>\r\n" + "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-2\">\r\n"
					+ "                        <label for=\"resolveDate\"><h4>Resolve Date</h4></label>\r\n"
					+ "                        <input  required=\"required\" type=\"datetime-local\" required=\"required\" value=\""
					+ ev.resolveToString() + "\" class=\"form-control\" name=\"resolveDate\" id=\"resolveDate\">\r\n"
					+ "                    </div>\r\n" + "                </div>\r\n"
					+ "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-8\">\r\n"
					+ "                        <label for=\"tags\"><h4>Tags (Split by ;)</h4></label>\r\n"
					+ "                        <textarea  required=\"required\" rows=\"1\" style=\"resize: none;\" class=\"form-control\" name=\"eventtags\" id=\"tags\"\r\n"
					+ "                                  placeholder=\"Event tags\">" + ev.tagsToString()
					+ "</textarea>\r\n" + "                    </div>\r\n" + "                </div>\r\n"
					+ "                <input type=\"hidden\" name=\"eventid\" value=\"" + ev.getID() + "\">\r\n"
					+ "                <div class=\"form-group\">\r\n"
					+ "                    <div class=\"col-xs-12\">\r\n" + "                        <hr>\r\n"
					+ "                        <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
					+ "                                class=\"glyphicon glyphicon-ok-sign\"></i> Update\r\n"
					+ "                        </button>\r\n"
					+ "                        <button class=\" btn btn-lg\" type=\"reset\"><i\r\n"
					+ "                                class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
					+ "                        </button>\r\n" + "                    </div>\r\n"
					+ "                </div>";

			a = doc.select("form#update");
			a.html(s);

			if (ev.isCancelled()) {
				s = "<h3>The Event is already Cancelled</h3><input type=\"hidden\" name=\"eventID\" value=\""
						+ ev.getID() + "\"><button disabled class=\"btn btn-lg btn-danger\" type=\"submit\"><i\r\n"
						+ "class=\"glyphicon glyphicon-ok-sign\"></i> Cancel event</button>";
			} else if (ev.isResolved()) {
				s = "<h3>The Event is already resolved, you cannot cancel it</h3><input type=\"hidden\" name=\"eventID\" value=\""
						+ ev.getID() + "\"><button disabled class=\"btn btn-lg btn-danger\" type=\"submit\"><i\r\n"
						+ "class=\"glyphicon glyphicon-ok-sign\"></i> Cancel event</button>";
			} else {
				s = "<input type=\"hidden\" name=\"eventID\" value=\"" + ev.getID()
						+ "\"><button class=\"btn btn-lg btn-danger\" type=\"submit\"><i\r\n"
						+ "class=\"glyphicon glyphicon-ok-sign\"></i> Cancel event</button>";
			}

			a = doc.select("form#cancelID");
			a.html(s);
			return doc.html();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the admincreatequestion page.
	 * 
	 * @param ID of the event
	 * @return HTML code of the page
	 * @throws Exception exception
	 */
	public static String createQuestion(long ID) throws Exception {
		File input = new File("./resources/templates2/administration/adminCreateQuestion.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			String s = "<input type=\"hidden\" name=\"id\" value=\"" + ID + "\">";
			Elements a = doc.select("div#eventID");
			a.html(s);
			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the viewquestions page. It shows information about a
	 * question
	 * 
	 * @param q  question to show
	 * @param id idof the event
	 * @param ev event to which belongs the question
	 * @return HTML code of the page
	 */
	public static String viewQuestions(Question q, long id, Event ev) {
		File input = new File("./resources/templates2/administration/adminQuestionData.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			String s = "<h2>Question ID:</h2>\r\n" + "            <br>\r\n" + "            <h4>" + q.getId()
					+ "</h4>\r\n" + "            <hr>\r\n" + "            <h2>Question Name:</h2>\r\n"
					+ "            <br>\r\n" + "            <h4>" + q.translation.getDefaultText() + "</h4>\r\n"
					+ "            <hr>\r\n" + "            <h2>Mode</h2>\r\n" + "            <br>\r\n"
					+ "            <h4>" + q.getQuestionMode() + "</h4>\r\n" + "            <hr>\r\n"
					+ "            <h2>Result</h2>\r\n" + "            <br>\r\n" + "            <h4>" + q.getAnswer()
					+ "</h4>\r\n" + "            <hr>\r\n" + "            <h2>Profit</h2>\r\n" + "            <br>\r\n"
					+ "            <h4>" + q.getProfit() + "</h4>\r\n" + "            <br>\r\n"
					+ "            <h2>Bet amount</h2>\r\n" + "            <br>\r\n" + "            <h4>"
					+ q.getBetAmount() + "</h4>\r\n" + "            <br>\r\n" + "            <h2>User Wins %</h2>\r\n"
					+ "            <br>\r\n" + "            <h4>" + q.getUserWinrate() * 100 + "%</h4>\r\n"
					+ "            <br>\r\n" + "            <h2>Answered</h2>\r\n" + "            <br>\r\n"
					+ "            <h4>" + q.isAnswered() + "</h4>";
			Elements a = doc.select("div#questionInfo");
			a.html(s);
			s = "";
			if (q.getQuestionMode().name().equals("UseValidSet")) {
				s = "<div class=\"panel panel-default\"><div class=\"panel-heading\">\r\n"
						+ "            <h1>Set values</h1><script>$(document).ready(function () {\r\n"
						+ "        $('#table_id').DataTable();\r\n" + "    });</script> </div>\r\n"
						+ "        <div class=\"panel-body\">\r\n"
						+ "            <table id=\"table_id\" class=\"display\">\r\n" + "                <thead>\r\n"
						+ "                <tr>\r\n" + "                    <th>ID</th>\r\n"
						+ "                    <th>Name</th><th>Payout</th></tr></thead>\r\n"
						+ "                <tbody id=\"events\">\r\n";

				String h = "";
				List<String> p = q.getValidSet();
				for (int i = 0; i < p.size(); i++) {
					h += "<tr><td>" + i + "</td>\r\n" + "<td>" + p.get(i) + "</td>\r\n" + "<td>" + q.getPayoff(p.get(i))
							+ "</td>" + "                </tr>";
				}
				s += h;
				s += " </tbody>\r\n" + " </table>\r\n" + " </div></div>";

			} else {
				String expectedResult = "";
				for (int i = 0; i < q.getExpectedResult().length - 1; i++) {
					expectedResult += q.getExpectedResult()[i] + "-";
				}
				expectedResult += q.getExpectedResult()[q.getExpectedResult().length - 1];
				s = "<div class=\"panel panel-default\" id=\"preview2\">\r\n"
						+ "            <div class=\"panel-heading\">\r\n"
						+ "                <h1>Numeric Values</h1>\r\n" + "            </div>\r\n"
						+ "            <div class=\"panel-body\" id=\"questionInfo\">\r\n"
						+ "                <h2>Length:</h2>\r\n" + "                <br>\r\n" + "                <h4>"
						+ q.getAnswerLength() + "</h4>\r\n" + "                <hr>\r\n"
						+ "                <h2>Expected result:</h2>\r\n" + "                <br>\r\n"
						+ "                <h4>" + expectedResult + "</h4>\r\n" + "                <hr>\r\n"
						+ "                <h2>Maximum Score Range:</h2>\r\n" + "                <br>\r\n"
						+ "                <h4>" + q.getMaxScoreRange() + "</h4>\r\n" + "            </div>\r\n"
						+ "        </div>";
			}

			a = doc.select("div#values");
			a.html(s);

			s = "";

			int i = 0;
			for (BetSuperClass b : q.getBets()) {
				s += "<tr><td>" + b.getId() + "</td>\r\n" + "<td>" + b.getUsername() + "</td>\r\n" + "<td>"
						+ b.getPrediction() + "</td>\r\n" + "<td>" + b.getBetAmount() + "</td>\r\n" + "<td>"
						+ b.getPayoff() + "</td>\r\n" + "<td>" + b.getFinalPay() + "</td>\r\n" + "<td>" + b.isWon()
						+ "</td></tr>";
				i++;
				if (i == 300) {
					break;
				}
			}
			a = doc.select("tbody#bet");
			a.html(s);
			if (!ev.isCancelled()) {
				if (!q.isAnswered()) {
					if (q.getQuestionMode().name().equals("UseValidSet")) {
						String s3 = "<select class=\"form-control\" name=\"answer\" id=\"answer\">";
						List<String> p = q.getValidSet();
						for (int j = 0; j < p.size(); j++) {
							s3 += "<option value=\"" + p.get(j) + "\">" + p.get(j) + "</option>";
						}
						s3 += "</select>";
						s = "<input type=\"hidden\" name=\"eventID\" value=\"" + id
								+ "\"><input type=\"hidden\" name=\"questionID\" value=\"" + q.getId() + "\">\r\n"
								+ "                    <input type=\"hidden\" name=\"questionType\" value=\"UseValidSet\">\r\n"
								+ "                    <div class=\"form-group\">\r\n"
								+ "                        <div class=\"col-xs-12\">\r\n"
								+ "                            <label for=\"answer\"><h4>Answer the question</h4></label>"
								+ s3 + "                        </div>\r\n" + "                    </div>\r\n"
								+ "                    <div class=\"form-group\">\r\n"
								+ "                        <div class=\"col-xs-6\">\r\n"
								+ "                            <hr>\r\n"
								+ "                            <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
								+ "                                    class=\"glyphicon glyphicon-ok-sign\"></i> Answer\r\n"
								+ "                            </button>\r\n"
								+ "                            <button class=\" btn btn-lg\" type=\"reset\"><i\r\n"
								+ "                                    class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
								+ "                            </button>\r\n" + "                        </div>\r\n"
								+ "                    </div>";
					} else {
						String s4 = "";
						for (int k = 0; k < q.getAnswerLength(); k++) {
							s4 += "<input required type=\"number\" style=\"margin-top:10px\" class=\"form-control\" name=\"answer\" id=\"answer\"\r\n"
									+ "                                   placeholder=\"Answer\"\r\n"
									+ "                                   value=\"\">\r\n";
						}
						s = "<input type=\"hidden\" name=\"eventID\" value=\"" + id
								+ "\"><input type=\"hidden\" name=\"questionID\" value=\"" + q.getId() + "\">\r\n"
								+ "                    <input type=\"hidden\" name=\"questionType\" value=\"NumericValue\">\r\n"
								+ "                    <input type=\"hidden\" name=\"questionLength\" value=\""
								+ q.getAnswerLength() + "\">\r\n" + "                    <div class=\"form-group\">\r\n"
								+ "                        <div class=\"col-xs-6\">\r\n"
								+ "                            <label for=\"answer\"><h4>Answer the question</h4></label>\r\n"
								+ s4 + "                        </div>\r\n" + "                    </div>\r\n"
								+ "                    <div class=\"form-group\">\r\n"
								+ "                        <div class=\"col-xs-12\">\r\n"
								+ "                            <hr>\r\n"
								+ "                            <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
								+ "                                    class=\"glyphicon glyphicon-ok-sign\"></i> Answer\r\n"
								+ "                            </button>\r\n"
								+ "                            <button class=\" btn btn-lg\" type=\"reset\"><i\r\n"
								+ "                                    class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
								+ "                            </button>\r\n" + "                        </div>\r\n"
								+ "                    </div>";
					}
				} else {
					s = "<h1>The Question is already answered</h1>";
				}

			} else {
				s = "<h1>The event has been cancelled</h1>";
			}

			a = doc.select("form#solveQuestion");
			a.html(s);
			return doc.html();

		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * This function rewrites the eventsettings page. Not used in the end.
	 * @param blP private business logic
	 * @param errorMessage message error
	 * @return HTML code of the page
	 */
	public static String rewriteEventSettings(BLFacadePrivate blP, String errorMessage) {
		File input = new File("./resources/templates2/administration/adminEventSettings.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements connected = doc.select("div.panel-group");

			Hashtable<String, List<String>> popularEvents = blP.getEvents().retrievePopularEventsTags();
			String l = "";
			int i = 1;
			for (String s : popularEvents.keySet()) {
				List<String> pTags = popularEvents.get(s);
				if (pTags.size() == 0) {
					pTags.add("");
					pTags.add("");
				} else if (pTags.size() == 1) {
					pTags.add("");
				}
				l += "<div class=\"panel panel-default\">\r\n" + "                    <div class=\"panel-heading\">\r\n"
						+ "                        <h4 class=\"panel-title\">\r\n"
						+ "                            <a data-toggle=\"collapse\" href=\"#collapse" + i + "\">" + s
						+ "</a>\r\n" + "                        </h4>\r\n" + "                    </div>\r\n"
						+ "                    <div id=\"collapse" + i + "\" class=\"panel-collapse collapse\">\r\n"
						+ "                        <div class=\"panel-body\">\r\n"
						+ "                            <form id=\"sport1\" method=\"post\" action=\"/administration/updateEventSettings\">\r\n"
						+ "                                <div class=\"form-group\">\r\n"
						+ "                                    <div class=\"col-xs-12\">\r\n"
						+ "                                        <label for=\"Sport1Settings\"><h4>Label 1</h4></label>\r\n"
						+ "                                        <input name=\"Team1\" class=\"form-control\" id=\"Sport1Settings\"\r\n"
						+ "                                                  placeholder=\"Label 1\" value=\""
						+ pTags.get(0) + "\" >\r\n"
						+ "                                        <label for=\"Sport2Settings\"><h4>Label 2</h4></label>\r\n"
						+ "                                        <input name=\"Team2\" class=\"form-control\" id=\"Sport2Settings\"\r\n"
						+ "                                                  placeholder=\"Label 2\" value=\""
						+ pTags.get(1) + "\" >\r\n" + "<input type=\"hidden\" value=\"" + s + "\" name=\"SportName\">"
						+ "                                    </div>\r\n"
						+ "                                </div>\r\n"
						+ "                                <div class=\"form-group\">\r\n"
						+ "                                    <div class=\"col-xs-12\">\r\n"
						+ "                                        <hr>\r\n"
						+ "                                        <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
						+ "                                                class=\"glyphicon glyphicon-ok-sign\"></i> Update\r\n"
						+ "                                        </button>\r\n"
						+ "                                        <button class=\" btn btn-lg\" type=\"reset\"><i\r\n"
						+ "                                                class=\"glyphicon glyphicon-repeat\"></i> Reset\r\n"
						+ "                                        </button>\r\n"
						+ "                                    </div>\r\n"
						+ "                                </div>\r\n" + "                            </form>\r\n"
						+ "                        </div>\r\n" + "                    </div>\r\n"
						+ "                </div>";
				i += 1;
			}
			connected.html(l);

			Elements p = doc.select("div#error");
			p.html(errorMessage);

			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function returns the html code a requested page
	 * @param WEB_ROOT the root where is located
	 * @param filePath path of the file
	 * @return HTML code of the page.
	 */
	public static String readFile(File WEB_ROOT, String filePath) {
		File supportInfo = new File(WEB_ROOT, filePath);
		String s = "";
		Scanner scanner = null;
		try {
			scanner = new Scanner(supportInfo);
			while (scanner.hasNextLine()) {
				s += scanner.nextLine();
			}
			scanner.close();
		} catch (Exception e) {
			return "";
		}
		return s;
	}

}
