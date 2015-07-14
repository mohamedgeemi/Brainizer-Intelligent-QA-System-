package QuestionProcessingModule.QuestionProcessing;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

/**
 * Created by Mahmoud on 21-Feb-15.
 */
public class LexicalParser {
    private static final String grammarPath = System.getProperty("user.dir") + "/stanford-corenlp-full-2015-01-30/stanford-corenlp-3.5.1-models/edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
    private static LexicalizedParser instance = LexicalizedParser.loadModel(grammarPath);

    private LexicalParser() {

    }

    public static LexicalizedParser getInstance() {
        return instance;
    }

}
