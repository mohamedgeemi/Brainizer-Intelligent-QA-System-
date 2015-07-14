package QuestionProcessingModule.QuestionProcessing;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetTree;
import net.didion.jwnl.dictionary.Dictionary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Mahmoud on 23-Feb-15.
 */
public class WordNetUtilities {
    private static final String PROPERTIES_FILE = "file_properties.xml";
    private static boolean initialized = false;

    private WordNetUtilities(){}

    public static Dictionary getInstance() {
        if (!initialized) {
            try {
                JWNL.initialize(new FileInputStream(PROPERTIES_FILE));
            } catch (JWNLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            initialized = true;
        }
        return Dictionary.getInstance();
    }

    public static IndexWord lookupIndexWord (POS tag, String lemma)
    {
        IndexWord indexedWord = null;
        try {
            indexedWord = WordNetUtilities.getInstance().lookupIndexWord(tag,lemma);
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return indexedWord;
    }

    public static Vector<Synset> getSenses(POS tag, String lemma)
    {
        Vector<Synset> senses = null;
        IndexWord indexWord = null;
        try {
            indexWord = WordNetUtilities.getInstance().lookupIndexWord(tag, lemma);

            if (indexWord == null)
            {
                // TODO Conversation
                return null;
            }

            senses = new Vector<Synset>(Arrays.asList(indexWord.getSenses()));

        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return senses;
    }

    public static Vector<Synset> getSenses(IndexWord indexWord)
    {
        Vector<Synset> senses = null;
        try {
            senses = new Vector<Synset>(Arrays.asList(indexWord.getSenses()));

        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return senses;
    }

    public static PointerTargetTree getHypernyms(Synset sense, int depth) {

        PointerTargetTree hypernymsTree = null;
        try {
            hypernymsTree = PointerUtils.getInstance().getHypernymTree(sense, 6);
        } catch (JWNLException e) {
            e.printStackTrace();
        }
        return hypernymsTree;
    }


}
