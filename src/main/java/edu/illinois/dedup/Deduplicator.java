package edu.illinois.dedup;

public interface Deduplicator
{

	/**
	 * 
	 * @param fields our array of fields (columns) extracted from our CSV-like file input.
	 * @return true/false... should we emit (analyze & print) this record?
	 */
	public boolean emit(String[] fields);
}
