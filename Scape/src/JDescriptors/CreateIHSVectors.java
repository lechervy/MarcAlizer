package JDescriptors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fr.lip6.jdescriptors.Descriptor;
import fr.lip6.jdescriptors.color.ColorVQDescriptorCreator;
import fr.lip6.jdescriptors.color.ColorVQFloatDescriptor;
import fr.lip6.jdescriptors.color.model.IHSColorQuantizer;
import fr.lip6.jdescriptors.detector.HoneycombDetector;
import fr.lip6.jdescriptors.io.XMLWriter;

public class CreateIHSVectors {
	/**
	 * Compute the HSV descriptors
	 * @param img the source image
	 * @param desc the list of output HSV descriptor
	 * @param I
	 * @param H
	 * @param S
	 * @param s
	 * @param r
	 * @param maxHeight
	 * @param onlyTop
	 */
	public static void run(BufferedImage img, ArrayList<Descriptor> desc, int I, int H, int S, int s, int r, int maxHeight, boolean onlyTop) {
		double temp = 0;
		
		ColorVQDescriptorCreator c = ColorVQDescriptorCreator.getInstance();
		
		//Descriptor creator
		HoneycombDetector detector = new HoneycombDetector(s, r);
		c.setDetector(detector); 
		IHSColorQuantizer q = new IHSColorQuantizer(I, H, S);
		c.setQuantizer(q);
		c.setNormalize(false);
		desc.addAll((ArrayList<Descriptor>) c.createDescriptors(img, maxHeight, onlyTop));		
	}

}
