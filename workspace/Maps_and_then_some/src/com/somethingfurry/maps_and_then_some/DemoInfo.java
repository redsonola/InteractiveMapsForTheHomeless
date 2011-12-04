package com.somethingfurry.maps_and_then_some;

import java.util.ArrayList;
import java.util.HashMap;

public class DemoInfo {
	
	public enum DemographicType { BATHROOMS, EAT, ELECTRICITY, SLEEP, HANGOUT, 
	AGE, TIME_ON_STREET, PERCENT_MALE, PERCENT_FEMALE,	PREGNANT, RELATIONSHIP,	PLACE_OF_ORIGIN,
	SUICIDE_ATTEMPT, SIBLINGS };
	
	public String[] _displayStrings = {"Bathroom Use: ", "Eating Use: ", "Electricity Use: ", "Sleep Use: ",
			"Hangout Use: ", "Age Use: ", "Time on Street: ", "Male: ", "Female: ",	"Pregnant: ", "Relationship Status: ", "Places of Origin:",	
			"Suicide Attempts (by users): ", "Number of Siblings: "};
	
	HashMap<DemographicType, String> _demoz; 	

	DemoInfo()
	{
		_demoz = new HashMap<DemographicType, String>();
	}
	
	public void add(DemographicType _type, String val )
	{
		_demoz.put(_type, val);
	}
	
	public String toString()
	{
		String str = new String(""); 
	    for (DemographicType i : DemographicType.values())
		{
	    	if( _demoz.containsKey(i) )
	    	{
	    		String val = _demoz.get(i);
	    		if (val != null) str += _displayStrings[i.ordinal()] + " " + val + "\n";
	    	}
		}
		return str;
	}
	
	public int size()
	{
		return _demoz.size(); 
	}
	
}
