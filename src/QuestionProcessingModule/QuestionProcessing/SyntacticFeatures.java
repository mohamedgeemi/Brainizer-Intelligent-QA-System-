package QuestionProcessingModule.QuestionProcessing;

import QuestionProcessingModule.QuestionProcessing.Enums.QUESTION_TYPE;
import edu.stanford.nlp.trees.Tree;

import java.util.Vector;

/**
 * Created by Mahmoud on 22-Feb-15.
 */
public abstract class SyntacticFeatures {
    protected Tree parseTree;
    protected String question;

    public SyntacticFeatures(Tree parseTree, String question) {
        this.parseTree = parseTree;
        this.question = question;
    }

    public abstract QUESTION_TYPE getQuestionType();
    public abstract Vector<String> getHeadWord();
    public abstract boolean isHeadWord();
    public abstract boolean isCategory();
}
