/*
* Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package edu.rit.LightBikesServer;

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.Scanner;


/**
* Player.java
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

public class Player implements Runnable {

    private String username = "None";
    private int playerID;
    private GameServer gameServer;
    private PrintWriter out;
    private Socket s;

    /**
    * Create a new subserver for a Player
    * @param  s The socket the client is connected to
    * @return   Nothing
    */
    public Player(Socket s, int playerID, GameServer gameServer) {
        this.s = s;
        this.playerID = playerID;
        this.gameServer = gameServer;
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
        System.out.println("Debug: sending player id: " + playerID + "to " + s);
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

    public void push(String command, String value) {
        push(makeCommandString(command, value));
    }

    private void pushToOthers(String command, String value) {
        gameServer.pushToOthers(playerID, makeCommandString(command, value));
    }

    private void pushToAll(String command, String value) {
        gameServer.pushToAll(makeCommandString(command, value));
    }

    private String makeCommandString(String command, String value) {
        return command + ":" + value + ";";
    }

    public void processCommand(String cmdString) {
        String[] temp = cmdString.split(":");
        String command = temp[0];
        String value = temp[1];

        System.out.println("Processing " + cmdString);

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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void sendPlayerID() {
        push("rsp-user-id", ""+playerID);
    }

}

/**
* Runnable Listener listens and processes messages from a client
*/
class Listener implements Runnable {

    Scanner scan;
    Socket s;
    Player p;

    /**
    * Creates new listener for a client application
    * @param  s The socket the client is bound to
    * @return   Nothing
    */
    public Listener(Socket s, Player p) {
        System.out.println("Listener started for " + s);
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
    * Don't call directly -- use thread.start()
    * Listens for messages until the client disconnects.
    */
    public void run() {
        try {
            System.out.println("Listener starting...");
            while (true) {
                System.out.println("Waiting for stuffs from client");
                String line = scan.nextLine();
                System.out.println("Parsing " + line);
                parseCommands(line);
            }
        }
        catch (Exception e) {
            System.out.println("Connection to client " + s + " lost.");
        }
    }

    private void parseCommands(String line) {
        String[] commands = line.split(";");
        for (String command : commands) {
            p.processCommand(command);
        }
    }
}
