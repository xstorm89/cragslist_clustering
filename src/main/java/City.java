import edu.stanford.nlp.util.ArraySet;
import wpmcn.StandoffNamedEntityRecognizer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class City {
    //total number of documents in this category
    private int dataCount;
    private String cityName;


    public ArrayList<Row> vectors;

    public ArrayList<Double> dataPoint;




    public City(String name)
    {
        dataCount =0;
        vectors = new ArrayList<>();
        dataPoint= new ArrayList<>();
        cityName = name ;
    }

    public  void addRow(Row row) {
        //increment the total data count
        dataCount++;

        vectors.add(row);

    }


    public ArraySet<String> getSections(){

        ArraySet<String> sections = new ArraySet<>();

        for(Row row :this.vectors){

            sections.add(row.section);
        }


        return sections;
    }

    public double getSectionPrecent(String section){

        double prec=0.0;
        int postCount =0;

        for(Row r: this.vectors){
            if(r.section.equalsIgnoreCase(section))
                postCount++;

        }


        prec= (double)postCount/this.dataCount;



        return prec;
    }


    public ArrayList<String> getSectionHeadings(String section){

        ArrayList<String> headings =new ArrayList<>();

        for (Row r: this.vectors){
            if(r.section.equalsIgnoreCase(section))
                headings.add(r.heading);
        }


        return  headings;
    }

    public double getSectionAvePrice(StandoffNamedEntityRecognizer entityRecognizer, String section){

        double average= 0;
        double sum= 0;

        int count =0;



        List<String> entity = new ArrayList<>();

        for(Row r: this.vectors) {
            if (r.section.equalsIgnoreCase(section)) {
                entity = entityRecognizer.getEntity(r.heading, "money");//Time, Location, Organization, Person, Money, Percent, Date
                if (!entity.isEmpty()) {
                    for (String st : entity) {
                        if(st.contains("-"))
                            break;
                        st = st.replaceAll("[^0-9.,]+", "");
                        if(!st.equals(" ")&&!st.equals("")) {
                            count++;
                            sum += Double.parseDouble(st);
                        }
                    }
                }

            }
        }

        if(count==0)
            return 0;

        average=sum/(double)count;
        return average;


    }



    public int getPostCount(){
        return this.dataCount;
    }

    public  String getCityName(){return this.cityName;}

}

