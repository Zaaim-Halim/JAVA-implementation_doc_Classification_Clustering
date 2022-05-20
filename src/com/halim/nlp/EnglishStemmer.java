package com.halim.nlp;

public class EnglishStemmer implements IStemmer {
	
	public static org.tartarus.snowball.ext.EnglishStemmer enStemmer =
			new org.tartarus.snowball.ext.EnglishStemmer();
	
	@Override
	public  String stemWord(String word) {
		enStemmer.setCurrent(word);
		enStemmer.stem();
		return enStemmer.getCurrent();
	}

	

}
