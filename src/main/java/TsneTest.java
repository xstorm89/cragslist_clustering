import org.deeplearning4j.datasets.fetchers.MnistDataFetcher;
import org.nd4j.linalg.dataset.DataSet;

import java.util.List;
import  java.util.ArrayList;

/**
 * Created by Ѹ on 2015/5/21.
 */
public class TsneTest {
    public static void main(String[] args) throws Exception  {
        Tsne tsne = new Tsne.Builder().setMaxIter(10000)
                .learningRate(500).useAdaGrad(false)
                .normalize(false).usePca(false).build();

        MnistDataFetcher fetcher = new MnistDataFetcher(true);
        fetcher.fetch(10);
        DataSet d2 = fetcher.next();
        List<String> list = new ArrayList<>();
        for(int i = 0; i < d2.numExamples(); i++)
            list.add(String.valueOf(d2.get(i).outcome()));
        tsne.plot(d2.getFeatureMatrix(),2,list);
    }
}
