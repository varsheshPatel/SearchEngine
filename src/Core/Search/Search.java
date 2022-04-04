/*
 * @Auther Hash Patel
 */
package Core.Search;

import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import Elements.Page;
import Elements.SearchResult;
import textprocessing.BoyerMoore;;



/**
 * The Class Search.
 */
public class Search implements Callable<SearchResult> {
	
	/** The page. */
	private Page page;
	
	/** The index. */
	private String index;
	
	/** The search string. */
	private String searchString;
	
	/** The m R. */
	private boolean mR;

	/**
	 * Instantiates a new search.
	 *
	 * @param page the page
	 * @param index the index
	 * @param searchString the search string
	 * @param mR the m R
	 */
	public Search(Page page, String index, String searchString, boolean mR) {
		this.page = page;
		this.index = index;
		this.searchString = searchString;
		this.mR = mR;
	}

	/**
	 * Call.
	 *
	 * @return the search result
	 * @throws Exception the exception
	 */
	@Override
	public SearchResult call() throws Exception {
		String fileText = "";
		if (!page.isStored()) {
			Document p = Jsoup.connect(page.getUrl()).get();
			fileText = p.text();
		} else {
			if (page.isCompressed()) {

				fileText = Utility.IO.readTextFile(Core.Path.TempUnCompStorePath + page.getId() + ".html");
				fileText = Jsoup.parse(fileText,"UTF-8").text();
			} else {
				fileText = Utility.IO.readTextFile(Core.Path.HtmlStorePath + page.getId() + ".html");
				fileText = Jsoup.parse(fileText,"UTF-8").text();
			}

		}
		byte[] bytes = fileText.getBytes();
		fileText= new String(bytes, "UTF-8");
		return new SearchResult(this.index, searchString(fileText), mR, this.searchString);
	}

	/**
	 * Search string.
	 *
	 * @param fileText the file text
	 * @return the integer
	 */
	private Integer searchString(String fileText) {
		var bm = new BoyerMoore(this.searchString);
		int count = 0;
		String inp = fileText;

		while (true) {
			int index=0;
			try {
				index = bm.search(inp);
				
			}
			catch(Exception ex) {
				index=inp.length()+1;
			}
			if (inp.length() <= index) {
				break;
			}
			inp = inp.substring(index + this.searchString.length());
			count++;
		}

		return count;
	}
}