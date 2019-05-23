package businessLogicV3.publicAPI.questions;

import domainV2.question.Question;

public interface BLFacadePublicQuestions {
	/**
	 * This function retrieves a question given its id
	 * 
	 * @param ID of the question
	 * @return the question
	 */
	public Question getQuestionByID(long ID);
}
