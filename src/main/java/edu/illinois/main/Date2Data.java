package edu.illinois.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.illinois.configuration.Configuration;
import edu.illinois.configuration.ConfigurationJSONImpl;
import edu.illinois.configuration.Parameter;
import edu.illinois.dates.BibDate;
import edu.illinois.dedup.DedupPrevLineSim;
import edu.illinois.dedup.Deduplicator;
import edu.illinois.transformations.ColumnReducer;

public class Date2Data
{
	public static final String COLUMN_SEP_PARAM = ",";
	public static final String DATA_FILE_PARAM = "input-file";
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String pathToJSONFile = args[0];
		
		Configuration config = new ConfigurationJSONImpl();
		config.readParams(pathToJSONFile);
		Map<String,Parameter> params = config.getParamMap();
		
		Deduplicator dedup = new DedupPrevLineSim();
		
		
		String pathToData = params.get(DATA_FILE_PARAM).getValue();		
		Scanner in = new Scanner(new FileReader(new File(pathToData)));
		
		String header = in.nextLine();
		String[] columnNames = ColumnReducer.separateFields(header);
		List<String> colList = new ArrayList<String>();
		
		for(String col : columnNames)
			colList.add(col.trim());
		
		int indexOfDate       = colList.indexOf("PUB_DATES_COMBINED");
		
		BibDate dateObj = new BibDate();
		
		
		while(in.hasNextLine()) {
			String rawLine = in.nextLine();
			String[] rawFields = ColumnReducer.separateFields(rawLine);
			
			if(!dedup.emit(rawFields))
				continue;
			
			String dateString = rawFields[indexOfDate];
			if(dateString.equals(""))
				continue;
		
			
			
			
			
			String sortable = dateObj.getDate(dateString);
			
			if(sortable == null)
				continue;

			System.out.println(sortable); // + " " + rawFields[indexOfDate]);
		}
		in.close();
	}

}
