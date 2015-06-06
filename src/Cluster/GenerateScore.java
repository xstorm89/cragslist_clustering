// Copyright 2015 smanna@cpp.edu
//
// !!WARNING!! STUDENT SHOULD NOT MODIFY THIS FILE.
// NOTE THAT THIS FILE WILL NOT BE SUBMITTED, WHICH MEANS MODIFYING THIS FILE
// WILL NOT TAKE EFFECT WHILE EVALUATING YOUR CODE.


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GenerateScore {

//    public static void main(String[] args) throws Exception {
//        try {
//            String referenceFile = null;
//            String redditData = null;
//            String referenceCentroids = null;
//            //args = new String[]{"eval"};
//            if (args.length < 1 || args[0].equals("test")) {
//                referenceFile = "data/test/Distance.txt";
//                referenceCentroids = "data/test/PerfectCentroids";
//                redditData = "data/test/RedditData";
//            } else if (args[0].equals("eval")) {
//                referenceFile = "data/eval/Distance.txt";
//                redditData = "data/eval/evalReddit";
//                referenceCentroids = "data/eval/PerfectCentroids";
//            } else {
//                System.out.println("Unknown option: " + args[0]);
//                System.exit(1);
//            }
//            File redditDataFolder = new File(redditData);
//            System.out.println("CHECKING REDDIT DATA FOLDER");
//            File[] filenames = redditDataFolder.listFiles(new FilenameFilter() {
//                @Override
//                public boolean accept(File dir, String name) {
//                    return name.matches(".+\\.TITLE");
//                }
//            });
//            Arrays.sort(filenames);
//            for (File filename : filenames) {
//                System.out.println(filename);
//            }
//            System.out.println("CREATING TF-IDF VECTORS");
//            KMeansVectors kMeansVectors = new KMeansVectors(filenames);
//            ArrayList<ArrayList<Double>> vectors = kMeansVectors.getVectors();
//            ArrayList<ArrayList<Double>> seeds = new ArrayList<>();
//            seeds.add(vectors.get(250));
//            seeds.add(vectors.get(750));
//            seeds.add(vectors.get(1250));
//            seeds.add(vectors.get(1750));
//            seeds.add(vectors.get(2250));
//
//            //calculates perfect centroids
//            createPerfectCentroids(vectors,filenames,kMeansVectors,referenceCentroids);
//
//            KMeansClustering kMeansClustering = new KMeansClustering(filenames.length, vectors);
//            System.out.println("FORMING CLUSTERS");
//            ArrayList<ArrayList<Double>> answers;
//            try {
//                answers = kMeansClustering.formClusters(seeds);
//            } catch (Exception e) {
//                throw new FormClusterException();
//            }
//            if (answers == null) {
//                System.out.println("formClusters method in KMeansClustering.java is returning a null value.");
//                throw new FormClusterException();
//            }
//
//            ArrayList<ArrayList<Double>> perfectCentroids = new ArrayList<>();
//            try {
//                BufferedReader bufferedReader = new BufferedReader(new FileReader(referenceCentroids));
//                String currentLine;
//                while ((currentLine = bufferedReader.readLine()) != null) {
//                    String[] split = currentLine.split(" ");
//                    ArrayList<Double> values = new ArrayList<>();
//                    for (String aSplit : split) {
//                        values.add(Double.valueOf(aSplit));
//                    }
//                    perfectCentroids.add(values);
//                }
//                bufferedReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            double ultimateSum = 0;
//            ArrayList<ArrayList<Double>> distanceBetween = new ArrayList<>();
//            for (int i = 0; i < perfectCentroids.size(); i++) {
//                ArrayList<Double> currentRow = new ArrayList<>();
//                for (ArrayList<Double> answer : answers)
//                    currentRow.add(calculateDistance(perfectCentroids.get(i), answer));
//                distanceBetween.add(currentRow);
//                System.out.print(currentRow);
//                System.out.println("\t\t\t\t\t Distance from perfect centroid " + i + " to closest cluster centroid is: " + getMin(currentRow));
//                ultimateSum += getMin(currentRow);
//            }
//            System.out.println("Total sum of distances is: " + ultimateSum);
//
//            BufferedReader bf = new BufferedReader(new FileReader(referenceFile));
//            double baseLineDistance = Double.parseDouble(bf.readLine());
//            double grade = baseLineDistance / ultimateSum * 100;
//            System.out.println("Your grade is : " + grade + "%");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (FormClusterException e) {
//            System.out.println(e.getMessage());
//            System.out.println("Your FormClusters method has an error");
//            System.out.println("Grade = 0%");
//        }
//    }
//
//    private static double calculateDistance(ArrayList<Double> a, ArrayList<Double> b) {
//        double sum = 0;
//        for (int i = 0; i < a.size(); i++) {
//            sum += (a.get(i) - b.get(i)) * (a.get(i) - b.get(i));
//        }
//        return Math.sqrt(sum);
//    }
//
//    private static double getMin(ArrayList<Double> doubles) {
//        double min = Double.POSITIVE_INFINITY;
//        for (Double aDouble : doubles) {
//            if (aDouble < min) {
//                min = aDouble;
//            }
//        }
//        return min;
//    }
//
//    private static void createPerfectCentroids(ArrayList<ArrayList<Double>> vectors, File[] filenames, KMeansVectors kMeansVectors, String referenceCentroids) throws IOException
//    {
//        PrintWriter pw = new PrintWriter(new FileWriter(referenceCentroids));
//        int docFrequency = kMeansVectors.getDocFrequency().keySet().size();
//        double[][] sums = new double[filenames.length][docFrequency];
//
//        for (int i = 0; i < 500; i++) {
//            for (int j = 0; j < vectors.get(i).size(); j++) {
//                sums[0][j] += vectors.get(i).get(j);
//            }
//        }
//        for (int j = 0; j < docFrequency; j++) {
//            sums[0][j] /= 500;
//        }
//
//        double sum = 0;
//        for (int j = 0; j < 500; j++)
//        {
//            sum += sums[0][j]*sums[0][j];
//        }
//        sum = Math.sqrt(sum);
//        for (int j = 0; j < 500; j++)
//        {
//            sums[0][j] /= sum;
//        }
//        // 500-1000
//        for (int i = 500; i < 1000; i++) {
//            for (int j = 0; j < vectors.get(i).size(); j++) {
//                sums[1][j] += vectors.get(i).get(j);
//            }
//        }
//        for (int j = 0; j < docFrequency; j++) {
//            sums[1][j] /= 500;
//        }
//
//        sum = 0;
//        for (int j = 500; j < 1000; j++)
//        {
//            sum += sums[1][j]*sums[1][j];
//        }
//        sum = Math.sqrt(sum);
//        for (int j = 500; j < 1000; j++)
//        {
//            sums[1][j] /= sum;
//        }
//
//        for (int i = 1000; i < 1500; i++) {
//            for (int j = 0; j < vectors.get(i).size(); j++) {
//                sums[2][j] += vectors.get(i).get(j);
//            }
//        }
//        for (int j = 0; j < docFrequency; j++) {
//            sums[2][j] /= 500;
//        }
//
//        sum = 0;
//        for (int j = 1000; j < 1500; j++)
//        {
//            sum += sums[2][j]*sums[2][j];
//        }
//        sum = Math.sqrt(sum);
//        for (int j = 1000; j < 1500; j++)
//        {
//            sums[2][j] /= sum;
//        }
//
//        for (int i = 1500; i < 2000; i++) {
//            for (int j = 0; j < vectors.get(i).size(); j++) {
//                sums[3][j] += vectors.get(i).get(j);
//            }
//        }
//        for (int j = 0; j < docFrequency; j++) {
//            sums[3][j] /= 500;
//        }
//
//        sum = 0;
//        for (int j = 1500; j < 2000; j++)
//        {
//            sum += sums[3][j]*sums[3][j];
//        }
//        sum = Math.sqrt(sum);
//        for (int j = 1500; j < 2000; j++)
//        {
//            sums[3][j] /= sum;
//        }
//
//        for (int i = 2000; i < 2500; i++) {
//            for (int j = 0; j < vectors.get(i).size(); j++) {
//                sums[4][j] += vectors.get(i).get(j);
//            }
//        }
//        for (int j = 0; j < docFrequency; j++) {
//            sums[4][j] /= 500;
//        }
//
//        sum = 0;
//        for (int j = 2000; j < 2500; j++)
//        {
//            sum += sums[4][j]*sums[4][j];
//        }
//        sum = Math.sqrt(sum);
//        for (int j = 2000; j < 2500; j++)
//        {
//            sums[4][j] /= sum;
//        }
//
//        for (double[] suma : sums) {
//            for (double v : suma) {
//                pw.print(v + " ");
//            }
//            pw.println();
//        }
//        pw.close();
//    }
}
