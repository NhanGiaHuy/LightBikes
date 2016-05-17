/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package edu.rit.LightBikesClient;

import javax.swing.*;
import java.awt.*;

/**
 * Grid.java
 * <p>
 * Assignment: Final Project <p>
 * Class: Rochester Institute of Technology, ISTE-121.01, 2155 <p>
 * Professor: Michael Floeser <p>
 *
 * <code>Grid</code> is used as the visible grid part of the GUI that the game
 * is played on. <code>Bike</code> objects created by the <code>Grid</code> move
 * across the visible grid once the game is started by calling the <code>
 * startGame</code> method. <code>Grid</code> also establishes communication
 * with the <code>GameServer</code> by creating an instance of
 * <code>NetworkConnector</code>.
 *
 * @author Felice Aprile
 * @author Justin W. Flory
 * @author Malcolm Jones
 * @author Timothy Endersby
 * @version 2016.04.11.v1
 */
public class Grid extends JPanel {

    /**
     * Height used as a constant in calculating the size of the visible GUI and
     * the grid array.
     */
    private final int GRID_HEIGHT = 100;

    /**
     * Width used as a constant in calculating the size of the visible GUI and
     * the grid array.
     */
    private final int GRID_WIDTH = 100;

    /**
     * Two-dimensional grid to keep track of which bikes have "claimed" which
     * spots on the visible grid.
     */
    private int[][] grid = new int[GRID_WIDTH][GRID_HEIGHT];

    /**
     * Number of pixels that make up the width of the graphical grid.
     */
    private final int WIDTH = GRID_WIDTH * 5;

    /**
     * Number of pixels that make up the height of the graphical grid.
     */
    private final int HEIGHT = GRID_HEIGHT * 5;

    /**
     * Color for player 1's bike trail.
     */
    private final Color PLAYER1 = Color.blue;

    /**
     * Color for player 2's bike trail.
     */
    private final Color PLAYER2 = Color.red;

    /**
     * Bike object of player 1.
     */
    private Bike bike1;

    /**
     * Bike object of player 2.
     */
    private Bike bike2;

    /**
     * Bike object that the user of this client is controlling.
     */
    private Bike controlledBike;

    /**
     * Bike object that the remote player is controlling (through the server).
     */
    private Bike serverBike;

    /**
     * Network communications object that the <code>Grid</code> and <code>Bike
     * </code> instances use for communicating with the <code>GameServer</code>.
     */
    private NetworkConnector connector;

    /**
     * Color of the bike that this client is in control of
     */
    private Color userColor;

    /**
     * Creates a new instance of <code>Grid</code> and creates the container for
     * the visible gameplay grid.
     */
    public Grid() {
        setPreferredSize(new Dimension(WIDTH + 1, HEIGHT + 1));//Plus one to assure the edge line is shown

        //Set everything to 0
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                grid[x][y] = 0;
            }
        }
        userColor = UIManager.getColor("Panel.background");
    }

    /**
     * Adds bikes to grid and lets game begin. (Used by NetworkConnector)
     * @param controlled Id of the bike this user is controlling
     */
    public void startGame(int controlled) {
        try {
            Thread.sleep(1000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        bike1 = new Bike(25, 75, grid, 1, Bike.DIRECTION_EAST, this);
        bike2 = new Bike(75, 25, grid, 2, Bike.DIRECTION_WEST, this);

        if(controlled == 1) {
            controlledBike = bike1;
            serverBike = bike2;
        }
        else {
            controlledBike = bike2;
            serverBike = bike1;
        }

        controlledBike.startGame();
    }

    /**
     * Initiates a connection to the Game Server
     * @param  hostname The hostname or IPv4 address of the game server
     * @param  username The username that this user would like to use
     */
    public void connect(String hostname, String username) {
        connector = new NetworkConnector(hostname, username, this);
        System.out.println(connector.getUserID());
        if(connector.getUserID() == 1){
            userColor = PLAYER1;
        }else if(connector.getUserID() == 2){
            userColor = PLAYER2;
        }
        repaint();
    }

    /**
     * Turns the user-controlled bike in the logically north direction.
     */
    public void turnNorth() {
        controlledBike.turnNorth();
    }

    /**
     * Turns the user-controlled bike in the logically east direction.
     */
    public void turnEast() {
        controlledBike.turnEast();
    }

    /**
     * Turns the user-controlled bike in the logically south direction.
     */
    public void turnSouth() {
        controlledBike.turnSouth();
    }

    /**
     * Turns the user-controlled bike in the logically west direction.
     */
    public void turnWest() {
        controlledBike.turnWest();
    }

    /**
     * Signals the user-controlled bike to stop moving.
     */
    public void stop(){
    	controlledBike.stop();
    }

    /**
     * Creates a popup message dialog to let the user know they won.
     */
    public void won(){
		JOptionPane.showMessageDialog(this, "You Win!");
	}

    /**
     * Creates a popup message dialog to let the user know they lost.
     */
	public void lost(){
		JOptionPane.showMessageDialog(this, "You Lost :/");
	}


    /**
     * Returns the <code>Bike</code> object that is being controlled by the
     * remote player. <p>
     * This method is utilized by <code>NetworkConnector</code> to manipulate the
     * <code>Bike</code> instance that is server-controlled.
     * @return Bike object that this client is not controlling
     */
    public Bike getServerBike() {
        return serverBike;
    }

    /**
     * Returns the <code>NetworkConnector</code> instance created by the <code>
     * connect</code> method for communication with the <code>GameServer</code>.
     * @return The <code>NetworkConnector</code> instance created by this object.
     */
    public NetworkConnector getConnector() {
        return connector;
    }

    /**
     * Paints the visible grid on the screen based on the different values in the
     * <code>grid</code> array.
     * @param g Graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //EDGES
        g.drawLine(0, 0, 0, HEIGHT);//left side
        g.drawLine(WIDTH, 0, WIDTH, HEIGHT);//right side
        g.drawLine(0, 0, WIDTH, 0);//top
        g.drawLine(0, HEIGHT, WIDTH, HEIGHT);//bottom

        //Draw snakes on screen here
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                if (grid[x][y] != 0) {
                    if (grid[x][y] == 1) {
                        g.setColor(PLAYER1);
                    } else if (grid[x][y] == 2) {
                        g.setColor(PLAYER2);
                    }
                    g.fillRect(x * 5, y * 5, 5, 5);
                }
            }
        }
        g.setColor(userColor);
        g.fillRect(0, 501, 501, 505);
    }
}
