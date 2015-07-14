package QuestionProcessingModule.OptimizationAlgorithms.SwarmOptimizationAlgorithms;

import java.util.function.Function;

/**
 * Created by mahmoud-hatem on 6/8/15.
 */
public abstract class SwarmOptimization {

    protected int numOfObjects;
    protected int dimensions;
    protected Function<double[], Double> costFunction;

    public SwarmOptimization(int numOfObjects, int dimensions, Function<double[], Double> costFunction) {
        this.numOfObjects = numOfObjects;
        this.dimensions = dimensions;
        this.costFunction = costFunction;
    }

    public abstract double[] optimize (double minError, int numOfIterations);
}
