package domainV2.event;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class SoccerDataCollector extends EventDataCollector {

	@Override
	public void generateEventQuestions() {
		// TODO Auto-generated method stub

	}

	@Override
	public void answerGeneratedQuestions() throws Exception {

		File file = new File(apiEventId + "_timeline.xml");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		@SuppressWarnings("unused")
		String winnerID = document.getElementsByTagName("sport_event_status winner_id").item(0).getTextContent();
		@SuppressWarnings("unused")
		String pwd = document.getElementsByTagName("password").item(0).getTextContent();

	}

}
