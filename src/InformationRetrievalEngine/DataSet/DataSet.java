package InformationRetrievalEngine.DataSet;

import InformationRetrievalEngine.IR_Package.Indexing;

import java.io.*;
import java.util.Vector;

/**
 * Created by Mohamed on 4/3/2015.
 */

public class DataSet {
    private Record DataSet[];

    public Record[] GetDataSet()
    {
        return DataSet;
    }

    private String readFile(String filename)
    {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        try {
            FileReader reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    private String GetValue(String Input)
    {
        int Index=0;
        while (Index < Input.length() && Input.charAt(Index)!=']')
        {
            Index++;
        }
        return Input.substring(Index+1);
    }
    public DataSet()
    {
        String AllData = readFile("src\\DataSet\\DataSet.json");
        String[] Tokens = AllData.split("\r\n");
        DataSet = new Record[Tokens.length];
        for(int Index = 0 ; Index<Tokens.length ; ++Index)
        {
            Vector<Integer>Indices = new Vector<Integer>();
            Indices.add(0);
            for(int i=0 ; i<Tokens[Index].length()-2 ; ++i)
            {
                if(Tokens[Index].charAt(i)=='~' && Tokens[Index].charAt(i+1)==':' && Tokens[Index].charAt(i+2)=='~')
                {
                    Indices.add(i-1);
                }
            }
            Indices.add(Tokens[Index].length()-1);
            DataSet[Index] = new Record();
            DataSet[Index].question = GetValue(Tokens[Index].substring(Indices.elementAt(0),Indices.elementAt(1)));
            DataSet[Index].answer = GetValue(Tokens[Index].substring(Indices.elementAt(1)+3,Indices.elementAt(2)));
            DataSet[Index].category = GetValue(Tokens[Index].substring(Indices.elementAt(2)+3,Indices.elementAt(3)));
            DataSet[Index].seen = GetValue(Tokens[Index].substring(Indices.elementAt(3)+3,Indices.elementAt(4)));
            DataSet[Index].source = GetValue(Tokens[Index].substring(Indices.elementAt(4)+3,Indices.elementAt(5)));
            DataSet[Index].year = GetValue(Tokens[Index].substring(Indices.elementAt(5)+3,Indices.elementAt(6)));
        }
    }

    public void Insert(Record NewInput)
    {
        String Line = "\r\n"+"[question]" + NewInput.question + "~:~";
        Line += "[answer]" + NewInput.answer + "~:~";
        Line += "[category]" + NewInput.category + "~:~";
        Line += "[seen]" + NewInput.seen + "~:~";
        Line += "[source]" + NewInput.source + "~:~";
        Line += "[year]" + NewInput.year;

        File file =new File("src\\DataSet\\DataSet.json");

        FileWriter fileWritter = null;
        try
        {
            fileWritter = new FileWriter(file,true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(Line);
            bufferWritter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Record Search(String Question)
    {
        for(int index=0 ; index<DataSet.length ; ++index)
        {
            if(DataSet[index].question == Question)
            {
                return DataSet[index];
            }
        }
        return new Record();
    }

    public void Update(Record record)
    {
        for(int index=0 ; index<DataSet.length ; ++index)
        {
            if(DataSet[index].question == record.question)
            {
                DataSet[index]=record;
                return;
            }
        }
    }
}
