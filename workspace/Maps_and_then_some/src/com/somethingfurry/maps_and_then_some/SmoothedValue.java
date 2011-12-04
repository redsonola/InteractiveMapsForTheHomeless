package com.somethingfurry.maps_and_then_some;

public class SmoothedValue {
	private float[] smoothingArray; 
	private int smoothValue = 5;
	private int index = 0; 
	private int _val =0 ;
	private boolean nunChukMode = true;
	
	private int val = 0;
	
	SmoothedValue(int len)
	{
		smoothValue = len;
		smoothingArray = new float[smoothValue];
		nunChukMode = true; 
	}
	
	SmoothedValue()
	{
		this(5); 
	}

	void setNunMode(boolean mode){ nunChukMode = mode; }
	
	void update(float num)
	{
		if (!nunChukMode)
		{
			float sum = 0;

			smoothingArray[index] = num;
			index++; 
			if (index >= smoothingArray.length){ index = 0; }

				
			for(int i=0; i<smoothingArray.length; i++)
			{
				sum += smoothingArray[i];
			}
			val = (int) ( sum / smoothingArray.length); 	
		}
		else _val =(int) num; 
		
	}
	
	public int value(){ if (!nunChukMode) return val; else return _val;}

}
