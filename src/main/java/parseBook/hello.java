package parseBook;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by strapper on 04.09.15.
 */
public class hello {

    public static ArrayList<String> loadLexicon( String fileName) {
        ArrayList<String> set = new ArrayList<String>();

        URL oracle = null;
        try {
//            oracle = new URL(fileName);
//            URLConnection yc = oracle.openConnection();
            Scanner in = new Scanner(new File(fileName));
            int i = 1;

            while ( in.hasNext()) {
                String inputLine = in.nextLine();
                if(!inputLine.equals("") && inputLine.length() > 2) {

                    String arr[] = inputLine.split("—pron\\.|pron\\.|—prep\\.|prep\\.|—adv\\.|adv\\.|—n\\.|n\\.|—adj\\.|adj\\.|—v\\.|v\\.|—abbr\\.|abbr\\.", 2);
                    if(arr.length == 2) {
                        arr[0] = arr[0].trim();


                        int wordLength = arr[0].length();
                        arr[1] = inputLine.substring(wordLength, inputLine.length());
                        arr[0] = StringUtils.strip(arr[0], "1");
                        arr[1] = arr[1].trim();

                        if(arr[1].substring(0,1).equals("—")) {
                            arr[1] = arr[1].substring(1,arr[1].length());
                        }
                    }
                    if(arr.length == 1) {
                        arr = inputLine.split(" ", 2);
                    }



                    try{
                        String firstWord = arr[0];   //first word
                        String theRest = arr[1];     //the rest of the sentence
                        String value = arr[0].toLowerCase() + "::" + theRest;
                        set.add(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(inputLine);
                    }


                  //  System.out.println(firstWord + "::" + theRest);


                }
                i++;
            }

            System.out.println(i);

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    public static void printToFile(ArrayList<String> list) {
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream("/home/strapper/Desktop/output2.txt"));
            System.setOut(out);

            for(String line: list) {
                System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(out);
    }



    public static void main(String[] args) {
        String fileName = "/home/strapper/Desktop/dictionary.txt";

        ArrayList<String> list = loadLexicon(fileName);

        printToFile(list);

        for(String line: list) {
//            System.out.println(line);
        }
    }

}
