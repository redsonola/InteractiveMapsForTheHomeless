package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.Map;
import processing.video.*;



public class VideoLocationMapMarker extends ImageMapMarker {
	
	Movie _movie;
	String _filename;
	
	VideoLocationMapMarker(Location location, PApplet display, Map map, LocationType locationType, String filename)
	{
		super(location, display, map, locationType, LocationTypeDisplay.MediaType.VIDEO, 620, 360);	
		_filename = filename; 
		//testExistence();
	}
	
	VideoLocationMapMarker(Location location, PApplet display, Map map,  LocationType locationType, String filename, int w, int h)
	{
		super(location, display, map, locationType, LocationTypeDisplay.MediaType.VIDEO, w, h);
		_filename = filename; 	
		//testExistence();
	}	
	
	public void testExistence()
	{
		_movie = new Movie(_display, _filename);; // good place to load??? we'll see...	

		if (_movie == null)
		{
			System.out.println("FUCKING BITCH DOES NOT EXIST FIND IT FIND IT!!!!!");
		}
		
		_movie.delete(); 
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
			_movie.dispose();
		}
	}
	
	public void movieEvent(Movie movie) {
		if(movie != null)
		  movie.read();
	}

	
	
}