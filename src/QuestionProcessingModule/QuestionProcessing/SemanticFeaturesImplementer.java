package QuestionProcessingModule.QuestionProcessing;

import QuestionProcessingModule.QuestionProcessing.Enums.GRAM_TYPE;
import QuestionProcessingModule.QuestionProcessing.Enums.REGEX_PATTERNS;
import QuestionProcessingModule.QuestionProcessing.Enums.SHAPE;
import QuestionProcessingModule.QuestionProcessing.Structures.TokensNode;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.patterns.surface.Token;
import javafx.geometry.Pos;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.list.PointerTargetTree;
import net.didion.jwnl.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by Mahmoud on 23-Feb-15.
 */
public class SemanticFeaturesImplementer extends SemanticFeatures {
    public SemanticFeaturesImplementer(Vector<String> headWords, Vector<TokensNode> tokensNodes) {
        super(headWords, tokensNodes);
    }

    @Override
    public Synset getBestSense() {
        TokensNode headWordToken = this.find(this.headWords.get(0));
        POS headWordTokenTag = this.wordPOS(headWordToken.tag);
        Vector<Synset> wordSenses = this.getSenses(headWordTokenTag, headWordToken);


        int count = 0;
        int maxCount = -1;
        Synset optimalSense = null;

        for(Synset sense : wordSenses) {
            count = 0;

            for(TokensNode token : this.tokensNodes) {
                if (token.word.equals(headWordToken.word))
                    continue;

                POS tokenTag = this.wordPOS(token.tag);
                if (tokenTag == null)
                    continue;

                String senseDefinition = sense.getGloss();
                senseDefinition = Utilities.senseDefinition(senseDefinition);

                int subMax = 0;

                Vector<Synset> senses = this.getSenses(tokenTag, token);

                for (Synset wordSense : senses) {
                    String wordSenseDefinition = wordSense.getGloss();
                    wordSenseDefinition = Utilities.senseDefinition(wordSenseDefinition);

                    int currentMax = Utilities.numberOfMatchedWords(senseDefinition, wordSenseDefinition);

                    subMax = Math.max(subMax, currentMax);
                }

                count = count + subMax;
            }

            if (count > maxCount) {
                maxCount = count;
                optimalSense = sense;
            }
        }

        return optimalSense;
    }

    @Override
    public Vector<String> getHypernyms(Synset sense) {
        PointerTargetTree hypernyms = WordNetUtilities.getHypernyms(sense, 6);
        ArrayList<PointerTargetNodeList> allHypernyms =  (ArrayList<PointerTargetNodeList>)hypernyms.toList();

        if (allHypernyms.isEmpty())
            return null;

        PointerTargetNodeList version = allHypernyms.get(0);
        PointerTargetNode[] nodes = Arrays.copyOf(version.toArray(), version.toArray().length, PointerTargetNode[].class);

        Vector<String> hypernymsLemmas = new Vector<String>();

        for (int i = 0; i < nodes.length; i++){
            hypernymsLemmas.add(nodes[i].getSynset().getWord(0).getLemma());
        }

        return hypernymsLemmas;
    }

    @Override
    public GRAM_TYPE getBestGram() {
        return null;
    }

    @Override
    public SHAPE getQuestionShape() {
        return null;
    }

    private TokensNode find(String word) {
        for (TokensNode token : this.tokensNodes) {
            if (token.word.equals(word)) {
                return token;
            }
        }

        return null;
    }

    private POS wordPOS(String tag) {
        if (tag.matches(REGEX_PATTERNS.TAG_ADJECTIVE_PATTERN)) {
            return POS.ADJECTIVE;
        }

        else if (tag.matches(REGEX_PATTERNS.TAG_NOUN_PATTERN)) {
            return POS.NOUN;
        }

        else if (tag.matches(REGEX_PATTERNS.TAG_ADVERB_PATTERN)) {
            return POS.ADVERB;
        }

        else if (tag.matches(REGEX_PATTERNS.TAG_VERB_PATTERN)) {
            return POS.VERB;
        }
        else if (tag.equals("FW"))
            return POS.NOUN;
        else
            return null;
    }

    private Vector<Synset> getSenses(POS tag, TokensNode token)
    {
        Vector<Synset> senses = WordNetUtilities.getSenses(tag, token.lemma);

        if (senses == null && !token.namedEntity.equals("O"))
            senses = WordNetUtilities.getSenses(tag, token.namedEntity);
        if (senses == null) {
            //TODO Conversation
            senses = new Vector<Synset>();
            System.out.println(token.lemma + " unknown");
        }

        return senses;
    }
}
