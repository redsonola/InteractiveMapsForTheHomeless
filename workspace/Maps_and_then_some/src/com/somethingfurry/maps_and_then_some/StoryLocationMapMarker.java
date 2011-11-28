package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import ddf.minim.Minim;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.Map;

//thisissohacked.

public class StoryLocationMapMarker extends MediaMarker {
	
	MediaMarker _marker; 
	MediaType _mediaType;
	
	
	StoryLocationMapMarker(Location location, PApplet display, Map map, String locationName, Minim minim, String filename)
	{
		super(location, display, map, STORY_ICON, locationName);	
		_mediaType = MediaType.AUDIO; 
		_marker = new AudioLocationMapMarker(location, display, map, locationName, minim, filename);	
		_marker.suppressIcon();

	}
	StoryLocationMapMarker(Location location, PApplet display, Map map, String locationName, String filename, int w, int h)
	{
		super(location, display, map, STORY_ICON, locationName);	
		_mediaType = MediaType.VIDEO; 		
		_marker = new VideoLocationMapMarker(location, display, map, locationName, filename, w, h);	
		_marker.suppressIcon();
	}
	
}