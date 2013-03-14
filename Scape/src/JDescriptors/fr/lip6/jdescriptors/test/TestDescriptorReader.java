package fr.lip6.jdescriptors.test;

import java.util.ArrayList;

import fr.lip6.jdescriptors.Descriptor;
import fr.lip6.jdescriptors.io.DescriptorReader;
import fr.lip6.jdescriptors.io.XMLWriter;

public class TestDescriptorReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception 
	{
		if(args.length < 1)
		{
			System.out.println("usage : TestXMLReader <filename.[osd|xml|xgz][.gz]>");
			return;
		}

		ArrayList<Descriptor> list = DescriptorReader.readFile(args[0]);
		
		System.out.println("size : "+list.size());
		
		int i = 1;
		for(Descriptor d : list)
		{
			System.out.println(i+" : "+XMLWriter.writeXMLString(d));
			i++;
		}
	}

}
