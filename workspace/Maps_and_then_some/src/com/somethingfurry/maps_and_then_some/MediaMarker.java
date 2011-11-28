package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

//a marker for something that will actually display media, so it has like, an icon and stuff.
//also it will display only when zoomed in
public class MediaMarker extends LocationMapMarker {
	
	protected String _iconFile; //must be png for now I don't wanna bother with parsing this shit now
	protected PImage _icon; 
	protected int _width = 20; 
	protected int _height = 20;
	protected boolean _over = false; 
	protected boolean _pressed = false;
	protected boolean _presentMedia = false; 
	
	//icons
	protected static final String VIDEO_ICON = "video_icon.png";
	protected static final String AUDIO_ICON = "video_icon.png";
	protected static final String STORY_ICON = "video_icon.png";
	protected static final String PHOTO_ICON = "video_icon.png";
	protected static final String DEMOGRAPHIC_ICON = "video_icon.png";

	
	

	MediaMarker(Location location, PApplet display, Map map, String iconFile)
	{
		super(location, display, map, LocationMapMarker.ZOOM_DISPLAY_THRESH);	
		_iconFile = iconFile; 
		_icon = _display.loadImage(_iconFile, "png");
	}
	
	private boolean zoomThres()
	{
		//System.out.println("zoomDisplay in MediaMarker is: " + _map.getZoomLevel());
		return ( _zoomDisplay >= _map.getZoomLevel());
	}
	
	//event class for when closing media
	public void onCloseMedia()
	{
	
	}
	
	//event class for when closing media
	public void onStartMedia()
	{
	
	}	
	
	public void draw()
	{
		update();
		if ( zoomThres() )
		{
		  _display.fill(215, 0, 0, 100);
		  
		  float xy[] = _map.getScreenPositionFromLocation(_location);
		  _display.image(_icon, xy[0], xy[1], _width, _height);
		}
		if (_presentMedia) presentMedia();
	}
	
	
	protected int[] center(int w, int h)
	{
		int[] xy = new int[2];
		xy[0] =  (int) ( (float) ((float) _display.width) / 2.0  ) - ( (int) ((float) ((float)w)/2.0) );
		return xy; 
	}
	
	//adapted from processing examples, Image Button 
	
	protected void overRect() {
		  float xy[] = _map.getScreenPositionFromLocation(_location);
		  _over = (_display.mouseX >= xy[0] && _display.mouseX <= xy[0]+_width && 
				  _display.mouseY >= xy[1] && _display.mouseY <= xy[1]+_height) ;
		}	
	
	 //TODO have a wii-pressed flag in display... COMING!!
	  void pressed() {
		    if(_over && _display.mousePressed) {
		    
		    //since the media just closed run the stop script	
		     if (_presentMedia )
		     {
		    	 onCloseMedia();
		     } else { onStartMedia(); } //since the media just started!
		     
			  _presentMedia = !_presentMedia; //flip!!
		     // _pressed = true;
		    } 
		    //else {
		     // _pressed = false;
		    //}    
		  }
	  
	  void update() 
	  {
		overRect();
	    pressed(); 
	  }
	 
	
}
