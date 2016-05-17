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
import java.net.Socket;
import java.net.UnknownHostException;

/**
* ChatClient.java <p>
*
* Assignment: Final Project <p>
* Class: Rochester Institute of Technology, ISTE-121.01, 2155 <p>
* Professor: Michael Floeser <p>
*
* Creates a client for interacting with chat services to accompany the game.
* The chat directly integrates with the GUI and is instantiated by the
* <code>LightBikes</code> class.
*
* @author Felice Aprile
* @author Justin W. Flory
* @author Malcolm Jones
* @author Timothy Endersby
* @version 2016.04.11.v1
*/
public class ChatClient {

    /**
     * Reads messages from server
     */
    private BufferedReader br;

    /**
     * Displays chat in GUI
     */
    private JTextArea chat;

    /**
     * Text field for user to enter new message into
     */
    private JTextField newMsg;

    /**
     * Sends messages to server
     */
    private PrintWriter pw;

    /**
     * Socket connection to server
     */
    private Socket sock;

    /**
     * Users username for chat
     */
    private String username;

    /**
     *	Creates a new client for chat services.
     *	@param chat      The <code>JTextArea</code> to post new messages from
     *	                 other players to.
     *	@param newMsg    The initial message to send to the server.
     */
    public ChatClient(JTextArea chat, JTextField newMsg) {
        username = "";
        this.chat = chat;
        this.newMsg = newMsg;

        newMsg.addActionListener(actionEvent -> {
            if(!newMsg.getText().equals("")) {//make sure user can't send empty message
                send(username + ": " + newMsg.getText());
                newMsg.setText("");
            }
        });
    }

    /**
     * Initial connection to the chat server. This command is to be called by
     * the menu bar item to initialize the chat server connection. This method
     * helps ensure that the client successfully connects to the chat server even
     * when it may not be running.
     *
     * @param host      The hostname of the chat server.
     * @param username  The desired username to use for chatting.
     * @return True on a successful connection, false otherwise.
     */
    public boolean connect(String host, String username) {
        this.username = username;
        try {
            sock = new Socket(host, 6667);
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

            newMsg.setEnabled(true);

            ReceiveMessages recv = new ReceiveMessages();
            recv.start();

            return true;
        } catch (UnknownHostException e) {
            chat.append("Cannot find chat server with specified IP and/or port. Is a firewall running?\n");
            return false;
        } catch (IOException e) {
            chat.append("Unable to connect to chat server. Is the server running?\n");
            return false;
        }
    }

    /**
     * Sends a message to the chat server.
     * @param msg The message to send.
     */
    public void send(String msg) {
        pw.println(msg);
        pw.flush();
    }

    /**
     * Closes the socket connection with the chat server as well as the different
     * datastreams associated with it.
     */
    public void closeSocket() {
        try {
            pw.println("s7XUH94y");
            pw.flush();
            br.close();
            pw.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runnable inner class that listens for messages from the chat server.
     */
    class ReceiveMessages extends Thread {

        /**
         * Runs the listener, running indefinitely until the connection is closed
         * by the client or the server.
         */
        public void run() {
            while (true) {
                try {
                    chat.append(br.readLine() + "\n");
                } catch (IOException e) {
                    break;
                }
            }
        }
    }
}
