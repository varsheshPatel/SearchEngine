/*
 *  @Auther Megh Patel
 */
package Core.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import Utility.IO;


/**
 * The Class Crawler.
 */
public class Crawler {
	
	/** The pages. */
	Hashtable<String, Document> pages;
	
	/** The urls. */
	HashSet<String> urls;
	
	/** The limit. */
	int limit;
	
	/** The data base urls. */
	HashSet<String> dataBaseUrls;

	/**
	 * Instantiates a new crawler.
	 *
	 * @param limit the limit
	 * @param urls  the urls
	 */
	public Crawler(int limit, HashSet<String> urls) {
		pages = new Hashtable<String, Document>();
		this.limit = limit;
		this.urls = urls;
	}

	/**
	 * Crawl url.
	 *
	 * @param url the url
	 */
	public void crawlUrl(String url) {
		if (!this.urls.contains(url)) {
			if (limit == 0) {
				return;
			}
			IO.outn("\t crawling url:" + url);
			limit--;
			urls.add(url);

			for (String i : extractUrls(url)) {
				crawlUrl(i);
			}
		}
	}

	/**
	 * Extract urls.
	 *
	 * @param url the url
	 * @return the array list
	 */
	public ArrayList<String> extractUrls(String url) {
		ArrayList<String> arr_urls = new ArrayList<String>();
		try {
			Document page = Jsoup.connect(url).get();
			pages.put(url, page);
			Elements urlLinks = page.select("a[href]");
			for (Element e : urlLinks) {
				if (!arr_urls.contains(e.attr("abs:href"))) {
					arr_urls.add(e.attr("abs:href"));
				}
			}
			String text = page.html();
			String reg = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
			Pattern pattern = Pattern.compile(reg);
			Matcher match = pattern.matcher(text);
			while (match.find()) {

				arr_urls.add(match.group(0));

			}

		} catch (IOException e) {
			IO.outn("Error: " + e.getMessage());
			IO.outn("\tUrl: " + url);
		}
		return arr_urls;
	}

	/**
	 * Gets the pages.
	 *
	 * @return the pages
	 */
	public Hashtable<String, Document> getPages() {
		return this.pages;
	}

	/**
	 * Gets the urls.
	 *
	 * @return the urls
	 */
	public HashSet<String> getUrls() {
		return this.urls;
	}
}
