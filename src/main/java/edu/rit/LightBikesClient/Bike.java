/*
* Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package edu.rit.LightBikesClient;

import java.util.concurrent.TimeUnit;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import sun.net.www.content.text.plain;

/**
* Bike.java
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
public class Bike {

	public static final int DIRECTION_NORTH = 0;
	public static final int DIRECTION_EAST = 1;
	public static final int DIRECTION_SOUTH = 2;
	public static final int DIRECTION_WEST = 3;

	public int xPosition;
	public int yPosition;
	public int[][] gridArray;
	public int player;
	private int direction;
	private final int DELAY_IN_MILLS = 100;
	public boolean gameState;
	private Grid grid;

	public Bike(int _xPosition, int _yPosition, int[][] _gridArray, int _player, int initialDirection, Grid _grid){
		direction = initialDirection;
		xPosition = _xPosition;
		yPosition = _yPosition;
		gridArray = _gridArray;
		player = _player;
		gameState = true;
		grid = _grid;
		gridArray[xPosition][yPosition] = player;
		grid.repaint();
	}

	/*
	* Info for each turn*() method.
	* First line sets the array value in the direction bike is going to bike id.
	* Second line changes bikes position.
	* Try loop delays bike movement.
	*/

	public void turnWest(){
		direction = DIRECTION_WEST;
	}

	public void turnEast(){
		direction = DIRECTION_EAST;
	}

	public void turnSouth(){
		direction = DIRECTION_SOUTH;
	}

	public void turnNorth(){
		direction = DIRECTION_NORTH;
	}

	public boolean checkLocation(int x, int y) {
		return x > 0 && x < gridArray.length && y > 0 && y < gridArray[0].length && gridArray[x][y] != 0;
	}

	public void updateLocation() {
		gridArray[xPosition][yPosition] = player;
		grid.repaint();
	}

	public boolean getGameState(){
		return gameState;
	}

	public int getXpos() {
		return xPosition;
	}

	public int getYpos() {
		return yPosition;
	}


	class Movement implements Runnable {

		@Override
		public void run() {
			while (gameState) {
				switch(direction) {

					case DIRECTION_NORTH:
					gameState = checkLocation(xPosition, yPosition-1);
					if (gameState) {
						yPosition--;
					}
					break;

					case DIRECTION_EAST:
					gameState = checkLocation(xPosition+1, yPosition);
					if (gameState) {
						xPosition++;
					}
					break;

					case DIRECTION_SOUTH:
					gameState = checkLocation(xPosition, yPosition+1);
					if (gameState) {
						yPosition++;
					}
					break;

					case DIRECTION_WEST:
					gameState = checkLocation(xPosition-1, yPosition);
					if (gameState) {
						xPosition--;
					}
					break;
				}

				if (!gameState) {
					break;
				}

				updateLocation();
				connector.sendLocation(xPosition, yPosition);

				try{
					TimeUnit.MILLISECONDS.sleep(DELAY_IN_MILLS);
				}
				catch(ArrayIndexOutOfBounds aiob) {
					aiob.printStackTrace();
				}
				catch(InterruptedException ie){
					ie.printStackTrace();
				}
			}
		}
	}
}
