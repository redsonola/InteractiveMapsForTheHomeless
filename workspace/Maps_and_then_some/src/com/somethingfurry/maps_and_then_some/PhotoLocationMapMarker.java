package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

public class PhotoLocationMapMarker extends ImageMapMarker {
		
	//TODO: TEST!!! Could not test on airplane since internetz for map
	
	private String _photoFileName;
	private String _filetype = "png"; //gif, jpg, etc. as well 
	private PImage _img;
		
	PhotoLocationMapMarker(Location location, PApplet display, Map map, String locationName, String filename, String filetype)
	{
		super(location, display, map, PHOTO_ICON, locationName);	
		setPhoto(filename, filetype);

	}
	
	PhotoLocationMapMarker(Location location, PApplet display, Map map, String locationName, String filename, String filetype, int w, int h)
	{
		super(location, display, map, PHOTO_ICON, locationName, w, h);
		setPhoto(filename, filetype, w, h);
	}	
	
	public void setPhoto(String filename, String filetype, int w, int h)
	{
		_photoFileName = filename; 
		_filetype = filetype; 
		resize(w, h);
	}
	
	//event class for when closing media
	public void onStartMedia()
	{
		_img = _display.loadImage(_photoFileName, _filetype); // good place to load??? we'll see...	
	}	


	//event class for when closing media
	public void onCloseMedia()
	{
		_img.delete();
	}	
	
	
	public void setPhoto(String filename, String filetype)
	{
		setPhoto(filename, filetype, 0, 0); 
	}
	
	
	//show mediaType -- display picture! 
	public void presentMedia()
	{
		//load and create image
		if (_defaults)
		{
			_width = _img.width; 
			_height = _img.height;
		}
		int xy[] = drawBorder();
		_display.image(_img, xy[0]+BORDER_SIZE/2,  xy[1]+BORDER_SIZE/2, _width, _height);
	}
	
}
