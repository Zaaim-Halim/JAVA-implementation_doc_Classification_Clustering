package com.halim.nlp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.halim.fileUtils.FileUtility;
import com.halim.utils.Language;
import com.halim.utils.StringUtils;

import safar.basic.morphology.stemmer.factory.StemmerFactory;
import safar.basic.morphology.stemmer.model.WordStemmerAnalysis;
import safar.util.remover.Remover;

public class OccurrenceCorpus implements Serializable, AbstractDoc {
    private static IStemmer stemmer = null;
	private static final long serialVersionUID = 1398130412709291574L;
    private String fileName;
	private Map<String,Map<String,Integer>> occurenceMap;
	
	
	public OccurrenceCorpus(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	public void buildOccurenceCorpus(Corpus corpus ,Language lang ) {
		occurenceMap = new HashMap<>();
	
		for(Document doc : corpus.getFiles()) {
			
			Map<String,Integer> occurenceDoc = buildOccurenceOfDoc(doc.getLines(),lang);
			occurenceMap.put(doc.getFileName(), occurenceDoc);
		}
			
	}
    
	public static Map<String,Integer> buildOccurenceOfDoc(List<String> lines,Language lang) {
	   
		List<String> tokenizedLines = new ArrayList<>();
		List<String>  tokenizedLinesAfterRemovingStopWords = new ArrayList<>();
		Map<String,Integer> occurenceMap = new HashMap<>();
		for(String line : lines) {
			tokenizedLines.addAll(StringUtils.toCleanList(line));
		}
		if(lang != Language.ARABIC) {
			tokenizedLinesAfterRemovingStopWords = StopWords.removeStopWords(tokenizedLines,lang);
			
			//============= apply stemming  ===========
			tokenizedLinesAfterRemovingStopWords = stemDoc(tokenizedLinesAfterRemovingStopWords,lang);
			
		}
		
		else {
			
			String str = String.join(" ",tokenizedLines);
			String stemmedString = stemDoc(str);
			tokenizedLines.clear();
			tokenizedLinesAfterRemovingStopWords = Arrays.asList(stemmedString.split(" "));
			
		}
		
		Set<String> unique =  new HashSet<>(tokenizedLinesAfterRemovingStopWords);
		for(String key : unique ) {
			occurenceMap.put(key, Collections.frequency(tokenizedLinesAfterRemovingStopWords, key));
		}
		
		return occurenceMap;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, Map<String, Integer>> getOccurenceMap() {
		return occurenceMap;
	}

	public void setOccurenceMap(Map<String, Map<String, Integer>> occurenceMap) {
		this.occurenceMap = occurenceMap;
	}

	@Override
	public void save(String dir) {
	
		FileUtility.serialize(this, dir +"/"+ fileName);
		
	}

	@Override
	public AbstractDoc load(String fullPath) {
		
		return FileUtility.deserialize(fullPath, this.getClass().getName());
	}
	
	public Map<String,Integer> calculateWordsOccurenceInDoc(Document doc){
		return null;
	}
	
	
	private static List<String> stemDoc(List<String> docWords,Language lang)
	{
		
		if(stemmer == null) {
		switch(lang) {
		case ARABIC:
			stemmer = new ArabicStemmer();
			break;
		case FRENSH:
			stemmer = new FrenshStemmer();
			break;
		case ENGLISH:
			stemmer = new EnglishStemmer();
			break;
		
		default:
			System.err.println("Usopporded Language !");
		}
		}
	
		return docWords.stream().map(word -> stemmer.stemWord(word)).collect(Collectors.toList());
		
	}
	public static String stemDoc(String rq) {
		@SuppressWarnings("unused")
		String stemRequette = "";
		String textWithoutSW = Remover.removeStopWords(rq);
		 safar.basic.morphology.stemmer.interfaces.IStemmer stemmer = StemmerFactory.getLight10Implementation();
		List<WordStemmerAnalysis> listResult = stemmer.stem(textWithoutSW);

		for (WordStemmerAnalysis wsa : listResult) {
			String stem = wsa.getListStemmerAnalysis().get(0).getMorpheme();
			stemRequette += " " + stem;

		}
		return stemRequette;

	}
	
}
