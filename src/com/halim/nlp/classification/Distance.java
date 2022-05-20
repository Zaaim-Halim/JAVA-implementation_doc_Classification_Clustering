package com.halim.nlp.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Distance {
	
	public static Float euclidienneDistanceBasedOnTfIdf(Map<String,Float> d1, Map<String,Float> d2) {
		int size1 = d1.size();
		int size2 = d2.size();
		List<Float> dd1 = (List<Float>) d1.values();
		List<Float> dd2 = (List<Float>) d2.values();
		Float sum = 0F;
	    if(size1 > size2) {
			for(int i = 0 ; i < size1 ; i++) {
				if(i >= size2)
				{
					sum = (float) (sum + Math.pow(dd1.get(i)-0 ,2));
				}else
				{
					sum = (float) (sum + Math.pow(dd1.get(i) - dd2.get(i),2));
				}
			}
		}else {
			for(int i = 0 ; i < size2 ; i++) {
				if(i >= size1)
				{
					sum = (float) (sum + Math.pow(dd2.get(i)-0 ,2));
				}else
				{
					sum = (float) (sum + Math.pow(dd1.get(i) - dd2.get(i),2));
				}
			}
		}
		return (float) Math.sqrt(sum);
	}
	
	public static Float euclidienneDistanceBasedOnOccurence(Map<String,Integer> d1, Map<String,Integer> d2) {
		int size1 = d1.size();
		int size2 = d2.size();
		List<Integer> dd1 = new ArrayList<Integer>(d1.values());
		List<Integer> dd2 = new ArrayList<Integer>(d2.values());
		Float sum = 0F;
	    if(size1 > size2) {
			for(int i = 0 ; i < size1 ; i++) {
				if(i >= size2)
				{
					sum = (float) (sum + Math.pow(dd1.get(i)-0 ,2));
				}else
				{
					sum = (float) (sum + Math.pow(dd1.get(i) - dd2.get(i),2));
				}
				
			}
		}else {
			for(int i = 0 ; i < size2 ; i++) {
				if(i >= size1)
				{
					sum = (float) (sum + Math.pow(dd2.get(i)-0 ,2));
				}else
				{
					sum = (float) (sum + Math.pow(dd1.get(i) - dd2.get(i),2));
				}
				
			}
			
		}
		return (float) Math.sqrt(sum);
	}
	public static Float euclidienneDistance(List<Double> d1, Map<String,Float> d2 ) {
		
		int size1 = d1.size();
		int size2 = d2.size();
		List<Double> dd1 = d1;
		List<Float> dd2 = new ArrayList<Float>(d2.values());
		Float sum = 0F;
	    if(size1 > size2) {
			for(int i = 0 ; i < size1 ; i++) {
				if(i >= size2)
				{
					sum = (float) (sum + Math.pow(dd1.get(i)-0 ,2));
				}else
				{
					sum = (float) (sum + Math.pow(dd1.get(i) - dd2.get(i),2));
				}
				
			}
		}else {
			for(int i = 0 ; i < size2 ; i++) {
				if(i >= size1)
				{
					sum = (float) (sum + Math.pow(dd2.get(i)-0 ,2));
				}else
				{
					sum = (float) (sum + Math.pow(dd1.get(i) - dd2.get(i),2));
				}
				
			}
			
		}
		return (float) Math.sqrt(sum);
	}
	
	public static Map<String ,Map<String,Float>> distanceFormAllSlassDocs(MultiClassCorpusOccurenceDoc occurence, Map<String,Integer> rq){
		Map<String ,Map<String,Float>> distanceMap = new HashMap<>(); 
		for(Entry<String, Map<String, Map<String, Integer>>> entry : occurence.getMulticlassOccurenceMap().entrySet()) {
			// iteration over each class 
			String classMame = entry.getKey();entry.getValue();
			Map<String,Float> docsMap = new HashMap<>();
			for(Entry<String, Map<String, Integer>> entry1 : entry.getValue().entrySet()) {
				// iteration over Docs of class
				Float distance = euclidienneDistanceBasedOnOccurence(entry1.getValue(),rq );
				docsMap.put(entry1.getKey(), distance);
			}
			distanceMap.put(classMame, docsMap);
		}
		return distanceMap;
	}

}
