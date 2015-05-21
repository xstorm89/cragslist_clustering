import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;



/**
 * Created by 迅 on 2015/5/21.
 */

public class Clustering {

    static HashMap<String,City> cityData;
    static int dataCount;
    static final String filePath = "src/main/resources/training.json";


    public static void main (String[] args) {


        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(read);
                String line = bufferedReader.readLine();

                dataCount = Integer.parseInt(line);//first row data count

                while ((line = bufferedReader.readLine()) != null) {
                    readline(line);
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

    public static Row readline(String line){

        String newstring =line;
        newstring =newstring.replace("{","");
        newstring =newstring.replace("}","");
        newstring =newstring.replace(",","");
        newstring =newstring.replace(":","");
        newstring =newstring.replace("\"", ",");



//        System.out.println(newstring);

        Row row;
        String[] splitted = newstring.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        row= new Row(splitted[3],splitted[7],splitted[11],splitted[15]);
//        System.out.println(row.city+" "+row.category+" "+ row.section+" "+row.heading);

        return row;
    }









/*
    public static List<String> readTxtFile(String filePath) {

        String Txtvalue = null;
        List<String> Txtlist = new ArrayList<String>();
        try {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int i = 0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    i++;
                    System.out.println(lineTxt);
                    Txtlist.add(lineTxt);
                }
                read.close();
                return Txtlist;
            } else {
                System.out.println("file does not exist");
            }
        } catch (Exception e) {
            System.out.println("error read file");
            e.printStackTrace();
        }

        return Txtlist;

    }

*/



}
