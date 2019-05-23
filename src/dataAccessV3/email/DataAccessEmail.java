package dataAccessV3.email;

public interface DataAccessEmail {
	/**
	 * This functions add a new request to reset the password
	 * 
	 * @param username     of the member
	 * @param email        of the member
	 * @param token        code to verify
	 * @param recoveryLink code of the user
	 * @return true if successfully added
	 */
	public boolean passwordReset(String username, String email, String token, String recoveryLink);

	/**
	 * This function check if the token sent to the email equals to the stored one
	 * 
	 * @param username that request the update
	 * @param token    to compare with the database
	 * @return true if the token equals to the token in the database
	 */
	public boolean checkPasswordReset(String username, String token);

	/**
	 * This method returns the user with the recoveryPasswordLink
	 * 
	 * @param passwordLink that identifies the user
	 * @return the username that match the passwordLink
	 */
	public String getUsernameVerifyLink(String passwordLink);
}
