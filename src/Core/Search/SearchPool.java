/*
 * @Auther Hash Patel
 */
package Core.Search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import Core.Path;
import Core.Huffman.HuffmanEncoding;
import Elements.SearchResult;
import Utility.IO;
import Elements.Page;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchPool.
 */
public class SearchPool {
	
	/** The executor. */
	private ThreadPoolExecutor executor;

	/**
	 * Instantiates a new search pool.
	 *
	 * @param pool_size the pool size
	 */
	public SearchPool(int pool_size) {
		this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(pool_size);
	}

	/**
	 * Execute search.
	 *
	 * @param pages the pages
	 * @param searchString the search string
	 * @return the array list
	 * @throws InterruptedException the interrupted exception
	 */
	public ArrayList<SearchResult> executeSearch(HashMap<String, Page> pages, String searchString)
			throws InterruptedException {
		Set<String> keys = pages.keySet();
		List<Future<SearchResult>> resultList = null;
		List<Search> taskList = new ArrayList<>();
		ArrayList<SearchResult> output = new ArrayList<>();
		for (Iterator<String> index = keys.iterator(); index.hasNext();) {
			String key = index.next();
			taskList.add(new Search(pages.get(key), key, searchString, true));
			StringTokenizer st = new StringTokenizer(searchString, " ,/\\'.\t");

			if (st.countTokens() > 1) {
				while (st.hasMoreTokens()) {
					String tk = st.nextToken();
					taskList.add(new Search(pages.get(key), key, tk, false));
				}
			}
		}
		resultList = executor.invokeAll(taskList);
		executor.shutdown();
		for (int i = 0; i < resultList.size(); i++) {

			try {
				SearchResult result = resultList.get(i).get();
				if (result.count != 0) {
					output.add(result);
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
}
