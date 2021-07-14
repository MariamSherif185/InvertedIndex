/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.util.*;

/**
 *
 * @author hp
 */
public class InvertedIndex002 {

    public static void main(String args[]) throws IOException {
       /* Index2 index = new Index2();
        HashSet<Integer> ans1= new HashSet<>();
        HashSet<Integer> ans2= new HashSet<>();
        ans1.add(2);
        ans1.add(4);
        ans1.add(6);
        ans2.add(1);
        ans2.add(2);
        ans2.add(6);
      
        index.intersect(ans1, ans2);*/
        
        
        Index2 index = new Index2();
        String phrase = "";

        index.buildIndex(new String[]{
            "C:\\Users\\Ayman\\Desktop\\tmp/100.txt", // change it to your path e.g. "c:\\tmp\\100.txt"
            "C:\\Users\\Ayman\\Desktop\\tmp/101.txt",
            "C:\\Users\\Ayman\\Desktop\\tmp/102.txt",
            "C:\\Users\\Ayman\\Desktop\\tmpp/103.txt",
            "C:\\Users\\Ayman\\Desktop\\tmp/104.txt",
            "C:\\Users\\Ayman\\Desktop\\tmp/105.txt",
            "C:\\Users\\Ayman\\Desktop\\tmp/106.txt",
            "C:\\Users\\Ayman\\Desktop\\tmp/107.txt",           
            "C:\\Users\\Ayman\\Desktop\\tmp/108.txt",           
            "C:\\Users\\Ayman\\Desktop\\tmp/109.txt"
        });
        //index.find("noor");
        //index.find_01("Information System");
      // index.find_02("Ehab Ezat Eman");

        //index.find_03("Ehab");
     
        //index.find_03("Ehab Ezat");
       //index.find_03("Ahmed Ramzy Ahmed Ahmed");
       //index.find_04("mariam noor");
        
        index.compare("mariam noor nermeen");
       /*do {
            System.out.println("Print search phrase: ");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            phrase = in.readLine();
            System.out.println(index.find(phrase));
        } while (!phrase.isEmpty());*/
    }
    
}