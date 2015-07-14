package QuestionProcessingModule.Classifiers;

import QuestionProcessingModule.OptimizationAlgorithms.SwarmOptimizationAlgorithms.*;
import QuestionProcessingModule.QuestionProcessing.Structures.RowData;
import QuestionProcessingModule.TrainingDataProcessing.TrainingDataCollection;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;
import java.util.Vector;
import java.util.function.Function;

/**
 * Created by ENG.AHMED HANI on 3/11/2015.
 */
public class MaxEntropy extends Classifier implements Function<double[], Double> {

    private Vector<Vector<Vector<int[]>>> SamplesBinrayFeatures;
    private FeaturesBinarization featuresBinarization;

    private double[] lamda;

    public MaxEntropy(TrainingDataCollection trainingDataCollection) {
        super(trainingDataCollection);
        this.init();
    }

    private void init()
    {
        this.featuresBinarization = new FeaturesBinarization(this.trainingDataCollection);
        this.SamplesBinrayFeatures = new Vector<>();


        for (int i = 0; i < this.trainingDataCollection.getFeatures().size(); ++i)
        {
            Vector<Vector<int[]>> classBinaryFeatures = new Vector<>();

            for (int j = 0; j < this.trainingDataCollection.getFeatures().elementAt(i).size(); ++j)
            {
                Vector<int[]> SampleBinaryFeatures = this.featuresBinarization.binarize(this.trainingDataCollection.getFeatures().elementAt(i).elementAt(j));
                classBinaryFeatures.add(SampleBinaryFeatures);
            }
            this.SamplesBinrayFeatures.add(classBinaryFeatures);
        }


        BatAlgorithm optimizationAlgorithm = new BatAlgorithm(10, this.featuresBinarization.NUM_OF_FEATURES, this);
        this.lamda = optimizationAlgorithm.optimize(Math.exp(-8), 100);
        //ParticleSwarmOptimization particleSwarmOptimization = new ParticleSwarmOptimization(15,this.featuresBinarization.NUM_OF_FEATURES, this);
        //this.lamda = particleSwarmOptimization.optimize(0.0, 500);
    }

    public double[] classify(RowData data)
    {
        Vector<int[]> binaryFeatures = this.featuresBinarization.binarize(data);

        double[] probabilityOfClassGivenData = new double[this.featuresBinarization.NUM_OF_ClASSES];

        for (int c = 0; c < this.featuresBinarization.NUM_OF_ClASSES; ++c)
        {
            probabilityOfClassGivenData[c] = this.calculateProbabilityOfClassGivenData(binaryFeatures, c);
        }

        return probabilityOfClassGivenData;
    }

    private double calculateProbabilityOfClassGivenData(Vector<int[]> binaryFeatures, int classNum )
    {
        double vote = 0;
        double normalization = 0;

        double sum = 0;
        //feature
        for (int k = 0; k < this.featuresBinarization.NUM_OF_FEATURES; ++k)
        {
            sum += this.lamda[k] * binaryFeatures.elementAt(classNum)[k];
        }
        vote = Math.exp(sum);

        for (int c = 0; c < this.featuresBinarization.NUM_OF_ClASSES; ++c)
        {
            double sumFeature = 0;
            for (int k = 0; k < this.featuresBinarization.NUM_OF_FEATURES; ++k) {
                sumFeature += this.lamda[k] * binaryFeatures.elementAt(c)[k];
            }
            normalization += Math.exp(sumFeature);
        }

        return  vote / normalization;
    }

    @Override
    public Double apply(double[] lamda) {
        this.lamda = lamda;

        double cost = 0;
        //class
        for (int i = 0; i < this.featuresBinarization.NUM_OF_ClASSES; ++i)
        {
            //sample
            for (int j = 0; j < this.SamplesBinrayFeatures.elementAt(i).size(); ++j)
            {
                Vector<int[]> binaryFeatures = this.SamplesBinrayFeatures.elementAt(i).elementAt(j);

                double probabilityOfClassGivenData = this.calculateProbabilityOfClassGivenData(binaryFeatures, i);

                cost += Math.log(probabilityOfClassGivenData);
            }
        }
        
        return - cost;
    }
}
