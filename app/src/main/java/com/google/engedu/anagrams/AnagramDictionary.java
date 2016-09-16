package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 2;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private int wordLength = DEFAULT_WORD_LENGTH;
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet=new HashSet<>();
    private HashMap<String,ArrayList<String>> lettersToWord=new HashMap<>();
    private HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordList.add(word);
            wordSet.add(word);
            String sortedWord=sortLetters(word);
            if(lettersToWord.containsKey(sortedWord)){
                lettersToWord.get(sortedWord).add(word);
            }else{
                lettersToWord.put(sortedWord,new ArrayList<String>());
                lettersToWord.get(sortedWord).add(word);
            }

            if(sizeToWords.containsKey(word.length())){
                sizeToWords.get(word.length()).add(word);
            }else {
                sizeToWords.put(word.length(),new ArrayList<String>());
                sizeToWords.get(word.length()).add(word);
            }

        }
    }

    public boolean isGoodWord(String word, String base) {
        return (wordSet.contains(word) && !(word.toLowerCase().contains(base.toLowerCase()))) ;
    }

    /*  NOT USED ANYWHERE

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTarget=sortLetters(targetWord);
        for(int i=0;i<wordList.size();i++){
            String temp=sortLetters(wordList.get(i));
            if(temp.equals(sortedTarget)){
                result.add(wordList.get(i));
            }
        }
        return result;
    }
    */

    private String sortLetters(String input){
        char[] chars=input.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public ArrayList<String> getAnagramsWithTwoMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String tempWord1,tempWord2;
        ArrayList<String> tempArrayList;
        for(char c='a';c<='z';c++){
            tempWord1 = word + c;
            for(char d='a'; d<='z';d++)
            {
                tempWord2 = tempWord1 + d;
                tempWord2 = sortLetters(tempWord2);
                if(lettersToWord.containsKey(tempWord2)){
                    tempArrayList=lettersToWord.get(tempWord2);
                    for(int i=0;i<tempArrayList.size();i++){
                        if(isGoodWord(tempArrayList.get(i),word)){
                            result.add(tempArrayList.get(i));
                        }
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int i;
        if(wordLength>MAX_WORD_LENGTH){
            wordLength=MAX_WORD_LENGTH;
        }
        if(sizeToWords.containsKey(wordLength)){
            ArrayList<String> lengthWords = sizeToWords.get(wordLength);
            int randomNum = random.nextInt(lengthWords.size()+1);
            for(i=randomNum;i<lengthWords.size();i++){
                if(getAnagramsWithTwoMoreLetter(lengthWords.get(i)).size()>=MIN_NUM_ANAGRAMS){
                    wordLength++;
                    return lengthWords.get(i);
                }
            }
            for(i=0;i<randomNum;i++){
                if(getAnagramsWithTwoMoreLetter(lengthWords.get(i)).size()>=MIN_NUM_ANAGRAMS){
                    wordLength++;
                    return lengthWords.get(i);
                }
            }
        }
        return null;
    }
}
