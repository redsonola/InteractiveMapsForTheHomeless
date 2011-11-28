//TODO: Add Volume control... would be nice, riiight? we'll see. 

package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import ddf.minim.*;

public class AudioLocationMapMarker extends MediaMarker {
	
	private String _filename; 
	private Minim _minim; 
	AudioPlayer _player;
	boolean _playing = false;

	
	AudioLocationMapMarker(Location location, PApplet display, Map map, Minim minim)
	{
		super(location, display, map, AUDIO_ICON);	
		_minim = minim; 
	}	
	
	AudioLocationMapMarker(Location location, PApplet display, Map map, Minim minim, String filename)
	{
		this(location, display, map, minim);
		_filename = filename; 
		_player = _minim.loadFile(_filename, 2048);

	}
	
	//event class for when closing media
	public void onStartMedia()
	{
		_player.play();
	}		
	
	//show mediaType
	public void presentMedia()
	{
		//draw something here to show audio is playing for shiz and giggles... TODO
		//would be nice... maybe have a playing image.
	}
	
	
	//stop audio
	public void onCloseMedia()
	{
		_player.close();
	}

}
