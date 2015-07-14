package QuestionProcessingModule.TrainingDataProcessing;

import QuestionProcessingModule.QuestionProcessing.Structures.RowData;
import QuestionProcessingModule.QuestionProcessing.Structures.StatisticalData;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by ENG.AHMED HANI on 3/9/2015.
 */
public class TrainingDataCollection {
    HashMap<String, Integer> questionsClasses;
    Vector<Vector<RowData>> features;
    Vector<StatisticalData> classesStatisticalData;

    public TrainingDataCollection() {
        this.questionsClasses = new HashMap<String, Integer>();
        this.features = new Vector<Vector<RowData>>();
        this.classesStatisticalData = new Vector<StatisticalData>();
    }

    public void addFeatureElement(String label, RowData data){
        if (this.questionsClasses.containsKey(label)){
            int id = this.questionsClasses.get(label);
            this.features.get(id).add(data);
        }

        else{
            int id = this.questionsClasses.size();  // 1 base
            this.questionsClasses.put(label, id);
            Vector<RowData> temp = new Vector<RowData>();
            temp.add(data);
            this.features.add(temp);
            this.classesStatisticalData.add(new StatisticalData());
        }
    }
    public void addStatisticalElement(String label, double mue, double sigma){
        if (this.questionsClasses.containsKey(label)){
            int id = this.questionsClasses.get(label);
            this.classesStatisticalData.get(id).mue = mue;
            this.classesStatisticalData.get(id).sigma = sigma;
        }

        else{
            System.out.println("No class with specified label");
        }
    }
    public void addStatisticalElement(int id, double mue, double sigma){
        this.classesStatisticalData.get(id).mue = mue;
        this.classesStatisticalData.get(id).sigma = sigma;
    }

    public HashMap<String, Integer> getQuestionsClasses() {
        return questionsClasses;
    }

    public Vector<Vector<RowData>> getFeatures() {
        return features;
    }
}
