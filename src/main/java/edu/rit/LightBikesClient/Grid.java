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
 * Assignment: Final Project
 * Class: Rochester Institute of Technology, ISTE-121.01, 2155
 * Professor: Michael Floeser
 * <p>
 * This class is used as the grid GUI that the game is played on. The bike objects will move across the grid as they
 * leave the wall of light.
 *
 * @author Felice Aprile
 * @author Justin W. Flory
 * @author Malcolm Jones
 * @author Timothy Endersby
 * @version 2016.04.11.v1
 */
public class Grid extends JPanel {

    private final int GRID_HEIGHT = 100;
    private final int GRID_WIDTH = 100;
    private final int DELAY_IN_MILLS = 100;
    private int[][] grid = new int[GRID_WIDTH][GRID_HEIGHT];
    private final int WIDTH = GRID_WIDTH * 5;
    private final int HEIGHT = GRID_HEIGHT * 5;
    private int i = 0;

    //Colors of lines (maybe set as a user setting?)
    private final Color PLAYER1 = Color.blue;
    private final Color PLAYER2 = Color.red;

    private Bike bike1;
    private Bike bike2;
    private Bike controlledBike;
    private Bike serverBike;

    private boolean bike1GameState;
    private boolean bike2GameState;

    private NetworkConnector connector;

    public Grid() {
        setPreferredSize(new Dimension(WIDTH + 1, HEIGHT + 1));//Plus one to assure the edge line is shown

        //Set everything to 0
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                grid[x][y] = 0;
            }
        }
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
    }

    public void turnNorth() {
        controlledBike.turnNorth();
    }

    public void turnEast() {
        controlledBike.turnEast();
    }

    public void turnSouth() {
        controlledBike.turnSouth();
    }

    public void turnWest() {
        controlledBike.turnWest();
    }

    public void stop(){
    	controlledBike.stop();
    }

    public void won() {
        controlledBike.won();
    }

    public void lost() {
        controlledBike.lost();
    }

    /**
     * Returns the other player's bike (Used by Network Connector).
     * @return Bike object that this client is not controlling
     */
    public Bike getServerBike() {
        return serverBike;
    }

    public NetworkConnector getConnector() {
        return connector;
    }

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
    }
}
