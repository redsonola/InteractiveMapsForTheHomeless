//TODO: Add Volume control... would be nice, riiight? we'll see. 

package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import ddf.minim.*;

public class AudioLocationMapMarker extends MediaMarker {
	
	private String _filename; 
	private Minim _minim; 
	AudioPlayer _player;
	boolean _playing = false;
	
	//for displaying location
	protected static final int BORDER_SIZE = 25; 
	private int _fontSize = 16; 
	private PFont _myFont; 
	
	

	AudioLocationMapMarker(Location location, PApplet display, Map map, LocationType locationType, Minim minim, String filename)
	{
		super(location, display, map, locationType);
		_minim = minim; 	
		_filename = filename; 
		_myFont = _display.createFont("Arial", _fontSize);
		_display.textFont(_myFont);		
	}
	
	//event class for when closing media
	public void onStartMedia()
	{
		_player = _minim.loadFile(_filename, 2048);		
		_player.play();
	}		
	
	//show mediaType
	public void presentMedia()
	{
		//draw something here to show audio is playing for shiz and giggles... TODO
		//would be nice... maybe have a playing image.
		
		int[] xy = new int[2];
		int len =box_len();
		xy = center(len, BORDER_SIZE);
		
		_display.fill(0, 0, 0, 255);
		_display.rect(xy[0], xy[1], len, BORDER_SIZE);
		_display.fill(255, 255, 255, 255);
		_display.text(_locationType.text(), xy[0]+25, xy[1]+18);
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

}
