/*
 * @Auther Varshesh Patel
 */
package SearchEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.naming.directory.SearchResult;

import org.jsoup.nodes.Document;

import Utility.Convert;
import Utility.IO;
import sorting.Sort;
import Core.Path;
import Core.Correct.Correct;
import Core.Search.SearchPool;
import Core.crawler.Crawler;
import Elements.Page;
import Elements.Result;
import Core.Huffman.*;

/**
 * The Class Engine.
 */
public class Engine {
	
	/** The cr. */
	private Crawler cr;
	
	/** The search engine. */
	private SearchPool searchEngine;
	
	/** The auto correct. */
	private Correct autoCorrect;
	
	/** The pages. */
	private HashMap<String, Elements.Page> pages;
	
	/** The Urls. */
	private HashSet<String> Urls;

	/**
	 * Instantiates a new engine.
	 */
	public Engine() {
		Initialize();
		InitialCrawl(true, false);
		InitialUnZipping();
		IO.outn("Search engine is initialized ...");
	}

	/**
	 * Initialize.
	 */
	@SuppressWarnings("unchecked")
	private void Initialize() {
		IO.outn("Initializing engine");

		if (IO.checkFileExist(Core.Path.RefPath + "pages.obj") && IO.checkFileExist(Core.Path.RefPath + "url.obj")) {
			this.pages = (HashMap<String, Page>) IO.ReadObjectFromFile(Core.Path.RefPath + "pages.obj");
			this.Urls = (HashSet<String>) IO.ReadObjectFromFile(Core.Path.RefPath + "url.obj");
		} else {
			this.pages = new HashMap<String, Elements.Page>();
			this.Urls = new HashSet<String>();
		}
		autoCorrect = new Correct();
	}

	/**
	 * Initial crawl.
	 *
	 * @param store the store
	 * @param compress the compress
	 */
	private void InitialCrawl(boolean store, boolean compress) {
		IO.outn("Initializing starting crawl ...");
		if (this.Urls.isEmpty()) {
			int i = 50;
			for (String s : IO.readTextFile(Core.Path.RefPath + "initUrls.txt").split("\n")) {
				this.crawle(s, 5, store, compress);
				i--;
				if (i == 0) {
					break;
				}
			}
		}
		this.saveEngine(true);
	}

	/**
	 * Initial un zipping.
	 */
	private void InitialUnZipping() {
		IO.outn("unziping page for run time...");
		for (Iterator<String> index = pages.keySet().iterator(); index.hasNext();) {
			String key = index.next();
			if (pages.get(key).isCompressed()) {
				HuffmanEncoding.decode(Path.CompStorePath + pages.get(key).getId() + ".huffman",
						Path.TempUnCompStorePath + pages.get(key).getId() + ".html", true);
			}
		}

	}

	/**
	 * Save engine.
	 *
	 * @param print the print
	 */
	public void saveEngine(boolean print) {

		IO.WriteObjectToFile(Core.Path.RefPath + "pages.obj", this.pages);
		IO.WriteObjectToFile(Core.Path.RefPath + "url.obj", this.Urls);
		if (print) {
			IO.outn("Engine state saved");
		}
	}

	/**
	 * Close engine.
	 */
	public void closeEngine() {
		this.saveEngine(true);
		IO.outn("Closing Search Engine");
		IO.outn("zip pages ...");
		for (Iterator<String> index = pages.keySet().iterator(); index.hasNext();) {
			String key = index.next();
			if (pages.get(key).isCompressed()) {
				HuffmanEncoding.deleteIfExists(Path.TempUnCompStorePath + pages.get(key).getId() + ".html");
			}
		}
	}

	/**
	 * Gets the search result.
	 *
	 * @param search the search
	 * @return the search result
	 */
	public List<Result> getSearchResult(String search) {
		if (this.Urls.isEmpty())
			return null;
		int cores = Runtime.getRuntime().availableProcessors();
		searchEngine = new SearchPool(cores);
		try {
			List<Elements.SearchResult> output = searchEngine.executeSearch(pages, search);
			List<Result> result = new ArrayList<Result>();
			for (Elements.SearchResult i : output) {
				pages.get(i.index).isMoreRelevent = i.moreRelavent;
				result.add(new Result(pages.get(i.index), i.count, i.index, i.searchString));
			}
			if (result.isEmpty())
				return null;
			Result[] arr = Convert.convertToArray(result);
			Sort.quicksort(arr);
			result = Arrays.asList(arr);
			Collections.reverse(result);
			return result;
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Update visit.
	 *
	 * @param index the index
	 * @param output the output
	 */
	public void updateVisit(int index, List<Result> output) {
		for (int i = 0; i < output.size(); i++) {
			if (i == index) {
				var temp = output.get(i);
				pages.get(temp.index).visited(output.size(), temp.frequency);
			} else {
				var temp = output.get(i);
				pages.get(temp.index).notVisited(output.size(), temp.frequency);
			}
		}
		this.saveEngine(false);
	}

	/**
	 * Crawle.
	 *
	 * @param url the url
	 * @param max_url the max url
	 * @param store the store
	 * @param compress the compress
	 * @return the int
	 */
	public int crawle(String url, int max_url, boolean store, boolean compress) {
		cr = new Crawler(max_url, this.Urls);
		try {
			cr.crawlUrl(url);
		} catch (Exception ex) {
			IO.outn("Error: Could not able to fetch one or more urls within following url");
			IO.outn("\tUrl: " + url);
		}
		this.Urls = cr.getUrls();
		var out = cr.getPages();
		int old = this.pages.size();
		int init = this.pages.size();
		for (Iterator<String> iterator = out.keySet().iterator(); iterator.hasNext();) {
			String s = iterator.next();
			Document doc = out.get(s);
			Page p = new Page(s, init);
			if (store) {
				if (compress) {
					try {
						Utility.IO.writeTextFile(doc.toString(), Path.TempUnCompStorePath + init + ".html");
						try {
							HuffmanEncoding.encode(Path.TempUnCompStorePath + init + ".html",
									Path.CompStorePath + init + ".huffman", 0);
							p.changeCompressedState(true);
						} catch (Exception ex) {
							IO.outn("Error: Could not able to compress file or store compressed file.");
							IO.outn("\tUrl\t: " + url);
							IO.outn("\tstatus\t: url stored as un compressed html.");
						}
						p.changeStoreState(true);
					} catch (Exception ex) {
						IO.outn("Error: Could not able to compress file or store compressed file.");
						IO.outn("\tUrl\t: " + url);
						IO.outn("\tstatus\t: url stored as page link. search time internet fetch.");
					}
				} else {
					try {
						Utility.IO.writeTextFile(doc.toString(), Path.HtmlStorePath + init + ".html");
						p.changeStoreState(true);
					} catch (Exception ex) {
						IO.outn("Error: Could not able to compress file or store compressed file.");
						IO.outn("\tUrl\t: " + url);
						IO.outn("\tstatus\t: url stored as page link. search time internet fetch.");
					}
				}
			}
			pages.put(s, p);
			init++;
		}
		return init - 1 - old;
	}

	/**
	 * Gets the auto correct.
	 *
	 * @param searchString the search string
	 * @return the auto correct
	 */
	public String[] getAutoCorrect(String searchString) {
		return this.autoCorrect.getAutoCorrect(searchString);
	}
}
