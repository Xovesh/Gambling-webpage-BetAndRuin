package businessLogicV3.publicAPI.news;

import java.util.List;

import businessLogicV3.privateAPI.BLFacadePrivate;
import domainV2.News;

public class BLFacadePublicNewsImplementation implements BLFacadePublicNews{

	private BLFacadePrivate privateAPI;
	public BLFacadePublicNewsImplementation(BLFacadePrivate privateAPI) {
		this.privateAPI = privateAPI;
	}
	
	/**
	 * @param amount
	 *            of news to retrieve
	 * @param language
	 *            of the new ES, EUS or EN
	 * @return list with the news
	 */

	@Override
	public List<News> getLatestNews(int amount, String language) {
		List<News> res = privateAPI.getNews().retrieveLastNewsByLanguage(amount, language);
		return res;
	}

}
