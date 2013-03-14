package JDescriptors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import fr.lip6.jdescriptors.Descriptor;
import fr.lip6.jdescriptors.bof.SpatialPyramidFactory;
import fr.lip6.jdescriptors.bof.SpatialPyramidFactory.Coding;
import fr.lip6.jdescriptors.bof.SpatialPyramidFactory.Norm;
import fr.lip6.jdescriptors.bof.SpatialPyramidFactory.Pooling;
import fr.lip6.jdescriptors.io.DescriptorReader;

public class SpatialPyramids2 {
		
	public static void run(String input,String output,String codebook,Coding coding,
			Pooling pooling,int knn,Norm norm,String scales,boolean l1_vectors) throws Exception
	{		
	    //printing options
	    System.out.println("SpatialPyramids options : ");
	    System.out.println("input : "+input);
	    System.out.println("output : "+output);
	    System.out.println("codebook : "+codebook);
	    System.out.println("knn : "+knn);
	    System.out.println("coding : "+coding);
	    System.out.println("pooling : "+pooling);
	    System.out.println("scales : "+scales);
	    System.out.println("norm : "+norm);
	    System.out.println("has norml1 : "+l1_vectors);
	    System.out.println();
	    
	    
	    //visual codebook
	    System.out.println("Reading visual codebook.");
		ObjectInputStream oin = new ObjectInputStream(new FileInputStream(codebook));
		double[][] centers = (double[][]) oin.readObject();
		double[] sigma = (double[]) oin.readObject();
		oin.close();
		
		
	    //parsing scales
	    ArrayList<int[]> listOfScales = new ArrayList<int[]>();
		for(String s : scales.trim().split("/+"))
		{
			String[] lc = s.split("x");
			if(lc.length != 2)
				continue;
			
			int[] linecols = new int[2];
			linecols[0] = Integer.parseInt(lc[0]);
			linecols[1] = Integer.parseInt(lc[1]);
			
			listOfScales.add(linecols);
			
		}
		System.out.println("processing scales : ");
		for(int[] s : listOfScales)
			System.out.println(Arrays.toString(s));
	    
		//letz go
		File directory2 = new File(input);
		if(!directory2.isDirectory())
		{
			System.out.println("input file "+directory2+" is not a directory.");
			System.exit(0);
		}
		File[] listFiles2 = directory2.listFiles();
		ArrayList<File> list = new ArrayList<File>();
		for (File f2 : listFiles2){
			//if (!f2.getName().equals("101")){
			//	continue;
			//}
			File[] listFiles3 = f2.listFiles();			
			for(File f3 : listFiles3) {
				File[] listFiles = f3.listFiles();			
		for(File f : listFiles) {
			/*
			System.out.println(f.getName());
			list.add(f);
		}
		int size = list.size()/46;
		//doing 46 sequential accesses only
		for(int i = 0 ; i < 46; i++)
		{*/
			long timeblock = System.currentTimeMillis();
			System.out.println("reading block "+f.getName());
			//reading all files in the current subset
			Map<File, ArrayList<Descriptor>> map = new Hashtable<File, ArrayList<Descriptor>>();
			//for(int j = i * size; j < Math.min((i+1)*size, list.size()); j++)
			//{
				//File f = list.get(j);
				if(f.exists())
				{
					//System.out.println(f.getPath());
					try{
					map.put(f, DescriptorReader.readFile(f.getAbsolutePath()));}
					catch (java.lang.NullPointerException e) {System.out.println(f.getAbsolutePath() + " is too small"); continue;}
					System.out.print(".");
				}
			//}
			System.out.println();
			System.out.println("put "+map.size()+" images in block in "+((System.currentTimeMillis()-timeblock)/1000.0)+"s.");
			
			List<File> fileList = new ArrayList<File>();
			fileList.addAll(map.keySet());
			//Collections.shuffle(fileList);
			//for(File f : fileList)
			{
				long filetime = System.currentTimeMillis();
				String destRep2 = output+"/"+f2.getName();
				File destRepF2 = new File(destRep2);
				if (!(destRepF2.exists())){
					destRepF2.mkdir();
				}
				String destRep = destRep2 + "/" + f3.getName();
				File destRepF = new File(destRep);
				if (!(destRepF.exists())){
					destRepF.mkdir();
				}

				String destName = destRep+"/"+(f.getName().substring(0, f.getName().indexOf(".")))+".obj";
				File outFile = new File(destName);

				if(outFile.exists())
				{
					System.out.println("not doing  : "+outFile+" ("+f+")");
					continue;
				}
				else
				{
					System.out.println("doing : "+outFile+" ("+f+")");
				}



				outFile.createNewFile();

				ArrayList<Descriptor> listOfWords = map.get(f);

				ArrayList<double[]> bag = new ArrayList<double[]>();
				//each scales
				for(int[] s : listOfScales)
				{
					System.out.println("doing scale "+Arrays.toString(s));
					SpatialPyramidFactory.lines = s[0];
					SpatialPyramidFactory.cols = s[1];

					SpatialPyramidFactory.knn = knn;
					SpatialPyramidFactory.coding = coding;
					SpatialPyramidFactory.pooling = pooling;

					SpatialPyramidFactory.norm = SpatialPyramidFactory.Norm.NONE;
					SpatialPyramidFactory.l1_norm = l1_vectors;

					ArrayList<double[]> l = SpatialPyramidFactory.createBagOfWindows(listOfWords, centers, sigma);

					bag.addAll(l);
				}
				long writetime = System.currentTimeMillis();
				System.out.println("done in "+(writetime-filetime)+"ms.");

				//writing
				ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(outFile));
				oout.writeObject(bag);
				oout.flush();
				oout.close();

				System.out.println("written "+outFile+" in "+(System.currentTimeMillis()-writetime)+"ms.");
				System.out.println();

			}
			
			System.out.println("block done in "+ ((System.currentTimeMillis()-timeblock)/1000.0)+"s.");
		}
		}
		}
	}


}
