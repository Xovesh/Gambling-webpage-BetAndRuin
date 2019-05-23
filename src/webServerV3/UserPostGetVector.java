package webServerV3;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import businessLogicV3.publicAPI.BLFacadePublic;
import domainV2.Page;
import domainV2.event.Event;
import domainV2.event.EventTag;

public class UserPostGetVector {
	static final File WEB_ROOT = new File("./resources/templates2/");
	static final int EVENTES_PER_PAGE = 400;
	static Page currentPage = new Page();
	// How to make a post request:


	// -------------- POST --------------------
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
	static String userPostRequest(char[] body, String request, List<String> help, Hashtable<String, String> cookies,
			BLFacadePublic bl) {
		boolean change = false;
		Locale locale = new Locale(cookies.get("language"));
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas", locale);
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
		String ip = help.get(0);
		help.clear();
		for (String s : values) {
			String[] hp1 = s.split("=");
			if (hp1.length == 2) {
				values2.put(hp1[0], hp1[1]);
			} else {
				values2.put(hp1[0], "");
			}
		}

		int nKeys = values2.keySet().size();

		
		try {
			
			
			switch (request) {
			
			case "/signUp":
				SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
				Calendar BirthDateSignUp= new GregorianCalendar();
				BirthDateSignUp.setTime(format2.parse(values2.get("birthDate")));
				@SuppressWarnings("static-access") int errorSignUp = bl.getUsers().registration(values2.get("username"), values2.get("password"), values2.get("password2"),BirthDateSignUp.YEAR, BirthDateSignUp.MONTH, BirthDateSignUp.DAY_OF_MONTH,
						values2.get("sex"), values2.get("phoneNumber"), values2.get("email"), values2.get("name"),
						values2.get("surname"), values2.get("country"), values2.get("city"), values2.get("address"), ip);
				if(errorSignUp == 0) {
					return RewriteUserHTML.RewriteLandingPage(cookies.get("language"), bl);
				}else if(errorSignUp == -5){
					return RewriteUserHTML.rewriteSignUp(cookies.get("language"), bl, "<h3><font color=\"red\">"+bundle.getString("PasswordMatch")+"</font><h3>");
				}else if(errorSignUp == 1) {
					return RewriteUserHTML.rewriteSignUp(cookies.get("language"), bl, "<h3><font color=\"red\">"+bundle.getString("UsernameInUse")+"</font><h3>");
				}else if(errorSignUp == 2) {
					return RewriteUserHTML.rewriteSignUp(cookies.get("language"), bl, "<h3><font color=\"red\">"+bundle.getString("EmailInUse")+"</font><h3>");
				}else if(errorSignUp == 3) {
					return RewriteUserHTML.rewriteSignUp(cookies.get("language"), bl, "<h3><font color=\"red\">"+bundle.getString("UserAndEmail")+"</font><h3>");
				}else if(errorSignUp == -2) {
					return RewriteUserHTML.rewriteSignUp(cookies.get("language"), bl, "<h3><font color=\"red\">"+bundle.getString("UsernameNotAllow")+"</font><h3>");
				}else if(errorSignUp == -6) {
						return RewriteUserHTML.rewriteSignUp(cookies.get("language"), bl, "<h3><font color=\"red\">"+bundle.getString("AtLeast18")+"</font><h3>");
				}else {
					return RewriteUserHTML.rewriteSignUp(cookies.get("language"), bl, "<h3><font color=\"red\">"+bundle.getString("NotYou")+"</font><h3>");
				}


				
			case "/userlogin":

				if (nKeys<2) {
					return RewriteUserHTML.RewriteLandingPage(cookies.get("language"), bl);
				}
				help.add("username=" + values2.get("username") + ";");
				String token = bl.getUsers().authentication(values2.get("username"), values2.get("password"));
				if (!token.equals("")) {
					help.add("token=" + token + ";");
					List<Event> emptyEventList = new LinkedList<Event>();
					currentPage.update(bl.getUsers().retrieveData(values2.get("username"), token),"Sports",bundle.getString("LastAvailableEvents"),"",
							bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
							 "","", cookies.get("language"));
					return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(values2.get("username"), token),"Sports",bundle.getString("LastAvailableEvents"),"",
							bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
							 "","", cookies.get("language"));
				}
				help.clear();
				RewriteUserHTML.RewriteLandingPage(cookies.get("language"), bl);
				

			case "/sendVerificationCode":
				if (nKeys<1) {
					return RewriteUserHTML.rewritePasswordRecoveryAsk(cookies.get("language"), bl);
				} else {
					bl.getEmail().resetPassword(values2.get("username"), cookies.get("language"));
					return RewriteUserHTML.RewriteLandingPage(cookies.get("language"), bl);
				}
				
			case "/recoverPassword":
				change = bl.getEmail().checkResetPassword(bl.getEmail().getUsernameVerifyLink(values2.get("code")), values2.get("verification"),
							values2.get("password"), values2.get("password2"));
				if(change) {
					return RewriteUserHTML.RewriteLandingPage(cookies.get("language"), bl);
				}else {
					return RewriteUserHTML.rewriteUserPasswordRecovery(values2.get("code"), cookies.get("language"), bl, "<h3><font color=\"red\">"+bundle.getString("ForSecurity")+"</font><h3>");
				}
				

			case "/EN/pointsmain":
				return RewriteUserHTML.pointsStore(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),cookies.get("language"));
				
				
			case "/addPoints":
				bl.getUsers().increaseFunds(cookies.get("username"), cookies.get("token"), Integer.parseInt(values2.get("pointsToAdd")), "Purchase Points");
				return RewriteUserHTML.pointsStore(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),cookies.get("language"));
				
				
			case "/cashout":
				return RewriteUserHTML.cashoutRewrite(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), cookies.get("language"));
				
			case "/removePoints":
				bl.getUsers().decreaseFunds(cookies.get("username"), cookies.get("token"), Integer.parseInt(values2.get("convertPoints")), "Withdraw points");
				return RewriteUserHTML.cashoutRewrite(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), cookies.get("language"));
				
				
			case "/userprofile":
				return RewriteUserHTML.rewriteProfileInfo(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), cookies.get("language"));
				
				
			case "/changePassword":
				bl.getUsers().updatePassword(cookies.get("username"), cookies.get("token"), values2.get("password1"), values2.get("password2"));
				return RewriteUserHTML.settingsLaunch(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"<font color=\"green\">"+bundle.getString("PasswordUpdated")+"</font>", cookies.get("language"));
				
			case "/updateinfo":
				List<String> newFavTeams = new LinkedList<String>();
				List<String> newFavSports = new LinkedList<String>();
				
				for (String s : values2.get("favouriteSports").split(";")) {
					newFavSports.add(s);
				}
				for (String s : values2.get("favouriteTeams").split(";")) {
					newFavTeams.add(s);
				}
				
				String[] infoMessage = {bundle.getString("Sex"), bundle.getString("PhoneNumber"), bundle.getString("Email"), bundle.getString("Name"), bundle.getString("Surname"), bundle.getString("Country"),
						bundle.getString("City"), bundle.getString("Address"), bundle.getString("FavouriteSports"), bundle.getString("FavouriteTeams")};
				List<Integer> error = bl.getUsers().updateUserData(cookies.get("username"), cookies.get("token"), values2.get("sex"), values2.get("phone"), values2.get("email"), values2.get("first_name"), values2.get("last_name"), values2.get("country"), values2.get("city"), values2.get("address"), newFavSports, newFavTeams);
				String resultInfo = "";
				for(int i=0; i<error.size();i++) {
					if(error.get(i) == -5) {
						continue;
					}else if(error.get(i) == 0) {
						resultInfo += "<font color=\"green\">"+infoMessage[i]+" "+bundle.getString("InfoUpdated")+"</font><br>";
					}else if(error.get(i) == 2 && i == 2) {
						resultInfo += "<font color=\"red\">"+infoMessage[i]+" "+bundle.getString("AlreadyInUse")+"</font><br>";
					}
				}
				
				return RewriteUserHTML.settingsLaunch(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), resultInfo, cookies.get("language"));
				
				
			case"/makebets":
				return RewriteUserHTML.rewriteMakeBet(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), bl.getEvents().getEventByID(Long.parseLong(values2.get("eventID"))), cookies.get("language"), bl, cookies.get("token"));
			
			case"/continueBet":
				String qAnswer = "";
				if(values2.get("mode").equals("UseValidSet")) {
					qAnswer = values[3].split("=")[1];
				}else {
					int qLength = Integer.parseInt(values[3].split("=")[1]);
					for(int i= 0; i<qLength-1; i++) {
						qAnswer += values[i+4].split("=")[1] + ",";
					}
					qAnswer += values[3+qLength].split("=")[1];
				}
				return RewriteUserHTML.rewriteContinueBet(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), bl.getEvents().getEventByID(Long.parseLong(values2.get("eventID"))),
						bl.getQuestions().getQuestionByID(Long.parseLong(values2.get("questionID"))),Long.parseLong(values2.get("betAmount")),qAnswer, cookies.get("language"));
					
			case "/submitBet":
				bl.getBets().storeNewBet(cookies.get("token"), Long.parseLong(values2.get("questionId")), Long.parseLong(values2.get("betAmount")), values2.get("prediction"), cookies.get("username"));
				return RewriteUserHTML.rewriteMakeBet(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), bl.getEvents().getEventByID(Long.parseLong(values2.get("eventID"))), cookies.get("language"), bl, cookies.get("token"));
				
			case "/backToQuestions":
				return RewriteUserHTML.rewriteMakeBet(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), bl.getEvents().getEventByID(Long.parseLong(values2.get("eventID"))), cookies.get("language"), bl, cookies.get("token"));
				
			case "/search":
				List<EventTag> tagsList = new ArrayList<EventTag>();
				EventTag[] allTags = EventTag.values();
				if(values2.get("eventType").equals("Sports")) {
					tagsList.add(EventTag.Sports);
				}
				if(!values2.get("searchTag").equals("")) {
					for(EventTag e: allTags) {
						if(e.toString().equals(values2.get("searchTag"))) {
							tagsList.add(e);
						}
					}
				}
				List<Event> eventsAll = bl.getEvents().getEventsByMultipleTags(tagsList);
				if(values2.get("eventType").equals("All") && values2.get("searchTag").equals("")) {
					eventsAll = bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE);
				}
				List<Event> events = new ArrayList<Event>();
				for (Event ev: eventsAll) {
					if(!ev.isCancelled() && !ev.isResolved() ) {
					if (!values2.get("mainSearchInput").equals("")) {
						if(ev.publicEventName.contains(values2.get("mainSearchInput")) || ev.deadlineToString().substring(0, 10).equals(values2.get("mainSearchInput")) ||
								ev.resolveToString().substring(0, 10).equals(values2.get("mainSearchInput"))) {
						events.add(ev);
						}
					}
					else {
						events.add(ev);
					}
					}
					
				}
				List<Event> emptyEventList = new LinkedList<Event>();
				if(!events.isEmpty()) {
				currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),bundle.getString("SearchResults"),"","",
						events,emptyEventList,emptyEventList,"","",cookies.get("language"));
				}
				else {
					currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),bundle.getString("SearchResults"),bundle.getString("NoMatchingResults"),"",
							events,emptyEventList,emptyEventList,"","",cookies.get("language"));
				}
				return RewriteUserHTML.rewriteMainpage(currentPage.getUser(),currentPage.getEventTypeMenu(), currentPage.getEventType(), currentPage.getEventSpecification(),
						currentPage.getEvents(),currentPage.getFirstTagGroup(),currentPage.getSecondTagGroup(), currentPage.getFirstTag(), currentPage.getSecondTag(), currentPage.getLanguage());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RewriteAdminHTML.readFile(WEB_ROOT, "/404.html");
		}

		return RewriteAdminHTML.readFile(WEB_ROOT, "/404.html");
	}

	// How to make a get Request:

	/*
	 * 1. Add to the userGetRequest a new case where the case "name" is the
	 * action name in the form 2. Make the rewrite if needed 3. return a string with
	 * the location of the file
	 */

	// -------------- GET ---------------
	/**
	 * This function selects the answer depending the get request
	 * @param fileRequested the file that the user request
	 * @param cookies cookies that use the user
	 * @param bl bl to help the communication between server and the business
	 *                logic
	 * @return HTML HTML code of the page requested
	 */
	static String userGetRequest(String fileRequested, Hashtable<String, String> cookies, BLFacadePublic bl) {
		String language = "/EN";
		Locale locale = new Locale(cookies.get("language"));
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas", locale);
		List<Event> emptyEventList = new ArrayList<Event>();
		int nrOfEvents = 50;
		if (cookies.containsKey("language")) {
			language = cookies.get("language");
		}
		
		if(!language.equals("ES") && !language.equals("EUS") && !language.equals("EN")) {
			return RewriteAdminHTML.readFile(WEB_ROOT, "/404.html");
		}
		

		if(fileRequested.length() == 6) {
			if(fileRequested.substring(0, fileRequested.length()-1).equals("/page")) {
				return RewriteUserHTML.changePageMainPage(currentPage.getUser(),currentPage.getEventTypeMenu(),
						currentPage.getEventType(), currentPage.getEventSpecification(),currentPage.getEvents(),
						currentPage.getFirstTagGroup(), currentPage.getSecondTagGroup(),currentPage.getFirstTag(),
						currentPage.getSecondTag(),currentPage.getLanguage(),Integer.parseInt(fileRequested.substring(5)));
			}
		}
		else if(fileRequested.length()==7) {
			if(fileRequested.substring(0, fileRequested.length()-2).equals("/page")) {
				return RewriteUserHTML.changePageMainPage(currentPage.getUser(),currentPage.getEventTypeMenu(),
						currentPage.getEventType(), currentPage.getEventSpecification(),currentPage.getEvents(),
						currentPage.getFirstTagGroup(), currentPage.getSecondTagGroup(),currentPage.getFirstTag(),
						currentPage.getSecondTag(),currentPage.getLanguage(),Integer.parseInt(fileRequested.substring(5)));
			}
		}
		else if(fileRequested.length()==8) {
			if(fileRequested.substring(0, fileRequested.length()-3).equals("/page")) {
				return RewriteUserHTML.changePageMainPage(currentPage.getUser(),currentPage.getEventTypeMenu(),
						currentPage.getEventType(), currentPage.getEventSpecification(),currentPage.getEvents(),
						currentPage.getFirstTagGroup(), currentPage.getSecondTagGroup(),currentPage.getFirstTag(),
						currentPage.getSecondTag(),currentPage.getLanguage(),Integer.parseInt(fileRequested.substring(5)));
			}
		}
		switch (fileRequested) {
		
		case "/mainPage":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
					 "","", cookies.get("language"));
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
					 "","", cookies.get("language"));
			
		case "/purchasepoints":
			return RewriteUserHTML.pointsStore(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), cookies.get("language"));
			
		case "/userpayments":
			return RewriteUserHTML.updateUserPayments(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),
					bl.getTransactions().getLastTransactions(cookies.get("username"), cookies.get("token"), nrOfEvents), cookies.get("language"));
			
		case "/cashout":
			return RewriteUserHTML.cashoutRewrite(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),cookies.get("language"));
			
		case "/settings":
			return RewriteUserHTML.settingsLaunch(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"", cookies.get("language"));
			
		case "/userprofile":
			return RewriteUserHTML.rewriteProfileInfo(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), cookies.get("language"));
			
		case "/userbets":
			return RewriteUserHTML.betInfo(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")), bl.getBets().getNBetsByUser(cookies.get("username"), cookies.get("token"), 200), bl, cookies.get("language"));
			
		case "/logout":
			bl.getUsers().logOut(cookies.get("username"), cookies.get("token"));
			return RewriteUserHTML.RewriteLandingPage(cookies.get("language"), bl);
			
		//Event type menu
		case "/sports":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
					 "","", cookies.get("language"));
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
					 "","", cookies.get("language"));
			
		case "/culture":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Culture","Soccer","Competitions",
					bl.getEvents().getEventsByTag(cookies.get("username"),"Soccer"), emptyEventList, emptyEventList,"","",language);
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Culture","Soccer","Competitions",
					bl.getEvents().getEventsByTag(cookies.get("username"),"Soccer"), emptyEventList, emptyEventList,"","",language);
		
			
		
			
		//Submenu
		case "/soccer/competitions":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports","Soccer","Competitions",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"),EVENTES_PER_PAGE,"Soccer"),bl.getEvents().getEventsByTag(cookies.get("username"),"LaLiga"),
					bl.getEvents().getEventsByTag(cookies.get("username"),"Calcio"),"LaLiga","Calcio",language);
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports","Soccer","Competitions",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"),EVENTES_PER_PAGE,"Soccer"),bl.getEvents().getEventsByTag(cookies.get("username"),"LaLiga"),
					bl.getEvents().getEventsByTag(cookies.get("username"),"Calcio"),"LaLiga","Calcio",language);
			
		case "/basketball/competitions":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports","Basketball","Competitions",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"),EVENTES_PER_PAGE,"Basketball"),bl.getEvents().getEventsByTag(cookies.get("username"),"PBL"),
					bl.getEvents().getEventsByTag(cookies.get("username"),"TBL"),"PBL","TBL",language);
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports","Basketball","Competitions",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"),EVENTES_PER_PAGE,"Basketball"),bl.getEvents().getEventsByTag(cookies.get("username"),"PBL"),
					bl.getEvents().getEventsByTag(cookies.get("username"),"TBL"),"PBL","TBL",language);
			
		case "/football/competitions":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports","Football","Competitions",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE, "Football"),bl.getEvents().getEventsByTag(cookies.get("username"),"NFL"),
					bl.getEvents().getEventsByTag(cookies.get("username"),"AAFL"),"NFL","AAFL",language);
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports","Football","Competitions",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE, "Football"),bl.getEvents().getEventsByTag(cookies.get("username"),"NFL"),
					bl.getEvents().getEventsByTag(cookies.get("username"),"AAFL"),"NFL","AAFL",language);
			
		case "/cycling/competitions":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports","Cycling","Competitions",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"),EVENTES_PER_PAGE, "Cycling"),bl.getEvents().getEventsByTag(cookies.get("username"),"Tour de France"),
					bl.getEvents().getEventsByTag(cookies.get("username"),"Tour of Flanders"),"Tour de France","Tour of Flanders",language);
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports","Cycling","Competitions",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"),EVENTES_PER_PAGE, "Cycling"),bl.getEvents().getEventsByTag(cookies.get("username"),"Tour de France"),
					bl.getEvents().getEventsByTag(cookies.get("username"),"Tour of Flanders"),"Tour de France","Tour of Flanders",language);
			
		case "/baseball/competitions":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports","Baseball","Competitions",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"),EVENTES_PER_PAGE,"Baseball"),bl.getEvents().getEventsByTag(cookies.get("username"),"MLB"),
					bl.getEvents().getEventsByTag(cookies.get("username"),"ELB"),"MLB","ELB",language);
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports","Baseball","Competitions",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"),EVENTES_PER_PAGE,"Baseball"),bl.getEvents().getEventsByTag(cookies.get("username"),"MLB"),
					bl.getEvents().getEventsByTag(cookies.get("username"),"ELB"),"MLB","ELB",language);
		
		case "/changeLanguage/ES":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
					 "","", cookies.get("language"));
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
					 "","", cookies.get("language"));
		case "/changeLanguage/EN":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
					 "","", cookies.get("language"));
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
					 "","", cookies.get("language"));
		case "/changeLanguage/EUS":
			currentPage.update(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
					 "","", cookies.get("language"));
			return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
					bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
					 "","", cookies.get("language"));
			
		
		}

		return RewriteUserHTML.rewriteMainpage(bl.getUsers().retrieveData(cookies.get("username"), cookies.get("token")),"Sports",bundle.getString("LastAvailable"),"",
				bl.getEvents().getNEventsByCanBet(cookies.get("username"), EVENTES_PER_PAGE),emptyEventList, emptyEventList,
				 "","", cookies.get("language"));
		
		
	}
}