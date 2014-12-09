package net.justonlyone.snippets.http.crawler;

import java.util.Set;

/**
 * 实例化一个crwler需要一个种子
 * 
 * @author Xinyuyu
 *
 */
public class CrawlerAction {

	private void initCrawler(String[] seeds) {
		for (String seed : seeds) {
			UrlPool.inQueue(seed);
		}
	}

	public CrawlerAction(String[] seeds) {
		initCrawler(seeds);
		System.out.println(UrlPool.queueNum());
		// 若urlpool中的元素超过1000并队列中的元素不为空的时候虚幻
		while ((UrlPool.queueNum() > 0) && (UrlPool.poolNum() < 1000)) {
			String visitUrl = UrlPool.outQueue();
			if (visitUrl == null) {
				continue;
			}
			DownloadFile downloadFile = new DownloadFile();
			String filePath = downloadFile.downloadFile(visitUrl);
			UrlPool.addPool(visitUrl);
			HtmlParser htmlParser = new HtmlParser();
			Set<String> links = htmlParser.extraLinks(visitUrl);
			// 若urlpool中不包含该链接的时候就入队
			for (String link : links) {
				UrlPool.inQueue(link);
			}
			System.out.println(visitUrl + "::" + filePath);
		}
	}
}