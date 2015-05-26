package wpmcn;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Application that uses the Stanford Named Entity recognizer to return offset annotation of named entities in a text
 * file.
 *
 * This program takes two arguments, a path to a Stanford Named Entity classifier
 * (e.g. all.3class.distsim.crf.ser.gz) and the name of a file containing text to classify. It prints a list of
 * triples indicating the annotation type and beginning and ending offsets into the document along with the string
 * those offsets refer to.
 */
public class StandoffNamedEntityRecognizer {

   String model;
   CRFClassifier annotator;
   public StandoffNamedEntityRecognizer(String model) throws IOException, ClassNotFoundException {
      this.model =model;
      Options options = new Options();
      options.addOption("c", "custom-tokenization", false, "Use custom tokenization");
      annotator= CRFClassifier.getClassifier(model);
   }

   public List<String> getEntity (String text, String tag) {

      List<Triple<String, Integer, Integer>> annotations = annotator.classifyToCharacterOffsets(text);

      if (annotations.contains("MONEY")) {
         System.out.println("money");

      }
      List<String> result = new ArrayList<>();

      for (Triple<String, Integer, Integer> annotation : annotations) {
         int beginIndex = annotation.second();
         int endIndex = annotation.third();
         result.add(text.substring(beginIndex, endIndex));
      }
      return  result;
   }


//   public static void main(String[] args) throws ClassNotFoundException, IOException, ParseException {
//      Options options = new Options();
//      options.addOption("c", "custom-tokenization", false, "Use custom tokenization");
//
//      CommandLineParser parser = new PosixParser();
//      CommandLine cmd = parser.parse(options, args);
//      args = cmd.getArgs();
//
////      String model = args[0];
//      String model ="classifiers/english.muc.7class.distsim.crf.ser.gz";
//      String filename = args[1];
//      String text = FileUtils.readFileToString(new File(filename));
//      //String text = "at&amp;t iphone 5 32gb black good condition no scratches at all $550";
//
//      CRFClassifier annotator = cmd.hasOption("c") ?
//            new CustomCRFClassifier(model) : CRFClassifier.getClassifier(model);
//
//
//      @SuppressWarnings({"unchecked"})
//      List<Triple<String, Integer, Integer>> annotations = annotator.classifyToCharacterOffsets(text);
//      for (Triple<String, Integer, Integer> annotation: annotations) {
//         int beginIndex = annotation.second();
//         int endIndex = annotation.third();
//         System.out.println(annotation + " " + text.substring(beginIndex, endIndex));
//      }
//
//
//   }


}
