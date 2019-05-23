package configuration;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * It provides the configuration data from the "resources/config.xml" XML file
 */
public class ConfigXML {

	private String configFile = "resources/config.xml";

	private String businessLogicNode;

	private String businessLogicPort;

	private String businessLogicName;

	private static String dbFilename;

	//Two possible values: "open" or "initialize"
	private String dataBaseOpenMode;

	//Two possible values: true (no instance of RemoteServer needs to be launched)
	// or false (RemoteServer needs to be run first)
	private boolean businessLogicLocal;

	//Two possible values: true (if the database is in same node as business logic)
	// or false (in other case)
	private boolean databaseLocal;

	private String databaseNode;

	private int databasePort;

	private String user;

	private String password;

	private String locale;

	public String getLocale() {
		return locale;
	}

	public int getDatabasePort() {
		return databasePort;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public boolean isDatabaseLocal() {
		return databaseLocal;
	}

	public boolean isBusinessLogicLocal() {
		return businessLogicLocal;
	}
	private static ConfigXML theInstance = new ConfigXML();

	private ConfigXML(){

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(configFile));
			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName("config");
			Element config = (Element) list.item(0); // list.item(0) is a Node that is an Element


			//Two possible values: true (no instance of RemoteServer needs to be launched)
			// or false (RemoteServer needs to be run first)
			String value= ((Element)config.getElementsByTagName("businessLogic").item(0))
					.getAttribute("local");
			businessLogicLocal=value.equals("true");

			businessLogicNode = getTagValue("businessLogicNode", config);

			businessLogicPort = getTagValue("businessLogicPort", config);

			businessLogicName = getTagValue("businessLogicName", config);

			locale = getTagValue("locale", config);

			dbFilename = getTagValue("dbFilename", config);

			//Two possible values: true (no instance of RemoteServer needs to be launched)
			// or false (RemoteServer needs to be run first)
			value= ((Element)config.getElementsByTagName("database").item(0)).getAttribute("local");
			databaseLocal=value.equals("true");

			//Two possible values: "open" or "initialize"
			dataBaseOpenMode= getTagValue("dataBaseOpenMode", config);


			databaseNode = getTagValue("databaseNode", config);

			databasePort=Integer.parseInt(getTagValue("databasePort", config));

			user=getTagValue("user", config);

			password=getTagValue("password", config);

			System.out.print("Read from config.xml: ");
			System.out.print("\t businessLogicLocal=" + businessLogicLocal);
			System.out.print("\t databaseLocal=" + databaseLocal);
			System.out.println("\t dataBaseOpenMode=" + dataBaseOpenMode); 

		} catch (Exception e) {
			System.out.println("Error in ConfigXML.java: problems with " + configFile);
			e.printStackTrace();
		}
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList= eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}

	public static ConfigXML getInstance() {
		return theInstance;
	}

	public String getBusinessLogicNode() {
		return businessLogicNode;
	}

	public String getBusinessLogicPort() {
		return businessLogicPort;
	}

	public String getBusinessLogicName() {
		return businessLogicName;
	}

	public String getDbFilename(){
		return dbFilename;
	}

	public String getDataBaseOpenMode(){
		return dataBaseOpenMode;
	}

	public String getDatabaseNode() {
		return databaseNode;
	}
}
