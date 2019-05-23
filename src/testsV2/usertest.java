package testsV2;

import java.util.Random;

import businessLogicV3.publicAPI.BLFacadePublic;

/**
 * The objective of this class is to populate the database with some users
 *
 */
public class usertest {

	// THIS IS A TEST TO POPULATE THE DATABASE
	public static void main(String[] args) {
		String names = "Sophia Jackson Olivia Liam Emma Noah Ava Aiden Isabella Caden Aria Grayson Riley Lucas Amelia Mason Mia Oliver Layla Elijah Zoe Logan Mila Carter Charlotte Ethan Harper Muhammad Lily Jayden Chloe Michael Aaliyah James Adalyn Sebastian Evelyn Alexander Avery Mateo Aubrey Jacob Ella Ryan Camilla Benjamin Nora Daniel Scarlett William Maya Jack Emily Julian Abigail Leo Madison Jayce Eliana Caleb Luna Luke Ellie Henry Hannah Gabriel Arianna Matthew Kinsley Wyatt Elizabeth Owen Leah Connor Hailey Josiah Sarah Levi Victoria David Paisley Isaac Elena John Penelope Carson Everly Cameron Madelyn Isaiah Addison Asher Grace Lincoln Brooklyn Adam Charlie Nicholas Isabelle Landon";
		String[] ls = names.split(" ");
		usertest.specialtest();
		for (int i = 0; i <= 10000; i++) {
			usertest.generateUser(ls);
		}
	}

	public static void generateUser(String[] names) {
		BLFacadePublic dataManager = new BLFacadePublic();
		Random ran = new Random();
		String username = "";
		String password = "";
		int year = 0;
		int month = 0;
		int day = 0;
		String mobilePhone = "";
		String email = "";
		String name = "";
		String surname = "";
		String country = "Spain";
		String city = "Donostia";
		String address = "Somewhere in Donostia";
		String ip_address = "";

		String[] abc = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
				"t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };

		name = names[ran.nextInt(names.length)];
		surname = names[ran.nextInt(names.length)];
		username = name;

		int l = 5;
		for (int i = 0; i < 5; i++) {
			username += abc[ran.nextInt(abc.length)];
			if (l == ran.nextInt(10)) {
				break;
			}
		}

		for (int i = 0; i < 10; i++) {
			password += abc[ran.nextInt(abc.length)];
			if (l == ran.nextInt(10)) {
				break;
			}
		}

		while (year < 1900 || year > 2019) {
			year = ran.nextInt(2020);
		}

		while (month < 1 || month > 12) {
			month = ran.nextInt(13);
		}

		while (day < 1 || day > 28) {
			day = ran.nextInt(29);
		}

		String[] phone = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
		for (int i = 0; i < 9; i++) {
			mobilePhone += phone[ran.nextInt(phone.length)];
		}

		email = username + "@";
		for (int i = 0; i < 5; i++) {
			email += abc[ran.nextInt(abc.length)];
		}
		email += ".com";

		for (int i = 0; i < 4; i++) {
			ip_address += ran.nextInt(256);
			if (i == 0 || i == 1 || i == 2) {
				ip_address += ".";
			}
		}

		String sex = "";
		int result = ran.nextInt(2);
		if (result == 1) {
			sex = "Male";
		} else if (result == 2) {
			sex = "Female";
		} else {
			sex = "Other";
		}

		System.out.println(dataManager.getUsers().registration(username, password, password, year, month, day, sex,
				mobilePhone, email, name, surname, country, city, address, ip_address));
		dataManager.getPrivateAPI().getUsers().increaseWalletFunds(username, ran.nextInt(99999),
				"Welcome to Bet And Ruin");

	}

	public static void specialtest() {
		BLFacadePublic dataManager = new BLFacadePublic();
		System.out.println(
				dataManager.getUsers().registration("Ccass", "Patata", "Patata", 1999, 5, 5, "Male", "123456789",
						"a@a.com", "Carlos", "Dominguez", "Spain", "Donostia", "Somewhere in Donostia", "192.168.0.1"));
		dataManager.getPrivateAPI().getUsers().updateUserRank("Ccass", 3);
		dataManager.getPrivateAPI().getUsers().increaseWalletFunds("Ccass", 999999, "Welcome to Bet And Ruin");
		System.out.println(dataManager.getUsers().registration("tractor", "Patata", "Patata", 1999, 1, 1, "Male",
				"123456789", "abcde@a.com", "Carlos", "Dominguez", "Spain", "Donostia", "Somewhere in Donostia",
				"192.168.0.1"));
		dataManager.getPrivateAPI().getUsers().updateUserRank("tractor", 3);
		dataManager.getPrivateAPI().getUsers().increaseWalletFunds("tractor", 999999, "Welcome to Bet And Ruin");
	}

}
