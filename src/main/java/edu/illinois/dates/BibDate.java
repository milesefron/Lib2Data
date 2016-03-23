package edu.illinois.dates;

import java.util.regex.Pattern;

public class BibDate
{
	private Pattern RANGE_PATTERN = Pattern.compile("-");
	private Pattern MISSING_PATTERN = Pattern.compile(".*u.*");
	private Pattern DATE_PATTERN = Pattern.compile("[0-9][0-9][0-9][0-9]");
	
	private DateRangePolicy dateRangePolicy = DateRangePolicy.START;
	
	private boolean rescue = true;
	private int earliestYear = 1200;
	private int latestYear   = 2017;
	
	public String getDate(String obs) {
		String ret = null;
		
		if(obs.contains("-"))
			ret = handleRange(obs);
		else
			if(!MISSING_PATTERN.matcher(obs).matches())
				ret = obs;
		if(ret != null && ! DATE_PATTERN.matcher(ret).matches())
			ret = null;
		if(ret != null) {
			int intVal = Integer.parseInt(ret);
			if(intVal < earliestYear || intVal > latestYear)
				ret = null;
		}
		
		return ret;
	}
	
	public String handleRange(String range) {
		String[] toks = RANGE_PATTERN.split(range);
		
		String target = null;
		String backup = null;
		
		// figure out which part of the range we want
		if(dateRangePolicy.equals(DateRangePolicy.START)) {
			target = toks[0];
			backup = toks[1];
		} else {
			target = toks[1];
			backup = toks[0];
		}
		
		// if both start and end are jacked up, return null
		if(MISSING_PATTERN.matcher(target).matches() && MISSING_PATTERN.matcher(backup).matches())
			return null;
		
		// if just our start is missing, return backup
		if(MISSING_PATTERN.matcher(target).matches())
			return backup;
		
		if(target.equals("9999"))
			target = backup;
		if(target.equals("9999"))
			target = null;
		
		return target;
		
	}
	
	public DateRangePolicy getDateRangePolicy()
	{
		return dateRangePolicy;
	}
	public void setDateRangePolicy(DateRangePolicy dateRangePolicy)
	{
		this.dateRangePolicy = dateRangePolicy;
	}
	public boolean isRescue()
	{
		return rescue;
	}
	public void setRescue(boolean rescue)
	{
		this.rescue = rescue;
	}
	
	
}
