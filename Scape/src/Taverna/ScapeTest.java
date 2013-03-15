package Taverna;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import fr.lip6.jdescriptors.Descriptor;

import JDescriptors.CreateIHSVectors;
import JDescriptors.SpatialPyramids;
import JKernelMachines.fr.lip6.classifier.SMOSVM;
import JKernelMachines.fr.lip6.kernel.IndexedKernel;
import Scape.Classification;
import Scape.ColorDescriptor;

public class ScapeTest {
	
	private boolean isInialize=false;
	private FileConfig configFile=null;
	private SMOSVM<double[]> svm=null;
	private ArrayList<double[][]> dicoCenters=null;
	private ArrayList<double[]> dicoSigma=null;
	
	/**
	 * Initialize the algorithm
	 * @param param the file with the paramter of our algorithm.
	 */
	public void init(File param){
		isInialize=true;

		try {
			configFile = FileConfig.deserializeXMLToObject(param);
		} catch (FileNotFoundException e1) {e1.printStackTrace();}
		
		/*
		 * Download SVM
		 */
		try {
			FileInputStream fis = new FileInputStream(configFile.getBinSVM());
			ObjectInputStream ois = new ObjectInputStream(fis);
			svm = (SMOSVM<double[]>)ois.readObject();
			ois.close();
		}
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 
		catch (ClassNotFoundException e) {e.printStackTrace();}
		
		/*
		 * Download Dico
		 */
		dicoCenters = new ArrayList<double[][]>();
		dicoSigma   = new ArrayList<double[]>();
		for(String codebook : configFile.getDicoHsv()){
			//visual codebook
			ObjectInputStream oin;
			try {
				oin = new ObjectInputStream(new FileInputStream(codebook));
				dicoCenters.add((double[][]) oin.readObject());
				dicoSigma.add((double[]) oin.readObject());
				oin.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * 
	 * @param imageIn1
	 * @param imageIn2
	 */
	public double run(BufferedImage image1,BufferedImage image2){
		if(!isInialize){
			System.err.println("we must initialize algorithm");
			System.exit(-1);
		}
		
		/* extract the visible part of web site */
		image1 = image1.getSubimage(0, 0, image1.getWidth(),min(image1.getHeight(),1000)); 
		image2 = image2.getSubimage(0, 0, image2.getWidth(),min(image2.getHeight(),1000));
		
		/* compute the HSV descriptor */		
		ArrayList<Descriptor> colorDesc1=new ArrayList<Descriptor>();
		ArrayList<Descriptor> colorDesc2=new ArrayList<Descriptor>();
		CreateIHSVectors.run(image1,  colorDesc1,  8,  3,  6,  12,  6, 800, true);
		CreateIHSVectors.run(image2,  colorDesc2,  8,  3,  6,  12,  6, 800, true);
		
		ArrayList<ArrayList<double[]> > histo1 = new ArrayList<ArrayList<double[]> >();
		ArrayList<ArrayList<double[]> > histo2 = new ArrayList<ArrayList<double[]> >();
		//parameters of clustering
		int knn = 10;
		String scales = "1x1";
		boolean l1_vectors = false;					
		/* BoW*/
		for(int i=0 ; i<dicoSigma.size() ; i++){
			try {
				histo1.add(SpatialPyramids.run(colorDesc1, dicoCenters.get(i), dicoSigma.get(i), knn, scales, l1_vectors));
				histo2.add(SpatialPyramids.run(colorDesc2, dicoCenters.get(i), dicoSigma.get(i), knn, scales, l1_vectors));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/* parameter of distance*/
		String distmean = "TRUE";
		String gamma = "1.0";
		boolean bComptuteMeanDist = false;
		/* create the visual couple feature descriptors */
		ArrayList<Double> pairDesc = new ArrayList<Double> ();
		for(int i=0 ; i<histo1.size() ; i++){
				IndexedKernel.run(histo1.get(i), histo2.get(i),pairDesc, distmean, gamma, bComptuteMeanDist,true);			
				IndexedKernel.run(histo1.get(i), histo2.get(i),pairDesc, distmean, gamma, bComptuteMeanDist,false);
		}
		/* convert ArrayList in array of double*/
		double[] pairDescTest = new double[pairDesc.size()];
		int i=0;
		for(Double a : pairDesc)
			pairDescTest[i++]=a.doubleValue();

		/* run SVM*/
		double res = svm.valueOf(pairDescTest);
		System.out.println("Distance between the two web-pages:: "+res);
		return res;
	}
	
	/**
	 * Prend un fichier de configuration, regénére les descripteurs visuels et structurel si il n'existe pas et lance la fonction de distance sur les deux images
	 * Si on ne dispose uniquement des descripteurs visuels ou structurelles, le classifieur correspondant sera utilisé
	 * @param args le path du fichier de configuration du marcalizer
	 */
	public static void main(String[] args) {
		ScapeTest sc= new ScapeTest();
		sc.init(new File(args[0]));
		try {
			sc.run(ImageIO.read(new File(args[1])),ImageIO.read(new File(args[2])));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
			
			/** End **/
	}
	/**
	 * This function compute the minimum between a and b
	 * @param a the first param
	 * @param b the second param
	 * @return min(a,b)
	 */
	private static int min(int a, int b) {
		return a<b?a:b;
	}
}
