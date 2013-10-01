package edu.cmu.deiis.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
//import edu.stanford.nlp.ling.Word;
//import edu.stanford.nlp.objectbank.TokenizerFactory;
//import edu.stanford.nlp.process.PTBTokenizer.PTBTokenizerFactory;
//import edu.stanford.nlp.process.Tokenizer;

public class TokenAnnotator extends JCasAnnotator_ImplBase {
  /*
   * regular expression is amazing! I do not regard "does't" as just one token since I 
   * remember that Professor Nyberg metioned that we could count it as two tokens.
   */
  private Pattern mTokenPattern = Pattern.compile("[a-zA-Z]+");

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    String docText = aJCas.getDocumentText();
    // search for token
    Matcher matcher = mTokenPattern.matcher(docText);
    while (matcher.find()) {
      /*
       *One error(or opportunity to reduce unrelated word) may occur if "A" is not a comment but a functional word in a sentence.
       *I think I can use more complicated  regular expression to solve that problem and I'm
       *working on it. Anyway it will not cause problem in this task.
       */
      if (!matcher.group().equals("Q") && !matcher.group().equals("A")) {
        Token token = new Token(aJCas);
        token.setBegin(matcher.start());
        token.setEnd(matcher.end());
        token.setConfidence(1);
        token.setCasProcessorId("TokenAnnotator");
        token.addToIndexes();
      }
    }
  }

}
