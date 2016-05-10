/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package edu.rit.LightBikesClient;

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * NetworkConnector.java
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
public class NetworkConnector {

    //Directional constants
    public static final int DIRECTION_NORTH = 0;
    public static final int DIRECTION_EAST = 1;
    public static final int DIRECTION_SOUTH = 2;
    public static final int DIRECTION_WEST = 3;

    private static final int PORT = 8888;
    private String hostname;
    private String username;
    private int userID;
    private Socket s;
    private PrintWriter out;
    private String[] otherPlayers;
    private Grid grid;

    /**
     * Creates a new connection to the server
     * @return Nothing
     */
    public NetworkConnector(String hostname, String username, Grid grid) {
        this.username = username;
        this.hostname = hostname;
        this.grid = grid;
        connect();
    }

    /**
     * Connect to the server
     */
    public void connect() {
        try {
            s = new Socket(hostname, PORT);
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            new Thread(new Listener(s)).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        send(makeCommandString("set-username", username));
    }

    /**
     * Sends a properly formatted command string. It is recommended to use
     * sendCommand() instead as it will format the string correctly for you.
     * @param commandString The command string to send.
     */
    public void send(String commandString) {
        out.println(commandString);
    }

    /**
     * Starts the game within the Grid object
     */
    public void startGame(int controlled) {
        grid.startGame(controlled);
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Send the current x,y position of the bike.
     * @param x The X-coordinate of the bike
     * @param y The Y-coordinate of the bike
     */
    public void sendLocation(int x, int y) {
        sendCommand("set-location", ("" + x + "," + y));
    }

    /**
     * Creates and sends a command string to server.
     * @param  command  The command to send
     * @param  value    The value to send
     */
    public void sendCommand(String command, String value) {
        send(makeCommandString(command, value));
    }

    public void sendCommand(String command, int value) {
        send(makeCommandString(command, "" + value));
    }

    public String makeCommandString(String command, String value) {
        return command + ":" + value + ";";
    }

    public void notifyDeath() {
        sendCommand("dead","true");
    }

    /**
     * Process a command string sent back by the server.
     * @param cmdString The command string to be processed
     */
    public void processCommand(String cmdString) {
        String[] temp = cmdString.split(":");
        String command = temp[0];
        String value = temp[1];

        switch (command) {
            case "resp-user-id":
                setUserID(Integer.parseInt(value));
                break;
            case "resp-username-list":
                setPlayers(value);
                break;
        }
    }

    private void setPlayers(String csvUsers) {
        otherPlayers = csvUsers.split(",");
    }

    class Listener implements Runnable {

        Scanner scan;
        Socket s;

        /**
         * Creates a listener bound to a port
         * @param  s The socket open with the server
         * @return   Nothing
         */
        public Listener(Socket s) {
            this.s = s;
            try {
                scan = new Scanner(s.getInputStream());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Don't call directly -- use threadObj.start()
         * Runs for the life of the connection, waiting to recieve messages
         */
        public void run() {
            try {
                while (true) {
                    String line = scan.nextLine();
                    parseCommands(line);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void parseCommands(String line) {
            String[] commands = line.split(";");
            for (String command : commands) {
                processCommand(command);
            }
        }
    }
}
