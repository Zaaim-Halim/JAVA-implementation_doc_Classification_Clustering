package com.halim.nlp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.halim.fileUtils.FileUtility;

public class TfIdfCorpus implements Serializable , AbstractDoc{

	
	private static final long serialVersionUID = 3545721367095843938L;
	private String fileName;
	private Map<String,Map<String,Float>> tfIdfMap;
	

	public TfIdfCorpus(String fileName) {
		this.fileName = fileName;
	}
    
	public void buildTfIdfCorpus(OccurrenceCorpus occurenceCorpus) {
	    tfIdfMap = new HashMap<String, Map<String,Float>>();
		Map<String,Float> termfrequency = null;
		Map<String,Float> inverseTermfrequency = inverseTermfrequency(occurenceCorpus);;
		for(Map.Entry<String, Map<String , Integer>> entry : occurenceCorpus.getOccurenceMap().entrySet()) {
			Map<String,Float> tfIdf = new HashMap<>();
			termfrequency = calculateTfOfDoc(entry.getValue());
			for(Map.Entry<String, Float> entry1 : termfrequency.entrySet()) {
				tfIdf.put(entry1.getKey(), inverseTermfrequency.get(entry1.getKey()) 
						* entry1.getValue());
			}
			this.tfIdfMap.put(entry.getKey(), tfIdf);
		}
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, Map<String, Float>> getTfIdfMap() {
		return tfIdfMap;
	}

	@Override
	public void save(String dir) {
		FileUtility.serialize(this, dir +"/"+ this.fileName);
		
	}

	@Override
	public AbstractDoc load(String fullPath) {
		
		return FileUtility.deserialize(fullPath, this.getClass().getName());
	}
	
	public  Map<String,Float> calculateTfOfDoc(Map<String,Integer> docOccurenceMap) {
		Map<String,Float> termfrequency = new HashMap<>();
		Float tf = null;
		int docSize = docOccurenceMap.size();
		for(Map.Entry<String, Integer> entry : docOccurenceMap.entrySet()) {
			tf = (float)entry.getValue() / docSize;
			termfrequency.put(entry.getKey(), tf);
			
		}
		return termfrequency;
	}
	private Map<String,Float> inverseTermfrequency(OccurrenceCorpus occurenceCorpus) {
		
		Map<String,Float> inverseTermfrequency = new HashMap<>();
		float idf = 0;
		int numberOfDocInCorpus = occurenceCorpus.getOccurenceMap().size();
		int n ;
		for(Map.Entry<String, Map<String , Integer>> entry : occurenceCorpus.getOccurenceMap().entrySet()) {
			for(Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()) {
				n = termOccrenceInCorpus(occurenceCorpus,entry2.getKey());
				idf = (float) Math.log(numberOfDocInCorpus/ n);
				inverseTermfrequency.put(entry2.getKey(), idf);
			}
		}
		return inverseTermfrequency;
	}
	
	private  boolean  isTermExistInDoc(Map<String,Integer> docOccurenceMap,String term) {
		return docOccurenceMap.containsKey(term);
	}
	
	private  int termOccrenceInCorpus(OccurrenceCorpus occurenceCorpus,String term) {
		int counter = 0;
		for(Map.Entry<String, Map<String , Integer>> entry : occurenceCorpus.getOccurenceMap().entrySet()) {
			if(isTermExistInDoc(entry.getValue(),term)) {
				counter++;
			
			}
		}
		return counter;
	}
	
	

}
