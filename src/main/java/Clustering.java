import edu.stanford.nlp.util.ArraySet;

import javax.xml.bind.util.ValidationEventCollector;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Ѹ on 2015/5/21.
 */

public class Clustering {

    static HashMap<String, City> cityData;
    static int dataCount;
    static final String filePath = "src/main/resources/training_v3.json";



    public static void main(String[] args) throws IOException, ClassNotFoundException, FormClusterException {

        Vectorlize vectorLize =new Vectorlize(filePath);

        vectorLize.addPrecent();
        vectorLize.addAveragePrice();
//        vectorLize.addLDA();
        vectorLize.normalize();

        for(ArrayList<Double> data :vectorLize.getVectors()){
            for(double d :data)
                System.out.println(d);
        }



        ArrayList<Vector> vectors = vectorLize.getCityVectors();

        int k = 3;

        FuzzyCMeansClustering FCM = new FuzzyCMeansClustering(k,1.5,0.5, vectors);
        FCM.runFcm_myself();

        for(int i=0; i<k;i++){
            System.out.print("clusters:"+i+": ");
            for(Vector city: FCM.getClusters().get(i).vectors)
                System.out.print(city.city+" ");
            System.out.print("\n");
        }
        System.out.println("centroids: "+FCM.getCentroids().toString());
        System.out.println("SSE: "+ FCM.getSSE().stream().mapToDouble(Double::doubleValue).sum());



//        System.out.println(vectors.size() + "  " + vectors.get(1).data.size());

        ArrayList<ArrayList<Double>> seeds = new ArrayList<>();
        for(int i=0; i<k;i++){
            Random rand = new Random();
            int randomNum = rand.nextInt(16);
            System.out.println(randomNum);
            seeds.add(vectors.get(randomNum).data);

        }



        KMeansClustering kMeansClustering = new KMeansClustering(k,vectors);
        ArrayList<Cluster> clusters;


        System.out.println("FORMING CLUSTERS");
        ArrayList<ArrayList<Double>> answers;
        try {
            answers = kMeansClustering.formClusters(seeds);

        } catch (Exception e) {
            throw new FormClusterException();
        }
        if (answers == null) {
            System.out.println("formClusters method in KMeansClustering.java is returning a null value.");
            throw new FormClusterException();
        }

        int index=0;
        for(Cluster c : kMeansClustering.getClusters()){
            System.out.print("clusters:"+index+": ");
            for(Vector v: c.vectors)
                System.out.print(v.city+" ");
            System.out.print("\n");
            index++;
        }


        System.out.println("centroids: "+answers.toString());
        System.out.println("SSE: " + kMeansClustering.getSSE().stream().mapToDouble(Double::doubleValue).sum());



    }

}
