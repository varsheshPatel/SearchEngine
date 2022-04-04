/*
 * @Auther Varshesh Patel
 */
package SearchEngine;

import java.awt.Desktop;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import Elements.Result;
import Utility.IO;
import textprocessing.BoyerMoore;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main {
	
	/**
	 * Search query.
	 *
	 * @param eng the eng
	 * @return the engine
	 */
	public static Engine SearchQuery(Engine eng) {
		Scanner sc = new Scanner(System.in);
		IO.out("Enter Search String");
		String query = sc.nextLine();
		long t1 = System.nanoTime();
		List<Result> results = eng.getSearchResult(query);
		long t2 = System.nanoTime();
		if (results == null) {
			IO.outn("No results found");
			String[] corrections = eng.getAutoCorrect(query);
			IO.outn("\n you might search with the following");
			int i = 1;
			String Output = "";
			for (String s : corrections) {
				Output += "\t" + s;
				if (i % 3 == 0) {
					Output += "\n";
				}
				i++;
			}
			IO.outn(Output);
		} else {
			IO.outn("total page found:" + results.size() + " in time " + (t2 - t1) / 1000 + " microseconds");
			int start = 0;
			int diff = 10;
			while (true) {
				boolean do_block = true;
				if (do_block) {
					if (results.size() < diff) {
						String o = "";
						for (; start < results.size(); start++) {
							Result r = results.get(start);
							String str = "\t\t[" + start + "]" + " " + r.index + "\n";
							str += String.format("\t\t\t search match: [ %10s" + " ]\n", r.searchString);
							str += String.format("\t\t\t         rank: [   %8f" + " ]\n", r.page.getRank());
							str += String.format("\t\t\t        count: [ %10s" + " ]\n", r.frequency);
							o += str + "\n";
						}
						diff = start + 10;
						IO.outn(o);
						do_block = false;
					} else {
						String o = "";
						for (; start <= diff; start++) {
							Result r = results.get(start);
							String str = "\t\t[" + start + "]" + " " + r.index + "\n";
							str += String.format("\t\t\t search match: [ %10s" + " ]\n", r.searchString);
							str += String.format("\t\t\t         rank: [   %8f" + " ]\n", r.page.getRank());
							str += String.format("\t\t\t        count: [ %10s" + " ]\n", r.frequency);
							o += str + "\n";
						}
						IO.outn(o);
						diff = start + 10;
					}
				}
				while (true) {
					if (do_block) {
						IO.outn("Select options \n\t1-more pages \n\t2-open page with index \n\t3-exit\n");
						try {
							int option = sc.nextInt();
							if (option == 1) {
								break;
							} else if (option == 2) {
								IO.out("Enter page index to open");
								option = sc.nextInt();
								if (0 <= option && option < results.size()) {
									Result r = results.get(option);
									Desktop.getDesktop().browse(new URI(r.page.getUrl()));
									eng.updateVisit(start, results);
									eng.saveEngine(false);
									return eng;
								} else {
									IO.outn("Invalid index !\n");
								}

							} else if (option == 3) {
								return eng;
							} else {
								IO.outn("Invalid Input !\n");
							}
						} catch (Exception ex) {
							IO.outn("Invalid Input !\n");
						}

					} else {
						IO.outn("Select options \n\t1-open page with index \n\t2-exit\n");
						try {
							int option = sc.nextInt();
							if (option == 1) {
								IO.out("Enter page index to open");
								option = sc.nextInt();
								if (0 <= option && option < results.size()) {
									Result r = results.get(option);
									Desktop.getDesktop().browse(new URI(r.page.getUrl()));
									eng.updateVisit(option, results);
									eng.saveEngine(false);
									return eng;
								} else {
									IO.outn("Invalid index !\n");
								}

							} else if (option == 2) {
								return eng;
							} else {
								IO.outn("Invalid Input !\n");
							}
						} catch (Exception ex) {
							IO.outn("Invalid Input !\n");
						}
					}

				}
			}
		}
		return eng;

	}

	/**
	 * Crawl.
	 *
	 * @param eng the eng
	 * @return the engine
	 */
	public static Engine crawl(Engine eng) {
		while (true) {
			IO.outn("Select options \n\t1-crawl url \n\t2-exit\n");
			Scanner sc = new Scanner(System.in);
			try {
				int option = sc.nextInt();
				switch (option) {
				case 1:
					IO.out("Enter Url ");
					sc = new Scanner(System.in);
					String url = sc.nextLine();
					boolean storeFlag = false;
					boolean compressFlag = false;
					while (true) {
						IO.outn("Want store page in storage \n\t1-Yes \n\t2-No\n");
						option = sc.nextInt();
						if (option == 1) {
							storeFlag = true;
							break;
						} else if (option == 2) {
							storeFlag = false;
							break;
						}

					}
					while (true && storeFlag) {
						IO.outn("Want compress page in storage \n\t1-Yes \n\t2-No\n");
						option = sc.nextInt();
						if (option == 1) {
							compressFlag = true;
							break;
						} else if (option == 2) {
							compressFlag = false;
							break;
						}

					}
					IO.outn("How many maximum url visit( not more value means more time to scrap)");
					option = sc.nextInt();
					IO.outn("crawling started ...");
					long t1 = System.nanoTime();
					int urls = eng.crawle(url, option, storeFlag, compressFlag);
					long t2 = System.nanoTime();
					IO.outn("crawling compilted ...");
					IO.outn("[" + urls + "]urls added in search engine in:[" + (t2 - t1) / 1000000 + " ms]");
					eng.saveEngine(false);
					return eng;
				case 2:
					return eng;
				default:
					IO.outn("Invalid input");
				}
			} catch (Exception ex) {
				IO.outn("Invalid input");
			}

		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Engine eng = new Engine();
		while (true) {
			Scanner sc = new Scanner(System.in);
			IO.outn("Select options \n\t1- Search \n\t2-crawl url \n\t3-exit\n");
			try {
				int option = sc.nextInt();
				switch (option) {
				case 1:
					eng = SearchQuery(eng);
					break;
				case 2:
					eng = crawl(eng);
					break;
				case 3:
					eng.saveEngine(true);
					return;
				default:
					IO.outn("Invalid Input !\n");
				}
			} catch (Exception ex) {
				IO.outn("Invalid Input !\n");
			}
		}
	}
}
