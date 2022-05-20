package com.halim.classifiers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.halim.nlp.RequestDoc;
import com.halim.nlp.classification.Distance;
import com.halim.nlp.classification.MultiClassCorpusOccurenceDoc;

/**
 * @author ZAAIM HALIM
 *
 */
public class KNNClassifier {
	
	public static String classify(MultiClassCorpusOccurenceDoc occurence , RequestDoc rq, int k) {
		Map<String ,Map<String,Float>> distance = Distance.distanceFormAllSlassDocs(occurence, rq.getOccurenceMap());
		Map<String , Float> reducedMap = new HashMap<>();
		for(Entry<String ,Map<String,Float>> entry : distance.entrySet()) {
			for(Entry<String,Float> entry1 : entry.getValue().entrySet()) {
				reducedMap.put(entry1.getKey()+"_"+entry.getKey(), entry1.getValue());
			}
		}
		Map<String,Float> knearest =
				reducedMap.entrySet().stream()
			       .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
			       .limit(k)
			       .collect(Collectors.toMap(
			          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
		// 
		Set<String> Klasses = occurence.getMulticlassOccurenceMap().keySet();
		// finalMap
		Map<String,Float> finalMap = new HashMap<>();
		for(String str : Klasses) {
			Float sum = 0F;
			for(Entry<String,Float> entry : knearest.entrySet()) {
				 
				if(entry.getKey().contains(str)) {
					sum = sum + entry.getValue();
				}
			}
			finalMap.put(str, sum);
		}
		// final order
		Map<String,Float> finall= finalMap.entrySet().stream()
			       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			       .collect(Collectors.toMap(
			          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		Map.Entry<String,Float> entry = finall.entrySet().iterator().next();
		 return entry.getKey();
	}
}
