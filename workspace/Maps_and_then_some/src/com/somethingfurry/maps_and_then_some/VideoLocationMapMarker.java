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
		_filename = filename; 	
	}	
	
	//show mediaType
	public void presentMedia()
	{
		if (_movie != null)
		{
			//load and create image
			if (_defaults)
			{
				_width = _movie.width; 
				_height = _movie.height;			
			}		
			int xy[] = drawBorder();	
			_display.image(_movie, xy[0]+BORDER_SIZE/2, xy[1]+BORDER_SIZE/2, _width, _height);
		}
	}
		
	//event class for when closing media
	public void onStartMedia()
	{	
		System.out.println("Loading video...");
		_movie = new Movie(_display, _filename);
		System.out.println("Done loading video...");		
		_movie.play();
	}	
	
	//stop audio
	public void onCloseMedia()
	{
		if (_movie != null)
		{
			_movie.stop();
			_movie.delete();
		}
	}
	
	public void movieEvent(Movie movie) {
		if(movie != null)
		  movie.read();
	}

	
	
}