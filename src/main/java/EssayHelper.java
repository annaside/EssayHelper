/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.*;

/**
 * Program is an essay helper that would give the user important information 
 * about an uploaded text.
 * The user may run the program after adding a text file containing the text 
 * that they want information about.
 * 
 * @author annaq
 */
public class EssayHelper {
    
    /**
     * <p> main method
     * </p>
     * @param args command line arguments
     */
    public static void main(String[] args) 
     {
        //scanner declare
        Scanner input = new Scanner(System.in);
        
        //initia prompt
        System.out.println("Please input your file name (example: text.txt)");
        String fileName = input.nextLine();
        
        //FileReader
        File textFile = new File(fileName);
        java.io.FileReader in;
        BufferedReader readFile;
        String lineOfText;
        String origText = "";
        String initText = "";
        try {
            in = new java.io.FileReader(textFile);
            readFile = new BufferedReader(in);
            while ((lineOfText = readFile.readLine()) != null ) {
                //initText: origText with added spaces to the end of lines so that words dont get jumbled
                if (lineOfText.equals("") || lineOfText.substring(lineOfText.length()-1).equals(" ")){
                    initText += (lineOfText);
                }
                else {
                    initText += (lineOfText + " ");
                }
                origText += lineOfText;
            }
            readFile.close();
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist or could not be found.");
            System.err.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Problem reading file.");
            System.err.println("IOException: " + e.getMessage());
    	}
        
        //newInitText: initText with removed spaces at the beginning and end of the text
        String newInitText;
        newInitText = initText;
        while (newInitText.substring(0,1).equals(" ")) {
            newInitText = newInitText.substring(1);
        }
        while (newInitText.substring(newInitText.length() - 1).equals(" ")) {
            newInitText = newInitText.substring(0,newInitText.length() - 1);
        }
        
        //user prompts and printed results
        System.out.println("""
                           
                           What would you like to do with your text?
                           sc - sentence count
                           as - average sentence length
                           wc - word count
                           wr - repetition count of chosen word
                           cc - character count
                           
                           Type "exit" to leave program""");
        
        String choice = "";
        
        while (!choice.equals("exit")) {
            System.out.println();
            choice = input.nextLine().toLowerCase();
            
            switch (choice) {
                case "sc" -> System.out.println("\nSentence count: " + sentenceCount(newInitText));
                case "as" -> System.out.println("\nAverage sentence length: " + averageSentenceLength(newInitText) + " words long");
                case "wc" -> System.out.println("\nWord count: " + wordCount(newInitText));
                case "wr" -> {
                    System.out.println("\nPlease choose a word");
                    String mot = input.nextLine();
                    System.out.println("Repeats: " + wordRepNum(mot, newInitText));
                }
                case "cc" -> System.out.println("\nCharacter count: " + origText.length());
                case "exit" -> System.out.println("\nThank you!");
                default -> System.out.println("\ninvalid input");
                //add more functions as needed
            }

        }
    }
    
    /**
     * <p>finds the sentence count of a text
     * </p>
     * @param text text that we want to find the sentence count of
     * @return the sentence count of text as int
     */
    public static int sentenceCount(String text) {
        
        int n = 0;
        for (int i = 0; i <= text.length(); i = indexSentenceEnd(i,text) + 1) {
            if (indexSentenceEnd(i,text) == -1) {
                return n;
            }
            
            n++;
        }
        return -2; // something went wrong
    }
    
    /**
     *<p> finds the index of the next sentence-ending punctuation
     * </p>
     * 
     * @param sentenceStart the index of the start of a sentence in text
     * @param text text potentially containing a sentence
     * @return index of the next sentence-ending punctuation
     */
    public static int indexSentenceEnd(int sentenceStart, String text){
        
        int a = text.indexOf(".",sentenceStart);
        while (!isUpper(text.substring(indexSkipSpace(a + 1,text))) && a != text.lastIndexOf(".") && a != -1)  {
            a++;
            a = text.indexOf("." , a);
        }
        if (a==-1) {
            a = Integer.MAX_VALUE;
        }
        
        int b = text.indexOf("!",sentenceStart);
        while (!isUpper(text.substring(indexSkipSpace(b + 1,text))) && b != text.lastIndexOf("!") && b!=-1) {
            b++;
            b = text.indexOf("!", b);
        }
        if (b==-1) {
            b = Integer.MAX_VALUE;
        }
        
        int c = text.indexOf("?",sentenceStart);
        while (!isUpper(text.substring(indexSkipSpace(c + 1,text))) && c != text.lastIndexOf("?") && c!=-1) {
            c++;
            c = text.indexOf("?", c);
        }
        if (c==-1) {
            c = Integer.MAX_VALUE;
        }
        
        if (a < b && a < c) {
            return a;
        } else if (b < c && b < a) {
            return b;
        } else if (c < a && c < b){
            return c;
        } else {
            return -1;
        }
    }
    
    /**
     * @param start the index of a space from where we want to find the next character
     * @param text
     * @return index of the next character that isn't a space
     */
    public static int indexSkipSpace(int start, String text) {
        
        int index = start;
        while (index < text.length() && text.substring(index, index+1).equals(" ")) {
            index++;
        }
        return index;
    }
    
    //creats an arraylist of all words in lowercase including duplicates
    /**
     * <p>creates an ArrayList with all words in the text (to lowercase, including duplicates)
     * </p>
     * @param text text which we want the complete word list of
     * @return an ArrayList with all words in text to lowercase
     */
    public static ArrayList<String> wordListDupes(String text) {
        String[] textArray = text.replaceAll("[^A-Za-z0-9\'\\s]", "").toLowerCase().split("\\s+");
        ArrayList<String> list;
        list = new ArrayList(Arrays.asList(textArray));
        return list;
    }
    
    /**
     *<p> finds the word count of a text
     * </p>
     * @param text
     * @return amount of words in text (separated by a space)
     */
    public static int wordCount(String text) {
        return wordListDupes(text).size();
    }
    
    /**
     * <p>checks if the first letter of a String is upper case
     * </p>
     * @param text
     * @return if the first letter of the text is upper case, returns true, if not, returns false
     */
    public static boolean isUpper(String text) {
        final String UPPER_CASE_LIST = "QWERTYUIOPASDFGHJKLZXCVBNM";
        if (text.length() == 0) {
            return false;
        }
        for (int i = 0; i < UPPER_CASE_LIST.length(); i++) {
            if (text.substring(0,1).equals(UPPER_CASE_LIST.substring(i, i + 1))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * <p>counts the amount of times a word appears in a text
     * </p>
     * @param word word we want the repetition amount of
     * @param text text containing sentences that may or may not have word
     * @return the amount of times the word appears (separated by spaces) in text
     */
    public static int wordRepNum(String word,String text) {
        ArrayList<String> list = new ArrayList(wordListDupes(text));
        int c = 0;
        
        for (String w : list) {
            if (w.equals(word.toLowerCase())) {
                c++;
            }
        }
        
        return c;
    }
    
    /**
     *<p>finds the average sentence length of a text using the word count and sentence count
     * </p>
     * @param text
     * @return average length of the sentences text contains
     */
    public static double averageSentenceLength(String text) {
        int wordAmount , sentenceAmount;
        
        wordAmount = wordCount(text);
        sentenceAmount = sentenceCount(text);
        
        if (sentenceAmount < 1) {
            return 0;
        }
        return (double) wordAmount / sentenceAmount;
    }
            
}
