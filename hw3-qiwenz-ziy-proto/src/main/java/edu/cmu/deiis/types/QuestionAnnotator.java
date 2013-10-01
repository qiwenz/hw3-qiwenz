package edu.cmu.deiis.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

/*
 *I read the tutorial ex1 "RoomNumberAnnotator" and develop my own annotator. 
 */
public class QuestionAnnotator extends JCasAnnotator_ImplBase {
  /*
   * regular expression is amazing!
   */
  private Pattern mQuestionPattern = Pattern.compile("(Q).*(?)");

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    String docText = aJCas.getDocumentText();
    // search for Question
    Matcher matcher = mQuestionPattern.matcher(docText);
    while (matcher.find()) {
      Question question = new Question(aJCas);
      question.setBegin(matcher.start() + 2);
      question.setEnd(matcher.end());
      question.setConfidence(1);
      question.setCasProcessorId("QuetionAnnotator");
      question.addToIndexes();
    }
  }
}
