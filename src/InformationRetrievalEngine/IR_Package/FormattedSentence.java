package InformationRetrievalEngine.IR_Package;

/**
 * Created by Mohamed on 3/23/2015.
 */
public class FormattedSentence
{

    public String format(String Sentence)
    {
        String[] SentenceTokens;
        String FormattedSentence = "";
        while (!Sentence.isEmpty() && Sentence.charAt(0)==' ')
        {
            if(Sentence.length()>1)
                Sentence = Sentence.substring(1,Sentence.length());
            else
                Sentence = "";
        }
        Sentence = Sentence.replaceAll(REGEX_PATTERNS.Spaces, " ");

        if (Sentence.matches(REGEX_PATTERNS.Email))
        {
            Sentence = Sentence.replaceAll("\\?", "");
            SentenceTokens = Sentence.split(" ");

            for (String str : SentenceTokens)
            {
                if (!str.matches(REGEX_PATTERNS.Email) || !str.matches(REGEX_PATTERNS.Numerical))
                {
                    FormattedSentence += str.replaceAll(REGEX_PATTERNS.NonAlphabetic, "");
                }
                else
                {
                    FormattedSentence += str;
                }

                FormattedSentence += " ";
            }
        }

        else
        {
            FormattedSentence = Sentence.replaceAll(REGEX_PATTERNS.NonAlphabetic, "");
        }

        if (FormattedSentence.lastIndexOf(' ') == FormattedSentence.length() - 1)
        {
            FormattedSentence = FormattedSentence.substring(0, FormattedSentence.length() - 1);
        }

        return FormattedSentence;
    }
}
