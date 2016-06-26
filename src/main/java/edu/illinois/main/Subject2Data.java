package edu.illinois.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javafx.scene.SubScene;
import edu.illinois.configuration.Configuration;
import edu.illinois.configuration.ConfigurationJSONImpl;
import edu.illinois.configuration.Parameter;
import edu.illinois.transformations.ColumnReducer;

public class Subject2Data
{
	public static final String COLUMN_SEP_PARAM = ",";
	public static final String DATA_FILE_PARAM = "input-file";
	public static final int COLUMN_OF_SUBJECTS = 3;

	public static void main(String[] args) throws FileNotFoundException
	{
		String pathToJSONFile = args[0];

		int policy = 1;
		try {
			policy = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.err.println("USAGE: Subject2Data <config_file> [123] ## where 1 2 3 mean different ways of handling compound subjects.");
			System.exit(-1);
		}

		Configuration config = new ConfigurationJSONImpl();
		config.readParams(pathToJSONFile);
		Map<String,Parameter> params = config.getParamMap();

		Map<String,Integer> histogram = new HashMap<String,Integer>();

		//Deduplicator dedup = new DedupPrevLineSim();


		String pathToData = params.get(DATA_FILE_PARAM).getValue();		
		Scanner in = new Scanner(new FileReader(new File(pathToData)));

		//String header = in.nextLine();
		//String[] columnNames = ColumnReducer.separateFields(header);
		//List<String> colList = new ArrayList<String>();

		//for(String col : columnNames)
		//	colList.add(col.trim());




		while(in.hasNextLine()) {
			String rawLine = in.nextLine();
			String[] rawFields = ColumnReducer.separateFields(rawLine);


			String subjectString = rawFields[COLUMN_OF_SUBJECTS];

			Set<String>subjectStrings = Subject2Data.formatSubject(subjectString, policy);
			for(String ss : subjectStrings) {
				Integer current = histogram.get(ss);
				if(current == null)
					current = 0;
				current++;
				histogram.put(ss, current);
			}

		}
		in.close();

		for(String subject : histogram.keySet())
			System.out.println(histogram.get(subject) + "\t\t" + subject);
	}



	/**
	 * 
	 * @param input
	 * @param policy 	1 -> 	literal, keep the whole line intact
	 * 					2 ->	explode each subject and tally each sub-field as a subject
	 * 					3 -> 	only retain the FIRST subject heading per line.  ignore all others.	
	 * @return
	 */
	public static Set<String> formatSubject(String input, int policy)
	{
		Set<String> subjects = new HashSet<String>();

		input = input.replaceAll("\\.", "");

		if(policy == 1) {
			input = input.replaceFirst("\\$.", "");
			input = input.replaceFirst("2fast.*", "");
			input = input.replaceAll("\\$.", " / ");
			if(input.endsWith("$"))
				input = input.substring(0, input.length()-1); // get rid of trailing $ on some lines.
			subjects.add(input);
		} else if(policy == 2) {
			input = input.replaceFirst("2fast.*", "");
			if(input.endsWith("$"))
				input = input.substring(0, input.length()-1); // get rid of trailing $ on some lines.
			String[] subfields = input.split("\\$.");
			for(String subfield : subfields)
				subjects.add(subfield);
		} else if (policy == 3) {
			input = input.replaceFirst("2fast.*", "");
			if(input.endsWith("$"))
				input = input.substring(0, input.length()-1); // get rid of trailing $ on some lines.
			String[] subfields = input.split("\\$.");
			if(subfields != null)
				subjects.add(subfields[1].trim());
		}
		//System.err.println(subjects);
		return subjects;
	}
}
