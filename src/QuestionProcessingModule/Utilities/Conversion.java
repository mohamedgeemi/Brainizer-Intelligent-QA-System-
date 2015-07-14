package QuestionProcessingModule.Utilities;

import java.util.Vector;

/**
 * Created by mahmoud-hatem on 6/7/15.
 */
public class Conversion {

    public static double[] vectorToArray (Vector<Double> vector)
    {
        double[] array = new double[vector.size()];
        for (int i = 0; i < vector.size(); ++i)
            array[i] = vector.elementAt(i);

        return array;
    }

}
