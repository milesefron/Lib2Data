package edu.illinois.configuration;

/**
 * Basic unit of a configuration.  Just a bunch of name/value tuples.
 * 
 * @author Miles Efron
 *
 */
public class Parameter
{
	private String name;
	private String value;
	
	// getters and setters
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	
	@Override
	public String toString()
	{
		String ret = name + ": " + value;
		return ret;
	}
	
}
