package QuestionProcessingModule.QuestionProcessing.Enums;

import java.security.PublicKey;

/**
 * Created by Mahmoud & Hani on 21-Feb-15.
 */
public final class REGEX_PATTERNS {

    public static final String NonAlphabetic = "[^\\w- .']";
    public static final String Spaces = "[ ]+";
    public static final String Email = ".*[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+.*";
    public static final String Numerical = "[+-]?\\d*(\\.\\d+)?";
    public static final String WhTag = "[wW][pPrRdD][bBtT]?";
    public static final String GLOSS_SPLITTER_PATTERN = "\"[\\w ]*\"|;";
    public static final String TAG_NOUN_PATTERN = "^NN\\w*";
    public static final String TAG_ADJECTIVE_PATTERN = "^JJ\\w*";
    public static final String TAG_ADVERB_PATTERN = "^RB\\w*";
    public static final String TAG_VERB_PATTERN = "^VB\\w*";


}
