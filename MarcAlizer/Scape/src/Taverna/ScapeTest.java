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
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import JDescriptors.CreateIHSVectors;
import JDescriptors.SpatialPyramids;
import JKernelMachines.fr.lip6.kernel.IndexedKernel;
import Scape.Classification;
import Scape.ColorDescriptor;

public class ScapeTest {

	/**
	 * Prend un fichier de configuration, regénére les descripteurs visuels et structurel si il n'existe pas et lance la fonction de distance sur les deux images
	 * Si on ne dispose uniquement des descripteurs visuels ou structurelles, le classifieur correspondant sera utilisé
	 * @param args le path du fichier de configuration du marcalizer
	 */
	public static void main(String[] args) {
		
		if (args.length < 1){
			System.out.println("USAGE: marcalizer <fichier de configuration 1> <fichier de configuration 2> ...");
			System.out.println("USAGE: marcalizer -rep <repertoire de fichier de configuration>");
			System.exit(-1);
		}
		ArrayList<String> nomsFichiersConfig = new ArrayList<String>(); 
		if(args[0].equals("-rep")){
			if(args.length != 2){
				System.out.println("USAGE: marcalizer -rep <repertoire de fichier de configuration>");
				System.exit(-1);
			}
			File repFichierConfig= new File(args[1]);
			File []listeFichierConfig = repFichierConfig.listFiles();
			for(File fC:listeFichierConfig )
				nomsFichiersConfig.add(fC.getAbsolutePath());
		}else
			for(int i= 0 ; i<args.length ; i++)
				nomsFichiersConfig.add(args[i]);
		
		for(int indiceCouple2=0 ; indiceCouple2<nomsFichiersConfig.size() ; indiceCouple2++ ){
			int indiceCouple = indiceCouple2+1;
			try{
			/** lecture du fichier de configuration du Marcalizer **/	
			FileConfig	fichierConfig = FileConfig.deserializeXMLToObject(nomsFichiersConfig.get(indiceCouple2));//FileConfig.deserializeXMLToObject(args[0]);
			/** creation de l'arborescence de sortie dans le repertoire de travail si necessaire **/
			//creation du rep des sorties
			File fOutput = new File(fichierConfig.getWorkDirectory()+"out");
			if(!fOutput.exists()){
				if(!fOutput.mkdir()){
					System.err.println("Erreur lors de la creation du repertoire de sortie: "+fichierConfig.getWorkDirectory()+"out");
					System.exit(-1);
				}
			}
			
			//fichier contenant le descripteur de couples
			String mTest = fOutput.getAbsolutePath()+"/M.txt";
			String page1 = fichierConfig.getSnapshot1Png();
			String page2 = fichierConfig.getSnapshot2Png();
			
			/** Copie des images d'entrée dans le dossier "images" **/
			String rPages = fOutput.getAbsolutePath()+"/images";
			FileChannel in1 = null , in2= null;
			FileChannel out1= null , out2= null;
			File fOut1=null;
			File fOut2=null;
			try {
				in1 = new FileInputStream(page1).getChannel();
				in2 = new FileInputStream(page2).getChannel();
				fOut1 = new File(rPages + "/"+indiceCouple);
				fOut2 = new File(rPages + "/"+indiceCouple);
				if(!fOut1.exists())
					fOut1.mkdirs();
				if(!fOut2.exists())
					fOut2.mkdirs();
				fOut1 = new File(rPages + "/"+indiceCouple+"/"+"page1.png");
				fOut2 = new File(rPages + "/"+indiceCouple+"/"+"page2.png");
				System.out.println(fOut1.getAbsolutePath());
				if(!fOut1.exists())
					fOut1.createNewFile();
				if(!fOut2.exists())
					fOut2.createNewFile();
				out1 = new FileOutputStream(fOut1).getChannel();
				out2 = new FileOutputStream(fOut2).getChannel();
				in1.transferTo(0, in1.size(), out1);
				in2.transferTo(0, in2.size(), out2);
			} 
			catch (IOException e) {e.printStackTrace();} 
			finally{
				try {
					in1.close();
					in2.close();
					out1.close();
					out2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//si les images existes
			if(fichierConfig.isImgValid()){
				/*************************************** Calcul de la partie visible des sites web ******************************/
				String rPartieVisible = fOutput.getAbsolutePath()+"/imagesPartieVisible";
				File fImages = new File(rPartieVisible+"/"+indiceCouple);
				if(!fImages.exists()){
					if(!fImages.mkdirs()){
						System.err.println("Erreur lors de la creation du repertoire de sortie des parties visibles des images: "+fImages.getAbsolutePath());
						System.exit(-1);
					}
					try {
						BufferedImage imageIn1 = ImageIO.read(fOut1);
						BufferedImage imageOut1 = imageIn1.getSubimage(0, 0, imageIn1.getWidth(),min(imageIn1.getHeight(),1000)); 
						//scale(imageIn1,imageIn1.getWidth(),min(imageIn1.getHeight(),1000));
						ImageIO.write(imageOut1,"PNG",new File(rPartieVisible + "/"+indiceCouple+"/"+"page1.png"));
						BufferedImage imageIn2 = ImageIO.read(fOut2);
						BufferedImage imageOut2 = imageIn2.getSubimage(0, 0, imageIn2.getWidth(),min(imageIn2.getHeight(),1000));
						//scale(imageIn2,imageIn2.getWidth(),min(imageIn2.getHeight(),1000));
						ImageIO.write(imageOut2,"PNG",new File(rPartieVisible + "/"+indiceCouple+"/"+"page2.png"));

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					/*
					Process p;
					try {
						String cutPartieVisible = fichierConfig.getBinDecoupage();//rInput+"decoupe_image_python_sift.py";
						p= Runtime.getRuntime().exec("python "+cutPartieVisible+" "+ rPages +" "+rPartieVisible);
						//System.out.println("python "+cutPartieVisible+" "+ rPages +" "+rPartieVisible);
						//System.exit(1);
						p.waitFor();
					} 
					catch (IOException e) {e.printStackTrace();} 
					catch (InterruptedException e) {e.printStackTrace();}*/
				}
				/*****************************************************************************************************************/
				
				/******************************************* Calcul des SIFT ****************************************************/
	
				System.out.println("SIFT Computation...");
				String rSiftPartieVisible = fOutput.getAbsolutePath()+"/features/sift";
				File fSift = new File(rSiftPartieVisible);
				if(!fSift.exists() || !fichierConfig.hasSift()){
					if(!fSift.mkdirs()){
						System.err.println("Erreur lors de la creation du repertoire de sortie des Sift: "+rSiftPartieVisible);
						System.exit(-1);
					}
				}
				
				if(!(new File(rSiftPartieVisible+"/"+indiceCouple)).exists()){
					//recuperation du path du programme permettant de generer les descripteurs SIFT
					String programmeSift = fichierConfig.getBinSIFT();
					ColorDescriptor.run(fImages, rSiftPartieVisible,programmeSift);
				}
				
				/*****************************************************************************************************************/
				
				/********************************************* Calcul des HSV *****************************************************/
				String rHSVPartieVisible = fOutput.getAbsolutePath()+"/features/hsv";
				File fHSV = new File(rHSVPartieVisible);
				if(!fHSV.exists() || !fichierConfig.hasHsv()){
					if(!fHSV.mkdirs()){
						System.err.println("Erreur lors de la creation du repertoire de sortie des HSV: "+rSiftPartieVisible);
						System.exit(-1);
					}					
				}
				if(!(new File(rHSVPartieVisible+"/"+indiceCouple)).exists()){
					CreateIHSVectors.run(fImages,  rHSVPartieVisible,  8,  3,  6,  12,  6, 800, true);
				}
				/*****************************************************************************************************************/
				
				/********************************************** Calcul des histogramme *******************************************/
				System.out.println("BOW Computation...");
				ArrayList<String> pathHistoSift = new ArrayList<String>();
				ArrayList<String> pathHistoHSV = new ArrayList<String>();
				//paramatre du clustering
				int knn = 10;
				String scales = "1x1";
				boolean l1_vectors = false;			
				try {
					/** Histogramme de SIFT **/
					int nSiftDico=0;
					for(String rDicoSift : fichierConfig.getDicoSift()){
						nSiftDico++;
						String rHistoSiftPartieVisible=fOutput.getAbsolutePath()+"/bow/sift/dico"+nSiftDico;
						File fHistoSiftPartieVisible = new File(rHistoSiftPartieVisible);
						if(!fHistoSiftPartieVisible.exists()){
							if(!fHistoSiftPartieVisible.mkdirs()){
								System.err.println("Erreur lors de la creation du repertoire de sortie des histogrammes de Sift: "+rHistoSiftPartieVisible);
								System.exit(-1);
							}
						}
						if(!(new File(rHistoSiftPartieVisible+"/"+indiceCouple)).exists())
							SpatialPyramids.run(new File(rSiftPartieVisible+"/"+indiceCouple), rHistoSiftPartieVisible+"/"+indiceCouple, rDicoSift, knn, scales, l1_vectors);
						
						pathHistoSift.add(rHistoSiftPartieVisible);
					}
	
					/** Histogramme de HSV **/
					int nHSVDico=0;
					for(String rDicoHSV : fichierConfig.getDicoHsv()){
						nHSVDico++;
						String rHistoHSVPartieVisible=fOutput.getAbsolutePath()+"/bow/hsv/dico"+nHSVDico;
						File fHistoHSVPartieVisible = new File(rHistoHSVPartieVisible);
						if(!fHistoHSVPartieVisible.exists()){
							if(!fHistoHSVPartieVisible.mkdirs()){
								System.err.println("Erreur lors de la creation du repertoire de sortie des histogrammes de HSV: "+rHistoHSVPartieVisible);
								System.exit(-1);
							}													
						}
						if(!(new File(rHistoHSVPartieVisible+"/"+indiceCouple)).exists())
							SpatialPyramids.run(new File(rHSVPartieVisible+"/"+indiceCouple), rHistoHSVPartieVisible+"/"+indiceCouple, rDicoHSV, knn, scales, l1_vectors);
						pathHistoHSV.add(rHistoHSVPartieVisible);
					}		
				}catch (Exception e) {e.printStackTrace();}
		
				/*****************************************************************************************************************/
				
				/** Calcul des caractéristiques visuelles **/
				System.out.println("Visual Features Computation...");			
	
				File f_mTest = new File(mTest);
				FileWriter fwriter_mTest=null;

				if(f_mTest.exists())
					f_mTest.delete();
				
				try {
					fwriter_mTest = new FileWriter(f_mTest,true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				String distmean = "TRUE";
				String gamma = "1.0";
				boolean bComptuteMeanDist = false;
				for(String rHistoSiftPartieVisible : pathHistoSift){
					System.out.println(rHistoSiftPartieVisible);
					IndexedKernel.run(new File(rHistoSiftPartieVisible+"/"+indiceCouple),fwriter_mTest, distmean, gamma, bComptuteMeanDist,true);
					IndexedKernel.run(new File(rHistoSiftPartieVisible+"/"+indiceCouple),fwriter_mTest, distmean, gamma, bComptuteMeanDist,false);
					//IndexedKernel.run_old(rHistoSiftPartieVisible, "", distmean, gamma, bComptuteMeanDist,true,mTest);
					//IndexedKernel.run_old(rHistoSiftPartieVisible, "", distmean, gamma, bComptuteMeanDist,false,mTest);
				}
				
				for(String rHistoHSVPartieVisible : pathHistoHSV){
					IndexedKernel.run(new File(rHistoHSVPartieVisible+"/"+indiceCouple),fwriter_mTest, distmean, gamma, bComptuteMeanDist,true);
					IndexedKernel.run(new File(rHistoHSVPartieVisible+"/"+indiceCouple),fwriter_mTest, distmean, gamma, bComptuteMeanDist,false);
					//IndexedKernel.run_old(rHistoHSVPartieVisible, "", distmean, gamma, bComptuteMeanDist,true,mTest);
					//IndexedKernel.run_old(rHistoHSVPartieVisible, "", distmean, gamma, bComptuteMeanDist,false,mTest);
				}
				try {
					fwriter_mTest.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/** Calcul des caractéristiques structurelles **/
			if(fichierConfig.isStructValid()){
				System.out.println("Structural Features Computation...");
				XMLDescriptors.Main.run(new File(fichierConfig.getVips1()),new File(fichierConfig.getVips2()),new File(fichierConfig.getVidiff()),mTest);
			}
			
			/** Calcul du score SVM **/
			String rSVM = fichierConfig.getBinSVM();
			System.out.println("Distance entre les deux pages:");
			Classification.test(mTest,rSVM);
			
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			/** End **/
		}
	}
	private static int min(int a, int b) {
		return a<b?a:b;
	}
	/** 
	 * Redimensionne une image.
	 * 
	 * @param source Image à redimensionner.
	 * @param width Largeur de l'image cible.
	 * @param height Hauteur de l'image cible.
	 * @return Image redimensionnée.
	 *//*
	public static BufferedImage scale(BufferedImage source, int width, int height) {
	    return source.getSubimage(0, 0, width, height);	    
	}*/

}
