package edu.illinois.dedup;

import java.util.Arrays;

public class DedupPrevLineSim implements Deduplicator
{
	private String[] previousLine = {""};
	private float similarityThreshold = 0.55f;

	private int numSeen = 0;
	private boolean verbose = false;
	
	@Override
	public boolean emit(String[] fields)
	{
		if(numSeen == 0)
		{
			previousLine = fields;
			numSeen++;
			return false;	
		}

		float union = 0.0f;
		for(int i=0; i<fields.length; i++) {
			if(previousLine.length > i && previousLine[i] != null)	// make sure we're not checking a null slot
			{
				if(fields[i].equals(previousLine[i]))
					union++;
			}
		}


		float proportion = union / fields.length;

		if(verbose && proportion > similarityThreshold)
		{
			System.err.println("Filtering THIS...");
			System.err.println("PREV: " + Arrays.toString(previousLine));
			System.err.println("THIS: " + Arrays.toString(fields));
			System.err.println("sim = " + proportion);
			System.err.println("union: " + union + "\n");
		}
		
		previousLine = fields;
		numSeen++;

		if(proportion >= similarityThreshold)
			return false;
		return true;
	}

	public void setVerbose(boolean verbose)
	{
		this.verbose = verbose;
	}
}
