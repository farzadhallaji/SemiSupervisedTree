import fromOldSrc.SplitingTwoParts;
import smile.classification.PlattScaling;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.EvaluationUtils;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class SemiDT {

//    protected static  Instances labledDataSet;
//    protected static  Instances UnlabledDataSet;
//    protected static  double m_numLabledData = 10.0;
    protected static  boolean m_randomSplit = false;
    public static  void setRandomSplit(boolean randomSplit) {
        m_randomSplit = randomSplit;

    }

    public boolean getRandomSplit() {
        return m_randomSplit;
    }

    public static Instances LabeledUnlabeldata(Instances data,Instances unlabeled, J48 tree) throws Exception{

        //  J48 tree = new J48();         // new instance of tree
        //  tree.setUseLaplace(true);
        //   tree.setUnpruned(true);
        System.out.println(" labeled data in the start     =  "+data.numInstances());
        System.out.println("ulabeled data in the start    =  "+unlabeled.numInstances());

        Instances data1 = new Instances(data);
        Instances labeling= new Instances(unlabeled);

        tree.buildClassifier(data1);   // build classifier
        Prediction r;

        EvaluationUtils  eval= new EvaluationUtils();
        // label instances
        double[]  predict=new double[labeling.numInstances()];
        int j=0,i=0,s=0;
        double clsLabel;
        int l=0;
//        double y=0.800000000001;
        double y=0.800000000001;
        System.out.println("tessssssssssssssssstttttttttttttttttttttttttttttttttttt");
        while (i<labeling.numInstances()){
            clsLabel= tree.classifyInstance(labeling.instance(i));
            r=eval.getPrediction(tree, labeling.instance(i));
            System.out.println(i+")" +" actual= "+r.actual()+", predicted=  "+r.predicted()+" weight=  "+r.weight());
            double[] dist=tree.distributionForInstance(labeling.instance(i));
            predict[i]=r.predicted();
            System.out.println(i+")" +"   "+predict[i]);
            for (int k=0; k<dist.length; k++){
                System.out.print("   "+dist[k]);
                if(dist[k]>=y){
                    j=i;
                    while(j<labeling.numInstances()){
                        clsLabel= tree.classifyInstance(labeling.instance(j));
                        r=eval.getPrediction(tree, labeling.instance(j));
                        dist=tree.distributionForInstance(labeling.instance(j));
                        for (int p=0; p<dist.length; p++){
                            System.out.print("   "+dist[p]);
                            if(dist[p]>=y){
                                //   System.out.println("tanhan="+j+"\n");
                                labeling.instance(j).setClassValue(clsLabel);
                                Instance x = labeling.instance(j);
                                //   System.out.println("\n X= "+x);
                                data1.add(x);
                                labeling.delete(j);
                                l++;
                                j=j-1;
                            }// if
                        }// for
                        //    predict[j]=r.predicted();
                        System.out.println();
                        j++;
                    }// while
                }// if
                if( (k==(dist.length-1)) && (l!=0) ){
                    tree.buildClassifier(data1);
                    i=-1; s=s+l; l=0;
                }
            }// for
            System.out.println();
            i++;
        }// while

        data1.compactify();
        return data1;

    }

    public static Instances LabeledUnlabeldataPlattScaling(Instances data,Instances unlabeled, J48 tree) throws Exception{

        //  J48 tree = new J48();         // new instance of tree
        //  tree.setUseLaplace(true);
        //   tree.setUnpruned(true);
        System.out.println(" labeled data in the start     =  "+data.numInstances());
        System.out.println("ulabeled data in the start     =  "+unlabeled.numInstances());

        Instances data1 = new Instances(data);
        Instances labeling= new Instances(unlabeled);

        tree.buildClassifier(data1);   // build classifier
        Prediction r;

        EvaluationUtils  eval= new EvaluationUtils();
        // label instances
        double[]  predict=new double[labeling.numInstances()];
        int j=0,i=0,s=0;
        double clsLabel;
        int l=0;
        double y=0.500000000001;

        System.out.println("tessssssssssssssssstttttttttttttttttttttttttttttttttttt");
        while (i<labeling.numInstances()){
            clsLabel= tree.classifyInstance(labeling.instance(i));
            r=eval.getPrediction(tree, labeling.instance(i));
            System.out.println(i+")" +" actual= "+r.actual()+", predicted=  "+r.predicted()+" weight=  "+r.weight());

            ///////////////////////////////////
//            double[] dist=tree.distributionForInstance(labeling.instance(i));
            double[] scores = new double[data1.size()];
            int[] y_label = new int[data1.size()];
            for(int ay = 0 ; ay < data1.size() ; ay++) {
                y_label[ay] = (int) data1.instance(ay).value(PreprocDataset.ClassIndexnum);
                scores[ay] = tree.distributionForInstance(data1.instance(ay))[y_label[ay]]*(y_label[ay]*2 - 1 );
            }
            PlattScaling plattScaling = new PlattScaling(scores, y_label);
            double dist1 = plattScaling.predict(scores[i]);
            double[] dist = new double[1];
            dist[0]=dist1;

//            System.out.println("dist.distributionForInstance  length   :    " + dist.length  );


            predict[i]=r.predicted();
            System.out.println(i+")" +"   "+predict[i]);
            for (int k=0; k<dist.length; k++){
                System.out.print("   "+dist[k]);
                if(dist[k]>=y){
                    j=i;
                    while(j<labeling.numInstances()){
                        clsLabel= tree.classifyInstance(labeling.instance(j));
                        r=eval.getPrediction(tree, labeling.instance(j));
                        ///TODO OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
                        dist=tree.distributionForInstance(labeling.instance(j));
                        for (int p=0; p<dist.length; p++){
                            System.out.print("   "+dist[p]);
                            if(dist[p]>=y){
                                //   System.out.println("tanhan="+j+"\n");
                                labeling.instance(j).setClassValue(clsLabel);
                                Instance x = labeling.instance(j);
                                //   System.out.println("\n X= "+x);
                                data1.add(x);
                                labeling.delete(j);
                                l++;
                                j=j-1;
                            }// if
                        }// for
                        //    predict[j]=r.predicted();
                        System.out.println();
                        j++;
                    }// while
                }// if
                if( (k==(dist.length-1)) && (l!=0) ){
                    tree.buildClassifier(data1);
                    i=-1; s=s+l; l=0;
                }
            }// for
            System.out.println();
            i++;
        }// while

        data1.compactify();
        return data1;

    }



    public static void main(String[] args) throws Exception {

        //Read dataset train and test
        BufferedReader reader = new BufferedReader(new FileReader(SplitingTwoParts.DatasetPath + "train.arff"));
        Instances train = new Instances(reader);
        reader.close();

        reader = new BufferedReader(new FileReader(SplitingTwoParts.DatasetPath + "test.arff"));
        Instances test = new Instances(reader);
        reader.close();
        System.out.println();

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //set class of dataset
        train.setClassIndex(PreprocDataset.ClassIndexnum) ;
        test.setClassIndex(PreprocDataset.ClassIndexnum) ;
        //test.setClassIndex(test.numAttributes() - 1 ) ;
//        System.out.println("test.numClasses()   = "+ test.numClasses());

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Split train test
//        SemiBoostSplitTrainSet splitTrain = new SemiBoostSplitTrainSet(train);
//        Random randomInstance = new Random(1);
//        splitTrain.setRandom(randomInstance);
//        splitTrain.setPercentLabeled(m_numLabledData);
//        setRandomSplit(true);
//        splitTrain.splitTrainSet(m_numLabledData, m_randomSplit);
//        labledDataSet = splitTrain.getLabledData();
//        UnlabledDataSet = splitTrain.getUnLabledData();

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //   tree.setUnpruned(true);
        //  tree.setUseLaplace(true);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ///////// Replacing missing value

//        ReplaceMissingValues fixMissinglabledDataSet = new ReplaceMissingValues();
//        fixMissinglabledDataSet.setInputFormat(labledDataSet);
//        labledDataSet = Filter.useFilter(labledDataSet, fixMissinglabledDataSet);
//
//        ReplaceMissingValues fixMissingUnlabledDataSet = new ReplaceMissingValues();
//        fixMissingUnlabledDataSet.setInputFormat(UnlabledDataSet);
//        UnlabledDataSet = Filter.useFilter(UnlabledDataSet, fixMissingUnlabledDataSet);


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        J48 tree = new J48();         // new instance of tree

        // Split train dataset to two dataset label and unlabeled
        ArrayList<Instances> splu = splitUnlabledDataSet(train);

        Instances labledDataSet = splu.get(0);
        Instances UnlabledDataSet = splu.get(1);

        //evaluation for labeled data set (before the first itr)
        Evaluation eval = new Evaluation(labledDataSet);
        tree.buildClassifier(labledDataSet);
        eval.evaluateModel(tree, test);
        System.out.println("Labeled data = "+(1 - eval.errorRate())*100+" number of instances = "+labledDataSet.numInstances());
        System.out.println("           Decision Tree    Simple                 " );
        System.out.println("      precision   recall     areaUnderROC            ");
        for(int i=0 ; i<test.numClasses(); i++)
            System.out.println(" "+eval.precision(i) +"  "+eval.recall(i) + "  "  +  eval.areaUnderROC(i));


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //semi Supervised
        Instances Newtrainpool = LabeledUnlabeldataPlattScaling(labledDataSet,UnlabledDataSet,tree);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	       Combining Two File that have label



        System.out.println("\n\n Labeled data == "+(1-eval.errorRate())*100+" number of instances == " + labledDataSet.numInstances());

        // Classifing the label data
        boolean f=false;
        System.out.println("           Decision Tree                       " );
        System.out.println("      precision   recall     areaUnderROC            ");
        for(int i=0 ; i<test.numClasses(); i++)
            System.out.println(" "+eval.precision(i) +"  "+eval.recall(i) + "  "  +  eval.areaUnderROC(i));


        f=true;
        ClassifyWithDT(Newtrainpool,test,f,tree);


    }

    private static ArrayList<Instances> splitUnlabledDataSet(Instances train) {

        int labelprec = 10;

        ArrayList<Instances> res = new ArrayList<>(2);

        Instances t = new Instances(train);
        Instances l = new Instances(train);
        Instances ul = new Instances(train);
        t.randomize(new Random(1));
        l.delete();
        ul.delete();
        int total = train.numInstances();
        double ltotal = labelprec*total/100;

        for (int i = 0 ; i<total ; i++ ){
            if(i <= ltotal){
                l.add(t.instance(i));
            }else {
                Instance temp = t.instance(i);
//                temp.setClassMissing();

                ul.add(temp);
//                System.out.println("temp.classIsMissing() = " + temp.classIsMissing());
//                System.out.println("temp.classIndex() = " + temp.classIndex());
            }
        }

        res.add(l);
        res.add(ul);
        System.out.println("train.numInstances    =  "+l.numInstances());
        System.out.println("train.numInstances    =  "+ul.numInstances());

        return res;

    }

    private static Instances getlabledDataSet(Instances train) {
        return null;
    }


    public static Evaluation ClassifyWithDT(Instances f3, Instances test, boolean f, J48 tree) throws Exception {
        // TODO Auto-generated method stub

        Evaluation eval= new Evaluation(f3);
        tree.buildClassifier(f3);

        eval.evaluateModel(tree, test);

        //	eval.
        System.out.println("Self-Training   data = "+(1-eval.errorRate())*100+" number of instances = "+f3.numInstances());
        System.out.println(" Error Rate = "+eval.errorRate());

        System.out.println("\tprecision\t\trecall\t\tareaUnderROC");
        for(int i=0 ; i<test.numClasses(); i++)
            System.out.println(eval.precision(i) +"  "+eval.recall(i) + "  "  +  eval.areaUnderROC(i));

        return eval;
    }



}
