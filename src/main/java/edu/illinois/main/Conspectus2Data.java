package edu.illinois.main;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.illinois.configuration.Configuration;
import edu.illinois.configuration.ConfigurationJSONImpl;
import edu.illinois.configuration.Parameter;
import edu.illinois.conspectus.Conspectus;
import edu.illinois.conspectus.ConspectusEntry;
import edu.illinois.conspectus.EntryComparator;

public class Conspectus2Data
{
	public static final String COLUMN_SEP_PARAM = ",";
	public static final String DATA_FILE_PARAM = "input-file";
	public static final String CONSPECTUS_FILE_PARAM = "conspectus-file";
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String pathToJSONFile = args[0];
		
		Configuration config = new ConfigurationJSONImpl();
		config.readParams(pathToJSONFile);
		Map<String,Parameter> params = config.getParamMap();
		
		
		
		String pathToData = params.get(DATA_FILE_PARAM).getValue();	
		String pathToConspectus = params.get(CONSPECTUS_FILE_PARAM).getValue();
		
		// read in the conspectus
		Conspectus conspectus = new Conspectus();
		
		// do we want to widen the conspectus categories?
		conspectus.setWidening(true);
		
		
		conspectus.readFromFile(pathToConspectus);
		
		// handle the CSV bib data
		conspectus.populateWithData(pathToData);
		
		
		
		List<ConspectusEntry> entries = conspectus.getEntries();
		
		Collections.sort(entries, new EntryComparator());
		for(ConspectusEntry e : entries)
			System.out.println(e);
		
		
	}

}
