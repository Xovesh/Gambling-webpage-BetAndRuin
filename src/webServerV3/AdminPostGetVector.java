package webServerV3;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import businessLogicV3.privateAPI.BLFacadePrivate;
import businessLogicV3.publicAPI.BLFacadePublic;
import domainV2.User;
import domainV2.event.Event;
import domainV2.event.EventTag;
import domainV2.util.Language;
import domainV2.util.Translation;

public class AdminPostGetVector {
	static final File WEB_ROOT = new File("./resources/templates2/");
	// ----------------- POST ---------------------------------

	/**
	 * This function selects the answer depending the post request
	 * 
	 * @param body    HTTP request body
	 * @param request request that the user is doing
	 * @param help    list to speak with the server
	 * @param cookies cookies that use the user
	 * @param bl      bl to help the communication between server and the business
	 *                logic
	 * @return HTML code of the page requested
	 */
	static String adminPostRequest(char[] body, String request, List<String> help, Hashtable<String, String> cookies,
			BLFacadePublic bl) {

		System.out.println(body);

		// body has all the information that you are sending in the form
		Hashtable<String, String> values2 = new Hashtable<String, String>();
		String[] values = null;
		try {
			values = java.net.URLDecoder.decode(String.valueOf(body), StandardCharsets.UTF_8.name()).split("&");
		} catch (UnsupportedEncodingException e1) {
			// Not going to happen
			return "";
		}

		for (String s : values) {
			String[] hp1 = s.split("=");
			if (hp1.length == 2) {
				values2.put(hp1[0], hp1[1]);
			} else {
				values2.put(hp1[0], "");
			}
		}
		// String ip = help.get(0);
		help.clear();
		int nKeys = values2.keySet().size();
		try {
			switch (request) {
			case "/administration/adminlogin":
				if (nKeys != 2) {
					return RewriteAdminHTML.readFile(WEB_ROOT, "/administration/adminLogin.html");
				}
				help.add("username=" + values2.get("username") + ";");
				String token = bl.getUsers().authentication(values2.get("username"), values2.get("password"));
				if (!token.equals("")) {
					help.add("token=" + token + ";");
					// change = true;
					// special case
					if (bl.getUsers().checkUserRank(values2.get("username"), token) == 3) {
						return RewriteAdminHTML.adminMain(bl.getPrivateAPI(values2.get("username"), token));
					}
				}
				help.clear();

				return RewriteAdminHTML.readFile(WEB_ROOT, "/administration/adminLogin.html");

			case "/administration/adminuserdata":
				System.out.println("Rewriting admin user data");
				User ust = bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
						.searchUserById(new Integer(values2.get("id")));
				// change = true;
				return RewriteAdminHTML.adminUserData(ust,
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getBets()
								.getLastNBetsByUser(ust.getUsername(), 200),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getTransactions()
								.getLastTransactions(ust.getUsername(), 50),
						bl);

			case "/administration/adminuserdatarankupdate":
				System.out.println("Updated rank");
				bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
						.updateUserRank(values2.get("newusername"), new Integer(values2.get("rank")));
				User ust2 = bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
						.retrieveUser(values2.get("newusername"));
				// change = true;
				return RewriteAdminHTML.adminUserData(ust2,
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getBets()
								.getLastNBetsByUser(ust2.getUsername(), 200),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getTransactions()
								.getLastTransactions(ust2.getUsername(), 50),
						bl);

			case "/administration/adminuserdataupdate":

				User uSD = bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
						.retrieveUser(values2.get("username"));

				if (nKeys != 11) {
					return RewriteAdminHTML.adminUserData(uSD,
							bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getBets()
									.getLastNBetsByUser(values2.get("username"), 200),
							bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getTransactions()
									.getLastTransactions(values2.get("username"), 50),
							bl);
				}

				List<String> adminFavouriteSports = new LinkedList<String>();
				if (values2.get("favouriteSports").length() != 1) {
					for (String s : values2.get("favouriteSports").split(";")) {
						adminFavouriteSports.add(s);
					}
				} else {
					adminFavouriteSports = null;
				}
				List<String> adminFavouriteTeams = new LinkedList<String>();
				if (values2.get("favouriteTeams").length() != 1) {
					for (String s : values2.get("favouriteTeams").split(";")) {
						adminFavouriteTeams.add(s);
					}
				} else {
					adminFavouriteTeams = null;
				}
				bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers().updateUserData(
						values2.get("username"), values2.get("sex"), values2.get("phone"), values2.get("email"),
						values2.get("first_name"), values2.get("last_name"), values2.get("country"),
						values2.get("city"), values2.get("address"), adminFavouriteSports, adminFavouriteTeams);
				uSD = bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
						.retrieveUser(values2.get("username"));
				// change = true;
				return RewriteAdminHTML.adminUserData(uSD,
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getBets()
								.getLastNBetsByUser(values2.get("username"), 200),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getTransactions()
								.getLastTransactions(values2.get("username"), 50),
						bl);

			case "/administration/adminuserdelete":
				bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
						.removeUser(values2.get("username"));
				// change = true;
				return RewriteAdminHTML.adminUsers(
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers().firstUserId("ALL"),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")), "ALL");

			case "/administration/updateadminpassword":
				User uUP = bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
						.retrieveUser(values2.get("username"));

				if (nKeys != 3) {
					// change = true;
					return RewriteAdminHTML.adminUserData(uUP,
							bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getBets()
									.getLastNBetsByUser(uUP.getUsername(), 200),
							bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getTransactions()
									.getLastTransactions(uUP.getUsername(), 50),
							bl);
				}
				bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
						.updatePassword(values2.get("username"), values2.get("password"), values2.get("password2"));
				// change = true
				return RewriteAdminHTML.adminUserData(uUP,
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getBets()
								.getLastNBetsByUser(uUP.getUsername(), 200),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getTransactions()
								.getLastTransactions(uUP.getUsername(), 50),
						bl);

			case "/administration/adminnewscreate":
				if (values2.keySet().size() != 4) {
					// change = false;
					return RewriteAdminHTML.readFile(WEB_ROOT, "/administration/adminCreateNews.html");
				}

				if (values2.get("picture").equals("")) {
					values2.put("picture", "https://dubsism.files.wordpress.com/2017/12/image-not-found.png?w=547");
				}

				int newsid = bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getNews().addNews(
						values2.get("language"), values2.get("head"), values2.get("content"), values2.get("picture"));
				// change = true;
				return RewriteAdminHTML.adminNewsData(bl.getPrivateAPI(cookies.get("username"), cookies.get("token"))
						.getNews().retrieveNewsById(new Integer(newsid)));

			case "/administration/newsdetails":
				System.out.println("Rewriting admin news data");
				// change = true;
				return RewriteAdminHTML.adminNewsData(bl.getPrivateAPI(cookies.get("username"), cookies.get("token"))
						.getNews().retrieveNewsById(new Integer(values2.get("newsid"))));

			case "/administration/adminnewsupdate":
				bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getNews().modifyNews(
						new Integer(values2.get("newsid")), values2.get("language"), values2.get("head"),
						values2.get("content"), values2.get("picture"));
				// change = true;
				return RewriteAdminHTML.adminNewsData(bl.getPrivateAPI(cookies.get("username"), cookies.get("token"))
						.getNews().retrieveNewsById(new Integer(values2.get("newsid"))));

			case "/administration/adminnewsdelete":
				bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getNews()
						.deleteNews(new Integer(values2.get("newsid")));
				System.out.println("Rewriting admin news data");
				// change = true
				return RewriteAdminHTML.adminNews(
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getNews().firstNewsId("ALL"),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")), "ALL");

			case "/administration/updatesupportinfo":
				System.out.println(bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getOthers()
						.updateSupportInfo(values2.get("content"), values2.get("language")));
				// change = true;
				return RewriteAdminHTML.adminSettings(
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getOthers()
								.readSupportInfo("ES"),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getOthers()
								.readSupportInfo("EN"),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getOthers()
								.readSupportInfo("EUS"));

			case "/administration/updatefunds":
				bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers().updateWalletFunds(
						values2.get("username"), Long.valueOf(values2.get("funds")),
						"FUNDS UPDATED BY AN ADMINISTRATION");
				User uUF = bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
						.retrieveUser(values2.get("username"));
				// change = true;
				return RewriteAdminHTML.adminUserData(uUF,
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getBets()
								.getLastNBetsByUser(uUF.getUsername(), 200),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getTransactions()
								.getLastTransactions(uUF.getUsername(), 50),
						bl);

			case "/administration/maintenancemodechange":
				SimpleHTTPServer.MAINTENANCE_MODE = !SimpleHTTPServer.MAINTENANCE_MODE;
				// change = true;
				return RewriteAdminHTML.adminSettings(
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getOthers()
								.readSupportInfo("ES"),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getOthers()
								.readSupportInfo("EN"),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getOthers()
								.readSupportInfo("EUS"));

			case "/administration/adminEventData":
				// change = true;
				return RewriteAdminHTML.adminViewEvents(bl.getEvents().getEventByID(Long.parseLong(values2.get("id"))),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")));

			case "/administration/admincreatequestion":
				// change = true
				return RewriteAdminHTML.createQuestion(Long.parseLong(values2.get("eventID")));

			case "/administration/admincreatequestioninevent":

				if (values2.keySet().size() < 9) {
					return RewriteAdminHTML.createQuestion(Long.parseLong(values2.get("id")));
				}
				List<String> set = new LinkedList<String>();
				Translation questionText = new Translation(values2.get("QuestionNameEN"));
				questionText.setTranslationText(Language.EN, values2.get("QuestionNameEN"));
				questionText.setTranslationText(Language.ES, values2.get("QuestionNameES"));
				questionText.setTranslationText(Language.EUS, values2.get("QuestionNameEUS"));
				if (values2.get("questionMode").equals("UseValidSet")) {
					for (String s : values2.get("questionSet").split(";")) {
						set.add(s);
					}
					List<Float> validSetPayouts = new LinkedList<Float>();
					for (String s : values2.get("validSetPayouts").replace(" ", "").split(";")) {
						validSetPayouts.add(Float.valueOf(s));
					}
					bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getQuestions().addQuestionValidSet(
							Long.parseLong(values2.get("id")), questionText, values2.get("QuestionNameEN"), set,
							validSetPayouts);
				} else if (values2.get("questionMode").equals("UseNumericValue")) {
					String expectedResultRaw = values2.get("expectedResult");
					expectedResultRaw.replace(" ", "");
					int[] expectedResult = new int[expectedResultRaw.split(";").length];
					int i = 0;
					for (String s : expectedResultRaw.split(";")) {
						expectedResult[i] = Integer.valueOf(s);
						i++;
					}
					bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getQuestions().addQuestionNumeric(
							Long.parseLong(values2.get("id")), questionText, values2.get("QuestionNameEN"),
							expectedResult, Integer.parseInt(values2.get("maxExpectedScore")));
				}

				// change = true;
				return RewriteAdminHTML.adminViewEvents(bl.getEvents().getEventByID(Long.parseLong(values2.get("id"))),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")));

			case "/administration/adminQuestionData":
				// change = true;
				Event ev = bl.getEvents().getEventByID(Long.parseLong(values2.get("id")));
				return RewriteAdminHTML.viewQuestions(ev.getQuestion(values2.get("eventName")),
						Long.parseLong(values2.get("id")), ev);

			case "/administration/admineventcreate":
				List<String> setEvent = new LinkedList<String>();
				List<EventTag> setEventTags = new LinkedList<EventTag>();
				for (String s : values2.get("eventtags").split(";")) {
					try {
						setEventTags.add(EventTag.valueOf(s));
					} catch (Exception e) {
						continue;
					}
				}

				Translation te = new Translation(values2.get("eventDescriptionEN"));
				te.setTranslationText(Language.ES, values2.get("eventDescriptionES"));
				te.setTranslationText(Language.EN, values2.get("eventDescriptionEN"));
				te.setTranslationText(Language.EUS, values2.get("eventDescriptionEUS"));

				Translation tq = new Translation(values2.get("QuestionNameEN"));
				tq.setTranslationText(Language.ES, values2.get("QuestionNameES"));
				tq.setTranslationText(Language.EN, values2.get("QuestionNameEN"));
				tq.setTranslationText(Language.EUS, values2.get("QuestionNameEUS"));
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
				Calendar deadLineEvent = new GregorianCalendar();
				deadLineEvent.setTime(format1.parse(values2.get("deadLineDate")));
				Calendar resolveEvent = new GregorianCalendar();
				resolveEvent.setTime(format1.parse(values2.get("resolveDate")));

				long newEventID = 0;
				if (values2.get("questionMode").equals("UseValidSet")) {
					try {
						for (String s : values2.get("questionSet").split(";")) {
							setEvent.add(s);
						}
					} catch (Exception e) {
						RewriteAdminHTML.readFile(WEB_ROOT, "/administration/adminCreateEvent.html");
					}
					List<Float> validSetPayouts = new LinkedList<Float>();
					for (String s : values2.get("validSetPayouts").replace(" ", "").split(";")) {
						validSetPayouts.add(Float.valueOf(s));
					}
					newEventID = bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getEvents()
							.createEvent(values2.get("eventNameEN"), deadLineEvent, resolveEvent, te, setEventTags);
					bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getQuestions().addQuestionValidSet(
							newEventID, tq, values2.get("QuestionNameEN"), setEvent, validSetPayouts);
				} else if (values2.get("questionMode").equals("UseNumericValue")) {
					newEventID = bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getEvents()
							.createEvent(values2.get("eventNameEN"), deadLineEvent, resolveEvent, te, setEventTags);
					String expectedResultRaw = values2.get("expectedResult");
					expectedResultRaw.replace(" ", "");
					int[] expectedResult = new int[expectedResultRaw.split(";").length];
					int i = 0;
					for (String s : expectedResultRaw.split(";")) {
						expectedResult[i] = Integer.valueOf(s);
						i++;
					}
					bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getQuestions().addQuestionNumeric(
							newEventID, tq, values2.get("QuestionNameEN"), expectedResult,
							Integer.parseInt(values2.get("maxExpectedScore")));
				}
				// change = true;
				return RewriteAdminHTML.adminViewEvents(bl.getEvents().getEventByID(newEventID),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")));

			case "/administration/admineventcancel":
				bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getEvents()
						.cancelEvent(Long.parseLong(values2.get("eventID")));
				return RewriteAdminHTML.adminViewEvents(
						bl.getEvents().getEventByID(Long.parseLong(values2.get("eventID"))),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")));

			case "/administration/admineventupdate":
				Translation ut = new Translation(values2.get("eventDescriptionEN"));
				ut.setTranslationText(Language.ES, values2.get("eventDescriptionES"));
				ut.setTranslationText(Language.EN, values2.get("eventDescriptionEN"));
				ut.setTranslationText(Language.EUS, values2.get("eventDescriptionEUS"));
				List<EventTag> setUpdateEventTags = new LinkedList<EventTag>();
				for (String s : values2.get("eventtags").split(";")) {
					try {
						setUpdateEventTags.add(EventTag.valueOf(s));
					} catch (Exception e) {
						continue;
					}
				}
				SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
				Calendar deadLineEventUpdate = new GregorianCalendar();
				deadLineEventUpdate.setTime(format2.parse(values2.get("deadLineDate")));
				Calendar resolveEventUpdate = new GregorianCalendar();
				resolveEventUpdate.setTime(format2.parse(values2.get("resolveDate")));
				bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getEvents().updateEvent(
						Long.parseLong(values2.get("eventid")), values2.get("eventNameEN"), deadLineEventUpdate,
						resolveEventUpdate, ut, setUpdateEventTags);
				// change = true
				return RewriteAdminHTML.adminViewEvents(
						bl.getEvents().getEventByID(Long.parseLong(values2.get("eventid"))),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")));

			case "/administration/resolveQuestion":
				String qAnswer = "";
				if (values2.get("questionType").equals("UseValidSet")) {
					qAnswer = values[3].split("=")[1];
				} else {
					int qLength = Integer.parseInt(values[3].split("=")[1]);
					for (int i = 0; i < qLength - 1; i++) {
						qAnswer += values[i + 4].split("=")[1] + ",";
					}
					qAnswer += values[3 + qLength].split("=")[1];
				}
				bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getQuestions()
						.answerQuestion(Long.parseLong(values2.get("questionID")), qAnswer);
				if (bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getEvents()
						.getEventByID(Long.parseLong(values2.get("eventID"))).allQuestionAnswerd()) {
					bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getEvents()
							.resolveEvent(Long.parseLong(values2.get("eventID")));
				}

				return RewriteAdminHTML.viewQuestions(
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getQuestions().getQuestionByID(
								Long.parseLong(values2.get("questionID"))),
						Long.parseLong(values2.get("eventID")),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getEvents()
								.getEventByID(Long.parseLong(values2.get("eventID"))));

			case "/administration/updateEventSettings":
				String updateMessage = "";
				List<String> newPopular = new LinkedList<String>();
				newPopular.add(values2.get("Team1"));
				newPopular.add(values2.get("Team2"));
				boolean update = bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getEvents()
						.updatePopularEventsTagsByType(values2.get("SportName"), newPopular);
				if (update) {
					updateMessage = "<h3><font color=\"green\">Successfully updated</font><h3>";
				} else {
					updateMessage = "<h3><font color=\"red\">Something went wrong during the update!</font><h3>";
				}
				return RewriteAdminHTML.rewriteEventSettings(
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")), updateMessage);

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return RewriteAdminHTML.readFile(WEB_ROOT, "/404.html");
		} catch (Exception e) {
			e.printStackTrace();
			return RewriteAdminHTML.readFile(WEB_ROOT, "/404.html");
		}

		return RewriteAdminHTML.readFile(WEB_ROOT, "/404.html");
	}

	// -------------- GET ------------------

	/**
	 * This function selects the answer depending the get request
	 * 
	 * @param fileRequested the file that the user request
	 * @param cookies       cookies that use the user
	 * @param bl            bl to help the communication between server and the
	 *                      business logic
	 * @return HTML HTML code of the page requested
	 */
	static String adminGetRequest(String fileRequested, Hashtable<String, String> cookies, BLFacadePublic bl) {
		BLFacadePrivate help = bl.getPrivateAPI(cookies.get("username"), cookies.get("token"));
		try {
			switch (fileRequested) {
			case "/administration/adminusers":
				System.out.println("Rewriting admin users");
				return RewriteAdminHTML.adminUsers(help.getUsers().firstUserId("ALL"), help, "ALL");

			case "/administration/adminmain":
				System.out.println("Rewriting admin main");
				return RewriteAdminHTML.adminMain(bl.getPrivateAPI(cookies.get("username"), cookies.get("token")));

			case "/administration/bannedusers":
				System.out.println("Rewriting admin banned users");
				return RewriteAdminHTML.adminUsers(help.getUsers().firstUserId("BANNED"), help, "BANNED");

			case "/administration/logout":
				System.out.print("Login out");
				bl.getUsers().logOut(cookies.get("username"), cookies.get("token"));
				return RewriteAdminHTML.readFile(WEB_ROOT, "/administration/adminLogin.html");

			case "/administration/adminnews":
				System.out.println("Admin News");
				return RewriteAdminHTML.adminNews(help.getNews().firstNewsId("ALL"), help, "ALL");

			case "/administration/adminserverstatus":
				System.out.println("Admin server status");
				return RewriteAdminHTML.adminServerStatus(
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
								.retrieveConnectedUsers(),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getUsers()
								.retrieveLastConnectedUsers());

			case "/administration/admincreatenews":
				return RewriteAdminHTML.readFile(WEB_ROOT, "/administration/adminCreateNews.html");

			case "/administration/adminnews?language=EN":
				return RewriteAdminHTML.adminNews(help.getNews().firstNewsId("EN"), help, "EN");

			case "/administration/adminnews?language=ES":
				return RewriteAdminHTML.adminNews(help.getNews().firstNewsId("ES"), help, "ES");

			case "/administration/adminnews?language=EUS":
				return RewriteAdminHTML.adminNews(help.getNews().firstNewsId("EUS"), help, "EUS");

			case "/administration/adminnews?language=ALL":
				return RewriteAdminHTML.adminNews(help.getNews().firstNewsId("ALL"), help, "ALL");

			case "/administration/adminstatistics":
				return RewriteAdminHTML.adminStatistics(help);

			case "/administration/adminsettings":
				return RewriteAdminHTML.adminSettings(
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getOthers()
								.readSupportInfo("ES"),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getOthers()
								.readSupportInfo("EN"),
						bl.getPrivateAPI(cookies.get("username"), cookies.get("token")).getOthers()
								.readSupportInfo("EUS"));

			case "/administration/admincreateevent":
				return RewriteAdminHTML.readFile(WEB_ROOT, "/administration/adminCreateEvent.html");

			case "/administration/adminallevents":
				return RewriteAdminHTML.adminEvents(help, "ALL");

			case "/administration/adminunresolvedevents":
				return RewriteAdminHTML.adminEvents(help, "UNRESOLVED");

			case "/administration/adminresolvedevents":
				return RewriteAdminHTML.adminEvents(help, "RESOLVED");
			
			case "/administration/adminalleventsbycanbet":
				return RewriteAdminHTML.adminEvents(help, "BYCANBET");

			case "/administration/adminEventSettings":
				return RewriteAdminHTML.rewriteEventSettings(help, "");

			}

			if (fileRequested.startsWith("/administration/users?from=")) {
				System.out.println("Rewriting admin users");
				String[] helperFrom = fileRequested.split("\\?");
				return RewriteAdminHTML.adminUsers(Integer.parseInt(helperFrom[1].split("&")[0].split("=")[1]), help,
						helperFrom[1].split("&")[1].split("=")[1].toUpperCase());

			}

			if (fileRequested.startsWith("/administration/searchuser")) {
				System.out.println("Rewriting admin user data");
				try {
					User ust = help.getUsers().retrieveUser(fileRequested.split("\\?")[1].split("=")[1]);
					return RewriteAdminHTML.adminUserData(ust,
							help.getBets().getLastNBetsByUser(ust.getUsername(), 200),
							help.getTransactions().getLastTransactions(ust.getUsername(), 50), bl);

				} catch (Exception e) {
					return RewriteAdminHTML.adminUsers(help.getUsers().firstUserId("ALL"), help, "ALL");

				}
			}

			if (fileRequested.startsWith("/administration/news?from=")) {
				System.out.println("Rewriting admin news");
				String[] helperFrom = fileRequested.split("\\?");
				return RewriteAdminHTML.adminNews(Integer.parseInt(helperFrom[1].split("&")[0].split("=")[1]), help,
						helperFrom[1].split("&")[1].split("=")[1].toUpperCase());

			}
		} catch (NumberFormatException e) {
			 e.printStackTrace();
			return RewriteAdminHTML.adminMain(bl.getPrivateAPI(cookies.get("username"), cookies.get("token")));
		} catch (Exception e) {
			 e.printStackTrace();
			return RewriteAdminHTML.adminMain(bl.getPrivateAPI(cookies.get("username"), cookies.get("token")));
		}

		return RewriteAdminHTML.adminMain(bl.getPrivateAPI(cookies.get("username"), cookies.get("token")));
	}
}
