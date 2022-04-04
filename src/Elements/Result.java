/*
 * @Auther Alaknanda Faldu
 */


package Elements;

import Utility.IO;


/**
 * The Class Result.
 */
public class Result implements Comparable<Result> {
	
	/** The page. */
	public Elements.Page page;
	
	/** The frequency. */
	public int frequency;
	
	/** The index. */
	public String index;
	
	/** The search string. */
	public String searchString;

	/**
	 * Instantiates a new result.
	 *
	 * @param page the page
	 * @param frequancy the frequancy
	 * @param index the index
	 * @param searchString the search string
	 */
	public Result(Elements.Page page, int frequancy, String index, String searchString) {
		this.page = page;
		this.frequency = frequancy;
		this.index = index;
		this.searchString = searchString;
	}

	/**
	 * Compare to.
	 *
	 * @param o the o
	 * @return the int
	 */
	@Override
	public int compareTo(Result o) {
		Result e = (Result) o;
		if (this.page.isMoreRelevent && o.page.isMoreRelevent) {
			if (this.frequency == e.frequency) {
				if (this.page.getRank() == o.page.getRank()) {
					return 0;
				} else if (this.page.getRank() < o.page.getRank()) {
					return -1;
				} else {
					return 1;
				}

			} else if (this.frequency < e.frequency) {
				if (this.page.getRank() == o.page.getRank()) {
					return -1;
				} else if (this.page.getRank() < o.page.getRank()) {
					return -1;
				} else {
					return 1;
				}
			} else {
				if (this.page.getRank() == o.page.getRank()) {
					return 1;
				} else if (this.page.getRank() < o.page.getRank()) {
					return -1;
				} else {
					return 1;
				}
			}
		} else if (this.page.isMoreRelevent && !o.page.isMoreRelevent) {
			return 1;
		} else if (o.page.isMoreRelevent && !this.page.isMoreRelevent) {
			return -1;
		} else {
			if (this.frequency == e.frequency) {
				if (this.page.getRank() == o.page.getRank()) {
					return 0;
				} else if (this.page.getRank() < o.page.getRank()) {
					return -1;
				} else {
					return 1;
				}

			} else if (this.frequency < e.frequency) {
				if (this.page.getRank() == o.page.getRank()) {
					return -1;
				} else if (this.page.getRank() < o.page.getRank()) {
					return -1;
				} else {
					return 1;
				}
			} else {
				if (this.page.getRank() == o.page.getRank()) {
					return 1;
				} else if (this.page.getRank() < o.page.getRank()) {
					return -1;
				} else {
					return 1;
				}
			}
		}
	}
}
