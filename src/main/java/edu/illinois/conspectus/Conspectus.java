package edu.illinois.conspectus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import edu.illinois.bibinfo.CallNumber;

public class Conspectus
{
	public static Pattern COMMA_PATTERN = Pattern.compile(",");
	public static Pattern DASH_PATTERN  = Pattern.compile("-");
	public static Pattern NUM_START_PATTERN = Pattern.compile("^[0-9].*");
	
	public static int DIVISION_COL = 0;
	public static int CATEGORY_COL = 1;
	public static int LC_CALL_COL  = 2;
	public static int DEWEY_CALL_COL = 3;
	
	private List<ConspectusEntry> entries;
	
	public Conspectus()
	{
		entries = new ArrayList<ConspectusEntry>();
	}
	
	public void readFromFile(String pathToFile) 
	{
		int skipped = 0;

		try
		{
			Scanner in = new Scanner(new FileInputStream(new File(pathToFile)));
			// read header
			in.nextLine();
			while(in.hasNext()) {
				String line = in.nextLine();
				String[] fields = COMMA_PATTERN.split(line);
				if(fields.length < 4) {
					skipped++;
					continue;
				}
				String division = fields[DIVISION_COL];
				String category = fields[CATEGORY_COL];
				String lcCallNo = fields[LC_CALL_COL];
				String deweyCallNo = fields[DEWEY_CALL_COL];
				
				if(deweyCallNo.equals("---")) {
					skipped++;
					continue;
				}
				
				String[] deweys = DASH_PATTERN.split(deweyCallNo);

				
				ConspectusEntry entry = new ConspectusEntry();
				entry.setCategory(category);
				entry.setDivision(division);
				float dLower = 0.0f;
				float dUpper = 0.0f;
				if(deweys != null) {
					dLower = Float.parseFloat(deweys[0]);
					if(deweys.length > 1)
						dUpper = Float.parseFloat(deweys[1]);
				}
				if(dUpper == 0.0)
					dUpper = dLower;
				// move our decimals
				dLower *= 1000;
				dUpper *= 1000;
				
				entry.setDeweyCallNoBottom(dLower);;
				entry.setDeweyCallNoTop(dUpper);
				
				entries.add(entry);
			}
			in.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.err.println("Read " + entries.size() + " conspectus entries.");
		System.err.println("Skipped " + skipped + " entries.");
	}
	
	public List<ConspectusEntry> getEntries()
	{
		return entries;
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String pathToData = "./data/google.csv";
		String pathToConspectus = "./data/conspectus.csv";
		
		Conspectus conspectus = new Conspectus();
		conspectus.readFromFile(pathToConspectus);
		
		Scanner in = new Scanner(new FileReader(new File(pathToData)));
		// skip header
		in.nextLine();

		
		while(in.hasNext()) {
			String line = in.nextLine();
			String[] fields = COMMA_PATTERN.split(line);

			
			CallNumber callNoObj = new CallNumber();
			String callNoString = callNoObj.CallNumberToSortableByMovingDecimal(fields[11], 1);
			if(callNoString == null)
				continue;
			float callNoFloat = Float.parseFloat(callNoString);
			
			System.out.println(callNoString);
		}
		in.close();
		System.exit(1);
		
		List<ConspectusEntry> entries = conspectus.getEntries();
		Collections.sort(entries, new EntryComparator());
		for(ConspectusEntry e : entries)
			System.out.println(e);
	}
}
