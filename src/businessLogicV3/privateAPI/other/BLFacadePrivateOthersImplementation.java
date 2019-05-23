package businessLogicV3.privateAPI.other;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import businessLogicV3.privateAPI.BLFacadePrivate;
import dataAccessV3.DataAccess;

public class BLFacadePrivateOthersImplementation implements BLFacadePrivateOthers {

	private DataAccess dataManager;

	public BLFacadePrivateOthersImplementation(DataAccess dataManager, BLFacadePrivate privateAPI) {
		this.dataManager = dataManager;
	}

	/**
	 * This method encrypt a string using SHA-256 algorithm
	 * 
	 * @param base String to be encrypted
	 * @return the string encrypted
	 */
	@Override
	public String encrypt(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

	/**
	 * This method generates a token of length 10
	 * 
	 * @return token
	 */
	@Override
	public String createToken(int length) {
		Random ran = new Random();
		String token = "";
		String[] abc = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
				"t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
				"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"0" };
		for (int i = 1; i <= length; i++) {
			token += abc[ran.nextInt(abc.length)];
		}
		return token;
	}

	/**
	 * This function updates the file SupportInfoLanguage
	 * 
	 * @param content  that is going to be written in the file
	 * @param language of the file
	 * @return true if successfully updated
	 */
	@Override
	public boolean updateSupportInfo(String content, String language) {
		FileWriter supportInfo = null;
		try {
			if (language.equals("ES")) {
				supportInfo = new FileWriter("./resources/others/SupportInfoSpanish.txt");
			} else if (language.equals("EN")) {
				supportInfo = new FileWriter("./resources/others/SupportInfoEnglish.txt");
			} else if (language.equals("EUS")) {
				supportInfo = new FileWriter("./resources/others/SupportInfoBasque.txt");
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}

		try {
			BufferedWriter buffer = new BufferedWriter(supportInfo);
			buffer.write(content);
			buffer.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * This function reads the file SupportInfoLanguage
	 * 
	 * @param language of the file
	 * @return file content
	 */
	@Override
	public String readSupportInfo(String language) {
		File supportInfo = null;
		if (language.equals("ES")) {
			supportInfo = new File("./resources/others/SupportInfoSpanish.txt");
		} else if (language.equals("EN")) {
			supportInfo = new File("./resources/others/SupportInfoEnglish.txt");
		} else if (language.equals("EUS")) {
			supportInfo = new File("./resources/others/SupportInfoBasque.txt");
		} else {
			return "";
		}

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
		}
		return s;
	}

	/**
	 * This function creates some fake stats to populate the database
	 * 
	 * @param date  date of the calendar
	 * @param bets  number of bets of the day
	 * @param coins bet in that day
	 */
	@Override
	public void fakeStats(Calendar date, long bets, long coins) {
		dataManager.getDataAccessOther().fakeStats(date, bets, coins);
	}
}
