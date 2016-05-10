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

     private String username;
     private Lobby lobby;
     private GameServer gameServer;
     private PrintWriter out;
     private Socket s;

     /**
      * Create a new subserver for a Player
      * @param  s The socket the client is connected to
      * @return   Nothing
      */
     public Player(Socket s, GameServer gameServer) {
         this.s = s;
         this.gameServer = gameServer;
     }

     /**
      * Don't call directly -- use thread.start()
      * Initializes I/O for the client connection and rolling out the welcome wagon
      */
     public void run() {
         try {
             new Thread(new Listener(s, this)).start();
             out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
         }
         catch (Exception e) {
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

     public void pushToAll(String cmdString) {
         lobby.pushToAll(cmdString);
     }

     public void pushToAll(String command, String value) {
         lobby.pushToAll(makeCommandString(command, value));
     }

     public void pushToAll(String command, String value, Boolean signed) {
         lobby.pushToAll(makeCommandString(command, (username + "," + value)));
     }

     public String makeCommandString(String command, String value) {
         return command + ":" + value + ";";
     }

     public void processCommand(String cmdString) {
         String[] temp = cmdString.split(":");
         String command = temp[0];
         String value = temp[1];

         switch (command) {
             case "set-lobby":
                 setLobby(value);
                 break;

             case "set-username":
                 setUsername(value);
                 break;

             case "set-direction":
                 pushToAll("rsp-direction", value, true);
                 break;

             case "set-location":
                 pushToAll("rsp-location", value, true);
                 break;

             case "get-game-master":
                 push("rsp-game-master", lobby.getGameMaster());
                 break;

             case "cmd-game-start":
                 startGame();
                 break;
         }
     }

     public void setLobby(String lobbyName) {
         int lobbyNum = -1;
         for (int i = 0, found = 0; i < lobbies.size() && found == 0; i++) {
             if (lobbyName.equals(lobbies.get(i).getLobbyName())) {
                 found = 1;
                 lobbyNum = i;
             }
         }
         if (lobbyNum >= 0) {
             lobby = lobbies.get(lobbyNum);
         }
         else {
             lobby = new Lobby(lobbyName);
         }
         lobby.addPlayer(this);
     }

     public String getLobby() {
         return lobby.getLobbyName();
     }

     public void setUsername(String username) {
         this.username = username;
     }

     public String getUsername() {
         return username;
     }

     public void startGame() {
         lobby.startGame();
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
             while (true) {
                 String line = scan.nextLine();
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
