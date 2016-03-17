package edu.illinois.bibinfo;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CallNumber
{
	public static final int DIGITS_OF_PRECISION = 2;

	private Pattern startsWithNumeric = Pattern.compile("^[0-9].*");

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

	/**
	 * Is this call number encoded as Dewey, LC, etc?
	 * 
	 * @param callNumber
	 * @return
	 */
	private CallNumberType callNumberType(String callNumber)
	{
		if(startsWithNumeric.matcher(callNumber).matches())
			return CallNumberType.DEWEY;

		if(callNumber.contains(":"))		// not sure this is an adequate test
			return CallNumberType.SUDOC;

		return CallNumberType.LC;
	}

	public String CallNumberToSortable(String callNumber)
	{
		String[] temp = callNumber.split(" ");
		callNumber = temp[0].trim();
		
		CallNumberType type = callNumberType(callNumber);

		if(type.equals(CallNumberType.DEWEY)) 
		{
			if(callNumber.contains("-") ||
					callNumber.contains("/"))
				return null;
			
			Double numeric = Double.parseDouble(callNumber);
			numeric *= Math.pow(10, DIGITS_OF_PRECISION);
			Integer intRep = numeric.intValue();
			String sortable = intRep.toString();
			
			return sortable;
			
		}	else {
			System.err.println("Skipping unhandled CallNumberType: " + callNumber);
			return null;
		}

	}


	
	public static void main(String[] args)
	{
		CallNumber callNumberObj = new CallNumber();
		
		Scanner in = new Scanner(System.in);
		String callNumber = "";
		while(! callNumber.equals("dummy")) {
			System.out.print("> ");
			callNumber = in.nextLine();
			
			if(callNumber.equalsIgnoreCase("q"))
				System.exit(0);
			
			String sortable = callNumberObj.CallNumberToSortable(callNumber);
			
			if(sortable == null)
				continue;
			
			

			System.out.println(":: " + sortable + " ::");
		}
		in.close();
	}
}
