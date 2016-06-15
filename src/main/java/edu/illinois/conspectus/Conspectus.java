package edu.illinois.conspectus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import edu.illinois.bibinfo.CallNumber;
import edu.illinois.transformations.ColumnReducer;

public class Conspectus
{
	public static Pattern COMMA_PATTERN = Pattern.compile(",");
	public static Pattern DASH_PATTERN  = Pattern.compile("-");
	public static Pattern NUM_START_PATTERN = Pattern.compile("^[0-9].*");

	public static int DIVISION_COL = 0;
	public static int CATEGORY_COL = 1;
	public static int LC_CALL_COL  = 2;
	public static int DEWEY_CALL_COL = 3;

	private boolean widen = false;
	
	private List<ConspectusEntry> entries;
	private int[] cellCounts;

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
				String[] fields = ColumnReducer.separateFields(line);
				
				
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
					

				CallNumber callNoObj = new CallNumber();
				if(deweys.length > 0) {
					if(deweys[0].startsWith("."))
						deweys[0] = "0" + deweys[1];
					dLower = Float.parseFloat(callNoObj.CallNumberToSortableByFormatting(deweys[0], CallNumber.DEWEY_TYPE));	
				}
				if(deweys.length > 1) {
					if(deweys[1].startsWith("."))
						deweys[1] = "0" + deweys[1];
					dUpper = Float.parseFloat(callNoObj.CallNumberToSortableByFormatting(deweys[1], CallNumber.DEWEY_TYPE));
				
					
				  //deal with integer call numbers
				} else { 
					if(!widen)
						dUpper = dLower;
					else
						dUpper = dLower + 0.99f; 
					
				}
				


				//System.err.println(dLower + "\t\t" + dUpper);
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

		cellCounts = new int[entries.size()];

		System.err.println("CONSPECTUS READ-IN STATS");
		System.err.println("Read " + entries.size() + " conspectus entries.");
		System.err.println("Skipped " + skipped + " unparsable entries.");
	}

	public void populateWithData(String pathToCSV) {
		Scanner in;
		try
		{
			in = new Scanner(new FileReader(new File(pathToCSV)));

			// skip header
			in.nextLine();


			System.err.print("Reading CSV data...");
			
			int orphans = 0;
			int nonOrphans = 0;
			while(in.hasNext()) {
				String line = in.nextLine();
				String[] fields = COMMA_PATTERN.split(line);


				CallNumber callNoObj = new CallNumber();
				if(fields.length < 12)
					continue;
				
				String callNoString = callNoObj.CallNumberToSortableByFormatting(fields[11], 1);
				if(callNoString == null)
					continue;
				float callNoFloat = Float.parseFloat(callNoString);
				
				// horribly ineffecient.
				int i=0;
				for(ConspectusEntry entry : entries) {
					if(entry.deweyBelongsIn(callNoFloat)) {
						cellCounts[i]++;
						//System.err.println(fields[11] + "," + callNoString + "," + entry.getCategory() + " // " + entry.getDivision());
						break;
					}
					i++;
				}
				if(i == entries.size()) {
					orphans++;
					System.err.println("ORPHAN: " + fields[0] + "," +  fields[11]);
				} else
					nonOrphans++;
				
			}
			System.err.println("done.");
			System.err.println("CSV READ-IN STATS");
			System.err.println("NON-ORPHANS: " + nonOrphans);
			System.err.println("ORPHANS: " + orphans + " :: %" + 100 * (float)orphans/((float)nonOrphans + (float)orphans));

		// reindex entries with count data
		int i=0;
		List<ConspectusEntry> wCounts = new ArrayList<ConspectusEntry>(entries.size());
		for(ConspectusEntry entry : entries) {
			entry.setPopulation(cellCounts[i++]);
			wCounts.add(entry);
		}
		entries = wCounts;
			
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public List<ConspectusEntry> getEntries()
	{
		return entries;
	}

	public void setWidening(boolean widen)
	{
		this.widen = widen;
	}
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String pathToData = "./data/google.csv";
		String pathToConspectus = "./data/conspectus.csv";

		Conspectus conspectus = new Conspectus();
		conspectus.readFromFile(pathToConspectus);
		
		String rawCallNoField = "631.6";
		CallNumber callNoObj = new CallNumber();
		String callNoString = callNoObj.CallNumberToSortableByFormatting(rawCallNoField, 1);
		float callNoFloat = Float.parseFloat(callNoString);
		
		System.err.println(callNoFloat);
		
		// horribly ineffecient.
		List<ConspectusEntry> entries = conspectus.getEntries();
		for(ConspectusEntry entry : entries) {
			if(entry.deweyBelongsIn(callNoFloat)) {
				System.err.println(callNoString + "," + entry.getCategory() + " // " + entry.getDivision());
				break;
			}
		}
		
	}
}
