package edu.illinois.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import edu.illinois.bibinfo.CallNumber;
import edu.illinois.configuration.Configuration;
import edu.illinois.configuration.ConfigurationJSONImpl;
import edu.illinois.configuration.Parameter;
import edu.illinois.stat.probability.MultinomialDist;
import edu.illinois.transformations.ColumnReducer;

public class LikelihoodRatioTestCallNos
{
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String pathToNullTrainingData = args[0];  // e.g. our file of ALL/sampled UIUC holdings
		String pathToAltTrainingData  = args[1];  // e.g. our file of "rare" holdings
		String pathToObservations     = args[2];  // e.g. the google data
		
		
		float[] bins = {100.0f, 200.0f, 300.0f, 400.0f, 500.0f, 600.0f, 700.0f, 800.0f, 900.0f, 1001.0f};
		
		MultinomialDist dmultNull = new MultinomialDist(0.0f, bins);
		Scanner in = new Scanner(new FileReader(new File(pathToNullTrainingData)));
		while(in.hasNextLine()) {
			String callno = in.nextLine();
			float cnf = Float.parseFloat(callno);
			dmultNull.addToSample(cnf);
		}
		in.close();
		
		MultinomialDist dmultAlt = new MultinomialDist(0.0f, bins);
		in = new Scanner(new FileReader(new File(pathToAltTrainingData)));
		while(in.hasNextLine()) {
			String callno = in.nextLine();
			float cnf = Float.parseFloat(callno);
			dmultAlt.addToSample(cnf);
		}
		in.close();
		
		double logLikelihoodNull = 0.0;
		double logLikelihoodAlt  = 0.0;
		
		in = new Scanner(new FileReader(new File(pathToObservations)));
		while(in.hasNextLine()) {
			String callno = in.nextLine();
			float cnf = Float.parseFloat(callno);
			
			double logProbNull = Math.log(dmultNull.probability(cnf));
			double logProbAlt  = Math.log(dmultAlt.probability(cnf));
			
			logLikelihoodNull += logProbNull;
			logLikelihoodAlt  += logProbAlt;

		}
		in.close();
		
		System.out.println("NULL MODEL log-likelihood: \t" + logLikelihoodNull);
		System.out.println("ALT MODEL log-likelihood: \t" + logLikelihoodAlt);
		
		double testStat = -2.0 * (logLikelihoodNull - logLikelihoodAlt);
		
		System.out.println("TEST STATISTIC: " + testStat);

	}

	

}
