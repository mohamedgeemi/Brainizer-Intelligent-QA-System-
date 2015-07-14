package QuestionProcessingModule.QuestionProcessing.Enums;


import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by Mahmoud on 22-Feb-15.
 */
public class QUESTION_CATEGORY {

    //TODO Complete Categories

    public static final String ABBR_ABB = "ABBR_abb";
    public static final String ABBR_EXP = "ABBR_exp";

    public static final String DESC_DEF = "DESC:def";
    public static final String DESC_REASON = "DESC:reason";
    public static final String DESC_DESC = "DESC:desc";

    public static final String ENTY_SUBSTANCE = "ENTY:substance";
    public static final String ENTY_TERM = "ENTY:termeq";

    public static final String HUM_DESC = "HUM:desc";


    public static Dictionary<String, Vector<String>> Patterns = new Hashtable<String, Vector<String>>(){{
        put(QUESTION_CATEGORY.DESC_DEF, new Vector<String>(){{
            add("(what) (is|are) ?(a|an|the)? \\w*( \\w*)?");
            add("(what) (do|does) (\\w* )+(mean)");
        }});

        put(QUESTION_CATEGORY.DESC_DESC, new Vector<String>() {{
            add("what does (\\w* )+do");
        }});

        put(QUESTION_CATEGORY.DESC_REASON, new Vector<String>() {{
            add("what (cause|causes) (\\w* ?)+");
            add("what (is|are) (\\w* )+used for");
        }});

        put(QUESTION_CATEGORY.ENTY_SUBSTANCE, new Vector<String>() {{
            add("what (is|are) (\\w* )+(composed of|made of|made out of)");
        }});

        put(QUESTION_CATEGORY.ENTY_TERM, new Vector<String>() {{
            add("what do you call (\\w* ?)+");
        }});

        put(QUESTION_CATEGORY.ABBR_EXP, new Vector<String>() {{
            add("what (does|do) (\\w* )+stand for ");
        }});

        put(QUESTION_CATEGORY.HUM_DESC, new Vector<String>() {{
            add("who (is|was) [A-Z](\\w* ?)+");
        }});

    }};


}
