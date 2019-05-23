package businessLogicV3.privateAPI.questions;

import java.util.List;

import domainV2.question.Question;
import domainV2.util.Translation;

public interface BLFacadePrivateQuestions {
	/**
	 * This function retrieves a question given its id
	 * 
	 * @param ID of the question
	 * @return the question
	 */
	public Question getQuestionByID(long ID);

	/**
	 * This function adds to a event a question of type set
	 * 
	 * @param eventId             the id of the event
	 * @param defaultQuestionText the default name of the question
	 * @param questionIdentifier  the identifier of the question
	 * @param initialValidSet     the set of values available
	 * @param validSetPayouts     the set of payouts for each value
	 * @return the id of the question
	 */
	public long addQuestionValidSet(long eventId, Translation defaultQuestionText, String questionIdentifier,
			List<String> initialValidSet, List<Float> validSetPayouts);

	/**
	 * This function adds to a event a question of type numeric value
	 * 
	 * @param eventId             the id of the event
	 * @param defaultQuestionText the default name of the question
	 * @param questionIdentifier  the identifier of the question
	 * @param expectedResult      the expected score
	 * @param maxExpectedScore    possible maximum score ex. football 10
	 * @return the id of the question
	 */
	public long addQuestionNumeric(long eventId, Translation defaultQuestionText, String questionIdentifier,
			int[] expectedResult, int maxExpectedScore);

	/**
	 * This function answers a question
	 * 
	 * @param questionId id of the question
	 * @param answer     to the question
	 */
	public void answerQuestion(long questionId, String answer);
}
