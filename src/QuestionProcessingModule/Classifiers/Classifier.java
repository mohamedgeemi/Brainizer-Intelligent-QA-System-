package QuestionProcessingModule.Classifiers;

import QuestionProcessingModule.QuestionProcessing.Structures.RowData;
import QuestionProcessingModule.TrainingDataProcessing.TrainingDataCollection;

/**
 * Created by ENG.AHMED HANI on 3/11/2015.
 */
public abstract class Classifier {
    protected TrainingDataCollection trainingDataCollection;

    public Classifier(TrainingDataCollection trainingDataCollection) {
        this.trainingDataCollection = trainingDataCollection;
    }

    //public abstract String classify(RowData features);
}
