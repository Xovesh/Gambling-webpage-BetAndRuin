package businessLogicV3.privateAPI.news;

import java.util.List;

import businessLogicV3.privateAPI.BLFacadePrivate;
import dataAccessV3.DataAccess;
import domainV2.News;

public class BLFacadePrivateNewsImplementation implements BLFacadePrivateNews{
	
	private DataAccess dataManager;

	
	public BLFacadePrivateNewsImplementation(DataAccess dataManager, BLFacadePrivate privateAPI) {
		this.dataManager = dataManager;
	}

	/**
	 * This function adds a new news to the database
	 * 
	 * @param language
	 *            of the news
	 * @param title
	 *            of the news
	 * @param content
	 *            of the news
	 * @param image
	 *            of the news
	 * @return if of the new news or -1 if error
	 */
	@Override
	public int addNews(String language, String title, String content, String image) {
		int error = dataManager.getDataAccessNews().addNews(language, title, content, image);
		return error;
	}

	/**
	 * This function modifies a news
	 * 
	 * @param id
	 *            of the news
	 * @param language
	 *            of the news
	 * @param title
	 *            of the news
	 * @param content
	 *            of the news
	 * @param image
	 *            of the news
	 * @return true if successfully updated
	 */
	@Override
	public boolean modifyNews(int id, String language, String title, String content, String image) {

		dataManager.getDataAccessNews().modifyNews(id, language, title, content, image);

		return true;
	}

	/**
	 * This function deletes a news from the database
	 * 
	 * @param id
	 *            of the news
	 * @return true if successfully deleted
	 */
	@Override
	public boolean deleteNews(int id) {

		dataManager.getDataAccessNews().deleteNews(id);

		return true;
	}

	/**
	 * This function retrieves all news from the database
	 * 
	 * @return list with all the news
	 */
	@Override
	public List<News> retrieveAllNews() {
		List<News> res = null;

		res = dataManager.getDataAccessNews().retrieveAllNews();

		return res;
	}

	/**
	 * This function retrieves a news by its id
	 * 
	 * @param id
	 *            of the news
	 * @return object of the news
	 */
	@Override
	public News retrieveNewsById(int id) {
		News res = null;

		res = dataManager.getDataAccessNews().retrieveNewsById(id);

		return res;
	}

	/**
	 * This function retrieve the latest news
	 * 
	 * @param amount
	 *            of news to be retrieved
	 * @return list with the latest news
	 */
	@Override
	public List<News> retrieveLastNews(int amount) {
		List<News> res = null;

		res = dataManager.getDataAccessNews().retrieveLastNews(amount);

		return res;
	}

	/**
	 * This function retrieve the latest news of an specific language
	 * 
	 * @param amount
	 *            of news to be retrieved
	 * @param language
	 *            of the news
	 * @return list with n news of a concrete language
	 */
	@Override
	public List<News> retrieveLastNewsByLanguage(int amount, String language) {
		List<News> res = null;
		res = dataManager.getDataAccessNews().retrieveLastNews(amount, language);
		return res;
	}

	/**
	 * This method retrieves news with a given id (from) to start
	 * 
	 * @param from
	 *            the id from we are retrieving the data
	 * @param amount
	 *            of news to retrieve
	 * @param language
	 *            of the news
	 * @return list with the all the news starting from "from" id
	 */
	@Override
	public List<News> retrieveNewsFromTo(int from, int amount, String language) {
		List<News> res = null;

		res = dataManager.getDataAccessNews().retrieveNewsFromTo(from, amount, language);

		return res;
	}

	/**
	 * This method returns the next id that proceed to a given id
	 * 
	 * @param id
	 *            the id before the one that we want
	 * @param language
	 *            ES EN OR EUS
	 * @return id of the next news
	 */
	@Override
	public int idAfterIdNews(int id, String language) {
		int res = 0;

		res = dataManager.getDataAccessNews().idAfterIdNews(id, language);

		return res;
	}

	/**
	 * This function returns the id of the first news in the database
	 * 
	 * @param language
	 *            ES EN or EUS
	 * @return id of the first news
	 */
	@Override
	public int firstNewsId(String language) {
		int res = 0;

		res = dataManager.getDataAccessNews().firstNewsId(language);

		return res;
	}

	/**
	 * This function returns the id of the last news *
	 * 
	 * @param language
	 *            ES EN or EUS
	 * @return the if of the last news
	 */
	@Override
	public int lastNewsId(String language) {
		int res = 0;

		res = dataManager.getDataAccessNews().lastNewsId(language);

		return res;
	}

	/**
	 * This function returns n ids before the given id
	 * 
	 * @param id
	 *            of the news
	 * @param amount
	 *            of news
	 * @param language
	 *            ES EN or EUS
	 * @return id of n before
	 */
	@Override
	public int nIdsBeforeNews(int id, int amount, String language) {
		int res = 0;

		res = dataManager.getDataAccessNews().nIdsBeforeNews(id, amount, language);

		return res;
	}

	/**
	 * This method retrieves all news until a given id
	 * 
	 * @param untilTo
	 *            until which id we want to retrieve
	 * @param language
	 *            ES EN or EUS
	 * @return List of news until untilTo
	 */
	@Override
	public List<News> untilGivenIdNews(int untilTo, String language) {
		List<News> res = null;

		res = dataManager.getDataAccessNews().untilGivenIdNews(untilTo, language);

		return res;
	}

	/**
	 * This function returns how many news are in the database
	 * 
	 * @return how many news are in the database
	 */
	@Override
	public int newsQuantity(String language) {
		int res = 0;

		res = dataManager.getDataAccessNews().newsQuantity(language);

		return res;
	}
}
