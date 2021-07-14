/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.io.*;
import java.util.*;
import java.util.Arrays;

/**
 *
 * @author hp
 */
public class Index2 {

    //--------------------------------------------
    Map<Integer, String> sources;  // store the doc_id and the file name
    HashMap<String, DictEntry2> index; // THe inverted index
    //--------------------------------------------

    Index2() {
        sources = new HashMap<Integer, String>();
        index = new HashMap<String, DictEntry2>();
    }

    //---------------------------------------------
    public void printDictionary() {
        Iterator it = index.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DictEntry2 dd = (DictEntry2) pair.getValue();
            HashSet<Integer> hset = dd.postingList;// (HashSet<Integer>) pair.getValue();
            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "] <" + dd.term_freq + "> =--> ");
            Iterator<Integer> it2 = hset.iterator();
            while (it2.hasNext()) {
                System.out.print(it2.next() + ", ");
            }
            System.out.println("");
            //it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("------------------------------------------------------");
        System.out.println("* Number of terms = " + index.size());
    }

    //-----------------------------------------------
    public void buildIndex(String[] files) {
        int i = 0;
        for (String fileName : files) {
            try (BufferedReader file = new BufferedReader(new FileReader(fileName))) {
                sources.put(i, fileName);
                String ln;
                while ((ln = file.readLine()) != null) {
                    String[] words = ln.split("\\W+");
                    for (String word : words) {
                        word = word.toLowerCase();
                        // check to see if the word is not in the dictionary
                        if (!index.containsKey(word)) {
                            index.put(word, new DictEntry2());
                        }
                        // add document id to the posting list
                        if (!index.get(word).postingList.contains(i)) {
                            index.get(word).doc_freq += 1; //set doc freq to the number of doc that contain the term 
                            index.get(word).postingList.add(i); // add the posting to the posting:ist
                        }
                        //set the term_fteq in the collection
                        index.get(word).term_freq += 1;
                    }
                }
                printDictionary();
            } catch (IOException e) {
                System.out.println("File " + fileName + " not found. Skip it");
            }
            i++;
        }
    }

    //--------------------------------------------------------------------------
    // query inverted index
    // takes a string of terms as an argument
     public String find(String phrase) {
        String[] words = phrase.split("\\W+");
        HashSet<Integer> res = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        for (String word : words) {
            res.retainAll(index.get(word).postingList);
        }
        if (res.size() == 0) {
            System.out.println("Not found");
            return "";
        }
        String result = "Found in: \n";
        for (int num : res) {
            result += "\t" + sources.get(num) + "\n";
        }
        return result;
    }
    //----------------------------------------------------------------------------  
    HashSet<Integer> intersect(HashSet<Integer> pL1, HashSet<Integer> pL2) {
        HashSet<Integer> answer = new HashSet<>();
        Iterator<Integer> i = pL1.iterator();  //2,4,6
        Integer it = i.next();
        while (i != null) {
            Iterator<Integer> i2 = pL2.iterator();  //1,2,6
            Integer it2 = i2.next();
            while (i2 != null) {
                if (it == it2) {
                    answer.add(it);
                    break;
                }
                if (i2.hasNext()) {

                    it2 = i2.next();

                } else {
                    i2 = null;
                }
            }
            if (i.hasNext()) {
                it = i.next();
            } else {
                i = null;
            }
        }

        return answer;

    }
    //-----------------------------------------------------------------------   

    public String find_01(String phrase) { // 2 term phrase  2 postingsLists
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        HashSet<Integer> answer = intersect(pL1, pL2);
        System.out.println("Found in: ");
        for (int num : answer) {
            System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        return result;
    }
//-----------------------------------------------------------------------         

    public String find_02(String phrase) { //  lists
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        HashSet<Integer> pL3 = new HashSet<Integer>(index.get(words[2].toLowerCase()).postingList);
        HashSet<Integer> answer = intersect(pL1, pL2);
        HashSet<Integer> answer2 = intersect(answer, pL3);

        System.out.println("Found in: ");
        for (int num : answer2) {
            System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        return result;

    }
    //-----------------------------------------------------------------------         

    public String find_03(String phrase) { 
        String result = "";
        String[] words = phrase.split("\\W+");
        //ArrayList<HashSet<Integer>> intersections = new ArrayList<HashSet<Integer>>();
        HashSet<Integer> answer = new HashSet<Integer>();
        if (words.length == 1) {
            answer = index.get(words[0].toLowerCase()).postingList;
          System.out.println("Found in: ");
            for (int num : answer) {
               System.out.println("\t" + sources.get(num));
                result += "\t" + sources.get(num) + "\n";
            }

        } else if (words.length == 2) {
            find_01(phrase);
        } else if (words.length == 3) {
            find_02(phrase);
        } //2,4 - 4 - 4
        else {

            HashSet<Integer> p1 = new HashSet<>();
            answer = index.get(words[0].toLowerCase()).postingList;
            for (int i = 1; i < words.length; i++) {
                p1 = index.get(words[i].toLowerCase()).postingList;
                answer = intersect(answer, p1);
               

            }
           System.out.println("Found in: ");
            for (int num : answer) {
               System.out.println("\t" + sources.get(num));
                result += "\t" + sources.get(num) + "\n";
            }
        }

        return result;
    }
    //-----------------------------------------------------------------------         

    public String find_04(String phrase) { // optimized search 
        String result = "";
        String[] words = phrase.split("\\W+");// noor, ayman , essmat
        HashSet<Integer> answer = new HashSet<Integer>();
        if (words.length == 1) {
            answer = index.get(words[0].toLowerCase()).postingList;
          System.out.println("Found in: ");
            for (int num : answer) {
               System.out.println("\t" + sources.get(num));
                result += "\t" + sources.get(num) + "\n";
            }

        } else //more than 2
        {
            String sorted_words[];
            sorted_words = new String[words.length];
            for(int i=0;i<words.length;i++){
            sorted_words[i]=words[i]; // 
            }
            Integer f, k;// noor[3],ayman[2] ,essmat[]
            String temp;
            for (f = 0; f < words.length; f++) {
                for (k = f + 1; k < words.length; k++) {
                    if (index.get(words[k].toLowerCase()).doc_freq< index.get(words[f].toLowerCase()).doc_freq) {
                        temp = sorted_words[f];
                        sorted_words[f] = sorted_words[k];
                        sorted_words[k] = temp;
                    }
                }
            }
           
            HashSet<Integer> p1 = new HashSet<>(); //noor[4],ayman[3],essmat[2]
            answer = index.get(sorted_words[0].toLowerCase()).postingList;
            for (int i = 1; i < sorted_words.length; i++) {
                p1 = index.get(sorted_words[i].toLowerCase()).postingList;
                answer = intersect(answer, p1);
            }
            
          System.out.println("Found in: ");
            for (int num : answer) {
                System.out.println("\t" + sources.get(num));
                result += "\t" + sources.get(num) + "\n";
            }
        }

        return result;
    }
    //-----------------------------------------------------------------------         
  //(*) elapsed = 250 ms.
 //(*) Find_03 non-optimized intersect  elapsed = 408 ms.
 //(*) Find_04 optimized intersect elapsed = 155 ms.
    public void compare(String phrase) { // optimized search 
        long iterations = 10000;
        String result = "";
        
        long startTime = System.currentTimeMillis();
        for (long i = 1; i < iterations; i++) {
            result = find(phrase);
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(" (*) elapsed = " + estimatedTime + " ms.");

        //System.out.println(" result = " + result);
        startTime = System.currentTimeMillis();
        for (long i = 1; i < iterations; i++) {
            result = find_03(phrase);
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(" (*) Find_03 non-optimized intersect  elapsed = " + estimatedTime + " ms.");
       // System.out.println(" result = " + result);

        startTime = System.currentTimeMillis();
        for (long i = 1; i < iterations; i++) {
            result = find_04(phrase);
        }
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(" (*) Find_04 optimized intersect elapsed = " + estimatedTime + " ms.");
        //System.out.println(" result = " + result);
    }
}
