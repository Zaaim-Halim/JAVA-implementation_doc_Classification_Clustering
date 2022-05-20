package com.halim.nlp;

import java.util.Map;
import java.util.Map.Entry;

public class Chi2 {
	
	public static Map<String ,Map<String,Map<String,Integer>>> filter(Map<String ,Map<String,Map<String,Integer>>> ocurrenceMap,float threshold){
		
		int nbTotal = 0;
		for(Entry<String ,Map<String,Map<String,Integer>>> entry : ocurrenceMap.entrySet()) {
			nbTotal = nbTotal + entry.getValue().size();
		}
		
		return null;
	}

}
