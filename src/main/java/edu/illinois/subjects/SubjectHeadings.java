package edu.illinois.subjects;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SubjectHeadings
{
	private static Pattern NEW_HEADING_PATTERN = Pattern.compile("^[a-z][A-Z].*");
	private static Pattern PUNCTUATION_PATTERN = Pattern.compile("[(){},.;!?<>%]");
	private static Pattern SPACE_PATTERN = Pattern.compile(" ");
	
	public static List<String> extractHeadings(String headingText) {
		List<String> headingList = new ArrayList<String>();
		
		
		String[] tokens = SPACE_PATTERN.split(headingText);
		StringBuilder b = new StringBuilder();
		for(String token : tokens) {
			token = PUNCTUATION_PATTERN.matcher(token).replaceAll(" ");
			token = token.trim();
			if(token.equals("650"))
				continue;
			if(token.length() < 2)
				continue;
			
			
			// if we see that a new heading has started, finalize our in-progress heading and push it to the list.
			if(NEW_HEADING_PATTERN.matcher(token).matches()) {
				token = token.substring(1);	// remove alpha subject code
				
				String heading = b.toString();
				if(heading.length() > 0) {
					headingList.add(heading.trim());
					b = new StringBuilder();
				}
			}
			b.append(token + " ");
		}
		String heading = b.toString();
		if(heading.length() > 0)
			headingList.add(heading.trim());
		
		return headingList;
	}
	
	public static void main(String[] args)
	{
		String test = "650 0 aGeology zNew York (State) zNew York.";
		List<String> headings = SubjectHeadings.extractHeadings(test);
		System.out.println(headings);
	}
}
