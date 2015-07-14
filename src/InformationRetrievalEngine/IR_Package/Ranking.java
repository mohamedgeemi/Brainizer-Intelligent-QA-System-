package InformationRetrievalEngine.IR_Package;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Mohamed on 3/23/2015.
 */
public class Ranking {
    private int TopRankedCounter = 20;
    private Vector<String> SomeSentences, SomeWords;
    public Ranking(Vector<String> SomeSentences, Vector<String> SomeWords)
    {
        this.SomeSentences=SomeSentences;
        this.SomeWords=SomeWords;
    }
    public Vector<RankedRecord> SentencesWithRanked()
    {

        Vector<RankedRecord> RankedSentences = new Vector<RankedRecord>();
        //
        //RankedSentences = SomeSentences;
        //1st way "How many"
        SortedMap<Double, Vector<String> > TopSentences = ByHowMany();
        //2nd way "Distance"
        RankedSentences = ByDistances(TopSentences);
        //

        return RankedSentences;
    }

    private SortedMap<Double, Vector<String> > ByHowMany()
    {
        SortedMap<Double, Vector<String> > Ranked = new TreeMap<Double, Vector<String>>();

        for(int Index = 0 ; Index < SomeSentences.size() ; Index++)
        {
            Vector<Integer> Distances = new Vector<Integer>();
            Map<String, Vector<Integer> >WordsLocations = new HashMap<String, Vector<Integer>>();

            String Word = "";
            String OneSentence = SomeSentences.elementAt(Index);
            String[] Tokens = OneSentence.split("[, !.?:;]");
            for(int LocalIndex = 0 ; LocalIndex < Tokens.length ; LocalIndex++)
            {
                Vector<Integer> ValuesMap = WordsLocations.get(Tokens[LocalIndex]);
                if (ValuesMap != null)
                {
                    ValuesMap.add(LocalIndex);
                    WordsLocations.put(Tokens[LocalIndex],ValuesMap);
                }
                else
                {
                    ValuesMap = new Vector<Integer>();
                    ValuesMap.add(LocalIndex);
                    WordsLocations.put(Tokens[LocalIndex],ValuesMap);
                }
            }
            //
            for(int LocalIndex = 0 ; LocalIndex < SomeWords.size() ; LocalIndex++)
            {
                Vector<Integer> ValuesMap = WordsLocations.get(SomeWords.elementAt(LocalIndex));
                if (ValuesMap != null)
                {
                    Distances.add(ValuesMap.elementAt(0));
                }
            }
            double TotalDistances = 0;

            //Sort Distances
            Collections.sort(Distances);
            //
            TotalDistances = SomeWords.size() - Distances.size();

            Vector<String> ValuesRanked = Ranked.get(TotalDistances);
            if (ValuesRanked != null)
            {
                ValuesRanked.add(SomeSentences.elementAt(Index));
                Ranked.put(TotalDistances,ValuesRanked);
            }
            else
            {
                ValuesRanked = new Vector<String>();
                ValuesRanked.add(SomeSentences.elementAt(Index));
                Ranked.put(TotalDistances,ValuesRanked);
            }
        }

        return Ranked;
    }

    private Vector<RankedRecord> ByDistances(SortedMap<Double, Vector<String> > Ranked)
    {
        Vector<RankedRecord> RankedSentences = new Vector<RankedRecord>();
        SortedMap<Double, Vector<String> > NewRanked = new TreeMap<Double, Vector<String>>();
        int counter=0;
        for(double Key : Ranked.keySet())
        {
            Vector<String> ValuesRanked = Ranked.get(Key);
            counter+=ValuesRanked.size();
            for (int Index = 0; Index < ValuesRanked.size(); ++Index)
            {
                Vector<Integer> Distances = new Vector<Integer>();
                Map<String, Vector<Integer>> WordsLocations = new HashMap<String, Vector<Integer>>();

                String Word = "";
                String OneSentence = ValuesRanked.elementAt(Index);
                String[] Tokens = OneSentence.split("[, !.?:;]");
                for (int LocalIndex = 0; LocalIndex < Tokens.length; LocalIndex++) {
                    Vector<Integer> ValuesMap = WordsLocations.get(Tokens[LocalIndex]);
                    if (ValuesMap != null) {
                        ValuesMap.add(LocalIndex);
                        WordsLocations.put(Tokens[LocalIndex], ValuesMap);
                    } else {
                        ValuesMap = new Vector<Integer>();
                        ValuesMap.add(LocalIndex);
                        WordsLocations.put(Tokens[LocalIndex], ValuesMap);
                    }
                }
                //
                for (int LocalIndex = 0; LocalIndex < SomeWords.size(); LocalIndex++) {
                    Vector<Integer> ValuesMap = WordsLocations.get(SomeWords.elementAt(LocalIndex));
                    if (ValuesMap != null) {
                        Distances.add(ValuesMap.elementAt(0));
                    }
                }
                double TotalDistances = 100.00;

                //Sort Distances
                Collections.sort(Distances);
                //
                TotalDistances -= ((double)(SomeWords.size() - Distances.size())/(double)(SomeWords.size()))*100.00;
                if (Distances.size() > 0)
                    TotalDistances -= Distances.elementAt(0) * 0.1;
                for (int LocalIndex = 0; LocalIndex < Distances.size() - 1; LocalIndex++) {
                    TotalDistances -= Distances.elementAt(LocalIndex + 1) * 0.1;
                    TotalDistances -= Distances.elementAt(LocalIndex + 1) - Distances.elementAt(LocalIndex) - 1;
                }
                TotalDistances = 100-TotalDistances;
                Vector<String> NewValuesRanked = Ranked.get(TotalDistances);
                if (NewValuesRanked != null) {
                    NewValuesRanked.add(ValuesRanked.elementAt(Index));
                    NewRanked.put(TotalDistances, NewValuesRanked);
                } else {
                    NewValuesRanked = new Vector<String>();
                    NewValuesRanked.add(ValuesRanked.elementAt(Index));
                    NewRanked.put(TotalDistances, NewValuesRanked);
                }
                if(counter>=TopRankedCounter)
                    break;
            }

        }

        int ID=0;
        for(double Key : NewRanked.keySet())
        {
            Vector<String> ValuesRanked = NewRanked.get(Key);

            for(int LocalIndex = 0 ; LocalIndex < ValuesRanked.size() ; LocalIndex++)
            {
                RankedRecord RR = new RankedRecord();
                RR.ID = ID++;
                RR.Rank= 100.00-Key;
                RR.Sentence=ValuesRanked.elementAt(LocalIndex);
                String[] Tokens = RR.Sentence.split("[, !.?:;]");
                Vector<WordObject> WordList = new Vector<WordObject>();
                int index=0;
                for(String word : Tokens)
                {
                    WordObject WordObj = new WordObject();
                    WordObj.Word=word;
                    WordObj.Position=index++;
                    WordObj.Type = "";
                    WordList.add(WordObj);
                }
                RR.Words=WordList;
                RankedSentences.add(RR);
            }
        }

        return  RankedSentences;
    }
}
