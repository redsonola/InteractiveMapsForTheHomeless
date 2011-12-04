package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import processing.core.PImage;

public class LocationTypeDisplay {
	
	public enum MediaType {VIDEO, PHOTO, AUDIO, DEMOGRAPHIC };  
	protected static final String LOCATIONS_ICONS[] = { "bathroom" , "eat", "electricity_2", "sleep", "hangout", "story", "demographic"};	
	protected static final String MEDIA_COLOR[] = { "_blue.png", "_green.png", "_red.png", ".png" };
	protected static final String LOCATIONS_TEXT[] = { "Bathroom", "Eat","Electricity","Sleep", "Hangout", "Story", "Demographics"};		
	
	protected LocationMapMarker.LocationType _locationType;
	protected MediaType _mediaType;	
	protected String _text;
	protected String _iconFile;
	protected PImage _icon;
	protected PApplet _display;
	
	public LocationTypeDisplay(LocationMapMarker.LocationType locationType, PApplet display, MediaType mType)
	{
		_locationType = locationType;
		_text = LOCATIONS_TEXT[_locationType.ordinal()];
		_display = display; 
		_mediaType = mType;
		_iconFile =LOCATIONS_ICONS[_locationType.ordinal()] + MEDIA_COLOR[_mediaType.ordinal()]; 
		
		
		_icon = _display.loadImage(_iconFile, "png");		
	}
	
	public PImage icon() { return _icon; }
	public LocationMapMarker.LocationType getType() { return _locationType; }
	public String text() { return _text; }
	
}
