package com.halim.classifiers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.halim.nlp.classification.MultiClassCorpusOccurenceDoc;

public class NaiveBayesClassifier {
	
	private Map<String,Double> classesProbabilities;
	public NaiveBayesClassifier() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String classify(MultiClassCorpusOccurenceDoc occurence,Map<String,Integer> rq) {
		//Map<String ,Map<String,Integer>> reducedOccMap = new HashMap<>(); //( doc_clas , Map<String,integer>)
		classesProbabilities = new HashMap<String, Double>();
		for(Entry<String, Map<String, Map<String, Integer>>> entry : occurence.getMulticlassOccurenceMap().entrySet()) {
			classesProbabilities.put(entry.getKey(), classProbability(entry.getValue(),rq,(double)1/(occurence.getMulticlassOccurenceMap().keySet().size())));
		}
		 Map<String,Double> orderedMap = classesProbabilities.entrySet().stream()
	       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
	       .collect(Collectors.toMap(
	          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return (String) orderedMap.keySet().toArray()[0];
	}
	
	private Double classProbability(Map<String, Map<String, Integer>> Klass, Map<String,Integer> rq,Double classProb) {
		// for each word in the rq we calculate the probability
		Map<String,Double> wordsProbab = new HashMap<String, Double>();
		// number of words in the class
		 int n = 0;
		for(Entry<String, Map<String, Integer>> entry : Klass.entrySet()) {
			n = n + entry.getValue().size();
		}
	
		// no repeated Words 
		Set<String> noRepeatedWords = new HashSet<>();
		for(Entry<String, Map<String, Integer>> entry : Klass.entrySet()) {
			for(Entry<String, Integer> entry1 : entry.getValue().entrySet()) {
				noRepeatedWords.add(entry1.getKey());
			}
		}
		int m = noRepeatedWords.size();

		for(Entry<String,Integer> word : rq.entrySet()) {
			Double num = 1D ;
			int freq =  0;
			for(Entry<String, Map<String, Integer>> entry : Klass.entrySet()) {
				if(entry.getValue().containsKey(word.getKey())) {
					freq = freq + entry.getValue().get(word.getKey());
				}
				else {
					continue;
				}
			}
			
			num = (double)(1+freq)/(n+m);
			wordsProbab.put(word.getKey(), num);
		}
		//.out.println(wordsProbab.toString());
		// class prob
		Double probability = 1.0D;
		for(Entry<String, Double> entry : wordsProbab.entrySet()) {
			probability = probability * entry.getValue();
		}
		
		return  probability*classProb;
	}
	

}
