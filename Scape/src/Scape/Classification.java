package Scape;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import JKernelMachines.fr.lip6.classifier.SMOSVM;
import JKernelMachines.fr.lip6.evaluation.Evaluator;
import JKernelMachines.fr.lip6.kernel.typed.DoubleLinear;
import JKernelMachines.fr.lip6.type.TrainingSample;

/**
 * Adaptation du fichier svm_training_detecte_les_mauvais.m écrit en Matlab
 * Cette classe:
 * - Lit un fichier de données contenant les descripteurs
 * - Lance un apprentissage
 * - Affiche les performances
 * @author suredac
 *
 */
public class Classification {

	static SMOSVM<double[]> svm;
	
	@SuppressWarnings("unchecked")
	public static void learn(String xyApp, String rSVM) {
		int nb_experiences = 30;
		int k = 20;
		ArrayList<double[]> testX = new ArrayList<double[]>();
		ArrayList<Integer> testY = new ArrayList<Integer>();
		ArrayList<double[]> trainingX = new ArrayList<double[]>();
		ArrayList<Integer> trainingY = new ArrayList<Integer>();
		ArrayList<Integer> indicesTest = new ArrayList<Integer>();
		FileReader fr;
		BufferedReader r = null;
		String line = "";
		
		/** Lecture du fichier contenant les descripteurs **/
		try {
			fr = new FileReader(xyApp);
			r = new BufferedReader(fr);
			line = r.readLine();
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		
		/** Lecture de la matrice **/
		ArrayList<ArrayList<Double>> lecture = new ArrayList<ArrayList<Double>>();	
		ArrayList<Double> lignes;
		while (line != null) {
			String[] decompose = line.split(" ");
			lignes = new ArrayList<Double>();
			for(int i=0;i<decompose.length;i++) 
				lignes.add(Double.parseDouble(decompose[i]));
			lecture.add(lignes);
			try {
				line = r.readLine();
			}catch (IOException e) {e.printStackTrace();}
		}
		
		/** Transposée de la matrice pour avoir une observation par ligne **/
		ArrayList<ArrayList<Double>> m = new ArrayList<ArrayList<Double>>();
		for(int i=0; i<lecture.get(0).size();i++)
			m.add(new ArrayList<Double>());
		for (int i=0; i<lecture.size();i++)
			for(int j=0;j<lecture.get(0).size();j++){
				m.get(j).add(lecture.get(i).get(j));
			}
		
		/** Changement de l'etiquette "0" (qui deviens -1) car SMOSVM != Matlab SVM **/
		for(int i=0; i<m.size(); i++)
			if(m.get(i).get(m.get(i).size()-1)==0)
				m.get(i).set(m.get(i).size()-1, (double)-1);
		
		/** Ajout des indices **/
		for (int i = 0; i<m.size(); i++) 
			m.get(i).add((double)i); 
		
		/** Bas Gauche et Haut Droite de la matrice de confusion **/
		final double[] BG = new double[m.size()];	
		final double[] HD = new double[m.size()];
		for(int i=0;i<m.size();i++){
			BG[i]=0.;
			HD[i]=0.;
		}
		
		/** Suppression des observations ambigües (etiquette = 2) **/
		/** et réorganisation des données (pas de mélange entre identiques et differents)**/
		ArrayList<ArrayList<Double>> mIDPos = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> mIDNeg = new ArrayList<ArrayList<Double>>();
		int nbIdentiques = 0;
		int nbDifferents = 0;
		for (int i=0; i<m.size(); i++)
			if (m.get(i).get(m.get(i).size()-2) != 2)
				if (m.get(i).get(m.get(i).size()-2) == 1){
					nbIdentiques++;
					mIDPos.add(m.get(i));
				}else{
					nbDifferents++;
					mIDNeg.add(m.get(i));
				}

		mIDPos.addAll(mIDNeg);
		m = (ArrayList<ArrayList<Double>>)mIDPos.clone();
		mIDPos.clear();
		mIDNeg.clear();		
		
		int[][] confusion = new int[2][2];
		for(int i=0;i<2; i++)
			for(int j=0; j<2; j++)
				confusion [i][j]=0;

		double[] big_result = new double[nb_experiences];
		
		/** Selection des variables **/
//		0. sift top 100 L2
//		1. sift top 100 L1
//		2. sift top 200 L2
//		3. sift top 200 L1
//		4. sift 100 L2
//		5. sift 100 L1
//		6. sift 200 L2
//		7. sift 200 L1
//		8. color 100 L2
//		9. color 100 L1
//		10. color 200 L2
//		11. color 200 L1
//		12. color top 100 L2
//		13. color top 100 L1
//		14. color top 200 L2
//		15. color top 200 L1
//		16. sift ad hoc block 100 L2
//		17. sift ad hoc block 100 L1
//		18. sift ad hoc block 200 L2
//		19. sift ad hoc block 200 L1
//		20. color ad hoc block 100 L2
//		21. color ad hoc block 100 L1
//		22. color ad hoc block 200 L2
//		23. color ad hoc block 200 L1
//		24. same structure
//		25. contains update
//		26. contains delete
//		27. contains insert
//		28. nb update in the tree
//		29. nb delete in the tree
//		30. nb insert in the tree
//		31. contains no delete nor insert
//		32. nb deleted blocks
//		33. nb inserted blocks
//		34. nb updates blocks
//		35. Source contains links
//		36. Version contains links
//		37. Source contains images
//		38. Version contains images
//		39. jaccard index of links between source and version
//		40. jaccard index of images between source and version
//		41. jaccard index of ID links between source and version
//		42. jaccard index of ID images between source and version
//		43. max ratio delete
//		44. min ratio delete
//		45. max ratio update
//		46. min ratio update
//		47. max ratio insert
//		48. min ratio insert
//		49. nb blocks in source
//		50. nb blocks in version
		ArrayList<ArrayList<Double>> mVarSelection = new ArrayList<ArrayList<Double>>();
		for(int i=0;i<m.size();i++){
			mVarSelection.add(new ArrayList<Double>());
			
			mVarSelection.get(i).addAll(m.get(i).subList(0, 4));
			mVarSelection.get(i).addAll(m.get(i).subList(12, 16));
//			mVarSelection.get(i).addAll(m.get(i).subList(4, 12));
			mVarSelection.get(i).add(m.get(i).get(31));
			mVarSelection.get(i).add(m.get(i).get(32));
			mVarSelection.get(i).add(m.get(i).get(m.get(i).size()-2));
			mVarSelection.get(i).add(m.get(i).get(m.get(i).size()-1));
			
			
//			mVarSelection.get(i).addAll(m.get(i).subList(0, 34));
//			mVarSelection.get(i).addAll(m.get(i).subList(35, 46));
//			mVarSelection.get(i).addAll(m.get(i).subList(47, m.get(i).size()));
			
//			mVarSelection.get(i).addAll(m.get(i).subList(0, 25));
		}
		m = (ArrayList<ArrayList<Double>>)mVarSelection.clone();
		mVarSelection.clear();		
		
		/**Données centralisées réduites**/
		for(int i=0;i<m.get(0).size()-2;i++){
			double mean = 0;
			double std = 0;
			for(int j=0;j<m.size();j++)
				mean += m.get(j).get(i);
			mean /= m.size();
			
			for(int j=0;j<m.size();j++)
				std += Math.pow(m.get(j).get(i)-mean,2);
			std /= m.size();
			std = Math.sqrt(std);

			for(int j=0;j<m.size();j++)
				m.get(j).set(i, (m.get(j).get(i)-mean)/std);
		}
		
		/**
		 * Affichage de la matrice
		 */
//		System.out.println("\t\tSIZE: "+m.size()+"\t"+m.get(0).size());
//		for(int i=0;i<m.size();i++){
//			for(int j=0;j<m.get(0).size();j++)
//				System.out.print(m.get(i).get(j)+"\t");
//			System.out.println();
//		}
		
		/** Boucle principale **/
		for (int expe=0; expe<nb_experiences; expe++){
			System.out.println("Expérience "+(expe+1) + "/"+nb_experiences);
			/** Création des sous ensembles pour la cross validation **/
			/** (traduction de la ligne :)
			 * free_list1 = [(mod(randperm(size(I,1)), k) + 1), (mod(size(I,1) + randperm(size(D,1)), k) + 1)] ;
			 */
			
			/** randperm(size(I,1)) **/
			int[] permutationI = new int[nbIdentiques];
			for(int i=0; i<nbIdentiques; i++)
				permutationI[i]=i;
			for (int i = permutationI.length - 1; i > 0; i--) {
			    int w = (int)Math.floor(Math.random() * (i+1));
			    int temp = permutationI[w];
			    permutationI[w] = permutationI[i];
			    permutationI[i] = temp;
			}
			
			/** (mod(randperm(size(I,1)), k) + 1) **/
			for(int i=0; i<permutationI.length; i++)
				permutationI[i]= permutationI[i]%k;
			
			/** randperm(size(D,1)) **/
			int[] permutationD = new int[nbDifferents];
			for(int i=0; i<nbDifferents; i++)
				permutationD[i]=i;
			for (int i = permutationD.length - 1; i > 0; i--) {
			    int w = (int)Math.floor(Math.random() * (i+1));
			    int temp = permutationD[w];
			    permutationD[w] = permutationD[i];
			    permutationD[i] = temp;
			}
			
			/** (mod(size(I,1) + randperm(size(D,1)), k) + 1) **/
			for(int i=0; i<permutationD.length; i++)
				permutationD[i]= (permutationD[i]+nbIdentiques)%k;
			
			int[] free_list = new int[nbIdentiques+nbDifferents];
			for(int i=0; i<nbIdentiques; i++)
				free_list[i] = permutationI[i];
			for(int i=0; i<nbDifferents; i++)
				free_list[nbIdentiques+i]=permutationD[i];
			
			int nb_good = 0;
			
			/** Cross validation **/
			for (int i=0; i<k; i++){
				System.out.print("\tk: "+(i+1)+"/"+k+"\t");
				testX = new ArrayList<double[]>();
				testY = new ArrayList<Integer>();
				trainingX = new ArrayList<double[]>();
				trainingY = new ArrayList<Integer>();
				indicesTest = new ArrayList<Integer>();
				/** Construction des ensembles d'apprentissage et test 
				 * Traduction des lignes
				 * TestSamples = free_list==i
				 * testX = M(TestSamples,1:(end-2))
				 * testY = M(TestSamples,end-1);
				 * IndicesTest = M(TestSamples,end);
				 * trainingX = M(~TestSamples,1:(end-2));
				 * trainingY = M(~TestSamples,end-1);
				 */
				for(int j=0; j<free_list.length; j++)
					if(free_list[j]==i){
						double[] subList = new double[m.get(j).size()-2];
						for(int l=0; l<m.get(j).size()-2; l++)
							subList[l]=m.get(j).get(l);
						testX.add(subList);
						testY.add(m.get(j).get(m.get(j).size()-2).intValue());
						indicesTest.add(m.get(j).get(m.get(j).size()-1).intValue());
					}else{
						double[] subList = new double[m.get(j).size()-2];
						for(int l=0; l<m.get(j).size()-2; l++)
							subList[l]=m.get(j).get(l);
						trainingX.add(subList);
						trainingY.add(m.get(j).get(m.get(j).size()-2).intValue());
					}				
				
				/**Apprentissage du SVM et calcul des performances**/				
//				DoubleGaussL2 kernel = new DoubleGaussL2();
				DoubleLinear kernel = new DoubleLinear();
				
				/** Calcul du gamma optimal**/
//				int nbBow = trainingX.size();
//				double gamma=0;
//				for(int p=0;p<nbBow;p++)
//					for(int j=0;j<nbBow;j++)
//						gamma+=distance(trainingX.get(p),trainingX.get(j));
//				
//				gamma=gamma/(nbBow*nbBow);
//				gamma=1/gamma;
//				kernel.setGamma(gamma);
//				System.out.println("------------------------gamma: "+gamma);
				
				svm = new SMOSVM<double[]>(kernel);
				svm.setC(1);
				svm.setVerbosityLevel(0);
				ArrayList<TrainingSample<double[]>> trainingSamples = new ArrayList<TrainingSample<double[]>>();
				for(int j=0; j<trainingX.size(); j++)
					trainingSamples.add(new TrainingSample<double[]>(trainingX.get(j), trainingY.get(j)));
				ArrayList<TrainingSample<double[]>> testSamples = new ArrayList<TrainingSample<double[]>>();
				for(int j=0; j<testX.size(); j++)
					testSamples.add(new TrainingSample<double[]>(testX.get(j), testY.get(j)));
				
				Evaluator<double[]> evaluator = new Evaluator<double[]>(svm, trainingSamples, testSamples);
				System.out.print("...");
				evaluator.evaluate();
				System.out.println("OK");
				
				/**
				 * Sauvegarde du SVM
				 */
				try {
					FileOutputStream fos = new FileOutputStream(rSVM);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(svm);
					oos.close();
				}
				catch (FileNotFoundException e) {e.printStackTrace();} 
				catch (IOException e) {e.printStackTrace();}
				
				
//				System.out.println("testing MAP: "+evaluator.getTestingMAP());
//				System.out.println("training MAP: "+evaluator.getTrainingMAP());
				
				/** Calcul de la matrice de confusion et comptage des erreurs pour chaque observation**/
				/** (traduction de la ligne: wrong_sample=IndicesTest(((TestResult == 1).*(Test == 0))~=0)') **/
				for(int j=0;j<testSamples.size();j++){
					if(svm.valueOf(testSamples.get(j).sample)>0 && testSamples.get(j).label==1){ 
						confusion[0][0]++; 
						nb_good++; 
					}
					else if(svm.valueOf(testSamples.get(j).sample)<0 && testSamples.get(j).label==-1){
						confusion[1][1]++; 
						nb_good++; 
					}
					else if(svm.valueOf(testSamples.get(j).sample)<0 && testSamples.get(j).label==1){
						confusion[0][1]++;
						HD[indicesTest.get(j)] ++;
					}
					else if(svm.valueOf(testSamples.get(j).sample)>0 && testSamples.get(j).label==-1){
						confusion[1][0]++;
						BG[indicesTest.get(j)] ++;
					}
				}
			}
			big_result[expe] = nb_good/(double)m.size();
		}
		
		/** Affichage des résultats **/
		
		/** Il faut trier les observation par rapport au nombre d'erreurs
		 * Or, il faut garder les indices pour pouvoir faire la correspondance entre
		 * nombre d'erreurs et observation (car si on trie par nombre d'erreurs on
		 * perd la référence à l'observation)
		 */
		final Integer[] idx = new Integer[m.size()];
		for(int i=0;i<m.size();i++)
			idx[i]=i;
		Arrays.sort(idx, new Comparator<Integer>() {
		    @Override public int compare(final Integer o1, final Integer o2) {
		    	int r = Double.compare(BG[o1], BG[o2]);
		    	if(r==1) return -1;
		    	if(r==-1) return 1;
		    	return 0;
		    }
		});
		System.out.println("Indices BG: ");
		for(int i=0; i<idx.length; i++)
			System.out.println(idx[i]+"\t"+BG[idx[i]]);
		
		Arrays.sort(idx, new Comparator<Integer>() {
		    @Override public int compare(final Integer o1, final Integer o2) {
		    	int r = Double.compare(HD[o1], HD[o2]);
		    	if(r==1) return -1;
		    	if(r==-1) return 1;
		    	return 0;
		    }
		});
		System.out.println("Indices HD: ");
		for(int i=0; i<idx.length; i++)
			System.out.println(idx[i]+"\t"+HD[idx[i]]);
		
		/** Affichage de la matrice de confusion **/
		System.out.println("Matrice de confusion:");
		System.out.println("\t"+confusion[0][0]/(double)nb_experiences+"   "+confusion[0][1]/(double)nb_experiences);
		System.out.println("\t"+confusion[1][0]/(double)nb_experiences+"   "+confusion[1][1]/(double)nb_experiences);
		
		/** Traduction des lignes
		 * confusion2 = confusion ./ nb_experiences
		 * confusion ./ repmat((sum(confusion,2)),1,2)
		 * (confusion(1,1)+confusion(2,2))/(sum(sum(confusion)))
		 */
		
		
		/** Affichage de la moyenne de bonne prédiction **/
		double sum = 0;
		for(int i=0; i<big_result.length; i++)
			sum += big_result[i];
		System.out.println("Moyenne de bonne prédiction: "+ sum/big_result.length);
		
		/** Affichage de la std **/
		double std = 0;
		double mean = sum/big_result.length;
		for(int i=0; i<big_result.length; i++)
			std += Math.pow(big_result[i]-mean,2);
		std /= big_result.length;
		std = Math.sqrt(std);
		System.out.println("Ecart type: "+ std);
		
	}
	
	public static void test(String m, String rSVM){
		FileReader fr = null;
		try {
			fr = new FileReader(m);
		}catch (FileNotFoundException e1) {e1.printStackTrace();};
		BufferedReader r = new BufferedReader(fr);;
		String line = "";
		double[] exemple = null;
		
		/** Lecture du fichier contenant le descripteur **/
		ArrayList<Double> lignes = new ArrayList<Double>();
		try {
			line = r.readLine();
		}catch (IOException e1) {e1.printStackTrace();}
		//lignes.add(Double.parseDouble(line));
		while (line != null){
			try {
				lignes.add(Double.parseDouble(line));
				line = r.readLine();
			}catch (IOException e) {e.printStackTrace();}
		}

		exemple = new double[lignes.size()];
		for(int i=0;i<lignes.size();i++) {
			exemple[i] = lignes.get(i);
		System.err.println("..."+exemple[i]);
		}
		
		/**
		 * Chargement du SVM
		 */
		try {
			FileInputStream fis = new FileInputStream(rSVM);
			ObjectInputStream ois = new ObjectInputStream(fis);
			svm = (SMOSVM<double[]>)ois.readObject();
			ois.close();
		}
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 
		catch (ClassNotFoundException e) {e.printStackTrace();}
		
		
		System.out.println(svm.valueOf(exemple));
	}
	
	/**Calcule la distance**/
	static double distance(double[] x1, double[] x2){
		int N = x1.length;
		double sum=0.;
		for(int k=0; k<N;k++){
			if((x1[k]+ x2[k])<Double.MIN_VALUE) continue;
			sum+= (Math.pow(x1[k]-x2[k], 2));
		}
		return sum;
	}

}
