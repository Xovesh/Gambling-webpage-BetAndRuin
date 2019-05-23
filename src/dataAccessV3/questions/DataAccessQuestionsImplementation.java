package dataAccessV3.questions;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dataAccessV3.DataAccess;
import domainV2.bet.BetSuperClass;
import domainV2.question.InvalidAnswerValueException;
import domainV2.question.Question;
import domainV2.util.Translation;

public class DataAccessQuestionsImplementation implements DataAccessQuestions {
	private EntityManager db;
	private DataAccess dataManager;

	public DataAccessQuestionsImplementation(EntityManager db, DataAccess dataManager) {
		this.db = db;
		this.dataManager = dataManager;
	}

	/**
	 * This function retrieves a question given its id
	 * 
	 * @param questionID of the question
	 * @return the question
	 */
	@Override
	public Question getQuestionById(long questionID) {
		Question questions = null;
		try {
			TypedQuery<Question> q2 = db.createQuery("SELECT us FROM Question us WHERE us.ID = ?1", Question.class);
			q2.setParameter(1, questionID);
			questions = q2.getSingleResult();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
		}
		return questions;
	}

	/**
	 * This function answers a question
	 * 
	 * @param questionID id of the question
	 * @param answer     to the question
	 */
	@Override
	public void answerQuestion(long questionID, String answer) {

		Question q = getQuestionById(questionID);
		db.getTransaction().begin();
		try {
			q.answer(answer);
		} catch (InvalidAnswerValueException e) {
			e.printStackTrace();
		}
		db.getTransaction().commit();

	}

	/**
	 * This function resolves a question
	 * 
	 * @param questionID id of the question
	 */
	@Override
	public void resolveQuestion(long questionID) {
		Question q = getQuestionById(questionID);
		System.out.println(q.translation.getDefaultText());
		q.getBets();
		for (BetSuperClass b : q.getBets()) {
			System.out.println("\t" + b.getUsername());
			if (b.isWon())
				dataManager.getDataAccessUsers().increaseWalletFunds(b.getUser().getUsername(), b.getFinalPay(),
						"Congratulations! You won your bet for question " + q.translation.getDefaultText()
								+ " in event "
								+ dataManager.getDataAccessEvents().getEventByID(q.getParentEventId()).publicEventName);

		}
	}

	/**
	 * This function cancel a question
	 * 
	 * @param id of the question
	 */
	@Override
	public void cancelQuestion(long id) {
		Question q = getQuestionById(id);
		for (BetSuperClass b : q.getBets()) {
			db.getTransaction().begin();
			q.removeProfit(b.getBetAmount());
			db.getTransaction().commit();
			dataManager.getDataAccessUsers().increaseWalletFunds(b.getUser().getUsername(), b.getBetAmount(),
					b.getQuestion().translation.getDefaultText() + " refund.");
		}
	}

	/**
	 * This function updates the translations of descriptions of an di
	 * 
	 * @param id          of the question
	 * @param translation new translations
	 */
	@Override
	public void updateTranslationByID(long id, Translation translation) {
		Question q = getQuestionById(id);
		db.getTransaction().begin();
		q.translation = translation;
		db.getTransaction().commit();
	}

}
