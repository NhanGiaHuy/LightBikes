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
    * @param hostname   The hostname of the server
    * @param username   The username to send the server
    * @param grid       The grid object to call methods in
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
            System.out.println("Debug: Network Connector connecting to server");
            s = new Socket(hostname, PORT);
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            new Thread(new Listener(s)).start();
            Thread.sleep(1000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Debug: connected, sending username");
        sendUsername(username);
    }

    /**
    * Change this client's username on the server
    * @param username The username to send.
    */
    public void sendUsername(String username) {
        sendCommand("set-username", username);
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
    * Tell the server that the user died
    */
    public void notifyDeath() {
        sendCommand("set-dead","true");
    }

    /**
    * Sends a properly formatted command string. It is recommended to use
    * sendCommand() instead as it will format the string correctly for you.
    * @param commandString The command string to send.
    */
    private void send(String commandString) {
        System.out.println("Sending " + commandString);
        try {
            if (out == null) {
                out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            }
            out.println(commandString);
            out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
    * Creates and sends a command string to server.
    * @param  command  The command to send
    * @param  value    The value to send
    */
    public void sendCommand(String command, String value) {
        send(makeCommandString(command, value));
    }

    /**
    * Creates and sends a command string to server.
    * @param  command  The command to send
    * @param  value    The value to send
    */
    public void sendCommand(String command, int value) {
        send(makeCommandString(command, "" + value));
    }

    /**
    * Generates a properly formatted command string to be sent to the server
    * @param   command The command to be contained in the command string
    * @param   value   The value to be contained in the command string
    * @return  Generated command string
    */
    private String makeCommandString(String command, String value) {
        return command + ":" + value + ";";
    }

    /**
    * Process a command string sent back by the server.
    * @param cmdString The command string to be processed
    */
    public void processCommand(String cmdString) {
        String[] temp = cmdString.split(":");
        String command = temp[0];
        String value = temp[1];

        System.out.println("Processing " + cmdString);

        switch (command) {
            case "rsp-user-id":
            setUserID(value);
            break;

            case "rsp-game-start":
            startGame(value);
            break;

            case "rsp-username-list":
            setPlayers(value);
            break;

            case "rsp-update-location":
            updateLocation(value);
            break;

            case "rsp-dead":
            grid.stop();
            grid.won();
            break;
        }
    }

    // Methods to process different commands from the server

    /**
    * Starts the game within the Grid object. UserID must already set
    */
    private void startGame(String value) {
        System.out.println("Startgame network connector");
        grid.startGame(userID);
    }

    /**
    * Process the list of usernames sent by the server
    * @param csvUsers A string of comma-serparated usenames;
    */
    private void setPlayers(String value) {
        otherPlayers = value.split(",");
    }

    /**
    * Update the location of the remote client's bike in this client.
    * @param value The coordinates of the remote client's bike
    */
    private void updateLocation(String value) {
        String[] pair = value.split(",");
        grid.getServerBike().setLocation(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]));
    }

    private void setUserID(String value) {
        userID = Integer.parseInt(value);
    }
    
    public int getUserID(){
    	return userID;
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
                System.out.println("Lost the server connection");
            }
        }

        /**
         * Parses a string of commands and breaks them into individual commands
         * to be processed.
         * @param line The string of different commands to process
         */
        private void parseCommands(String line) {
            String[] commands = line.split(";");
            for (String command : commands) {
                processCommand(command);
            }
        }
    }
}
