package QuestionProcessingModule.QuestionProcessing;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.Lin;

/**
 * Created by ENG.AHMED HANI on 3/10/2015.
 */
public class WordsSimilarity {
    private static ILexicalDatabase database = new NictWordNet();
    private static RelatednessCalculator lin = new Lin(database);
    private static WordsSimilarity ourInstance = new WordsSimilarity();

    public static WordsSimilarity getInstance() {
        return ourInstance;
    }

    private WordsSimilarity() {

    }

    public double getSimilarity(String first, String second) {
        return lin.calcRelatednessOfWords(first, second);
    }
}
