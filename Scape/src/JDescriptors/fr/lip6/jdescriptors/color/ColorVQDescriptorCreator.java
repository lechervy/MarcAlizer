package fr.lip6.jdescriptors.color;

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.Serializable;
import java.util.ArrayList;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;

import fr.lip6.jdescriptors.DescriptorCreator;
import fr.lip6.jdescriptors.detector.Detector;

public class ColorVQDescriptorCreator implements Serializable,
		DescriptorCreator<ColorVQFloatDescriptor> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9006684915019721722L;
	
	private static ColorVQDescriptorCreator instance;
	
	private ColorVQDescriptorCreator()
	{
		//default parameters;
		quantizer = new DefaultColorQuantizer();
	}
	
	//Parameters
	ColorQuantizer quantizer;
	Detector detector;
	boolean normalize = false;
	
	public static boolean DEBUG = false;
	private static long time = 0;
	
	
	
	@Override
	public ArrayList<ColorVQFloatDescriptor> createDescriptors(String imageName){
		return createDescriptors(imageName, -1);
	}
	
	public ArrayList<ColorVQFloatDescriptor> createDescriptors(String imageName, int maxHeight){
		return createDescriptors(imageName, maxHeight, false);
	}
	
	public ArrayList<ColorVQFloatDescriptor> createDescriptors(String imageName, int maxHeight, boolean onlyTop) {
		
		if(DEBUG)
		{
			time = System.currentTimeMillis();
			System.err.println((System.currentTimeMillis()-time)/1000.0+" : Starting");
		}
		
		RenderedOp srcImg;
		BufferedImage bfImg;
		//RenderedOp srcImg2;
		//1. read the image

		if(DEBUG)
			System.err.println((System.currentTimeMillis()-time)/1000.0+" : Opening "+imageName);
		try
		{
//			srcImg = ImageIO.read(new File(imageName));
			srcImg = JAI.create("fileload", imageName);
			//check has 3 or more band, if not, converting
			if(srcImg.getColorModel().getNumComponents() == 1 )
			{
				// Prepare operation parameters
				double[][] matrix = {
					{ 1.0D, 0.0D },
					{ 1.0D, 0.0D },
					{ 1.0D, 0.0D } };
				 
				ParameterBlock pb = new ParameterBlock();
				pb.addSource(srcImg);
				pb.add(matrix);
				 
				// Prepare rendering hints
				ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
				 
				ColorModel cm = RasterFactory.createComponentColorModel(
					DataBuffer.TYPE_BYTE, cs, false, false,
					Transparency.OPAQUE);
				 
				SampleModel sm = cm.createCompatibleSampleModel(srcImg.getWidth(), srcImg.getHeight());
				 
				ImageLayout imageLayout = new ImageLayout();
				imageLayout.setSampleModel(sm);
				imageLayout.setColorModel(cm);
				 
				RenderingHints renderingHints = new RenderingHints(null);
				renderingHints.clear();
				renderingHints.put(JAI.KEY_IMAGE_LAYOUT, imageLayout);
				 
				// Perform the band combine operation. 
				srcImg = JAI.create("bandcombine", pb, renderingHints);

			}
		}
		catch(Exception ioe)
		{
			System.err.println("Impossible de lire "+imageName);
			ioe.printStackTrace(System.err);
			return null;
		}
		if(DEBUG)
			System.err.println((System.currentTimeMillis()-time)/1000.0+"\t : image opened");
		
		//2. convert it to indexed colorspace
//		ColorConvertOp cco = new ColorConvertOp(srcImg.getColorModel().getColorSpace(), quantizer.getColorModel().getColorSpace(), new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY));
//		 
////		BufferedImage dstImg = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, quantizer.getColorModel());
//		BufferedImage dstImg = new BufferedImage(quantizer.getColorModel(), srcImg.getRaster().createCompatibleWritableRaster(), false, null); 
//		cco.filter(srcImg, dstImg);

		//System.out.println("passage dans ColorVQDescriptor 1 : " + srcImg.getWidth() + ", " + srcImg.getHeight());
		Raster r;
		//System.out.println("r.maxsize = " + r.getDataBuffer().getSize());
		ArrayList<ColorVQFloatDescriptor> list = new ArrayList<ColorVQFloatDescriptor>();
		int nbCuts = srcImg.getHeight()/maxHeight + 1;
		int deltaMove = onlyTop ? Math.min(srcImg.getHeight(), maxHeight) : srcImg.getHeight()/nbCuts;
		bfImg = srcImg.getAsBufferedImage();
		ParameterBlock pb = new ParameterBlock();
		pb.add(quantizer.getColorModel());
		pb.add(quantizer.getColorModel().createCompatibleSampleModel(srcImg.getWidth(), deltaMove));
		for (int nbSubImages = 0 ; nbSubImages < (onlyTop ? 1 : nbCuts) ; ++nbSubImages){
//			System.out.println("height = " + srcImg.getHeight() + " ; nbCuts = " + nbCuts + " ; nbSubImages = " + nbSubImages + " ; deltaMove = " + deltaMove);
			int move = nbSubImages*srcImg.getHeight()/nbCuts;
			if (maxHeight == -1 || nbCuts == 1) {
				pb.addSource(bfImg);
			}
			else {
				//System.out.println("hauteur = " + srcImg.getHeight() + " ; move = " + move + " ; move+deltaMove = " + (move + deltaMove));
				pb.addSource(bfImg.getSubimage(0, move, srcImg.getWidth(), deltaMove));
				//System.out.println("passage par ici");
			}
			RenderedOp IHSImage  = JAI.create("colorconvert", pb);
			r = IHSImage.getData();
				
				//System.out.println("hauteur = " + srcImg.getHeight() + " ; move = " + move + " ; move+deltaMove = " + (move + deltaMove));
				//r= IHSImage.getData(new Rectangle(0,move,srcImg.getWidth(), deltaMove));
			//System.out.println("passage dans ColorVQDescriptor 2");
		
			if(DEBUG)
				System.err.println((System.currentTimeMillis()-time)/1000.0+"\t : ColorSpace convertion done");
		
			//3. loop over considered patches
			ArrayList<ColorVQFloatDescriptor> listOfPatches;
			if (maxHeight == -1 || nbCuts == 1){
				listOfPatches = detector.getDescriptors(ColorVQFloatDescriptor.class, bfImg);
			}
			else {
				listOfPatches = detector.getDescriptors(ColorVQFloatDescriptor.class, bfImg.getSubimage(0, move, srcImg.getWidth(), deltaMove));				
			}
			for(ColorVQFloatDescriptor i : listOfPatches)
			{
//				System.out.println(i+" : "+i.getXmin()+","+i.getXmax()+","+i.getYmin()+","+i.getYmax());
			
				//the descriptor
				ColorVQFloatDescriptor cvqfd = new ColorVQFloatDescriptor();
				//System.out.println("i.getXmin() : " + i.getXmin() + " ; i.getXmax() : " + i.getXmax() + " ; i.getYmin() : " + i.getYmin() + " ; i.getYmax() : " + i.getYmax() + " ; i.getShape() : " + i.getShape());
				cvqfd.setXmin(i.getXmin());
				cvqfd.setXmax(i.getXmax());
				cvqfd.setYmin(i.getYmin());
				cvqfd.setYmax(i.getYmax());
				cvqfd.setShape(i.getShape());
				//System.out.println("d = " + quantizer.getBinNumber());
				float[] d = new float[quantizer.getBinNumber()];
			
				//loop over pixels in the patch
//				Raster r = IHSImage.getData(new Rectangle(i.getXmin(), i.getYmin(), i.getXmax()-i.getXmin(), i.getYmax()-i.getYmin()));

//				Raster rsrc = srcImg.getData(new Rectangle(i.getXmin(), i.getYmin(), i.getXmax()-i.getXmin(), i.getYmax()-i.getYmin()));
				
				float surf = (i.getXmax() - i.getXmin())*(i.getYmax()-i.getYmin());
				//System.out.println("deltamove = " + (srcImg.getHeight()/nbCuts));
				//System.out.println("i.getXmin() = " + i.getXmin() +" ; i.getXmax() = " + i.getXmax() + " ; i.getYmin() = " + i.getYmin() + " ; i.getYmax() = " + i.getYmax());
				for(int x = i.getXmin(); x < i.getXmax(); ++x)
					for(int y = i.getYmin(); y < i.getYmax(); ++y)
					{ 
						int[] color = r.getPixel(x, y, (int[])null);
//					int[] csrc = rsrc.getPixel(x, y, (int[])null);
//					System.out.println("src : "+Arrays.toString(csrc)+" dst : "+Arrays.toString(color));
//					float[] fcol = r.getPixel(x, y, (float[])null);
						int index = quantizer.getBin(color);
//					int index = quantizer.getBin(fcol);
						if(index >= 0 && index < d.length)
						{
							if(normalize)
								d[index] += 1.0f/surf;
							else
								d[index] += 1.0f;
						}

//					System.out.println(x+","+y+" : "+Arrays.toString(color)+" => "+index);
					}
			
				cvqfd.setD(d);
			
				list.add(cvqfd);
			}
			//System.out.println("taille de la liste = " + list.size());
		}
		if(DEBUG)
			System.err.println((System.currentTimeMillis()-time)/1000.0+"\t : Descriptors done.");
		
		return list;
	}

	/**
	 * getter of the singleton pattern
	 * @return
	 */
	public static ColorVQDescriptorCreator getInstance()
	{
		if(instance == null)
			instance = new ColorVQDescriptorCreator();
		return instance;
	}

	

	/**
	 * @return the detector
	 */
	public Detector getDetector() {
		return detector;
	}

	/**
	 * @param detector the detector to set
	 */
	public void setDetector(Detector detector) {
		this.detector = detector;
	}

	public ColorQuantizer getQuantizer() {
		return quantizer;
	}

	public void setQuantizer(ColorQuantizer quantizer) {
		this.quantizer = quantizer;
	}

	public boolean isNormalize() {
		return normalize;
	}

	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}

	
}
