package QuestionProcessingModule.TrainingDataProcessing;

import QuestionProcessingModule.QuestionProcessing.*;
import QuestionProcessingModule.QuestionProcessing.Enums.QUESTION_TYPE;
import QuestionProcessingModule.QuestionProcessing.Structures.TokensNode;
import com.sun.swing.internal.plaf.synth.resources.synth_sv;
import edu.stanford.nlp.patterns.surface.Token;
import edu.stanford.nlp.trees.Tree;
import javafx.scene.control.Separator;
import net.didion.jwnl.data.Synset;

import java.io.*;
import java.util.Vector;

/**
 * Created by Mahmoud on 24-Feb-15.
 */
public class DataSetParser {
    private static QuestionFormat qf;
    private static QuestionAnalysis qa;
    private static SyntacticFeaturesImplementer ptf;
    private static SemanticFeaturesImplementer sf;

    public static void main (String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/questionTrainingData.txt"));


        String line = br.readLine();
        String question = "";
        String label = "";
        StringBuilder sb = new StringBuilder();

        int counter = 0;
        while(line != null) {
            System.out.println(++counter);

            if (counter == 32) {
                int x = 10;
            }

            int index = line.indexOf(" ");
            label = line.substring(0, index);
            String pureQuestion = line.substring(index + 1, line.length());
            question = QuestionFormat.getInstance().format(pureQuestion);

            qa = new QuestionAnalysis(question);
            Tree parseTree = qa.parser();
            Vector<TokensNode> tokens = qa.tokenizer();

            ptf = new SyntacticFeaturesImplementer(parseTree, question);
            QUESTION_TYPE questionType = ptf.getQuestionType();
            Vector<String> heads = ptf.getHeadWord();

            sb = push(sb,label," ");
            sb = push(sb,questionType.toString()," ");

            if (ptf.isCategory()) {
                // no hypernyms
                sb = push(sb,heads.get(0)," ");
                sb = push(sb,"#",System.lineSeparator());
            }
            else if (ptf.isHeadWord()){
                sb = push(sb,"#"," ");

                // hypernyms
                sf = new SemanticFeaturesImplementer(heads, tokens);
                Synset bestSense = sf.getBestSense();
                if (bestSense != null) {

                    Vector<String> hypernyms = sf.getHypernyms(bestSense);
                    sb = push(sb, heads.get(0), " ");
                    String hypernymLine = "";
                    for (int i = 0; i < hypernyms.size(); i++) {
                        if (i == 0)
                            hypernymLine += hypernyms.get(i);
                        else
                            hypernymLine += " " + hypernyms.get(i);
                    }

                    sb = push(sb, hypernymLine, System.lineSeparator());
                }
                else//TODO handel with conversation
                    sb = push(sb, "#", System.lineSeparator());
            }
            else{
                // why, where or when question
                sb = push(sb,"#"," ");
                sb = push(sb,"#",System.lineSeparator());
            }


            line = br.readLine();
            //System.out.println(sb.toString());
           // sb.delete(0, sb.length());
        }


        Writer wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("questionOutputData.txt")));
        wr.write(sb.toString());
        wr.close();
        br.close();
    }


    private static StringBuilder push (StringBuilder sb, String line, String separator)
    {
        sb.append(line);
        sb.append(separator);

        return sb;
    }

}
