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
	public int xPosition;
	public int yPosition;
	public int[][] gridArray;
	public int player;
    private final int DELAY_IN_MILLS = 100;
    public boolean gameState;
    private Grid grid;

	public Bike(int _xPosition, int _yPosition, int[][] _gridArray, int _player, Grid _grid){
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
		while(gameState == true){
			if(gridArray[xPosition-1][yPosition] != 0){
				gameState = false;
			}
			gridArray[xPosition-1][yPosition] = player;
			xPosition = xPosition - 1;
			grid.repaint();
			try{
				TimeUnit.MILLISECONDS.sleep(DELAY_IN_MILLS);
			}
			catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
	}

	public void turnEast(){
		while(gameState == true){
			if(gridArray[xPosition+1][yPosition] != 0){
				gameState = false;
			}
			gridArray[xPosition+1][yPosition] = player;
			xPosition = xPosition + 1;
			grid.repaint();
			try{
				TimeUnit.MILLISECONDS.sleep(DELAY_IN_MILLS);
			}
			catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
	}

	public void turnSouth(){
		while(gameState == true){
			if(gridArray[xPosition][yPosition+1] != 0){
				gameState = false;
			}
			gridArray[xPosition][yPosition+1] = player;
			yPosition = yPosition + 1;
			grid.repaint();
			try{
				TimeUnit.MILLISECONDS.sleep(DELAY_IN_MILLS);
			}
			catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
	}

	public void turnNorth(){
		while(gameState == true){
			if(gridArray[xPosition][yPosition-1] != 0){
				gameState = false;
			}
			gridArray[xPosition][yPosition-1] = player;
			yPosition = yPosition - 1;
			grid.repaint();
			try{
				TimeUnit.MILLISECONDS.sleep(DELAY_IN_MILLS);
			}
			catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
	}
	
	public boolean getGameState(){
		return gameState;
	}
}