package org.zzl.choltfo.IRCBot;

import java.io.IOException;
import java.util.HashMap;

public class LindTranslator {
    public String translate(String input) throws IOException {
    	HashMap<String, String> Dictionary = new HashMap<String, String>();
    	Dictionary.put("claw", "chela");
    	Dictionary.put("yes", "ceja");
    	Dictionary.put("hello", "ctrath");
    	Dictionary.put("hey", "ctrath");
    	Dictionary.put("hi", "ctrath");
    	Dictionary.put("yo", "ctrath");
    	Dictionary.put("den", "daga");
    	Dictionary.put("home", "domta");
    	Dictionary.put("12", "duntanvad");
    	Dictionary.put("elders", "eldas");
    	Dictionary.put("elder", "elda");
    	Dictionary.put("mate", "eln");
    	Dictionary.put("hurt", "gin");
    	Dictionary.put("council", "gtratha");
    	Dictionary.put("leader", "grathlin");
    	Dictionary.put("eat", "jeza");
    	Dictionary.put("ate", "jeza");
    	Dictionary.put("woods", "lian");
    	Dictionary.put("forest", "lian");
    	Dictionary.put("3", "lok");
    	Dictionary.put("youth", "itscta");
    	Dictionary.put("child", "itscta");
    	Dictionary.put("children", "itscta");
    	Dictionary.put("kid", "itscta");
    	Dictionary.put("kids", "itsctas");
    	Dictionary.put("ride", "ptatch");
    	Dictionary.put("rides", "ptatch");
    	Dictionary.put("pack", "rtath");
    	Dictionary.put("pack-mates", "rtathen");
    	Dictionary.put("packmates", "rtathen");
    	Dictionary.put("hero", "ruza");
    	Dictionary.put("many", "sataldrn");
    	Dictionary.put("commander", "susa");
    	Dictionary.put("2", "vad");
    	Dictionary.put("cavalry", "vada");
    	Dictionary.put("pair", "vadeln");
    	Dictionary.put("murder", "volat");
    	Dictionary.put("died", "vola");
    	Dictionary.put("killed", "vola");
    	Dictionary.put("shot", "vola");
    	Dictionary.put("stab", "vola");
    	Dictionary.put("stabbed", "vola");
    	Dictionary.put("idiot", "vuz");
    	Dictionary.put("the", "ni");
    	Dictionary.put("1", "vok");
    	Dictionary.put("a", "vok");
    	Dictionary.put("an", "vok");
    	Dictionary.put("those", "pet");
    	Dictionary.put("that", "");
    	
   		String output = "";
   		String inputWords[] = input.split(" ");
   		int wordNumber = 0;
       	HashMap<Integer, String> outputWords = new HashMap<Integer, String>();
   		for(String word : inputWords){										//for every word...  
   			if(word.contains("'")){											//check for '
   				if(word.split("'").length > 1){								//if the word has 2 pieces afterwards
   					String wordPart1 = word.split("'")[0];
   					String wordPart2 = word.split("'")[1];					//record part 2 of the word
   					if (Dictionary.containsKey(wordPart1.toLowerCase())) {	//if the dictionary has the word in it
   						word = (Dictionary.get(wordPart1.toLowerCase()) + "'" + wordPart2);	//add the translation, the
   					}														//Apostrophe, and the suffix.
   				}
   			}
   			if (Dictionary.containsKey(word.toLowerCase())) {
       			outputWords.put(wordNumber, Dictionary.get(word.toLowerCase()));
   			} else {
       			outputWords.put(wordNumber, word.toLowerCase());
   			}
   			wordNumber++;
   		}
   		for(int i = 0; i < outputWords.size(); i++){						//compile separate output words into
   			if (i == 0) {													//single string.
   				String Word = outputWords.get(i);
   				char wordChars[] = Word.toCharArray();
   				wordChars[0] = Character.toUpperCase(wordChars[0]);
   				outputWords.put(i, String.copyValueOf(wordChars));
   			}
   			if (i != 0) {													
   				output = (output + " ");									//Insert periods and CaPiTaLs
   			}
   			output = (output + outputWords.get(i));
   			if (i == outputWords.size()-1) {
   				output = (output + ".");
   			}
   		}
   		//System.out.println(outputWords.toString());
   		return output;
    	}
}