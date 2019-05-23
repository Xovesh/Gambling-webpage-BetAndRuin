package businessLogicV3.publicAPI.questions;

import businessLogicV3.privateAPI.BLFacadePrivate;
import domainV2.question.Question;

public class BLFacadePublicQuestionsImplementation implements BLFacadePublicQuestions {

	private BLFacadePrivate privateAPI;

	public BLFacadePublicQuestionsImplementation(BLFacadePrivate privateAPI) {
		this.privateAPI = privateAPI;
	}

	/**
	 * This function retrieves a question given its id
	 * 
	 * @param ID of the question
	 * @return the question
	 */
	@Override
	public Question getQuestionByID(long ID) {
		return privateAPI.getQuestions().getQuestionByID(ID);
	}

}
