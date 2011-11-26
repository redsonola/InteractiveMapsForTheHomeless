package com.somethingfurry.maps_and_then_some;

import processing.core.*;

import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;


//import these libraries for the wii functionality... using OSCulator, etc.
import netP5.*;
import oscP5.*; 

 public class HomelessMapProject extends PApplet {

	 Map map;
	 boolean isZoomed = false; 
	 private int zoom = 10;
	 Location phoenix = new Location(33.43f, -112.02f);
	 Location curLocation = phoenix; 
	 OscP5 oscP5;
	 public static final int OSC_PORT = 9000; //max port coz I'm lazy

	 public static final String ADDR_WIIPLUS = "/wii/1/button/Plus";
	 public static final String ADDR_WIIMINUS = "/wii/1/button/Minus";
	 public static final String ADDR_WIILEFT = "/wii/1/button/Left";
	 public static final String ADDR_WIIRIGHT = "/wii/1/button/Right";
	 public static final String ADDR_WIIACCEL = "/wii/1/accel/pry";
	 public static final String ADDR_WIIIR = "/wii/1/ir";
	 
	 private SmoothedValue posX;
	 private SmoothedValue posY;
	 	 
	 
	 
	 
	 public void setup(){
		 size(600, 500, GLConstants.GLGRAPHICS);
		 smooth();  
		 
		 map = new de.fhpotsdam.unfolding.Map(this, "map", 0, 0, 600, 500, true, false, new Microsoft.AerialProvider());
				  	MapUtils.createDefaultEventDispatcher(this, map);
		 /*
		 map = new de.fhpotsdam.unfolding.Map(this, "map", 0, 0, 600, 500, true, false, 
				  	new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 30635));
		*/
				  	
		  //zoom and pan to Phoenix
		  map.zoomAndPanTo(phoenix, zoom);
		  OscP5 oscP5 = new OscP5(this,OSC_PORT);
		  
		  
		  // handle the simple messages first. 
		  oscP5.plug(this,"zoomIn", ADDR_WIIPLUS);
		  oscP5.plug(this, "zoomOut", ADDR_WIIMINUS);
		  posX = new SmoothedValue();
		  posY = new SmoothedValue();

	 }
	 
	 void oscEvent(OscMessage message) {
		float x;
		float y;
		/*  if( message.checkAddrPattern(ADDR_WIIIR) )
		{
		    x = (float) message.get(0).floatValue();
			y = (float) message.get(1).floatValue();  
			
			posX = (int) (map(x, 0, 1, 0, width) );
			posY = (int) (map(y, 0, 1, 0, height) );
			System.out.println("ir_x: "+  x + "  ir_y:"  + y + "posX: "+  posX + "posY:"  + posY);
		}
		else */
		if( message.checkAddrPattern(ADDR_WIIACCEL) )
		{
		    y = (float) message.get(0).floatValue();
			x = (float) message.get(1).floatValue();  
			
			posX.update( map(x, 0, 1, 0, width) ) ;
			posY.update( map(y, 0, 1, 0, height)) ;
			System.out.println("posX: "+  posX.value() + "posY:"  + posY.value());

		}		
			
	 }
 
	  void zoomIn(int i)
	  {
		  zoom++;
		  map.zoomAndPanTo(curLocation, zoom);  
	  }
	  
	  void zoomOut(int i)
	  {
		  if (zoom > 0) zoom--;
		  map.zoomAndPanTo(curLocation, zoom);	
		  
	  }	 
  
	 public void draw(){
		 map.draw();
		 ellipse(posX.value(), posY.value(), 20, 20);

		 
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
		 }
	 }
	 

 }


