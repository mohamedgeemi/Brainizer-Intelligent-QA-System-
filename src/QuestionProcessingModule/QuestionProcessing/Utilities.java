package QuestionProcessingModule.QuestionProcessing;

import QuestionProcessingModule.QuestionProcessing.Enums.REGEX_PATTERNS;

/**
 * Created by Mahmoud on 23-Feb-15.
 */
public final class Utilities {
    private Utilities() {

    }

    public static String senseDefinition(String definition) {
        return definition.replaceAll(REGEX_PATTERNS.GLOSS_SPLITTER_PATTERN, "");
    }

    public static int numberOfMatchedWords(String first, String second) {
        //TODO Cosine similarity


        String[] firstArray = first.split(" ");
        String[] secondArray = second.split(" ");

        int count = 0;

        for (int i = 0; i < firstArray.length; i++) {
            for (int j = 0; j < secondArray.length; j++) {
                if (firstArray[i].equals(secondArray[j])) {
                    count++;
                }
            }
        }

        return count;
    }
}
