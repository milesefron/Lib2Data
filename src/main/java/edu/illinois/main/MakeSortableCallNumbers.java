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
import edu.illinois.transformations.ColumnReducer;

public class MakeSortableCallNumbers
{
	public static final String COLUMN_SEP_PARAM = ",";
	public static final String DATA_FILE_PARAM = "input-file";
	public static final String CALL_NUM_TYPE = "callno-type";
	public static final String CALL_NUM_COL_NO = "callno-col-name";
	public static final String CALL_NUM_DIGITS = "callno-digits";
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String pathToJSONFile = args[0];
		
		Configuration config = new ConfigurationJSONImpl();
		config.readParams(pathToJSONFile);
		Map<String,Parameter> params = config.getParamMap();
		

		
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
		
		while(in.hasNextLine()) {
			String rawLine = in.nextLine();
			String[] rawFields = ColumnReducer.separateFields(rawLine);
			
			String callNoTypeString = rawFields[indexOfCallNoType];
			if(callNoTypeString.equals("")) {
				System.err.println("SKIPPING: " + rawLine);
				continue;
			}
			
			int callNoType = Integer.parseInt(callNoTypeString);

			
			if(callNoType != desiredCallNoType)
				continue;
			
			
			
			String sortable = callNumberObj.CallNumberToSortable(rawFields[indexOfCallNo], callNoType);
			


			System.out.println(sortable);
		}
		in.close();
	}

}
