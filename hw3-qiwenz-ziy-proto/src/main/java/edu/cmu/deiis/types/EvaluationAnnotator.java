package edu.cmu.deiis.types;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

/*
 *I do traversal to "Answer" and get the size and the feature "isCorrect" of it.
 *Then do traversal to "AnswerScore" to get the score of each answer and store them in an 
 *double array. Finally I used bubble sort method to rearrange the sequence of the array and record their position 
 *change which is helpful for the final printout.
 */
public class EvaluationAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    FSIndex answerIndex = aJCas.getAnnotationIndex(Answer.type);
    FSIndex questionIndex = aJCas.getAnnotationIndex(Question.type);
    FSIndex answerscoreIndex = aJCas.getAnnotationIndex(AnswerScore.type);

    Iterator questionIter = questionIndex.iterator();
    while (questionIter.hasNext()) {
      Question question = (Question) questionIter.next();
      ArrayList<String> questionstring = new ArrayList<String>();
      questionstring.add(question.getCoveredText());

      int answersize = 0;
      Iterator answerIter = answerIndex.iterator();
      while (answerIter.hasNext()) {
        Answer answer = (Answer) answerIter.next();
        answersize++;
      }
      // System.out.println(answersize++);

      double[] arr = new double[answersize];// to store the score of each answer
      int[] seq = new int[answersize];// to record each answer's position
      int[] judge = new int[answersize];// to store the "isCorrect" for each answer
      for (int j = 0; j < answersize; j++) {
        seq[j] = j;
        judge[j] = 0;
      }
      int i = 0;

      ArrayList<String> answerScoreAnswer = new ArrayList<String>();
      Iterator answerscoreIter = answerscoreIndex.iterator();
      while (answerscoreIter.hasNext()) {
        AnswerScore answerscore = (AnswerScore) answerscoreIter.next();
        arr[i] = answerscore.getScore();

        answerScoreAnswer.add(answerscore.getCoveredText());
        if (answerscore.getAnswer().getIsCorrect()) {
          judge[i] = 1;
        }
        i++;
      }
      
      //bubble sort
      for (int m = 0; m < arr.length - 1; m++) {
        for (int n = 0; n < arr.length - m - 1; n++) {
          if (arr[n] > arr[n + 1]) {
            double temp = arr[n];
            arr[n] = arr[n + 1];
            arr[n + 1] = temp;
            int temp2 = seq[n];
            seq[n] = seq[n + 1];
            seq[n + 1] = temp2;
            int temp3 = judge[n];
            judge[n] = judge[n + 1];
            judge[n + 1] = temp3;
          }
        }
      }

      System.out.println(questionstring);
      for (int a = answersize - 1; a >= 0; a--) {
        System.out.print(judge[a]);
        System.out.print("  ");
        System.out.print(String.format("%.2f", arr[a]));
        System.out.print("  ");
        System.out.println(answerScoreAnswer.get(seq[a]));
      }
      System.out.println();
    }
  }

}
