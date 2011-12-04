package com.somethingfurry.maps_and_then_some;

import java.util.ArrayList;

import com.somethingfurry.maps_and_then_some.DemoInfo.DemographicType;
import com.somethingfurry.maps_and_then_some.LocationMapMarker.LocationType;

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

	Map map; // the map display and functionality
	boolean isZoomed = false; // whether mapped is zoomed or not -- not used,
								// legacy!
	private int zoom = 10; // the current zoom of the map
	Location phoenix = new Location(33.43f, -112.02f); // the location of
														// phoenix
	Location curLocation = phoenix; // the current location of the map... this
									// is latitude and longitude
	Minim minim; // for audio classes
	NunChuckHandler nunChuckHandler;

	OscP5 oscP5; // object to enable OSC shiz
	public static final int OSC_PORT = 9000; // max port coz I'm lazy //the port
												// to listen for OSC messages on

	// OSC messages incoming from wiimote / osculator
	public static final String ADDR_WIIPLUS = "/wii/1/button/Plus";
	public static final String ADDR_WIIMINUS = "/wii/1/button/Minus";
	public static final String ADDR_WIILEFT = "/wii/1/button/Left";
	public static final String ADDR_WIIRIGHT = "/wii/1/button/Right";
	public static final String ADDR_WIIACCEL = "/wii/1/accel/pry";
	public static final String ADDR_WIIIR = "/wii/1/ir";
	public static final String ADDR_WIIA = "/wii/1/button/A";
	public static final String ADDR_WII1 = "/wii/1/button/1";
	public static final String ADDR_WIINUNCHUCK = "/wii/1/nunchuk/joy";

	public static final String ADDR_WII2 = "/wii/1/button/2";

	// hack hack hack hack
	public SmoothedValue posX; // the current position of the wii - cursor
	public SmoothedValue posY; // the current position of hte wii - cursor

	private boolean isPanning = false; // whether we are in panning mode... if
										// so pan to where cursor is

	private ArrayList<LocationMapMarker> markers;

	// analogous to setup in the processing file. create shit. set it up!
	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		// TODO: CHECK IF THERE IS INTERNET>>> IF SO THEN COOL MAP... IF NOT
		// THEN UNCOOLZ MAP!!! SHIZ!!
		map = new de.fhpotsdam.unfolding.Map(this, "map", 0, 0, 800, 600, true,
				false, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		/*
		 * map = new de.fhpotsdam.unfolding.Map(this, "map", 0, 0, 600, 500,
		 * true, false, new
		 * OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY,
		 * 30635));
		 */

		// zoom and pan to Phoenix
		map.zoomAndPanTo(phoenix, zoom);
		OscP5 oscP5 = new OscP5(this, OSC_PORT);

		// connect event handlers to OSC (re: wii events)
		oscP5.plug(this, "zoomIn", ADDR_WIIPLUS);
		oscP5.plug(this, "zoomOut", ADDR_WIIMINUS);
		oscP5.plug(this, "panning", ADDR_WIIA);
		oscP5.plug(this, "presentMedia", ADDR_WII1);
		oscP5.plug(this, "closeMedia", ADDR_WII2);
		oscP5.plug(this, "nunChuckCursor", ADDR_WIINUNCHUCK);

		// create the current x, y pos of screen
		posX = new SmoothedValue();
		posY = new SmoothedValue();
		posY.update(height / 2);
		posX.update(width / 2);

		minim = new Minim(this);

		nunChuckHandler = new NunChuckHandler(this);

		markers = new ArrayList<LocationMapMarker>();
		initMarkers();

	}

	// void init the markers, that is add in all the data
	void initMarkers() {

		// TODO: add real data and locations

		LocationMapMarker zMarker = new ZoomedOutMapLocationMarker(phoenix,
				this, map);
		LocationMapMarker pMarker = new PhotoLocationMapMarker(phoenix, this,
				map, LocationType.HANGOUT, "nasal_passages.jpg", "jpg", 512,
				384);
		LocationMapMarker aMarker = new AudioLocationMapMarker(phoenix, this,
				map, LocationType.EAT, minim, "02 - Blue Monday.mp3");
		LocationMapMarker vMarker = new VideoLocationMapMarker(phoenix, this,
				map, LocationType.BATHROOMS, "sound_picture_coif_excerpt.mov",
				400, 300);

		DemoInfo info = new DemoInfo();
		info.add(DemographicType.AGE, "17");
		info.add(DemographicType.PERCENT_FEMALE, "100%");
		info.add(DemographicType.PERCENT_MALE, "0%");
		info.add(DemographicType.BATHROOMS, "35%");
		DemographcMapMarker dMarker = new DemographcMapMarker(phoenix, this,
				map, info);

		markers.add(zMarker);
		// markers.add(pMarker);
		// markers.add(aMarker);
		markers.add(dMarker);

		// E 7th St.

		Location e7thSt = new Location(33.4259f, -111.8937f);

		LocationMapMarker zE7thSt = new ZoomedOutMapLocationMarker(e7thSt,
				this, map);

		markers.add(zE7thSt);

		LocationMapMarker pE7thSt_bathroom = new PhotoLocationMapMarker(e7thSt,
				this, map, LocationType.BATHROOMS,
				"Bathrooms/17_E.7th_St_MG_1394 copy.JPG", "jpg",
				400, 300);

		markers.add(pE7thSt_bathroom);
		PImage img = loadImage("/Bathrooms/17_E.7th_St_MG_1394 copy.JPG", "jpg"); // good place to load??? we'll see...	

		
		/*

		Location e7thSt_eat_01 = new Location(33.4380f, -111.8948f);

		LocationMapMarker pE7thSt_eat_01 = new PhotoLocationMapMarker(
				e7thSt_eat_01, this, map, LocationType.EAT,

				"photo\\17_E.7th_St._IMG_1395.jpg", "jpg");

		markers.add(pE7thSt_eat_01);

		Location e7thSt_eat_02 = new Location(33.4363f, -111.8920f);

		LocationMapMarker pE7thSt_eat_02 = new PhotoLocationMapMarker(

		e7thSt_eat_02, this, map, LocationType.EAT,

		"photo\\17_E.7th_St._IMG_1396.jpg", "jpg");

		markers.add(pE7thSt_eat_02);

		Location e7thSt_eat_03 = new Location(33.4320f, -111.8951f);

		LocationMapMarker pE7thSt_eat_03 = new PhotoLocationMapMarker(

		e7thSt_eat_03, this, map, LocationType.EAT,

		"photo\\17_E.7th_St._IMG_1397.jpg", "jpg");

		markers.add(pE7thSt_eat_03);

		Location e7thSt_elec_01 = new Location(33.4344f, -111.8937f);

		LocationMapMarker eE7thSt_elec_01 = new PhotoLocationMapMarker(

		e7thSt_elec_01, this, map, LocationType.ELECTRICITY,

		"electricity\\11_E._7th_St_IMG_1393.JPG", "jpg");

		markers.add(eE7thSt_elec_01);

		Location e7thSt_elec_02 = new Location(33.4351f, -111.8942f);

		LocationMapMarker eE7thSt_elec_02 = new PhotoLocationMapMarker(

		e7thSt_elec_02, this, map, LocationType.ELECTRICITY,

		"electricity\\17_E.7th_St_MG_1394.JPG", "jpg");

		markers.add(eE7thSt_elec_02);

		Location e7thSt_movie = new Location(33.4373f, -111.8952f);

		LocationMapMarker vE7thSt_movie = new VideoLocationMapMarker(

		e7thSt_movie, this, map, LocationType.STORY,

		"/Maps_and_then_some/data/Stories/Movies/17_E.7th_St.mov");

		markers.add(vE7thSt_movie);

		// Ash and University

		Location ashAndUniv = new Location(33.4219f, -111.9424f);

		LocationMapMarker zAshAndUniv = new ZoomedOutMapLocationMarker(

		ashAndUniv, this, map);

		markers.add(zAshAndUniv);

		LocationMapMarker pAshAndUniv_bathroom = new PhotoLocationMapMarker(

		ashAndUniv, this, map, LocationType.BATHROOMS,

		"119_Ash_and_University_IMG_1411 copy.JPG", "jpg");

		markers.add(pAshAndUniv_bathroom);

		Location ashAndUniv_01 = new Location(33.4230f, -111.9444f);

		LocationMapMarker pAshAndUniv_hangout_01 = new PhotoLocationMapMarker(

		ashAndUniv_01, this, map, LocationType.HANGOUT,

		"hangout\\photo\\119_Ash_and_University_IMG_1411.JPG", "JPG");

		markers.add(pAshAndUniv_hangout_01);

		// Mill Ave.

		Location millAve = new Location(33.4201f, -111.9397f);

		LocationMapMarker zMillAve = new ZoomedOutMapLocationMarker(millAve,

		this, map);

		markers.add(zMillAve);

		LocationMapMarker pMillAve_bathroom = new PhotoLocationMapMarker(

		millAve, this, map, LocationType.BATHROOMS,

		"721_Mill_Ave_IMG_1398 copy.JPG", "jpg");

		markers.add(pMillAve_bathroom);
		
		

		Location millAve_eat = new Location(33.4211f, -111.9300f);

		LocationMapMarker aMillAve_eat = new AudioLocationMapMarker(

		millAve_eat, this, map, LocationType.EAT, minim,

		"Eat\\Audio\\414_Mill_Ave.aiff");

		markers.add(aMillAve_eat);

		Location millAve_eat_movie = new Location(33.4222f, -111.9328f);

		LocationMapMarker vMillAve_eat = new VideoLocationMapMarker(

		millAve_eat_movie, this, map, LocationType.EAT,

		"Eat\\movie\\740_S_Mill_Ave._#140.mov");

		markers.add(vMillAve_eat);

		Location millAve_eat_photo = new Location(33.4232f, -111.9355f);

		LocationMapMarker pMillAve_eat = new PhotoLocationMapMarker(

		millAve_eat_photo, this, map, LocationType.EAT,

		"Eat\\photo\\721_Mill_Ave_IMG_1398.JPG", "jpg");

		markers.add(pMillAve_eat);

		Location millAve_eat_photo_01 = new Location(33.4259f, -111.9397f);

		LocationMapMarker pMillAve_eat_01 = new PhotoLocationMapMarker(

		millAve_eat_photo_01, this, map, LocationType.EAT,

		"Eat\\photo\\721_Mill_Ave_Jack_Dan_IMG_1389.JPG", "jpg");

		markers.add(pMillAve_eat_01);

		Location millAve_hangout_audio = new Location(33.4227f, -111.9348f);

		LocationMapMarker aMillAve_hangout = new AudioLocationMapMarker(

		millAve_hangout_audio, this, map, LocationType.HANGOUT, minim,

		"hangout\\audio\\740_Mill_Ave.aiff");
		
		

		markers.add(aMillAve_hangout);

		Location millAve_hangout_photo_01 = new Location(33.4201f, -111.9397f);

		LocationMapMarker pMillAve_hangout_01 = new PhotoLocationMapMarker(

				millAve_hangout_photo_01,

				this,

				map,

				LocationType.HANGOUT,

				"Hangout/Photos/501_S._Mill_Ave_IMG_1409.jpg",

				"JPG");

		markers.add(pMillAve_hangout_01);

		Location millAve_hangout_photo_02 = new Location(33.4242f, -111.9334f);

		LocationMapMarker pMillAve_hangout_02 = new PhotoLocationMapMarker(

		millAve_hangout_photo_02,

		this,

		map,

		LocationType.HANGOUT,

		"Hangout/Photos/509_Mill_Ave_IMG_1410.JPG",

		"JPG");

		markers.add(pMillAve_hangout_02);

		Location millAve_hangout_photo_03 = new Location(33.4239f, -111.9329f);

		LocationMapMarker pMillAve_hangout_03 = new PhotoLocationMapMarker(

		millAve_hangout_photo_03,

		this,

		map,

		LocationType.HANGOUT,

		"Hangout/Photos/699_Mill_Ave_IMG_1401.jpg",

		"JPG");

		markers.add(pMillAve_hangout_03);

		Location millAve_hangout_photo_04 = new Location(33.4224f, -111.9367f);

		LocationMapMarker pMillAve_hangout_04 = new PhotoLocationMapMarker(

		millAve_hangout_photo_04,

		this,

		map,

		LocationType.HANGOUT,

		"Hangout/Photos/721_Mill_Ave_2_Dan.jpg",

		"JPG");

		markers.add(pMillAve_hangout_04);

		Location millAve_hangout_photo_05 = new Location(33.4239f, -111.9400f);

		LocationMapMarker pMillAve_hangout_05 = new PhotoLocationMapMarker(

		millAve_hangout_photo_05,

		this,

		map,

		LocationType.HANGOUT,

		"Hangout/Photos/721_Mill_Ave_Bus_Stop.jpg",

		"JPG");

		markers.add(pMillAve_hangout_05);

		Location millAve_hangout_photo_06 = new Location(33.4266f, -111.9381f);

		LocationMapMarker pMillAve_hangout_06 = new PhotoLocationMapMarker(

		millAve_hangout_photo_06, this, map, LocationType.HANGOUT,

		"Hangout/Photos/721_Mill_Ave.jpg",

		"JPG");

		markers.add(pMillAve_hangout_06);

		Location millAve_hangout_photo_07 = new Location(33.4246f, -111.9401f);

		LocationMapMarker pMillAve_hangout_07 = new PhotoLocationMapMarker(

				millAve_hangout_photo_07,

				this,

				map,

				LocationType.HANGOUT,

				"Hangout/Photos/Mill_and_6thSt._IMG_1403.JPG",

				"JPG");

		markers.add(pMillAve_hangout_07);

		Location millAve_hangout_photo_08 = new Location(33.4233f, -111.9402f);

		LocationMapMarker pMillAve_hangout_08 = new PhotoLocationMapMarker(

				millAve_hangout_photo_08,

				this,

				map,

				LocationType.HANGOUT,

				"Hangout/Photos/Mill_Ave_And_7th_IMG_1392.jpg",

				"JPG");

		markers.add(pMillAve_hangout_08);

		Location millAve_sleep_video = new Location(33.3995f, -111.9400f);

		LocationMapMarker vMillAve_sleep = new VideoLocationMapMarker(

		millAve_sleep_video, this, map, LocationType.SLEEP,

		"Sleep/Movies/Mill_Rte202.mov");

		markers.add(vMillAve_sleep);

		Location millAve_sleep_photo = new Location(33.4057f, -111.9373f);

		LocationMapMarker pMillAve_sleep = new PhotoLocationMapMarker(

		millAve_sleep_photo,

		this,

		map,

		LocationType.SLEEP,

		"Sleep/Photos/425_Mill_Ave_IMG_1408.jpg",

		"JPG");

		markers.add(pMillAve_sleep);

		Location millAve_stories_movie = new Location(33.4217f, -111.9392f);

		LocationMapMarker vMillAve_stories = new VideoLocationMapMarker(

		millAve_stories_movie, this, map, LocationType.STORY,

		"/Maps_and_then_some/data/Stories/Movies/Mill_Ave_and_University.mov");

		// East Roosevelt

		Location eastRoosevelt = new Location(33.4585f, -111.9264f);

		LocationMapMarker zEastRoosevelt = new ZoomedOutMapLocationMarker(

		eastRoosevelt, this, map);

		markers.add(zEastRoosevelt);

		LocationMapMarker pEastRoosevelt = new PhotoLocationMapMarker(

		eastRoosevelt, this, map, LocationType.BATHROOMS,

		"1546_East_Roosevelt_Street.jpg", "jpg");

		markers.add(pEastRoosevelt);

		// North black canyon highway

		Location northBlackCanyonHighway = new Location(33.6008f, -112.1164f);

		LocationMapMarker zNBCH = new ZoomedOutMapLocationMarker(
				northBlackCanyonHighway, this, map);

		markers.add(zNBCH);

		LocationMapMarker vNBCH_hangout = new VideoLocationMapMarker(

		northBlackCanyonHighway, this, map, LocationType.HANGOUT,

		"hangout\\movies\\5050_North_Black_Canyon_Highway.mov");

		markers.add(vNBCH_hangout);

		Location vNBCH_stories = new Location(33.6012f, -112.1148f);

		LocationMapMarker vNBCH_stories_movie = new VideoLocationMapMarker(

		vNBCH_stories, this, map, LocationType.STORY,

		"/Maps_and_then_some/data/Stories/Movies/5050_North_Black_Canyon_Highway.mov");

		markers.add(vNBCH_stories_movie);

		// Tempe town lake

		Location tempeTownLake = new Location(33.4339f, -111.9325f);

		LocationMapMarker zTempeTownLake = new ZoomedOutMapLocationMarker(

		tempeTownLake, this, map);

		markers.add(zTempeTownLake);

		LocationMapMarker vTempeTownLake_hangout = new VideoLocationMapMarker(

		tempeTownLake, this, map, LocationType.HANGOUT,

		"hangout\\movies\\Tempe_Town_Lake.mov");

		markers.add(vTempeTownLake_hangout);

		// W 5th St

		Location w5thSt = new Location(33.4254f, -111.9555f);

		LocationMapMarker zW5thSt = new ZoomedOutMapLocationMarker(w5thSt,

		this, map);

		markers.add(zW5thSt);

		LocationMapMarker pW5thSt = new PhotoLocationMapMarker(w5thSt, this,

		map, LocationType.HANGOUT,

		"hangout\\photos\\24_W_5th_St_IMG_1405.JPG", "JPG");

		markers.add(pW5thSt);

		Location w5thSt_sleep = new Location(334270f, -111.9534f);

		LocationMapMarker vW5thSt_sleep = new VideoLocationMapMarker(

		w5thSt_sleep, this, map, LocationType.SLEEP,

		"Sleep/Movies/24_W_5th_St.MOV");

		markers.add(vW5thSt_sleep);

		// E Washington St

		Location eWashington = new Location(33.4463f, -111.9631f);

		LocationMapMarker zEWashington = new ZoomedOutMapLocationMarker(

		eWashington, this, map);

		markers.add(zEWashington);

		LocationMapMarker pEWashington = new PhotoLocationMapMarker(

		eWashington, this, map, LocationType.HANGOUT,

		"hangout\\photos\\3150_E_Washington_St.jpg", "JPG");

		markers.add(pEWashington);

		// East Van Buren Street

		Location eVB = new Location(33.4513f, -112.0148f);

		LocationMapMarker zEVB = new ZoomedOutMapLocationMarker(eVB, this, map);

		markers.add(zEVB);

		LocationMapMarker pEVB = new PhotoLocationMapMarker(

				eVB,

				this,

				map,

				LocationType.HANGOUT,

				"Hangout/Photos/3333 East Van Buren Street Phoenix, Arizona_TumbleWeeds.jpg",

				"JPG");

		markers.add(pEVB);

		// Edison Park

		Location edisonPark = new Location(33.4583f, -112.0400f);

		LocationMapMarker zEdisonPark = new ZoomedOutMapLocationMarker(

		edisonPark, this, map);

		markers.add(zEdisonPark);

		LocationMapMarker pEdisonPark = new PhotoLocationMapMarker(

		edisonPark,

		this,

		map,

		LocationType.HANGOUT,

		"Hangout/Photos/Edison_Park‎_Phoenix.jpg",

		"JPG");

		markers.add(pEdisonPark);

		Location vEdisonPark = new Location(33.4579f, -112.0404f);

		LocationMapMarker aEdisonPark = new AudioLocationMapMarker(vEdisonPark,

		this, map, LocationType.STORY, minim,

		"Stories/Audio/Edison_Park‎_Phoenix.aiff");

		markers.add(aEdisonPark);

		// Willow Park

		Location willowPark = new Location(33.4544f, -112.0189f);

		LocationMapMarker zWillowPark = new ZoomedOutMapLocationMarker(

		willowPark, this, map);

		markers.add(zWillowPark);

		LocationMapMarker pWillowPark = new PhotoLocationMapMarker(

				willowPark,

				this,

				map,

				LocationType.HANGOUT,

				"Hangout/Photos/Willow_Park‎_2815_W. Taylor_Street.jpg",

				"JPG");

		markers.add(pWillowPark);

		// west camelback

		Location westCamelback = new Location(33.5081f, -112.2772f);

		LocationMapMarker zWestCamelback = new ZoomedOutMapLocationMarker(

		westCamelback, this, map);

		markers.add(zWestCamelback);

		LocationMapMarker pWestCamelBack = new PhotoLocationMapMarker(

		westCamelback,

		this,

		map,

		LocationType.SLEEP,

		"Sleep/Photos/2750_West_Camelback_Road.jpg",

		"JPG");

		markers.add(pWestCamelBack);

		// North 16th Street

		Location north16th = new Location(33.4119f, -111.9477f);

		LocationMapMarker zNorth16th = new ZoomedOutMapLocationMarker(

		north16th, this, map);

		markers.add(zNorth16th);

		LocationMapMarker pNorth16th_photo = new PhotoLocationMapMarker(

		north16th,

		this,

		map,

		LocationType.SLEEP,

		"Sleep/Photos/402_North_16th_Street.jpg",

		"JPG");

		markers.add(pNorth16th_photo);

		// Biehn Colony Park Guadalupe

		Location biehnCPG = new Location(33.3677f, -111.9616f);

		LocationMapMarker zBCPG = new ZoomedOutMapLocationMarker(biehnCPG,

		this, map);

		markers.add(zBCPG);

		LocationMapMarker pBCPG = new PhotoLocationMapMarker(

				biehnCPG,

				this,

				map,

				LocationType.SLEEP,

				"Sleep/Photos/Biehn_Colony_Park‎_Guadalupe.jpg",

				"JPG");

		markers.add(pBCPG);

		// Cielito Park

		Location cielitoPark = new Location(33.5043f, -112.1318f);

		LocationMapMarker zCielitoPark = new ZoomedOutMapLocationMarker(

		cielitoPark, this, map);

		markers.add(zCielitoPark);

		LocationMapMarker pCielitoPark = new PhotoLocationMapMarker(

				cielitoPark,

				this,

				map,

				LocationType.SLEEP,

				"Sleep/Photos/Cielito Park‎_3402_West_Campbell_Avenue.jpg",

				"JPG");

		markers.add(pCielitoPark);

		// Margaret T Hance Park

		Location hancePark = new Location(33.4616f, -112.0756f);

		LocationMapMarker zHancePark = new ZoomedOutMapLocationMarker(

		hancePark, this, map);

		markers.add(zHancePark);

		LocationMapMarker pHancePark = new PhotoLocationMapMarker(

				hancePark,

				this,

				map,

				LocationType.SLEEP,

				"Sleep/Photos/Margaret_T_Hance_Park_67_West_Culver_Street.jpg",

				"JPG");

		markers.add(pHancePark);

		Location hancePark_movie = new Location(33.4623f, -112.0743f);

		LocationMapMarker vHancePark_movie = new VideoLocationMapMarker(

				hancePark_movie,

				this,

				map,

				LocationType.STORY,

				"Stories/Movies/Margaret_T_Hance_Park_67_West_Culver_Street.mov");

		markers.add(vHancePark_movie);
		
		*/
	}

	void spreadMarkers() {
		// spread markers all entered into the same location

		// keep track of markers with same location, put them into bins

		// go through ea. bin and spread them

	}

	// all purpose OSC event handler for un-plugged OSC events
	// I handle the acceleration params from the wii here, since I was trying to
	// use IR to control the cursor
	// but IR SUCKS.
	void oscEvent(OscMessage message) {
		float x;
		float y;
		// System.out.println(message);

		// the IR codesz
		/*
		 * if( message.checkAddrPattern(ADDR_WIIIR) ) { x = (float)
		 * message.get(0).floatValue(); y = (float) message.get(1).floatValue();
		 * 
		 * posX = (int) (map(x, 0, 1, 0, width) ); posY = (int) (map(y, 0, 1, 0,
		 * height) ); System.out.println("ir_x: "+ x + "  ir_y:" + y + "posX: "+
		 * posX + "posY:" + posY); } else
		 */

		// use the acceleration to change position.
		/*
		 * if( message.checkAddrPattern(ADDR_WIIACCEL) ) { y = (float)
		 * message.get(0).floatValue(); x = (float) message.get(1).floatValue();
		 * 
		 * posX.update( map(x, 0, 1, 0, width) ) ; posY.update(height - map(y,
		 * 0, 1, 0, height)) ; curLocation =
		 * map.getLocationFromScreenPosition(posX.value(), posY.value());
		 * 
		 * // System.out.println("posX: "+ posX.value() + "posY:" +
		 * posY.value());
		 * 
		 * } /* if (message.checkAddrPattern(ADDR_WIINUNCHUCK)) { // x = (float)
		 * message.get(0).floatValue(); // y = (float)
		 * message.get(1).floatValue();
		 * 
		 * System.out.println(message);
		 * 
		 * // posX.update( map(x, 0, 1, 0, width) ) ; // posY.update(height -
		 * map(y, 0, 1, 0, height)) ; // curLocation =
		 * map.getLocationFromScreenPosition(posX.value(), posY.value()); }
		 */

	}

	// increase zoom -- this is the wii.PLUS event handler
	void zoomIn(int isDown) {
		if (isDown == 1) {
			zoom++;
			map.zoomAndPanTo(curLocation, zoom);
		}
	}

	// decrease zoom -- this is the wii.MINUS event handler
	void zoomOut(int isDown) {
		if (isDown == 1) {
			if (zoom > 0)
				zoom--;
			map.zoomAndPanTo(curLocation, zoom);
		}
	}

	public int[] curXY() {
		int[] xy = new int[2];
		xy[0] = posX.value();
		xy[1] = posY.value();
		return xy;
	}

	void presentMedia(int down) {
		if (1 == down) {
			for (int i = 0; i < markers.size(); i++) {
				markers.get(i).pressOn();
			}
		}

	}

	void closeMedia(int down) {
		if (1 == down) {
			for (int i = 0; i < markers.size(); i++) {
				markers.get(i).pressOff();
			}
		}

	}

	// pan while the user halds the wii.A button down, and stop and finalize pan
	// when it comes up
	public void panning(int isDown) {
		isPanning = (isDown == 1);
		if (!isPanning) {
			map.panTo(posX.value(), posY.value()); // perform final pan

		}
	}

	// pan while wii.A is down (when panning flag is on) but at a reduced
	// sampling than the draw
	public void pan() {
		// only check every couple milliseconds
		if (millis() % 25 == 0) {
			map.panTo(posX.value(), posY.value());
		}
	}

	// this is where we draw / dislay shit.
	public void draw() {
		map.draw();
		fill(0, 0, 0, 255);
		ellipse(posX.value(), posY.value(), 7, 7);

		if (isPanning) {
			pan();
		}

		// draw
		for (int i = 0; i < markers.size(); i++) {
			markers.get(i).draw();
		}

	}

	void nunChuckCursor(float x, float y) {
		nunChuckHandler.handleNunchuck(x, y);
	}

	public void mouseClicked() // disabled for now
	{
		/*
		 * if (!isZoomed) { // Shows geo-location at mouse position Location
		 * location = map.getLocationFromScreenPosition(mouseX, mouseY);
		 * map.zoomAndPanTo(location, 13); isZoomed = true; } else {
		 * map.zoomAndPanTo(phoenix, 10); isZoomed = false; }
		 */

	}
	
	public void updateCurPos()
	{	
		curLocation = map.getLocationFromScreenPosition(posX.value(), posY.value());
	}

	// for testing w/o wii
	public void keyPressed() {
		if (key == '1') {
			zoomIn(1);
		} else if (key == '2') {
			zoomOut(1);
		}
	}

	public void stop() {
		minim.stop();
		super.stop();
	}

}
