//TODO: Add Volume control... would be nice, riiight? we'll see. 

package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.video.Movie;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import ddf.minim.*;

public class AudioLocationMapMarker extends ImageMapMarker {
	
	private String _filename; 
	private Minim _minim; 
	AudioPlayer _player;
	boolean _playing = false;
	
	//for displaying location
	protected static final int BORDER_SIZE = 25; 
	private int _fontSize = 16; 
	private PFont _myFont; 
	private PImage _img; 
	
	

	AudioLocationMapMarker(Location location, PApplet display, Map map, LocationType locationType, Minim minim, String filename)
	{
		super(location, display, map, locationType, LocationTypeDisplay.MediaType.AUDIO);
		_minim = minim; 	
		_filename = filename; 
		_myFont = _display.createFont("Arial", _fontSize);
		_display.textFont(_myFont);		
		//testExistence();
		_img = _display.loadImage("audio_pic.jpg", "jpg");
	}
	
	//event class for when closing media
	public void onStartMedia()
	{
		_player = _minim.loadFile(_filename, 2048);		
		_player.play();	
	}		
	
	//show mediaType
	//show mediaType -- display picture! 
	public void presentMedia()
	{
		int xy[] = drawBorder();
		_display.image(_img, xy[0]+BORDER_SIZE/2,  xy[1]+BORDER_SIZE/2, _img.width, _img.height);
	}
	
	private int box_len()
	{
		int count = _locationType.text().length(); 
		return (int) count * ( (_fontSize * 5 ));	
	}
	
	
	//stop audio
	public void onCloseMedia()
	{
		_player.close();
	}

	public void testExistence()
	{
		_player = _minim.loadFile(_filename, 2048);				
	}	
	
}
