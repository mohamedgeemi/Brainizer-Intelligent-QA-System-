package AnswerProcessingModule;


import InformationRetrievalEngine.IR_Package.RankedRecord;
import InformationRetrievalEngine.IR_Package.WordObject;

import java.util.*;
import java.io.*;


/**
 * Created by Mohamed on 08/06/2015.
 */
public class AnswerExtractor {

    // Extract All the Named Entities in the passage that match the same answer type

    public static List<WordObject> ExtractMatchedEntities(String passage, List<WordObject> Entities, WordObject Answer)
    {
        String[] LOW = passage.split(" ");


        List<WordObject> ListOfWords = new ArrayList<WordObject>();
        WordObject[] WordClasses = new WordObject[LOW.length];

        for (int i =0 ; i< LOW.length; i++){

            WordClasses[i] = new WordObject(LOW[i]);
            ListOfWords.add(WordClasses[i]);
        }

        for (WordObject i : ListOfWords){

            if( i.getNamesEntity() == Answer.getNamesEntity()){
                Entities.add(i);
            }
        }
        return Entities;
    }

    public static WordObject Decision_Maker(List<RankedRecord> passages, List<WordObject> Entities,
                                            String QuestionType , String ExpectedAnswerType )
    {

        Vector<WordObject> Answer;


        for (RankedRecord i : passages){
            for( WordObject j : Entities){

                if (QuestionType == "When"){

                }



            }
        }
        return Answer;
    }


}
