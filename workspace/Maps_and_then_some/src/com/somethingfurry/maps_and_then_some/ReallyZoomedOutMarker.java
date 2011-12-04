/**
 * 
 */
package com.somethingfurry.maps_and_then_some;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * @author courtney
 *
 */
public class ReallyZoomedOutMarker extends LocationMapMarker {

	/**
	 * @param location
	 * @param display
	 * @param map
	 */
	
	public ReallyZoomedOutMarker(Location location, PApplet display, Map map) {
		super(location, display, map, LocationMapMarker.ZOOM_DISPLAY_THRESH_RLY);
		// TODO Auto-generated constructor stub
	}
	
	//show marker or not?
	protected boolean zoomThres()
	{
		return ( _zoomDisplay < _map.getZoomLevel());
	}
	

}
