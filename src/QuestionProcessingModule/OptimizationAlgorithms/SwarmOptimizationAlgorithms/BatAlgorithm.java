package QuestionProcessingModule.OptimizationAlgorithms.SwarmOptimizationAlgorithms;

import QuestionProcessingModule.Utilities.Conversion;
import QuestionProcessingModule.Utilities.Randomize;
import QuestionProcessingModule.Utilities.Statistics;

import java.util.Random;
import java.util.Vector;
import java.util.function.Function;

/**
 * Created by mahmoud-hatem on 6/7/15.
 */
public class BatAlgorithm extends SwarmOptimization{

    private final double minFreq = 0, maxFreq = 100;
    private final double maxLoudness = 1;

    private Vector<double[]> batsPosition;
    private Vector<double[]> batsVelocity;
    private Vector<Double> batsPulseFreq;
    private Vector<Double> initialBatsPulseRate;
    private Vector<Double> batsLoudness;

    private double[] bestPosition;
    private Vector<double[]> newBatsPosition;

    public BatAlgorithm(int numOfBats, int dimensions, Function<double[], Double> costFunction)
    {
        super(numOfBats, dimensions, costFunction);
        this.init();
    }

    private void init()
    {
        Random rand = new Random();

        this.batsPosition = new Vector<>();
        this.newBatsPosition = new Vector<>();
        this.batsVelocity = new Vector<>();
        this.batsPulseFreq = new Vector<>();
        this.initialBatsPulseRate = new Vector<>();
        this.batsLoudness = new Vector<>();

        for (int i = 0; i < this.numOfObjects; ++i)
        {
            this.batsPosition.add(Randomize.randomArray(rand, this.dimensions));
            this.batsVelocity.add(Randomize.randomArray(rand, this.dimensions));
            this.batsPulseFreq.add(Randomize.randomArray(rand, 1)[0] * (maxFreq - minFreq) + minFreq );
            this.initialBatsPulseRate.add(Randomize.randomArray(rand, 1)[0]);
            this.batsLoudness.add(Randomize.randomArray(rand, 1)[0] * this.maxLoudness);
        }

    }

    @Override
    public double[] optimize(double MinError, int numOfIteration)
    {
        Vector<Double> batsPulseRate = this.initialBatsPulseRate;

        Random rand = new Random();
        int iteration = 0;
        this.bestPosition = this.getBestPosition();
        double error = Math.abs(this.costFunction.apply(bestPosition));

        while(error > MinError && iteration < numOfIteration)
        {
         /*   System.out.println(error + " " + iteration);
            for (int i = 0; i < this.bestPosition.length; ++i)
                System.out.print(this.bestPosition[i] + " ");
            System.out.println("");
*/
            this.updateBats();
            double avgLoudness = Statistics.Mean(Conversion.vectorToArray(this.batsLoudness));

            for (int i = 0; i < this.numOfObjects; ++i)
            {
                if (rand.nextDouble() > batsPulseRate.elementAt(i))
                {
                    double rnd = (rand.nextDouble() * 2) - 1;
                    double[] newValue = new double[this.dimensions];
                    for (int j = 0; j < this.dimensions; ++j)
                        newValue[j] = this.bestPosition[j] + (rnd * avgLoudness);

                    this.newBatsPosition.set(i, newValue);
                }

                // Fly Random
                double[] addedValue = new double[this.dimensions];
                for (int j = 0; j < this.dimensions; ++j)
                    addedValue [j] = this.newBatsPosition.elementAt(i)[j] + rand.nextGaussian();
                this.newBatsPosition.set(i, addedValue);

                if (rand.nextDouble()* this.maxLoudness < this.batsLoudness.elementAt(i) &&
                        this.costFunction.apply(this.newBatsPosition.elementAt(i)) <= this.costFunction.apply(this.batsPosition.elementAt(i)))
                {
                    this.batsPosition.set(i, this.newBatsPosition.elementAt(i));
                    this.batsLoudness.set(i, this.updateLoudness(this.batsLoudness.elementAt(i)));
                    batsPulseRate.set(i, this.updatePulseRate(this.initialBatsPulseRate.elementAt(i), iteration));
                }

                if (this.costFunction.apply(this.newBatsPosition.elementAt(i)) <= this.costFunction.apply(this.bestPosition))
                    this.bestPosition = this.newBatsPosition.elementAt(i);
            }

            error = Math.abs(this.costFunction.apply(bestPosition));
            iteration++;
        }

        return this.bestPosition;
    }

    private double[] getBestPosition()
    {
        int minIndex = 0;
        double minValue = this.costFunction.apply(this.batsPosition.elementAt(0));

        for (int i = 1; i < this.numOfObjects; ++i)
        {
            double value = this.costFunction.apply(this.batsPosition.elementAt(i));
            if (value < minValue)
                minIndex = i;
        }

        return  this.batsPosition.elementAt(minIndex);
    }

    private void updateBats()
    {
        this.updateFreq();
        this.updateVelocity();
        this.updatePosition();
    }
    private void updateFreq()
    {
        Random rand = new Random();

        for (int i = 0; i < this.numOfObjects; ++i)
        {
            double value = this.minFreq + rand.nextDouble() * (this.maxFreq - this.minFreq);
            this.batsPulseFreq.set(i, value);
        }
    }
    private void updateVelocity()
    {
        for (int i = 0; i < this.numOfObjects; ++i)
        {
            double[] newVelocity = new double[this.dimensions];
            for (int j = 0; j < this.dimensions; ++j)
            {
                newVelocity[j] = this.batsVelocity.elementAt(i)[j] + (this.batsPosition.elementAt(i)[j] - this.bestPosition[j]) * this.batsPulseFreq.elementAt(j);
            }
            this.batsVelocity.set(i, newVelocity);
        }
    }
    private void updatePosition()
    {
        this.newBatsPosition.clear();
        for (int i = 0; i < this.numOfObjects; ++i)
        {
            double[] newPosition = new double[this.dimensions];
            for (int j = 0; j < this.dimensions; ++j)
            {
                newPosition[j] = this.batsPosition.elementAt(i)[j] + this.batsVelocity.elementAt(i)[j];
            }
            this.newBatsPosition.add(i, newPosition);
        }
    }
    private double updateLoudness(double oldLoudness)
    {
        double alpha = 0.5;
        return alpha * oldLoudness;
    }
    private double updatePulseRate(double oldPulseRate, int t)
    {
        double gamma = 0.3;
        return (1 - Math.exp(- gamma * t)) * oldPulseRate;
    }
}
