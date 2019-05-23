package businessLogicV3.publicAPI.other;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import businessLogicV3.privateAPI.BLFacadePrivate;

public class BLFacadePublicOtherImplementation implements BLFacadePublicOther {

	public BLFacadePublicOtherImplementation(BLFacadePrivate privateAPI) {
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

}
