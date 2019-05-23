package businessLogicV3.publicAPI.news;

import java.util.List;

import domainV2.News;

public interface BLFacadePublicNews {
	/**
	 * @param amount
	 *            of news to retrieve
	 * @param language
	 *            of the new ES, EUS or EN
	 * @return list with the news
	 */
	public List<News> getLatestNews(int amount, String language);
}
