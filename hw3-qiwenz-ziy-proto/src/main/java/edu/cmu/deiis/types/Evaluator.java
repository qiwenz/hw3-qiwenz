package edu.cmu.deiis.types;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceProcessException;

public class Evaluator extends CasConsumer_ImplBase {

  // @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {
    // TODO Auto-generated method stub
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    // aJCas
    // String docText = jcas.getDocumentText();
    FSIndex answerIndex = jcas.getAnnotationIndex(Answer.type);
    FSIndex questionIndex = jcas.getAnnotationIndex(Question.type);
    FSIndex answerscoreIndex = jcas.getAnnotationIndex(AnswerScore.type);

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

      // bubble sort
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
