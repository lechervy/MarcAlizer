package fr.lip6.jdescriptors;

import java.util.ArrayList;

public interface DescriptorCreator<T extends Descriptor> {
	
	public ArrayList<T> createDescriptors(String imageName);

}
