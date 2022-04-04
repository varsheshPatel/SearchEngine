/*
 * @Auther Hash Patel
 */
package Elements;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchResult.
 */
public class SearchResult {
	
	/** The index. */
	public String index;
	
	/** The count. */
	public Integer count;
	
	/** The more relavent. */
	public boolean moreRelavent;
	
	/** The search string. */
	public String searchString;
	
	/**
	 * Instantiates a new search result.
	 *
	 * @param index the index
	 * @param count the count
	 * @param moreRelavent the more relavent
	 * @param SearchString the search string
	 */
	public SearchResult(String index,Integer count,boolean moreRelavent,String SearchString) {
		this.index=index;
		this.count=count;
		this.moreRelavent=moreRelavent;
		this.searchString=SearchString;
	}
}
