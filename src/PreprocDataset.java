import fromOldSrc.SplitingTwoParts;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class PreprocDataset {

    public static int getClassIndexnum() {
        return ClassIndexnum;
    }
    public static void setClassIndexnum(int classIndexnum) {
        ClassIndexnum = classIndexnum;
    }

    public static int ClassIndexnum = 0;


    public static void main(String[] args) throws Exception  {


        BufferedReader reader = new BufferedReader(new FileReader(SplitingTwoParts.DatasetPath + "colicMain.arff"));
        // BufferedReader reader = new BufferedReader(new FileReader(SplitingTwoParts.DatasetPath + "test.arff"));
        Instances data = new Instances(reader);
        reader.close();

        // because of two classes
        int[] findex = new int[data.numAttributes()-1];
        for (int i = 0; i < findex.length ; i++){
            //Todo
            findex[i] = i;
        }
        ClassIndexnum = 0 ;
        Remove removeFilter = new Remove();
        removeFilter.setAttributeIndicesArray(findex);
        removeFilter.setInvertSelection(true);
        removeFilter.setInputFormat(data);
        data = Filter.useFilter(data, removeFilter);

        ////////////////////////

        data.setClassIndex(ClassIndexnum);

        System.out.println(data);
//        System.out.println(data.attribute(2));

        BufferedWriter writer1 = new BufferedWriter(new FileWriter(SplitingTwoParts.DatasetPath+"colic1.arff"));
        writer1.write(data.toString());
        writer1.newLine();
        writer1.flush();
        writer1.close();

    }

}
