package com.halim.nlp;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.halim.fileUtils.FileUtility;

public class Document implements Serializable , AbstractDoc{

	private static final long serialVersionUID = -630695673643668710L;
	private String fileName;
	
	private List<String> lines;

	public Document() {
		super();
	}

	public Document(String fileName) {
		this.fileName = fileName;
	}

	public void buildDocument(String baseDir) {
     this.lines =  FileUtility.readTxtFile(baseDir + File.separator+ fileName);
	}
    
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}
    
	@Override
	public void save(String dir) {
		FileUtility.serialize(this, dir + File.separator + fileName);
		
	}

	@Override
	public AbstractDoc load(String fullPath) {
	
		return FileUtility.deserialize(fullPath, this.getClass().getName());
	}

	@Override
	public String toString() {
		return "Document [fileName=" + fileName + ", lines=" + lines + "]";
	}
	

   
}
