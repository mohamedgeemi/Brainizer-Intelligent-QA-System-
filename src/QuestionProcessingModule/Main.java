package QuestionProcessingModule;

import QuestionProcessingModule.Classifiers.MaxEntropy;
import QuestionProcessingModule.QuestionProcessing.QuestionAnalysis;
import QuestionProcessingModule.QuestionProcessing.QuestionFormat;
import QuestionProcessingModule.QuestionProcessing.Structures.RowData;
import QuestionProcessingModule.QuestionProcessing.Structures.TokensNode;
import QuestionProcessingModule.QuestionProcessing.SyntacticFeaturesImplementer;
import QuestionProcessingModule.TrainingDataProcessing.TrainingDataConstruction;
import QuestionProcessingModule.Utilities.Statistics;
import edu.stanford.nlp.trees.Tree;

import java.security.KeyStore;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by mahmoud-hatem on 6/7/15.
 */

public class Main {

    public static void main(String[] args) {
        // write your code here
        TrainingDataConstruction tdc = new TrainingDataConstruction(System.getProperty("user.dir") + "/src/QuestionClassification/questionOutputData.txt");
        tdc.parse();

        MaxEntropy maxent = new MaxEntropy(tdc.trainingDataCollection);

        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        while (input != "x") {
            String question = QuestionFormat.getInstance().format(input);
            QuestionAnalysis qa = new QuestionAnalysis(question);
            Tree parseTree = qa.parser();
            Vector<TokensNode> tokens = qa.tokenizer();
            SyntacticFeaturesImplementer ptf = new SyntacticFeaturesImplementer(parseTree, question);

            RowData data = new RowData();
            data.headWord = ptf.getHeadWord().elementAt(0);
            data.questionType = ptf.getQuestionType().toString();

            double[] probabilities = maxent.classify(data);

            int maxIndex = Statistics.Max(probabilities);

            for (Map.Entry<String, Integer> entry : tdc.trainingDataCollection.getQuestionsClasses().entrySet()) {
                if (entry.getValue().intValue() == maxIndex)
                    System.out.println(entry.getKey());
            }
            input = scanner.nextLine();
        }
    }
}