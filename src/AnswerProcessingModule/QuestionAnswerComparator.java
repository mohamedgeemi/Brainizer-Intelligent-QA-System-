package AnswerProcessingModule;

import QuestionProcessingModule.QuestionProcessing.Structures.TokensNode;
import QuestionProcessingModule.QuestionProcessing.QuestionAnalysis;

import java.util.Stack;
import java.util.Vector;

/**
 * Created by ma7modx on 6/7/2015.
 */
public class QuestionAnswerComparator {

    Vector<TokensNode> questionTokens;
    Vector< Vector<TokensNode> > answersTokens;

    Vector<String> ImpQuestionWords;
    Vector< Vector<String> > ImpAnswersWords;

    private static boolean IsFeature(String pos)
    {
        if( pos .equals("NN")  || pos .equals("JJ") || pos .equals("VB") || pos .equals("NNS") || pos .equals("RB") || pos .equals("VBN") || pos .equals("VBD") || pos .equals("VBG") || pos .equals("NNP") || pos .equals("CD"))
            return true ;
        return false ;
    }
    private static int CompareWords (String w1 , String w2)
    {
        w1 = w1.toLowerCase() ;
        w2 = w2.toLowerCase() ;
        // return math.abs(w1 - w2);
        if(w1.equals(w2))
            return 0;
        return 1;

    }
    private static Vector<Integer> CompareSentence(Vector<String> s1 , Vector<String> s2)
    {
        Vector<Integer> distance = new Stack<Integer>();
        for( String s1i : s1)
        {
            int min = 10000 ;
            for (String s2i : s2)
            {
                min = min < CompareWords(s1i , s2i) ? min : CompareWords(s1i , s2i) ;
            }
            distance.add(min);
        }
        return distance;
    }

    public QuestionAnswerComparator(String questionI , Vector<String> answersI)
    {

        QuestionAnalysis question = new QuestionAnalysis(questionI);
        question.parser();

        Vector<QuestionAnalysis> answers= new Vector<QuestionAnalysis>();
        for(int i = 0 ; i < answersI.size() ;++i)
        {
            answers.add(new QuestionAnalysis(answersI.get(i)));
            answers.get(i).parser();
        }

        questionTokens = question.tokenizer();

        answersTokens = new Vector<Vector<TokensNode>>();
        for(int i = 0 ; i < answers.size() ;++i)
        {
            answersTokens.add(answers.get(i).tokenizer());
        }

        ImpQuestionWords = new Vector<String>();
        ImpAnswersWords = new Vector<Vector<String>>();

    }

    private void GetImpWords()
    {
        for(TokensNode i : questionTokens)
        {
            if(IsFeature(i.tag))
                ImpQuestionWords.add(i.word);
        }

        for(int i =0 ; i < answersTokens.size() ; ++i)
        {
            Vector<TokensNode> answer = answersTokens.get(i);
            ImpAnswersWords.add(new Vector<String>());
            for (TokensNode token : answer) {
                if (IsFeature(token.tag) == true)
                    ImpAnswersWords.get(i).add(token.word);
            }
        }
    }
    private Vector<Integer> CalcDistance()
    {
        Vector< Vector <Integer> > Distances = new Stack<Vector<Integer>>() , // between question and answer
                DistancesVersa = new Stack<Vector<Integer>>() ; // vice versa
        for(int i = 0 ; i < ImpAnswersWords.size() ; ++i)
        {
            Vector<String> ImpAnswerWords = ImpAnswersWords.get(i);

            Distances.add(CompareSentence(ImpQuestionWords , ImpAnswerWords));
            DistancesVersa.add(CompareSentence(ImpAnswerWords, ImpQuestionWords));
        }


        Vector<Integer> totalDistance = new Stack<Integer>() ;
        for(int i = 0 ; i < Distances.size() ; ++i)
        {
            totalDistance.add(0);
            for (int j = 0; j < Distances.get(i).size(); ++j)
            {
                int distance = Distances.get(i).get(j);
                totalDistance.set(i, totalDistance.get(i) + distance);
            }
        }

        for(int i = 0 ; i < DistancesVersa.size() ; ++i)
        {
            for (int j = 0; j < DistancesVersa.get(i).size(); ++j)
            {
                int distance = DistancesVersa.get(i).get(j);
                totalDistance.set(i , totalDistance.get(i) + distance);
            }
        }
        return totalDistance;
    }

    private int GetTheMinDistance(Vector<Integer> totalDistance)
    {
        int minDistance = 10000 ;
        int minDistanceIndex = 0 ;
        for(int i = 0 ; i < totalDistance.size() ; ++i)
        {
            if(totalDistance.get(i) < minDistance)
            {
                minDistance = totalDistance.get(i);
                minDistanceIndex = i ;
            }
        }
        return minDistanceIndex;
    }
    public int Execute()
    {
        GetImpWords();
        Vector<Integer> Distance = CalcDistance();
        return  GetTheMinDistance(Distance);
    }
}
