package fr.lip6.jdescriptors.test;

import java.util.ArrayList;

import fr.lip6.jdescriptors.color.ColorVQDescriptorCreator;
import fr.lip6.jdescriptors.color.ColorVQFloatDescriptor;
import fr.lip6.jdescriptors.color.model.IHSColorQuantizer;
import fr.lip6.jdescriptors.detector.HoneycombDetector;
import fr.lip6.jdescriptors.io.XMLWriter;

public class TestColorVQFloatDescriptorCreator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ColorVQDescriptorCreator c = ColorVQDescriptorCreator.getInstance();
		
		//honeycomb patches
		HoneycombDetector detector = new HoneycombDetector(12, 12);
		c.setDetector(detector);
		
		int x = 10, y = 6, z = 4;
//		RGBColorQuantizer q =new RGBColorQuantizer(x, y, z); 
		IHSColorQuantizer q = new IHSColorQuantizer(x, y, z);
		c.setQuantizer(q);
		
		c.setNormalize(true);
		
		ArrayList<ColorVQFloatDescriptor> d = c.createDescriptors(args[0]);
		
//		for(ColorVQFloatDescriptor f : d)
			System.out.println(XMLWriter.writeXMLString(d));
		
		System.out.println("size : "+d.size());

	
	}

}
