package com.halim.fileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.halim.nlp.AbstractDoc;
import com.halim.nlp.Corpus;
import com.halim.nlp.Document;
import com.halim.nlp.IndexDoc;
import com.halim.nlp.OccurrenceCorpus;
import com.halim.nlp.TfIdfCorpus;
import com.halim.nlp.classification.MultiClassCorpusOccurenceDoc;
import com.halim.nlp.classification.MultiClassTfIdfDoc;
import com.halim.utils.Language;

/**
 * @author zaaim halim
 *
 */

public class FileUtility {
	private static FileInputStream fileIs = null;
	private static FileOutputStream fileOs = null;

	private static BufferedInputStream bufferedReader = null;
	private static ObjectInputStream objectIs = null;
	private static ObjectOutputStream objectOs = null;

	/**
	 * <p>
	 * reads a file to a list of Lines ( Strings)
	 * </p>
	 * 
	 * @return List<String>
	 *
	 */
	public static List<String> readTxtFile(String path) {
		List<String> lines = new ArrayList<String>();
		String line = null;

		if (!Files.exists(Path.of(path))) {
			System.err.print("file does not exist !");
			System.exit(1);
		}

		try {
			fileIs = new FileInputStream(path);
			bufferedReader = new BufferedInputStream(fileIs);
			BufferedReader reader = new BufferedReader(new InputStreamReader(bufferedReader, StandardCharsets.UTF_8));
			try {
				while ((line = reader.readLine()) != null) {
					lines.add(line);
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} finally {
			if (fileIs != null && bufferedReader != null) {
				try {
					fileIs.close();
					bufferedReader.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

		return lines;
	}

	/**
	 * 
	 * @param dir :
	 *            <p>
	 *            base dir that contains all documents
	 *            </p>
	 * @return a map that contains file name as key and a collection of file lines
	 */
	public static Map<String, List<String>> readDirTxtFiles(String dir) {
		if (!Files.exists(Path.of(dir)) || !Files.isDirectory(Path.of(dir))) {
			System.err.print("directory does not exist !");
			System.exit(1);
		}
		Map<String, List<String>> files = new HashMap<String, List<String>>();
		Set<String> fileNames = listFilesOfDir(dir);
		

		for (String name : fileNames) {
			files.put(name, readTxtFile(dir + File.separator + name));
		}
  
		return files;
	}

	/**
	 * @param dir
	 * @return set of files name in the current <B>dir</B>
	 */
	private static Set<String> listFilesOfDir(String dir) {
		return Stream.of(new File(dir).listFiles()).filter(file -> !file.isDirectory()).map(File::getName)
				.collect(Collectors.toSet());
	}

	/**
	 * 
	 * @param doc      :
	 *                 <p>
	 *                 the Object to serialize
	 *                 </p>
	 * @param fullPath :
	 *                 <p>
	 *                 the path of the file to serialize
	 *                 </p>
	 */
	public static void serialize(AbstractDoc doc, String fullPath) {

		try {
			objectOs = new ObjectOutputStream(fileOs);
			fileOs = new FileOutputStream(new File(fullPath));
			objectOs.writeObject(doc);
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				fileOs.close();
				objectOs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 
	 * @param fullPath:
	 *                  <p>
	 *                  path to file to serialize
	 *                  </p>
	 * @param c         : class
	 * @return <b>doc</b> : deserialized object
	 */
	public static AbstractDoc deserialize(String fullPath, String className) {
		AbstractDoc doc = null;
		try {
			fileIs = new FileInputStream(new File(fullPath));
			objectIs = new ObjectInputStream(fileIs);
			if (className.equals(Corpus.class.getName())) {
				doc = (Corpus) objectIs.readObject();
			} else if (className.equals(Document.class.getName())) {
				doc = (Document) objectIs.readObject();
			} else if (className.equals(OccurrenceCorpus.class.getName())) {
				doc = (OccurrenceCorpus) objectIs.readObject();
			} else if (className.equals(TfIdfCorpus.class.getName())) {
				doc = (TfIdfCorpus) objectIs.readObject();
			} else if (className.equals(IndexDoc.class.getName())) {
				doc = (IndexDoc) objectIs.readObject();
			} else if (className.equals(MultiClassCorpusOccurenceDoc.class.getName())) {
				doc = (MultiClassCorpusOccurenceDoc) objectIs.readObject();
			} else if (className.equals(MultiClassTfIdfDoc.class.getName())) {
				doc = (MultiClassTfIdfDoc) objectIs.readObject();
			} else {
				System.err.println("Not supported class");
			}
		} catch (FileNotFoundException e) {
			System.err.println("file not fount : " + fullPath);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fileIs.close();
				objectIs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc;

	}

	public static Set<String> loadStopWordFile(Language lang) {
		Set<String> stopWords = new HashSet<>();
		List<String> lines = null;
		switch (lang) {
		case ARABIC:
			lines = (List<String>) readTxtFile("resources/arabic.txt");
			break;
		case FRENSH:
			lines = (List<String>) readTxtFile("resources/frensh.txt");
			break;
		case ENGLISH:
			lines = (List<String>) readTxtFile("resources/english.txt");
			break;

		default:
			System.err.println("Usopporded Language !");
		}
		List<String> tokenizedLine = null;
		for (String line : lines) {
			tokenizedLine = Arrays.asList(line.trim().split(" "));
			tokenizedLine.stream().map(word -> word.trim());
			stopWords.addAll(tokenizedLine);
		}

		return stopWords;
	}

	@SuppressWarnings("deprecation")
	public static <T> T getInstance(Class<T> type) {
		Object o = null;
		T t = null;
		try {
			o = type.newInstance();
			t = type.cast(o);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return t;
	}

}
