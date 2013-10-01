package edu.cmu.deiis.types;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
/**
 * I used Professor Nyberg's method to achieve NGram. His code could be seen in the class 
 * PDF "02 Intelligent Info Systems", I made some changes to make his code work for mine.
 */

public class NGramAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    AnnotationIndex<?> index = jcas.getAnnotationIndex(Token.type);
    Token[] array = new Token[index.size()];
    FSIterator<?> iterator = index.iterator();

    for (int i = 0; i < index.size(); i++)
      array[i] = (Token) iterator.next();

    for (int n = 1; n <= 3; n++) {
      int lastNGramStart = index.size() - n + 1;

      for (int offset = 0; offset < lastNGramStart; offset++) {
        FSArray elementsArray = new FSArray(jcas, n);
        for (int i = 0; i < n; i++)
          elementsArray.set(i, array[offset + i]);

        /**
         *To define the boundary. To judge whether the word is within one row;
         *I discussed with Yi Song about how to define the boundary and his solution 
         *is much easier. So I used his method to solve this problem.
         */
        Boolean DEBUG = true;
        for (int i = 0; i < n - 1; i++)
          if ((array[offset + i + 1].getBegin() - array[offset + i].getEnd()) != 1)
            DEBUG = false;

        if (DEBUG) {
          NGram ngram = new NGram(jcas, array[offset].getBegin(), array[offset + n - 1].getEnd());
          ngram.setElementType("Token");
          ngram.setElements(elementsArray);
          ngram.setCasProcessorId("NGramAnnotator");
          ngram.setConfidence(1.0d);
          ngram.addToIndexes();
          // System.out.println(a);
          // System.out.println(a);
        }
      }
    }
  }
}
