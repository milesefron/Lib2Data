package edu.illinois.stat.probability;

import java.util.Arrays;
import java.util.List;

public class MultinomialDist
{
	private float bottom;			// this gives the lowest value in our vocabulary (usually 0 for this study).
	private float[] bins;			// enumeration of the CEILINGS of each bin in our vocabulary
									// each float names the TOP (largest) value for each bin
	
	private int n = 0;				// total number of training observations we've put into bins
	private float[] counts;			// vector of counts parameterizing this distribution
	private int pseudoCounts = 1;	// number of pseudocounts to be put into each bin
	
	public MultinomialDist(float bottom, float[] bins) {
		this.bottom = bottom;
		this.bins = bins;
		
		Arrays.sort(bins);

		counts = new float[bins.length];
		//for(int i=0; i<counts.length; i++) 
		//	counts[i] += pseudoCounts;
		
		//n += bins.length * pseudoCounts; 

	}
	
	public MultinomialDist(float[] bins) {
		bottom = 0.0f;
		this.bins = bins;
		
		Arrays.sort(bins);
		
		counts = new float[bins.length];
		//for(int i=0; i<counts.length; i++) 
		//	counts[i] += pseudoCounts;
		//n += bins.length * pseudoCounts; 
	}
	
	
	public void addToSample(float x) {
		int binIndex = findBin(x);
		if(binIndex < 0) {
			System.err.println("value " + x + " out of range.  skipping.");
			//System.err.println(bottom + " ... "+  Arrays.toString(bins));
			//System.exit(-1);
			return;
		}
		
		int myBin = findBin(x);
		counts[myBin]++;
		n++;
	}
	
	public float probability(float x) {
		int i = findBin(x);
		float probability = counts[i] / n;
		return probability;
	}
	
	public float smoothedProbability(float x) {
		int i = findBin(x);
		float probability = (pseudoCounts + counts[i]) / (n + counts.length * pseudoCounts);
		return probability;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("N: " + n + "\n");
		for(int i=0; i<bins.length; i++) {
			b.append(bins[i] + " \t " + counts[i] + "\n");
		}
		return b.toString();
	}
	
	private int findBin(float x) {
		if(x > bottom && x <= bins[0])
			return 0;
		
		for(int i=1; i<bins.length; i++) {
			if(x > bins[i-1] && x <= bins[i])
				return i;
		}
		return -1;
	}
	
	
	public static void main(String[] args)
	{
		float[] bins = {100.0f, 200.0f, 300.0f, 400.0f, 500.0f, 600.0f, 700.0f, 800.0f, 900.0f, 1000.0f};
		MultinomialDist dmult = new MultinomialDist(0, bins);
		dmult.addToSample(1.0f);
		dmult.addToSample(99.0f);
		dmult.addToSample(100.0f);
		dmult.addToSample(100.0001f);
		//dmult.addToSample(1000.0001f);

		System.out.println(dmult);
		
		System.out.println("MLEs");
		System.out.println("P(60.0) =\t " + dmult.probability(50.0f));
		System.out.println("P(200.0) =\t " + dmult.probability(200.0f));
		System.out.println("P(801) =\t " + dmult.probability(801.0f));
		
		
		System.out.println();
		System.out.println("smoothed");
		System.out.println("~P(50.0) =\t " + dmult.smoothedProbability(50.0f));
		System.out.println("~P(200.0) =\t " + dmult.smoothedProbability(200.0f));
		System.out.println("~P(801) =\t " + dmult.smoothedProbability(801.0f));
		
	}
}
