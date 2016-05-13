/*
* Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package edu.rit.LightBikesServer;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;


/**
* Player.java <p>
*
* Assignment: Final Project <p>
* Class: Rochester Institute of Technology, ISTE-121.01, 2155 <p>
* Professor: Michael Floeser <p>
*
* Runnable class for each client connected to the <code>GameServer</code>.
* <code>Player</code> includes a threaded <code>Listener</code> inner class to
* process data sent by its assigned client.
*
* @author Felice Aprile
* @author Justin W. Flory
* @author Malcolm Jones
* @author Timothy Endersby
* @version 2016.04.11.v1
*/

public class Player implements Runnable {

    private String username = "None";
    private int playerID;
    private GameServer gameServer;
    private PrintWriter out;
    private Socket s;
    public JTextArea playerOutput;

    /**
    * Create a new subserver for a Player
    * @param  s The socket the client is connected to
    */
    public Player(Socket s, int playerID, GameServer gameServer, JTextArea playerOutput) {
        this.s = s;
        this.playerID = playerID;
        this.gameServer = gameServer;
        this.playerOutput = playerOutput;
    }

    /**
    * Don't call directly -- use thread.start()
    * Initializes I/O for the client connection and rolling out the welcome wagon
    */
    public void run() {
        try {
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            new Thread(new Listener(s, this)).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        playerOutput.append("Sending player id: " + playerID + " to " + s + "\n");
        sendPlayerID();

        if (playerID == gameServer.MAX_CLIENTS) {
            gameServer.startGame();
        }
    }

    /**
    * Pushes out a message to the individual client.
    * @param line The message to be sent
    */
    public void push(String line) {
        try {
            if (out == null) {
                out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            }
            out.println(line);
            out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Make and pushes out a command-value string to this client.
    * @param command    The command to use when making the command-value string.
    * @param value      The value to use when making the command-value string.
    */
    public void push(String command, String value) {
        push(makeCommandString(command, value));
    }

    /**
    * Make and pushes out a command-value string to the other clients connected
    * to the <code>GameServer</code>.
    * @param command    The command to use when making the command-value string.
    * @param value      The value to use when making the command-value string.
    */
    private void pushToOthers(String command, String value) {
        gameServer.pushToOthers(playerID, makeCommandString(command, value));
    }

    /**
    * Make and pushes out a command-value string to ALL clients connected
    * to the <code>GameServer</code>.
    * @param command    The command to use when making the command-value string.
    * @param value      The value to use when making the command-value string.
    */
    private void pushToAll(String command, String value) {
        gameServer.pushToAll(makeCommandString(command, value));
    }

    /**
     * Makes a command-value string with the given values.
     * @param   command The command to use.
     * @param   value   The value to use.
     * @return  The generated command-value string.
     */
    private String makeCommandString(String command, String value) {
        return command + ":" + value + ";";
    }

    /**
     * Processes a command-value string sent by the client.
     * @param cmdString The command-value string sent.
     */
    public void processCommand(String cmdString) {
        String[] temp = cmdString.split(":");
        String command = temp[0];
        String value = temp[1];

        playerOutput.append("Processing " + cmdString + "\n");

        switch (command) {

            case "set-username":
            setUsername(value);
            break;

            case "set-location":
            pushToOthers("rsp-update-location", value);
            break;

            case "set-dead":
            pushToOthers("rsp-dead", value);
            break;
        }
    }

    /**
     * Sets the username of this <code>Player</code>.
     * @param username The username desired.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the username of this <code>Player</code>.
     * @returns The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the <code>playerID</code>. Player IDs are assigned by the
     * <code>GameServer</code> on creation of the player object.
     * @return The player id of this <code>Player</code>.
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Pushes the <code>playerID</code> of this <code>Player</code> to the
     * client connected.
     */
    public void sendPlayerID() {
        push("rsp-user-id", ""+playerID);
    }

}

/**
* Runnable <code>Listener</code> listens and processes messages from the client
*/
class Listener implements Runnable {

    Scanner scan;
    Socket s;
    Player p;

    /**
    * Creates new <code>Listener</code> to bind to the <code>Socket</code>
    * and receive all incoming data.
    * @param  s The <code>Socket</code> the client is bound to.
    * @param  p The <code>Player</code> that this <code>Listener</code> is
    * processing commands for.
    */
    public Listener(Socket s, Player p) {
        p.playerOutput.append("Listener started for " + s + "\n");
        this.s = s;
        this.p = p;
        try {
            scan = new Scanner(s.getInputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Starts this <code>Listener</code> and runs for the lifetime of the
    * connection unless the thread is terminated. This method should not be
    * called directly, instead it should be started as a <code>Thread</code>
    * instance of <code>Listener</code>.
    */
    public void run() {
        try {
            while (true) {
                String line = scan.nextLine();
                parseCommands(line);
            }
        }
        catch (Exception e) {
            p.playerOutput.append("Connection to client " + s + " lost.\n");
        }
    }

    /**
     * Parse a string of commands sent by the client into individual
     * command-value strings that can then be processed accordingly.
     * @param line The string of commands sent.
     */
    private void parseCommands(String line) {
        String[] commands = line.split(";");
        for (String command : commands) {
            p.processCommand(command);
        }
    }
}
