package com.somethingfurry.maps_and_then_some;

import java.util.ArrayList;

import processing.core.*;
import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import ddf.minim.*;
import processing.video.*;

//import these libraries for the wii functionality... using OSCulator, etc.
import netP5.*;
import oscP5.*; 

 public class HomelessMapProject extends PApplet {

	 Map map; //the map display and functionality
	 boolean isZoomed = false;  //whether mapped is zoomed or not -- not used, legacy!
	 private int zoom = 10; //the current zoom of the map
	 Location phoenix = new Location(33.43f, -112.02f); //the location of phoenix
	 Location curLocation = phoenix; //the current location of the map... this is latitude and longitude
	 Minim minim; //for audio classes
	 
	 OscP5 oscP5; //object to enable OSC shiz
	 public static final int OSC_PORT = 9000; //max port coz I'm lazy //the port to listen for OSC messages on

	 //OSC messages incoming from wiimote / osculator	
	 public static final String ADDR_WIIPLUS = "/wii/1/button/Plus";
	 public static final String ADDR_WIIMINUS = "/wii/1/button/Minus";
	 public static final String ADDR_WIILEFT = "/wii/1/button/Left";
	 public static final String ADDR_WIIRIGHT = "/wii/1/button/Right";
	 public static final String ADDR_WIIACCEL = "/wii/1/accel/pry";
	 public static final String ADDR_WIIIR = "/wii/1/ir";
	 public static final String ADDR_WIIA = "/wii/1/button/A";	 
	 
	 private SmoothedValue posX; //the current position of the wii - cursor
	 private SmoothedValue posY; //the current position of hte wii - cursor
	 private boolean isPanning = false;  //whether we are in panning mode... if so pan to where cursor is
	 
	 private ArrayList<LocationMapMarker> markers; 
	 
	 //analogous to setup in the processing file.  create shit.  set it up!
	 public void setup(){
		 size(800, 600, GLConstants.GLGRAPHICS);
		 smooth();  
		 
		 
		 
		 //TODO: CHECK IF THERE IS INTERNET>>> IF SO THEN COOL MAP... IF NOT THEN UNCOOLZ MAP!!! SHIZ!!
		 map = new de.fhpotsdam.unfolding.Map(this, "map", 0, 0, 800, 600, true, false, new Microsoft.AerialProvider());
				  	MapUtils.createDefaultEventDispatcher(this, map);
		 /*
		 map = new de.fhpotsdam.unfolding.Map(this, "map", 0, 0, 600, 500, true, false, 
				  	new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 30635));
		*/
				  	
		  //zoom and pan to Phoenix
		  map.zoomAndPanTo(phoenix, zoom);
		  OscP5 oscP5 = new OscP5(this,OSC_PORT);		  
		  
		  // connect event handlers to OSC (re: wii events)  
		  oscP5.plug(this,"zoomIn", ADDR_WIIPLUS);
		  oscP5.plug(this, "zoomOut", ADDR_WIIMINUS);
		  oscP5.plug(this, "panning", ADDR_WIIA);
		  
		  //create the current x, y pos of screen
		  posX = new SmoothedValue();
		  posY = new SmoothedValue();
		  
		  minim = new Minim(this);
		  
		  markers = new ArrayList<LocationMapMarker>();
		  initMarkers();
		  
		  System.out.println("zoom level is: " + map.getZoomLevel());
	 }
	 
	 //void init the markers, that is add in all the data
	 void initMarkers()
	 {
		 LocationMapMarker zMarker = new ZoomedOutMapLocationMarker(phoenix, this, map);
		// LocationMapMarker pMarker = new PhotoLocationMapMarker(phoenix, this, map, "Hangout", "nasal_passages.jpg", "jpg", 512, 384);
	//	 LocationMapMarker aMarker = new AudioLocationMapMarker(phoenix, this, map, "Hangout", minim, "02 - Blue Monday.mp3"); ///TODO: import an mp3 for testing
		 LocationMapMarker vMarker = new VideoLocationMapMarker(phoenix, this, map, "Eat", "courtney_cheezy_shit5 01-21-45.mov"); //TODO: IMPORT A FREAKING VIDEO FILE TO TEST THIS SHIT
		 
		 markers.add(zMarker);
		 //markers.add(pMarker);
		 
		 
		 //TODO FINISH TESTING
	//	 markers.add(aMarker);
		 markers.add(vMarker);
	 }
	 
	 //all purpose OSC event handler for un-plugged OSC events
	 //I handle the acceleration params from the wii here, since I was trying to use IR to control the cursor
	 //but IR SUCKS.
	 void oscEvent(OscMessage message) {
		float x;
		float y;
		
		//the IR codesz
		/*  if( message.checkAddrPattern(ADDR_WIIIR) )
		{
		    x = (float) message.get(0).floatValue();
			y = (float) message.get(1).floatValue();  
			
			posX = (int) (map(x, 0, 1, 0, width) );
			posY = (int) (map(y, 0, 1, 0, height) );
			System.out.println("ir_x: "+  x + "  ir_y:"  + y + "posX: "+  posX + "posY:"  + posY);
		}
		else */
		
		//use the acceleration to change position.
		if( message.checkAddrPattern(ADDR_WIIACCEL) )
		{
		    y = (float) message.get(0).floatValue();
			x = (float) message.get(1).floatValue();  
			
			posX.update( map(x, 0, 1, 0, width) ) ;
			posY.update(height - map(y, 0, 1, 0, height)) ;
			curLocation = map.getLocationFromScreenPosition(posX.value(), posY.value());
					
			System.out.println("posX: "+  posX.value() + "posY:"  + posY.value());

		}		
			
	 }
 
	 //increase zoom -- this is the wii.PLUS event handler
	  void zoomIn(int isDown)
	  {		
		  if (isDown == 1)
		  {
			  zoom++;
			  map.zoomAndPanTo(curLocation, zoom);  
		  }
	  }
	  
	  //decrease zoom -- this is the wii.MINUS event handler	  
	  void zoomOut(int isDown)
	  {   
		  if (isDown == 1)
		  {		  
			  if (zoom > 0) zoom--;
			  map.zoomAndPanTo(curLocation, zoom);	
		  }
	  }	 
  
	  //pan while the user halds the wii.A button down, and stop and finalize pan when it comes up
	  public void panning(int isDown)
	  {
		  	isPanning = ( isDown == 1 ); 
		  	if (!isPanning)
		  	{
				map.panTo(posX.value(), posY.value()); //perform final pan

		  	}
	  }
	  
	  //pan while wii.A is down (when panning flag is on) but at a reduced sampling than the draw
	  public void pan()
	  {
		  //only check every couple milliseconds
		  if (millis() % 25 == 0)
		  {
			  map.panTo(posX.value(), posY.value());
		  }
	  }
	  
	 //this is where we draw / dislay shit. 
	 public void draw(){
		 map.draw();
		 ellipse(posX.value(), posY.value(), 10, 10);
		 
		 if (isPanning)
		 {
			pan(); 
		 } 
		 
		 //draw
		 for (int i=0; i<markers.size(); i++)
		 {
			 markers.get(i).draw();
		 }
		 
	 }
	 
	 public void mouseClicked() //disabled for now
	 {
/*		 if (!isZoomed)
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
		 */
		 
	 }
	 
	 //for testing w/o wii
	 public void keyPressed()
	 {
		 if (key == '1')
		 {
			zoomIn(1); 
		 }
		 else if (key == '2')
		 {
			 zoomOut(1);
		 }
	 }
	 
	 public void stop()
	 {
		  minim.stop();
		  super.stop();		 
	 }
	 
	 
	//finagling this.. prob. should have had the markers be applets oh well., we see.....
	void movieEvent(Movie movie) {
		  movie.read();
	}
	 

 }


