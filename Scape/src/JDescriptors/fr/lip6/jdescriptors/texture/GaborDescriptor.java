package fr.lip6.jdescriptors.texture;

import java.util.Arrays;

import fr.lip6.jdescriptors.Descriptor;

public class GaborDescriptor extends Descriptor<double []> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2591989613483130742L;


	@Override
	public int getDimension() {
		// TODO Auto-generated method stub
		return d.length;
	}
	
	@Override
	public String toString()
	{
		return Arrays.toString(getD()).replaceAll("\\[", "").replaceAll("\\]", "");
		
	}

	@Override
	public void initD() {
		d = new double[64];
	}

}
