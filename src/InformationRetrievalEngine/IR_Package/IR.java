package InformationRetrievalEngine.IR_Package;

import java.util.Vector;

/**
 * Created by Mohamed on 3/21/2015.
 */
public class IR
{
    private Indexing DataIndexing;

    public IR(String InputData)
    {
        DataIndexing = new Indexing(InputData);
    }

    public Vector<RankedRecord> GetSentencesWhichHaveWords(Vector<String> Words)
    {
        Vector<String> SomeSentences = DataIndexing.GetSentencesWhichHaveWords(Words);
        Ranking RankedSentences = new Ranking(SomeSentences, Words);
        return RankedSentences.SentencesWithRanked();
    }
}
