package fr.lip6.jdescriptors.color;

import java.awt.image.ColorModel;

public class DefaultColorQuantizer implements ColorQuantizer {

	@Override
	public ColorModel getColorModel() {
		return ColorModel.getRGBdefault();
	}

	@Override
	public void setColorModel(ColorModel colorModel) {
		//Do nothing
	}

	@Override
	public int getBin(int[] p) {
		return 0;
	}

	@Override
	public int getBinNumber() {
		return 1;
	}

	@Override
	public float[] getColorFromBin(int b) {
		return new float[] {0, 0, 0};
	}

	@Override
	public int getBin(float[] fcol) {
		return 0;
	}

}
