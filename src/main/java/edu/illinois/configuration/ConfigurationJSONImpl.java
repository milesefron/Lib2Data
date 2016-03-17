package edu.illinois.configuration;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class ConfigurationJSONImpl extends Configuration
{

	@Override
	public void readParams(String pathToParams)
	{
		
		Gson gson = new Gson();
		Type collectionType = new TypeToken<List<Parameter>>(){}.getType();
		JsonReader reader;
		try
		{
			reader = new JsonReader(new FileReader(pathToParams));
			parameters = gson.fromJson(reader, collectionType);
			
			paramMap = new HashMap<String,Parameter>();
			for(Parameter param : parameters)
				paramMap.put(param.getName(), param);
			
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}

}
