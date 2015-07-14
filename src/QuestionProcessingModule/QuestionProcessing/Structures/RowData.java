package QuestionProcessingModule.QuestionProcessing.Structures;

import java.util.Vector;

/**
 * Created by Mahmoud on 02-Mar-15.
 */
public class RowData {
    public int index;
    public String questionType;
    public String category;
    public String headWord;
    public Vector<String> hypernyms;

    public RowData() {
        hypernyms = new Vector<String>();
    }
}
