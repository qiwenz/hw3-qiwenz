package edu.cmu.deiis.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

//import org.apache.uima.jcas.tcas.Annotation;
/*
 *I read the tutorial ex1 "RoomNumberAnnotator" and develop my own annotator. 
 */

public class AnswerAnnotator extends JCasAnnotator_ImplBase {
  /*
   * regular expression is amazing!
   */
  private Pattern mAnswerPattern = Pattern.compile("(A).*(?)");

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    // read the text.
    String docText = aJCas.getDocumentText();
    // search for Answer
    Matcher matcher = mAnswerPattern.matcher(docText);
    while (matcher.find()) {
      Answer answer = new Answer(aJCas);
      answer.setIsCorrect(matcher.group().substring(2, 3).equals("1"));
      answer.setBegin(matcher.start() + 4);
      answer.setEnd(matcher.end());
      answer.setConfidence(1);
      answer.setCasProcessorId("AnswerAnnotator");
      answer.addToIndexes();
    }
  }
}