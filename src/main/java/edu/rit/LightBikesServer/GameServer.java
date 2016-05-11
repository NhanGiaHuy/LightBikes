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
import java.util.Vector;


/**
 * GameServer.java
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
public class GameServer extends JFrame {
    private static final int PORT = 8888;
    public static final int MAX_CLIENTS = 2;
    private boolean acceptingPlayers = true;
    private Vector<Player> players = new Vector<Player>();

    public static void main (String[] args) {
        new GameServer();
    }

    public GameServer() {
        ServerSocket ss = null;
        Socket s = null;
        try {
            ss = new ServerSocket(PORT);
            System.out.println("Waiting for client connections...");
            while (acceptingPlayers) {
                s = ss.accept();
                System.out.println("Caught one - " + s);
                Player temp = new Player(s, players.size() + 1, this);
                players.add(temp);
                new Thread(temp).start();
                acceptingPlayers = players.size() < MAX_CLIENTS;
            }
        }
        catch (IOException ioe) {
        }
    }

    public void pushToAll(String commandString) {
        for (Player player : players) {
            player.push(commandString);
        }
    }

    public void pushToPlayer(int playerID, String commandString) {
        players.get(playerID-1).push(commandString);
    }

    public void pushToOthers(int playerID, String commandString) {
        for (Player player : players) {
            if (player.getPlayerID() != playerID) {
                player.push(commandString);
            }
        }
    }

    private String makeCommandString(String command, String value) {
        return command + ":" + value + ";";
    }

    public void startGame() {
        pushToAll(makeCommandString("rsp-game-start", "true"));
    }
}
