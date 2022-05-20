package com.halim.nlp;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.halim.fileUtils.FileUtility;

public class Corpus implements Serializable ,AbstractDoc{

	private static final long serialVersionUID = 8948996344476086286L;
	private String corpusName;
	private List<Document> files = new ArrayList<Document>();
    
	public Corpus() {
		super();
	}
	
	public Corpus(String corpusName) {
	
		this.corpusName = corpusName;
	}

	public void buildCorpus(String corpusDir)
    {
    	Map<String, List<String>> rowfilesLines = FileUtility.readDirTxtFiles(corpusDir);
        for(Map.Entry<String, List<String>> entry : rowfilesLines.entrySet()) {
        	Document doc = new Document();
        	doc.setFileName(entry.getKey());
        	doc.setLines(entry.getValue());
        	this.files.add(doc);
        }
       
    }
	public Collection<Document> getFiles() {
		return files;
	}

	
	@Override
   public void save(String dir) {
		FileUtility.serialize(this,dir + File.separator + corpusName);

	}
	@Override
	public Corpus load(String fullPath) {
		
		return (Corpus) FileUtility.deserialize(fullPath, this.getClass().getName());
	}
	

}
