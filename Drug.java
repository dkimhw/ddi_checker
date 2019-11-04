import java.util.ArrayList;
import java.util.HashMap;
/**
 * This class describes a FDA-approved small-molecule drug.
 * @author Wei Han
 */
public class Drug {
	private String id;
	private String name;
	private ArrayList<String> synonyms = new ArrayList<String>();
	private String description;
	private String formula;
	private String unii;
	private String casNum;
	private String pubchemCom;
	private String pubchemSub;
	private String toxicity;
	private ArrayList<String> foodInteractions = new ArrayList<String>();
	/*
	 *  A hash map is used to store the information of drug-drug interactions (DDIs). The key is a drug having interactions
	 *  with the target drug and the value (string) is a short description of the interaction.
	 */
	private HashMap<Drug,String> drugInteractions = new HashMap<Drug, String>();
	/**
	 * This is a temporary hash map storing the DDI information while reading the dataset. During the reading process, not all
	 * keys (drug objects) for the above hash map are ready. Instead, the DrugBank IDs read from the data can be used as keys.
	 */
	private HashMap<String, String> rawDDI = new HashMap<String, String>();
	private String structure;
	
	/**
	 * Constructs a drug object containing the necessary information of a drug.
	 * @param id The DrugBank ID of the drug
	 * @param name the common name of the drug
	 * @param synonyms other names of the drug
	 * @param description a short paragraph describing the drug
	 * @param formula the chemical formula of a small-molecule drug
	 * @param unii the drug's Unique Ingredient Identifier in FDA's system
	 * @param casNum the CAS registry number of the chemical compound (i.e., the small-molecule drug)
	 * @param pubchemCom the drug's compound ID in PubChem database (a compound must have a substance ID)
	 * @param pubchemSub the drug's substance ID in PubChem database (a substance does not necessarily have a compound ID)
	 * @param toxicity the toxicity information of the drug
	 * @param foodInteractions the known interactions between the drug and food substances
	 * @param drugInteractions the known interactions between the drug and other drugs
	 * @param rawDDI drug-interaction information read from the data file: DrugBank IDs are keys and descriptions are values.
	 * @param structure the SMILES (simplified molecular-input line-entry system) structure of the drug, standardized by PubChem's structure standardization service
	 */
	public Drug(String id, String name, ArrayList<String> synonyms, String description, String formula, String unii, String casNum,
			String pubchemCom, String pubchemSub, String toxicity, ArrayList<String> foodInteractions,
			HashMap<Drug, String> drugInteractions, HashMap<String, String> rawDDI, String structure) {
		this.id = id;
		this.name = name;
		this.synonyms = synonyms;
		this.description = description;
		this.formula = formula;
		this.unii = unii;
		this.casNum = casNum;
		this.pubchemCom = pubchemCom;
		this.pubchemSub = pubchemSub;
		this.toxicity = toxicity;
		this.foodInteractions = foodInteractions;
		this.drugInteractions = drugInteractions;
		this.rawDDI = rawDDI;
		this.structure = structure;
	}

	/**
	 * This helper method Wraps an extremely long string so that when printed, it looks prettier with the the line breaks.
	 * @param str the input string
	 * @param maxLineLen maximum number of characters allowed to be printed in each line
	 * @return a wrapped string with line breaks
	 */
	private String wrapString(String str, int maxLineLen) {
		String[] words = str.split(" ");
		// the number of characters in the current line
		int lineCharNum = 0;
		// the number of words that have been processed
		int wordCount = 0;
		while (wordCount < words.length) {
			int wordCharNum = words[wordCount].toCharArray().length;
			// add the character number of the new word to the line character number counter
			lineCharNum += wordCharNum;
			/*
			 * if adding the word will exceed the limit, make the word to next line by adding a line-feed.
			 * update the line character number counter
			 */
			if(lineCharNum > maxLineLen) {
				words[wordCount] = "\n" +  words[wordCount];
				lineCharNum = wordCharNum;
			}
			// consider the space position after the word
			lineCharNum ++;
			wordCount ++;
		}
		String recombinedWord = "";
		for (String word : words) {
			recombinedWord += word + " ";
		}
		return recombinedWord;
	}
	
	/**
	 * Overrides the toString method to print the information of a drug object
	 * @param lineLen the maximum number of characters allowed for each line
	 * @return the drug information
	 */
	public String toString(int lineLen) {	
		//count the number of drug-drug interactions
		int DDINum = drugInteractions.keySet().size();
		// count the number of drug-food interaction (DFI) descriptions
		int DFINum = foodInteractions.size();
		String printInfo = ("DrugBank ID: " + id + "\nDrug Name: " + wrapString(name, lineLen) + "\nSynonyms: \n" + wrapString(synonyms.toString(),lineLen) + "\nChemical Formula: " +
				formula + "\nStructure: \n" + structure + "\nUNII: " + unii + "\nDescription: \n" + wrapString(description, lineLen) + "\nToxicity: \n" + wrapString(toxicity, lineLen) + 
				"\nFood Interactions: there is/are " + DFINum + " description(s) about food interactions." + "\nDrug Interactions: there is/are " + DDINum + " known drug-drug interaction(s).");
		return printInfo;
	}
	
	public static void main(String[] args) {
	}
	/**
	 * Obtains the DrugBank ID of a drug.
	 * @return the DrugBank ID of the drug
	 */
	public String getId() {
		return id;
	}

	/**
	 * Obtains the name of a drug.
	 * @return the name of the drug
	 */
	public String getName() {
		return name;
	}

	/**
	 * Updates the name of a drug.
	 * @param name the desired name of the drug.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtains synonyms of the name of a drug.
	 * @return a list of names for a specific drug
	 */
	public ArrayList<String> getSynonyms() {
		return synonyms;
	}

	/**
	 * Updates the synonyms of a drug.
	 * @param synonyms a desired list of names
	 */
	public void setSynonyms(ArrayList<String> synonyms) {
		this.synonyms = synonyms;
	}

	/**
	 * Obtains the description of a drug.
	 * @return a short paragraph describing the drug
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Updates the description of drug.
	 * @param description the new description of the drug
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Obtains the chemical formula of a drug.
	 * @return the chemical formula of the drug
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * Obtains the UNII of a drug.
	 * @return the UNII of the drug.
	 */
	public String getUnii() {
		return unii;
	}

	/**
	 * Obtains the CAS number of a drug.
	 * @return the CAS number of the drug
	 */
	public String getCasNum() {
		return casNum;
	}

	/**
	 * Obtains the PubChem Compound ID of a drug.
	 * @return the PubChem Compound ID
	 */
	public String getPubchemCom() {
		return pubchemCom;
	}


	/**
	 * Obtains the PubChem Substance ID of a drug.
	 * @return the PubChem Substance ID
	 */
	public String getPubchemSub() {
		return pubchemSub;
	}

	/**
	 * Obtains the toxicity information of a drug.
	 * @return the toxicity of the drug
	 */
	public String getToxicity() {
		return toxicity;
	}

	/**
	 * Updates the toxicity information of a drug.
	 * @param toxicity the new toxicity description
	 */
	public void setToxicity(String toxicity) {
		this.toxicity = toxicity;
	}

	/**
	 * Obtains the drug-food interaction information of a drug.
	 * @return the known drug-food interaction of the drug
	 */
	public ArrayList<String> getFoodInteractions() {
		return foodInteractions;
	}

	/**
	 * Updates the drug-food interaction information of a drug.
	 * @param foodInteractions the new descriptions of drug-food interactions
	 */
	public void setFoodInteractions(ArrayList<String> foodInteractions) {
		this.foodInteractions = foodInteractions;
	}

	/**
	 * Obtains the drug-drug interactions of a drug.
	 * @return the known DDIs of the drug
	 */
	public HashMap<Drug, String> getDrugInteractions() {
		return drugInteractions;
	}

	/**
	 * Updates the drug-drug interactions of a drug
	 * @param drugInteractions the new DDIs of the drug
	 */
	public void setDrugInteractions(HashMap<Drug, String> drugInteractions) {
		this.drugInteractions = drugInteractions;
	}

	/**
	 * Obtains the SMILES structure of a small-molecule drug.
	 * @return the SMILES of the drug
	 */
	public String getStructure() {
		return structure;
	}

	/**
	 * Updates the SMILES structure of a drug.
	 * @param structure the new SMILES structure
	 */
	public void setStructure(String structure) {
		this.structure = structure;
	}

	/**
	 * Obtains the raw DDI information read from the database.
	 * @return a hash map between DrugBank IDs and interaction descriptions
	 */
	public HashMap<String, String> getRawDDI() {
		return rawDDI;
	}

	/**
	 * Updates the raw DDI information of a drug.
	 * @param rawDDI a hash map containing the desired DDI information
	 */
	public void setRawDDI(HashMap<String, String> rawDDI) {
		this.rawDDI = rawDDI;
	}
	
}
