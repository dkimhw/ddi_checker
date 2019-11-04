import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
/**
 * Reads a drug dataset, parses the information and generates drug objects
 * @author  Wei Han
 */
public class DataReader {
	private String fileName;
	private ArrayList<Drug> drugs;
	
	/**
	 * Constructs a DataReader that can read a drug database file and construct the drug objects
	 * @param fileName the name of the drug database file
	 */
	public DataReader (String fileName) {
		System.out.println("Loading the database...");
		this.fileName = fileName;
		drugs = new ArrayList<Drug>();
		try {
			Scanner readData = new Scanner(new FileReader(fileName));
			readData.nextLine();
			while (readData.hasNextLine()) {
				String line = readData.nextLine();
				// parse each line and add a drug object
				drugs.add(parseDrug(line));
			}
			readData.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Data loading done. Now building the drug-drug interactions...");
		// now all drug objects are ready. Update the DDI hash map using drug objects
		for (Drug drug : drugs) {
			HashMap<Drug, String> DDITemp = new HashMap<Drug,String>();
			// read the rawDDI map of a drug. Obtain the interacting drug IDs and descriptions
			for (String dbID : drug.getRawDDI().keySet()) {
				String info = drug.getRawDDI().get(dbID);
				// find the drug object using the drug bank ID (dbID)
				for (Drug drugObj : drugs) {
					if (drugObj.getId().equals(dbID)) {
						DDITemp.put(drugObj, info);
						break;
					}
				}			
			}
			drug.setDrugInteractions(DDITemp);
		}
		System.out.println("All done.");
	}

	/**
	 * This helper method reads a line of the drug data file, parses the information and output a drug object
	 * @param line a string containing all information of a drug
	 * @return a drug object
	 */
	private Drug parseDrug (String line) {
		String [] properties = line.split(">,<");
		String id = properties[0].substring(1);
		String name = properties[2];
		// the synonyms are separated by ";;". split them to an array list.
		String synonymLine = properties[3];
		ArrayList<String> synonyms = new ArrayList<String>(Arrays.asList(synonymLine.split(";;")));
		String description = properties[4];
		String formula = properties[6];
		String unii = properties[7];
		String casNum = properties[8];
		String pubchemCom = properties[9];
		String pubchemSub = properties[10];
		String toxicity = properties[11];
		// the drug-food interaction entries are separated by ";;". split them to an array list of strings.
		String DFILine = properties[12];
		// the DFI array list will be null if the information is not available or empty
		ArrayList<String> foodInteractions = new ArrayList<String>();
		if (!(DFILine.equals("Not Available") || DFILine.equals(""))) {
			String[] DFIs = DFILine.split(";;");
			Collections.addAll(foodInteractions, DFIs);
		}
		// initialize the drugInteractions hash map first. It will be updated later when all drugs are ready.
		HashMap<Drug,String> drugInteractions = new HashMap<Drug, String>();	
		/*
		 *  the format of DDIs in the raw file is: ID(a)@@name(a)@@description(a);;ID(b)@@name(b)@@description(b)...
		 *  store the relationship between ID and description in a hash map generated for the raw information
		 */
		String DDILine = properties[13];
		HashMap<String, String> rawDDI = new HashMap<String, String>();
		// the DDI should be null if the information is not available or empty
		if (!(DDILine.equals("Not Available") || DDILine.equals(""))) {
			ArrayList<String> DDIs = new ArrayList<String>(Arrays.asList(DDILine.split(";;")));
			for (String DDI : DDIs) {
				String dbID = DDI.split("@@")[0];
				String info = DDI.split("@@")[2];
				rawDDI.put(dbID, info);
			}
		}
		String structure = properties[14].substring(0, (properties[14].length() - 1));
		// construct a drug object using the obtained information
		Drug drug = new Drug(id, name, synonyms, description, formula, unii, casNum, pubchemCom, pubchemSub, toxicity,
				foodInteractions, drugInteractions, rawDDI, structure);
		return drug;
	}


	/**
	 * Obtains the list of drugs read by the data reader
	 * @return an array list of drug objects
	 */
	public ArrayList<Drug> getDrugs() {
		return drugs;
	}

	/**
	 * Obtains the current database file name
	 * @return the current database file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name of the database that the reader will read
	 * @param fileName the file name of the data
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static void main(String[] args) {
		double startTime = System.currentTimeMillis();
		String fileName = "20191031_FDASMDrugs_2546.txt";
		DataReader test = new DataReader(fileName);
		ArrayList<Drug> drugs = test.getDrugs();
		HashMap<String,Drug> drugNameList = new HashMap<String,Drug>();
		for (Drug drug : drugs) {
			String name = drug.getName();
			drugNameList.put(name, drug);
		}
		System.out.println("Testing the display of a drug...");
		Drug testDrug = drugs.get(5);
		System.out.println(testDrug.toString(120));	
		System.out.println("\n\n\nTesting to print one of its interacting drugs...");
		System.out.println(testDrug.getDrugInteractions().keySet().iterator().next().toString(120));
		System.out.println("\n\n\nLooking for the interaction between the current drug and Abacavir: ");
		for (String name : drugNameList.keySet()) {
			if (name.equals("Abacavir")) {
				Drug targetDrug = drugNameList.get(name);
				String interactionInfo = testDrug.getDrugInteractions().get(targetDrug);
				System.out.println(interactionInfo);
				break;
			}
		}
		double endTime = System.currentTimeMillis();
		System.out.println("The whole process took " + (endTime - startTime) / 1000 + " seconds");

	}
}
