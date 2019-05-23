package businessLogicV3.publicAPI.other;

public interface BLFacadePublicOther {
	/**
	 * This function reads the file SupportInfoLanguage
	 * 
	 * @param language of the file
	 * @return file content
	 */

	public String readSupportInfo(String language);
}
