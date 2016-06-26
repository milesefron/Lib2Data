package edu.illinois.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import javafx.scene.SubScene;
import edu.illinois.configuration.Configuration;
import edu.illinois.configuration.ConfigurationJSONImpl;
import edu.illinois.configuration.Parameter;
import edu.illinois.transformations.ColumnReducer;

public class SubjectCompareModels
{

	public static final int COLUMN_OF_SUBJECT_COUNTS = 0;
	public static final int COLUMN_OF_SUBJECT_NAMES  = 1;

	public static final Pattern FIELD_SEP = Pattern.compile("\t+");
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String pathToSubjectInfoData = args[0];	// our "observed" data.  e.g. subjects from google
		String pathToSubjectInfoAlt  = args[1]; // our "alternative" data.  
		String pathToSubjectInfoNull = args[2];	// our "null" data.  
		
		File subjectFileData = new File(pathToSubjectInfoData);
		File subjectFileAlt  = new File(pathToSubjectInfoAlt);
		File subjectFileNull = new File(pathToSubjectInfoNull);
		

		Set<String> vocab = new HashSet<String>();
		Map<String,Float> histData = new HashMap<String,Float>();
		Map<String,Float> histNull = new HashMap<String,Float>();
		Map<String,Float> histAlt  = new HashMap<String,Float>();

		Scanner inData = new Scanner(new FileReader(subjectFileData));
		Scanner inAlt  = new Scanner(new FileReader(subjectFileAlt));
		Scanner inNull = new Scanner(new FileReader(subjectFileNull));

		System.err.println("reading " + subjectFileData);
		while(inData.hasNextLine()) {
			String rawLine = inData.nextLine();
			if(rawLine.startsWith("#"))
				continue;
			String[] rawFields = FIELD_SEP.split(rawLine);
			String subjectString = rawFields[COLUMN_OF_SUBJECT_NAMES];
			Float subjectCount   = Float.parseFloat(rawFields[COLUMN_OF_SUBJECT_COUNTS]);
			vocab.add(subjectString);
			histData.put(subjectString, subjectCount);
		}
		inData.close();
		System.err.println("reading " + subjectFileAlt);
		while(inAlt.hasNextLine()) {
			String rawLine = inAlt.nextLine();
			if(rawLine.startsWith("#"))
				continue;
			String[] rawFields = FIELD_SEP.split(rawLine);
			String subjectString = rawFields[COLUMN_OF_SUBJECT_NAMES];
			Float subjectCount   = Float.parseFloat(rawFields[COLUMN_OF_SUBJECT_COUNTS]);
			vocab.add(subjectString);
			histAlt.put(subjectString, subjectCount);
		}
		inAlt.close();
		System.err.println("reading " + subjectFileNull);
		while(inNull.hasNextLine()) {
			String rawLine = inNull.nextLine();
			if(rawLine.startsWith("#"))
				continue;
			String[] rawFields = FIELD_SEP.split(rawLine);
			String subjectString = rawFields[COLUMN_OF_SUBJECT_NAMES];
			Float subjectCount   = Float.parseFloat(rawFields[COLUMN_OF_SUBJECT_COUNTS]);
			vocab.add(subjectString);
			histNull.put(subjectString, subjectCount);
		}
		inNull.close();
		
		System.err.println("smoothing histograms.");
		histNull = SubjectCompareModels.smoothHistogram(histNull, vocab);
		histAlt  = SubjectCompareModels.smoothHistogram(histAlt,  vocab);

		histNull = SubjectCompareModels.normalize(histNull);
		histAlt  = SubjectCompareModels.normalize(histAlt);
		
		float llNull = 0.0f;
		float llAlt  = 0.0f;
		
		for(String term : histData.keySet()) {
			float dataObs = histData.get(term);
			llNull += dataObs * Math.log(histNull.get(term));
			llAlt  += dataObs * Math.log(histAlt.get(term));
		}
		
		System.out.println(subjectFileAlt  + ": \t log-likelihood = " + llNull);
		System.out.println(subjectFileNull + ": \t\t log-likelihood = " + llAlt);
 
	}


	public static Map<String,Float> smoothHistogram(Map<String,Float> hist, Set<String> vocab) { 
		Map<String,Float> smoothed = new HashMap<String,Float>(vocab.size());
		Float smoother = 1.0f;
		for(String term : vocab) {
			Float obsCount = hist.get(term);
			if(obsCount == null)
				obsCount = 0.0f;
			obsCount += smoother;
			smoothed.put(term, obsCount);
		}
		return smoothed;
	}

	public static Map<String,Float> normalize(Map<String,Float> hist) { 
		Map<String,Float> normalized = new HashMap<String,Float>(hist.keySet().size());
		Float n = 0.0f;;
		for(String term : hist.keySet()) {
			n += hist.get(term);
		}
		for(String term : hist.keySet()) {
			Float histObs = hist.get(term);
			histObs /= n;
			normalized.put(term, histObs);
		}
		return normalized;
	}
	
}
