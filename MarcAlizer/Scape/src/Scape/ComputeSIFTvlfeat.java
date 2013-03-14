package Scape;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

// Required matlab package: http://code.google.com/p/matlabcontrol/downloads/list
// Required VLFeat software: http://www.vlfeat.org/download.html
@SuppressWarnings("static-access")
public class ComputeSIFTvlfeat {
   
    static Option sift = OptionBuilder.withArgName("SIFT type")
            							.hasArg()
            							.withDescription("SIFT type of vlfeat software (options: vlphow, vldsift (default))")
            							.create("sift");
    static Option input = OptionBuilder.withArgName("images directory|list")
    									.hasArg()
    									.withDescription("Image files directory|list")
    									.create("i");
    static Option output = OptionBuilder.withArgName("output directory")
            							.hasArg()
            							.withDescription("Output directory")
            							.create("o");
    static Option step = OptionBuilder.withArgName("step")
    									.hasArg()
    									.withDescription("Step (in pixels) of the grid at which the SIFT features are extracted (eg: [8] (default), [2,4,6,8]). Attention: You can choose 1 STEP for 'N' SIZE or 'N' STEP for 'N' SIZE. ")
    									.create("step");
    static Option size = OptionBuilder.withArgName("size")
            							.hasArg()
            							.withDescription("Scales at which the SIFT features are extracted. Each value is used as bin size, ie SIZE = [4] -> patch size = 16x16 (eg: [4] (default), [4,6,8,10])")
            							.create("size");
    static Option contrast = OptionBuilder.withArgName("contrast")
										.hasArg()
										.withDescription("It works only for vlphow option. Contrast threshold below which SIFT features are mapped to zero (default: 0.005).")
										.create("c");
    static Option threshold = OptionBuilder.withArgName("threshold")
										.hasArg()
										.withDescription("It works only for vldsift option. Threshold below which SIFT features are mapped to zero (default: 0.0025).")
										.create("t");
    static Option matlabpath = OptionBuilder.withArgName("matlab path")
            							.hasArg()
            							.withDescription("matlab path, eg /usr/local/matlab/bin/matlab")
            							.create("m");
    static Option vlfeatpath = OptionBuilder.withArgName("vl_setup path")
            							.hasArg()
            							.withDescription("VLFeat path, eg ../vlfeat-0.9.13/toolbox/vl_setup")
            							.create("v");

    static Option floatdescriptors = new Option("floatdescriptors", "The descriptor are returned in floating point rather than integer format");
    static Option fast = new Option("fast", "In practice is much faster to compute");
   
    static Option help = new Option("help", "Print this message");

    static Options options = new Options();
   
    static {
        options.addOption(sift);
        options.addOption(input);
        options.addOption(output);

        options.addOption(step);
        options.addOption(size);
        options.addOption(contrast);
        options.addOption(threshold);
        
        options.addOption(matlabpath);
        options.addOption(vlfeatpath);
           
        options.addOption(help);
       
        options.addOption("floatdescriptors", false, "The descriptor are returned in floating point rather than integer format.");
        options.addOption("fast", false, "In practice is much faster to compute. See VLFeat documentation for further details. ");

    }
   
    public static void main(String[] args) throws MatlabConnectionException, MatlabInvocationException
    {
        String input     = null;
        String output    = null;
        String sift      = null;
        String step      = null;
        String size      = null;
        String matlab    = null;
        String vlfeat    = null;
        double contrast  = 0;
        double threshold = 0;
        boolean fast     = false;
        boolean floatdescriptors = false;
       
        // Option parsing       
        // Create the parser
        CommandLineParser parser = new GnuParser();
       
        try {
            // parse the command line arguments
            CommandLine line = parser.parse( options, args );
           
            // input, output
            if(!line.hasOption("i") | !line.hasOption("o"))
            {
                // Automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ComputeSIFTvlfeat", options );
                System.exit(-1);
            }
            else
            {
                input = line.getOptionValue("i");
                output = line.getOptionValue("o");
            }
             
            // output    
            File outdir = new File(output);
            if(!outdir.exists())
            	outdir.mkdir();
            else if(!outdir.isDirectory())
            {
            	System.out.println("output "+outdir+" exists and is not a directory.");
            	HelpFormatter formatter = new HelpFormatter();
            	formatter.printHelp( "ComputeSIFTvlfeat", options );
            	System.exit(-1);
            }
           
            // sift
            sift = line.getOptionValue("sift", "vldsift");
           
            // step
            step = line.getOptionValue("step", "[8]");
                       
            // size
            size = line.getOptionValue("size", "[4]");
            
            // contrast threshold
            contrast = Double.parseDouble(line.getOptionValue("c", "0.005"));
            
            // threshold
            threshold = Double.parseDouble(line.getOptionValue("t", "0.0025"));
            
            // matlab path
            matlab = line.getOptionValue("m", "/usr/local/matlab/bin/matlab");
            
            // vlfeat path
            vlfeat = line.getOptionValue("v");
                                  
            // fast
            fast = line.hasOption("fast");
           
            // floatdescriptors
            floatdescriptors = line.hasOption("floatdescriptors");
           
        }
        catch( ParseException exp ) {
           
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "ComputeSIFTvlfeat", options );
            System.exit(-1);
        }
       
        // Printing options
        System.out.println("#####################");
        System.out.println(" SIFT VLFeat options ");
        System.out.println("#####################\n");
        System.out.println("input  : " + input);
        System.out.println("output : " + output);
        System.out.println("sift   : " + sift.toLowerCase());
        System.out.println("step   : " + step);
        System.out.println("size   : " + size);
        System.out.println("fast   : " + fast);
        System.out.println("float  : " + floatdescriptors);  
        if (sift.toLowerCase().contains("vlphow"))
        	System.out.println("constrast : " + contrast);
        else
        	System.out.println("threshold : " + threshold);
        System.out.println();   

        ArrayList<File> list = new ArrayList<File>();
		File directory = new File(input);
		if(!directory.isDirectory())
		{
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(input));
				String line = null;

				while (br.ready()){
					line = br.readLine().trim();
					list.add(new File(line));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			File[] listFiles = directory.listFiles();		
			for(File f : listFiles)
				list.add(f);
		}
		
        // Processing the STEP input
        String steptemp = step.replaceAll("\\[", "").replaceAll("\\]", "").trim();
        StringTokenizer tokens = new StringTokenizer(steptemp);
        
        ArrayList<Float> steplist = new ArrayList<Float>();
        while (tokens.hasMoreTokens()) 
        	steplist.add(Float.parseFloat(tokens.nextToken(",")));

        if (steplist.isEmpty()) 
        {
        	System.err.println("STEP list "+step+" is empty.");
        	System.exit(-1);
        }
       
        // Processing the SIZE input
        String sizetemp = size.replaceAll("\\[", "").replaceAll("\\]", "").trim();
        tokens = new StringTokenizer(sizetemp);
        
        ArrayList<Float> sizelist = new ArrayList<Float>();
        while (tokens.hasMoreTokens()) 
        	sizelist.add(Float.parseFloat(tokens.nextToken(",")));
        
        if (sizelist.isEmpty())
        {
        	System.err.println("SIZE list "+size+" is empty.");
        	System.exit(-1);
        }
       
       
        if (steplist.size() > 1 && sizelist.size() > 1 && steplist.size() != sizelist.size() )
        {
        	System.err.println("Attention: The size of STEP and SIZE lists are both greater than 1, but they are not the same size.");
        	System.err.println("You can choose 1 STEP for 'N' SIZE or 'N' STEP for 'N' SIZE.");
        	System.exit(-1);
        }
        
        // Fill the STEP list to have the same size of SIZE list
        if (steplist.size() == 1 && sizelist.size() > 1 )
        {
        	for (int i = 1; i < sizelist.size(); i++)
        		steplist.add(steplist.get(0));      	
        }
 		
		// Create a proxy, which we will use to control MATLAB   
        MatlabProxyFactoryOptions optionsMatlab = new MatlabProxyFactoryOptions.Builder()
        																	   .setUsePreviouslyControlledSession(true)
        																	   .setHidden(true)
        																	   .setMatlabLocation(matlab)
        																	   .build();
        
    	MatlabProxyFactory factory = new MatlabProxyFactory(optionsMatlab);
    	MatlabProxy proxy = factory.getProxy();
    	
       	// Start VLFeat
    	proxy.eval("run('" + vlfeat + "')");
       
    	for (File f : list) {

    		FileWriter filetxt = null;
    		try {
    			String destName = output+(f.getName().substring(0, f.getName().indexOf(".")))+".sift";

    			File outfile = new File(destName);
    			if (outfile.exists()) {
    				System.out.println("not doing: " + destName);
    				continue;
    			}
    				   			
    			System.out.println("doing: " + destName);
    			filetxt = new FileWriter(destName);
    			
       	    	MatlabTypeConverter processor = new MatlabTypeConverter(proxy);

    			// Calculating SIFT
    			double[][] siftList = listFramesDesc(f, sift, steplist, sizelist, floatdescriptors, fast, contrast, threshold, processor, proxy, factory);

    			int dimension     = siftList[0].length-2;
    			int nbDescriptors = siftList.length;
      			
    	    	int width  = (int)processor.getNumericArray("width").getRealValue(0);
    	        int height = (int)processor.getNumericArray("height").getRealValue(0);

    	        // file head
    	        filetxt.write("VLFEAT "+ sift.toLowerCase() + " step=" + step + " size=" + size + "\n");
    			filetxt.write(dimension + " " + nbDescriptors + " " + width + " " + height + "\n");
    			
    			// body file
    			if (floatdescriptors) {
    				for(int i = 0; i < nbDescriptors; i++){
    					for(int j = 0; j < dimension+2; j++){
    						filetxt.write((double)siftList[i][j] + " ");
    					}
    					filetxt.write("\n");
    				}
    			}
    			else {
    				for(int i = 0; i < nbDescriptors; i++){
    					// frames 
    					filetxt.write((double)siftList[i][0] + " " + (double)siftList[i][1] + " ");
    					// SIFT list
    					for(int j = 2; j < dimension+2; j++){
    						filetxt.write((int)siftList[i][j] + " ");
    					}
    					filetxt.write("\n");
    				}
    			}
    			filetxt.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        
    	//Disconnect the proxy from MATLAB
    	proxy.disconnect();
    }

    private static double[][] listFramesDesc (File f, String sift, ArrayList<Float> step, ArrayList<Float> size, boolean floatdescriptors, boolean fast, double contrast, double threshold, 
    		MatlabTypeConverter processor, MatlabProxy proxy, MatlabProxyFactory factory ) throws MatlabConnectionException, MatlabInvocationException, IOException {

    	double[][] matrixFramesDescr = new double[][]{ { 0 }, { 0 } };
    	
    	// Creating the variable img (in Matlab) 
    	double[][] img = new double[][]{ { 0 }, { 0 } };
    	processor.setNumericArray("img", new MatlabNumericArray(img, null));

    	// number of descriptors
    	int nbDescriptor = 0;
    	// SIFT dimension
    	int dimension = 0;
    	
    	proxy.eval("im = imread('" + f + "');");
    	proxy.eval("y = size(im,3)==3; if y==1 img = im2single(rgb2gray(im)); else img = im2single(im); end");
    	proxy.eval("width = size(img,2); height = size(img,1);");
    	   	
     	for (int i = 0; i < step.size(); i++)
    	{
    		if (sift.equalsIgnoreCase("vldsift"))
    		{
    			if (!floatdescriptors && !fast)
    				proxy.eval("[frames,descriptors] = vl_dsift(img, 'step'," + step.get(i) + ", 'size'," + size.get(i) + ", 'norm');");
    			else if (floatdescriptors && !fast)
    				proxy.eval("[frames,descriptors] = vl_dsift(img, 'step'," + step.get(i) + ", 'size'," + size.get(i) + ", 'floatdescriptors', 'norm');");
    			else if (!floatdescriptors && fast)
    				proxy.eval("[frames,descriptors] = vl_dsift(img, 'step'," + step.get(i) + ", 'size'," + size.get(i) + ", 'fast', 'norm');");
    			else if (floatdescriptors && fast)
    				proxy.eval("[frames,descriptors] = vl_dsift(img, 'step'," + step.get(i) + ", 'size'," + size.get(i) + ", 'floatdescriptors', 'fast', 'norm');");
    		}
    		else if (sift.equalsIgnoreCase("vlphow"))
    		{
    			if (!floatdescriptors && !fast)
    				proxy.eval("[frames,descriptors] = vl_phow(img, 'step'," + step.get(i) + ", 'sizes'," + size.get(i) + ", 'floatdescriptors', false, 'fast', false, 'contrastthreshold'," + contrast + ");");
    			else if (floatdescriptors && !fast)
    				proxy.eval("[frames,descriptors] = vl_phow(img, 'step'," + step.get(i) + ", 'sizes'," + size.get(i) + ", 'floatdescriptors', true,  'fast', false, 'contrastthreshold'," + contrast + ");");
    			else if (!floatdescriptors && fast)
    				proxy.eval("[frames,descriptors] = vl_phow(img, 'step'," + step.get(i) + ", 'sizes'," + size.get(i) + ", 'floatdescriptors', false, 'fast', true,  'contrastthreshold'," + contrast + ");");
    			else if (floatdescriptors && fast)
    				proxy.eval("[frames,descriptors] = vl_phow(img, 'step'," + step.get(i) + ", 'sizes'," + size.get(i) + ", 'floatdescriptors', true,  'fast', true,  'contrastthreshold'," + contrast + ");");
    		}
    		
    		proxy.eval("frames = frames'; descriptors = double(descriptors');");

    		int[] descriptorsInfo = processor.getNumericArray("descriptors").getLengths();
    		// number of descriptors
    		nbDescriptor = nbDescriptor + descriptorsInfo[0];
    		// SIFT dimension
    		dimension = descriptorsInfo[1];

    		// Computing frames and descriptors
    		proxy.eval("matrix"+i+" = zeros(" + descriptorsInfo[0] +", " + dimension + "+2);"); 

    		if (sift.equalsIgnoreCase("vldsift"))
    		{
        		proxy.eval("for i=1:" +descriptorsInfo[0]+", " +
        						"matrix"+i+"(i,1:2) = frames(i,1:2); " +
        							"if frames(i,3) < " +threshold+ "; " + // frames(i,3) is equal to the descriptor norm, or engergy, before contrast normalization
        								"descriptors(i,:) = zeros(1,"+dimension+"); " +
        							"end; " +
        						"matrix"+i+"(i,3:" + dimension + "+2) = descriptors(i,1:" + dimension + "); " +
        				   "end");
     		}
    		else if (sift.equalsIgnoreCase("vlphow"))
    		{
    			proxy.eval("for i=1:" +descriptorsInfo[0]+", " +
    							"matrix"+i+"(i,1:2) = frames(i,1:2); " +
    							"matrix"+i+"(i,3:" +dimension+ "+2) = descriptors(i,1:" +dimension+ "); " +
    					   "end");
    		}

    	}

        proxy.eval("matrix = zeros(" + nbDescriptor +", " + dimension + "+2);");
        String str = "matrix0;";
        for (int j = 1; j < size.size(); j++)
        	str = str.concat("matrix".concat(Integer.toString(j)).concat(";"));

        proxy.eval("matrix = ["+str+"];");
        matrixFramesDescr = processor.getNumericArray("matrix").getRealArray2D();
    
    	return matrixFramesDescr; 
    }
}