/*
 *  @Auther Varshesh Patel
 */
package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import textprocessing.In;

/**
 * The Class IO.
 */
public class IO {
	
	/**
	 * Out.
	 *
	 * @param s the s
	 */
	public static void out(String s) {
		System.out.print(s);
	}

	/**
	 * Outn.
	 *
	 * @param s the s
	 */
	public static void outn(String s) {
		System.out.println(s);
	}

	/**
	 * Write text file.
	 *
	 * @param lines the lines
	 * @param fileName the file name
	 */
	public static void writeTextFile(String lines, String fileName) {
		try {
			FileWriter fw = new FileWriter(new File(fileName));
			fw.write(lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read text file.
	 *
	 * @param filePath the file path
	 * @return the string
	 */
	public static String readTextFile(String filePath) {

		String docText = "";
		try {
			In fileIn = new In(filePath);
			while (!fileIn.isEmpty()) {
				docText += fileIn.readLine() + "\n";
			}
			return docText;
		} catch (Exception e) {
			System.out.println(e);
		}
		return docText;
	}

	/**
	 * Write object to file.
	 *
	 * @param filepath the filepath
	 * @param serObj the ser obj
	 */
	public static void WriteObjectToFile(String filepath, Object serObj) {

		try {
			FileOutputStream fileOut = new FileOutputStream(filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(serObj);
			objectOut.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Read object from file.
	 *
	 * @param filepath the filepath
	 * @return the object
	 */
	public static Object ReadObjectFromFile(String filepath) {

		try {

			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			Object obj = objectIn.readObject();
			// System.out.println("The Object has been read from the file");
			objectIn.close();
			return obj;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Check file exist.
	 *
	 * @param filePath the file path
	 * @return true, if successful
	 */
	public static boolean checkFileExist(String filePath) {
		File f = new File(filePath);
		if (f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}
}
