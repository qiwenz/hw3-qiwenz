package edu.cmu.deiis.types;

/*
 *First, I choose all the NGram(1,2,3) within the question and store them in an 
 *ArrayList<String>. Then I analysis each answer and store all NGram for each answer in 
 *another ArrayList<String>. Finally, I do traversal to the two  ArrayList<String> and 
 *compare the NGram in them to get the score.
 */

import java.util.Iterator;
import java.util.ArrayList;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

public class AnswerScoreAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    FSIndex answerIndex = aJCas.getAnnotationIndex(Answer.type);
    FSIndex nGramIndex = aJCas.getAnnotationIndex(NGram.type);
    FSIndex nGram2Index = aJCas.getAnnotationIndex(NGram.type);
    FSIndex questionIndex = aJCas.getAnnotationIndex(Question.type);

    Iterator questionIter = questionIndex.iterator();
    while (questionIter.hasNext()) {
      Question question = (Question) questionIter.next();
      // System.out.println("dddd");
      int QuestionEndPosition = question.getEnd();
      // System.out.println(QuestionEndPosition);
      ArrayList<String> questionNGram = new ArrayList<String>();

      Iterator nGramIter = nGramIndex.iterator();
      while (nGramIter.hasNext()) {
        NGram nGram = (NGram) nGramIter.next();
        if (nGram.getEnd() <= QuestionEndPosition) {
          questionNGram.add(nGram.getCoveredText());
        }
        // System.out.println(questionNGram);
      }
      // System.out.println(questionNGram);
      // System.out.println(QuestionEndPosition);

      // ArrayList<String> answerNGram =new ArrayList<String>();
      Iterator answerIter = answerIndex.iterator();
      while (answerIter.hasNext()) {
        Answer answer = (Answer) answerIter.next();
        // System.out.println("cccc");
        // AnswerScore answerscore = new AnswerScore(aJCas);
        // answerscore.setAnswer(answer);
        int AnswerBeginPosition = answer.getBegin();
        int AnswerEndPosition = answer.getEnd();
        // System.out.println("bbbb");

        ArrayList<String> answerNGram = new ArrayList<String>();
        Iterator nGram2Iter = nGram2Index.iterator();
        while (nGram2Iter.hasNext()) {
          NGram nGram2 = (NGram) nGram2Iter.next();
          if (nGram2.getBegin() >= AnswerBeginPosition && nGram2.getEnd() <= AnswerEndPosition) {
            // ArrayList<String> answerNGram =new ArrayList<String>();
            answerNGram.add(nGram2.getCoveredText());
            // System.out.println(answerNGram);
            // System.out.println("aaa");
          }
          // System.out.println("aaa");
          // System.out.println(answerNGram);
          // System.out.println(questionNGram);
          // System.out.println(nGram);
          // System.out.println(answerscore);
        }
        
        int i = questionNGram.size();
        int mother = answerNGram.size();
        int son = 0;
        double score = 0;
        for (int m = 0; m < i; m++) {
          for (int n = 0; n < mother; n++) {
            if (answerNGram.get(n).equals(questionNGram.get(m))) {
              son++;
            }
          }
        }
        
        // System.out.println(score);
        // System.out.println(questionNGram);
        // System.out.println(answerNGram);
        // System.out.println("aaa");
        score = (double) son / (double) mother;
        AnswerScore answerscore = new AnswerScore(aJCas);
        answerscore.setAnswer(answer);
        answerscore.setScore(score);
        answerscore.setBegin(answer.getBegin());
        answerscore.setEnd(answer.getEnd());
        answerscore.setConfidence(1);
        answerscore.setCasProcessorId("AnswerScoreAnnotator");
        answerscore.addToIndexes();
      }

    }
  }
}
