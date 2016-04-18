import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;

/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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
public class Controller implements KeyListener{
	public Bike bike;
	public Controller(Bike _bike){
		bike = _bike;
	}
	
	public void keyPressed(KeyEvent ke){
		Object choice = ke.getSource();
		
		if(choice == VK_RIGHT){
			bike.turnEast();
		}
		else if(choice == VK_LEFT){
			bike.turnWest();
		}
		else if(choice == VK_UP){
			bike.turnNorth();
		}
		else if(choice == VK_DOWN){
			bike.turnSouth();
		}	
	}
}
