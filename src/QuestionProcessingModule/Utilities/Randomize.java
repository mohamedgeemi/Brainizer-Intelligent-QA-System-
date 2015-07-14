package QuestionProcessingModule.Utilities;

import java.util.Random;

/**
 * Created by mahmoud-hatem on 6/7/15.
 */
public class Randomize {

    public static double[] randomArray (Random rand, int size)
    {
        double[] array = new double[size];
        for (int i = 0; i < size; ++i)
            array[i] = rand.nextDouble();

        return array;
    }
}
