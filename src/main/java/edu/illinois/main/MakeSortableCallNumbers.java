package edu.illinois.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import edu.illinois.bibinfo.CallNumber;
import edu.illinois.configuration.Configuration;
import edu.illinois.configuration.ConfigurationJSONImpl;
import edu.illinois.configuration.Parameter;
import edu.illinois.transformations.ColumnReducer;

public class MakeSortableCallNumbers
{
	public static final String DATA_SEP_PARAM = ",";
	public static final String DATA_FILE_PARAM = "input-file";
	public static final String COLUMN_NAME_PARAM = "columns-to-keep";
	public static final String TRANSFORMATION_PARAM = "transformation";
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String pathToJSONFile = args[0];
		
		Configuration config = new ConfigurationJSONImpl();
		config.readParams(pathToJSONFile);
		Map<String,Parameter> params = config.getParamMap();
		
		
		String pathToData = params.get(DATA_FILE_PARAM).getValue();
		String columnsToKeep = params.get(COLUMN_NAME_PARAM).getValue();
		String transformationToDo = params.get(TRANSFORMATION_PARAM).getValue();
		
		String[] desiredCols = columnsToKeep.split(DATA_SEP_PARAM);
		for(int i=0; i<desiredCols.length; i++)
			desiredCols[i] = desiredCols[i].trim();
		
		Scanner in = new Scanner(new FileReader(new File(pathToData)));
		
		String header = in.nextLine();
		String[] columnNames = ColumnReducer.separateFields(header);
		List<String> colList = new ArrayList<String>(desiredCols.length);
		
		for(String col : columnNames)
			colList.add(col.trim());
		
		int[] indexesToKeep = new int[desiredCols.length];
		int i=0;
		
		for(String col : desiredCols) {
			indexesToKeep[i] = colList.indexOf(col);
			i++;
		}
		
		
		while(in.hasNextLine()) {
			String rawLine = in.nextLine();
			String[] rawFields = ColumnReducer.separateFields(rawLine);
			
			int callNoIndex = colList.indexOf("NORMALIZED_CALL_NO");
			
			CallNumber callNumberObj = new CallNumber();
			String sortable = callNumberObj.CallNumberToSortable(rawFields[callNoIndex]);
			
			rawFields[callNoIndex] = sortable;
					
			String[] cols = ColumnReducer.removeColumns(rawFields, indexesToKeep);

			System.out.println(Arrays.toString(cols));
		}
		in.close();
	}

}
