package InformationRetrievalEngine.IR_Package;

import QuestionProcessingModule.QuestionProcessing.QuestionAnalysis;
import QuestionProcessingModule.QuestionProcessing.Structures.TokensNode;

import javax.lang.model.element.Name;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Vector;

/**
 * Created by Mohamed on 6/8/2015.
 */

public class WordObject {
    public String Word;
    public int Position;
    public String NamedEntity;
    QuestionAnalysis NERGetter;
    boolean IsDigit;

    public WordObject (String WordString){

        Word = WordString;
        Position = 0;
        NERGetter = new QuestionAnalysis(Word);
        NERGetter.parser();
        Vector<TokensNode> myTokens = NERGetter.tokenizer();
        NamedEntity = myTokens.get(0).namedEntity;
        IsDigit = false;
    }

    public String getWord(){
        return Word;
    }

    public String getNamesEntity(){
        return NamedEntity;
    }
    public boolean DIGITorNOT(){
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(Word, pos);
        return Word.length() == pos.getIndex();
    }

}
