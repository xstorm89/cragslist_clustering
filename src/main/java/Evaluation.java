import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by charlesxu on 6/5/15.
 */
public class Evaluation {

    ArrayList<Cluster> clusters;
    String standardFile = "src/main/resources/test_k3.json";
    HashMap<String, Integer> dataAssociation;


    public Evaluation(ArrayList<Cluster> clusters) throws IOException {

        this.clusters = clusters;

        dataAssociation = new HashMap<>();
        readFile();


    }

    private void readFile() throws IOException {

        File file = new File(standardFile);
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(read);
            String line;


            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\\s+");
                int association = Integer.parseInt(tokens[0]);


                for (int i = 1; i < tokens.length; i++) {
//                    System.out.println(tokens[i]);
                    dataAssociation.put(tokens[i], association);
                }

            }

        }

    }


    public ArrayList<Double> purity(){
        ArrayList<Double> result = new ArrayList<>();

        for(Cluster c:clusters){
//            System.out.println(dataAssociation.toString());
            int max ;
            ArrayList<Integer> count= new ArrayList<>();
            for(int k=0; k<clusters.size();k++)
                count.add(0);
            System.out.println(count.toString());
            for(int i=0; i<c.vectors.size();i++){
                for(int j=0;j<clusters.size();j++) {
                    if(dataAssociation.containsKey(c.vectors.get(i).city))
                        if (dataAssociation.get(c.vectors.get(i).city) == j) {
                            count.set(j, count.get(j) + 1);
                        }
//                    System.out.println(j);
                }

            }
            max= Collections.max(count);

            result.add((double)max/c.vectors.size());

        }



        return result;
    }


}
