package com.somethingfurry.maps_and_then_some;


import processing.core.PApplet;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.Map;

public class ZoomedOutMapLocationMarker extends LocationMapMarker{
		
		ZoomedOutMapLocationMarker(Location location, PApplet display, Map map)
		{
			super(location, display, map, LocationMapMarker.ZOOM_DISPLAY_THRESH);	
		}
		
		//show mediaType
		public void presentMedia()
		{
			
			
		}	

	}
