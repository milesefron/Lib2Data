package edu.illinois.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
			
			// subjectString = Subject2Data.formatSubject(subjectString);
			subjectString = Subject2Data.formatSubjectCoarse(subjectString);
			Integer current = histogram.get(subjectString);
			if(current == null)
				current = 0;
			current++;
			histogram.put(subjectString, current);
			
			
		}
		in.close();
		
		for(String subject : histogram.keySet())
			System.out.println(histogram.get(subject) + "\t\t" + subject);
	}

	
	public static String formatSubject(String input)
	{
		input = input.replaceAll("\\.", "");
		return input;
	}
	
	public static String formatSubjectCoarse(String input)
	{
		input = input.replaceAll("\\.", "");
		input = input.replaceFirst("\\$.", "");
		if(input.contains("$"))
			input = input.substring(0, input.indexOf("$"));
		
		return input;
	}
}
