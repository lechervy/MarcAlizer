package Scape;
import java.io.File;
import java.io.IOException;


public class ColorDescriptor {
	/**
	 * Permet de calculer les descripteurs SIFT d'un dossier d'images
	 * @param input dossiers contenant les images a traiter
	 * @param output dossiers où placer les descripteurs SIFT correspondant à chaque image 
	 * @param colorDescriptor path du programme de conversion
	 */
	public static void run(File repInput,String new_path,String colorDescriptor){
		String s;
		String out;
		Process p;
					
		File featureDir = new File(new_path+"/"+repInput.getName());
		if(!featureDir.exists()){
			if(!featureDir.mkdirs()){
				System.err.println("Erreur dans la création du repertoire "+new_path+"/"+repInput.getName());
				System.exit(-1);
			}
		}
		for(File img: repInput.listFiles()){
			if(img.getName().substring(img.getName().length()-4).equals(".png")){
				s = repInput.getAbsolutePath()+"/"+img.getName();
				out = new_path+"/"+repInput.getName()+"/"+img.getName().substring(0, img.getName().length()-4);
				try {
					p = Runtime.getRuntime().exec(/*cd+"*/colorDescriptor+" "+s+" --detector densesampling --ds_spacing 24 --descriptor sift --output "+out+".txt");
					p.waitFor();
				} catch (IOException e) {e.printStackTrace();}
				catch (InterruptedException e) {e.printStackTrace();}
			}
			
		}

	}
	public static void run_old(String input,String output,String colorDescriptor){
		File repImages = new File(input);
		File new_path = new File(output);
		//String cd = colorDescriptor;
		String s;
		String out;
		Process p;
		double tmpSIFTPage;
		//double tmpTotal = 0;
		//double i=0;
		
		for(File f: repImages.listFiles()){			
			File featureDir = new File(new_path+"/"+f.getName());
			if(!featureDir.exists()){
				if(!featureDir.mkdirs()){
					System.err.println("Erreur dans la création du repertoire "+new_path+"/"+f.getName());
					System.exit(-1);
				}
			}/*
			try {
				Runtime.getRuntime().exec("mkdir "+new_path+"/"+f.getName());
			} catch (IOException e) {e.printStackTrace();}*/
			for(File f2: f.listFiles()){
				if(f2.getName().substring(f2.getName().length()-4).equals(".png")){
					s = repImages+"/"+f.getName()+"/"+f2.getName();
					out = new_path+"/"+f.getName()+"/"+f2.getName().substring(0, f2.getName().length()-4);
					try {
						tmpSIFTPage = System.currentTimeMillis();
						p = Runtime.getRuntime().exec(/*cd+"*/colorDescriptor+" "+s+" --detector densesampling --ds_spacing 24 --descriptor sift --output "+out+".txt");
						p.waitFor();
						tmpSIFTPage = System.currentTimeMillis()-tmpSIFTPage;
						//tmpTotal += tmpSIFTPage;
						//i++;
					} catch (IOException e) {e.printStackTrace();}
					catch (InterruptedException e) {e.printStackTrace();}
				}
				
			}
		}
//		System.out.println("\t##############################");
//		System.out.println("\tSIFT Page:\t"+(tmpTotal/(1000*i))+" secondes");
//		System.out.println("\t##############################");
	}
}
