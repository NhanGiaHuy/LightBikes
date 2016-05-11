/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package edu.rit.LightBikesServer;

import javax.swing.*;
import java.awt.*;
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

        setLayout(new BorderLayout());

        JPanel topLables = new JPanel(new GridLayout(0, 3));

        JLabel player1Lable = new JLabel("Player 1");
        player1Lable.setHorizontalAlignment(JLabel.CENTER);
        topLables.add(player1Lable);

        JLabel player2Lable = new JLabel("Player 2");
        player2Lable.setHorizontalAlignment(JLabel.CENTER);
        topLables.add(player2Lable);

        JLabel chatLable = new JLabel("Chat");
        chatLable.setHorizontalAlignment(JLabel.CENTER);
        topLables.add(chatLable);

        add(topLables, BorderLayout.NORTH);

        JPanel centerTexts = new JPanel(new GridLayout(0, 3));

        JTextArea player1 = new JTextArea(25, 15);

        JScrollPane scroll = new JScrollPane(player1);
        //scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        player1.setEditable(false);
        //centerTexts.add(player1);
        centerTexts.add(scroll);

        JTextArea player2 = new JTextArea(25, 15);
        player2.setEditable(false);
        centerTexts.add(player2);

        JTextArea chat = new JTextArea(25, 15);
        chat.setEditable(false);
        centerTexts.add(chat);
        add(centerTexts, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setTitle("Light Bikes Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        ServerSocket ss = null;
        Socket s = null;
        try {
            ss = new ServerSocket(PORT);
            player1.append("Waiting for client connections...\n");
            while (acceptingPlayers) {
                s = ss.accept();
                Player temp = null;
                if(players.size() == 0) {
                    player1.append("Caught one - " + s + "\n");
                    temp = new Player(s, players.size() + 1, this, player1);
                } else if(players.size() == 1)  {
                    player2.append("Caught one - " + s + "\n");
                    temp = new Player(s, players.size() + 1, this, player2);
                }
                players.add(temp);
                new Thread(temp).start();
                acceptingPlayers = players.size() < MAX_CLIENTS;
                player2.append("Waiting for client connections...");
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
