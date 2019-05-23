package businessLogicV3.publicAPI.email;

import businessLogicV3.privateAPI.BLFacadePrivate;

public class BLFacadePublicEmailImplementation implements BLFacadePublicEmail {

	private BLFacadePrivate privateAPI;

	public BLFacadePublicEmailImplementation(BLFacadePrivate privateAPI) {
		this.privateAPI = privateAPI;
	}

	/**
	 * This method sends an email to your account
	 * 
	 * @param username of member
	 * @return true if email sent
	 */
	@Override
	public boolean resetPassword(String username, String language) {
		return privateAPI.getEmail().recoverPassword(username, language);
	}

	/**
	 * This function checks if the token to reset the password is valid and allows
	 * you to changethe password
	 * 
	 * @param username  of the member
	 * @param token     to reset the password
	 * @param password  new password
	 * @param password2 repeat the password
	 * @return true if successfully updated
	 */
	@Override
	public boolean checkResetPassword(String username, String token, String password, String password2) {

		boolean error = privateAPI.getDataManager().getDataAccessEmail().checkPasswordReset(username, token);
		int i = -1;
		if (error) {
			i = privateAPI.getUsers().updatePassword(username, password, password2);
		}
		System.out.println(i);
		System.out.println(error);
		if (i == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method returns the user with the recoveryPasswordLink
	 * 
	 * @param passwordLink that identifies the user
	 * @return the username that match the passwordLink
	 */
	@Override
	public String getUsernameVerifyLink(String passwordLink) {
		String username = privateAPI.getDataManager().getDataAccessEmail().getUsernameVerifyLink(passwordLink);
		return username;
	}

}
