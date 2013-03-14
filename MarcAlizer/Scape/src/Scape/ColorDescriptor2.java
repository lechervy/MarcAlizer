package Scape;
import java.io.File;
import java.io.IOException;


public class ColorDescriptor2 {
	public static void run(String input,String output){
		File repImages = new File(input);
		File new_path = new File(output);
		String cd = "/home/suredac/Scape/CodeMarc/codejava/colordescriptors/x86_64-linux-gcc/";
		String s;
		String out;
		Process p;
		for(File f: repImages.listFiles()){
			try {
				p= Runtime.getRuntime().exec("mkdir "+new_path+"/"+f.getName());
				p.waitFor();
			} 
			catch (IOException e) {e.printStackTrace();} 
			catch (InterruptedException e) {	e.printStackTrace();}
			for(File f3: f.listFiles()){
				try {
					p = Runtime.getRuntime().exec("mkdir "+new_path+"/"+f.getName()+"/"+f3.getName());
					p.waitFor();
				}
				catch (IOException e) {e.printStackTrace();} 
				catch (InterruptedException e) {e.printStackTrace();}
				for(File f2: f3.listFiles()){
					if(f2.getName().substring(f2.getName().length()-4).equals(".png")){
						s = repImages+"/"+f.getName()+"/"+f3.getName()+"/"+f2.getName();
						out = new_path+"/"+f.getName()+"/"+f3.getName()+"/"+f2.getName().substring(0, f2.getName().length()-4);
						try {
							p = Runtime.getRuntime().exec(cd+"colorDescriptor "+s+" --detector densesampling --ds_spacing 12 --descriptor sift --output "+out+".txt");
							p.waitFor();
						} 
						catch (IOException e) {e.printStackTrace();} 
						catch (InterruptedException e) {e.printStackTrace();}
					}

				}
			}
		}
	}
}
