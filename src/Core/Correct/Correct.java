/*
 * @Author Jahnavi Shah
 */
package Core.Correct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import Core.Path;
import Utility.IO;


/**
 * The Class Correct.
 */
public class Correct {
	
	/** The words. */
	ArrayList<String> words = new ArrayList<>();

	/**
	 * Initialize.
	 */
	public void Initialize() {
		String data = Utility.IO.readTextFile(Path.RefPath + "/dictionary.txt");
		StringTokenizer st = new StringTokenizer(data, " \n\t\r");
		while (st.hasMoreTokens()) {
			String tk = st.nextToken().toLowerCase(Locale.ROOT);
			if (!words.contains(tk)) {
				words.add(tk);
			}
		}
	}

	/**
	 * Instantiates a new correct.
	 */
	public Correct() {
		Initialize();
	}

	/**
	 * Gets the auto correct.
	 *
	 * @param query the query
	 * @return the auto correct
	 */
	public String[] getAutoCorrect(String query) {
		HashMap<String, Integer> map = new HashMap<>();
		String[] altWords = new String[10];
		for (String w : words) {
			int editDis = editDistance(query, w);
			map.put(w, editDis);
		}
		Map<String, Integer> map1 = sortByValue(map);
		int rank = 0;
		for (Map.Entry<String, Integer> en : map1.entrySet()) {
			if (en.getValue() != 0) {
				altWords[rank] = en.getKey();
				rank++;
				if (rank == 10) {
					break;
				}
			}
		}
		return altWords;
	}

	/**
	 * Sort by value.
	 *
	 * @param map the Hash map
	 * @return the hash map
	 */
	public HashMap<String, Integer> sortByValue(HashMap<String, Integer> map) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());

		// Sort the list
		list.sort(Map.Entry.comparingByValue());

		// put data from sorted list to hashmap
		HashMap<String, Integer> temp = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	/**
	 * Edits the distance.
	 *
	 * @param word1 the word 1
	 * @param word2 the word 2
	 * @return the int
	 */
	private int editDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();
		int[][] dp = new int[len1 + 1][len2 + 1]; // Edit distance table

		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}
		// iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);

				// if last two chars equal
				if (c1 == c2) {
					// update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;

					int min = Math.min(replace, insert);
					min = Math.min(delete, min);
					dp[i + 1][j + 1] = min;
				}
			}
		}
		return dp[len1][len2];
	}
}
