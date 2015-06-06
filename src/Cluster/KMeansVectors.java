// Copyright 2015 smanna@cpp.edu
//
// !!WARNING!! STUDENT SHOULD NOT MODIFY THIS FILE.
// NOTE THAT THIS FILE WILL NOT BE SUBMITTED, WHICH MEANS MODIFYING THIS FILE
// WILL NOT TAKE EFFECT WHILE EVALUATING YOUR CODE.


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Used to vectorize text from files.
 */
public class KMeansVectors {
    //dictionary maps each unique string to its document frequency
    //doc frequency is the number of documents it has appeared in
    private LinkedHashMap<String, Integer> docFrequency = new LinkedHashMap<>();

    private int totalDocumentCount = 0; //total number of documents added to the classifier
    private ArrayList<HashMap<String, Integer>> docTFs = new ArrayList<>();
    private ArrayList<ArrayList<Double>> vectors = new ArrayList<>();

    public KMeansVectors(File[] files) throws FileNotFoundException {
        int num = 0;
        for (File filename : files) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    addDocument(currentLine);
                    System.out.println("ADDED A DOCUMENT " + num++);
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        finish();
    }

    //given an input training doc, this method
    //converts string document to lowercase, removes punctuations, stems the words
    //and iterates through each individual string of the doc (delimited by whitespace)
    //and updates the fields of the classifier accordingly
    private void addDocument(String doc) {
        //increment the total document count
        totalDocumentCount++;
        HashMap<String, Integer> docTF = new HashMap<>(); // will be added to category's documentTF arraylist
        //use clean to remove punctuation, switch to lowercase, stem, and split
        //the document on whitespace
        String[] documents = clean(doc);
        for (String current : documents) {
            //increment the TF count of this string in the document's HashMap representation
            //and also, if appropriate, increment the doc frequency counter of string
            if (!docTF.containsKey(current)) {
                if (!docFrequency.containsKey(current))
                    docFrequency.put(current, 1);
                else
                    docFrequency.put(current, docFrequency.get(current) + 1);
                docTF.put(current, 1);
            } else
                docTF.put(current, docTF.get(current) + 1);
        }
        docTFs.add(docTF);
    }

    //must call this method after finishing entering the last training data (through the addTrainingDoc method)
    //and before using the attemptClassify method
    //will update each Document's vector model, and calculate category's centroid vectors
    //note: vectors are un-normalized
    private void finish() throws FileNotFoundException {
        //create an alphabetical ordering of all of the unique strings
        ArrayList<String> ordering = createOrdering();
        //create the documentVectorModel for each category's documents
        //for each of the categories...
        //for each document in each category
      //  PrintWriter pw = new PrintWriter(new File("Debug.txt"));

        int currentNum = 0;
        for (HashMap<String, Integer> docTF : docTFs) {
            //singleDoc will be each document's tf*idf vector
            ArrayList<Double> singleDoc = new ArrayList<>();
        //    pw.println();
            //for each string in the overall unique string ordering
            for (String key : ordering) {
                //add the tf*idf value for the string in the document to the singleDoc arraylist
                if (docTF.containsKey(key)){
                    singleDoc.add((1 + Math.log10(docTF.get(key))) * Math.log10(totalDocumentCount * 1.0 / docFrequency.get(key)));
                   // pw.print(key + " ");
                }

                else
                    singleDoc.add(0.0);

            }
            //add the singleDoc tf*idf vector to the documentVectorModel for this category
            vectors.add(singleDoc);
            System.out.println("FINISHED VECTORIZING DOCUMENT " + currentNum++);
        }
        //normalize the vectors
        for (int i = 0; i < vectors.size();i++)
        {
            ArrayList<Double> current = vectors.get(i);
            double sum = 0;
            for (int j = 0; j < current.size(); j++)
            {
                sum += current.get(j)*current.get(j);
            }
            sum = Math.sqrt(sum);
            for (int j = 0; j < current.size(); j++)
            {
                current.set(j,current.get(j)/sum);
            }
        }
     //   pw.close();
//        try {
//            PrintWriter cw = new PrintWriter(new File("Debug1.txt"));
//            for (int i = 0; i < vectors.size(); i++) {
//                ArrayList<Double> currentVector = vectors.get(i);
//                for (int j = 0; j < currentVector.size(); j++){
//                    cw.print(currentVector.get(j) + " ");
//                }
//                cw.println();
//
//
//            }
//            pw.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


    }

    private ArrayList<String> createOrdering() {
        ArrayList<String> ordering = new ArrayList<>();
        for (String s : docFrequency.keySet()) {
            ordering.add(s);
        }
        return ordering;
    }
    //cleans and stems each string, and gets rid of stop words
    private String[] clean(String doc) {
        String[] titles = doc.toLowerCase().replaceAll("[^\\w ]", "").split("\\s+");
        StopWords stop = new StopWords();
        ArrayList<String> holder = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            if (stop.isStopWord(titles[i])) {
                titles[i] = null;
                continue;
            }
            Stemmer s = new Stemmer();
            char[] word = titles[i].toCharArray();
            s.add(word, word.length);
            s.stem();
            holder.add(s.toString());
        }

        titles = holder.toArray(new String[holder.size()]);
        return titles;

    }

    protected HashMap<String, Integer> getDocFrequency() {
        return docFrequency;
    }

    int getTotalDocumentCount() {
        return totalDocumentCount;
    }

    public ArrayList<ArrayList<Double>> getVectors() {
        return vectors;
    }
}
