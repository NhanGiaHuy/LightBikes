/*
* Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package edu.rit.LightBikesClient;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
* NetworkConnector.java <p>
*
* Assignment: Final Project <p>
* Class: Rochester Institute of Technology, ISTE-121.01, 2155 <p>
* Professor: Michael Floeser <p>
*
* This class serves to provide network communication between the
* <code>LightBikes</code> client and <code>LightBikes</code> server. A flexible,
*  extendable API is provided in order to send and process different commands
*  and data between the clients and server. <code>NetworkConnector</code> also
*  creates a threaded <code>Listener</code> object which serves to simply read
*  and process data sent by the server.
*
* @author Felice Aprile
* @author Justin W. Flory
* @author Malcolm Jones
* @author Timothy Endersby
* @version 2016.04.11.v1
*/
public class NetworkConnector {

    /**
     * Constant to define what port to connect to the <code>GameServer</code>.
     */
    private static final int PORT = 8888;

    private String hostname;
    private String username;
    private int userID = -1;
    private Socket s;
    private PrintWriter out;
    private String[] otherPlayers;
    private Grid grid;

    /**
    * Creates a new <code>NetworkConnector</code> instance and connects to the
    * to the <code>LightBikes</code> server specified with <code>hostname</code>.
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
    * Connects to the <code>LightBikes</code> server with <code>username</code>
    * and <code>hostname</code> values set in the constructor on the port
    * defined by the <code>PORT</code> constant.
    */
    public void connect() {
        try {
            s = new Socket(hostname, PORT);
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            new Thread(new Listener(s)).start();
            Thread.sleep(1000);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to server, restart program");
            System.exit(0);
        }
        sendUsername(username);
    }

    /**
    * Register a change of <code>username</code> with the server.
    * @param username The username to change to.
    */
    public void sendUsername(String username) {
        sendCommand("set-username", username);
    }


    /**
    * Send the current x,y position of the user-controlled bike.
    * @param x The X-coordinate of the bike.
    * @param y The Y-coordinate of the bike.
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
    private void send(String commandString) {;
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
    * Creates and sends a command-value string to the <code>LightBikes</code> server.
    * @param  command  The command to send
    * @param  value    The value to send
    */
    public void sendCommand(String command, String value) {
        send(makeCommandString(command, value));
    }

    /**
    * Creates and sends a command-value string to the <code>LightBikes</code> server.
    * @param  command  The command to send
    * @param  value    The value to send
    */
    public void sendCommand(String command, int value) {
        send(makeCommandString(command, "" + value));
    }

    /**
    * Generates a properly formatted command-value string to be sent to the
    * <code>LightBikes</code> server.
    * @param   command The command to be contained in the command string
    * @param   value   The value to be contained in the command string
    * @return  Generated command string
    */
    private String makeCommandString(String command, String value) {
        return command + ":" + value + ";";
    }

    /**
    * Process a command-value string sent back by the <code>LightBikes</code>
    * server.
    * @param cmdString The command-value string to be processed.
    */
    public void processCommand(String cmdString) {
        String[] temp = cmdString.split(":");
        String command = temp[0];
        String value = temp[1];

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
    * Calls the <code>startGame</code> method within the <code>Grid</code>
    * object. UserID must previously set via a call to the <code>setUserID</code>
    * method.
    * @param value Unused.
    */
    private void startGame(String value) {
        grid.startGame(userID);
    }

    /**
    * Process the list of usernames sent by the <code>LightBikes</code> server
    * and store them for later use.
    * @param value A string of comma-serparated usenames.
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

    /**
     * Sets the user ID of this <code>Bike</code> object. The user ID is used to
     * determine the color and starting location of a <code>Bike</code> within
     * the <code>Grid</code>.
     * @param value The userID to set
     */
    private void setUserID(String value) {
        userID = Integer.parseInt(value);
    }

    /**
     * Get the userID value of this <code>Bike</code> object. The userID is set
     * by default to -1 until set by the server.
     * @return The userID assigned by the server.
     */
    public int getUserID(){
    	return userID;
    }

    /**
     * Runnable inner class which simply opens the <code>Socket</code>'s
     * <code>InputStream</code> and waits for the server to send data, processing
     * it as it comes.
     */
    class Listener implements Runnable {

        private Scanner scan;
        private Socket s;

        /**
        * Creates a listener bound to a port
        * @param  s The socket open with the server
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
         * Core method of the <code>Listener</code> object, waiting
         * for data to be received from the server. Once opened, it will run for
         * the life of the connection unless the thread is killed. <p>
         * This method should not be called directly, as it is intended to run
         * as a separate thread.
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
