package com.halim.nlp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.halim.fileUtils.FileUtility;
import com.halim.utils.Language;

public class RequestDoc {

	private Map<String, Float> tfMap;
	private Map<String, Integer> occurenceMap;
	private Map<String,Float> tfIdfMap;

	public RequestDoc() {
		super();
	}
    public void readFromFile(String path ,Language lang){
    	List<String> lines = FileUtility.readTxtFile(path);
    	String[] array = new String[lines.size()];
    	buildRequestDoc(lang,lines.toArray(array));
    }
	public void buildRequestDoc(Language lang,String ...lines) {
		TfIdfCorpus c = new TfIdfCorpus(null);
		occurenceMap = OccurrenceCorpus.buildOccurenceOfDoc(Arrays.asList(lines), lang);
		this.tfMap = c.calculateTfOfDoc(OccurrenceCorpus.buildOccurenceOfDoc(Arrays.asList(lines), lang));
	}
    public void buildRequestTFIDF(OccurrenceCorpus occurence) {
    	int corpussize = occurence.getOccurenceMap().size() + 1 ;
    	for(Entry<String, Float> request : tfMap.entrySet()) {
    		int counter = 0;
    		for(Entry<String, Map<String,Integer>> entry: occurence.getOccurenceMap().entrySet()) {
        		if(entry.getValue().containsKey(request.getKey())){
        			counter ++;
        		}
        	 float idf = (float) Math.log(corpussize/counter);
        	 tfIdfMap.put(request.getKey(), idf*request.getValue());
        	}
    	}
    	
    }
	public Map<String, Float> getTfMap() {
		return tfMap;
	}

	public void setTfMap(Map<String, Float> tfMap) {
		this.tfMap = tfMap;
	}

	public Map<String, Float> getTfIdfMap() {
		return tfIdfMap;
	}

	public void setTfIdfMap(Map<String, Float> tfIdfMap) {
		this.tfIdfMap = tfIdfMap;
	}

	public Map<String, Integer> getOccurenceMap() {
		return occurenceMap;
	}

	public void setOccurenceMap(Map<String, Integer> occurenceMap) {
		this.occurenceMap = occurenceMap;
	}
	

}
