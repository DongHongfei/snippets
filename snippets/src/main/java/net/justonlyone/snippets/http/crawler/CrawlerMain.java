package net.justonlyone.snippets.http.crawler;

public class CrawlerMain {
	public static void main(String[] args) {
		String[] seeds = { "http://www.shicimingju.com" };
		CrawlerAction crawler = new CrawlerAction(seeds);
	}
}