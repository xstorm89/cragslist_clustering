import wpmcn.StandoffNamedEntityRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 迅 on 2015/5/26.
 */
public class Vectorlize {

    private ArrayList<ArrayList<Double>> vectors = new ArrayList<>();


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        List<String> entity = new ArrayList<>();

        String text ="$5000 On this day in 1945, U.S. Marines captured Iwo Jima's Mount Suribachi in one of the bloodiest, most famous battles of World War II. The historic Associated Press photo of Marines raising the flag was also taken on this day.";
        StandoffNamedEntityRecognizer entityRecognizer = new StandoffNamedEntityRecognizer("classifiers/english.muc.7class.distsim.crf.ser.gz");
       entity= entityRecognizer.getEntity(text,"MONEY");

        System.out.println(entity.toString());
    }

    public ArrayList<ArrayList<Double>> getVectors() {
        return vectors;
    }

}
