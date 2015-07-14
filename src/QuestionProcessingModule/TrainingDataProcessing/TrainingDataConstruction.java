package QuestionProcessingModule.TrainingDataProcessing;

import QuestionProcessingModule.QuestionProcessing.Structures.RowData;
import QuestionProcessingModule.QuestionProcessing.WordsSimilarity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Mahmoud  on 02-Mar-15.
 */
public class TrainingDataConstruction {
    public TrainingDataCollection trainingDataCollection;
    public HashMap<String, Vector<RowData>> map;
    private String filepath;

    public TrainingDataConstruction(String filePath) {
        this.filepath = filePath;
        this.map = new HashMap<String, Vector<RowData>>();
        this.trainingDataCollection = new TrainingDataCollection();
    }

    public void parse() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(this.filepath));

            int labelIndex = 0;
            int questionTypeIndex = 1;
            int categoryIndex = 2;
            int headWordIndex = 3;
            int hypernymsIndex = 4;

            String line = br.readLine();
            int count = 1;
            int classID = 0;

            while(line != null) {
                String[] tokens = line.split(" ");
                String label = tokens[labelIndex];
                RowData rowData = new RowData();
                rowData.questionType =  tokens[questionTypeIndex];
                rowData.category = tokens[categoryIndex];
                rowData.headWord = tokens[headWordIndex];
                rowData.index = count++;

                if (!rowData.headWord.equals("#")) {
                    for (int i = hypernymsIndex; i < tokens.length; i++) {
                        rowData.hypernyms.add(tokens[i]);
                    }
                }

                this.trainingDataCollection.addFeatureElement(label, rowData);

                line = br.readLine();
            }

            for (int i = 0; i < this.trainingDataCollection.questionsClasses.size(); i++) {
                Vector<RowData> classFeatures = this.trainingDataCollection.features.elementAt(i);
                Vector<String> classHeads = new Vector<String>();

                for (RowData rowData : classFeatures) {
                    if (rowData.headWord.equals("#")) {
                        continue;
                    }
                    classHeads.add(rowData.headWord);
                }


                double mue = this.getMue(classHeads);
                double sigma = this.getSigma(mue, classHeads);

                this.trainingDataCollection.addStatisticalElement(i, mue, sigma);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        int x = 0;
    }

    private double getMue(Vector<String> words) {
        int wordsLength = words.size();
        double valuesSum = 0.0;

        for (int i = 0; i < wordsLength; i++) {
            for (int j = i + 1; j < wordsLength; j++) {
                double sim = WordsSimilarity.getInstance().getSimilarity(words.get(i), words.get(j));
                if (sim == Double.MAX_VALUE) {
                    sim = 1.0;
                }
                valuesSum += sim;
            }
        }

        return valuesSum / (wordsLength * (wordsLength - 1) / 2);
    }

    private double getSigma(double mue, Vector<String> words) {
        int wordsLength = words.size();
        double valuesSum = 0.0;

        for (int i = 0; i < wordsLength; i++){
            for (int j = i + 1; j < wordsLength; j++) {
                double sim = WordsSimilarity.getInstance().getSimilarity(words.get(i), words.get(j));
                if (sim == Double.MAX_VALUE) {
                    sim = 1.0;
                }
                valuesSum += Math.pow(sim - mue, 2);
            }
        }

        return Math.sqrt(valuesSum / wordsLength);
    }
}
