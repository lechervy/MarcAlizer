package JKernelMachines.fr.lip6.kernel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

@SuppressWarnings("static-access")
public class IndexedKernel {
	/**
	 * Permet de calculer la distance entre deux sites pour un descripteur donnée
	 * @param dirInput repertoire contenant les descriteurs des deux sites
	 * @param writer fichier de sortie
	 * @param distmean
	 * @param gamma
	 * @param bComptuteMeanDist
	 * @param norm_L2
	 */
	public static void run(File dirInput,FileWriter writer,String distmean,String gamma,boolean bComptuteMeanDist,boolean norm_L2) {
		
		ArrayList<ArrayList<double[]>> bows = new ArrayList<ArrayList<double[]>> ();

		// parsing input directory containing bows
		double distance = 0;
		bows.clear();
		File[] listeF= dirInput.listFiles();
		for (File f: listeF){
			try{
				System.out.println("input+filename2 " + f.getAbsolutePath());
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
				ArrayList<double[]> value = (ArrayList<double[]>)in.readObject();
				for (double[] d : value) {
					double sum_L2 = 0;
					double sum_L1 = 0;
					for (int j = 0 ; j < d.length ; ++j) {
							sum_L2 += d[j] * d[j];
							sum_L1 += Math.abs(d[j]);
					}
					for (int j = 0 ; j < d.length ; ++j) {
						if (norm_L2){
							d[j] /= Math.sqrt(sum_L2);
						}
						else {
							d[j] /= sum_L1;
						}
					}
				}
				bows.add(value);
				in.close();
			}
			catch(ClassNotFoundException e){
				e.printStackTrace();
			}
			catch(IOException e){
				e.printStackTrace();
			}			
		}
		boolean source = (listeF[0].getName().compareToIgnoreCase(listeF[1].getName()) < 0);
		double[] bow1 = bows.get(source?0:1).get(0);
		double[] bow2 = bows.get(source?1:0).get(0);
		double distance_L2 = 0;
		double distance_L1 = 0;
		for (int l = 0 ; l < bow1.length ; ++l) {
			distance_L2 += (bow1[l] - bow2[l])*(bow1[l] - bow2[l]); // L2
			if ((bow1[l] + bow2[l]) != 0)
				distance_L1 += ((bow1[l] - bow2[l])*(bow1[l] - bow2[l]))/(bow1[l] + bow2[l]); // chi 2
		}
		
		distance = (norm_L2) ? distance_L2 : distance_L1;
		
		System.out.print(distance + " ");
		
		
		String texte = "";
		texte += distance + "\n";
		
		try{
			writer.write(texte);//,0,texte.length());
			writer.flush();
		}catch(IOException ex){
		    ex.printStackTrace();
		}

		return;
		
	}
	
	public static void run_old(String input,String output,String distmean,String gamma,boolean bComptuteMeanDist,boolean norm_L2,String m) {
		
		ArrayList<ArrayList<double[]>> bows = new ArrayList<ArrayList<double[]>> ();

		// printing options
//		System.out.println("IndexKernel options: ");
//		System.out.println("input: " + input);
//		System.out.println("output: " + output);
//		System.out.println("meandist: " + distmean);
//		System.out.println("gamma: " + gamma);
//		System.out.println();

		// parsing input directory containing bows
		File[] f = (new File(input)).listFiles();
		if (f == null)
			return;
		double[] distances = new double[f.length];
		for (int i = 0; i < f.length; i++){
			String filename = f[i].getName();
			//System.out.println(filename);
			File[] f2 = f[i].listFiles();
			if (f2 == null)
			return;
			bows.clear();
			if(f2.length!=2)
				continue;
			for (int k = 0 ; k < f2.length ; ++k){
				String filename2 = f2[k].getName();
//				System.out.println(filename2);


				try{
					System.out.println("input+filename2 " + input+"/" +filename + "/" +filename2);
					ObjectInputStream in = new ObjectInputStream(new FileInputStream(input+"/"+filename + "/" +filename2));
					ArrayList<double[]> value = (ArrayList<double[]>)in.readObject();
					for (double[] d : value) {
						double sum_L2 = 0;
						double sum_L1 = 0;
						for (int j = 0 ; j < d.length ; ++j) {
//							System.out.print(" d[j] = "  + d[j]);
								sum_L2 += d[j] * d[j];
								sum_L1 += Math.abs(d[j]);
						}
//						System.out.println("sum_L2 = " + sum_L2);
//						System.out.println("sum_L1 = " + sum_L1);
//						System.out.println("norm = " + Math.sqrt(sum_L2));
						for (int j = 0 ; j < d.length ; ++j) {
							if (norm_L2){
								d[j] /= Math.sqrt(sum_L2);
							}
							else {
								d[j] /= sum_L1;
							}
						}
						//System.out.println("");
					}
					//System.out.println("");
					//System.out.println("value = " + value);
					bows.add(value);
					//bownames.add(filename.split("_")[1]);
					in.close();
				}
				catch(ClassNotFoundException e){
					e.printStackTrace();
				}
				catch(IOException e){
					e.printStackTrace();
				}			
			}
			boolean source = (f2[0].getName().compareToIgnoreCase(f2[1].getName()) < 0);
		    double[] bow1 = bows.get(source?0:1).get(0);
			double[] bow2 = bows.get(source?1:0).get(0);
			// System.out.println(bow1.length);
			// System.out.println(bow2.length);
			double distance_L2 = 0;
			double distance_L1 = 0;
			for (int l = 0 ; l < bow1.length ; ++l) {
				distance_L2 += (bow1[l] - bow2[l])*(bow1[l] - bow2[l]); // L2
				//System.out.println("dista, = " + distance_L1);
				if ((bow1[l] + bow2[l]) != 0)
					distance_L1 += ((bow1[l] - bow2[l])*(bow1[l] - bow2[l]))/(bow1[l] + bow2[l]); // chi 2
			}
//			System.out.println("distance = " + distance);
//			System.out.println("i: "+i);
//			System.out.println("f[i]: "+f[i].getName());
//			System.out.println("indice: "+ (Integer.valueOf(f[i].getName())-1));
			
			if(Integer.valueOf(f[i].getName())-1 < distances.length)
				distances[Integer.valueOf(f[i].getName())-1] = (norm_L2) ? distance_L2 : distance_L1;
		}
		for (int i = 0 ; i < distances.length ; ++i){
			System.out.print(distances[i] + " ");
		}
		FileWriter writer = null;
		String texte = "";
		for (int i = 0 ; i < distances.length -1; ++i){
			texte += distances[i] + " ";
		}
		texte += distances[distances.length-1] + "\n";
		try{
		     writer = new FileWriter(m, true);
		     writer.write(texte,0,texte.length());
		}catch(IOException ex){
		    ex.printStackTrace();
		}finally{
		  if(writer != null){
		     try {
				writer.close();
			} catch (IOException e) {e.printStackTrace();}
		  }
		}

		return;
		
	}

}