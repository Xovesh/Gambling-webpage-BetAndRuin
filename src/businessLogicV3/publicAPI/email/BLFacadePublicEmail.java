package businessLogicV3.publicAPI.email;

public interface BLFacadePublicEmail {
	/**
	 * This method sends an email to your account
	 * 
	 * @param username
	 *            of member
	 * @param language language of the email
	 * @return true if email sent
	 */
	public boolean resetPassword(String username, String language);

	/**
	 * This function checks if the token to reset the password is valid and allows
	 * you to changethe password
	 * 
	 * @param username
	 *            of the member
	 * @param token
	 *            to reset the password
	 * @param password
	 *            new password
	 * @param password2
	 *            repeat the password
	 * @return true if successfully updated
	 */
	public boolean checkResetPassword(String username, String token, String password, String password2);

	/**
	 * This method returns the user with the recoveryPasswordLink
	 * 
	 * @param passwordLink
	 *            that identifies the user
	 * @return the username that match the passwordLink
	 */
	public String getUsernameVerifyLink(String passwordLink);

}
