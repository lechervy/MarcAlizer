package fr.lip6.jdescriptors.test;

import java.util.ArrayList;

import fr.lip6.jdescriptors.color.ColorVQDescriptorCreator;
import fr.lip6.jdescriptors.color.ColorVQFloatDescriptor;
import fr.lip6.jdescriptors.color.model.IHSColorQuantizer;
import fr.lip6.jdescriptors.detector.HoneycombDetector;
import fr.lip6.jdescriptors.io.XMLWriter;

public class TestXMLWriter {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if(args.length < 2)
		{
			System.out.println("usage : TestXMLWriter <image> <output>");
			return;
		}
		
		ColorVQDescriptorCreator c = ColorVQDescriptorCreator.getInstance();
		
		//honeycomb patches
		HoneycombDetector detector = new HoneycombDetector(12, 12);
		c.setDetector(detector);
		
		int x = 8, y = 6, z = 3;
//		RGBColorQuantizer q = new RGBColorQuantizer(x, y, z); 
		IHSColorQuantizer q = new IHSColorQuantizer(x, y, z);
		c.setQuantizer(q);
		
		c.setNormalize(false);
		
		ArrayList<ColorVQFloatDescriptor> d = c.createDescriptors(args[0]);

		XMLWriter.writeXMLFile(args[1], d, true);
		
	}

}
