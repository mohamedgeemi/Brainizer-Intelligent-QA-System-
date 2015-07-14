package QuestionProcessingModule.OptimizationAlgorithms.SwarmOptimizationAlgorithms;

/**
 * Created by ENG.AHMED HANI on 6/8/2015.
 */
public class Bird {
    public double[] position;
    public double[] velocity;
    public double[] bestPosition;
    public double currentFitness;
    public double bestFitness;

    public Bird(double[] position, double[] velocity, double[] bestPosition, double currentFitness,
                double bestFitness) {
        this.position = position;
        this.velocity = velocity;
        this.bestPosition = bestPosition;
        this.currentFitness = currentFitness;
        this.bestFitness = bestFitness;
    }

    public void printBirdsData() {
        String output = "";
        output += "Birds Statistics\n";
        output += "=====================\n";
        output += "Birds Current Position\n";

        for (int i = 0; i < this.position.length; i++) {
            output += this.position[i] + " ";
        }

        output += "\n";
        output += "Bird Current Fitness: ";
        output += this.currentFitness;
        output += "\n";
        output += "Velocity: ";

        for (int i = 0; i < this.velocity.length; i++) {
            output += this.velocity[i] + " ";
        }

        output += "\n";
        output += "Best Solution: ";

        for (int i = 0; i < this.bestPosition.length; i++) {
            output += this.bestPosition[i] + " ";
        }

        output += "\n";
        output += "Best Fitness: ";
        output += this.bestFitness + " ";
        output += "\n";
        output += "=====================\n";

        System.out.println(output);
    }
}
