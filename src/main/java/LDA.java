/**
 * Created by charlesxu on 5/30/15.
 */


import hankcs.lda.Corpus;
import hankcs.lda.LdaGibbsSampler;
import hankcs.lda.LdaUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static hankcs.lda.Corpus.loadString;

public class LDA {




    public ArrayList<Double> generateResult(String st) throws IOException {

        Corpus corpus = loadString(st);
        // 2. Create a LDA sampler
        LdaGibbsSampler ldaGibbsSampler = new LdaGibbsSampler(corpus.getDocument(), corpus.getVocabularySize());
        // 3. Train it
        ldaGibbsSampler.gibbs(5);
        // 4. The phi matrix is a LDA model, you can use LdaUtil to explain it.
        double[][] phi = ldaGibbsSampler.getPhi();
        Map<String, Double>[] topicMap = LdaUtil.translate(phi, corpus.getVocabulary(), 5);
//        LdaUtil.explain(topicMap);

        return LdaUtil.averageTopicDistribution(topicMap);
    }



}
