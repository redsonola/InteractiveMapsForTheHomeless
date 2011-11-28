package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.Map;
import processing.video.*;



public class VideoLocationMapMarker extends ImageMapMarker {
	
	Movie _movie;
	String _filename;
	
	VideoLocationMapMarker(Location location, PApplet display, Map map, String locationName, String filename)
	{
		super(location, display, map, VIDEO_ICON, locationName);	
		_filename = filename; 
	}
	
	VideoLocationMapMarker(Location location, PApplet display, Map map,  String locationName, String filename, int w, int h)
	{
		super(location, display, map, VIDEO_ICON, locationName, w, h);	
	}	
	
	//show mediaType
	public void presentMedia()
	{
		//load and create image
		if (_defaults)
		{
			_width = _movie.width; 
			_height = _movie.height;
		}		
		int xy[] = drawBorder();	
		_display.image(_movie, xy[0]-BORDER_SIZE,  xy[1]-BORDER_SIZE, _width, _height);
	}
		
	//event class for when closing media
	public void onStartMedia()
	{	
		_movie = new Movie(_display, _filename);
	}	
	
	//stop audio
	public void onCloseMedia()
	{
		_movie.dispose();
	}	
	
}