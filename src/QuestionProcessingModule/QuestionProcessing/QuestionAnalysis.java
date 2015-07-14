package QuestionProcessingModule.QuestionProcessing;

import QuestionProcessingModule.QuestionProcessing.Structures.TokensNode;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.trees.Tree;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Mahmoud on 21-Feb-15.
 */
public class QuestionAnalysis {
    private String question;
    private Tree parseTree;
    private Vector<TokensNode> tokens;

    public QuestionAnalysis(String question) {
        this.question = question;
    }

    public Tree parser() {
        this.parseTree = LexicalParser.getInstance().parse(this.question);

        return this.parseTree;
    }


    public Vector<TokensNode> tokenizer ()
    {
        this.tokens = new Vector<TokensNode>();
        List<List<CoreLabel>> NERList = getNER();

        for (List<CoreLabel> sentence : NERList){
            for (CoreLabel wordLabel : sentence) {

                String word = wordLabel.word();
                String tag = this.getTag(word);
                String lemma = this.getLemma(word, tag);
                String nameEntity = wordLabel.get(CoreAnnotations.AnswerAnnotation.class);

                this.tokens.add(new TokensNode(word, lemma, tag, nameEntity));
            }
        }

        return this.tokens;
    }

    private String getLemma (String word, String tag){
        Morphology morphology = new Morphology();

        return morphology.lemma(word, tag);
    }

    private String getTag(String word) {

        for (TaggedWord i : this.parseTree.taggedYield()) {
            if(i.value().equals(word)) {
                return i.tag();
            }
        }

        return null;
    }

    private List<List<CoreLabel>> getNER()
    {
        List<List<CoreLabel>> _3class = NERClassifier.get_3classInstance().classify(this.question);
        List<List<CoreLabel>> _7class = NERClassifier.get_7classInstance().classify(this.question);

        List<List<CoreLabel>> result = new ArrayList<List<CoreLabel>>();

        for (int i=0, i2 = 0; i < _3class.size() && i2 < _7class.size(); i++,i2++)
        {
            List<CoreLabel> sentence = new ArrayList<CoreLabel>();
            for (int j=0, j2 = 0; j < _3class.get(i).size() && j2 < _7class.get(i2).size(); j++,j2++){
                CoreLabel wordLabel = _3class.get(i).get(j);

                String ner3 = _3class.get(i).get(j).get(CoreAnnotations.AnswerAnnotation.class);
                String ner7 = _7class.get(i2).get(j2).get(CoreAnnotations.AnswerAnnotation.class);

                if (ner3.equals("O") && !ner7.equals("O"))
                    wordLabel.set(CoreAnnotations.AnswerAnnotation.class, ner7);
                else if (ner7.equals("O") && !ner3.equals("O"))
                    wordLabel.set(CoreAnnotations.AnswerAnnotation.class, ner3);

                sentence.add(wordLabel);
            }
            result.add(sentence);
        }

        return result;
    }
}
