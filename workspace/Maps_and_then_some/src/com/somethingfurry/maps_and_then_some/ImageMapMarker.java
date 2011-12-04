package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

public class ImageMapMarker extends MediaMarker {
	
	protected int _width;
	protected int _height;
	protected boolean _defaults = false;	
	PFont _myFont;
	
	protected static final int BORDER_SIZE = 50;
	

	ImageMapMarker(Location location, PApplet display, Map map, LocationType locationType,  LocationTypeDisplay.MediaType mType, int w, int h)
	{
		super(location, display, map, locationType, mType);	
		resize(w, h);
		_myFont = _display.createFont("Arial", 16);
		_display.textFont(_myFont);
	}
	
	ImageMapMarker(Location location, PApplet display, Map map, LocationType locationType,  LocationTypeDisplay.MediaType mType)
	{
		this(location, display, map, locationType, mType, 0, 0);	
		_defaults = true; 
	}	
	
	protected int[] drawBorder()
	{
		int[] xy = new int[2];
		xy = center(_width+BORDER_SIZE, _height+BORDER_SIZE);
		
		_display.fill(0, 0, 0, 255);
		_display.rect(xy[0], xy[1], _width+BORDER_SIZE, _height+BORDER_SIZE);
		_display.fill(255, 255, 255, 255);
		_display.text(_locationType.text(), xy[0]+25, xy[1]+21);
		return xy; 
	}
	
	public void resize(int w, int h)
	{
		_defaults = false;
		_width = w;
		_height = h; 
	}
	
	

	
}
