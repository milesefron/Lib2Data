package edu.illinois.transformations;

import java.util.regex.Pattern;

public class ColumnReducer
{
	public static final Pattern SEP_PATTERN = Pattern.compile(",");
	
	public static String[] separateFields(String rawLine) {
		String[] fields = SEP_PATTERN.split(rawLine);
		return fields;
	}
	
	public static String[] removeColumns(String[] allColumns, int[] columnsToKeep) {
		String[] groomedRow = new String[columnsToKeep.length];
		for(int i=0; i<columnsToKeep.length; i++) {
			groomedRow[i] = allColumns[columnsToKeep[i]];
		}
		return groomedRow;
	}
	
}
