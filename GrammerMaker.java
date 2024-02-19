package comprehensive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class GrammerMaker {

	private static Map<String, ArrayList<String>> definitions = new HashMap<String, ArrayList<String>>();
	static Random rng = new Random();
	static ArrayList<String> dictionary = new ArrayList<String>();

	public static void createGrammarFile(String sourceFile, int nonTerminals, int productionsRules, int nonTerminalsCount) throws IOException  {
		ArrayList<String> lines = new ArrayList<String>();
		buildDictionary(new File(sourceFile));
		createDefinitions(nonTerminalsCount);
		makeGrammar(lines, nonTerminals, productionsRules);
		createStartClause(lines, nonTerminals, productionsRules);

		FileWriter file = new FileWriter("GrammarMakerOutput.txt"); // this creates the file the grammar will be stored or overwrites an existing file
		file.write(String.join("", lines));
		file.flush();
		file.close();
	}
	
	private static void buildDictionary(File source) throws FileNotFoundException {
		Scanner input = new Scanner (source);
		while (input.hasNextLine()) 
			dictionary.add(input.nextLine());
	}
	
	private static void createDefinitions(int nonTerminalsCount) {
		for (int i = 0; i < nonTerminalsCount; i++) {
			ArrayList<String> arr;
			String terminal = "<" + dictionary.get(rng.nextInt(dictionary.size())) + ">";
			if (definitions.get(terminal) != null) {
				arr = definitions.get(terminal);
			}
			else {
				arr = new ArrayList<String>();
				arr.add(dictionary.get(rng.nextInt(dictionary.size())));
			}
			arr.add(dictionary.get(rng.nextInt(dictionary.size())));
  			definitions.put(terminal, arr);
		}
	}
	
	public static void makeGrammar(ArrayList<String> lines, int nonTerminalsQuantity, int productionRules) throws IOException {
		
		for (String nonTerminal : definitions.keySet()) {
			lines.add("{\n" + nonTerminal + "\n");
			for (String terminal : definitions.get(nonTerminal)) {
				lines.add(terminal + "\n");
			}
			lines.add("}\n");
		}
		
	}

	private static void createStartClause(ArrayList<String> lines, int productionRules, int nonTerminalsQuantity) {
		lines.add("{");
		lines.add("\n<start>\n");
		ArrayList<String> nonTerminalsList = new ArrayList<String>(definitions.keySet());
	
		for (int l = 0; l < productionRules; l++) {
			for (int i = 0; i < nonTerminalsQuantity; i++) {
				String nonTerminal = nonTerminalsList.get(rng.nextInt(nonTerminalsList.size()));
				int arbitrary = rng.nextInt(nonTerminalsQuantity);
				if (arbitrary % 2 == 0) {
					lines.add(nonTerminal);
				}
				else {
					lines.add(definitions.get(nonTerminal).get(rng.nextInt(definitions.get(nonTerminal).size())));
				}
				lines.add(" ");
				
			}
			lines.add("\n");
		}
		lines.add("}");

	}
}
