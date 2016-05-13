/*
* Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package edu.rit.LightBikesClient;

import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

/**
* Bike.java	<p>
*
* Assignment: Final Project <p>
* Class: Rochester Institute of Technology, ISTE-121.01, 2155 <p>
* Professor: Michael Floeser <p>
*
* A <code>Bike</code> represents each player on screen. Each <code>Bike</code>
* object keeps track of its location, and updates its position in the <code>
* Grid</code> object. The <code>Movement</code> inner class is used to move the
* bike forward in a constant direction as specified by the <code>direction</code>
* attribute.
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
	/**
	 * The amount of time in milliseconds between each step for movement.
	 */
	private static final int DELAY_IN_MILLS = 80;

	public int xPosition;
	public int yPosition;
	public int[][] gridArray;
	public int player;
	private int direction;
	public boolean gameState;
	private Grid grid;
	private NetworkConnector connector;

	/**
	 * Creates a new <code>Bike</code> object, setting default values and adding
	 * the bike to the specified starting location.
	 * @param  _xPosition 			The starting x coordinate to place the bike
	 *                       		on the grid.
	 * @param  _yPosition 			The starting x coordinate to place the bike
	 *                       		on the grid.
	 * @param  _gridArray 			The grid array to keep track of locations of
	 *                       		different <code>Bike</code> objects.
	 * @param  _player 				The player id to use. This value is what
	 *                     			is entered in the grid array when moving.
	 * @param  initialDirection 	The initial direction the bike should head
	 *                           	when the <code>Movement</code> thread is
	 *                           	started.
	 * @param  _grid 				The <code>Grid</code> object controlling the
	 *                   			different <code>Bike</code> objects.
	 */
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

	/**
	 * Sets the direction of this <code>Bike</code> to the <code>DIRECTION_WEST</code>
	 * constant. <p> The direction will not be changed if set to the exact
	 * opposite as <code>Bike</code>s are not allowed to go backwards along the
	 * same path.
	 */
	public void turnWest(){
		if(direction != DIRECTION_EAST){
			direction = DIRECTION_WEST;
		}
	}

	/**
	 * Sets the direction of this <code>Bike</code> to the <code>DIRECTION_EAST</code>
	 * constant. <p> The direction will not be changed if set to the exact
	 * opposite as <code>Bike</code>s are not allowed to go backwards along the
	 * same path.
	 */
	public void turnEast(){
		if(direction != DIRECTION_WEST){
			direction = DIRECTION_EAST;
		}
	}

	/**
	 * Sets the direction of this <code>Bike</code> to the <code>DIRECTION_SOUTH</code>
	 * constant. <p> The direction will not be changed if set to the exact
	 * opposite as <code>Bike</code>s are not allowed to go backwards along the
	 * same path.
	 */
	public void turnSouth(){
		if(direction != DIRECTION_NORTH){
			direction = DIRECTION_SOUTH;
		}
	}

	/**
	 * Sets the direction of this <code>Bike</code> to the <code>DIRECTION_NORTH</code>
	 * constant. <p> The direction will not be changed if set to the exact
	 * opposite as <code>Bike</code>s are not allowed to go backwards along the
	 * same path.
	 */
	public void turnNorth(){
		if(direction != DIRECTION_SOUTH){
			direction = DIRECTION_NORTH;
		}
	}

	/**
	 * Stops this <code>Bike</code> from moving any more by changing the
	 * <code>gameState</code> to false, signalling a halt to the inner
	 * <code>Movement</code> thread.
	 */
	public void stop(){
		gameState = false;
	}

	/**
	 * Checks the <code>grid</code> array to verify that a location is valid.
	 * Locations that are both unoccupied (neither player has marked it) and
	 * within the <code>grid</code>'s bounds are considered valid.
	 * @param   x	The x-value of the coordinate to check.
	 * @param   y	The y-value of the coordinate to check.
	 * @return  <code>True</code> if the location is valid,
	 * <code>false</code> if not.
	 */
	public boolean checkLocation(int x, int y) {
		return x > 0 && x < gridArray.length && y > 0 && y < gridArray[0].length && gridArray[x][y] == 0;
	}

	/**
	 * Registers the current location of this <code>Bike</code> as specified in its
	 * <code>xPosition</code> and <code>yPosition</code> variables witin the
	 * <code>grid</code> array.
	 */
	public void updateLocation() {
		gridArray[xPosition][yPosition] = player;
		grid.repaint();
	}

	/**
	 * Set the location of the bike manually (Used by Network Connector)
	 * @param  x	The x value of the location
	 * @param  y	The y value of the location
	 */
	public void setLocation(int x, int y) {
		this.xPosition = x;
		this.yPosition = y;
		updateLocation();
	}

	/**
	 * Gets the current <code>gameState</code> of this <code>Bike</code> object.
	 * @return Returns <code>true</code> if the game is still in play,
	 * <code>false</code> if not.
	 */
	public boolean getGameState(){
		return gameState;
	}

	/**
	 * Gets the current x value of the coordinates of this <code>Bike</code>.
	 * @return The current x value.
	 */
	public int getXpos() {
		return xPosition;
	}

	/**
	 * Gets the current y value of the coordinates of this <code>Bike</code>.
	 * @return The current y value.
	 */
	public int getYpos() {
		return yPosition;
	}

	/**
	 * Start the game (i.e. set this bike moving forward) (Used by NetworkConnector
	 * when the server starts the game)
	 */
	public void startGame() {
		new Thread(new Movement()).start();
	}

	/**
	 * Runnable inner class which faciliates the systematic movement of the
	 * <code>Bike</code> in the GUI based on the time delay set in the
	 * <code>Bike</code>, the value of the <code>Bike</code> object's
	 * <code>direction</code>, and the <code>gameState</code>. The
	 * <code>Movement</code> thread will complete and exit once the
	 * <code>gameState</code> has been set to false.
	 *
	 */
	class Movement implements Runnable {

		/**
		 * Begins movement of the this <code>Bike</code> object within the GUI,
		 * moving the <code>Bike</code> at the interval specifed in
		 * <code>Bike.DELAY_IN_MILLIS</code> and registering the changes with
		 * both the <code>Grid</code> and the <code>NetworkConnector</code>
		 * created by <code>Grid</code>.
		 */
		@Override
		public void run() {
			NetworkConnector connector = grid.getConnector();
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
					grid.stop();
					connector.notifyDeath();
					grid.lost();
					break;
				}

				// Send the server the bike's new location
				connector.sendLocation(xPosition, yPosition);

				// Show the new location on the grid
				updateLocation();

				// Take a break
				try{
					TimeUnit.MILLISECONDS.sleep(DELAY_IN_MILLS);
				}
				catch(InterruptedException ie){
					ie.printStackTrace();
				}
			}
		}
	}
}
