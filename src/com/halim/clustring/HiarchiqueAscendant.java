package com.halim.clustring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Vector;

import com.halim.nlp.OccurrenceCorpus;
import com.halim.nlp.classification.Distance;

public class HiarchiqueAscendant {
	
	
	public static Map<String,List<String>> doClustring(int k, OccurrenceCorpus occurence ) {
		// key : className , value : list of files belonging to
		// the class 
		Map<String,List<String>> cluster = new HashMap<>();
		for(int i = 1;i<=k; i++ ) {
			cluster.put(i+"", new ArrayList<String>());
		}
		int nbClass = occurence.getOccurenceMap().size();
		int t = 0 ;
		Vector<String> classes = new Vector<>(nbClass);

		Vector<String> classesTemp = new Vector<>();
	
		for(Entry<String, Map<String , Integer>> entry : occurence.getOccurenceMap().entrySet()) {
			classes.add(entry.getKey());
			
		}
		while(nbClass > k) {
		
			String closestClasses = getClosetClasses(occurence.getOccurenceMap(),classesTemp);
			
			if(closestClasses != null) {
				classesTemp.add(closestClasses.split("_")[1]);
				
				classes.remove(closestClasses.split("_")[1]);
				int index = 0;
				for(int i= 0; i< classes.size();i++) {
					if(classes.get(i).contains(closestClasses.split("_")[0])) {
						index = i;
						break;
					}
				}
				classes.set(index, classes.get(index)+"_"+closestClasses.split("_")[1]);
			}
			
			nbClass--;	
		}
		System.out.println("FINAL SIZE : "+classes.size());
		System.out.println(classes.toString());
		return null;
	}

	private static String getClosetClasses(Map<String, Map<String, Integer>> occurenceMap,Vector<String> classesTemp) {
		for(String str : classesTemp) {
			occurenceMap.remove(str);
		}
		
		Map<String, Float> distances = new HashMap<>();
		for(Entry<String, Map<String, Integer>> entry : occurenceMap.entrySet()) {
			for(Entry<String, Map<String, Integer>> entry1 : occurenceMap.entrySet()) {
				if(entry.getKey().equals(entry1.getKey()))
					continue;
				Float distance = Distance.euclidienneDistanceBasedOnOccurence(entry.getValue(), entry1.getValue());
				if(!distances.containsKey(entry1.getKey()+"_"+entry.getKey()))
					distances.put(entry.getKey()+"_"+entry1.getKey(), distance);
			}
		}
		
		Map<String, Float> sorted = distances.entrySet().stream()
	       .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
	       .collect(Collectors.toMap(
	          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	   
		Map.Entry<String,Float> entry = sorted.entrySet().iterator().next();
		 return entry.getKey();
	}
	
	

}
