/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

/**
 * Grid.java
 *
 * Assignment: Final Project
 * Class: Rochester Institute of Technology, ISTE-121.01, 2155
 * Professor: Michael Floeser
 *
 * This class is used as the grid GUI that the game is played on. The bike objects will move across the grid as they
 * leave the wall of light.
 *
 * @author Felice Aprile
 * @author Justin W. Flory
 * @author Malcolm Jones
 * @author Timothy Endersby
 * @version 2016.04.11.v1
 */
public class Grid {
	public final int GRID_HEIGHT = 100;
	public final int GRID_WIDTH = 100;
	public final int DELAY_IN_MILLS = 100;
	public int[][] grid = new int[GRID_WIDTH][GRID_HEIGHT];
}