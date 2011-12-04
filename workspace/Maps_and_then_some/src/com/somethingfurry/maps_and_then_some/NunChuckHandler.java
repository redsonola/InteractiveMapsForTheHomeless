package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;

public class NunChuckHandler {
	
	private boolean _nunChuckHome = true;
	
	private float _px = 0;
	private float _py = 0;
	
	private float _dx = 0;
	private float _dy = 0;	
	
	private HomelessMapProject _display;
	private float homeXY[] = { (float)0.48431373, (float)0.5 };  //check home, probably calibrate, etc.
	private final static float THRESH = (float) 0.01;
	private final static float MAX_STEP = (float) 5;

	
	NunChuckHandler(HomelessMapProject display)
	{
		_display = display; 
		_px = homeXY[0];
		_py = homeXY[1];
	}
	
	public void handleNunchuck(float x, float y)
	{
		boolean prevHome = _nunChuckHome; 
		_nunChuckHome = ( ( Math.abs(x - homeXY[0] ) <= THRESH) && ( Math.abs(y - homeXY[1] ) <= THRESH) );
		
		float newDX = _px - x;
		float newDY = _py - y;	
		
		if (!_nunChuckHome)
		{
		
			if (prevHome  || ( ((newDX * _dx) >= 0) && ((newDY * _dy) >= 0) ) ) 
			{
				_display.posX.update( _display.posX.value() + processing.core.PApplet.map(x, 0, 1, -MAX_STEP, MAX_STEP)  );
				_display.posY.update( _display.posY.value() - processing.core.PApplet.map(y, 0, 1, -MAX_STEP, MAX_STEP)  );
				_display.updateCurPos();
			}
		}
		_dx = newDX; 
		_dy = newDY; 
		
		_px = x;
		_py = y;
	}
	
}
