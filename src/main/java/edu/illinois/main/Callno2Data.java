package edu.illinois.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.illinois.bibinfo.CallNumber;
import edu.illinois.configuration.Configuration;
import edu.illinois.configuration.ConfigurationJSONImpl;
import edu.illinois.configuration.Parameter;
import edu.illinois.dedup.DedupPrevLineSim;
import edu.illinois.dedup.Deduplicator;
import edu.illinois.transformations.ColumnReducer;

public class Callno2Data
{
	public static final String COLUMN_SEP_PARAM = ",";
	public static final String DATA_FILE_PARAM = "input-file";
	public static final String CALL_NUM_TYPE = "callno-type";
	public static final String CALL_NUM_COL_NO = "callno-col-name";
	public static final String CALL_NUM_DIGITS = "callno-digits";

	public static boolean removePrefixes = true;

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

		int desiredCallNoType = Integer.parseInt(params.get(CALL_NUM_TYPE).getValue());
		int callNoDigits      = Integer.parseInt(params.get(CALL_NUM_DIGITS).getValue());
		int indexOfCallNoType = colList.indexOf("CALL_NO_TYPE");
		int indexOfCallNo     = colList.indexOf("NORMALIZED_CALL_NO");

		CallNumber callNumberObj = new CallNumber();
		callNumberObj.setDigitsOfPrecision(callNoDigits);
		callNumberObj.setOmitPrefixes(removePrefixes);

		int nulls = 0;
		while(in.hasNextLine()) {
			String rawLine = in.nextLine();
			String[] rawFields = ColumnReducer.separateFields(rawLine);

			if(!dedup.emit(rawFields))
				continue;
			
			String callNoTypeString = rawFields[indexOfCallNoType];
			if(callNoTypeString.equals("") || callNoTypeString.equals(" ")) 
				continue;
			

			
			
			int callNoType = 1;
			
			if(Callno2Data.isNumeric(callNoTypeString))
				Integer.parseInt(callNoTypeString);


			
			if(callNoType != desiredCallNoType)
				continue;


			String sortable = callNumberObj.CallNumberToSortableByFormatting(rawFields[indexOfCallNo], callNoType);
			
			if(sortable == null) {
				nulls++;
				System.err.println("FAILURE: " + sortable + "  ::  " + rawFields[indexOfCallNo]);
				System.err.println(Arrays.toString(rawFields));
				continue;
			} else {
				//System.err.println("SUCCES: " + sortable + "  ::  " + rawFields[indexOfCallNo]);
			}

			float parsed = Float.parseFloat(sortable);
			if(parsed < 0)
				continue;
			
			System.out.println(sortable);
			
		}
		in.close();
		System.err.println(nulls + " null callnos.");
	}

	
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
