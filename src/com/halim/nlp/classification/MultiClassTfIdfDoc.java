package com.halim.nlp.classification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.halim.fileUtils.FileUtility;
import com.halim.nlp.AbstractDoc;
import com.halim.nlp.OccurrenceCorpus;
import com.halim.nlp.TfIdfCorpus;

public class MultiClassTfIdfDoc implements Serializable, AbstractDoc {

   
	private static final long serialVersionUID = 1L;
    private String fileName;
    private Map<String ,Map<String,Map<String,Float>>> multiClassTfIdf ;
	
    @SuppressWarnings("unused")
	private MultiClassTfIdfDoc() {
		super();
	}
    
    public void buildMultiClassTfIdfDoc(MultiClassCorpusOccurenceDoc multiClassCorpusOccurenceDoc) {
    	multiClassTfIdf = new HashMap<>();
    	OccurrenceCorpus occDoc = new OccurrenceCorpus(" ") ;
    	TfIdfCorpus tfIdfCorpus = new TfIdfCorpus(" ");
    	for(Map.Entry<String, Map<String,Map<String,Integer>>> entry : multiClassCorpusOccurenceDoc.getMulticlassOccurenceMap().entrySet()) {
    		occDoc.setOccurenceMap(entry.getValue());
    		tfIdfCorpus.buildTfIdfCorpus(occDoc);
    		multiClassTfIdf.put(entry.getKey(), tfIdfCorpus.getTfIdfMap());
    	}
    }
    public void preformChi2TestFilter() {
    	
    }
	public MultiClassTfIdfDoc(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void save(String dir) {
		FileUtility.serialize(this,dir+"/"+"multiClassTfIdf.ser");
		
	}

	@Override
	public AbstractDoc load(String fullPath) {
	
		return FileUtility.deserialize(fullPath, this.getClass().getName());
	}

}
