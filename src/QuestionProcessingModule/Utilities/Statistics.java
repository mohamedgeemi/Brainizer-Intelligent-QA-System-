package QuestionProcessingModule.Utilities;

/**
 * Created by mahmoud-hatem on 6/7/15.
 */
public class Statistics {


    public static double Mean (double[] data)
    {
        int size = data.length;

        double sum = 0;
        for (int i = 0; i < size; ++i)
            sum += data[i];

        return sum/size;
    }

    public static int Max (double[] data)
    {
        int size = data.length;

        if (size != 0) {
            double max = data[0];
            int maxIndex = 0;

            for (int i = 1; i < size; ++i)
            {
                if (data[i] > max)
                {
                    max = data[i];
                    maxIndex = i;
                }
            }
            return maxIndex;
        }
        return -1;
    }
}
