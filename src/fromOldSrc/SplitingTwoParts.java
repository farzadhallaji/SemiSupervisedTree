package fromOldSrc;

import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;


public class SplitingTwoParts {
    public static String DatasetPath= "/home/farzad/Desktop/jrnl/semiWithTree/sick/";

	public static void divideIntoTwoPart(Instances data,Instances test, int n, int flag){

		data.sort(data.classIndex());
		int i=0,j=1,count=0;
		int m;
		Instance x;
		int g,c=0,f=0;

		int[] distr = new int[data.numClasses()];
		for(int i1 = 0; i1 < data.numInstances(); i1++)
		{
			//     double[] d = m_Classifiers[i1].distributionForInstance(inst);
			Instance inst;
			inst=data.instance(i1);
			for(int iClass = 0; iClass < data.numClasses(); iClass++){
				if(inst.classValue()==iClass){
					distr[iClass] += 1;
					//   		  System.out.println("Yesssssssssssssss");
				}
			}
		}
		for(int h=0; h< data.numClasses();h++)
			System.out.println("Number of classes== "+ distr[h]);

		Random generator = new Random();
		for(int k=0; k<data.numClasses();k++){
			m=  ( n *distr[k])/100;
			f=0;
			for(int p=1; p<=m ;p++){
				g=generator.nextInt(distr[k]-f);
				x=data.instance( g + c);
				if (flag==1)
					x.setClassMissing();
				test.add(x);
				data.delete(g+c);
				f++;
			}
			c+=distr[k]-f;

		}
	}

		public static void main(String[] args) throws Exception {

            BufferedReader reader = new BufferedReader(new FileReader(SplitingTwoParts.DatasetPath + "colic1.arff"));
            Instances data = new Instances(reader);
            reader.close();
            Instances  test= new Instances(data);

			reader.close();


			////////////////////////////////TODO Set Class Index
			data.setClassIndex(0) ;
			test.setClassIndex(0) ;


//			 Creating in ARFF file

			test.delete();

			int n=0,flag=0;
	         
	        // N is percentage of each class that we want to select for test set and flag=0 shows labeled data
			System.out.println("Number of instances we need=====  "+data.numInstances());
	        n=30;
	        divideIntoTwoPart(data,test,n,flag);

	  
	        // dataaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
	        System.out.println("Number of instances we need for training=====  "+data.numInstances());
			BufferedWriter writer1 = new BufferedWriter(
					new FileWriter("/home/farzad/Desktop/jrnl/semiWithTree/sick/train.arff"));
			writer1.write(data.toString());
			writer1.newLine();
			writer1.flush();
			writer1.close();

			// fffffffffffffffffffffffffffffffffffffffffffffffff1
			BufferedWriter writer2 = new BufferedWriter(
					new FileWriter("/home/farzad/Desktop/jrnl/semiWithTree/sick/test.arff"));
			writer2.write(test.toString());
			writer2.newLine();
			writer2.flush();
			writer2.close();
	   	 
		}

}
