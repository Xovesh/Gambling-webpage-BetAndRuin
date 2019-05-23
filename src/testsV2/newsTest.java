package testsV2;

import java.util.Random;

import businessLogicV3.privateAPI.BLFacadePrivate;
import businessLogicV3.publicAPI.BLFacadePublic;


/**
 * The objective of this class is to populate the database with news
 *
 */
public class newsTest {
	public static void main(String[] args) {
		String names = "Sophia Jackson Olivia Liam Emma Noah Ava Aiden Isabella Caden Aria Grayson Riley Lucas Amelia Mason Mia Oliver Layla Elijah Zoe Logan Mila Carter Charlotte Ethan Harper Muhammad Lily Jayden Chloe Michael Aaliyah James Adalyn Sebastian Evelyn Alexander Avery Mateo Aubrey Jacob Ella Ryan Camilla Benjamin Nora Daniel Scarlett William Maya Jack Emily Julian Abigail Leo Madison Jayce Eliana Caleb Luna Luke Ellie Henry Hannah Gabriel Arianna Matthew Kinsley Wyatt Elizabeth Owen Leah Connor Hailey Josiah Sarah Levi Victoria David Paisley Isaac Elena John Penelope Carson Everly Cameron Madelyn Isaiah Addison Asher Grace Lincoln Brooklyn Adam Charlie Nicholas Isabelle Landon";
		String[] ls = names.split(" ");
		BLFacadePublic dataManager = new BLFacadePublic();
		String s = dataManager.getUsers().authentication("tractor", "Patata");
		BLFacadePrivate p = dataManager.getPrivateAPI("tractor", s);
		for(int i=0; i<=1000;i++) {
			newsTest.generateNews( p, ls);
		}
		
		p.getNews().addNews("ES", "Tu conoces tu fútbol, ¿Por qué no se lo enseñas a los demas?", "¡Tus ligas favoritas en BetAndRuin!", "https://www.thenation.com/wp-content/uploads/2018/04/soccer-ball-ss-img.jpg?scale");
		p.getNews().addNews("EN", "You know your soccer, why not show everyone else?", "Your favourite leagues now in BetAndRuin!", "https://www.thenation.com/wp-content/uploads/2018/04/soccer-ball-ss-img.jpg?scale");
		p.getNews().addNews("EUS", "Zure futbola dakizu, zergatik ez erakusi denoi?", "Zure liga faboritoak orain BetAndRuin-en!", "https://www.thenation.com/wp-content/uploads/2018/04/soccer-ball-ss-img.jpg?scale");
		
		p.getNews().addNews("ES", "Promoción! Registrate ahora y obten 10000 puntos gratis*.", "¡No te lo pierdas!", "https://www.thepremierprice.com/wp-content/uploads/2013/08/gold.png");
		p.getNews().addNews("EN", "Promotion! Sign up and get 10000 points for free*.", "Don't miss out!", "https://www.thepremierprice.com/wp-content/uploads/2013/08/gold.png");
		p.getNews().addNews("EUS", "Promozioa!, Erregistratu orain eta hartu 10000 puntu dohainik!.", "Ez Galdu!", "https://www.thepremierprice.com/wp-content/uploads/2013/08/gold.png");
		
		p.getNews().addNews("ES", "¡Bienvenido a BetAndRuin!", "¡Registrate y empieza a ganar dinero!", "https://veganstrategist.org/wp-content/uploads/2018/02/money-heart.jpg");
		p.getNews().addNews("EN", "Welcome to BetAndRuin!", "Sign up and make money!", "https://veganstrategist.org/wp-content/uploads/2018/02/money-heart.jpg");
		p.getNews().addNews("EUS", "Ongi etorri BetAndRuinera", "Erregistratu eta egin dirua!", "https://veganstrategist.org/wp-content/uploads/2018/02/money-heart.jpg");
		
	}
	
	public static void generateNews( BLFacadePrivate p, String[] ls) {
		Random ran = new Random();
		int t = ran.nextInt(ls.length);
		String[] language = {"EN", "ES", "EUS"};
		int k = ran.nextInt(language.length);
		int s = p.getNews().addNews(language[k], ls[t], ls[t], "http://vallartaopina.net/wp-content/uploads/2019/01/01-35.jpg");
		System.out.println(s);
		
	}
}
