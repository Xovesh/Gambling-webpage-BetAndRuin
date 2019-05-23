package testsV2;

import businessLogicV3.publicAPI.BLFacadePublic;
/**
 * The objective of this class is test the email service
 *
 */
public class emailTest {

	public static void main(String[] args) {
		BLFacadePublic dataManager = new BLFacadePublic();
		String username = "tractor";
		//dataManager.resetPassword(username);
		System.out.println(dataManager.getEmail().checkResetPassword(username, "ACwEBsuKvj", "abc", "abc"));
	}

}
