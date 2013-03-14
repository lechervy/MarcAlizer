package Scape;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * @author mkyong
 *
 */
public class Resize {

	private static final double IMG_WIDTH_RATIO = 0.7;
	private static final double IMG_HEIGHT_RATIO = 0.7;
	private static final String path = "/home/suredac/Scape/MainScape/Images/Pages/";
	private static final String final_path = "/home/suredac/Scape/MainScape/Images/Pages_resize/";

	public static void main(String [] args){
		String[] dossiers = new File(path).list();
		File[] images;
		File dossier;
		File new_dossier;
		
		for(int f=0; f<dossiers.length;f++){
			dossier = new File(dossiers[f]);
			images = new File(path+dossier).listFiles();
			for(int f2=0;f2<images.length;f2++){
				try{
					BufferedImage originalImage = ImageIO.read(images[f2]);
					int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

					BufferedImage resizeImagePng = resizeImage(originalImage, type);
					new_dossier = new File(final_path+dossier);
					if(!new_dossier.exists())
						new_dossier.mkdir();
					ImageIO.write(resizeImagePng, "png", new File(final_path+dossier+"/"+(f2+1)+".png")); 

				}catch(IOException e){System.out.println(e.getMessage());}
			}
		}
	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int type){
		int new_width = (int)(originalImage.getWidth()*IMG_WIDTH_RATIO);
		int new_height = (int)(originalImage.getHeight()*IMG_HEIGHT_RATIO);
		BufferedImage resizedImage = new BufferedImage(new_width,new_height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, new_width, new_height, null);
		g.dispose();

		return resizedImage;
	}
}