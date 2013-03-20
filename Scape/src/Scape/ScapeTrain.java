package Scape;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import JKernelMachines.fr.lip6.classifier.SMOSVM;
import JKernelMachines.fr.lip6.kernel.IndexedKernel;
import JKernelMachines.fr.lip6.kernel.typed.DoubleLinear;
import JKernelMachines.fr.lip6.type.TrainingSample;
import Taverna.ScapeTest;

public class ScapeTrain extends ScapeTest{
	private boolean isTrain=false;
	private ArrayList<TrainingSample<double[]>> trainingSamples = new ArrayList<TrainingSample<double[]>>();
	
	public void addExampleOfTrain(BufferedImage image1,BufferedImage image2, int label){
		//we ignore the image with label 2
		if(label==2){
			return;
		}
		else
			label = label==1 ? 1 : -1;
		/* compute the descriptor */
		ArrayList<ArrayList<double[]> >  histo1 = computeHisto(image1);
		ArrayList<ArrayList<double[]> >  histo2 = computeHisto(image2);
		/* compute the pair */		
		/* get Labels */
		/* parameter of distance*/
		String distmean = "TRUE";
		String gamma = "1.0";
		boolean bComptuteMeanDist = false;
		ArrayList<Double> pairDesc = new ArrayList<Double> ();
		for(int j=0 ; j<histo1.size() ; j++){
			IndexedKernel.run(histo1.get(j), histo2.get(j),pairDesc, distmean, gamma, bComptuteMeanDist,true);
			IndexedKernel.run(histo1.get(j), histo2.get(j),pairDesc, distmean, gamma, bComptuteMeanDist,false);
		}

		double []p = new double[pairDesc.size()];
		for(int j=0 ; j<pairDesc.size() ; j++)
			p[j] = pairDesc.get(j).doubleValue();
		
		trainingSamples.add(new TrainingSample<double[]>(p, label));
	}
	
	public void train(){
		isTrain = true;
		if(!isInialize){
			System.err.println("You need initialize this algorithm for training.");
			return ;
		}
		/* train SVM */
		DoubleLinear kernel = new DoubleLinear();
		svm = new SMOSVM<double[]>(kernel);
		//svm.setC(1);
		svm.setVerbosityLevel(0);
		
		svm.train(trainingSamples);
		
		/*Evaluator<double[]> evaluator = new Evaluator<double[]>(svm, trainingSamples, trainingSamples);
		System.out.print("...");
		evaluator.evaluate();
		System.out.println("Map: "+evaluator.getTestingMAP());
		*/
	}
	/**
	 * save the SVM after training
	 */
	public void saveSVM(){
		if(isTrain){
			try {
				FileOutputStream fos = new FileOutputStream(configFile.getBinSVM());
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(svm);
				oos.close();
			}
			catch (FileNotFoundException e) {e.printStackTrace();} 
			catch (IOException e) {e.printStackTrace();}
			
		}else{
			System.err.println("You must run the save.");
		}
	}
	/**
	 * @usage java ScapeTrain <config.xml> <train.txt> 
	 * @param args
	 */
	public static void main(String[] args) {
		ScapeTrain sc= new ScapeTrain();
		sc.init(new File(args[0]));
		
		FileReader fr=null;
		BufferedReader r=null;
		try {
			fr = new FileReader(args[1]);
			r = new BufferedReader(fr);
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		
		String parent=(new File(args[1])).getParentFile().getAbsolutePath()+"/";
		int i=0;
		try {
			while(r.ready()){
				String []l=r.readLine().split("\t");
				System.out.println("n°"+i+" -> "+l[1]+" , "+(i+1)+" -> "+l[2]+"|| label= "+l[0]);
				i+=2;
				sc.addExampleOfTrain(ImageIO.read(new File(parent+l[1])), ImageIO.read(new File(parent+l[2])), new Integer(l[0]));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sc.train();
		sc.saveSVM();
	}
	
}
