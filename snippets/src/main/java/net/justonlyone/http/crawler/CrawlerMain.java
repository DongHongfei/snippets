package net.justonlyone.http.crawler;

public class CrawlerMain {
	public static void main(String[] args) {
		String[] seeds = { "http://www.coderanch.com/t/63473/open-source/host-parameter-null-httpclient" };
		CrawlerAction crawler = new CrawlerAction(seeds);

	}
}