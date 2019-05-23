package businessLogicV3.privateAPI.news;

import java.util.List;

import domainV2.News;

public interface BLFacadePrivateNews {
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
	public int addNews(String language, String title, String content, String image);

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
	public boolean modifyNews(int id, String language, String title, String content, String image);

	/**
	 * This function deletes a news from the database
	 * 
	 * @param id
	 *            of the news
	 * @return true if successfully deleted
	 */
	public boolean deleteNews(int id);

	/**
	 * This function retrieves a news by its id
	 * 
	 * @param id
	 *            of the news
	 * @return object of the news
	 */
	public News retrieveNewsById(int id);

	/**
	 * This function retrieves all news from the database
	 * 
	 * @return list with all the news
	 */
	public List<News> retrieveAllNews();

	/**
	 * This function retrieve the latest news
	 * 
	 * @param amount
	 *            of news to be retrieved
	 * @return list with the latest news
	 */
	public List<News> retrieveLastNews(int amount);

	/**
	 * This function retrieve the latest news of an specific language
	 * 
	 * @param amount
	 *            of news to be retrieved
	 * @param language
	 *            of the news
	 * @return list with n news of a concrete language
	 */
	public List<News> retrieveLastNewsByLanguage(int amount, String language);

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
	public List<News> retrieveNewsFromTo(int from, int amount, String language);

	/**
	 * This method returns the next id that proceed to a given id
	 * 
	 * @param id
	 *            the id before the one that we want
	 * @param language
	 *            ES EN OR EUS
	 * @return id of the next news
	 */
	public int idAfterIdNews(int id, String language);

	/**
	 * This function returns the id of the first news in the database
	 * 
	 * @param language
	 *            ES EN or EUS
	 * @return id of the first news
	 */
	public int firstNewsId(String language);

	/**
	 * This function returns the id of the last news *
	 * 
	 * @param language
	 *            ES EN or EUS
	 * @return the if of the last news
	 */
	public int lastNewsId(String language);

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
	public int nIdsBeforeNews(int id, int amount, String language);

	/**
	 * This method retrieves all news until a given id
	 * 
	 * @param untilTo
	 *            until which id we want to retrieve
	 * @param language
	 *            ES EN or EUS
	 * @return List of news until untilTo
	 */
	public List<News> untilGivenIdNews(int untilTo, String language);

	/**
	 * This function returns how many news are in the database
	 * @param language language of the news
	 * @return how many news are in the database
	 */
	public int newsQuantity(String language);
}
