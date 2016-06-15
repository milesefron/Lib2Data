package edu.illinois.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Files are assumed to be comparable to the output of Callno2Data.  i.e. one callno per line.
 * @author Miles Efron
 *
 */
public class CompareCallnos
{
	public static void main(String[] args) throws FileNotFoundException
	{
		if(args.length != 2) {
			System.err.println("USAGE: CompareCallnos <file1> <file2>");
			System.exit(1);
		}
		
		// specify the granularity of each model
		float slotsPerSubject = 10.0f;
		float[] subjects = {0.0f, 100.0f, 200.0f, 300.0f, 400.0f, 500.0f, 600.0f, 700.0f, 800.0f, 900.0f};
		float[] slots    = {0f, 10f, 20f, 30f, 40f, 50f, 60f, 70f, 80f, 90f};

		
		
		Map<Float,Integer> m1 = new TreeMap<Float,Integer>();
		Map<Float,Integer> m2 = new TreeMap<Float,Integer>();

		for(int i=0; i < subjects.length; i++) {
			for(int j=0; j < slots.length; j++) {
				float subject = subjects[i];
				float slot    = slots[j];
				float address = subject + slot;
				m1.put(address, 0);
				m2.put(address, 0);
			}
		}
			
		
		
		Scanner in = new Scanner(new FileReader(new File(args[0])));
		in.nextLine();
		while(in.hasNext()) {
			String line = in.nextLine();
			try {
				float callno = Float.parseFloat(line);
				float c1 = CompareCallnos.findAddress(callno, m1);
				//System.out.println(callno + "\t" + c1 + "\n");
				Integer current = m1.get(c1);
				if(current == null)
					current = 0;
				current++;
				m1.put(c1, current);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		in.close();
		
		in = new Scanner(new FileReader(new File(args[1])));
		in.nextLine();
		while(in.hasNext()) {
			String line = in.nextLine();
			try {
				float callno = Float.parseFloat(line);
				float c2 = CompareCallnos.findAddress(callno, m2);
				//System.out.println(callno + "\t" + c1 + "\n");
				Integer current = m2.get(c2);
				if(current == null)
					current = 0;
				current++;
				m2.put(c2, current);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		in.close();
		
		System.out.println("cosine: " + CompareCallnos.cosine(m1, m2));

	}
	
	
	public static float cosine(Map<Float,Integer> m1, Map<Float, Integer> m2) {
		
		Set<Float> vocab = new HashSet<Float>();
		vocab.addAll(m1.keySet());
		vocab.addAll(m2.keySet());
		
		float dotProduct = 0.0f;
		float norm1 = 0.0f;
		float norm2 = 0.0f;
		for(Float key : vocab) {
			dotProduct += m1.get(key) * m2.get(key);
			norm1 += (float)Math.pow(m1.get(key), 2.0);
			norm2 += (float) Math.pow(m2.get(key), 2.0);
			
			System.out.println(key + "\t" + m1.get(key) + "\t" + m2.get(key) + "\t" + dotProduct);
		}
				
		return dotProduct / (float)Math.sqrt(norm1 * norm2);
	}
	
	public static float findAddress(float obs, Map<Float,Integer> model) {
		float minDistance = Float.MAX_VALUE;
		float bestSlot = -1.0f;
		
		for(Float slot : model.keySet()) {
			float distance = Math.abs(obs - slot);
			if(distance < minDistance) {
				minDistance = distance;
				bestSlot = slot;
			}
		}
		return bestSlot;
	}
	


}
