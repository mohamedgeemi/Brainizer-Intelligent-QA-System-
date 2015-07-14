package InformationRetrievalEngine.IR_Package;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Mohamed on 3/22/2015.
 */
public class Indexing {

    private Vector<String> Sentences;
    private Hashtable<String, Set<Integer> > WordsTable;

    public Indexing(String InputData)
    {
        PrepareSentences(InputData);
        PrepareWordsTable();
    }

    private void PrepareSentences(String Data)
    {
        Data = Data.toLowerCase();
        Sentences = new Vector<String>();
        String OneSentence = "";
        for(int index = 0 ; index < Data.length() ; index++)
        {
            OneSentence += Data.charAt(index);
            if(Data.charAt(index) == '.')
            {
                //Clear Sentence
                FormattedSentence Sentence = new FormattedSentence();
                OneSentence = Sentence.format(OneSentence);
                //
                if(!OneSentence.isEmpty())
                    Sentences.addElement(OneSentence);
                OneSentence = "";
            }
        }
    }

    private void PrepareWordsTable()
    {
        WordsTable = new Hashtable<String, Set<Integer>>();
        for(int Index = 0 ; Index < Sentences.size() ; Index++)
        {
            String Word = "";
            String OneSentence = Sentences.elementAt(Index);
            String[] Tokens = OneSentence.split("[, !.?:;]");
            for(int LocalIndex = 0 ; LocalIndex < Tokens.length ; LocalIndex++)
            {
                Set<Integer> ValuesSet = WordsTable.get(Tokens[LocalIndex]);
                if (ValuesSet != null)
                {
                    ValuesSet.add(Index);
                    WordsTable.put(Tokens[LocalIndex],ValuesSet);
                }
                else
                {
                    ValuesSet = new HashSet<Integer>();
                    ValuesSet.add(Index);
                    WordsTable.put(Tokens[LocalIndex],ValuesSet);
                }

            }
        }
    }

    public Vector<String> GetSentencesWhichHaveWords(Vector<String> Words)
    {
        Vector<String> SomeSentences = new Vector<String>();
        Set<Integer> ValuesSets = new HashSet<Integer>();
        for(int Index = 0 ; Index < Words.size() ; Index++)
        {
            String OneWord = Words.elementAt(Index).toLowerCase();
            Set<Integer> ValuesSet = WordsTable.get(OneWord);
            if(ValuesSet!=null)
            {
                for (Integer SentenceIndex : ValuesSet)
                {
                    ValuesSets.add(SentenceIndex);
                }
            }
        }
        for (Integer SentenceIndex : ValuesSets)
        {
            SomeSentences.addElement(Sentences.elementAt(SentenceIndex));
        }

        return SomeSentences;
    }
}
