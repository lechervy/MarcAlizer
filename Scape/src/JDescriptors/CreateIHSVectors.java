package JDescriptors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fr.lip6.jdescriptors.color.ColorVQDescriptorCreator;
import fr.lip6.jdescriptors.color.ColorVQFloatDescriptor;
import fr.lip6.jdescriptors.color.model.IHSColorQuantizer;
import fr.lip6.jdescriptors.detector.HoneycombDetector;
import fr.lip6.jdescriptors.io.XMLWriter;

public class CreateIHSVectors {
	/**
	 * Permet de calcul un descriteur HSV pour chaque image d'un dossier
	 * @param srcDir dossier des images source
	 * @param dstDir dossier des descripteurs a généré
	 * @param I
	 * @param H
	 * @param S
	 * @param s
	 * @param r
	 * @param maxHeight
	 * @param onlyTop
	 */
	public static void run(File srcDir, String dstDir, int I, int H, int S, int s, int r, int maxHeight, boolean onlyTop) {
		double temp = 0;
		
		ColorVQDescriptorCreator c = ColorVQDescriptorCreator.getInstance();
		
		//Descriptor creator
		HoneycombDetector detector = new HoneycombDetector(s, r);
		c.setDetector(detector); 
		IHSColorQuantizer q = new IHSColorQuantizer(I, H, S);
		c.setQuantizer(q);
		c.setNormalize(false);
				
		if(!srcDir.exists() || !srcDir.isDirectory())
		{
			System.out.println(srcDir+" : No such directory !");
			return;
		}
		
		(new File(dstDir+"/"+srcDir.getName())).mkdir();
		for(String f : srcDir.list())
		{
			if (!(f.endsWith(".png"))){
				continue;
			}
			try
			{
				if (!(new File(dstDir+srcDir.getName()+"/"+f.substring(0, f.indexOf('.'))+".xgz").exists())) {
					temp = System.currentTimeMillis();
					ArrayList<ColorVQFloatDescriptor> list = c.createDescriptors(srcDir.getAbsolutePath()+"/"+f, maxHeight, onlyTop);
					temp = System.currentTimeMillis() - temp;
					try{
						XMLWriter.writeXMLFile(dstDir+"/"+srcDir.getName()+"/"+f.substring(0, f.indexOf('.')), list, true);
					}catch (IOException e) {
						// TODO: handle exception
					}
				}
			}
			catch(Exception ioe)
			{
				System.err.println("no descriptors for "+srcDir.getName());
				ioe.printStackTrace();
			}		
		}

	}
	
	public static void run_old(String srcDir, String dstDir, int I, int H, int S, int s, int r, int maxHeight, boolean onlyTop) {
		// TODO Auto-generated method stub
		
		double temp = 0;
		//double tmp_total = 0;
		//int nb_rep=0;
		
		ColorVQDescriptorCreator c = ColorVQDescriptorCreator.getInstance();
		//System.out.println(" s = " + s + " ; r = " + s + " ; cut = " + cut);
		
		//Descriptor creator
		HoneycombDetector detector = new HoneycombDetector(s, r);
		c.setDetector(detector); 
		IHSColorQuantizer q = new IHSColorQuantizer(I, H, S);
		c.setQuantizer(q);
		c.setNormalize(false);
		
		File src = new File(srcDir);
		if(!src.exists() || !src.isDirectory())
		{
			System.out.println(srcDir+" : No such directory !");
			return;
		}
		
		String[] files2 = src.list();
		if(files2 == null)
		{
			System.out.println("No files in "+srcDir);
			return;
		}
		//(new File(dstDir)).mkdir();
		for (String f2 : files2) {
//			System.out.println("f2 = " + dstDir+"/"+f2);

			(new File(dstDir+"/"+f2)).mkdir();
			for(String f : new File(srcDir+"/"+f2).list())
			{
				if (!(f.endsWith(".png"))){
					continue;
				}
				try
				{
					//if (!(new File(dstDir+f2+"/"+f.substring(0, f.indexOf('.'))+".xgz").exists()) && !f.equals("280.png") && !f.equals("281.png") ){
					//System.out.println(f);
//					System.out.println("lecture de " + srcDir+"/"+f2+"/"+f);
					if (!(new File(dstDir+f2+"/"+f.substring(0, f.indexOf('.'))+".xgz").exists())) {
						temp = System.currentTimeMillis();
						ArrayList<ColorVQFloatDescriptor> list = c.createDescriptors(srcDir+"/"+f2+"/"+f, maxHeight, onlyTop);
						temp = System.currentTimeMillis() - temp;
						//tmp_total += temp;
						//nb_rep++;
						try{
							XMLWriter.writeXMLFile(dstDir+"/"+f2+"/"+f.substring(0, f.indexOf('.')), list, true);
						}catch (IOException e) {
							// TODO: handle exception
						}
//						System.out.println(f+" descriptor written : taille = " + list.size());
					}
				}
				catch(Exception ioe)
				{
					System.err.println("no descriptors for "+f2);
					ioe.printStackTrace();
				}
			}
		}

//		System.out.println("\t##############################");
//		System.out.println("\tHSV Page:\t"+(tmp_total/(1000*nb_rep))+" secondes");
//		System.out.println("\t##############################");
	}

}
