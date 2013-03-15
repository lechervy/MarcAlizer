package fr.lip6.jdescriptors.color;

import java.awt.image.ColorModel;

import fr.lip6.jdescriptors.quantizer.Quantizer;

public interface ColorQuantizer extends Quantizer {

	/**
	 * @return the colorModel
	 */
	public ColorModel getColorModel();

	/**
	 * @param colorModel
	 *            the colorModel to set
	 */
	public void setColorModel(ColorModel colorModel);
	
	
	/**
	 * the channel array of the color  corresponding to the specified bin
	 * @param b
	 * @return
	 */
	public float[] getColorFromBin(int b);

	public int getBin(float[] fcol);
}
