package com.halim.nlp;

public class FrenshStemmer implements IStemmer{
	
	public static org.tartarus.snowball.ext.FrenchStemmer frStemmer =
			new org.tartarus.snowball.ext.FrenchStemmer();
	
	@Override
	public  String stemWord(String word) {
		frStemmer.setCurrent(word);
		frStemmer.stem();
		return frStemmer.getCurrent();
	}

}
