package businessLogicV3.privateAPI.other;

import java.util.Calendar;

public interface BLFacadePrivateOthers {
	/**
	 * This method encrypt a string using SHA-256 algorithm
	 * 
	 * @param base String to be encrypted
	 * @return the string encrypted
	 */
	public String encrypt(String base);

	/**
	 * This method generates a token of length 10
	 * 
	 * @param length lenght of the token
	 * @return token
	 */
	public String createToken(int length);

	/**
	 * This function updates the file SupportInfoLanguage
	 * 
	 * @param content  that is going to be written in the file
	 * @param language of the file
	 * @return true if successfully updated
	 */
	public boolean updateSupportInfo(String content, String language);

	/**
	 * This function reads the file SupportInfoLanguage
	 * 
	 * @param language of the file
	 * @return file content
	 */
	public String readSupportInfo(String language);

	/**
	 * This function creates some fake stats to populate the database
	 * 
	 * @param date  date of the calendar
	 * @param bets  number of bets of the day
	 * @param coins bet in that day
	 */
	public void fakeStats(Calendar date, long bets, long coins);

}
