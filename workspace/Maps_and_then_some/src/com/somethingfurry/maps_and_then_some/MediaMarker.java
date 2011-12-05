package com.somethingfurry.maps_and_then_some;

import com.somethingfurry.maps_and_then_some.LocationTypeDisplay.MediaType;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

//a marker for something that will actually display media, so it has like, an icon and stuff.
//also it will display only when zoomed in
public class MediaMarker extends LocationMapMarker {
	 
	protected int _width = 40; 
	protected int _height = 40;
	protected boolean _over = false; 
	protected boolean _pressed = false;
	protected boolean _overWii = false; 
	protected boolean _pressedWii = false; 	
	protected boolean _presentMedia = false; 
	protected boolean _suppressIcon = false;
	protected LocationTypeDisplay _locationType;  //TODO: write locations 

	MediaMarker(Location location, PApplet display, Map map, LocationType locationType, LocationTypeDisplay.MediaType mType)
	{
		super(location, display, map, LocationMapMarker.ZOOM_DISPLAY_THRESH );	
		_locationType = new LocationTypeDisplay(locationType, _display, mType);
	}
	
	protected boolean zoomThres()
	{
		return ( _zoomDisplay <= _map.getZoomLevel());
	}
	
	//event class for when closing media
	public void onCloseMedia()
	{
	
	}
	
	//event class for when closing media
	public void onStartMedia()
	{

	}	
	
	void suppressIcon()
	{
		_suppressIcon = true; 	//hack hack hack hack ahem.
	}
	
	public void drawIcon()
	{
		if ( zoomThres() & !_suppressIcon)
		{
		  _display.fill(215, 0, 0, 100);
		  
		  float xy[] = _map.getScreenPositionFromLocation(_location);
		  _display.image(_locationType.icon(), xy[0], xy[1], _width, _height);
		}		
	}
	
	public void draw()
	{
		update();
		if (_presentMedia) presentMedia();
	}
	
	
	protected int[] center(int w, int h)
	{
		int[] xy = new int[2];
		xy[0] =  (int) ( (float) ((float) _display.width) / 2.0  ) - ( (int) ((float) ((float)w)/2.0) );
		xy[1] =  (int) ( (float) ((float) _display.height) / 2.0  ) - ( (int) ((float) ((float)h)/2.0) );		
		return xy; 
	}
	
	//adapted from processing examples, Image Button 
	
	protected void overRect() {
		  float xy[] = _map.getScreenPositionFromLocation(_location);
		  _over = (_display.mouseX >= xy[0] && _display.mouseX <= xy[0]+_width && 
				  _display.mouseY >= xy[1] && _display.mouseY <= xy[1]+_height) ;
		}
	
	protected void overRectWii() {
		  float xy[] = _map.getScreenPositionFromLocation(_location);
		  int[] m_xy = ((HomelessMapProject)_display).curXY();
		  _overWii = (m_xy[0] > xy[0] && m_xy[0] <= xy[0]+_width && 
				  m_xy[1] >= xy[1] && m_xy[1] <= xy[1]+_height) ;
		}	
	
	//events for wii
	public void pressOn()
	{
		if ( ((HomelessMapProject) _display).usingWii() )
		{
			if (!_presentMedia) onStartMedia();
			_presentMedia = _overWii || _presentMedia;	
		}
		else
		{
			if (_over)
			{
				if (!_presentMedia) onStartMedia();
				_presentMedia = _over || _presentMedia;				
			}
		}
	}

	public void pressOff()
	{
		if (_presentMedia) onCloseMedia();
		_presentMedia = false;		
	}	
	
	 //TODO have a wii-pressed flag in display... COMING!!
	//TODO: not press in a particular spot to go back... 
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
		overRectWii();
	    //pressed(); 		
	  }
	 
	
}
