package fromOldSrc;

import weka.core.Instances;


public interface SplitTrainSet {

   
    void splitTrainSet();
    void splitTrainSet(int numLabledData, boolean randomSplit);
    void splitTrainSet(double percentLabeled, boolean randomSplit);
     
    Instances getLabledData();
   
    Instances getUnLabledData();
}
