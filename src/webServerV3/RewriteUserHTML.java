package webServerV3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import businessLogicV3.publicAPI.BLFacadePublic;
import domainV2.User;
import domainV2.bet.BetSuperClass;
import domainV2.event.Event;
import domainV2.event.EventTag;
import domainV2.question.Question;
import domainV2.util.Language;
import domainV2.News;
import domainV2.Transaction;

public class RewriteUserHTML {
	/**
	 * This function rewrites the user password recovery page
	 * 
	 * @param code         unique code given to the user to reset the password
	 * @param language     language used by the user
	 * @param bl           public business logic
	 * @param errorMessage error message shown
	 * @return HTML code of the page
	 */
	public static String rewriteUserPasswordRecovery(String code, String language, BLFacadePublic bl,
			String errorMessage) {
		File input = new File("./resources/templates2/" + language + "/PasswordRecoveryAttempt.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements connected = doc.select("div#code");
			String s = "<input type=\"hidden\" name=\"code\" value=\"" + code + "\">";
			connected.html(s);
			connected = doc.select("div#info");
			s = bl.getOthers().readSupportInfo(language);
			connected.html(s);
			Elements error = doc.select("div#error");
			error.html(errorMessage);
			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the password recovery ask
	 * 
	 * @param language language that the user is using
	 * @param bl       public business logic
	 * @return HTML code of the page
	 */
	public static String rewritePasswordRecoveryAsk(String language, BLFacadePublic bl) {
		File input = new File("./resources/templates2/" + language + "/PasswordRecovery.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements connected = doc.select("div#info");
			String s = bl.getOthers().readSupportInfo(language);
			connected.html(s);
			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the sign up
	 * 
	 * @param language     language that the user is using
	 * @param bl           public business logic
	 * @param errorMessage message shown to the user
	 * @return HTML code of the page
	 */
	public static String rewriteSignUp(String language, BLFacadePublic bl, String errorMessage) {
		File input = new File("./resources/templates2/" + language + "/SignUp.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements connected = doc.select("div#info");
			String s = bl.getOthers().readSupportInfo(language);
			connected.html(s);

			Elements error = doc.select("div#error");
			error.html(errorMessage);
			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the landing page
	 * 
	 * @param language language used by the user
	 * @param bl       public business logic
	 * @return HTML code of the page
	 */
	public static String RewriteLandingPage(String language, BLFacadePublic bl) {
		File input = new File("./resources/templates2/" + language + "/LandingPage.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements connected = doc.select("div#info");
			String s = bl.getOthers().readSupportInfo(language);
			connected.html(s);
			List<News> lastNews = bl.getNews().getLatestNews(3, language);
			s = "<div class=\"item active\">";
			int i = 0;
			for (News nw : lastNews) {
				s += "<img src=\"" + nw.getPicture() + "\"\r\n" + "                             alt=\"Image1\">\r\n"
						+ "                        <div class=\"carousel-caption\">\r\n"
						+ "                            <h3>" + nw.getHead() + "</h3>\r\n"
						+ "                            <p>" + nw.getContent() + "</p>\r\n"
						+ "                        </div>\r\n" + "                    </div>";
				i++;
				if (!(i == lastNews.size())) {
					s += "<div class=\"item\">";
				}
			}

			Elements news = doc.select("div#news");
			news.html(s);

			return doc.html();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * This function rewrites the points store
	 * 
	 * @param user     user which data needs to be updated
	 * @param language language used by the user
	 * @return HTML code of the page
	 */
	public static String pointsStore(User user, String language) {
		Locale locale = new Locale(language);
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas", locale);
		File input = new File("./resources/templates2/" + language + "/userPurchasePoints/pointsMain.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
			String s = "";
			s = s + "<h5>" + bundle.getString("AddingPoints") + "</h5>" + "<p>" + bundle.getString("Username") + ": "
					+ user.getName() + "</p>" + "<p>" + bundle.getString("BillingAddress") + ": " + user.getAddress()
					+ "</p>" + "<p id=\"modalMoneyInfo\"> " + bundle.getString("AmountToBePaid") + ": </p>"
					+ "<p id=\"modalPointsInfo\"> " + bundle.getString("PointsToBeAdded") + ": </p> " + "<p>"
					+ bundle.getString("Date") + ": " + timeStamp + "</p>";

			String s2 = "";
			s2 = s2 + user.getWalletFunds();

			Element a = doc.getElementById("modalInformation");
			a.html(s);

			Element b = doc.getElementById("currentPoints");
			b.html(s2);
			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the cash out page
	 * 
	 * @param user     user that asked for the page
	 * @param language language used by the user
	 * @return HTML code of the page
	 */
	public static String cashoutRewrite(User user, String language) {
		File input = new File("./resources/templates2/" + language + "/userPurchasePoints/cashout.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");

			String s2 = "";
			s2 = s2 + user.getWalletFunds();

			Element b = doc.getElementById("currentPoints");
			b.html(s2);
			Element c = doc.getElementById("pointsValue");
			String s3 = "";
			s3 += user.getWalletFunds() / 10;
			c.html(s3);

			Element a = doc.getElementById("pointsLimit");
			a.html(s2);

			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the profile info page of a user
	 * 
	 * @param user     user which profile is going to be rewritten
	 * @param language language used by the user
	 * @return HTML code of the page
	 */
	public static String rewriteProfileInfo(User user, String language) {
		File input = new File("./resources/templates2/" + language + "/Profile/userProfile.html");
		Locale locale = new Locale(language);
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas", locale);
		try {
			Calendar cal = user.getBirthDate();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String formatted = format1.format(cal.getTime());
			Document doc = Jsoup.parse(input, "UTF-8");
			String s = "";
			s = s + "<tr>" + "<th scope=\"row\">" + bundle.getString("Username") + "</th>" + "<td>" + user.getUsername()
					+ "</td></tr><tr>" + "<th scope=\"row\">Name</th>" + "<td>" + user.getName() + "</td></tr><tr>"
					+ "<th scope=\"row\">Surname</th>" + "<td>" + user.getSurname() + "</td></tr><tr>"
					+ "<th scope=\"row\">" + bundle.getString("BirthDate") + "</th>" + "<td>" + formatted
					+ "</td></tr><tr>" + "<th scope=\"row\">" + bundle.getString("Email") + "</th>" + "<td>"
					+ user.getEmail() + "</td></tr><tr>" + "<th scope=\"row\">" + bundle.getString("PhoneNumber")
					+ "</th>" + "<td>" + user.getPhoneNumber() + "</td></tr><tr>" + "<th scope=\"row\">"
					+ bundle.getString("Country") + "</th>" + "<td>" + user.getCountry() + "</td></tr><tr>"
					+ "<th scope=\"row\">" + bundle.getString("City") + "</th>" + "<td>" + user.getCity()
					+ "</td></tr><tr>" + "<th scope=\"row\">" + bundle.getString("Address") + "</th>" + "<td>"
					+ user.getAddress() + "</td></tr><tr>" + "<th scope=\"row\">" + bundle.getString("WalletFunds")
					+ "</th>" + "<td>" + user.getWalletFunds() + "</td></tr><tr>" + "<th scope=\"row\">"
					+ bundle.getString("FavouriteSports") + "</th>" + "<td>" + user.favouriteSportsToString()
					+ "</td></tr><tr>" + "<th scope=\"row\">" + bundle.getString("FavouriteTeams") + "</th>" + "<td>"
					+ user.favouriteTeamsToString() + "</td></tr>";

			String s2 = "";
			s2 = s2 + user.getWalletFunds();

			String s3 = "";
			s3 = s3 + "<h1>" + user.getUsername() + "</h1>" + "<h4>" + user.getName() + " " + user.getSurname()
					+ "</h4>" + "<span>" + user.getCity() + "</span>";

			Element a = doc.getElementById("userInformation");
			a.html(s);

			Element b = doc.getElementById("currentPoints");
			b.html(s2);

			Element c = doc.getElementById("headingInfo");
			c.html(s3);

			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the transaction page of a user
	 * @param user user which transactions are shown
	 * @param transactions list with transactions of the user
	 * @param language language used by the user
	 * @return HTML code of the page
	 */
	public static String updateUserPayments(User user, List<Transaction> transactions, String language) {
		File input = new File("./resources/templates2/" + language + "/userPurchasePoints/userPayments.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			StringBuilder s = new StringBuilder();
			for (Transaction tr : transactions) {
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				fmt.setCalendar(tr.getDate());
				String dateFormatted = fmt.format(tr.getDate().getTime());
				if (tr.getType().equals("OUT")) {
					s.append("<tr class=\"danger\">\r\n" + "                        <td>" + tr.getId() + "</td>\r\n"
							+ "                        <td>" + tr.getMessage() + "</td>\r\n"
							+ "                        <td>-" + tr.getAmount() + "</td>\r\n"
							+ "                        <td>" + dateFormatted + "</td>\r\n"
							+ "                    </tr>");
				} else if (tr.getType().equals("IN")) {
					s.append("<tr class=\"success\">\r\n" + "                        <td>" + tr.getId() + "</td>\r\n"
							+ "                        <td>" + tr.getMessage() + "</td>\r\n"
							+ "                        <td>+" + tr.getAmount() + "</td>\r\n"
							+ "                        <td>" + dateFormatted + "</td>\r\n"
							+ "                    </tr>");
				}
			}

			String s2 = "";
			s2 = s2 + user.getWalletFunds();

			Element a = doc.getElementById("paymentHistory");
			a.html(s.toString());

			Element b = doc.getElementById("currentPoints");
			b.html(s2);

			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the settings page
	 * 
	 * @param user     user which settings are shown
	 * @param message  shown when updating
	 * @param language language of the user
	 * @return HTML code of the page
	 */
	public static String settingsLaunch(User user, String message, String language) {
		File input = new File("./resources/templates2/" + language + "/Profile/settings.html");
		Locale locale = new Locale(language);
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas", locale);
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			StringBuilder gender = new StringBuilder();
			if (user.getSex().equals("Male")) {
				gender.append("<option value=\"Male\">" + bundle.getString("Male") + "</option>"
						+ "<option value=\"Female\">" + bundle.getString("Female") + "</option>"
						+ "<option value=\"Other\">" + bundle.getString("Other") + "</option>");
			} else if (user.getSex().equals("Female")) {
				gender.append("<option value=\"Female\">" + bundle.getString("Female") + "</option>"
						+ "<option value=\"Male\">" + bundle.getString("Male") + "</option>"
						+ "<option value=\"Other\">" + bundle.getString("Other") + "</option>");
			} else {
				gender.append("<option value=\"Other\">" + bundle.getString("Other") + "</option>"
						+ "<option value=\"Female\">" + bundle.getString("Female") + "</option>"
						+ "<option value=\"Male\">" + bundle.getString("Male") + "</option>");
			}

			String s = "";
			s = s + "<div class=\"row\">" + "                <h1>" + bundle.getString("Settings") + "</h1>"
					+ "<span  id=\"update\"><h2><font color=\"green\"></font></h2></span>" + "                <hr>"
					+ "                <h3 class=\"form-title text-left\">" + bundle.getString("ChangeInformation")
					+ "</h3>"
					+ "                <form class=\"form\" data-toggle=\"validator\" action=\"/updateinfo\" method=\"POST\" id=\"updateInfo\">"
					+ "                    <div class=\"form-group has-feedback\">"
					+ "                        <div class=\"col-xs-6\">"
					+ "                            <label for=\"first_name\">" + "                                <h4>"
					+ bundle.getString("FirstName") + "</h4>" + "                            </label>"
					+ "                            <input type=\"text\" class=\"form-control\" name=\"first_name\" id=\"first_name\""
					+ "                                placeholder=\"" + bundle.getString("FirstName") + "\" value="
					+ user.getName() + " required>"
					+ "                            <span class=\"glyphicon form-control-feedback\" aria-hidden=\"true\"></span>"
					+ "                            <div class=\"help-block with-errors\"></div>"
					+ "                        </div>" + "                    </div>"
					+ "                    <div class=\"form-group has-feedback\">"
					+ "                        <div class=\"col-xs-6\">"
					+ "                            <label for=\"last_name\">" + "                                <h4>"
					+ bundle.getString("LastName") + "</h4>" + "                            </label>"
					+ "                            <input type=\"text\" class=\"form-control\" name=\"last_name\" id=\"last_name\""
					+ "                                placeholder=\"" + bundle.getString("LastName") + "\" value="
					+ user.getSurname() + " required>"
					+ "                            <span class=\"glyphicon form-control-feedback\" aria-hidden=\"true\"></span>"
					+ "                            <div class=\"help-block with-errors\"></div>"
					+ "                        </div>" + "                    </div>"
					+ "                    <div class=\"form-group has-feedback\">"
					+ "                        <div class=\"col-xs-6\">"
					+ "                            <label for=\"phone\">" + "                                <h4>"
					+ bundle.getString("PhoneNumber") + "</h4>" + "                            </label>"
					+ "                            <input type=\"tel\" class=\"form-control\" name=\"phone\" id=\"phone\" placeholder=\""
					+ bundle.getString("PhoneNumber") + "\"" + "                                value="
					+ user.getPhoneNumber() + " pattern=\"^(:?([+]*[0-9]{1,4}[\" \"]{1}))*[0-9]{4,13}$\""
					+ "                                maxlength=\"15\" required>"
					+ "                            <span class=\"glyphicon form-control-feedback\" aria-hidden=\"true\"></span>"
					+ "                            <div class=\"help-block with-errors\"></div>"
					+ "                        </div>" + "                    </div>"
					+ "                    <div class=\"form-group has-feedback\">"
					+ "                        <div class=\"col-xs-6\">"
					+ "                            <label for=\"sex\">" + "                                <h4>"
					+ bundle.getString("Sex") + "</h4>" + "                            </label>"
					+ "                            <select class=\"form-control\" name=\"sex\" id=\"sex\">"
					+ gender.toString() + "                            </select>"
					+ "                            <span class=\"glyphicon form-control-feedback\" aria-hidden=\"true\"></span>"
					+ "                            <div class=\"help-block with-errors\"></div>"
					+ "                        </div>" + "                    </div>"
					+ "                    <div class=\"form-group has-feedback\">"
					+ "                        <div class=\"col-xs-6\">"
					+ "                            <label for=\"email\">" + "                                <h4>"
					+ bundle.getString("Email") + "</h4>" + "                            </label>"
					+ "                            <input type=\"email\" class=\"form-control\" name=\"email\" id=\"email\" placeholder=\""
					+ bundle.getString("Email") + "\"" + "                                value=" + user.getEmail()
					+ " data-error=\"" + bundle.getString("ThatEmailAddressIsInvalid") + "\" required>"
					+ "                            <span class=\"glyphicon form-control-feedback\" aria-hidden=\"true\"></span>"
					+ "                            <div class=\"help-block with-errors\"></div>"
					+ "                        </div>" + "                    </div>"
					+ "                    <div class=\"form-group has-feedback\">"
					+ "                        <div class=\"col-xs-6\">"
					+ "                            <label for=\"country\">" + "                                <h4>"
					+ bundle.getString("Country") + "</h4>" + "                            </label>"
					+ "                            <input type=\"text\" class=\"form-control\" name=\"country\" id=\"country\" placeholder=\""
					+ bundle.getString("Country") + "\"" + "                                value=" + user.getCountry()
					+ " required>"
					+ "                            <span class=\"glyphicon form-control-feedback\" aria-hidden=\"true\"></span>"
					+ "                            <div class=\"help-block with-errors\"></div>"
					+ "                        </div>" + "                    </div>"
					+ "                    <div class=\"form-group has-feedback\">"
					+ "                        <div class=\"col-xs-6\">"
					+ "                            <label for=\"city\">" + "                                <h4>"
					+ bundle.getString("City") + "</h4>" + "                            </label>"
					+ "                            <input type=\"text\" class=\"form-control\" name=\"city\" id=\"city\" placeholder=\""
					+ bundle.getString("City") + "\"" + "                                value=" + user.getCity()
					+ " required>"
					+ "                            <span class=\"glyphicon form-control-feedback\" aria-hidden=\"true\"></span>"
					+ "                            <div class=\"help-block with-errors\"></div>"
					+ "                        </div>" + "                    </div>"
					+ "                    <div class=\"form-group has-feedback\">"
					+ "                        <div class=\"col-xs-6\">"
					+ "                            <label for=\"address\">" + "                                <h4>"
					+ bundle.getString("Address") + "</h4>" + "                            </label>"
					+ "                            <input type=\"text\" class=\"form-control\" name=\"address\" id=\"address\" placeholder=\""
					+ bundle.getString("Address") + "\"" + "                                value=" + user.getAddress()
					+ " required>"
					+ "                            <span class=\"glyphicon form-control-feedback\" aria-hidden=\"true\"></span>"
					+ "                            <div class=\"help-block with-errors\"></div>"
					+ "                        </div>" + "                    </div>"
					+ "                    <div class=\"form-group has-feedback\">"
					+ "                        <div class=\"col-xs-12\">"
					+ "                            <label for=\"favouriteSports\">"
					+ "                                <h4>" + bundle.getString("FavouriteSports") + "</h4>"
					+ "                                <h5>" + bundle.getString("SeparateBy") + "</h5>"
					+ "                            </label>"
					+ "                            <textarea rows=\"5\" style=\"resize: none\" class=\"form-control\" name=\"favouriteSports\""
					+ "                                id=\"favouriteSports\" placeholder=\""
					+ bundle.getString("FavouriteSports") + "\">" + user.favouriteSportsToString() + "</textarea>\r\n"
					+ "                            <span class=\"glyphicon form-control-feedback\" aria-hidden=\"true\"></span>\r\n"
					+ "                            <div class=\"help-block with-errors\"></div>\r\n"
					+ "                        </div>\r\n" + "                    </div>\r\n"
					+ "                    <div class=\"form-group has-feedback\">\r\n"
					+ "                        <div class=\"col-xs-12\">\r\n"
					+ "                            <label for=\"favouriteTeams\">\r\n"
					+ "                                <h4>" + bundle.getString("FavouriteTeams") + "</h4>\r\n"
					+ "                                <h5>" + bundle.getString("SeparateBy") + "</h5>\r\n"
					+ "                            </label>\r\n"
					+ "                            <textarea rows=\"5\" style=\"resize: none\" class=\"form-control\" name=\"favouriteTeams\"\r\n"
					+ "                                id=\"favouriteTeams\" placeholder=\""
					+ bundle.getString("FavouriteTeams") + "\">" + user.favouriteTeamsToString() + "</textarea>\r\n"
					+ "                            <span class=\"glyphicon form-control-feedback\" aria-hidden=\"true\"></span>\r\n"
					+ "                            <div class=\"help-block with-errors\"></div>\r\n"
					+ "                        </div>\r\n" + "                    </div>\r\n"
					+ "                    <input type=\"hidden\" name=\"username\" value=\"Elenaf\">\r\n"
					+ "                    <div class=\"form-group\">\r\n"
					+ "                        <div class=\"col-xs-12\">\r\n" + "                            <br>\r\n"
					+ "                            <button class=\"btn btn-lg btn-success\" type=\"submit\"><i\r\n"
					+ "                                    class=\"glyphicon glyphicon-ok-sign\"></i> "
					+ bundle.getString("Update") + "\r\n" + "                            </button>\r\n"
					+ "                        </div>\r\n" + "                    </div>\r\n"
					+ "                </form>\r\n" + "            </div>\r\n" + "            </div>";

			String s2 = "";
			s2 = s2 + user.getWalletFunds();

			Element a = doc.getElementById("settingsInfo");
			a.html(s);

			Element b = doc.getElementById("currentPoints");
			b.html(s2);

			if (!message.equals("")) {
				String s3 = "";
				s3 = s3 + "<h2>" + message + "</h2>";
				Element c = doc.getElementById("update");
				c.html(s3);
			}

			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the betinfo page.
	 * 
	 * @param user     user which bets are going to be shown
	 * @param uBets    list of bets of the user
	 * @param bl       public business logic
	 * @param language language that the user is using
	 * @return HTML code of the page
	 */
	public static String betInfo(User user, List<BetSuperClass> uBets, BLFacadePublic bl, String language) {
		File input = new File("./resources/templates2/" + language + "/Profile/userBets.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");

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
							+ "                                        <td>" + uBet.getFinalPay() + "</td>" + "<td>"
							+ uBet.isWon() + "</td><td>" + ev.isResolved() + "</td></tr>";
				}
			}
			Elements bet = doc.select("tbody#betInfo");
			bet.html(bets);

			String s2 = "";
			s2 = s2 + user.getWalletFunds();

			Element c = doc.getElementById("currentPoints");
			c.html(s2);

			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the change main page
	 * 
	 * @param user               data of the user which page is for
	 * @param eventTypeMenu      type of menu
	 * @param eventType          type of the event
	 * @param eventSpecification specification of the event
	 * @param events             that are going to be showed
	 * @param firstTagGroup      events corresponding to the first tag
	 * @param secondTagGroup     events corresponding to the second tag
	 * @param firstTag           first tag
	 * @param secondTag          second tag
	 * @param language           language used by the user
	 * @return HTML code of the page
	 */
	public static String rewriteMainpage(User user, String eventTypeMenu, String eventType, String eventSpecification,
			List<Event> events, List<Event> firstTagGroup, List<Event> secondTagGroup, String firstTag,
			String secondTag, String language) {
		File input = new File("./resources/templates2/" + language + "/mainPage.html");
		Locale locale = new Locale(language);
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas", locale);
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			String s = "";
			String isVisible = "style=\"display: none;\"";
			int eventsPerPage = 8;

			StringBuilder s3 = new StringBuilder();
			int pages = (int) Math.ceil((double) events.size() / eventsPerPage);
			s3.append("<ul class=\"pagination\">\r\n");
			for (int i = 1; i < pages + 1; i++) {
				if (i == 1) {
					s3.append("                    <li class=\"active\"><a href=\"/page" + i + "\">" + i
							+ "</a></li>\r\n");
				} else {
					s3.append("                    <li><a href=\"/page" + i + "\">" + i + "</a></li>\r\n");
				}
			}
			s3.append("                  </ul>");

			StringBuilder body = new StringBuilder();
			String color = "";
			String icon = "";
			int eventCounter = 0;
			for (Event e : events) {
				if (eventCounter < eventsPerPage) {
					if (e.getTags().contains(EventTag.Soccer)) {
						color = "#7cdb66";
						icon = "fa-futbol";
					} else if (e.getTags().contains(EventTag.Basketball)) {
						color = "peru";
						icon = "fa-basketball-ball";
					} else if (e.getTags().contains(EventTag.Football)) {
						color = "#d15c36";
						icon = "fa-football-ball";
					} else if (e.getTags().contains(EventTag.Cycling)) {
						color = "#e2e916";
						icon = "fa-bicycle";
					} else if (e.getTags().contains(EventTag.Baseball)) {
						color = "#70d4ff";
						icon = "fa-baseball-ball";
					}
					body.append("<div class=\"panel-conteiner\">\n"
							+ "                <div class=\"panel panel-default\">\n"
							+ "                        <div class=\"panel-heading text-center\">" + e.publicEventName
							+ "</div>\n" + "                        <div class=\"row\">\n"
							+ "                            <div class=\"panel-body\">\n"
							+ "                                    <div class=\"col-md-2\" >\n"
							+ "                                        \n"
							+ "                                            <i class=\"fas " + icon
							+ " fa-7x eventIcon\" style=\"color:" + color + ";\"></i> \n"
							+ "                                        \n"
							+ "                                    </div>\n"
							+ "                                <div class=\"col-md-10\">\n"
							+ "                                    <div class=\"panelText\">\n"
							+ "                                    <p>"
							+ e.eventDescription.getTranslationText(Language.valueOf(language)) + "</p>\n"
							+ "                                        <p>Deadline " + e.deadlineToString() + "</p>\n"
							+ "                                        <p>Resolve " + e.resolveToString() + "</p>\n"
							+ "                                    </div>\n"
							+ "                                    <form role=\"form\" method=\"POST\" action=\"/makebets\">\n"
							+ "                                        <div class=\"form-group\" >\n"
							+ "                                        <input id=\"eventID\" type=\"hidden\" name=\"eventID\" value=\""
							+ e.getID() + "\">\n" + "                                        </div>\n"
							+ "                                        <button class=\"btn panelBtn btn-primary\" type=\"submit\" style=\"background-color:"
							+ color + "; border-color: #000; color: white\">" + bundle.getString("ViewQuestions")
							+ "</button>\n" + "                                    </form>\n"
							+ "                                </div>\n" + "                            </div>\n"
							+ "                                \n"
							+ "                                                            \n" + "\n"
							+ "                            </div>\n" + "       </div>      </div>");
					eventCounter += 1;
				}
			}

			if (eventTypeMenu.equals("Sports")) {
				isVisible = "";
				s = s + "<div class=\"nav-side-menu\"" + isVisible
						+ "><i class=\"fa fa-bars fa-2x toggle-btn\" data-toggle=\"collapse\" data-target=\"#menu-content\"></i>\r\n"
						+ "                    <div class=\"menu-list\">\r\n"
						+ "                        <ul id=\"menu-content\" class=\"menu-content collapse out\">\r\n"
						+ "\r\n"
						+ "                            <li data-toggle=\"collapse\" data-target=\"#Soccer\" class=\"collapsed\">\r\n"
						+ "                                <a href=\"#\"><i class=\"fas fa-futbol\"></i> "
						+ bundle.getString("Soccer") + " <span class=\"arrow\"></span></a>\r\n"
						+ "                            </li>\r\n"
						+ "                            <ul class=\"sub-menu collapse\" id=\"Soccer\">\r\n"
						+ "                                <li><a href=\"/soccer/competitions\">"
						+ bundle.getString("Competitions") + "</a></li>\r\n" + "                            </ul>\r\n"
						+ "\r\n"
						+ "                            <li data-toggle=\"collapse\" data-target=\"#Basketball\" class=\"collapsed\">\r\n"
						+ "                                <a href=\"#\"><i class=\"fas fa-basketball-ball\"></i> "
						+ bundle.getString("Basketball") + " <span\r\n"
						+ "                                        class=\"arrow\"></span></a>\r\n"
						+ "                            </li>\r\n"
						+ "                            <ul class=\"sub-menu collapse\" id=\"Basketball\">\r\n"
						+ "                                <li><a href=\"/basketball/competitions\">"
						+ bundle.getString("Competitions") + "</a></li>\r\n" + "                            </ul>\r\n"
						+ "\r\n" + "\r\n"
						+ "                            <li data-toggle=\"collapse\" data-target=\"#Football\" class=\"collapsed\">\r\n"
						+ "                                <a href=\"#\"><i class=\"fas fa-football-ball\"></i> "
						+ bundle.getString("Football") + " <span\r\n"
						+ "                                        class=\"arrow\"></span></a>\r\n"
						+ "                            </li>\r\n"
						+ "                            <ul class=\"sub-menu collapse\" id=\"Football\">\r\n"
						+ "                                <li><a href=\"/football/competitions\">"
						+ bundle.getString("Competitions") + "</a></li>\r\n" + "                            </ul>\r\n"
						+ "\r\n"
						+ "                            <li data-toggle=\"collapse\" data-target=\"#Cycling\" class=\"collapsed\">\r\n"
						+ "                                <a href=\"#\"><i class=\"fas fa-bicycle\"></i> "
						+ bundle.getString("Cycling") + " <span class=\"arrow\"></span></a>\r\n"
						+ "                            </li>\r\n"
						+ "                            <ul class=\"sub-menu collapse\" id=\"Cycling\">\r\n"
						+ "                                <li><a href=\"/cycling/competitions\">"
						+ bundle.getString("Competitions") + "</a></li>\r\n" + "                            </ul>\r\n"
						+ "\r\n"
						+ "                            <li data-toggle=\"collapse\" data-target=\"#Baseball\" class=\"collapsed\">\r\n"
						+ "                                <a href=\"#\"><i class=\"fas fa-baseball-ball\"></i> "
						+ bundle.getString("Baseball") + " <span\r\n"
						+ "                                        class=\"arrow\"></span></a>\r\n"
						+ "                            </li>\r\n"
						+ "                            <ul class=\"sub-menu collapse\" id=\"Baseball\">\r\n"
						+ "                                <li><a href=\"/baseball/competitions\">"
						+ bundle.getString("Competitions") + "</a></li>\r\n" + "                            </ul>\r\n"
						+ "\r\n" + "                        </ul>\r\n" + "                    </div></div>";
			}

			String s2 = "";
			s2 = s2 + user.getWalletFunds();

			Element a = doc.getElementById("eventType");
			a.html(eventType);

			if (!eventSpecification.equals("")) {
				Element b = doc.getElementById("eventSpecification");
				b.html(eventSpecification);
			}

			Element c = doc.getElementById("currentPoints");
			c.html(s2);

			Element d = doc.getElementById("submenu");
			d.html(s);

			Element e = doc.getElementById("eventContent");
			e.html(body.toString());

			Element f = doc.getElementById("pagination");
			f.html(s3.toString());
			return doc.html();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * This function rewrites the change main page
	 * 
	 * @param user               data of the user which page is for
	 * @param eventTypeMenu      type of menu
	 * @param eventType          type of the event
	 * @param eventSpecification specification of the event
	 * @param events             that are going to be showed
	 * @param firstTagGroup      events corresponding to the first tag
	 * @param secondTagGroup     events corresponding to the second tag
	 * @param firstTag           first tag
	 * @param secondTag          second tag
	 * @param language           language used by the user
	 * @param pageNr             number of the page
	 * @return HTML code of the page
	 */
	public static String changePageMainPage(User user, String eventTypeMenu, String eventType,
			String eventSpecification, List<Event> events, List<Event> firstTagGroup, List<Event> secondTagGroup,
			String firstTag, String secondTag, String language, int pageNr) {
		File input = new File("./resources/templates2/" + language + "/mainPageTemp.html");
		FileWriter fooWriter;
		Locale locale = new Locale(language);
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas", locale);
		try {
			fooWriter = new FileWriter(input, false); // false to overwrite.
			fooWriter.write(rewriteMainpage(user, eventTypeMenu, eventType, eventSpecification, events, firstTagGroup,
					secondTagGroup, firstTag, secondTag, language));
			fooWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			int eventsPerPage = 8;

			StringBuilder s3 = new StringBuilder();
			int pages = (int) Math.ceil((double) events.size() / eventsPerPage);
			s3.append("<ul class=\"pagination\">\r\n");
			for (int i = 1; i < pages + 1; i++) {
				if (i == pageNr) {
					s3.append("                    <li class=\"active\"><a href=\"/page" + i + "\">" + i
							+ "</a></li>\r\n");
				} else {
					s3.append("                    <li><a href=\"/page" + i + "\">" + i + "</a></li>\r\n");
				}
			}
			s3.append("                  </ul>");

			StringBuilder body = new StringBuilder();
			String color = "";
			String icon = "";

			int eventCounter = 0;
			int pageCountStart = 0;
			if (pageNr > 1) {
				pageCountStart = (pageNr - 1) * eventsPerPage;
			}
			for (int i = pageCountStart; i < events.size(); i++) {
				if (eventCounter < eventsPerPage) {
					if (events.get(i).getTags().contains(EventTag.Soccer)) {
						color = "#7cdb66";
						icon = "fa-futbol";
					} else if (events.get(i).getTags().contains(EventTag.Basketball)) {
						color = "peru";
						icon = "fa-basketball-ball";
					} else if (events.get(i).getTags().contains(EventTag.Football)) {
						color = "#d15c36";
						icon = "fa-football-ball";
					} else if (events.get(i).getTags().contains(EventTag.Cycling)) {
						color = "#e2e916";
						icon = "fa-bicycle";
					} else if (events.get(i).getTags().contains(EventTag.Baseball)) {
						color = "#70d4ff";
						icon = "fa-baseball-ball";
					}
					body.append("<div class=\"panel-conteiner\">\n"
							+ "                <div class=\"panel panel-default\">\n"
							+ "                        <div class=\"panel-heading text-center\">"
							+ events.get(i).publicEventName + "</div>\n"
							+ "                        <div class=\"row\">\n"
							+ "                            <div class=\"panel-body\">\n"
							+ "                                    <div class=\"col-md-2\" >\n"
							+ "                                        \n"
							+ "                                            <i class=\"fas " + icon
							+ " fa-7x eventIcon\" style=\"color:" + color + ";\"></i> \n"
							+ "                                        \n"
							+ "                                    </div>\n"
							+ "                                <div class=\"col-md-10\">\n"
							+ "                                    <div class=\"panelText\">\n"
							+ "                                    <p>"
							+ events.get(i).eventDescription.getTranslationText(Language.valueOf(language)) + "</p>\n"
							+ "                                        <p>Deadline " + events.get(i).deadlineToString()
							+ "</p>\n" + "                                        <p>Resolve "
							+ events.get(i).resolveToString() + "</p>\n"
							+ "                                    </div>\n"
							+ "                                    <form role=\"form\" method=\"POST\" action=\"/makebets\">\n"
							+ "                                        <div class=\"form-group\" >\n"
							+ "                                        <input id=\"eventID\" type=\"hidden\" name=\"eventID\" value=\""
							+ events.get(i).getID() + "\">\n" + "                                        </div>\n"
							+ "                                        <button class=\"btn panelBtn btn-primary\" type=\"submit\" style=\"background-color:"
							+ color + "; border-color: #000; color: white\">" + bundle.getString("ViewQuestions")
							+ "</button>\n" + "                                    </form>\n"
							+ "                                </div>\n" + "                            </div>\n"
							+ "                                \n"
							+ "                                                            \n" + "\n"
							+ "                            </div>\n" + "       </div>      </div>");
					eventCounter += 1;
				}
			}

			Element e = doc.getElementById("eventContent");
			e.html(body.toString());

			Element f = doc.getElementById("pagination");
			f.html(s3.toString());

			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the makebet page
	 * 
	 * @param user     that is betting
	 * @param event    event that the user is betting
	 * @param language language of the page
	 * @param bl       public business logic
	 * @param token    token of the user
	 * @return HTML code of the page
	 */
	public static String rewriteMakeBet(User user, Event event, String language, BLFacadePublic bl, String token) {
		File input = new File("./resources/templates2/" + language + "/makeBets.html");
		Locale locale = new Locale(language);
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas", locale);
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			String s = "";
			s = s + "<h4>" + event.publicEventName + "</h4>";

			String s2 = "";
			s2 = s2 + user.getWalletFunds();

			String s3 = "";
			s3 = s3 + "<h5 class=\"text-center\">"
					+ event.eventDescription.getTranslationText(Language.valueOf(language)) + "</h5>\n"
					+ "                    <br>\n" + "                    <h5 class=\"text-center\">"
					+ bundle.getString("Deadline") + ": " + event.deadlineToString() + "</h5>\n"
					+ "                    <h5 class=\"text-center\">" + bundle.getString("Resolve") + ": "
					+ event.resolveToString() + "</h5>";

			String sMenu = "<div class=\"nav-side-menu\""
					+ "><i class=\"fa fa-bars fa-2x toggle-btn\" data-toggle=\"collapse\" data-target=\"#menu-content\"></i>\r\n"
					+ "                    <div class=\"menu-list\">\r\n"
					+ "                        <ul id=\"menu-content\" class=\"menu-content collapse out\">\r\n"
					+ "\r\n"
					+ "                            <li data-toggle=\"collapse\" data-target=\"#Soccer\" class=\"collapsed\">\r\n"
					+ "                                <a href=\"#\"><i class=\"fas fa-futbol\"></i> "
					+ bundle.getString("Soccer") + " <span class=\"arrow\"></span></a>\r\n"
					+ "                            </li>\r\n"
					+ "                            <ul class=\"sub-menu collapse\" id=\"Soccer\">\r\n"
					+ "                                <li><a href=\"/soccer/competitions\">"
					+ bundle.getString("Competitions") + "</a></li>\r\n" + "                            </ul>\r\n"
					+ "\r\n"
					+ "                            <li data-toggle=\"collapse\" data-target=\"#Basketball\" class=\"collapsed\">\r\n"
					+ "                                <a href=\"#\"><i class=\"fas fa-basketball-ball\"></i> "
					+ bundle.getString("Basketball") + " <span\r\n"
					+ "                                        class=\"arrow\"></span></a>\r\n"
					+ "                            </li>\r\n"
					+ "                            <ul class=\"sub-menu collapse\" id=\"Basketball\">\r\n"
					+ "                                <li><a href=\"/basketball/competitions\">"
					+ bundle.getString("Competitions") + "</a></li>\r\n" + "                            </ul>\r\n"
					+ "\r\n" + "\r\n"
					+ "                            <li data-toggle=\"collapse\" data-target=\"#Football\" class=\"collapsed\">\r\n"
					+ "                                <a href=\"#\"><i class=\"fas fa-football-ball\"></i> "
					+ bundle.getString("Football") + " <span\r\n"
					+ "                                        class=\"arrow\"></span></a>\r\n"
					+ "                            </li>\r\n"
					+ "                            <ul class=\"sub-menu collapse\" id=\"Football\">\r\n"
					+ "                                <li><a href=\"/football/competitions\">"
					+ bundle.getString("Competitions") + "</a></li>\r\n" + "                            </ul>\r\n"
					+ "\r\n"
					+ "                            <li data-toggle=\"collapse\" data-target=\"#Cycling\" class=\"collapsed\">\r\n"
					+ "                                <a href=\"#\"><i class=\"fas fa-bicycle\"></i> "
					+ bundle.getString("Cycling") + " <span class=\"arrow\"></span></a>\r\n"
					+ "                            </li>\r\n"
					+ "                            <ul class=\"sub-menu collapse\" id=\"Cycling\">\r\n"
					+ "                                <li><a href=\"/cycling/competitions\">"
					+ bundle.getString("Competitions") + "</a></li>\r\n" + "                            </ul>\r\n"
					+ "\r\n"
					+ "                            <li data-toggle=\"collapse\" data-target=\"#Baseball\" class=\"collapsed\">\r\n"
					+ "                                <a href=\"#\"><i class=\"fas fa-baseball-ball\"></i> "
					+ bundle.getString("Baseball") + " <span\r\n"
					+ "                                        class=\"arrow\"></span></a>\r\n"
					+ "                            </li>\r\n"
					+ "                            <ul class=\"sub-menu collapse\" id=\"Baseball\">\r\n"
					+ "                                <li><a href=\"/baseball/competitions\">"
					+ bundle.getString("Competitions") + "</a></li>\r\n" + "                            </ul>\r\n"
					+ "\r\n" + "                        </ul>\r\n" + "                    </div></div>";

			Element d = doc.getElementById("submenu");
			d.html(sMenu);

			StringBuilder s4 = new StringBuilder();
			List<Question> questions = event.getAllQuestions();

			for (Question q : questions) {
				if (!bl.getBets().hasAlreadyBet(user.getUsername(), token, q.getId())) {
					if (q.getQuestionMode().toString().equals("UseNumericValue")) {
						s4.append("<div class=\"panel panel-default\">\r\n"
								+ "                    <div class=\"panel-heading\"><h3>"
								+ q.translation.getTranslationText(Language.valueOf(language)) + "</h3></div>\r\n"
								+ "                    <div class=\"row\">\r\n"
								+ "                        <div class=\"panel-body\">\r\n"
								+ "                            <div class=\"col-md-7\">\r\n" + "\r\n"
								+ "                                <form role=\"form\" method=\"POST\" action=\"/continueBet\">\r\n"
								+ "                                    <div class=\"form-group\">\r\n"
								+ "<input id=\"questionID\" type=\"hidden\" name=\"questionID\" value=\"" + q.getId()
								+ "\">\r\n" + "\r\n<input id=\"eventID\" type=\"hidden\" name=\"eventID\" value=\""
								+ event.getID() + "\"><input id=\"type\" type=\"hidden\" name=\"mode\" value=\""
								+ q.getQuestionMode().name()
								+ "\"><input id=\"length\" type=\"hidden\" name=\"length\" value=\""
								+ q.getAnswerLength() + "\">");

						for (int i = 1; i < q.getAnswerLength() + 1; i++) {
							s4.append("<label for=\"textAnswer\"><h4>" + bundle.getString("Team") + " " + i
									+ "</h4></label><input type=\"number\" class=\"form-control\" required id=\"textAnswer\"\r\n"
									+ "                                            placeholder=\""
									+ bundle.getString("EnterYourAnswer") + "\" min=\"1\" name=\"textAnswer\">\r\n "
									+ "                                            <br>\r\n");
						}

						s4.append("<label for=\"betAmount\"><h4>" + bundle.getString("BetAmount")
								+ "</h4></label><input id=\"betAmount\" class=\"form-control\" name=\"betAmount\" + "
								+ "type=\"number\" required placeholder=\"" + bundle.getString("EnterBetAmount")
								+ "\" min=" + q.getMinimumBetAmount() + " max=" + user.getWalletFunds()
								+ ">\r\n</div><button class=\"btn btn-primary\" type=\"submit\">"
								+ bundle.getString("Continue") + "</button>\r\n"
								+ "                                </form>\r\n"
								+ "                            </div>\r\n"
								+ "                            <div class=\"col-md-5\">\r\n"
								+ "                                <p>Min bet: " + q.getMinimumBetAmount() + " "
								+ bundle.getString("Points") + "</p>\r\n" + "                                <p>"
								+ bundle.getString("Deadline") + " " + event.deadlineToString() + "<p>\r\n"
								+ "                                        <p>" + bundle.getString("Resolve") + " "
								+ event.resolveToString() + "<p>\r\n" + "                            </div>\r\n"
								+ "                        </div>\r\n" + "                    </div>\r\n"
								+ "                </div>");
					}

					if (q.getQuestionMode().toString().equals("UseValidSet")) {
						List<String> validSet = q.getValidSet();
						s4.append("<div class=\"panel panel-default\"><div class=\"panel-heading\"><h3>"
								+ q.translation.getTranslationText(Language.valueOf(language)) + "</h3></div>\n"
								+ "                    <div class=\"row\">\n"
								+ "                        <div class=\"panel-body\">\n"
								+ "                            <dic class=\"col-md-7\">\n" + "\n"
								+ "                                <form role=\"form\" method=\"POST\" action=\"/continueBet\">\n"
								+ "                                    <div class=\"form-group\">\n"
								+ "                                        <label for=\"multipleAnswer\"><h4>"
								+ bundle.getString("ChooseAnswer") + "</h4></label>\n"
								+ "                                        <div>\n"
								+ "<input id=\"questionID\" type=\"hidden\" name=\"questionID\" value=\"" + q.getId()
								+ "\">\n<input id=\"eventID\" type=\"hidden\" name=\"eventID\" value=\"" + event.getID()
								+ "\"><input id=\"type\" type=\"hidden\" name=\"mode\" value=\""
								+ q.getQuestionMode().name()
								+ "\"><select class=\"custom-select\" id=\"multipleAnswer\" name=\"multipleAnswer\">\n");

						for (int i = 0; i < validSet.size(); i++) {
							if (i == 0) {
								s4.append("<option selected value=\"" + validSet.get(i) + "\">" + validSet.get(i)
										+ "</option>\n");
							} else {
								s4.append(
										"<option value=\"" + validSet.get(i) + "\">" + validSet.get(i) + "</option>\n");
							}
						}
						s4.append("                                            </select>\n"
								+ "                                        </div>\n" + "<br>\r\n"
								+ "                                        <label for=\"betAmount\"><h4>"
								+ bundle.getString("BetAmount")
								+ "</h4></label><input id=\"betAmount\" required class=\"form-control\" name=\"betAmount\" type=\"number\" "
								+ "placeholder=\"" + bundle.getString("EnterBetAmount") + "\" min=\""
								+ q.getMinimumBetAmount() + "\" max=\"" + user.getWalletFunds() + "\">"
								+ "                                    </div>\n"
								+ "                                    <button class=\"btn btn-primary\" type=\"submit\">"
								+ bundle.getString("Continue") + "</button>\n"
								+ "                                </form>\n" + "                            </dic>\n"
								+ "                            <div class=\"col-md-5\">\n"
								+ "                                <p>" + bundle.getString("MinBet") + ": "
								+ q.getMinimumBetAmount() + " " + bundle.getString("Points") + "</p>\n"
								+ "                                <p>" + bundle.getString("Deadline") + ": "
								+ event.deadlineToString() + "<p>\n" + "                                        <p>"
								+ bundle.getString("Resolve") + ": " + event.resolveToString() + "<p>\n"
								+ "                            </div>\n" + "                        </div>\n"
								+ "                    </div>");
					}

				} else {
					String qAnswer = "";
					for (BetSuperClass t : q.getBets()) {
						if (t.getUsername().equals(user.getUsername())) {
							qAnswer = t.getPrediction();
							break;
						}
					}
					String qAnswerFormat = "";
					String[] qAnswerHelper = qAnswer.split(",");
					if (qAnswerHelper.length > 1) {
						for (int j = 0; j < qAnswerHelper.length - 1; j++) {
							qAnswerFormat += qAnswerHelper[j] + " - ";
						}
						qAnswerFormat += qAnswerHelper[qAnswerHelper.length - 1];
					} else {
						qAnswerFormat = qAnswerHelper[0];
					}

					s4.append("<div class=\"panel panel-default\">\r\n"
							+ "                    <div class=\"panel-heading\"><h3>"
							+ q.translation.getTranslationText(Language.valueOf(language)) + "</h3></div>\r\n"
							+ "                    <div class=\"row\">\r\n"
							+ "                        <div class=\"panel-body\">\r\n"
							+ "                            <div class=\"col-md-7\">" + "<h3><p>"
							+ bundle.getString("YouHave") + "</p></h3>" + "<br>" + "<h3><p>"
							+ bundle.getString("YourAnswerIs") + ": " + qAnswerFormat + "</p><h3>"
							+ "                              </div></div></div>\r\n" + "                </div>");
				}

			}

			Element a = doc.getElementById("eventHead");
			a.html(s.toString());

			Element b = doc.getElementById("currentPoints");
			b.html(s2);

			Element c = doc.getElementById("eventDescription");
			c.html(s3);

			Element p = doc.getElementById("questions");
			p.html(s4.toString());

			return doc.html();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * This function rewrites the continueBet.
	 * 
	 * @param user       user which is betting
	 * @param event      to which is the user is betting
	 * @param q          which the user is bettings
	 * @param betAmount  the betamount
	 * @param prediction the prediction of the user
	 * @param language   language used in the page
	 * @return HTML code of the page
	 */
	public static String rewriteContinueBet(User user, Event event, Question q, long betAmount, String prediction,
			String language) {
		File input = new File("./resources/templates2/" + language + "/makeBets.html");
		Locale locale = new Locale(language);
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas", locale);
		try {
			Document doc = Jsoup.parse(input, "UTF-8");

			String s = "";
			s = s + "<h4>" + event.publicEventName + "</h4>";

			String s2 = "";
			s2 = s2 + user.getWalletFunds();

			String s3 = "";
			s3 = s3 + "<h5 class=\"text-center\">" + bundle.getString("EventDescription") + "</h5>\n"
					+ "                    <br>\n" + "                    <h5 class=\"text-center\">Deadline: "
					+ event.deadlineToString() + "</h5>\n" + "                    <h5 class=\"text-center\">Resolve: "
					+ event.resolveToString() + "</h5>";
			String qAnswer = "";

			String[] qAnswerHelper = prediction.split(",");
			if (qAnswerHelper.length > 1) {
				for (int j = 0; j < qAnswerHelper.length - 1; j++) {
					qAnswer += qAnswerHelper[j] + " - ";
				}
				qAnswer += qAnswerHelper[qAnswerHelper.length - 1];
			}

			StringBuilder s4 = new StringBuilder();
			s4.append("<div class=\"panel panel-default\">\r\n"
					+ "                        <div class=\"panel-heading\">"
					+ q.translation.getTranslationText(Language.valueOf(language)) + "</div>\r\n"
					+ "                            <div class=\"panel-body\">\r\n" + "<div class=\"row\">"
					+ "                                <div class=\"col-md-3\">\r\n"
					+ " 									   <p>" + bundle.getString("YourAnswer") + ": " + qAnswer
					+ "</p>" + "                                    <p>" + bundle.getString("YourBet") + ": "
					+ betAmount + " " + bundle.getString("Points") + "</p>\r\n"
					+ "                                    <p>" + bundle.getString("Payoff") + ": "
					+ q.getPayoff(prediction) + "</p>\r\n" + "                                    <p>"
					+ bundle.getString("EstimatedRevenue") + ": " + q.getPayoff(prediction) * betAmount + " "
					+ bundle.getString("Points") + "</p>\r\n" + "                                </div>\r\n"
					+ "                                <div class=\"col-md-9\">\r\n"
					+ "                                    <p>" + bundle.getString("MinBet") + ": "
					+ q.getMinimumBetAmount() + " " + bundle.getString("Points") + "</p>\r\n"
					+ "                                    <p>" + bundle.getString("Deadline") + " "
					+ event.deadlineToString() + "<p>\r\n" + "                                            <p>"
					+ bundle.getString("Resolve") + " " + event.resolveToString() + "<p>\r\n"
					+ "                                </div>" + "<div class=\"col-md-12\">\r\n"
					+ "                                <form id=\"betForm\" role=\"form\" method=\"POST\" action=\"/submitBet\">\r\n"
					+ "                                    <input id=\"questionId\" type=\"hidden\" name=\"questionId\" value=\""
					+ q.getId() + "\">\r\n"
					+ "                                    <input id=\"eventID\" type=\"hidden\" name=\"eventID\" value=\""
					+ event.getID() + "\">\r\n"
					+ "                                    <input id=\"betAmount\" type=\"hidden\" name=\"betAmount\" value=\""
					+ betAmount + "\">\r\n"
					+ "                                    <input id=\"prediction\" type=\"hidden\" name=\"prediction\" value=\""
					+ prediction + "\">\r\n"
					+ "                                    <button class=\"btn btn-primary\" type=\"submit\">"
					+ bundle.getString("Bet!") + "</button>\r\n"
					+ " <input id=\"eventID\" type=\"hidden\" name=\"eventID\" value=\"" + event.getID()
					+ "\"></form></div></div>" + "<div class=\"row\"><div class=\"col-md-12\">"
					+ "<form id=\"betForm\" role=\"form\" method=\"post\" action=\"/backToQuestions\">"
					+ "<input type=\"hidden\" name=\"eventID\" value=\"" + event.getID() + "\">"
					+ "<button class=\"btn btn-primary \"  type=\"submit\">" + bundle.getString("BackToQuestions")
					+ "</button>\r\n" + "              </form></div></div>\r\n" + " </div></div>\r\n"
					+ "            </div>");

			Element a = doc.getElementById("eventHead");
			a.html(s.toString());

			Element b = doc.getElementById("currentPoints");
			b.html(s2);

			Element c = doc.getElementById("eventDescription");
			c.html(s3);

			Element d = doc.getElementById("questions");
			d.html(s4.toString());

			return doc.html();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * This function returns the html code a requested page
	 * 
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
		} catch (FileNotFoundException e) {
			return "";
		} catch (Exception e) {
			return "";
		}
		return s;
	}

}
