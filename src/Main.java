import AnswerProcessingModule.QuestionAnswerComparator;
import InformationRetrievalEngine.IR_Package.RankedRecord;
import InformationRetrievalEngine.InformationRetrieval;
import QuestionProcessingModule.Classifiers.MaxEntropy;
import QuestionProcessingModule.QuestionProcessing.QuestionAnalysis;
import QuestionProcessingModule.QuestionProcessing.QuestionFormat;
import QuestionProcessingModule.QuestionProcessing.Structures.RowData;
import QuestionProcessingModule.QuestionProcessing.Structures.TokensNode;
import QuestionProcessingModule.QuestionProcessing.SyntacticFeaturesImplementer;
import QuestionProcessingModule.TrainingDataProcessing.TrainingDataConstruction;
import QuestionProcessingModule.Utilities.Statistics;
import edu.stanford.nlp.trees.Tree;

import javax.swing.plaf.synth.Region;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by mahmoud-hatem on 6/9/15.
 */
public class Main {

    public static String readFile(String filename)
    {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        try {
            FileReader reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void main(String[] args)
    {
        System.out.println("1- Reading question training data");
        // Read training data
        TrainingDataConstruction trainingDataConstruction = new TrainingDataConstruction(System.getProperty("user.dir") + "/src/QuestionProcessingModule/questionOutputData.txt");
        trainingDataConstruction.parse();

        System.out.println("2- Training maxent classifier");
        // Traing maxent classifier
        MaxEntropy maxent = new MaxEntropy(trainingDataConstruction.trainingDataCollection);

        System.out.println("3- Reading document");
        // Read document
        String document = readFile(System.getProperty("user.dir") + "/src/document");
        InformationRetrieval.getInstance().setDocument(document);


        System.out.print("Enter Question: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (input != "") {

            System.out.println("1) Extracting keywords and question class");
            // Formating Question and analysis
            String question = QuestionFormat.getInstance().format(input);
            QuestionAnalysis questionAnalysis = new QuestionAnalysis(question);
            Tree parseTree = questionAnalysis.parser();
            Vector<TokensNode> tokens = questionAnalysis.tokenizer();

            SyntacticFeaturesImplementer syntacticFeaturesImplementer = new SyntacticFeaturesImplementer(parseTree, question);

            RowData data = new RowData();
            data.headWord = syntacticFeaturesImplementer.getHeadWord().elementAt(0);
            data.questionType = syntacticFeaturesImplementer.getQuestionType().toString();

            // Classify question
            double[] probabilities = maxent.classify(data);

            int maxIndex = Statistics.Max(probabilities);

            for (Map.Entry<String, Integer> entry : trainingDataConstruction.trainingDataCollection.getQuestionsClasses().entrySet()) {
                if (entry.getValue().intValue() == maxIndex)
                    System.out.println("question class: " + entry.getKey());
            }


            Vector<String> keywords = new Vector<>();

            System.out.print("question keywords: ");
            for (TokensNode token : tokens)
            {
              //  if (token.tag.contains("NN") ||/*( token.tag.contains("VB") && !token.tag.equals("VBP")) ||*/ token.tag.contains("JJ") ) {
                    keywords.add(token.word);
                    System.out.print(token.word + " ");
              //  }
            }
            System.out.println("");

            System.out.println("2) Retrieve related passages.");
            // IR phase
            Vector<RankedRecord> relevantSentences = InformationRetrieval.getInstance().getRelevantSentences(keywords);

            Vector<String> passages = new Vector<>();
            for (int i = 0; i < relevantSentences.size(); i++) {

                passages.add(relevantSentences.elementAt(i).Sentence);
                //System.out.println(relevantSentences.elementAt(i).Rank + " %\n" + relevantSentences.elementAt(i).Sentence + "\n");
            }

            System.out.println("3) Extracting the answer");
            QuestionAnswerComparator questionAnswerComparator = new QuestionAnswerComparator(question, passages);
            int answerIndex = questionAnswerComparator.Execute();

            System.out.println("The answer: " + passages.elementAt(answerIndex));

            System.out.print("\nEnter Question: ");
            input = scanner.nextLine();
        }
    }
}
