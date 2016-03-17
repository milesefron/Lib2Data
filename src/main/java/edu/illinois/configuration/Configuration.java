package edu.illinois.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract class that simply holds and provides access to a List of Parameter objects.
 * 
 * @author Miles Efron
 *
 */
public abstract class Configuration
{
	protected List<Parameter> parameters;
	protected Map<String,Parameter> paramMap;
	
	public Configuration()
	{
		parameters = new ArrayList<Parameter>();
		paramMap = new HashMap<String,Parameter>();
	}
	
	public void addParameter(Parameter param)
	{
		parameters.add(param);
		paramMap.put(param.getName(), param);
	}
	
	/**
	 * Returns our List of Parameter objects in whatever state we might find it.  i.e. Returned List could be empty
	 * if we haven't added any Parameters to it yet.
	 * 
	 * @return	Our current List of Parameter objects
	 */
	public List<Parameter> getParams()
	{
		return parameters;
	}
	
	/**
	 * Returns our Map of Parameter objects in whatever state we might find it.  i.e. Returned List could be empty
	 * if we haven't added any Parameters to it yet. N.B. The Map resolves paramName -> ParamObject...a bit redundant.
	 * 
	 * @return	Our current List of Parameter objects
	 */
	public Map<String,Parameter> getParamMap()
	{
		return paramMap;
	}
	
	/**
	 * Subclasses will provide concrete methods for reading parameters serialized to disk.
	 * 
	 * @param pathToParams	Resolvable path to serialized parameters.
	 */
	public  abstract void readParams(String pathToParams);
}
