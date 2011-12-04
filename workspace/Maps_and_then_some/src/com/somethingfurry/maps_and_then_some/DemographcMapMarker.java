package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

public class DemographcMapMarker extends ImageMapMarker {
	
	DemoInfo _demo; 
	private static final int _w = 300; 
	private static final int _h = 300; 
	

	DemographcMapMarker(Location location, PApplet display, Map map, DemoInfo demo) {
		super(location, display, map, LocationType.DEMOGRAPHICS, LocationTypeDisplay.MediaType.DEMOGRAPHIC, _w, _h);
		// TODO Auto-generated constructor stub
		_demo = demo; 
	}

	//show mediaType -- display picture! 
	public void presentMedia()
	{
		int xy[] = drawBorder();
		_display.text(_demo.toString(), xy[0]+BORDER_SIZE/2,  xy[1]+BORDER_SIZE);
	}
	
}
