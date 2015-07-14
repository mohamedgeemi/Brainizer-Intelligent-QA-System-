package QuestionProcessingModule.QuestionProcessing;

import edu.stanford.nlp.ie.crf.CRFClassifier;

/**
 * Created by Mahmoud on 21-Feb-15.
 */
public class NERClassifier {
    private static final String _3classGrammarPath = System.getProperty("user.dir") + "/stanford-corenlp-full-2015-01-30/stanford-corenlp-3.5.1-models/edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz";
    private static final String _7classGrammarPath = System.getProperty("user.dir") + "/stanford-corenlp-full-2015-01-30/stanford-corenlp-3.5.1-models/edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";
    private static CRFClassifier _3classInstance = CRFClassifier.getClassifierNoExceptions(_3classGrammarPath);
    private static CRFClassifier _7classInstance = CRFClassifier.getClassifierNoExceptions(_7classGrammarPath);

    private NERClassifier() {

    }

    public static CRFClassifier get_3classInstance() {
        return _3classInstance;
    }

    public static CRFClassifier get_7classInstance() {
        return _7classInstance;
    }
}
