package dataAccessV3.questions;

import domainV2.question.Question;
import domainV2.util.Translation;

public interface DataAccessQuestions {
	/**
	 * This function retrieves a question given its id
	 * 
	 * @param questionID id of the question
	 * @return the question
	 */
	public Question getQuestionById(long questionID);

	/**
	 * This function answers a question
	 * 
	 * @param questionID id of the question
	 * @param answer     to the question
	 */
	public void answerQuestion(long questionID, String answer);

	/**
	 * This function resolves a question
	 * 
	 * @param questionID id of the question
	 */
	public void resolveQuestion(long questionID);

	/**
	 * This function cancel a question
	 * 
	 * @param id of the question
	 */
	public void cancelQuestion(long id);

	/**
	 * This function updates the translations of descriptions of an di
	 * 
	 * @param id          of the question
	 * @param translation new translations
	 */
	public void updateTranslationByID(long id, Translation translation);
}
