package com.somethingfurry.maps_and_then_some;


import processing.core.PApplet;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.Map;

public class ZoomedOutMapLocationMarker extends LocationMapMarker{
		
		//for inbetween really zoom-out and completely zoomed in
		protected float _zoomDisplayHigher; //the zoom at which this marker is displayed (or not)
	
		ZoomedOutMapLocationMarker(Location location, PApplet display, Map map)
		{
			super(location, display, map, LocationMapMarker.ZOOM_DISPLAY_THRESH);	
			_zoomDisplayHigher = ZOOM_DISPLAY_THRESH_RLY;
		}
		
		//show mediaType
		public void presentMedia()
		{
			
			
		}	
		
		//show marker or not?
		protected boolean zoomThres()
		{
			System.out.println("zoomDisplay in ZoomedMarker is: " + _map.getZoomLevel() + "& we are displaying? "+(_zoomDisplay >= _map.getZoomLevel()));			
			return ( _zoomDisplay > _map.getZoomLevel() &&  _zoomDisplayHigher <= _map.getZoomLevel() );
		}

	}
