package edu.illinois.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import edu.illinois.bibinfo.CallNumber;

/**
 * This class should be run last in the pipeline.  It simply makes sure that each call number
 * is indeed the numeric, formatted (e.g. clipped at 2 decimal places), part of a Dewey call
 * number.
 * 
 * File is assumed to be comparable to the output of Callno2Data.  i.e. one callno per line.
 * 
 * @author Miles Efron
 *
 */
public class CleanCallnos
{
	public static void main(String[] args) throws FileNotFoundException
	{
		if(args.length != 1) {
			System.err.println("USAGE: CleanCallnos <file of messy callnos>");
			System.exit(1);
		}
		
		CallNumber callNumberObj = new CallNumber();
		callNumberObj.setDigitsOfPrecision(2);
		callNumberObj.setOmitPrefixes(true);
		
		Scanner in = new Scanner(new FileReader(new File(args[0])));
		in.nextLine();
		while(in.hasNext()) {
			String line = in.nextLine();
			try {
				String callno = callNumberObj.CallNumberToSortableByFormatting(line, 1);
				if(callno == null)
					continue;
				if(callno.length() < 2 || callno.contains(" ") || callno.matches(".*[A-Za-z].*"))
					continue;
				
				System.out.println(callno);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		in.close();
		
		
	}
	
	


}
