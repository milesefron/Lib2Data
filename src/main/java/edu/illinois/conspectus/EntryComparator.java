package edu.illinois.conspectus;

import java.util.Comparator;

public class EntryComparator implements Comparator<ConspectusEntry>
{

	@Override
	public int compare(ConspectusEntry o1, ConspectusEntry o2)
	{
		return o1.getPopulation()< o2.getPopulation() ? -1 : 
			o1.getPopulation() == o2.getPopulation() ? 0 : 1;
	}

}
