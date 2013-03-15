package Scape;

import java.io.FileWriter;
import java.io.IOException;

//import JDescriptors.CreateIHSVectors;
//import JDescriptors.CreateIHSVectors2;
//import JDescriptors.SpatialPyramids;
//import JDescriptors.VisualCodeBook;
import JKernelMachines.fr.lip6.kernel.IndexedKernel;
//import fr.lip6.jdescriptors.bof.SpatialPyramidFactory.Coding;
//import fr.lip6.jdescriptors.bof.SpatialPyramidFactory.Norm;
//import fr.lip6.jdescriptors.bof.SpatialPyramidFactory.Pooling;

public class ScapeTrain {
	public static void main(String[] args) {
		String rPrincipal = "/home/suredac/Scape/MainScape/";
		String rDeltas = rPrincipal+"xml/";
		/*String rImagettes = rPrincipal+"Images/Imagettes";
		String rPartieVisible = rPrincipal+"Images/PartieVisible";
		String rPages = rPrincipal+"Images/Pages";
		String rSiftPages = rPrincipal+"sift/Pages/";
		String rSiftImagettes = rPrincipal+"sift/Imagettes";
		String rSiftPartieVisible = rPrincipal+"sift/PartieVisible";
		String rHSVPages = rPrincipal+"HSV/Pages";
		String rHSVImagettes = rPrincipal+"HSV/Imagettes";
		String rHSVPartieVisible = rPrincipal+"HSV/PartieVisible";
		String rDicoSift100 = rPrincipal+"Dicos/sift/100/output.obj";
		String rDicoSift200 = rPrincipal+"Dicos/sift/200/output.obj";
		String rDicoHSV100 = rPrincipal+"Dicos/color/100/output.obj";
		String rDicoHSV200 = rPrincipal+"Dicos/color/200/output.obj";*/
		String rHistoSiftPage100 = rPrincipal+"Histogrammes/Pages/sift/100";
		String rHistoSiftPartieVisible100 = rPrincipal+"Histogrammes/PartieVisible/sift/100";
		//String rHistoSiftImagette100 = rPrincipal+"Histogrammes/Imagettes/sift/100";
		String rHistoSiftPage200 = rPrincipal+"Histogrammes/Pages/sift/200";
		String rHistoSiftPartieVisible200 = rPrincipal+"Histogrammes/PartieVisible/sift/200";
		//String rHistoSiftImagette200 = rPrincipal+"Histogrammes/Imagettes/sift/200";
		String rHistoHSVPage100 = rPrincipal+"Histogrammes/Pages/color/100";
		String rHistoHSVPartieVisible100 = rPrincipal+"Histogrammes/PartieVisible/color/100";
		//String rHistoHSVImagette100 = rPrincipal+"Histogrammes/Imagettes/color/100";
		String rHistoHSVPage200 = rPrincipal+"Histogrammes/Pages/color/200";
		String rHistoHSVPartieVisible200 = rPrincipal+"Histogrammes/PartieVisible/color/200";
		//String rHistoHSVImagette200 = rPrincipal+"Histogrammes/Imagettes/color/200";
		String m = rPrincipal+"Base/M.txt";
		String rSVM = rPrincipal+"svm";

		/*int I =8;
		int H =3;
		int S =6;
		int s =12;
		int r =6;
		int maxHeight =800;
		boolean onlyTop= false;
		
		int nbCluster;
		int nbPoints = 100000;
		int maxPoints = 500;
		boolean l1norm = false;
		
		Coding coding = Coding.HARD;;
		Pooling pooling = Pooling.SUM;
		int knn = 10;
		Norm norm = Norm.NONE;
		String scales = "1x1";
		boolean l1_vectors = false;
		*/
		String distmean = "TRUE";
		String gamma = "1.0";
		boolean bComptuteMeanDist = false;
		
		/**
		 * ColorDescriptor
		 * IN: Page
		 * IN: Partie visible
		 * IN: Imagettes
		 * OUT: Fichiers SIFT Page
		 * OUT: Fichiers SIFT imagettes
		 * OUT: Fichiers SIFT Partie visible
		 */
//		ColorDescriptor.run(rPages, rSiftPages,"/home/suredac/Scape/CodeMarc/codejava/colordescriptors/x86_64-linux-gcc/");
////	ColorDescriptor2.run(rImagettes, rSiftImagettes);
//		ColorDescriptor.run(rPartieVisible, rSiftPartieVisible,"/home/suredac/Scape/CodeMarc/codejava/colordescriptors/x86_64-linux-gcc/");
		
		/**
		 * CreateIHSVectors
		 * IN: Page
		 * IN: Partie visible
		 * IN: Imagettes
		 * IN: Paramètres HSV
		 * OUT: Fichiers HSV Page
		 * OUT: Fichiers HSV imagettes
		 * OUT: Fichiers HSV Partie visible
		 */
//		CreateIHSVectors.run( rPages,  rHSVPages,  I,  H,  S,  s,  r,  maxHeight, onlyTop);
//		onlyTop=true;
//		CreateIHSVectors.run( rPartieVisible,  rHSVPartieVisible,  I,  H,  S,  s,  r,  maxHeight, onlyTop);
//		onlyTop=false;
////	CreateIHSVectors2.run( rImagettes,  rHSVImagettes,  I,  H,  S,  s,  r,  maxHeight, onlyTop);
		
		/**
		 * VisualCodeBook
		 * IN: Fichiers HSV
		 * IN: Fichiers SIFT
		 * OUT: dico SIFT 100
		 * OUT: dico SIFT 200
		 * OUT: dico HSV 100
		 * OUT: dico HSV 200
		 */
//		nbCluster = 100;
//		VisualCodeBook.run(rSiftPages, rDicoSift100, nbCluster, nbPoints, maxPoints, l1norm);
//		nbCluster = 200;
//		VisualCodeBook.run(rSiftPages, rDicoSift200, nbCluster, nbPoints, maxPoints, l1norm);
//		nbCluster = 100;
//		VisualCodeBook.run(rHSVPages, rDicoHSV100, nbCluster, nbPoints, maxPoints, l1norm);
//		nbCluster = 200;
//		VisualCodeBook.run(rHSVPages, rDicoHSV200, nbCluster, nbPoints, maxPoints, l1norm);
		
		/**
		 * SpatialPyramide
		 * IN: dico SIFT 100
		 * IN: dico SIFT 200
		 * IN: dico HSV 100
		 * IN: dico HSV 200
		 * IN: HSV Page
		 * IN: HSV Partie visible
		 * IN: HSV Imagettes
		 * IN: SIFT Page
		 * IN: SIFT partie visible
		 * IN: SIFT imagettes
		 * IN: parametres histogramme
		 * OUT: Histogramme HSV Page 100 
		 * OUT: Histogramme HSV Page 200 
		 * OUT: Histogramme SIFT Page 100 
		 * OUT: Histogramme SIFT Page 200 
		 * OUT: Histogramme HSV Partie visible 100 
		 * OUT: Histogramme HSV Partie visible 200 
		 * OUT: Histogramme SIFT Partie visible 100 
		 * OUT: Histogramme SIFT Partie visible 200 
		 * OUT: Histogramme HSV Imagettes 100 
		 * OUT: Histogramme HSV Imagettes 200 
		 * OUT: Histogramme SIFT Imagettes 100 
		 * OUT: Histogramme SIFT Imagettes 200 
		 */
		try {
//			SpatialPyramids.run(rSiftPages, rHistoSiftPage100, rDicoSift100, knn, scales, l1_vectors);
//			SpatialPyramids.run(rSiftPartieVisible, rHistoSiftPartieVisible100, rDicoSift100, knn, scales, l1_vectors);
////			SpatialPyramids2.run(rSiftImagettes, rHistoSiftImagette100, rDicoSift100, knn, scales, l1_vectors);
//			SpatialPyramids.run(rSiftPages, rHistoSiftPage200, rDicoSift200, knn, scales, l1_vectors);
//			SpatialPyramids.run(rSiftPartieVisible, rHistoSiftPartieVisible200, rDicoSift200, knn, scales, l1_vectors);
////			SpatialPyramids2.run(rSiftImagettes, rHistoSiftImagette200, rDicoSift200, knn, scales, l1_vectors);
//			
//			SpatialPyramids.run(rHSVPages, rHistoHSVPage100, rDicoHSV100, knn, scales, l1_vectors);
//			SpatialPyramids.run(rHSVPartieVisible, rHistoHSVPartieVisible100, rDicoHSV100, knn, scales, l1_vectors);
////			SpatialPyramids2.run(rHSVImagettes, rHistoHSVImagette100, rDicoHSV100, knn, scales, l1_vectors);
//			SpatialPyramids.run(rHSVPages, rHistoHSVPage200, rDicoHSV200, knn, scales, l1_vectors);
//			SpatialPyramids.run(rHSVPartieVisible, rHistoHSVPartieVisible200, rDicoHSV200, knn, scales, l1_vectors);
////			SpatialPyramids2.run(rHSVImagettes, rHistoHSVImagette200, rDicoHSV200, knn, scales, l1_vectors);
			
		}catch (Exception e) {e.printStackTrace();}
		
		/**
		 * IndexedKernel
		 * IN: Toutes les OUT de SpatialPyramide
		 * OUT: Le vecteur des distances
		 */		/*
		IndexedKernel.run(rHistoSiftPartieVisible100, "", distmean, gamma, bComptuteMeanDist,true,m);
		IndexedKernel.run(rHistoSiftPartieVisible100, "", distmean, gamma, bComptuteMeanDist,false,m);
		IndexedKernel.run(rHistoSiftPartieVisible200, "", distmean, gamma, bComptuteMeanDist,true,m);
		IndexedKernel.run(rHistoSiftPartieVisible200, "", distmean, gamma, bComptuteMeanDist,false,m);
		IndexedKernel.run(rHistoSiftPage100, "", distmean, gamma, bComptuteMeanDist,true,m);
		IndexedKernel.run(rHistoSiftPage100, "", distmean, gamma, bComptuteMeanDist,false,m);
		IndexedKernel.run(rHistoSiftPage200, "", distmean, gamma, bComptuteMeanDist,true,m);
		IndexedKernel.run(rHistoSiftPage200, "", distmean, gamma, bComptuteMeanDist,false,m);
		IndexedKernel.run(rHistoHSVPage100, "", distmean, gamma, bComptuteMeanDist,true,m);
		IndexedKernel.run(rHistoHSVPage100, "", distmean, gamma, bComptuteMeanDist,false,m);
		IndexedKernel.run(rHistoHSVPage200, "", distmean, gamma, bComptuteMeanDist,true,m);
		IndexedKernel.run(rHistoHSVPage200, "", distmean, gamma, bComptuteMeanDist,false,m);
		IndexedKernel.run(rHistoHSVPartieVisible100, "", distmean, gamma, bComptuteMeanDist,true,m);
		IndexedKernel.run(rHistoHSVPartieVisible100, "", distmean, gamma, bComptuteMeanDist,false,m);
		IndexedKernel.run(rHistoHSVPartieVisible200, "", distmean, gamma, bComptuteMeanDist,true,m);
		IndexedKernel.run(rHistoHSVPartieVisible200, "", distmean, gamma, bComptuteMeanDist,false,m);
		*/
//		IndexedKernel2.run(rHistoSiftImagette100, "", distmean, gamma, bComptuteMeanDist,true,m);
//		IndexedKernel2.run(rHistoSiftImagette100, "", distmean, gamma, bComptuteMeanDist,false,m);
//		IndexedKernel2.run(rHistoSiftImagette200, "", distmean, gamma, bComptuteMeanDist,true,m);
//		IndexedKernel2.run(rHistoSiftImagette200, "", distmean, gamma, bComptuteMeanDist,false,m);
//		IndexedKernel2.run(rHistoHSVImagette100, "", distmean, gamma, bComptuteMeanDist,true,m);
//		IndexedKernel2.run(rHistoHSVImagette100, "", distmean, gamma, bComptuteMeanDist,false,m);
//		IndexedKernel2.run(rHistoHSVImagette200, "", distmean, gamma, bComptuteMeanDist,true,m);
//		IndexedKernel2.run(rHistoHSVImagette200, "", distmean, gamma, bComptuteMeanDist,false,m);

		/**
		 * XMLDescriptors
		 * IN: Arbres delta
		 * OUT: Caractéristiques structurelles pour chaque couple
		 */
//		XMLDescriptors.Main.run(rDeltas,m);
		
		/**
		 * Ajout des etiquettes
		 */
		FileWriter writer = null;
		String texte = "0 2 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 " +
				"0 1 2 1 1 1 1 0 1 1 2 1 1 2 1 1 1 0 1 1 0 1 1 1 1 1 1 1 1 1 2 1 0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 " +
				"1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 2 1 1 2 2 2 1 1 1 1 1 1 1 1 0 1 1 1 1 1 1 1 2 1 1 2 1 1 1 1 1 1" +
				" 1 0 1 2 1 1 1 1 1 1 1 1 1 1 1 2 1 1 1 1 2 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 1 1 1 1 1 1 1 1 1 1 1 1";
		try{
		     writer = new FileWriter(m, true);
		     writer.write(texte,0,texte.length());
		     writer.close();
		}catch(IOException ex){
		    ex.printStackTrace();
		}
		
		/**
		 * Classification
		 * IN: Concatenation de toutes les OUT de XMLDescriptors et IndexedKernel
		 * OUT: Les perfs
		 */
		Classification.learn(m,rSVM);		
	}
}
