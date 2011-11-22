package com.somethingfurry.maps_and_then_some;

import processing.core.*;

import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;



 public class HomelessMapProject extends PApplet {

	 Map map;
	 boolean isZoomed = false; 
	 Location phoenix = new Location(33.43f, -112.02f);

	 public void setup(){
		 size(600, 500, GLConstants.GLGRAPHICS);
		 smooth();
		 map = new de.fhpotsdam.unfolding.Map(this, "map", 0, 0, 600, 500, true, false, 
				  	new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 30635));
				  	MapUtils.createDefaultEventDispatcher(this, map);
				  	
		 
		  //zoom and pan to Phoenix
		  Double p_lat = 33.0;// + 26.0/60.0;
		  Double p_long = 112.0;// + 4.0/60.0;
		  map.zoomAndPanTo(phoenix, 10);

	 }
  
	 public void draw(){
		 map.draw();
		 
	 }
	 
	 public void mouseClicked()
	 {
		 if (!isZoomed)
		 {
			 // Shows geo-location at mouse position
			 Location location = map.getLocationFromScreenPosition(mouseX, mouseY);	 
			 map.zoomAndPanTo(location, 13);
			 isZoomed = true; 
		 }
		 else
		 {
			 map.zoomAndPanTo(phoenix, 10); 
			 isZoomed = false;
		 }
	 }
	 
	 


 }


