package QuestionProcessingModule.QuestionProcessing.Structures;

/**
 * Created by Mahmoud on 21-Feb-15.
 */
public class TokensNode {
    public String word;
    public String lemma;
    public String tag;
    public String namedEntity;

    public TokensNode(String word, String lemma, String tag, String namedEntity) {
        this.word = word;
        this.lemma = lemma;
        this.tag = tag;
        this.namedEntity = namedEntity;
    }
}
