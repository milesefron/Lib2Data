package edu.illinois.conspectus;


public class ConspectusEntry 
{
	private String category;
	private String division;
	private float  lcCallNoBottom;
	private float  lcCallNoTop;
	private float  deweyCallNoBottom;
	private float deweyCallNoTop;
	private int population = 0;
	
	public boolean deweyBelongsIn(float deweyCandidate)  {
		if(deweyCallNoBottom <= deweyCandidate && deweyCallNoTop >= deweyCandidate)
			return true;
		return false;
	}
	
	public void increment()
	{
		population++;
	}
	public int getPopulation()
	{
		return population;
	}
	public void setPopulation(int population)
	{
		this.population = population;
	}
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
	public String getDivision()
	{
		return division;
	}
	public void setDivision(String division)
	{
		this.division = division;
	}
	public float getLcCallNoBottom()
	{
		return lcCallNoBottom;
	}
	public void setLcCallNoBottom(float lcCallNoBottom)
	{
		this.lcCallNoBottom = lcCallNoBottom;
	}
	public float getLcCallNoTop()
	{
		return lcCallNoTop;
	}
	public void setLcCallNoTop(float lcCallNoTop)
	{
		this.lcCallNoTop = lcCallNoTop;
	}
	public float getDeweyCallNoBottom()
	{
		return deweyCallNoBottom;
	}
	public void setDeweyCallNoBottom(float deweyCallNoBottom)
	{
		this.deweyCallNoBottom = deweyCallNoBottom;
	}
	public float getDeweyCallNoTop()
	{
		return deweyCallNoTop;
	}
	public void setDeweyCallNoTop(float deweyCallNoTop)
	{
		this.deweyCallNoTop = deweyCallNoTop;
	}

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append(category + " // " + division + " :: " + deweyCallNoBottom + "-" + deweyCallNoTop + "," + population);
		return b.toString();
	}

	
	
	
}
