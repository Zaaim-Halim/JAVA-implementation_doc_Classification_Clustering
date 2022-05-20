package com.halim.utils;

import java.util.Arrays;
import java.util.List;

public class StringUtils {
	//private static Pattern regex = null;
	
	public static String removePunctuation(String str) {
		return str.replaceAll("\\p{Punct}", "").trim();
	}
	
	public static List<String> toCleanList(String str){
		return Arrays.asList(clean(str).split(" "));
	}
	
	public static String removeDigits(String str) {
		return str.replaceAll("[0-9]","");
	}
    
	public static String removeMoreUnwantedChars(String str) {
		str = str.replaceAll("[^\\p{InArabic}\\s]", "");
		str = str.replaceAll("", "");
		str = str.replaceAll("،", "");
		str = str.replaceAll("؛", "");
		str = str.replaceAll("“", "");
		str = str.replaceAll("”", "");
		str = str.replaceAll(",", "");
		return str;
	}
	
	public static String clean(String str) {
		str = removePunctuation(str.toLowerCase());
		str = removeDigits(str);
		str = removeMoreUnwantedChars(str);
		return str;
	}
	
	
}
