package InformationRetrievalEngine;

import InformationRetrievalEngine.IR_Package.IR;
import InformationRetrievalEngine.IR_Package.RankedRecord;

import java.util.Vector;

/**
 * Created by mahmoud-hatem on 6/9/15.
 */
public class InformationRetrieval {

    private static InformationRetrieval instance;

    private String document;
    private IR IREngine;

    private InformationRetrieval()
    {
    }

    public static InformationRetrieval getInstance() {
        if (instance == null)
            instance = new InformationRetrieval();

        return instance;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Vector<RankedRecord> getRelevantSentences (Vector<String> keywords)
    {
        this.IREngine = new IR(this.document);

        return  this.IREngine.GetSentencesWhichHaveWords(keywords);
    }

}
