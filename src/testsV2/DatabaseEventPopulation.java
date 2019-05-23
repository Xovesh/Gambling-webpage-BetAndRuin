package testsV2;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import businessLogicV3.privateAPI.BLFacadePrivate;
import businessLogicV3.publicAPI.BLFacadePublic;
import domainV2.User;
import domainV2.event.EventTag;
import domainV2.util.Language;
import domainV2.util.Translation;

/**
 * The objective of this class is to create some events for testing
 *
 */
public class DatabaseEventPopulation {
	static final int SOCCER = 30, BASKETBALL = 30, FOOTBALL = 30, BASEBALL = 30, CYCLING = 30;
	static final int SOCCER_BETS = 40, BASKETBALL_BETS = 40, FOOTBALL_BETS = 40, BASEBALL_BETS = 40, CYCLING_BETS = 40;
	static Random r = new Random();
	static BLFacadePublic s = new BLFacadePublic();
	static BLFacadePrivate bl = s.getPrivateAPI();

	static String yearIdentifier = "2018-2019";
	static List<User> users = bl.getUsers().retrieveAllUsers();
	static List<String> validSet = new LinkedList<String>();
	// SOCCER---------------------------------------------------------------------------------------------------------------
	static String[] soccerTeams = { "Alavés", "Athletic", "Atlético Madrid", "Barcelona", "Betis", "Celta", "Eibar",
			"Espanyol", "Getafe", "Girona", "Huesca", "Leganés", "Levante", "Rayo Vallecano", "Real Madrid",
			"Real Sociedad", "Sevilla", "Valencia", "Valladolid", "Villarreal" };
	static List<EventTag> soccerTags = new LinkedList<>();
	// BASKETBALL-----------------------------------------------------------------------------------------------------------
	static String[] basketTeams = { "Barça Lassa", "BAXI Manresa", "Cafés Candelas Breogán", "Delteco GBC",
			"Divina Seguros Joventut", "Herbalife Gran Canaria", "Iberostar Tenerife", "Kirolbet Baskonia",
			"Monbus Obradoiro", "Montakit Fuenlabrada", "MoraBanc Andorra", "Movistar Estudiantes", "Real Madrid",
			"San Pablo Burgos", "Tecnyconta Zaragoza", "Unicaja", "Universidad Católica de Murcia", "Valencia Basket" };
	static List<EventTag> basketballTags = new LinkedList<>();
	// FOOTBALL-------------------------------------------------------------------------------------------------------------
	static String[] footballTeams = { "Arizona Cardinals", "Atlanta Falcons", "Baltimore Ravens", "Buffalo Bills",
			"Carolina Panthers", "Chicago Bears", "Cincinnati Bengals", "Cleveland Browns", "Dallas Cowboys",
			"Denver Broncos", "Detroit Lions", "Green Bay Packers", "Houston Texans", "Indianapolis Colts",
			"Jacksonville Jaguars", "Kansas City Chiefs", "Los Angeles Chargers", "Los Angeles Rams", "Miami Dolphins",
			"Minnesota Vikings", "New England Patriots", "New Orleans Saints", "New York Giants", "New York Jets",
			"Oakland Raiders", "Philadelphia Eagles", "Pittsburgh Steelers", "San Francisco 49ers", "Seattle Seahawks",
			"Tampa Bay Buccaneers", "Tennessee Titans", "Washington Redskins" };
	static List<EventTag> footballTags = new LinkedList<>();
	// CYCLING--------------------------------------------------------------------------------------------------------------
	static String[] cyclingTeams = { "AG2R LA MONDIALE", "ASTANA PRO TEAM", "BAHRAIN – MERIDA", "BORA – HANSGROHE",
			"CCC TEAM", "DECEUNINCK – QUICK-STEP", "EF EDUCATION FIRST", "GROUPAMA – FDJ", "LOTTO SOUDAL",
			"MOVISTAR TEAM", "MITCHELTON – SCOTT", "TEAM DIMENSION DATA", "TEAM JUMBO-VISMA", "TEAM KATUSHA ALPECIN",
			"TEAM SKY", "TEAM SUNWEB", "TREK-SEGAFREDO", "UAE TEAM EMIRATES" };
	static List<EventTag> cyclingTags = new LinkedList<>();
	static String[] frenchCities = { "Colmar", "Haguenau", "Mulhouse", "Ribeauvillé", "Strasbourg", "Agen", "Bayonne",
			"Bergerac", "Biarritz", "Bordeaux", "Dax", "Lacq", "Libourne", "Mont-de-Marsan", "Pau", "Périgueux",
			"Pessac", "Saint-Jean-de-Luz", "Talence", "Aurillac", "Clermont-Ferrand", "Le Puy-en-Velay", "Montluçon",
			"Moulins", "Riom", "Vichy", "Alençon", "Arromanches", "Avranches", "Bayeux", "Bénouville", "Caen",
			"Cherbourg", "Courseulles", "Coutances", "Deauville", "Falaise", "Grandcamp-Maisy", "Granville", "Honfleur",
			"Lisieux", "Ouistreham", "Saint-Lô", "Sainte-Marie-du-Mont", "Sainte-Mère-Église", "Trouville", "Auray",
			"Brest", "Carnac", "Dinan", "Dinard", "Douarnenez", "Fougères", "Guingamp", "Locmariaquer", "Lorient",
			"Morlaix", "Quimper", "Rennes", "Saint-Brieuc", "Saint-Malo", "Vannes", "Autun", "Auxerre", "Beaune",
			"Chalon-sur-Saône", "Cîteaux", "Cluny", "Dijon", "Le Creusot", "Mâcon", "Nevers", "Sens", "Vézelay",
			"Amboise", "Azay-le-Rideau", "Beaugency", "Blois", "Bourges", "Chambord", "Chartres", "Châteauroux",
			"Chenonceaux", "Chinon", "Dreux", "Langeais", "Loches", "Orléans", "Saint-Amand-Montrond",
			"Saint-Benoît-sur-Loire", "Sancerre", "Tours", "Vendôme", "Vierzon", "Villandry", "Châlons-en-Champagne",
			"Charleville-Mézières", "Chaumont", "Clairvaux", "Épernay", "Langres", "Reims", "Rocroi", "Saint-Dizier",
			" Sedan", "Troyes", "Ajaccio", "Bastia", "Bonifacio", "Corte", "Belfort", "Besançon", "Dole",
			"Lons-le-Saunier", "Montbéliard", "Vesoul", "Cayenne", "Kourou", "Mana", "Saint-Laurent du Maroni",
			"Basse-Terre", "Pointe-à-Pitre", "Dieppe", "Elbeuf", "Évreux", "Fécamp", "Gisors", "Jumièges", "Le Havre",
			"Le Petit-Quevilly", "Lillebonne", "Rouen", "Argenteuil", "Athis-Mons", "Champigny-sur-Marne",
			"Charenton-le-Pont", "Châtillon", "Chatou", "Chelles", "Clichy", "Colombes", "Corbeil-Essonnes",
			"Courbevoie", "Créteil", "Drancy", "Épinay-sur-Seine", "Étampes", "Évry", "Fontainebleau", "Fresnes",
			"Gagny", "Gennevilliers", "Issy-les-Moulineaux", "Ivry-sur-Seine", "Levallois-Perret", "Malakoff", "Meaux",
			"Melun", "Meudon", "Montreuil", "Montrouge", "Nanterre", "Nemours", "Neuilly-sur-Seine", "Paris", "Poissy",
			"Pontoise", "Provins", "Puteaux", "Rambouillet", "Rueil-Malmaison", "Saint-Cloud", "Saint-Denis",
			"Saint-Germain-en-Laye", "Saint-Maur-des-Fossés", "Saint-Ouen", "Sénart", "Sèvres", "Suresnes",
			"Versailles", "Villejuif", "Villeneuve-Saint-Georges", "Vincennes", "Viry-Châtillon", "Vitry-sur-Seine",
			"Aigues-Mortes", "Alès", "Beaucaire", "Béziers", "Carcassonne", "Mende", "Montpellier", "Narbonne", "Nîmes",
			"Perpignan", "Sète", "Aubusson", "Brive-la-Gaillarde", "Guéret", "Limoges", "Oradour-sur-Glane",
			"Saint-Yrieix-la-Perche", "Tulle", "Bar-le-Duc", "Domrémy-la-Pucelle", "Épinal", "Forbach", "Longwy",
			"Lunéville", "Metz", "Nancy", "Remiremont", "Saint-Dié", "Saint-Mihiel", "Thionville", "Toul", "Verdun",
			"Fort-de-France", "La Trinité", "Saint-Pierre", "Albi", "Auch", "Cahors Castres", "Foix", "Gavarnie",
			"Lourdes", "Millau", "Montauban", "Rocamadour", "Rodez", "Saint-Affrique", "Tarbes", "Toulouse",
			"Armentières", "Arras", "Béthune", "Boulogne", "Calais", "Cambrai", "Croix", "Douai", "Dunkirk",
			"Gravelines", "Henin-Beaumont", "Hesdin", "Le Touquet-Paris-Plage", "Lens", "Liévin", "Lille",
			"Marcq-en-Baroeul", "Maubeuge", "Roubaix", "Saint-Amand-les-Eaux", "Saint-Omer", "Tourcoing",
			"Valenciennes", "Wattrelos", "Angers", "Cholet", "Fontevrault-l’Abbaye", "La Baule-Escoublac",
			"La Roche-sur-Yon", "Laval", "Le Mans", "Nantes", "Rezé", "Saint-Nazaire", "Saumur", "Solesmes",
			"Abbeville", "Amiens", "Beauvais", "Chantilly", "Château-Thierry", "Compiègne", "Coucy", "Creil", "Ham",
			"Laon", "Noyon", "Saint-Quentin", "Senlis", "Soissons", "Angoulême", "Châtellerault", "Cognac",
			"La Rochelle", "Niort", "Poitiers", "Rochefort", "Aix-en-Provence", "Antibes", "Arles", "Aubagne",
			"Avignon", "Briançon", "Cannes", "Digne-les-Bains", "Fos", "Fréjus", "Gap", "Grasse", "Hyères",
			"La Seyne-sur-Mer", "Les Baux-en-Provence", "Marseille", "Martigues", "Menton", "Nice", "Orange",
			"Saintes-Maries-de-la-Mer", "Salon-de-Provence", "Tarascon", "Villefranche-sur-Mer", "Saint-Denis",
			"Aix-les-Bains", "Annecy", "Bourg-en-Bresse", "Chambéry", "Chamonix-Mont-Blanc", "Courchevel",
			"Évian-les-Bains", "Firminy", "Grenoble", "Le Chambon-Feugerolles", "Lyon", "Montélimar", "Oullins",
			"Roanne", "Romans-sur-Isère", "Saint-Étienne", "Thonon-les-Bains", "Valence", "Villefranche-sur-Saône",
			"Villeurbanne" };
	// BASEBALL-------------------------------------------------------------------------------------------------------------
	static String[] baseballTeams = { "Baltimore Orioles", "Boston Red Sox", "New York Yankees", "Tampa Bay Rays",
			"Toronto Blue Jays", "Atlanta Braves", "Miami Marlins", "New York Mets", "Philadelphia Phillies",
			"Washington Nationals", "Chicago White Sox", "Cleveland Indians", "Detroit Tigers", "Kansas City Royals",
			"Minnesota Twins", "Chicago Cubs", "Cincinnati Reds", "Milwaukee Brewers", "Pittsburgh Pirates",
			"St. Louis Cardinals", "Houston Astros", "Los Angeles Angels", "Oakland Athletics", "Seattle Mariners",
			"Texas Rangers", "Arizona Diamondbacks", "Colorado Rockies", "Los Angeles Dodgers", "San Diego Padres",
			"San Francisco Giants" };
	static List<EventTag> baseballTags = new LinkedList<>();
	// ---------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		initializeLists();

		// EVENT CREATION
		for (int i = 0; i < FOOTBALL; i++) {
			createSoccerEvent();
		}

		// BASKETBALL EVENT CREATION
		for (int i = 0; i < BASKETBALL; i++) {
			createBasketballEvent();
		}

		// BASEBALL EVENT CREATION
		for (int i = 0; i < BASEBALL; i++) {
			createBaseballEvent();
		}

		// FOOTBALL EVENT CREATION
		for (int i = 0; i < FOOTBALL; i++) {
			createFootballEvent();
		}
		// CYCLING EVENT CREATION
		for (int i = 0; i < CYCLING; i++) {
			createTourStage();
		}

	}

	public static void initializeLists() {
		validSet.add("1");
		validSet.add("2");
		validSet.add("X");
		soccerTags.add(EventTag.Sports);
		soccerTags.add(EventTag.Europe);
		soccerTags.add(EventTag.Spain);
		soccerTags.add(EventTag.Soccer);
		soccerTags.add(EventTag.LaLiga);
		basketballTags.add(EventTag.Sports);
		basketballTags.add(EventTag.Basketball);
		basketballTags.add(EventTag.Europe);
		basketballTags.add(EventTag.Spain);
		footballTags.add(EventTag.Sports);
		footballTags.add(EventTag.Football);
		footballTags.add(EventTag.NFL);
		footballTags.add(EventTag.America);
		cyclingTags.add(EventTag.Sports);
		cyclingTags.add(EventTag.Cycling);
		cyclingTags.add(EventTag.France);
		baseballTags.add(EventTag.Sports);
		baseballTags.add(EventTag.Baseball);
		baseballTags.add(EventTag.MLB);
		baseballTags.add(EventTag.America);
	}

	public static String chooseTwoUnique(String[] source) {
		int a = r.nextInt(source.length), b = a;
		while (b == a)
			b = r.nextInt(source.length);
		return source[a] + "-" + source[b];
	}

	public static Calendar generateAfter(Calendar c) {
		Calendar out = c;
		out.add(Calendar.DAY_OF_MONTH, r.nextInt(15) + 2);
		out.add(Calendar.MONTH, r.nextInt(6) + 2);
		return out;
	}

	public static void createSoccerEvent() {
		// ADDING EVENT
		String eventName = chooseTwoUnique(soccerTeams);
		Calendar deadline = generateAfter(new GregorianCalendar());
		Translation t = new Translation("This is event " + eventName + " for " + yearIdentifier);
		t.setTranslationText(Language.EN, t.getDefaultText());
		t.setTranslationText(Language.ES, "Este es el evento " + eventName + " para " + yearIdentifier);
		t.setTranslationText(Language.EUS, "Hau da " + eventName + " " + yearIdentifier + " urterako");
		long eventID = bl.getEvents().createEvent(eventName + " " + yearIdentifier, deadline, generateAfter(deadline),
				t, soccerTags);
		// ADDING QUESTIONS
		Translation tq = new Translation("Who wins?");
		tq.setTranslationText(Language.ES, "Quien gana?");
		tq.setTranslationText(Language.EN, "Who wins?");
		tq.setTranslationText(Language.EUS, "Nork irabaziko du?");
		Translation t2 = new Translation("End result?");
		t2.setTranslationText(Language.ES, "Resultado final?");
		t2.setTranslationText(Language.EN, "End result?");
		t2.setTranslationText(Language.EUS, "Azken emaitza?");
		List<Float> validSetValues = new LinkedList<Float>();
		Random r = new Random();
		float max = 2.5F;
		float min = 1.1F;
		for (int i = 0; i < validSet.size(); i++) {
			validSetValues.add(min + r.nextFloat() * (max - min));
		}
		int[] val = { r.nextInt(3), r.nextInt(3) };
		long q1Id = bl.getQuestions().addQuestionValidSet(eventID, tq, "Who wins?", validSet, validSetValues);
		long q2Id = bl.getQuestions().addQuestionNumeric(eventID, t2, "End result?", val, 10);
		// ADDING BETS
		for (int j = 0; j < SOCCER_BETS; j++) {
			bl.getBets().storeNewBet(q1Id, r.nextInt(600) + 250, validSet.get(r.nextInt(3)),
					users.get(r.nextInt(users.size())).getUsername());
			List<Integer> prediction = new LinkedList<Integer>();
			prediction.add(r.nextInt(12));
			prediction.add(r.nextInt(12));
			bl.getBets().storeNewBet(
					q2Id, r.nextInt(600) + 250, prediction.toString().replace('[', Character.MIN_VALUE)
							.replace(']', Character.MIN_VALUE).replaceAll("\\s+", "").trim(),
					users.get(r.nextInt(users.size())).getUsername());
		}
	}

	public static void createBasketballEvent() {
		// ADDING EVENT
		String eventName = chooseTwoUnique(basketTeams);
		Calendar deadline = generateAfter(new GregorianCalendar());
		Translation t = new Translation("This is event " + eventName + " for " + yearIdentifier);
		t.setTranslationText(Language.EN, t.getDefaultText());
		t.setTranslationText(Language.ES, "Este es el evento " + eventName + " para " + yearIdentifier);
		t.setTranslationText(Language.EUS, "Hau da " + eventName + " " + yearIdentifier + " urterako");
		long eventID = bl.getEvents().createEvent(eventName + " " + yearIdentifier, deadline, generateAfter(deadline),
				t, basketballTags);
		Translation tq = new Translation("Who wins?");
		tq.setTranslationText(Language.ES, "Quien gana?");
		tq.setTranslationText(Language.EN, "Who wins?");
		tq.setTranslationText(Language.EUS, "Nork irabaziko du?");
		Translation t2 = new Translation("End result?");
		t2.setTranslationText(Language.ES, "Resultado final?");
		t2.setTranslationText(Language.EN, "End result?");
		t2.setTranslationText(Language.EUS, "Azken emaitza?");
		// ADDING QUESTIONS
		List<Float> validSetValues = new LinkedList<Float>();
		Random r = new Random();
		float max = 2.5F;
		float min = 1.1F;
		for (int i = 0; i < validSet.size(); i++) {
			validSetValues.add(min + r.nextFloat() * (max - min));
		}
		int[] val = { r.nextInt(200), r.nextInt(200) };
		long q1Id = bl.getQuestions().addQuestionValidSet(eventID, tq, "Who wins?", validSet, validSetValues);
		long q2Id = bl.getQuestions().addQuestionNumeric(eventID, t2, "End result?", val, 350);

		// ADDING BETS
		for (int j = 0; j < BASKETBALL_BETS; j++) {
			bl.getBets().storeNewBet(q1Id, r.nextInt(600) + 250, validSet.get(r.nextInt(3)),
					users.get(r.nextInt(users.size())).getUsername());
			List<Integer> prediction = new LinkedList<Integer>();
			prediction.add(r.nextInt(150));
			prediction.add(r.nextInt(150));
			bl.getBets().storeNewBet(
					q2Id, r.nextInt(600) + 250, prediction.toString().replace('[', Character.MIN_VALUE)
							.replace(']', Character.MIN_VALUE).replaceAll("\\s+", "").trim(),
					users.get(r.nextInt(users.size())).getUsername());
		}
	}

	public static void createFootballEvent() {
		// ADDING EVENT
		String eventName = chooseTwoUnique(footballTeams);
		Calendar deadline = generateAfter(new GregorianCalendar());
		Translation t = new Translation("This is event " + eventName + " for " + yearIdentifier);
		t.setTranslationText(Language.EN, t.getDefaultText());
		t.setTranslationText(Language.ES, "Este es el evento " + eventName + " para " + yearIdentifier);
		t.setTranslationText(Language.EUS, "Hau da " + eventName + " " + yearIdentifier + " urterako");
		long eventID = bl.getEvents().createEvent(eventName + " " + yearIdentifier, deadline, generateAfter(deadline),
				t, footballTags);
		Translation tq = new Translation("Who wins?");
		tq.setTranslationText(Language.ES, "Quien gana?");
		tq.setTranslationText(Language.EN, "Who wins?");
		tq.setTranslationText(Language.EUS, "Nork irabaziko du?");
		Translation t2 = new Translation("End result?");
		t2.setTranslationText(Language.ES, "Resultado final?");
		t2.setTranslationText(Language.EN, "End result?");
		t2.setTranslationText(Language.EUS, "Azken emaitza?");
		// ADDING QUESTIONS
		List<Float> validSetValues = new LinkedList<Float>();
		Random r = new Random();
		float max = 2.5F;
		float min = 1.1F;
		for (int i = 0; i < validSet.size(); i++) {
			validSetValues.add(min + r.nextFloat() * (max - min));
		}
		int[] val = { r.nextInt(3), r.nextInt(3) };
		long q1Id = bl.getQuestions().addQuestionValidSet(eventID, tq, "Who wins?", validSet, validSetValues);
		long q2Id = bl.getQuestions().addQuestionNumeric(eventID, t2, "End result?", val, 120);

		// ADDING BETS
		for (int j = 0; j < FOOTBALL_BETS; j++) {
			bl.getBets().storeNewBet(q1Id, r.nextInt(600) + 250, validSet.get(r.nextInt(3)),
					users.get(r.nextInt(users.size())).getUsername());
			List<Integer> prediction = new LinkedList<Integer>();
			prediction.add(r.nextInt(60));
			prediction.add(r.nextInt(60));
			bl.getBets().storeNewBet(
					q2Id, r.nextInt(600) + 250, prediction.toString().replace('[', Character.MIN_VALUE)
							.replace(']', Character.MIN_VALUE).replaceAll("\\s+", "").trim(),
					users.get(r.nextInt(users.size())).getUsername());
		}
	}

	public static void createBaseballEvent() {
		// ADDING EVENT
		String eventName = chooseTwoUnique(baseballTeams);
		Calendar deadline = generateAfter(new GregorianCalendar());
		Translation t = new Translation("This is event " + eventName + " for " + yearIdentifier);
		t.setTranslationText(Language.EN, t.getDefaultText());
		t.setTranslationText(Language.ES, "Este es el evento " + eventName + " para " + yearIdentifier);
		t.setTranslationText(Language.EUS, "Hau da " + eventName + " " + yearIdentifier + " urterako");
		long eventID = bl.getEvents().createEvent(eventName + " " + yearIdentifier, deadline, generateAfter(deadline),
				t, baseballTags);
		Translation tq = new Translation("Who wins?");
		tq.setTranslationText(Language.ES, "Quien gana?");
		tq.setTranslationText(Language.EN, "Who wins?");
		tq.setTranslationText(Language.EUS, "Nork irabaziko du?");
		Translation t2 = new Translation("End result?");
		t2.setTranslationText(Language.ES, "Resultado final?");
		t2.setTranslationText(Language.EN, "End result?");
		t2.setTranslationText(Language.EUS, "Azken emaitza?");
		// ADDING QUESTIONS
		List<Float> validSetValues = new LinkedList<Float>();
		Random r = new Random();
		float max = 2.5F;
		float min = 1.1F;
		for (int i = 0; i < validSet.size(); i++) {
			validSetValues.add(min + r.nextFloat() * (max - min));
		}
		int[] val = { r.nextInt(20), r.nextInt(20) };
		long q1Id = bl.getQuestions().addQuestionValidSet(eventID, tq, "Who wins?", validSet, validSetValues);
		long q2Id = bl.getQuestions().addQuestionNumeric(eventID, t2, "End result?", val, 40);

		// ADDING BETS
		for (int j = 0; j < BASEBALL_BETS; j++) {
			bl.getBets().storeNewBet(q1Id, r.nextInt(600) + 250, validSet.get(r.nextInt(3)),
					users.get(r.nextInt(users.size())).getUsername());
			List<Integer> prediction = new LinkedList<Integer>();
			prediction.add(r.nextInt(20));
			prediction.add(r.nextInt(20));
			bl.getBets().storeNewBet(
					q2Id, r.nextInt(600) + 250, prediction.toString().replace('[', Character.MIN_VALUE)
							.replace(']', Character.MIN_VALUE).replaceAll("\\s+", "").trim(),
					users.get(r.nextInt(users.size())).getUsername());
		}
	}

	public static void createTourStage() {
		// ADDING EVENT
		String eventName = chooseTwoUnique(frenchCities);
		Calendar deadline = generateAfter(new GregorianCalendar());
		Translation t = new Translation("This is event " + eventName + " for " + yearIdentifier);
		t.setTranslationText(Language.EN, t.getDefaultText());
		t.setTranslationText(Language.ES, "Este es el evento " + eventName + " para " + yearIdentifier);
		t.setTranslationText(Language.EUS, "Hau da " + eventName + " " + yearIdentifier + " urterako");
		long eventID = bl.getEvents().createEvent(eventName + " " + yearIdentifier, deadline, generateAfter(deadline),
				t, cyclingTags);
		Translation tq = new Translation("Who wins?");
		tq.setTranslationText(Language.ES, "Quien gana?");
		tq.setTranslationText(Language.EN, "Who wins?");
		tq.setTranslationText(Language.EUS, "Nork irabaziko du?");
		// ADDING QUESTIONS
		List<Float> validSetValues = new LinkedList<Float>();
		Random r = new Random();
		float max = 2.5F;
		float min = 1.1F;
		for (int i = 0; i < Arrays.asList(cyclingTeams).size(); i++) {
			validSetValues.add(min + r.nextFloat() * (max - min));
		}
		long q1Id = bl.getQuestions().addQuestionValidSet(eventID, tq, "Who wins?", Arrays.asList(cyclingTeams),
				validSetValues);
		// ADDING BETS
		for (int j = 0; j < CYCLING_BETS; j++) {
			bl.getBets().storeNewBet(q1Id, r.nextInt(600) + 250,
					Arrays.asList(cyclingTeams).get(r.nextInt(Arrays.asList(cyclingTeams).size())),
					users.get(r.nextInt(users.size())).getUsername());
		}
	}

}
