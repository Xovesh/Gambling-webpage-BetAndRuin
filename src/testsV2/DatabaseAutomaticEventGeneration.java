package testsV2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import businessLogicV3.privateAPI.BLFacadePrivate;
import businessLogicV3.publicAPI.BLFacadePublic;
import domainV2.event.EventTag;
import domainV2.util.Language;
import domainV2.util.Translation;

public class DatabaseAutomaticEventGeneration {
	@SuppressWarnings({ "unused", "deprecation" })
	public static void main(String[] args) {
		String apiKey = "cd4m6kz7hp47b7wfncjuatta";
		String premierID = "sr:tournament:17";
		String premierSeason = "sr:season:54571";
		String Bundesliga = "sr:tournament:35";
		String serieA = "sr:tournament:23";
		String bundesligaURLRequest = "https://api.sportradar.us/soccer-t3/eu/en/tournaments/" + Bundesliga
				+ "/schedule.xml?api_key=" + apiKey;

		BLFacadePublic s = new BLFacadePublic();
		BLFacadePrivate bl = s.getPrivateAPI();

		File file = new File("./resources/test.xml");
		try {
			// Get matches
			getXMLToFile(bundesligaURLRequest, file);
			// Get all match ids
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			List<String> matchIds = new LinkedList<>();
			for (Node n : nodeListToList(document.getDocumentElement().getChildNodes().item(1).getChildNodes())) {
				matchIds.add(n.getAttributes().getNamedItem("id").getTextContent());
				System.out.println(matchIds.get(matchIds.size() - 1));
			}

			// Get all match stats
			for (String matchId : matchIds) {
				String matchProbsRequest = "https://api.sportradar.us/soccer-t3/eu/en/matches/" + matchId
						+ "/probabilities.xml?api_key=" + apiKey;
				float homeWin, tie, awayWin;
				getXMLToFile(matchProbsRequest, file);
				document = documentBuilder.parse(file);
				// Match Names
				String homeTeam = document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(3)
						.getChildNodes().item(0).getAttributes().getNamedItem("name").getTextContent();
				String awayTeam = document.getDocumentElement().getChildNodes().item(0).getChildNodes().item(3)
						.getChildNodes().item(1).getAttributes().getNamedItem("name").getTextContent();
				// Probabilities
				homeWin = Float.parseFloat(document.getDocumentElement().getChildNodes().item(1).getChildNodes().item(0)
						.getChildNodes().item(0).getAttributes().getNamedItem("probability").getTextContent());
				tie = Float.parseFloat(document.getDocumentElement().getChildNodes().item(1).getChildNodes().item(0)
						.getChildNodes().item(1).getAttributes().getNamedItem("probability").getTextContent());
				awayWin = Float.parseFloat(document.getDocumentElement().getChildNodes().item(1).getChildNodes().item(0)
						.getChildNodes().item(2).getAttributes().getNamedItem("probability").getTextContent());
				// Match date
				String dateString = document.getDocumentElement().getChildNodes().item(0).getAttributes()
						.getNamedItem("scheduled").getTextContent();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH);
				Date date = null;
				try {
					date = format.parse(dateString);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				// Output
				System.out.println("Match:" + homeTeam + "-" + awayTeam + " At " + date + " Probabilities: homeWin "
						+ homeWin + ", tie " + tie + ", awayWin " + awayWin);
				// Event Creation
				String eventName = homeTeam + "-" + awayTeam, yearIdentifier = Integer.toString(date.getYear() + 1900);
				Translation t = new Translation("This is event " + eventName + " for " + yearIdentifier);
				t.setTranslationText(Language.EN, t.getDefaultText());
				t.setTranslationText(Language.ES, "Este es el evento " + eventName + " para " + yearIdentifier);
				t.setTranslationText(Language.EUS, "Hau da " + eventName + " " + yearIdentifier + " urterako");

				List<EventTag> soccerTags = new LinkedList<>();
				soccerTags.add(EventTag.Sports);
				soccerTags.add(EventTag.Europe);
				soccerTags.add(EventTag.Germany);
				soccerTags.add(EventTag.Soccer);

				Calendar deadline = new GregorianCalendar();
				deadline.setTime(date);
				Calendar close = deadline;
				close.add(Calendar.DAY_OF_YEAR, -2);
				long eventID = bl.getEvents().createEvent(eventName + " " + yearIdentifier, close, deadline, t,
						soccerTags);

				Translation tq = new Translation("Who wins?");
				tq.setTranslationText(Language.ES, "Quien gana?");
				tq.setTranslationText(Language.EN, "Who wins?");
				tq.setTranslationText(Language.EUS, "Nork irabaziko du?");

				List<String> validSet = new LinkedList<String>();
				validSet.add("1");
				validSet.add("X");
				validSet.add("2");
				List<Float> payouts = new LinkedList<>();
				payouts.add((float) (1f + (((0.9 * 100) * ((homeWin / 100) / 2 - 1)) + 100) / 100));
				payouts.add((float) (1f + (((0.9 * 100) * ((tie / 100) / 2 - 1)) + 100) / 100));
				payouts.add((float) (1f + (((0.9 * 100) * ((awayWin / 100) / 2 - 1)) + 100) / 100));
				long q1Id = bl.getQuestions().addQuestionValidSet(eventID, tq, "Who wins?", validSet, payouts);
				Thread.sleep(1500);
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Node> nodeListToList(NodeList in) {
		List<Node> out = new LinkedList<>();
		for (int i = 0; i < in.getLength(); i++) {
			out.add(in.item(i));
		}
		return out;
	}

	public static List<Node> nodeListToList(NamedNodeMap namedNodeMap) {
		List<Node> out = new LinkedList<>();
		for (int i = 0; i < namedNodeMap.getLength(); i++) {
			out.add(namedNodeMap.item(i));
		}
		return out;
	}

	public String xmlRequest(String pin) {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
				+ "  <soap:Body>\n" + "    <GetUserInfo xmlns=\"http://tempuri.org/\">\n" + "      <pin>" + pin
				+ "</pin>\n" + "    </GetUserInfo>\n" + "  </soap:Body>\n" + "</soap:Envelope>";
	}

	public static void getXMLToFile(String uri, File f) {
		System.out.println("Making XML request . . .");
		try {
			String postData = new DatabaseAutomaticEventGeneration().xmlRequest("QWERTY10");

			URL url = new URL(uri);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.write(postData.getBytes());
			wr.close();

			InputStream xml = connection.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(xml));
			String line = "";
			String xmlResponse = "";
			while ((line = reader.readLine()) != null) {
				xmlResponse += line;
			}

			FileWriter fileWriter = new FileWriter(f);
			fileWriter.write(xmlResponse);
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
