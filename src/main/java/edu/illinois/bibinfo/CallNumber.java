package edu.illinois.bibinfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CallNumber
{

	public static final int DEWEY_TYPE = 1;

	public static final Pattern LEADING_DOT_PATTERN = Pattern.compile(" \\.");
	public static final Pattern META_PATTERN = Pattern.compile("[ \\-\\/]");
	public static final Pattern NUMERIC_PATTERN = Pattern.compile("^[0-9].*");
	
	private static final Map<String, String> LETTERS_TO_DIGITS;
	static {
		Map<String, String> map = new HashMap<String, String>();
		map.put("A", "01");
		map.put("B", "02");
		map.put("C", "03");
		map.put("D", "04");
		map.put("E", "05");
		map.put("F", "06");
		map.put("G", "07");
		map.put("H", "08");
		map.put("I", "09");
		map.put("J", "10");
		map.put("K", "11");
		map.put("L", "12");
		map.put("M", "13");
		map.put("N", "14");
		map.put("O", "15");
		map.put("P", "16");
		map.put("Q", "17");
		map.put("R", "18");
		map.put("S", "19");
		map.put("T", "20");
		map.put("U", "21");
		map.put("V", "22");
		map.put("W", "23");
		map.put("X", "24");
		map.put("Y", "25");
		map.put("Z", "26");
		LETTERS_TO_DIGITS = Collections.unmodifiableMap(map);
	}

	private int digitsOfPrecision = 2;



	/**
	 * @param callNumber
	 * @return
	 */
	public String CallNumberToSortable(String callNumber, int callNumberType)
	{
		
		if(callNumberType != DEWEY_TYPE)
			return null;

		callNumber = LEADING_DOT_PATTERN.matcher(callNumber).replaceAll(" ");
		
		String[] callNumberFields = META_PATTERN.split(callNumber);
		
		callNumber = callNumberFields[0].trim();



		if(! NUMERIC_PATTERN.matcher(callNumber).matches()) {
			System.err.println("skipping weird Dewey prefix: " + callNumber);
			return null;
		}

		Double numeric = 0.0;
		try {
			numeric = Double.parseDouble(callNumber);
		} catch (Exception e) {
			System.err.println("BAD DEWEY: " + callNumber);
			return null;
		}
		
		numeric *= Math.pow(10, digitsOfPrecision);
		Integer intRep = numeric.intValue();
		String sortable = intRep.toString();

		return sortable;



	}

	public void setDigitsOfPrecision(int digitsOfPrecision) {
		this.digitsOfPrecision = digitsOfPrecision;
	}


}
