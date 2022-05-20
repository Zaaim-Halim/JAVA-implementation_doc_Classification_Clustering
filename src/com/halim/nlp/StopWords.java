package com.halim.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.halim.fileUtils.FileUtility;
import com.halim.utils.Language;

public class StopWords {
	
	public static List<String> removeStopWords(List<String> docWords, Language lang){
		if(lang == Language.ARABIC) {
			List<String> stopWs = arabicStopWords();
			List<String> docWordsWithStopWordsRemoved = new ArrayList<>();
			for(String word : docWords) {
				if(!isStopword(stopWs, word.trim())) {
					docWordsWithStopWordsRemoved.add(word.trim());
				}
			}
			return docWordsWithStopWordsRemoved;
		}
		Set<String> stopWordLines = FileUtility.loadStopWordFile(lang);
		List<String> stopWords = new ArrayList<String>();
		for(String line : stopWordLines) {
			Set<String > words = Arrays.asList(line.split(" ")).stream().filter(word -> word.trim().length() != 0)
					.collect(Collectors.toSet());
			stopWords.addAll(words);
		}
		return docWords.stream().filter(word -> ! isStopword(stopWords, word))
				.collect(Collectors.toList());
	}
	
	private static boolean isStopword(List<String> docWords , String word) {
		
		return docWords.contains(word);
	}
	private static List<String> arabicStopWords(){
		String stopWords = " بعد ضد  يلي الى في من حتى وهو يكون به وليس أحد على وكان تلك كذلك التي وبين فيها عليها إن وعلى لكن "
				+ "عن مساء ليس منذ الذي أما حين ومن لا ليسب وكانت أي ما عنه حول دون مع لكنه ولكن له هذا والتي فقط "
				+ "ثم هذه أنه تكون قد بين جدا لن نحو كان لهم لأن اليوم لم هؤلاء فإن فيه ذلك لو عند اللذين كل بد لدى وثي أن"
				+ "ومع فقد بل هو عنها منه بها وفي فهو تحت لها أو إذ علي عليه كما كيف هنا وقد كانت لذلك أمام هناك قبل معه "
				+ "يوم منها إلى إذا هل حيث هي اذا او و ما لا الي إلي مازال لازال لايزال مايزال اصبح أصبح أمسى امسى أضحى "
				+ "اضحى ظل مابرح مافتئ ماانفك بات صار ليس إن كأن ليت لعل لاسيما ولايزال الحالي ضمن اول وله ذات اي بدلا اليها"
				+ " انه الذين فانه وان والذي وهذا لهذا الا فكان ستكون مما منهم  وذلك أن"
				+ " أبو بإن الذي اليه يمكن بهذا لدي  وأن  وهي وأبو آل الذي هن الذى إما وإما";
		return Arrays.asList(stopWords.split(" ")).stream().map(word->word.trim()).filter(word -> word.length() !=0)
				
				.collect(Collectors.toList());
	}


}
