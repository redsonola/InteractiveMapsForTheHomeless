package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import processing.core.PImage;

public class LocationTypeDisplay {

	
//	public enum LocationType { BATHROOMS, EAT, ELECTRICITY, SLEEP, HANGOUT, STORY, DEMOGRAPHICS };  
	
	protected static final String LOCATIONS_ICONS[] = { "bathroom.png", "restaurant_food.png", "electricity.png", "sleep.png","hangout.png", "video_icon.png", "video_icon.png"};		
	protected static final String LOCATIONS_TEXT[] = { "Bathroom", "Eat","Electricity","Sleep", "Hangout", "Story", "Demographics"};		
	
	protected LocationMapMarker.LocationType _locationType;
	protected String _text;
	protected String _iconFile;
	protected PImage _icon;
	protected PApplet _display;
	
	public LocationTypeDisplay(LocationMapMarker.LocationType locationType, PApplet display)
	{
		_locationType = locationType;
		_text = LOCATIONS_TEXT[_locationType.ordinal()];
		_iconFile =LOCATIONS_ICONS[_locationType.ordinal()]; 
		_display = display; 
		
		_icon = _display.loadImage(_iconFile, "png");		
	}
	
	public PImage icon() { return _icon; }
	public LocationMapMarker.LocationType getType() { return _locationType; }
	public String text() { return _text; }
	
}
