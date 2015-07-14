package QuestionProcessingModule.QuestionProcessing;

import QuestionProcessingModule.QuestionProcessing.Enums.QUESTION_CATEGORY;
import QuestionProcessingModule.QuestionProcessing.Enums.QUESTION_TYPE;
import QuestionProcessingModule.QuestionProcessing.Enums.REGEX_PATTERNS;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.*;

import java.util.*;

/**
 * Created by Mahmoud on 22-Feb-15.
 */
public class SyntacticFeaturesImplementer extends SyntacticFeatures {

    private QUESTION_TYPE questionType;
    private boolean isWhenWhereWhy;
    private Vector<String> headWords;
    private boolean hasHeadWord;
    private boolean hasCategory;
    private QUESTION_CATEGORY questionCategory;



    public SyntacticFeaturesImplementer(Tree parseTree, String question) {
        super(parseTree, question);
        this.headWords = new Vector<String>();
        this.hasHeadWord = false;
        this.hasCategory = false;
    }

    @Override
    public QUESTION_TYPE getQuestionType() {
        boolean isWH = false;
        ArrayList<TaggedWord> current = this.parseTree.taggedYield();
        for (TaggedWord word : current) {
            String tag = word.tag();
            if (tag.matches(REGEX_PATTERNS.WhTag)){
                this.questionType = QUESTION_TYPE.valueOf(word.value().toUpperCase());
                isWH = true;
                break;
            }
        }
        if (isWH == false){
            this.questionType = QUESTION_TYPE.OTHER;
        }

        return this.questionType;
    }

    @Override
    public Vector<String> getHeadWord() {
        if (isWhenWhereWhy()) {
            return null;
        }

        if (this.questionType == QUESTION_TYPE.HOW){
            List<Tree> words = this.parseTree.getLeaves();

            for (int i = 0; i < words.size(); i++){
                if (words.get(i).value().matches("[hH][oO][wW]") && i != words.size() - 1){
                    if (this.isAuxWord(words.get(i + 1).value())) {
                        String dep = words.get(i + 1).value();
                        String gov = this.getDependentWord(dep);
                        this.headWords.add(gov);
                        break;
                    } else {
                        this.headWords.add(words.get(i + 1).value());
                        break;
                    }

                }
            }
            this.hasHeadWord = true;
            return this.headWords;
        }
        // what and who patterns
        if (this.questionType != QUESTION_TYPE.OTHER) {

            Enumeration<String> iterator = QUESTION_CATEGORY.Patterns.keys();
            String lowerCaseQuestion = this.question.toLowerCase();
            String caseSensitiveQuestion;

            while(iterator.hasMoreElements()) {
                String key = iterator.nextElement();
                Vector<String> patterns = QUESTION_CATEGORY.Patterns.get(key);

                if (key.equals(QUESTION_CATEGORY.HUM_DESC)){
                    caseSensitiveQuestion = this.question;
                }
                else
                    caseSensitiveQuestion = lowerCaseQuestion;

                for (String pattern : patterns) {

                    if (caseSensitiveQuestion.matches(pattern)) {
                        this.headWords.add(key);
                        this.hasCategory = true;
                        return this.headWords;
                    }
                }
            }
        }

        this.hasHeadWord = true;

        this.headWords = this.getHeadWords_auxiliary(this.parseTree, new SemanticHeadFinder(true));

        ArrayList<TaggedWord> tokens = this.parseTree.taggedYield();

        TaggedWord firstNoun = null;
        TaggedWord firstHead = null;
        for (TaggedWord token: tokens){
            if (firstNoun == null && (token.tag().contains("NN") ||token.tag().contains("FW")) ) {
                firstNoun = token;
            }
            if (firstHead == null && token.value().equals(this.headWords.get(0))) {
                firstHead = token;
            }
        }

        if (!firstHead.tag().contains("NN") || !firstHead.tag().contains("FW")) {
            this.headWords.set(0, firstNoun.value());
        }

        return this.headWords;
    }

    private Vector<String> getHeadWords_auxiliary (Tree tree, HeadFinder headFinder)
    {
        return this.headWords = dfs(tree,null,headFinder);
    }

    private Vector<String> dfs (Tree node, Tree parent, HeadFinder headFinder)
    {
        if (node == null || node.isLeaf()) {
            return null;
        }

        if (node.value().equals("NP") || node.value().equals("NN")) {
            Vector<String> tmp = new Vector<String>();
            tmp.add(node.headTerminal(headFinder, parent).toString());

            return tmp;
        }

        Vector<String> head = new Vector<String>();

        for(Tree child : node.children()) {
            Vector<String> childHead = this.dfs(child, node, headFinder);

            if (childHead != null) {
                head.addAll(childHead);
            }
        }

        return head;
    }

    private boolean isWhenWhereWhy() {
        if (this.questionType == QUESTION_TYPE.WHEN ||
                this.questionType == QUESTION_TYPE.WHERE ||
                this.questionType == QUESTION_TYPE.WHY)
            return this.isWhenWhereWhy = true;

        return this.isWhenWhereWhy = false;
    }
    @Override
    public boolean isHeadWord() {
        return this.hasHeadWord;
    }

    @Override
    public boolean isCategory() {
        return this.hasCategory;
    }

    private String getDependentWord(String word) {
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(this.parseTree);
        Collection<TypedDependency> td = gs.typedDependencies();


        TypedDependency typedDependency;
        Object[] obj = td.toArray();

        for (Object i : obj) {
            typedDependency = (TypedDependency) i;
            if (typedDependency.reln().getShortName().equals("aux")) {
                return typedDependency.gov().value();
            }
        }

        return null;
    }

    private boolean isAuxWord(String word) {
        if (word.equals("do") || word.equals("does") || word.equals("did")) {
            return true;
        }

        if (word.equals("can") || word.equals("could") || word.equals("shall") || word.equals("should")) {
            return true;
        }

        return false;
    }
}
