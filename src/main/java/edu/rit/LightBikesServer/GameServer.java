/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package edu.rit.LightBikesServer;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Vector;


/**
 * GameServer.java <p>
 *
 * Assignment: Final Project <p>
 * Class: Rochester Institute of Technology, ISTE-121.01, 2155 <p>
 * Professor: Michael Floeser <p>
 *
 * Main game server to facilitate multiplayer <code>LightBikes</code> games.
 * Each <code>GameServer</code> instance represents one game and.
 *
 * @author Felice Aprile
 * @author Justin W. Flory
 * @author Malcolm Jones
 * @author Timothy Endersby
 * @version 2016.04.11.v1
 */
public class GameServer extends JFrame {
    /**
     * Constant to define the port used for gameplay. This port should be the
     * same as the one configured in <code>LightBikesClient.NetworkConnector</code>
     * class.
     */
    private static final int PORT = 8888;

    /**
     * Constant to define the max number of players allowed per game.
     */
    public static final int MAX_CLIENTS = 2;

    private boolean acceptingPlayers = true;
    /**
     * <code>Vector</code> to keep track of connected players.
     */
    private Vector<Player> players = new Vector<Player>();

    /**
     * Main method to kickstart the <code>GameServer</code>.
     * @param args Unused
     */
    public static void main (String[] args) {
        new GameServer();

    }

    /**
     * Builds GUI
     * Connects to players
     */
    public GameServer() {
        System.out.println("Game server test");
        setLayout(new BorderLayout());

        //Add Labels to top
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

        //Add Text outputs to center
        JPanel centerTexts = new JPanel(new GridLayout(0, 3));

        JTextArea player1 = new JTextArea(35, 25);
        JScrollPane scroll1 = new JScrollPane(player1);
        DefaultCaret caret1 = (DefaultCaret) player1.getCaret();
        caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        player1.setEditable(false);
        centerTexts.add(scroll1);

        JTextArea player2 = new JTextArea(35, 25);
        JScrollPane scroll2 = new JScrollPane(player2);
        DefaultCaret caret2 = (DefaultCaret) player2.getCaret();
        caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        player2.setEditable(false);
        centerTexts.add(scroll2);

        JTextArea chat = new JTextArea(35, 25);
        JScrollPane scrollChat = new JScrollPane(chat);
        DefaultCaret caretChat = (DefaultCaret) chat.getCaret();
        caretChat.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        chat.setEditable(false);
        centerTexts.add(scrollChat);
        add(centerTexts, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setTitle("Light Bikes Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        new Thread(new ChatServer(chat)).start();

        //Connect players to server
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
                    player2.append("Waiting for client connections...\n");
                } else if(players.size() == 1)  {
                    player2.append("Caught one - " + s + "\n");
                    temp = new Player(s, players.size() + 1, this, player2);
                }
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
