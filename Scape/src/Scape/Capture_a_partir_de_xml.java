package Scape;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * Adaptation du fichier capture_a_partir_de_xml.py écrit en Python
 * Cette classe:
 * - Lit un repertoire contenant des fichiers xml 
 * - Utilise les coordonnées calculés par VIPS pour faire une capture de la page web
 * @author suredac
 *
 */
public class Capture_a_partir_de_xml {

	public static void main(String[] args) {
		String path = "/home/suredac/Scape/Code Marc/python/IECapt/";
		String xmlfilepath = path+"xml/";
		int nbpages = 0;
		//int numero_page = nbpages*2;
		boolean version = false;
		//String imagefilepath = path+"images_29_aout_2011";
		//int max_wait = 50000;
		File f_xmlfilepath = new File(xmlfilepath);
		File f_sousDossiers;
		String[] dossiers = f_xmlfilepath.list();
		File[] fichiers;
		Document doc = null;
		SAXBuilder sxb = new SAXBuilder();
		//String url;
		double width;
		String[] list_width;
		List<Element> documents;
		List<Element> list_blocks;
		String[] list_pos;
		
		for(int f=0; f<dossiers.length;f++){
			f_sousDossiers = new File(dossiers[f]);
			fichiers = new File(xmlfilepath+f_sousDossiers).listFiles();
			for(int f2=0;f2<fichiers.length;f2++){
				if(! fichiers[f2].isFile())
					continue;
				version = !version;
				/*numero_page ++;
				if(version)
					nbpages ++;
				 */
				try {
					doc = sxb.build(fichiers[f2]);
				} catch (JDOMException e) {e.printStackTrace();
				} catch (IOException e) {e.printStackTrace();}

				documents = doc.getRootElement().getChildren("Document");
				//url = documents.get(0).getAttributeValue("Url");
				list_width = documents.get(0).getAttributeValue("Pos").split(" ")[1].split(":");
				width = Double.parseDouble(list_width[list_width.length-1]);
				list_blocks = doc.getRootElement().getChildren("Block");
				for(int k=0;k<list_blocks.size();k++){
					list_pos = list_blocks.get(k).getAttributeValue("Pos").split(" ")[3].split(":");
					if(list_pos[list_pos.length-1].matches("^\\p{Digit}+$"))
						width = Math.max(width, Double.parseDouble(list_pos[list_pos.length-1]));
				}
				
				//LANCER LA COMMANDE DE CAPTURE
			}
		}
	}

}
