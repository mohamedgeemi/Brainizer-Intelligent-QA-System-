package QuestionProcessingModule.QuestionProcessing;

import QuestionProcessingModule.QuestionProcessing.Enums.REGEX_PATTERNS;

/**
 * Created by Hani & hatem on 21-Feb-15.
 */
public class QuestionFormat {
    private static QuestionFormat instance = new QuestionFormat();

    private QuestionFormat() {

    }

    public static QuestionFormat getInstance() {
        return instance;
    }

    public String format(String question) {
        String[] questionTokens;
        String formattedQuestion = "";
        question = question.replaceAll(REGEX_PATTERNS.Spaces, " ");

        if (question.matches(REGEX_PATTERNS.Email)) {
            question = question.replaceAll("\\?", "");
            questionTokens = question.split(" ");

            for (String str : questionTokens) {
                if (!str.matches(REGEX_PATTERNS.Email) || !str.matches(REGEX_PATTERNS.Numerical)) {
                    formattedQuestion += str.replaceAll(REGEX_PATTERNS.NonAlphabetic, "");
                }
                else {
                    formattedQuestion += str;
                }

                formattedQuestion += " ";
            }
        }

        else {
            formattedQuestion = question.replaceAll(REGEX_PATTERNS.NonAlphabetic, "");
        }

        if (formattedQuestion.lastIndexOf(' ') == formattedQuestion.length() - 1) {
            formattedQuestion = formattedQuestion.substring(0, formattedQuestion.length() - 1);
        }

        return formattedQuestion;
    }
}
