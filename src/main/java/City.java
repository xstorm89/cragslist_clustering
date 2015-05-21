import java.util.ArrayList;
import java.util.HashMap;

public class City {
    //total number of documents in this category
    private int dataCount;
    private String cityName;
    private ArrayList<ArrayList<String>> vector;
    private HashMap<String,Double> categoryPrecent;

    //centroid vector of the category
    public ArrayList<Double> centroid;

    public City( )
    {
        dataCount =0;
        vector = new ArrayList<ArrayList<String>>();
        centroid = new ArrayList<Double>();
        categoryPrecent = new HashMap<String, Double>();
        cityName = "" ;
    }

    private  void addRow(Row row) {
        //increment the total data count
        dataCount++;
        cityName = row.city;
        ArrayList<String> newRow = new ArrayList<String>();
        newRow.add(row.city);
        newRow.add(row.category);
        newRow.add(row.section);
        newRow.add(row.heading);
        vector.add(newRow);

    }

    public ArrayList<ArrayList<String>> getVector(){
        return vector;
    }
}

