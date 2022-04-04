package Core.Huffman;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * The Class HuffmanEncoding.
 */
public class HuffmanEncoding {
	
	/**
	 * The Class TreeNode.
	 */
	private static class TreeNode {
		
		/** The my left. */
		private TreeNode myLeft;
		
		/** The my right. */
		private TreeNode myRight;
		
		/** The character. */
		private String character;
        
        /** The frequency. */
        private int frequency;
       
		/**
		 * Instantiates a new tree node.
		 *
		 * @param left the left
		 * @param right the right
		 * @param ch the ch
		 * @param freq the freq
		 */
		public TreeNode(TreeNode left, TreeNode right, String ch, int freq) {
			myLeft = left;
			myRight = right;
			character = ch;
			frequency = freq;
		}
		
		/**
		 * Checks if node is leaf.
		 *
		 * @return true, if is leaf
		 */
		public boolean isLeaf() {
			return myLeft == null && myLeft == null;
		}
	}
	
	
	/**
	 * Hashmap object that stores the frequency of each letter character in the file. 
	 *
	 * @param iterator the iterator
	 * @return the hash map
	 */
	public static HashMap<String, Integer> frequencyCount(Iterator<String> iterator) {
		HashMap<String, Integer> binaryFrequency = new HashMap<String, Integer>();
		
		while (iterator.hasNext()) { 
			String binary = iterator.next();
			
			if (binaryFrequency.containsKey(binary))
				binaryFrequency.put(binary, binaryFrequency.get(binary) + 1);
			else
				binaryFrequency.put(binary, 1);
		}
		return binaryFrequency;
	}
	
	
	//  
	/**
	 * A method that shows the frequency of each character.
	 *
	 * @param binaryFrequency the binary frequency
	 */
	public static void showFrequency(HashMap<String, Integer> binaryFrequency) {
		for (Map.Entry<String, Integer> entry : binaryFrequency.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		    String str = new Character((char)Integer.parseInt(key, 2)).toString();
		    
		    System.out.println(key + " : " + str + " = " + value);
		}
	}
	
	
	// 
	/**
	 * Methods the looks for the characters with the minimum frequency and then removes them from the hashmap to create the huffman tree.  
	 *
	 * @param binaryFrequency the binary frequency
	 * @return the map. entry
	 */
	public static Map.Entry<String, Integer> delMinFrequency(HashMap<String, Integer> binaryFrequency) {		
		Integer min = Collections.min(binaryFrequency.values());
		
		for (Map.Entry<String, Integer> entry : binaryFrequency.entrySet()) {
			if (entry.getValue() == min) {
				binaryFrequency.remove(entry.getKey());
				return entry;
			}
		}
		return null;
	}
	
	/** The comparator based on frequency. */
    public static Comparator<TreeNode> idComparator = new Comparator<TreeNode>(){
         
        @Override
        public int compare(TreeNode c1, TreeNode c2) {
            return (c1.frequency - c2.frequency);
        }
    };
	
	/**
	 * build the Huffman trie according to the frequencies
	 *
	 * @param binaryFrequency the binary frequency
	 * @return the hash map
	 */
	public static HashMap<String, String> buildTrie(HashMap<String, Integer> binaryFrequency) {
		PriorityQueue<TreeNode> leafNode = new PriorityQueue<TreeNode>(binaryFrequency.size(), idComparator);
		
		// create the leafs
		while (binaryFrequency.size() > 0) {
			Map.Entry<String, Integer> leaf = delMinFrequency(binaryFrequency);
		    leafNode.add(new TreeNode(null, null, leaf.getKey(), leaf.getValue()));
		}
	
		// merge the leafs
		while (leafNode.size() > 1) {	
			TreeNode left = leafNode.remove();
			TreeNode right = leafNode.remove();
			leafNode.add(new TreeNode(left, right, null, left.frequency + right.frequency));
		}
		
		// Construct a table mapping characters
		return codewords(new HashMap<String, String>(), leafNode.peek(), "");
	}
	
	
	/**
	 * Method creating a hashmap of encoded codewords relating to each ASCII term. .
	 *
	 * @param cws the cws
	 * @param x the x
	 * @param s the s
	 * @return the hash map
	 */
	
	public static HashMap<String, String> codewords(HashMap<String, String> cws, TreeNode x, String s) {
    	if (x.isLeaf())
    		cws.put(x.character, s);
    	else {
    		codewords(cws, x.myLeft, s + "0");
    		codewords(cws, x.myRight, s + "1");
    	}
    	return cws;
    }
    
	
	/**
	 * Method that creates a header at the beginning of the file. 
	 *
	 * @param codewords the codewords
	 * @return the string
	 */
	public static String formatHeader(HashMap<String, String> codewords) {
		String header = "";

		for (Map.Entry<String, String> entry : codewords.entrySet())
		    header += entry.getKey() + "," + entry.getValue() + "\n";
		return header + "\n";
	}
	
	/**
	 * Method that converts the encoded binary strings into a format of 8 bits each. 
	 *
	 * @param binary the binary
	 * @return the string
	 */
	public static String convertTo8bits(String binary) {
		String zeros = "";
		
		for (int i = 0; i != (8 - binary.length()); i++)
			zeros += "0";
		return zeros + binary;
	}
	
	
	/**
	 * Method that displays the header in file format.
	 *
	 * @param header the header
	 * @param outputFileName the output file name
	 */ 
    public static void writeHeader(String header, String outputFileName) {
    	for (int i = 0; i < header.length(); i++) {
    		String section = convertTo8bits(Integer.toBinaryString(header.charAt(i)));
    		FileOutputHelper.writeBinStrToFile(section, outputFileName);
    	}
    }
    
    
    /**
     * Encoding each individual character. 
     *
     * @param codes the codes
     * @param iterator the iterator
     * @return the string builder
     */
    public static StringBuilder  encodeCharacters(HashMap<String, String> codes, Iterator<String> iterator) {
    	StringBuilder binChar = new StringBuilder("");
  
    	while (iterator.hasNext())
    		binChar.append(codes.get(iterator.next()));
    	return binChar.append(codes.get("EOF"));
    }
    
    /**
     * Method that creates the body of the encoded file.
     *
     * @param binChar the bin char
     * @param outputFileName the output file name
     */ 
    public static void writeBody(StringBuilder binChar, String outputFileName) {
    	// write the bytes to the file 
    	while (binChar.length() > 8) {
    		int readMax = 8 * (binChar.length() / 8);
    		FileOutputHelper.writeBinStrToFile(binChar.substring(0, readMax), outputFileName);
    		binChar.delete(0, readMax);
    	}
    	
    	if (binChar.length() != 0) {
    		for (int i = binChar.length(); i != 8; i++)
    			binChar.append("0");
    		FileOutputHelper.writeBinStrToFile(binChar.toString(), outputFileName);
    	}
    }
    
    
    /**
     * Method that loads the encoded file. 
     *
     * @param it the it
     * @return the queue
     */
    public static Queue<String> loadFile(Iterator<String> it) {
    	Queue<String> bin = new LinkedList<String>();
    	
    	while (it.hasNext())
    		bin.add(it.next());
    	return bin;
    }
    
    /**
     * tries to delete a non-existing file
     *
     * @param path the path
     */
    public static void deleteIfExists(String path) {
	   File f = new File(path);
	   
	   try {
		   f.delete();
		   f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * Overall encoding method. 
     *
     * @param target the target
     * @param destination the destination
     * @param n the n
     */
	public static void encode(String target, String destination, int n) {
		// Load the file
		Queue<String> file = loadFile(n > 0 ? new FileFreqWordsIterator(target, n) : new FileCharIterator(target));

		// Counts the frequency of each character
		HashMap<String, Integer> binaryFrequency = frequencyCount(file.iterator());

		// End of the file code
		binaryFrequency.put("EOF", 1);

		// build the Huffman tree
		HashMap<String, String> codes = buildTrie(binaryFrequency);

		// get the encode header
		String header = formatHeader(codes);
		
		// encode all the characters according to the codewords 
		StringBuilder binChar = encodeCharacters(codes, file.iterator());

		// write encoded character
		deleteIfExists(destination);
		writeHeader(header, destination);
		writeBody(binChar, destination);
	}
	
	
	/**
 * Method retrieving codewords in the encoded file.
 *
 * @param header the header
 * @return the hash map
 */
	public static HashMap<String, String> retrieveCodewords(String header) {
		String[] lines = header.split("\n");
		HashMap<String, String> codewords = new HashMap<String, String>();
		
		for (int i = 0; i < lines.length; i++) {
			String[] codes = lines[i].split(",");

			if (codes.length > 1)
				codewords.put(codes[1], codes[0]);
		}
		return codewords;
	}
	
	
	/**
	 * Method that return the encoded header from a file
	 *
	 * @param it the it
	 * @return the string
	 */ 
	public static String retrieveEncodedHeader(FileCharIterator it) {
		String endOfHeader = convertTo8bits(Integer.toBinaryString('\n'));
		StringBuilder header = new StringBuilder("");
		String doubleKey = "";
		String section = "";
		boolean error = true;
		
		endOfHeader += endOfHeader;
		while (it.hasNext()) {
			section = it.next();
			doubleKey += section;
			header.append((char) Integer.parseInt(section, 2));
			
			if (doubleKey.length() == 16) {
				if (doubleKey.equals(endOfHeader)) {
					error = false;
					break;
				}
				doubleKey = doubleKey.substring(8);
			}
		}

		// check if the header is well formated
		if (error == true) {
			System.err.println("Error: bad header format");
			System.exit(0);
		}
		return header.toString();
	}
	
	/**
	 * Method that return the encoded body from a file
	 *
	 * @param it the it
	 * @return the string
	 */
	public static String retrieveEncodedBody(FileCharIterator it) {
		StringBuilder section = new StringBuilder("");

		while (it.hasNext())
			section.append(it.next());
		return section.toString();
	}
	
	/**
	 * Method that search in the codewords hashmap the new compresed binary
	 *
	 * @param code the code
	 * @param codewords the codewords
	 * @return the string
	 */
	public static String searchForCode(String code, HashMap<String, String> codewords) {
		return codewords.containsKey(code) ? codewords.get(code) : null;
	}
	
	/**
	 * Method that write the decoded file
	 *
	 * @param section the section
	 * @param destination the destination
	 */
	public static void writeDecodedFile(StringBuilder section, String destination) {
		while (section.length() > 8) {
    		int readMax = 8 * (section.length() / 8);
    		FileOutputHelper.writeBinStrToFile(section.substring(0, readMax), destination);
    		section.delete(0, readMax);
    	}
    	
    	if (section.length() != 0) {
    		for (int x = section.length(); x != 8; x++)
    			section.append("0");
    		FileOutputHelper.writeBinStrToFile(section.toString(), destination);
    	}
	}
	
	/**
	 * Decode.
	 *
	 * @param target the target
	 * @param destination the destination
	 * @param saveAsDecode the save as decode
	 * @return the string
	 */
	public static String decode(String target, String destination, boolean saveAsDecode) {
		FileCharIterator it = new FileCharIterator(target);
		String header = retrieveEncodedHeader(it);
		String body = retrieveEncodedBody(it);
		HashMap<String, String> codewords = retrieveCodewords(header);		
		String bin = "";
		int i = 0;
		StringBuilder section = new StringBuilder("");
		
		while (body.length() > 1) {
			bin = body.substring(0, i);
			String code = searchForCode(bin, codewords);
			
			if (code != null) {
				if (code.equals("EOF"))
					break;
				section.append(code);
				body = body.substring(i);
				i = 0;
			}
			i++;
		}
		if(saveAsDecode) {
			deleteIfExists(destination);
			writeDecodedFile(section, destination);
		}
		return section.toString();
	}
}
