package QuestionProcessingModule.OptimizationAlgorithms.SwarmOptimizationAlgorithms;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by ENG.AHMED HANI on 6/8/2015.
 */
public class ParticleSwarmOptimization extends SwarmOptimization {
    private final double MAX_POSITION = 100;
    private final double MIN_POSITION = -100;
    private final double MAX_VELOCITY = 100;
    private final double MIN_VELOCITY = -100;
    private final double INSERTIA_WEIGHT = 0.729;
    private final double LOCALE_WEIGHT = 1.49445;
    private final double GLOBAL_WEIGHT = 1.49445;
    private final double DEATH_PROBABILITY = 0.01;

    private Bird[] birdsFlock;
    private double[] bestSolutions;
    private double bestFitness;

    public ParticleSwarmOptimization(int numberOfBirds, int dimensions, Function<double[], Double> costFunction) {
        super(numberOfBirds, dimensions, costFunction);

        this.init();
    }

    private void init() {
        this.birdsFlock = new Bird[this.numOfObjects];
        this.bestSolutions = new double[this.dimensions];
        this.bestFitness = Double.MAX_VALUE;
        Random rnd = new Random();

        for (int bird = 0; bird < this.numOfObjects; bird++) {
            double[] randomPositions = new double[this.dimensions];
            double[] randomVelocities = new double[this.dimensions];
            Arrays.fill(randomPositions, 0.0);
            Arrays.fill(randomVelocities, 0.0);

            for (int i = 0; i < this.dimensions; i++) {
                randomPositions[i] = (this.MAX_POSITION - this.MIN_POSITION) * rnd.nextDouble() + this.MIN_POSITION;
                randomVelocities[i] = (this.MAX_POSITION * 0.1 - this.MIN_POSITION * 0.1) * rnd.nextDouble() + this.MIN_POSITION;
            }

            double fitness = this.costFunction.apply(randomPositions);

            this.birdsFlock[bird] = new Bird(randomPositions, randomVelocities, randomPositions, fitness, fitness);

            if (this.birdsFlock[bird].currentFitness < this.bestFitness) {
                this.bestFitness = this.birdsFlock[bird].currentFitness;

                for (int i = 0; i < this.dimensions; i++) {
                    this.bestSolutions[i] = this.birdsFlock[bird].position[i];
                }
            }
        }
    }

  /*  private double objectiveFunction(double[] x) {
        double res = 2.0 + x[0] + x[1];
        double trueThete = 2.0;

        return (res - trueThete) * (res - trueThete);
    }
*/
    @Override
    public double[] optimize(double minimumError, int numberOfIterations) {
        for (int it = 0; it < numberOfIterations; it++) {
            System.out.println("Current Iteration Error " + this.bestFitness + "\n");
            for (int bird = 0; bird < this.numOfObjects; bird++) {
                Random rnd1 = new Random();
                Random rnd2 = new Random();

                for (int i = 0; i < this.dimensions; i++) {
                    this.birdsFlock[bird].velocity[i] = (this.INSERTIA_WEIGHT * this.birdsFlock[bird].velocity[i])
                            + (this.GLOBAL_WEIGHT * rnd1.nextDouble()
                            * (this.birdsFlock[bird].bestPosition[i] - this.birdsFlock[bird].position[i]))
                            + (this.LOCALE_WEIGHT * rnd2.nextDouble()
                            * (this.bestSolutions[i] - this.birdsFlock[bird].position[i]));

                    if (this.birdsFlock[bird].velocity[i] < this.MIN_VELOCITY) {
                        this.birdsFlock[bird].velocity[i] = this.MIN_VELOCITY;
                    }
                    else if (this.birdsFlock[bird].velocity[i] > this.MAX_VELOCITY) {
                        this.birdsFlock[bird].velocity[i] = this.MAX_VELOCITY;
                    }

                    this.birdsFlock[bird].position[i] += this.birdsFlock[bird].velocity[i];

                    if (this.birdsFlock[bird].position[i] < this.MIN_POSITION) {
                        this.birdsFlock[bird].position[i] = this.MIN_POSITION;
                    }
                    else if (this.birdsFlock[bird].position[i] > this.MAX_POSITION) {
                        this.birdsFlock[bird].position[i] = this.MAX_POSITION;
                    }
                }

                double updatedFitness = this.costFunction.apply(this.birdsFlock[bird].position);
                this.birdsFlock[bird].currentFitness = updatedFitness;

                if (updatedFitness < this.birdsFlock[bird].bestFitness) {
                    this.birdsFlock[bird].bestFitness = updatedFitness;

                    for (int i = 0; i < this.dimensions; i++) {
                        this.birdsFlock[bird].bestPosition[i] = this.birdsFlock[bird].position[i];
                    }
                }

                if (updatedFitness < this.bestFitness) {
                    this.bestFitness = updatedFitness;

                    for (int i = 0; i < this.dimensions; i++) {
                        this.bestSolutions[i] = this.birdsFlock[bird].position[i];
                    }
                }

                Random dieRand = new Random();
                Random rnd = new Random();

                if (dieRand.nextDouble() < this.DEATH_PROBABILITY) {
                    for (int i = 0; i < this.dimensions; i++) {
                        this.birdsFlock[bird].position[i] = (this.MAX_POSITION - this.MIN_POSITION) * rnd.nextDouble()
                                + this.MIN_POSITION;
                        this.birdsFlock[bird].bestPosition[i] = this.birdsFlock[bird].position[i];
                    }

                    this.birdsFlock[bird].currentFitness = this.costFunction.apply(this.birdsFlock[bird].position);
                    this.birdsFlock[bird].bestFitness = this.birdsFlock[bird].currentFitness;
                }

                this.birdsFlock[bird].printBirdsData();
            }
        }

        //for (int i = 0; i < this.numberOfBirds; i++)
          //  this.birdsFlock[i].printBirdsData();

        System.out.println("Error : " + this.bestFitness);

        return bestSolutions;
    }
}
