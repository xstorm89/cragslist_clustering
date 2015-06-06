import wpmcn.StandoffNamedEntityRecognizer;

import java.io.*;
import java.util.*;

/**
 * Created by 迅 on 2015/5/26.
 */
public class Vectorlize {

    private ArrayList<ArrayList<Double>> vectors = new ArrayList<>();

    static HashMap<String,City> cityData =new HashMap<>();
    static int dataCount;
    static final String filePath = "src/main/resources/training_v3.json";


    public Vectorlize(String filePath) throws FileNotFoundException {

        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                String line = bufferedReader.readLine();

                dataCount = Integer.parseInt(line);//first row data count

                while ((line = bufferedReader.readLine()) != null) {
                    Row rowToAdd;
                    rowToAdd = readline(line);

                    if(!cityData.containsKey(rowToAdd.city))
                        cityData.put(rowToAdd.city,new City(rowToAdd.city));

                    cityData.get(rowToAdd.city).addRow(rowToAdd);

                }
                read.close();
            } else {
                System.out.println("file does not exist");
            }
        } catch (Exception e) {
            System.out.println("error read file");
            e.printStackTrace();
        }

    }


    public void addPrecent(){


        ArrayList<ArrayList<Double>> vectorToAdd = new ArrayList<>();
        for(HashMap.Entry<String,City> e: cityData.entrySet()){
//            System.out.println(e.getKey()+": "+cityData.get(e.getKey()).getSections().toString());
            for(String section: e.getValue().getSections() ){
//                System.out.println(section+": "+cityData.get(e.getKey()).getSectionPrecent(section));
                e.getValue().dataPoint.add(cityData.get(e.getKey()).getSectionPrecent(section));
            }
//            System.out.println("total: "+e.getValue().getPostCount()+"\n");


            vectorToAdd.add(e.getValue().dataPoint);
        }

        vectors= vectorToAdd;

    }


    public void addAveragePrice() throws IOException, ClassNotFoundException {

        StandoffNamedEntityRecognizer entityRecognizer = new StandoffNamedEntityRecognizer("classifiers/english.muc.7class.distsim.crf.ser.gz");
        ArrayList<ArrayList<Double>> vectorToAdd = new ArrayList<>();


        for(HashMap.Entry<String,City> e: cityData.entrySet()) {
//            System.out.println(e.getKey()+": "+cityData.get(e.getKey()).getSections().toString());
            for (String section : e.getValue().getSections()) {
//                System.out.println(section+": "+cityData.get(e.getKey()).getSectionPrecent(section));

                e.getValue().dataPoint.add(cityData.get(e.getKey()).getSectionAvePrice(entityRecognizer, section));
//                System.out.println(e.getKey() + ": " + section + ": " + cityData.get(e.getKey()).getSectionAvePrice(entityRecognizer, section));

            }

            vectorToAdd.add(e.getValue().dataPoint);
        }

        vectors= vectorToAdd;
    }


    public void addLDA() throws IOException {


        LDA lda = new LDA();
        String input = "";

        ArrayList<ArrayList<Double>> vectorToAdd = new ArrayList<>();

        for (HashMap.Entry<String, City> e : cityData.entrySet()) {

            System.out.println(e.getKey() + ": ");
            for (String section: e.getValue().getSections()) {
//                System.out.println(section+": "+cityData.get(e.getKey()).getSectionPrecent(section));

                System.out.println(section+": ");

                input = cityData.get(e.getKey()).getSectionHeadings(section).toString();
//                e.getValue().dataPoint.add(cityData.get(e.getKey()).getSectionAvePrice(entityRecognizer, section));
//                System.out.println(e.getKey() + ": " + section + ": " + cityData.get(e.getKey()).getSectionAvePrice(entityRecognizer, section));

                e.getValue().dataPoint.addAll(lda.generateResult(input));// lda Feature


            }
            vectorToAdd.add(e.getValue().dataPoint);

        }
        vectors= vectorToAdd;

    }


    public void normalize(){

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

            vectors.set(i,current);
        }
    }

    public ArrayList<ArrayList<Double>> getVectors() {
        return vectors;
    }

    public ArrayList<Vector> getCityVectors(){

        ArrayList<Vector> vector = new ArrayList<>();

        for(HashMap.Entry<String,City> city: cityData.entrySet()) {
            vector.add(new Vector(city.getValue().dataPoint,city.getValue().getCityName()));
        }


        return vector;
    }

    private Row readline(String line){

        String newstring =line;
        newstring =newstring.replace("{","");
        newstring =newstring.replace("}","");
        newstring =newstring.replace(",","");
        newstring =newstring.replace(":","");
        newstring =newstring.replace("\"", ",");


        Row row;
        String[] splitted = newstring.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        row= new Row(splitted[3],splitted[7],splitted[11],splitted[15]);
//        System.out.println(row.city+" "+row.category+" "+ row.section+" "+row.heading);

        return row;
    }

    public void cleanFeature(){

        this.vectors.clear();
    }


//        public static void main(String[] args) throws IOException, ClassNotFoundException {
//
//        try {
//            File file = new File(filePath);
//            if (file.isFile() && file.exists()) {
//                InputStreamReader read = new InputStreamReader(new FileInputStream(file));
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String line = bufferedReader.readLine();
//
//                dataCount = Integer.parseInt(line);//first row data count
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    Row rowToAdd;
//                    rowToAdd = readline(line);
//
//                    if(!cityData.containsKey(rowToAdd.city))
//                        cityData.put(rowToAdd.city,new City(rowToAdd.city));
//
//                    cityData.get(rowToAdd.city).addRow(rowToAdd);
//
//
//
//
////                    System.out.println(cityData.size());
//
//
////                    entity= entityRecognizer.getEntity(rowToAdd.heading, "money");//Time, Location, Organization, Person, Money, Percent, Date
//
////                    if(!entity.isEmpty())
////                        System.out.println(entity.toString());
//
//
//
//
//                }
//                read.close();
//            } else {
//                System.out.println("file does not exist");
//            }
//        } catch (Exception e) {
//            System.out.println("error read file");
//            e.printStackTrace();
//        }
//
//        String text ="$5000 On this day in 1945, U.S. Marines captured Iwo Jima's Mount Suribachi in one of the bloodiest, most famous battles of World War II. The historic Associated Press photo of Marines raising the flag was also taken on this day.";
//
//        StandoffNamedEntityRecognizer entityRecognizer = null;
//
//
//        entityRecognizer = new StandoffNamedEntityRecognizer("classifiers/english.muc.7class.distsim.crf.ser.gz");
//
//
//        for(HashMap.Entry<String,City> e: cityData.entrySet()){
////            System.out.println(e.getKey()+": "+cityData.get(e.getKey()).getSections().toString());
//            for(String section: e.getValue().getSections() ){
////                System.out.println(section+": "+cityData.get(e.getKey()).getSectionPrecent(section));
//
//                System.out.println(e.getKey() + ": " + section + ": " + cityData.get(e.getKey()).getSectionAvePrice(entityRecognizer, section));
//
//            }
////            System.out.println("total: "+e.getValue().getPostCount()+"\n");
//
//        }
//
//
//
//
//
//    }


}
