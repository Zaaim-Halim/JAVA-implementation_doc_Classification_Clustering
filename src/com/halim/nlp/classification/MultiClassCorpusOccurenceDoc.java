package com.halim.nlp.classification;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.halim.fileUtils.FileUtility;
import com.halim.nlp.AbstractDoc;
import com.halim.nlp.Corpus;
import com.halim.nlp.OccurrenceCorpus;
import com.halim.utils.Language;

public class MultiClassCorpusOccurenceDoc implements Serializable , AbstractDoc {
   private String fileName;
   private Map<String ,Map<String,Map<String,Integer>>> multiclassOccurenceMap;

	private static final long serialVersionUID = 1L;

	
	@SuppressWarnings("unused")
	private MultiClassCorpusOccurenceDoc() {
		
	}

	public MultiClassCorpusOccurenceDoc(String fileName) {
		this.fileName = fileName;
	}
    public void buildMultiClassOccurenceCorpus(String baseDir,Language lang){
    	multiclassOccurenceMap  = new HashMap<>();

    	Set<String> listOfCorpusCalsses = Arrays.asList(new File(baseDir).list())
    			.stream().map(word -> word).collect(Collectors.toSet());
    	for(String dirClass : listOfCorpusCalsses) {
    		Corpus corpus = new Corpus(" "); 
    		corpus.buildCorpus(baseDir +"/"+dirClass);
    		OccurrenceCorpus classOccurenceCorpus = new OccurrenceCorpus(" ");// we can rather load it ready  
    		classOccurenceCorpus.buildOccurenceCorpus(corpus,lang);
    		multiclassOccurenceMap.put(dirClass, classOccurenceCorpus.getOccurenceMap());
    	}
    	
    }
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, Map<String, Map<String, Integer>>> getMulticlassOccurenceMap() {
		return multiclassOccurenceMap;
	}

	public void setMulticlassOccurenceMap(Map<String, Map<String, Map<String, Integer>>> multiclassOccurenceMap) {
		this.multiclassOccurenceMap = multiclassOccurenceMap;
	}

	@Override
	public void save(String dir) {
		FileUtility.serialize(this, dir+"/"+"mclassesOccurenceMap.se");
		
	}

	@Override
	public AbstractDoc load(String fullPath) {
	
		return FileUtility.deserialize(fullPath, this.getClass().getName());
	}

}
