package com.halim.nlp;

import java.io.Serializable;

import com.halim.fileUtils.FileUtility;
import com.halim.utils.Language;

public class IndexDoc implements Serializable , AbstractDoc{


	private static final long serialVersionUID = -1186880934560544861L;
    private String fileName;
    private TfIdfCorpus tfIdfCorpus ;
	
   	public IndexDoc(String fileName, TfIdfCorpus tfIdfCorpus) {
		super();
		this.fileName = fileName;
		this.tfIdfCorpus = tfIdfCorpus;
	}
   	
   
   	public IndexDoc(String fileName) {
		super();
		this.fileName = fileName;
	}


	public void buildIndexOfCorpusFromExistingFiles(String baseDir,Language lang) {
   		Corpus corpus = new Corpus();
   		corpus.buildCorpus(baseDir);
   		OccurrenceCorpus occurence = new OccurrenceCorpus(lang.toString()+".ser");
   		occurence.buildOccurenceCorpus(corpus, lang);
   		occurence.save(lang.toString()+"occurence.ser");
   		tfIdfCorpus = new TfIdfCorpus(lang.toString()+"ftIdf.ser");
   		tfIdfCorpus.buildTfIdfCorpus(occurence);
   		tfIdfCorpus.save(baseDir+lang.toString()+tfIdfCorpus.getFileName());
   		
   		// save the index for later use
   		this.save(baseDir);
   		
   	}
   	
	public TfIdfCorpus getTfIdfCorpus() {
		return tfIdfCorpus;
	}

	public void setTfIdfCorpus(TfIdfCorpus tfIdfCorpus) {
		this.tfIdfCorpus = tfIdfCorpus;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void save(String dir) {
		FileUtility.serialize(this, dir +"/" + fileName);
		
	}

	@Override
	public AbstractDoc load(String fullPath) {
		return FileUtility.deserialize(fullPath, this.getClass().getName());
	}

}
