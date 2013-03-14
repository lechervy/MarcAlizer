package Taverna;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class FileConfig implements Serializable{
	/**
	 * numéro de version du fichier 
	 */
	private static final long serialVersionUID = 1L;
	//path de l'image associé au premier site web
	private String snapshot1Png;
	//path de l'image associé au deuxieme site web
	private String snapshot2Png;
	//path du fichier vips associé au premier site web
	private String vips1;
	//path du fichier vips associé au deuxième site web
	private String vips2;
	//path du fichier de difference des vips
	private String vidiff;
	//path du repertoire de travail
	private String workDirectory;
	//path des fichiers de dictionnaire de SIFT
	private ArrayList<String> dicoSift = new ArrayList<String>();
	//path des fichiers de dictionnaire de SIFT
	private ArrayList<String> dicoHsv = new ArrayList<String>();
	//path du programme permettant de générer les SIFT
	private String binSIFT;
	//path du programme permettant de découper les images
	//private String binDecoupage;
	//path du SVM utilisé
	private String binSVM;
	/**
	 * Permet de lire un fichier XML de configuration de Scape et de le transformer au format lissible par le programme
	 * @param file fichier XML contenant la configuration de SCAPE
	 * @return un objet java contenant la configuration correspondant au fichier XML fournie
	 * @throws FileNotFoundException 
	 * @throws Exception
	 */
	public static FileConfig deserializeXMLToObject(String file) throws FileNotFoundException{
		FileInputStream os = new FileInputStream(file);
		XMLDecoder decoder = new XMLDecoder (os);
		Object ob = decoder.readObject();
		decoder.close();
		
		return (FileConfig)ob;
	}
	/**
	 * permet de sauvegarder au format XML un fichier de configuration
	 * @param file nom du fichier de configuration a créer
	 * @throws Exception
	 */
	public void serializeXMLToObject(String file) throws FileNotFoundException, IOException{
		FileOutputStream os = new FileOutputStream(file);
		XMLEncoder encoder = new XMLEncoder(os);
		try{
			encoder.writeObject(this);
			encoder.flush();
		} finally{
			encoder.close();
		}
	}
	//Exemple d'option a lancer pour creer un fichier XML de configuration
	//-snapshot1 /home/lechervya/code/CArlosSureda/exemple/images/4/7.png 
	//-snapshot2 /home/lechervya/code/CArlosSureda/exemple/images/4/8.png
	//-vips1 /home/lechervya/code/CArlosSureda/exemple/xml/toto/
	//-vips2 /home/lechervya/code/CArlosSureda/exemple/xml/toto/
	//-vidiff /home/lechervya/code/CArlosSureda/exemple/xml/toto/
	//-binDecoupage /home/lechervya/code/CArlosSureda/Scape/bin/in/decoupe_image_python_sift.py
	//-binSIFT /home/lechervya/code/CArlosSureda/Scape/bin/in/colorDescriptor
	//-binSVM /home/lechervya/code/CArlosSureda/Scape/bin/in/svm
	//-dicoSift 2 /home/lechervya/code/CArlosSureda/Scape/bin/in/dictionary/sift/100/output.obj /home/lechervya/code/CArlosSureda/Scape/bin/in/dictionary/sift/200/output.obj
	//-dicoHsv 2 /home/lechervya/code/CArlosSureda/Scape/bin/in/dictionary/color/100/output.obj /home/lechervya/code/CArlosSureda/Scape/bin/in/dictionary/color/200/output.obj
	//-workDir /home/lechervya/code/CArlosSureda/exemple/work/
	//-fichier /home/lechervya/code/CArlosSureda/exemple/test.xml
	public static void main(String[] args){
		String snapshot1Png="";
		String snapshot2Png="";
		String vips1 ="";
		String vips2 ="";
		String vidiff ="";
		String fichier="";
		String workDir="";
		String binSIFT="";
		//String binDecoupage="";
		String binSVM="";
		ArrayList<String> dicoSift = new ArrayList<String>();
		ArrayList<String> dicoHsv = new ArrayList<String>();
		for(int i=0 ; i<args.length-1 ; i++){
			if(args[i].equals("-snapshot1")){
				snapshot1Png = args[i+1];
				i++;
				continue;
			}
			if(args[i].equals("-snapshot2")){
				snapshot2Png = args[i+1];
				i++;
				continue;
			}
			if(args[i].equals("-vips1")){
				vips1 = args[i+1];
				i++;
				continue;
			}
			if(args[i].equals("-vips2")){
				vips2 = args[i+1];
				i++;
				continue;
			}
			if(args[i].equals("-vidiff")){
				vidiff = args[i+1];
				i++;
				continue;
			}
			if(args[i].equals("-workDir")){
				workDir = args[i+1];
				i++;
				continue;
			}
			if(args[i].equals("-dicoSift")){
				int numDicoSift = new Integer(args[i+1]);
				i++;
				for(int j=0 ; i<args.length && j<numDicoSift ; i++,j++){
					dicoSift.add(args[i+1]);
				}
				continue;
			}
			if(args[i].equals("-dicoHsv")){
				int numDicoHsv = new Integer(args[i+1]);
				i++;
				for(int j=0 ; i<args.length && j<numDicoHsv ; i++,j++){
					dicoHsv.add(args[i+1]);
				}
				continue;
			}
			if(args[i].equals("-fichier")){
				fichier = args[i+1];
				i++;
				continue;
			}
			if(args[i].equals("-binSIFT")){
				binSIFT = args[i+1];
				i++;
				continue;
			}/*
			if(args[i].equals("-binDecoupage")){
				binDecoupage = args[i+1];
				i++;
				continue;
			}*/
			if(args[i].equals("-binSVM")){
				binSVM = args[i+1];
				i++;
				continue;
			}
		}
		if(fichier.equals("")){
			System.err.println("Ajouter -fichier <nom du fichier de sortie>");
			return;
		}
		FileConfig f= new FileConfig(snapshot1Png,snapshot2Png,vips1,vips2,vidiff,binSIFT/*,binDecoupage*/,binSVM,dicoSift,dicoHsv,workDir);
		try {
			f.serializeXMLToObject(fichier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fichier XML de configuration créer");
	}
	public FileConfig(){
		this.setSnapshot1Png("");
		this.setSnapshot2Png("");
		this.setVips1("");
		this.setVips2("");
		this.setVidiff("");
		this.setBinSIFT("");
		this.setWorkDirectory("");
	}

	private FileConfig(String snapshot1Png,String snapshot2Png,String vips1,String vips2,String vidiff,String binSIFT/*,String binDecoupage*/,String binSVM, ArrayList<String> dicoSift, ArrayList<String> dicoHsv,String rep){
		this.setSnapshot1Png(snapshot1Png);
		this.setSnapshot2Png(snapshot2Png);
		this.setVips1(vips1);
		this.setVips2(vips2);
		this.setVidiff(vidiff);
		this.setBinSIFT(binSIFT);
		//this.setBinDecoupage(binDecoupage);
		this.setBinSVM(binSVM);
		this.setDicoSift(dicoSift);
		this.setDicoHsv(dicoHsv);
		this.setWorkDirectory(rep);
	}

	/**
	 * Permet de savoir si les informations visuels sont disponibles
	 * @return true si les informations de configuration visuels sont diponible
	 */
	public boolean isImgValid(){
		return !snapshot1Png.equals("") && !snapshot2Png.equals("");  
	}
	/**
	 * Permet de savoir si les informations structurelles sont disponibles
	 * @return true si les informations de configuration structurelles sont diponible
	 */
	public boolean isStructValid(){
		return !vips1.equals("") && !vips2.equals("") && !vidiff.equals("");  
	}
	/**
	 * Permet de savoir si des dico de Sift sont dans la configuration
	 * @return true si il y a des dico de Sift
	 */
	public boolean hasSift(){
		return !dicoSift.isEmpty() && !binSIFT.equals("");
	}
	/**
	 * Permet de savoir si des dico de Hsv sont dans la configuration
	 * @return true si il y a des dico de Sift
	 */
	public boolean hasHsv(){
		return !dicoHsv.isEmpty();
	}
	/**
	 * @return the snapshot1Png
	 */
	public String getSnapshot1Png() {
		return snapshot1Png;
	}
	/**
	 * @param snapshot1Png the snapshot1Png to set
	 */
	public void setSnapshot1Png(String snapshot1Png) {
		this.snapshot1Png = snapshot1Png;
	}
	/**
	 * @return the snapshot2Png
	 */
	public String getSnapshot2Png() {
		return snapshot2Png;
	}
	/**
	 * @param snapshot2Png the snapshot2Png to set
	 */
	public void setSnapshot2Png(String snapshot2Png) {
		this.snapshot2Png = snapshot2Png;
	}
	/**
	 * @return the vips1
	 */
	public String getVips1() {
		return vips1;
	}
	/**
	 * @param vips1 the vips1 to set
	 */
	public void setVips1(String vips1) {
		this.vips1 = vips1;
	}
	/**
	 * @return the vips2
	 */
	public String getVips2() {
		return vips2;
	}
	/**
	 * @param vips2 the vips2 to set
	 */
	public void setVips2(String vips2) {
		this.vips2 = vips2;
	}
	/**
	 * @return the vidiff
	 */
	public String getVidiff() {
		return vidiff;
	}
	/**
	 * @param vidiff the vidiff to set
	 */
	public void setVidiff(String vidiff) {
		this.vidiff = vidiff;
	}
	/**
	 * @return the directoryWork
	 */
	public String getWorkDirectory() {
		return workDirectory;
	}
	/**
	 * @param directoryWork the directoryWork to set
	 */
	public void setWorkDirectory(String workDirectory) {
		this.workDirectory = workDirectory;
	}
	/**
	 * @return the dicoSift
	 */
	public ArrayList<String> getDicoSift() {
		return dicoSift;
	}
	/**
	 * @param dicoSift the dicoSift to set
	 */
	public void setDicoSift(ArrayList<String> dicoSift) {
		this.dicoSift = dicoSift;
	}
	/**
	 * @return the dicoHsv
	 */
	public ArrayList<String> getDicoHsv() {
		return dicoHsv;
	}
	/**
	 * @param dicoHsv the dicoHsv to set
	 */
	public void setDicoHsv(ArrayList<String> dicoHsv) {
		this.dicoHsv = dicoHsv;
	}
	/**
	 * @return the binSIFT
	 */
	public String getBinSIFT() {
		return binSIFT;
	}
	/**
	 * @param binSIFT the binSIFT to set
	 */
	public void setBinSIFT(String binSIFT) {
		this.binSIFT = binSIFT;
	}
	/**
	 * @return the binDecoupage
	 *//*
	public String getBinDecoupage() {
		return binDecoupage;
	}*/
	/**
	 * @param binDecoupage the binDecoupage to set
	 *//*
	public void setBinDecoupage(String binDecoupage) {
		this.binDecoupage = binDecoupage;
	}*/
	/**
	 * @return the binSVM
	 */
	public String getBinSVM() {
		return binSVM;
	}
	/**
	 * @param binSVM the binSVM to set
	 */
	public void setBinSVM(String binSVM) {
		this.binSVM = binSVM;
	}
}