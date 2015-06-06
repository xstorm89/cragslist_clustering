// Copyright 2015 smanna@cpp.edu

import java.awt.print.Printable;
import java.io.IOException;
import java.util.ArrayList;


public class KMeansClustering implements KMeansClusteringInterface {
    private int kValue; //Number of clusters
    private ArrayList<Vector> cityVecs;
    private ArrayList<Cluster> clusters;


    private ArrayList<City> cityData;

    // TODO: students need to finish all the unfinished method stubs. You may
    // define any private data structure or method if needed.

    /**
     * Assign kValue to k
     * assign docVecs as shallow copy of s
     * create kValue new "Cluster" objects, in clusters arraylist
     * @param k number of clusters
     * @param s
     */
    public KMeansClustering(int k, ArrayList<Vector> s) {
        // TODO: students need to finish this constructor

        // Initialize the field “kValue” with the argument k
        this.kValue = k;

        // Initialize “docVecs” as a shallow copy of argument s

        this.cityVecs =new ArrayList<>(s);

        // Create kValue new Cluster objects to initialize the “clusters” arraylist.

        this.clusters = new ArrayList<Cluster>();

        for (int i=0; i<k; i++){
            Cluster c = new Cluster();
            c.centroid = new ArrayList<>();
            c.vectors = new ArrayList<>();
            c.sse =0.0;
            clusters.add(c);
        }


    }


    /**
     * Assign all docvecs to clusters whose centroids are closest
     * recalculate centroids
     * For our purposes, use fixed iterations of 25.
     * @param seeds initial centroids
     * @return centroids
     */
    public ArrayList<ArrayList<Double>> formClusters(ArrayList<ArrayList<Double>> seeds) {
        // TODO: students need to finish this method stub


        // Initially, you should copy each of the ArrayList<Double> vectors
        // within argument “seeds” to each cluster object’s centroid field.
        // We have chosen these seeds so that each seed corresponds to a title
        // chosen from a unique subreddit.

        // Then, perform 25 iterations of the KMeans Clustering algorithm by:
        // 1) assigning each docVec vector to the cluster with the closest
        // centroid vector (preferably using the findMin method).
        // 2) recalculating each cluster’s centroid vector by averaging the
        // document vectors within that cluster.
        // Be sure to then normalize the centroid vector afterwards.

        // Finally, return an arraylist of all centroid vectors.

        ArrayList<ArrayList<Double>> centoridV = new ArrayList<ArrayList<Double>>();

        for (int i = 0; i < seeds.size(); i++){
            clusters.get(i).centroid.addAll(seeds.get(i));
        }


        for (int i = 0; i < 25; i++) {

            for(int k=0; k<kValue; k++)
                clusters.get(k).vectors.clear();

            for (int j = 0; j < cityVecs.size(); j++) {

                clusters.get(findMin(cityVecs.get(j).data, clusters)).vectors.add(cityVecs.get(j));

//                System.out.print(clusters.size() + "asdasdasd");

            }


            for(int k=0; k<kValue; k++)
            {
                for(int j=0; j<clusters.get(k).vectors.get(0).data.size(); j++)
                {
                    double sum = 0.0;
                    for(int a=0; a<clusters.get(k).vectors.size(); a++)
                    {
                        sum += clusters.get(k).vectors.get(a).data.get(j);
                    }
                    clusters.get(k).centroid.set(j, sum/clusters.get(k).vectors.size());
                }
            }



//            for (int k = 0; i < kValue; i++) {
//                clusters.get(k).centroid =new ArrayList<>(meanCol(clusters.get(k).vectors));
////                System.out.print(clusters.size() + "asdasdasd");

//            }
        }

//normalize

//        for (int i = 0; i <clusters.size();i++)
//        {
//            double sum = 0;
//            for (int j = 0; j < clusters.get(i).centroid.size(); j++)
//            {
//                sum += Math.pow(clusters.get(i).centroid.get(j),2);
//            }
//            sum = Math.sqrt(sum);
//            for (int j = 0; j < clusters.get(i).centroid.size(); j++)
//            {
//                clusters.get(i).centroid.set(j, clusters.get(i).centroid.get(j)/sum);
//            }
//        }


        for(int i=0; i<kValue; i++)
            centoridV.add(clusters.get(i).centroid);


        return centoridV;
    }


    /**
     * Calculates the distances between vectors a and b.
     * @param a first vector
     * @param b second vector
     * @return distance between vectors
     */
    private static double calculateDistance(ArrayList<Double> a, ArrayList<Double> b) {
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += (a.get(i) - b.get(i)) * (a.get(i) - b.get(i));
        }
        return Math.sqrt(sum);
    }


    /**
     * Find the index number of arraylist of clusters to which the current 
     * arraylist belongs to
     * @param current current vector
     * @param clusters All clusters
     * @return index of cluster whose centroid is closest to current vector
     */
    private int findMin(ArrayList<Double> current, ArrayList<Cluster> clusters) {
        int min = 0;
        double smallestDistance = Double.MAX_VALUE;
        for (int i = 0; i < clusters.size(); i++) {
            double distance = calculateDistance(current, clusters.get(i).centroid);
            if (distance < smallestDistance) {
                min = i;
                smallestDistance = distance;
            }
        }
        return min;
    }


    private ArrayList<Double> meanCol(ArrayList<ArrayList<Double>> matrix) {
        ArrayList<Double> mean = new ArrayList<>();

        for(int i=0; i<matrix.get(0).size();i++){
            double sum = 0.0;
            for(int j=0; j<matrix.size();j++)
                sum+=matrix.get(i).get(j);

            mean.add(sum / kValue);

        }
        return mean;
    }

    public ArrayList<Double> getSSE(){

        ArrayList<Double> SSEs= new ArrayList<>();

        for(Cluster c: clusters){

            SSEs.add(calculateSSE(c));

        }
        return  SSEs;

    }

    private double calculateSSE(Cluster c) {
        double sse;
        double sum=0.0;

        for(Vector cityVector: c.vectors){
            sum+=squareSum(cityVector.data, c.centroid);
        }


        sse= sum/(double)c.vectors.size();
        return sse;
    }

    private  double squareSum(ArrayList<Double> a, ArrayList<Double> b) {
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += (a.get(i) - b.get(i)) * (a.get(i) - b.get(i));
        }
        return sum;
    }

    /**
     * Return all the clusters
     */
    public ArrayList<Cluster> getClusters() {
        return clusters;
    }



}
