/*
 * @Auther AalakNanda 
 */
package Elements;

import java.io.Serializable;

import Utility.IO;

// TODO: Auto-generated Javadoc
/**
 * The Class Page.
 */
public class Page implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4049481439176050518L;
	
	/** The rank. */
	private double rank;
	
	/** The visited. */
	private int visited;
	
	/** The is compressed. */
	private boolean isCompressed;
	
	/** The is stored. */
	private boolean isStored;
	
	/** The url. */
	private String url;
	
	/** The page ID. */
	private int pageID;
	
	/** The is more relevent. */
	public boolean isMoreRelevent;

	/**
	 * Instantiates a new page.
	 *
	 * @param url the url
	 * @param pageId the page id
	 */
	public Page(String url, int pageId) {
		this.pageID = pageId;
		this.rank = 1;
		this.visited = 0;
		this.isCompressed = false;
		this.isStored = false;
		this.url = url;
		this.isMoreRelevent = false;
	}

	/**
	 * Visited.
	 *
	 * @param num_pages the num pages
	 * @param searchCount the search count
	 */
	public void visited(int num_pages, int searchCount) {
		this.visited++;
		this.calculateRank(num_pages, searchCount, true);
	}

	/**
	 * Not visited.
	 *
	 * @param num_pages the num pages
	 * @param searchCount the search count
	 */
	public void notVisited(int num_pages, int searchCount) {
		this.calculateRank(num_pages, searchCount, false);
	}

	/**
	 * Calculate rank.
	 *
	 * @param num_pages the num pages
	 * @param searchCount the search count
	 * @param isVisit the is visit
	 */
	private void calculateRank(int num_pages, int searchCount, boolean isVisit) {
		if (isVisit) {
			double frac=(1.0 + ((this.visited * searchCount) / ((this.visited * searchCount) + (2.4))));
			double per=(1.0 / num_pages);
			double new_val =  per* frac; 
			this.rank = this.rank + new_val;

		} else {
			if (rank > 1) {
				double new_val= (1.0 / num_pages)
						* (1 + ((this.visited * searchCount) / ((this.visited * searchCount) + 2.4))) * 1/searchCount;
				this.rank=this.rank-new_val;
			}
			if (rank < 1) {
				rank = 1;
			}
		}
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Change compressed state.
	 *
	 * @param state the state
	 */
	public void changeCompressedState(boolean state) {
		this.isCompressed = state;
	}

	/**
	 * Change store state.
	 *
	 * @param state the state
	 */
	public void changeStoreState(boolean state) {
		this.isStored = state;
	}

	/**
	 * Checks if is compressed.
	 *
	 * @return true, if is compressed
	 */
	public boolean isCompressed() {
		return this.isCompressed;
	}

	/**
	 * Checks if is stored.
	 *
	 * @return true, if is stored
	 */
	public boolean isStored() {
		return this.isStored;
	}

	/**
	 * Gets the rank.
	 *
	 * @return the rank
	 */
	public double getRank() {
		return this.rank;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return this.pageID;
	}
}
