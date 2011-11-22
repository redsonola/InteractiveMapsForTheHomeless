package com.somethingfurry.maps_and_then_some;

import processing.core.*;

import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;

//import these libraries for the wii
import lll.Loc.*;
import lll.wrj4P5.*; 




 public class HomelessMapProject extends PApplet {

	 Map map;
	 boolean isZoomed = false; 
	 Location phoenix = new Location(33.43f, -112.02f);
	 Wrj4P5 wii; 

	 public void setup(){
		 size(600, 500, GLConstants.GLGRAPHICS);
		 smooth();
		 map = new de.fhpotsdam.unfolding.Map(this, "map", 0, 0, 600, 500, true, false, 
				  	new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 30635));
				  	MapUtils.createDefaultEventDispatcher(this, map);
				  	
		 
		  //zoom and pan to Phoenix
		  map.zoomAndPanTo(phoenix, 10);
		  
		   wii = new Wrj4P5(this); //create wii object

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
	 
	 public void keyPressed()
	 {
		 if (key == ' ')
		 {
			 wii.connect(1); //connect performer 1
		 }
	 }
	 
	 
	  void buttonPressed(RimokonEvent evt, int rid) 
	  { 
	    
	    if (evt.wasPressed(RimokonEvent.TWO)) 
	    {
	    }
	    if (evt.wasPressed(RimokonEvent.ONE)) 
	    {
	    }
	    if (evt.wasPressed(RimokonEvent.B)) 
	    {
	    }
	    if (evt.wasPressed(RimokonEvent.A)) 
	    {
	    }
	    if (evt.wasPressed(RimokonEvent.MINUS)) {
	    }
	    if (evt.wasPressed(RimokonEvent.HOME)) 
	    {
	
	    } 
	    if (evt.wasPressed(RimokonEvent.LEFT)) {
	      }
	    if (evt.wasPressed(RimokonEvent.RIGHT)) {
	    }
	    if (evt.wasPressed(RimokonEvent.DOWN)){

	    }
	    if (evt.wasPressed(RimokonEvent.UP)) 
	    {
	    }
	     if (evt.wasPressed(RimokonEvent.PLUS)) 
	     {
	     }
	    } 


 }


