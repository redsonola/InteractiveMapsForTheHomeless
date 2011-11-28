package com.somethingfurry.maps_and_then_some;

import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;

import processing.core.*;


//master class for markers with media, etc.
public class LocationMapMarker {
	
	public enum MediaType { PHOTO, VIDEO, AUDIO, STORY, ZOOMED_OUT, DEMOGRAPHIC };
	protected MediaType _mediaType;  //what kind of media type
	protected Location _location; //where is located on the map
	protected PApplet _display; //the applet where we draw shiz
	protected Map _map; //the map where we display shiz
	protected float _zoomDisplay; //the zoom at which this marker is displayed (or not)
	
	protected static final float ZOOM_DISPLAY_THRESH = (float) 5.0; 
	
	LocationMapMarker(Location location, PApplet display, Map map, float zoomDisplay)
	{
		_location = location;
		_display = display; 
		_map = map;
		_zoomDisplay = zoomDisplay;
	}
	
	//show marker or not?
	private boolean zoomThres()
	{
		return ( _zoomDisplay < _map.getZoomLevel());
	}
	
	//display the marker
	public void draw()
	{
		if ( zoomThres() )
		{
		  _display.fill(215, 0, 0, 100);
		  
		  float xy[] = _map.getScreenPositionFromLocation(_location);
		  _display.ellipse(xy[0], xy[1], 20, 20);
		}
	}
	
	//show/present mediaType
	public void presentMedia()
	{
				
	}
	
}


