package com.halim;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.halim.classifiers.KNNClassifier;
import com.halim.classifiers.NaiveBayesClassifier;
import com.halim.clustring.CentreMobile;
import com.halim.nlp.Corpus;
import com.halim.nlp.OccurrenceCorpus;
import com.halim.nlp.RequestDoc;
import com.halim.nlp.TfIdfCorpus;
import com.halim.nlp.classification.MultiClassCorpusOccurenceDoc;
import com.halim.utils.Language;

/**
 * @author ZAAIM HALIM
 *
 */
public class Application {
	private static String baseDir = "C:/Users/X1/Desktop/master - S3/text-mining/calssification";
	static Logger log = Logger.getLogger(Application.class.getName());

	public void classification() {
		log.warn("starting the classification ...");

		MultiClassCorpusOccurenceDoc occurence = new MultiClassCorpusOccurenceDoc("multi-occurence.ser");
		occurence.buildMultiClassOccurenceCorpus(baseDir + "/" + "classes", Language.ARABIC);

		RequestDoc rq = new RequestDoc();
		rq.readFromFile(
				"C:\\Users\\X1\\Desktop\\master - S3\\text-mining\\calssification\\test\\حماية المستهلك\\المادة 1.txt",
				Language.ARABIC);
		// rq.readFromFile("C:\\Users\\X1\\Desktop\\master  - S3\\text-mining\\calssification\\test\\تنفيذ الدستور\\الفصل 1.txt", Language.ARABIC);
		// rq.readFromFile("C:\\Users\\X1\\Desktop\\master - S3\\text-mining\\calssification\\test\\القضاء العسكري\\المادة 1.txt", Language.ARABIC);
		String Klass = KNNClassifier.classify(occurence, rq, 3);
		log.info("result of classification KNN = " + Klass);

		NaiveBayesClassifier nb = new NaiveBayesClassifier();
		String className = nb.classify(occurence, rq.getOccurenceMap());
		log.info("result of classification  Naive Bayes = " + className);
	}

	public void clustering() {
		log.info("Starting building  ...");
		Corpus corpus = new Corpus("corpus.ser");
		corpus.buildCorpus("C:/Users/X1/Desktop/master - S3/text-mining/clustring");
		//corpus.buildCorpus("C:/Users/X1/Desktop/master - S3/text-mining/news");
		OccurrenceCorpus occurence = new OccurrenceCorpus("occurence.ser");
		occurence.buildOccurenceCorpus(corpus, Language.ARABIC);
		//occurence.buildOccurenceCorpus(corpus, Language.ENGLISH);
		// HiarchiqueAscendant.doClustring(2, occurence);

		TfIdfCorpus tfIdf = new TfIdfCorpus("tfidf.ser");
		tfIdf.buildTfIdfCorpus(occurence);
		CentreMobile centerMobile = new CentreMobile();
		Map<String, Map<String, Map<String, Float>>> finalClusters = centerMobile.clustring(tfIdf.getTfIdfMap(), 2,
				3500);
		log.info("####################### final clusters #########################");
		for (Entry<String, Map<String, Map<String, Float>>> entry : finalClusters.entrySet()) {
			log.info(entry.getKey());
			log.info(entry.getValue().keySet().toString());
			int mada = howManyDocUnderclass( entry.getValue(),"المادة");
			int fassl = howManyDocUnderclass( entry.getValue(),"الفصل");
			log.info(entry.getKey() + " final size : " + entry.getValue().size() +":  الفصل : "+fassl +"  &  المادة : "+mada);
		}
		log.info("###################################################################");

	}

	public static void main(String[] args) {
		// new Application().classification();
		new Application().clustering();

	}
	
	public static int howManyDocUnderclass(Map<String, Map<String, Float>> map, String klass) {
		int counter = 0;
		for(Entry<String, Map<String, Float>> entry : map.entrySet()) {
			if(entry.getKey().contains(klass))
				counter ++;
			
		}
		return counter;
	}

}
