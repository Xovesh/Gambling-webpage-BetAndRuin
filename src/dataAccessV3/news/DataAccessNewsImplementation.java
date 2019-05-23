package dataAccessV3.news;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import dataAccessV3.DataAccess;
import domainV2.News;

public class DataAccessNewsImplementation implements DataAccessNews {
	private EntityManager db;

	public DataAccessNewsImplementation(EntityManager db, DataAccess dataManager) {
		this.db = db;
	}

	/**
	 * This method add a new news in the database
	 * 
	 * @param language of the news
	 * @param title    of the news
	 * @param content  of the news
	 * @param image    of the news
	 * @return integer errorcode id of the news -1 - Error in the database
	 */
	@Override
	public int addNews(String language, String title, String content, String image) {
		try {
			db.getTransaction().begin();
			News news = new News(language, title, content, image);
			db.persist(news);
			db.getTransaction().commit();
			return news.getId();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return -1;
		}
	}

	/**
	 * This method deletes a news from the database
	 * 
	 * @param id of the new
	 * @return true if successfully deleted
	 */
	@Override
	public boolean deleteNews(int id) {
		try {
			db.getTransaction().begin();
			Query q4 = db.createQuery("DELETE FROM News nw WHERE nw.id = ?1", News.class);
			q4.setParameter(1, id);
			q4.executeUpdate();
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This method modify a news from the database
	 * 
	 * @param id of the news
	 * @param language of the news
	 * @param title of the news
	 * @param content of the news
	 * @param image of the news
	 * @return true if successfully updated
	 */
	@Override
	public boolean modifyNews(int id, String language, String title, String content, String image) {
		try {
			News news = retrieveNewsById(id);
			if (news == null) {
				System.out.println("The new that you are trying to modify is not in the database");
				return false;
			} else {
				db.getTransaction().begin();
				news.setContent(content);
				news.setPicture(image);
				news.setHead(title);
				news.setLanguage(language);
				db.getTransaction().commit();
				return true;
			}
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return false;
		}
	}

	/**
	 * This method retrieve the Last news added to the database
	 * 
	 * @param amount of news to be retrieved
	 * @return list with the last news
	 */
	@Override
	public List<News> retrieveLastNews(int amount) {
		try {
			List<News> res = new LinkedList<News>();
			TypedQuery<News> query = db.createQuery("SELECT nw FROM News nw ORDER BY nw.date DESC", News.class);
			List<News> newsList = query.getResultList();
			int i = 1;
			for (News nw : newsList) {
				res.add(nw);
				if (i == amount) {
					break;
				}
				i++;
			}
			return res;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

	/**
	 * This method retrieve the Last news added to the database by its language
	 * 
	 * @param amount   of news to be retrieved
	 * @param language of the news
	 * @return List with the last news
	 */
	@Override
	public List<News> retrieveLastNews(int amount, String language) {
		// checking the possible languages
		if (!language.equals("ES") && !language.equals("EN") && !language.equals("EUS") && !language.equals("ALL")) {
			return new LinkedList<News>();
		}

		try {
			List<News> res = new LinkedList<News>();
			TypedQuery<News> query = null;
			if (language.equals("ALL")) {
				query = db.createQuery("SELECT nw FROM News nw ORDER BY nw.date DESC", News.class);
			} else {
				query = db.createQuery("SELECT nw FROM News nw WHERE nw.language = ?1 ORDER BY nw.date DESC",
						News.class);
				query.setParameter(1, language);
			}

			List<News> newsList = query.getResultList();
			int i = 1;
			for (News nw : newsList) {
				res.add(nw);
				if (i == amount) {
					break;
				}
				i++;
			}
			return res;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return new LinkedList<News>();
		}
	}

	/**
	 * This method returns all the news from the database
	 * 
	 * @return list with all the news
	 */
	@Override
	public List<News> retrieveAllNews() {
		try {
			TypedQuery<News> q2 = db.createQuery("SELECT nw FROM News nw ORDER BY nw.date DESC", News.class);
			return q2.getResultList();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

	/**
	 * This method returns all the news from the database that have a language
	 * 
	 * @param language of the news ES, EUS or EN
	 * @return list with all the news
	 */
	@Override
	public List<News> retrieveAllNewsByLanguage(String language) {
		try {
			TypedQuery<News> q2 = db.createQuery("SELECT nw FROM News nw WHERE nw.language = ?1 ORDER BY nw.date DESC",
					News.class);
			q2.setParameter(1, language);
			return q2.getResultList();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

	/**
	 * This method retrieves news with a given id (from) to start
	 * 
	 * @param from     the id from we are retrieving the data
	 * @param amount   of news to retrieve
	 * @param language of the news
	 * @return list with the all the news starting from "from" id
	 */
	@Override
	public List<News> retrieveNewsFromTo(int from, int amount, String language) {
		TypedQuery<News> query = null;
		if (language.equals("ALL")) {
			query = db.createQuery("SELECT nw FROM News nw WHERE nw.id <= ?2 ORDER BY nw.date DESC", News.class);
		} else {
			query = db.createQuery("SELECT nw FROM News nw WHERE nw.language= ?1 AND nw.id <= ?2 ORDER BY nw.date DESC",
					News.class);
			query.setParameter(1, language);
		}

		query.setParameter(2, from);
		try {
			List<News> res = new LinkedList<News>();
			List<News> helper = query.getResultList();
			int j = 1;
			for (News news : helper) {
				res.add(news);
				if (j == amount) {
					break;
				}
				j++;

			}
			return res;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

	/**
	 * This method retrieves news with a given id (from) to start
	 * 
	 * @param from   the id from we are retrieving the data
	 * @param amount of news to retrieve
	 * @return list with the all the news starting from "from" id
	 */
	@Override
	public List<News> retrieveNewsFromTo(int from, int amount) {

		TypedQuery<News> query = db.createQuery("SELECT nw FROM News nw WHERE nw.id > ?1 ORDER BY nw.date DESC",
				News.class);
		query.setParameter(1, from);
		try {
			List<News> res = new LinkedList<News>();
			List<News> helper = query.getResultList();
			int j = 0;
			for (News news : helper) {
				res.add(news);
				if (j == amount) {
					break;
				}
				j++;
			}
			return res;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

	/**
	 * This method retrieves a new by its id
	 * 
	 * @param id of the new
	 * @return the new object
	 */
	@Override
	public News retrieveNewsById(int id) {
		try {
			TypedQuery<News> q2 = db.createQuery("SELECT nw FROM News nw WHERE nw.id=?1", News.class);
			q2.setParameter(1, id);
			News news = null;
			try {
				news = q2.getSingleResult();
			} catch (Exception e) {

			}
			return news;
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

	/**
	 * This method returns the next id that proceed to a given id
	 * 
	 * @param id       the id before the one that we want
	 * @param language ES EN EUS or ALL
	 * @return id of the next news
	 */
	@Override
	public int idAfterIdNews(int id, String language) {
		List<News> news = null;
		if (language.equals("ES")) {
			news = retrieveNewsFromTo(id, 201, "ES");
		} else if (language.equals("EN")) {
			news = retrieveNewsFromTo(id, 201, "EN");
		} else if (language.equals("EUS")) {
			news = retrieveNewsFromTo(id, 201, "EUS");
		} else if (language.equals("ALL")) {
			news = retrieveNewsFromTo(id, 201, "ALL");
		} else {
			return -1;
		}
		if (news.size() >= 2) {
			return news.get(1).getId();
		} else {
			return -1;
		}
	}

	/**
	 * This function returns the id of the first news in the database
	 * 
	 * @param language ES EN EUS or ALL
	 * @return id of the first news
	 */
	@Override
	public int firstNewsId(String language) {
		List<News> newsList = null;
		if (language.equals("ES")) {
			newsList = retrieveLastNews(2, "ES");
		} else if (language.equals("EUS")) {
			newsList = retrieveLastNews(2, "EUS");
		} else if (language.equals("EN")) {
			newsList = retrieveLastNews(2, "EN");
		} else if (language.equals("ALL")) {
			newsList = retrieveLastNews(2);
		} else {
			return -1;
		}
		if (newsList != null && newsList.size() > 0) {
			return newsList.get(0).getId();
		} else {
			return -1;
		}
	}

	/**
	 * This function returns the id of the last news
	 * 
	 * @param language ES EN EUS or ALL
	 * @return the if of the last news
	 */
	@Override
	public int lastNewsId(String language) {
		List<News> newsList = null;
		if (language.equals("ES")) {
			newsList = retrieveAllNewsByLanguage("ES");
		} else if (language.equals("EUS")) {
			newsList = retrieveAllNewsByLanguage("EUS");
		} else if (language.equals("EN")) {
			newsList = retrieveAllNewsByLanguage("EN");
		} else if (language.equals("ALL")) {
			newsList = retrieveAllNews();
		} else {
			return -1;
		}
		if (newsList != null) {
			return newsList.get(newsList.size() - 1).getId();
		} else {
			return -1;
		}
	}

	/**
	 * This function returns n ids before the given id
	 * 
	 * @param id       of the news
	 * @param amount   of news
	 * @param language ES EN EUS or ALL
	 * @return id of n before
	 */
	@Override
	public int nIdsBeforeNews(int id, int amount, String language) {
		List<News> NewsList = untilGivenIdNews(id, language);
		if (NewsList != null && NewsList.size() > 0) {
			if (NewsList.size() < amount) {
				return NewsList.get(0).getId();
			} else {
				return NewsList.get(NewsList.size() - amount).getId();
			}
		} else {
			return -1;
		}
	}

	/**
	 * This method retrieves all news until a given id
	 * 
	 * @param untilTo  until which id we want to retrieve
	 * @param language ES EN EUS or ALL
	 * @return List of news until untilTo
	 */
	@Override
	public List<News> untilGivenIdNews(int untilTo, String language) {
		TypedQuery<News> query = null;
		if (language.equals("ES")) {
			query = db.createQuery("SELECT nw FROM News nw WHERE nw.id > ?1 AND nw.language = ?2 ORDER BY nw.date DESC",
					News.class);
			query.setParameter(2, language);
		} else if (language.equals("EUS")) {
			query = db.createQuery("SELECT nw FROM News nw WHERE nw.id > ?1 AND nw.language = ?2 ORDER BY nw.date DESC",
					News.class);
			query.setParameter(2, language);
		} else if (language.equals("EN")) {
			query = db.createQuery("SELECT nw FROM News nw WHERE nw.id > ?1 AND nw.language = ?2 ORDER BY nw.date DESC",
					News.class);
			query.setParameter(2, language);
		} else if (language.equals("ALL")) {
			query = db.createQuery("SELECT nw FROM News nw WHERE nw.id > ?1 ORDER BY nw.date DESC", News.class);

		} else {
			return null;
		}
		query.setParameter(1, untilTo);
		try {
			return query.getResultList();
		} catch (Exception e) {
			System.out.println("Upps, Something happened in the database");
			return null;
		}
	}

	/**
	 * This function returns how many news are in the database
	 * 
	 * @return how many news are in the database
	 */
	@Override
	public int newsQuantity(String language) {
		List<News> news = null;
		if (language.equals("ALL")) {
			news = retrieveAllNews();
		} else {
			news = retrieveAllNewsByLanguage(language);
		}
		if (news == null) {
			return -1;
		} else {
			return news.size();
		}
	}

}
