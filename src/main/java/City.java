import java.util.ArrayList;
import java.util.HashMap;

public class City {
    //total number of documents in this category
    private int dataCount;
    private String cityName;
    HashMap<String,Double> categoryPrecent;

    //centroid vector of the category
    public ArrayList<Double> centroid;
    public ArrayList<ArrayList<String>> vectors;


    public City( )
    {
        dataCount =0;
        vectors = new ArrayList<ArrayList<String>>();
        centroid = new ArrayList<Double>();
        categoryPrecent = new HashMap<String, Double>();
        cityName = "" ;
    }

    public  void addRow(Row row) {
        //increment the total data count
        dataCount++;
        cityName = row.city;
        ArrayList<String> newRow = new ArrayList<>();
        newRow.add(row.city);
        newRow.add(row.category);
        newRow.add(row.section);
        newRow.add(row.heading);
        vectors.add(newRow);

    }

}

