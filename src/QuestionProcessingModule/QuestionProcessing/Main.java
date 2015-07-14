package QuestionProcessingModule.QuestionProcessing;

import QuestionProcessingModule.TrainingDataProcessing.TrainingDataConstruction;

public class Main {

    public static void main(String[] args) {
	// write your code here
        TrainingDataConstruction tdc = new TrainingDataConstruction(System.getProperty("user.dir") + "/question-processing-module/QuestionClassification/questionOutputData.txt");
        tdc.parse();
        int x = 0;
    }
}