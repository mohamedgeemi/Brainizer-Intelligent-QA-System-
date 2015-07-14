package QuestionProcessingModule.Classifiers;

import QuestionProcessingModule.QuestionProcessing.Enums.QUESTION_TYPE;
import QuestionProcessingModule.QuestionProcessing.Structures.RowData;
import QuestionProcessingModule.TrainingDataProcessing.TrainingDataCollection;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by ENG.AHMED HANI on 3/11/2015.
 */
public class FeaturesBinarization {
    public final int NUM_OF_FEATURES = 2;
    public int NUM_OF_ClASSES;
    //private TrainingDataCollection trainingDataCollection;

    private Vector<HashSet<String>> questionType;
    private Vector<HashSet<String>> headWords;

    Writer wr = null;

    public FeaturesBinarization(TrainingDataCollection trainingDataCollection) {
        this.init(trainingDataCollection);
    }

    private void init(TrainingDataCollection trainingDataCollection)
    {

        try {
            wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("fetures.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.questionType = new Vector<>();
        this.headWords = new Vector<>();

        NUM_OF_ClASSES = trainingDataCollection.getQuestionsClasses().size();

        for (int i = 0; i < NUM_OF_ClASSES; ++i)
        {
            HashSet<String> questionType = new HashSet<>();
            HashSet<String> headWords = new HashSet<>();
            for (int j = 0; j < trainingDataCollection.getFeatures().elementAt(i).size(); ++j)
            {
                questionType.add(trainingDataCollection.getFeatures().elementAt(i).elementAt(j).questionType);
                headWords.add(trainingDataCollection.getFeatures().elementAt(i).elementAt(j).headWord);
            }
            this.questionType.add(questionType);
            this.headWords.add(headWords);
        }
    }

    public Vector<int[]> binarize(RowData data) {

        Vector<int[]> binaryFeatures = new Vector<>();

        for (int i = 0; i < NUM_OF_ClASSES; ++i) {
            int[] features = new int[NUM_OF_FEATURES];
            Arrays.fill(features, 0);

            String questionType = data.questionType;
            if (this.questionType.elementAt(i).contains(questionType))
                this.setQuestionTypeFeature(features, questionType);

            String headWord = data.headWord;
            if (this.headWords.elementAt(i).contains(headWord))
                features[1] = 1;
            //double averageSimilarity = 0.0;

            //for (RowData iRowData : )

            try {



            for (int j = 0; j < NUM_OF_FEATURES; ++j)
                wr.write(features[j] +  " ");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            binaryFeatures.add(features);
        }
        try {
            wr.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return binaryFeatures;
    }

    private void setQuestionTypeFeature(int[] binaryFeatures, String questionType) {
        if (questionType.equals(QUESTION_TYPE.WHAT.name())) {
            binaryFeatures[0] = 1;
        }
        else if (questionType.equals(QUESTION_TYPE.WHERE.name())) {
            binaryFeatures[0] = 2;
        }
        else if (questionType.equals(QUESTION_TYPE.WHO.name())) {
            binaryFeatures[0] = 3;
        }
        else if (questionType.equals(QUESTION_TYPE.WHEN.name())) {
            binaryFeatures[0] = 4;
        }
        else if (questionType.equals(QUESTION_TYPE.WHICH.name())) {
            binaryFeatures[0] = 5;
        }
        else if (questionType.equals(QUESTION_TYPE.WHY.name())) {
            binaryFeatures[0] = 6;
        }
        else if (questionType.equals(QUESTION_TYPE.HOW.name())) {
            binaryFeatures[0] = 7;
        }
    }
}
