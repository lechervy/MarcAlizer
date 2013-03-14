package Scape;

import JDescriptors.SpatialPyramids;
//import fr.lip6.jdescriptors.bof.SpatialPyramidFactory.Coding;
//import fr.lip6.jdescriptors.bof.SpatialPyramidFactory.Norm;
//import fr.lip6.jdescriptors.bof.SpatialPyramidFactory.Pooling;


public class ScapeTest {

	public static void main(String[] args) {
		String rPrincipal = "/home/suredac/Scape/MainScape/Test/";
		String rParent = "/home/suredac/Scape/MainScape/DonneesMarc/";
		//String rDeltas = rPrincipal+"xml/";
		//String rImagettes = rPrincipal+"Images/Imagettes";
		String rPartieVisible = rPrincipal+"Images/PartieVisible";
		String rPages = rPrincipal+"Images/Pages";
		String rSiftPages = rPrincipal+"sift/Pages/";
		//String rSiftImagettes = rPrincipal+"sift/Imagettes";
		String rSiftPartieVisible = rPrincipal+"sift/PartieVisible";
		//String rHSVPages = rPrincipal+"HSV/Pages";
		//String rHSVImagettes = rPrincipal+"HSV/Imagettes";
		//String rHSVPartieVisible = rPrincipal+"HSV/PartieVisible";
		String rDicoSift100 = rParent+"Dicos/sift/100/output.obj";
		String rDicoSift200 = rParent+"Dicos/sift/200/output.obj";
		//String rDicoHSV100 = rParent+"Dicos/color/100/output.obj";
		//String rDicoHSV200 = rParent+"Dicos/color/200/output.obj";
		String rHistoSiftPage100 = rPrincipal+"Histogrammes/Pages/sift/100";
		String rHistoSiftPartieVisible100 = rPrincipal+"Histogrammes/PartieVisible/sift/100";
		//String rHistoSiftImagette100 = rPrincipal+"Histogrammes/Imagettes/sift/100";
		String rHistoSiftPage200 = rPrincipal+"Histogrammes/Pages/sift/200";
		String rHistoSiftPartieVisible200 = rPrincipal+"Histogrammes/PartieVisible/sift/200";
		/*String rHistoSiftImagette200 = rPrincipal+"Histogrammes/Imagettes/sift/200";
		String rHistoHSVPage100 = rPrincipal+"Histogrammes/Pages/color/100";
		String rHistoHSVPartieVisible100 = rPrincipal+"Histogrammes/PartieVisible/color/100";
		String rHistoHSVImagette100 = rPrincipal+"Histogrammes/Imagettes/color/100";
		String rHistoHSVPage200 = rPrincipal+"Histogrammes/Pages/color/200";
		String rHistoHSVPartieVisible200 = rPrincipal+"Histogrammes/PartieVisible/color/200";
		String rHistoHSVImagette200 = rPrincipal+"Histogrammes/Imagettes/color/200";
		String mTest = rPrincipal+"MTest.txt";
		String cutImagettes = "/home/suredac/Scape/CodeMarc/python/decoupe_image_python.py";
		String cutPartieVisible = "/home/suredac/Scape/CodeMarc/python/decoupe_image_python_sift.py";
		int I =8;
		int H =3;
		int S =6;
		int s =12;
		int r =6;
		int maxHeight =800;
		boolean onlyTop= false;
		*/
		//Coding coding = Coding.HARD;;
		//Pooling pooling = Pooling.SUM;
		int knn = 10;
		//Norm norm = Norm.NONE;
		String scales = "1x1";
		boolean l1_vectors = false;
		/*
		String distmean = "TRUE";
		String gamma = "1.0";
		boolean bComptuteMeanDist = false;
		*/
		/**
		 * Test pour un couple
		 * Calcul du temps nécessaire
		 */
		/** Start **/
		//double tmpTotal = System.currentTimeMillis();
		
		/** Calcul des imagettes et partie visible **/
//		System.out.println("\t\tCalcul des imagettes et partie visible");
//		Process p;
//		try {
////			p= Runtime.getRuntime().exec("python "+cutImagettes);
////			p.waitFor();
//			p= Runtime.getRuntime().exec("python "+cutPartieVisible);
//			p.waitFor();
//		} 
//		catch (IOException e) {e.printStackTrace();} 
//		catch (InterruptedException e) {e.printStackTrace();}
//		double tmpImagettes = (System.currentTimeMillis()-tmpTotal)/1000;
//		System.out.println("\t##############################");
//		System.out.println("\tImagettes:\t"+tmpImagettes+" secondes");
//		System.out.println("\t##############################");
		
		/** Calcul des SIFT **/
		System.out.println("\t\tCalcul des SIFT");
//		double tmpSIFTPage = System.currentTimeMillis();
		ColorDescriptor.run(rPages, rSiftPages,"/home/suredac/Scape/CodeMarc/codejava/colordescriptors/x86_64-linux-gcc/");
//		tmpSIFTPage = (System.currentTimeMillis()-tmpSIFTPage)/1000;
//		System.out.println("\t##############################");
//		System.out.println("\tSIFT Page:\t"+tmpSIFTPage+" secondes");
//		System.out.println("\t##############################");
		
//		double tmpSIFTIma = System.currentTimeMillis();
//		ColorDescriptor2.run(rImagettes, rSiftImagettes);
//		tmpSIFTIma = (System.currentTimeMillis()-tmpSIFTIma)/1000;
		
//		double tmpSIFTVis = System.currentTimeMillis();
		ColorDescriptor.run(rPartieVisible, rSiftPartieVisible,"/home/suredac/Scape/CodeMarc/codejava/colordescriptors/x86_64-linux-gcc/");
//		tmpSIFTVis = (System.currentTimeMillis()-tmpSIFTVis)/1000;
//		System.out.println("\t##############################");
//		System.out.println("\tSIFT Bloc:\t"+tmpSIFTVis+" secondes");
//		System.out.println("\t##############################");
		
		/** Calcul des HSV **/
//		System.out.println("\t\tCalcul des HSV");
//		double tmpHSVPage = System.currentTimeMillis();
//		CreateIHSVectors.run( rPages,  rHSVPages,  I,  H,  S,  s,  r,  maxHeight, onlyTop);
//		onlyTop=true;
//		tmpHSVPage = (System.currentTimeMillis()-tmpHSVPage)/1000;
//		System.out.println("\t##############################");
//		System.out.println("\tHSV Page:\t"+tmpHSVPage+" secondes");
//		System.out.println("\t##############################");
		
//		double tmpHSVVis = System.currentTimeMillis();
//		CreateIHSVectors.run( rPartieVisible,  rHSVPartieVisible,  I,  H,  S,  s,  r,  maxHeight, onlyTop);
//		onlyTop=false;
//		tmpHSVVis = (System.currentTimeMillis()-tmpHSVVis)/1000;
//		System.out.println("\t##############################");
//		System.out.println("\tHSV Bloc:\t"+tmpHSVVis+" secondes");
//		System.out.println("\t##############################");
		
//		double tmpHSVIma = System.currentTimeMillis();
//		CreateIHSVectors2.run( rImagettes,  rHSVImagettes,  I,  H,  S,  s,  r,  maxHeight, onlyTop);
//		tmpHSVIma = (System.currentTimeMillis()-tmpHSVIma)/1000;
		
		/** Calcul des histogramme **/
		System.out.println("\t\tCalcul des histogrammes");
		/*double tmpHistoSiftPage100 = 0;
		double tmpHistoSiftVis100 = 0;
		double tmpHistoSiftIma100 = 0;
		double tmpHistoSiftPage200 = 0;
		double tmpHistoSiftVis200 = 0;
		double tmpHistoSiftIma200 = 0;
		double tmpHistoHSVPage100 = 0;
		double tmpHistoHSVVis100 = 0;
		double tmpHistoHSVIma100 = 0;
		double tmpHistoHSVPage200 = 0;
		double tmpHistoHSVVis200 = 0;
		double tmpHistoHSVIma200 = 0;*/
		try {
//			tmpHistoSiftPage100 = System.currentTimeMillis();
			SpatialPyramids.run(rSiftPages, rHistoSiftPage100, rDicoSift100, knn, scales, l1_vectors);
//			tmpHistoSiftPage100 = (System.currentTimeMillis()-tmpHistoSiftPage100)/1000;
//			tmpHistoSiftPage200 = System.currentTimeMillis();
			SpatialPyramids.run(rSiftPages, rHistoSiftPage200, rDicoSift200, knn, scales, l1_vectors);
//			tmpHistoSiftPage200 = (System.currentTimeMillis()-tmpHistoSiftPage200)/1000;
//			System.out.println("\t##############################");
//			System.out.println("\tBOW SIFT Page:\t"+tmpHistoSiftPage200+tmpHistoSiftPage100+" secondes");
//			System.out.println("\t##############################");
			
//			tmpHistoSiftVis100 = System.currentTimeMillis();
			SpatialPyramids.run(rSiftPartieVisible, rHistoSiftPartieVisible100, rDicoSift100, knn, scales, l1_vectors);
//			tmpHistoSiftVis100 = (System.currentTimeMillis()-tmpHistoSiftVis100)/1000;
//			tmpHistoSiftVis200 = System.currentTimeMillis();
			SpatialPyramids.run(rSiftPartieVisible, rHistoSiftPartieVisible200, rDicoSift200, knn, scales, l1_vectors);
//			tmpHistoSiftVis200 = (System.currentTimeMillis()-tmpHistoSiftVis200)/1000;
//			System.out.println("\t##############################");
//			System.out.println("\tBOW SIFT Vis:\t"+tmpHistoSiftVis200+tmpHistoSiftVis100+" secondes");
//			System.out.println("\t##############################");
			
//			tmpHistoSiftIma100 = System.currentTimeMillis();
//			SpatialPyramids2.run(rSiftImagettes, rHistoSiftImagette100, rDicoSift100, coding, pooling, knn, norm, scales, l1_vectors);
//			tmpHistoSiftIma100 = (System.currentTimeMillis()-tmpHistoSiftIma100)/1000;
			
//			tmpHistoSiftIma200 = System.currentTimeMillis();
//			SpatialPyramids2.run(rSiftImagettes, rHistoSiftImagette200, rDicoSift200, coding, pooling, knn, norm, scales, l1_vectors);
//			tmpHistoSiftIma200 = (System.currentTimeMillis()-tmpHistoSiftIma200)/1000;
			
//			tmpHistoHSVPage100 = System.currentTimeMillis();
//			SpatialPyramids.run(rHSVPages, rHistoHSVPage100, rDicoHSV100, coding, pooling, knn, norm, scales, l1_vectors);
//			tmpHistoHSVPage100 = (System.currentTimeMillis()-tmpHistoHSVPage100)/1000;
//			tmpHistoHSVPage200 = System.currentTimeMillis();
//			SpatialPyramids.run(rHSVPages, rHistoHSVPage200, rDicoHSV200, coding, pooling, knn, norm, scales, l1_vectors);
//			tmpHistoHSVPage200 = (System.currentTimeMillis()-tmpHistoHSVPage200)/1000;
//			System.out.println("\t##############################");
//			System.out.println("\tBOW HSV Page:\t"+tmpHistoHSVPage200+tmpHistoHSVPage100+" secondes");
//			System.out.println("\t##############################");
			
//			tmpHistoHSVVis100 = System.currentTimeMillis();
//			SpatialPyramids.run(rHSVPartieVisible, rHistoHSVPartieVisible100, rDicoHSV100, coding, pooling, knn, norm, scales, l1_vectors);
//			tmpHistoHSVVis100 = (System.currentTimeMillis()-tmpHistoHSVVis100)/1000;
//			tmpHistoHSVVis200 = System.currentTimeMillis();
//			SpatialPyramids.run(rHSVPartieVisible, rHistoHSVPartieVisible200, rDicoHSV200, coding, pooling, knn, norm, scales, l1_vectors);
//			tmpHistoHSVVis200 = (System.currentTimeMillis()-tmpHistoHSVVis200)/1000;
//			System.out.println("\t##############################");
//			System.out.println("\tBOW HSV Vis:\t"+tmpHistoHSVVis200+tmpHistoHSVVis100+" secondes");
//			System.out.println("\t##############################");
			
//			tmpHistoHSVIma100 = System.currentTimeMillis();
//			SpatialPyramids2.run(rHSVImagettes, rHistoHSVImagette100, rDicoHSV100, coding, pooling, knn, norm, scales, l1_vectors);
//			tmpHistoHSVIma100 = (System.currentTimeMillis()-tmpHistoHSVIma100)/1000;
			
//			tmpHistoHSVIma200 = System.currentTimeMillis();
//			SpatialPyramids2.run(rHSVImagettes, rHistoHSVImagette200, rDicoHSV200, coding, pooling, knn, norm, scales, l1_vectors);
//			tmpHistoHSVIma200 = (System.currentTimeMillis()-tmpHistoHSVIma200)/1000;
		}catch (Exception e) {e.printStackTrace();}

		/** Calcul des caractéristiques visuelles **/
//		System.out.println("\t\tCalcul des caractéristiques visuelles");
//		double tmpVisu = System.currentTimeMillis();
//		IndexedKernel.run(rHistoSiftPartieVisible100, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel.run(rHistoSiftPartieVisible100, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel.run(rHistoSiftPartieVisible200, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel.run(rHistoSiftPartieVisible200, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel.run(rHistoSiftPage100, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel.run(rHistoSiftPage100, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel.run(rHistoSiftPage200, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel.run(rHistoSiftPage200, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel.run(rHistoHSVPage100, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel.run(rHistoHSVPage100, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel.run(rHistoHSVPage200, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel.run(rHistoHSVPage200, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel.run(rHistoHSVPartieVisible100, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel.run(rHistoHSVPartieVisible100, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel.run(rHistoHSVPartieVisible200, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel.run(rHistoHSVPartieVisible200, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel2.run(rHistoSiftImagette100, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel2.run(rHistoSiftImagette100, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel2.run(rHistoSiftImagette200, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel2.run(rHistoSiftImagette200, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel2.run(rHistoHSVImagette100, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel2.run(rHistoHSVImagette100, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		IndexedKernel2.run(rHistoHSVImagette200, "", distmean, gamma, bComptuteMeanDist,true,mTest);
//		IndexedKernel2.run(rHistoHSVImagette200, "", distmean, gamma, bComptuteMeanDist,false,mTest);
//		tmpVisu = (System.currentTimeMillis()-tmpVisu)/1000;
//		System.out.println("\t##############################");
//		System.out.println("\tC. Visu:\t"+tmpVisu+" secondes");
//		System.out.println("\t##############################");
		
		/** Calcul des caractéristiques structurelles **/
//		System.out.println("\t\tCalcul des caractéristiques structurelles");
//		double tmpStru = System.currentTimeMillis();
//		XMLDescriptors.Main.run(rDeltas,mTest);
//		tmpStru = (System.currentTimeMillis()-tmpStru)/1000;
//		System.out.println("\t##############################");
//		System.out.println("\tC. Stru:\t"+tmpStru+" secondes");
//		System.out.println("\t##############################");
		
		/** Calcul du score SVM **/
//		System.out.println("\t\tCalcul du score SVM");
//		double tmpSVM = System.currentTimeMillis();
//		Classification.test(mTest);
//		tmpSVM = (System.currentTimeMillis()-tmpSVM)/1000;
//		System.out.println("\t##############################");
//		System.out.println("\tSVM:\t"+tmpSVM+" secondes");
//		System.out.println("\t##############################");
		
		/** End **/
//		tmpTotal = (System.currentTimeMillis()-tmpTotal)/1000;
//		
//		System.out.println("\t##############################");
//		System.out.println("TEMPS DE CALCUL: "+tmpTotal+" secondes (soit "+tmpTotal/60+" minutes)");
//		System.out.println("\t##############################");

		
//		System.out.println("\tSIFT Imag.:\t"+tmpSIFTIma+" secondes");
		
		
//		System.out.println("\tHSV Imag.:\t"+tmpHSVIma+" secondes");
//		System.out.println("\tBOW SIFT Page100:\t"+tmpHistoSiftPage100+" secondes");
//		System.out.println("\tBOW SIFT Page200:\t"+tmpHistoSiftPage200+" secondes");
		
//		System.out.println("\tBOW SIFT Ima100:\t"+tmpHistoSiftIma100+" secondes");
//		System.out.println("\tBOW SIFT Ima200:\t"+tmpHistoSiftIma200+" secondes");
//		System.out.println("\tBOW SIFT Ima:\t"+tmpHistoSiftIma200+tmpHistoSiftIma100+" secondes");
//		System.out.println("\tBOW SIFT Vis100:\t"+tmpHistoSiftVis100+" secondes");
//		System.out.println("\tBOW SIFT Vis200:\t"+tmpHistoSiftVis200+" secondes");
		
//		System.out.println("\tBOW HSV Page100:\t"+tmpHistoHSVPage100+" secondes");
//		System.out.println("\tBOW HSV Page200:\t"+tmpHistoHSVPage200+" secondes");
		
//		System.out.println("\tBOW HSV Ima100:\t"+tmpHistoHSVIma100+" secondes");
//		System.out.println("\tBOW HSV Ima200:\t"+tmpHistoHSVIma200+" secondes");
//		System.out.println("\tBOW HSV Ima:\t"+tmpHistoHSVIma200+tmpHistoHSVIma100+" secondes");
//		System.out.println("\tBOW HSV Vis100:\t"+tmpHistoHSVVis100+" secondes");
//		System.out.println("\tBOW HSV Vis200:\t"+tmpHistoHSVVis200+" secondes");
		
		
		
		
	}

}
