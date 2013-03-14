package fr.lip6.jdescriptors.detector;

import java.awt.image.RenderedImage;
import java.util.ArrayList;

import fr.lip6.jdescriptors.Descriptor;

public interface Detector {

	
	/**
	 * Method for getting a list of positions in the image where to compute the descriptors.<br/>
	 * Note that the returned list contains Descriptors with d field set to null.
	 * @param c the class of Descriptors to be returned.
	 * @param image the image to run the detector on.
	 * @return a list of Descriptor with location set to the detected points.
	 */
	public <T extends Descriptor> ArrayList<T> getDescriptors(Class<T> c, RenderedImage image);
	
}
