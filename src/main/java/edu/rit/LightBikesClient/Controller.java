/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package edu.rit.LightBikesClient;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controller.java
 *
 * Assignment: Final Project
 * Class: Rochester Institute of Technology, ISTE-121.01, 2155
 * Professor: Michael Floeser
 *
 *
 *
 * @author Felice Aprile
 * @author Justin W. Flory
 * @author Malcolm Jones
 * @author Timothy Endersby
 * @version 2016.04.11.v1
 */
public class Controller implements KeyListener {

	public Bike bike;
	public Controller(){//Bike _bike){
		//bike = _bike;
	}
	
	public void keyPressed(KeyEvent ke){
		int choice = ke.getKeyCode();
		
		if(choice == KeyEvent.VK_RIGHT){
			//bike.turnEast();
			System.out.println("right");
		}
		else if(choice == KeyEvent.VK_LEFT){
			//bike.turnWest();
            System.out.println("left");
		}
		else if(choice == KeyEvent.VK_UP){
			//bike.turnNorth();
            System.out.println("up");
		}
		else if(choice == KeyEvent.VK_DOWN){
			//bike.turnSouth();
            System.out.println("down");
		}	
	}
	
	public void keyReleased(KeyEvent ke){
		
	}
	
	public void keyTyped(KeyEvent ke){
		
	}
}
