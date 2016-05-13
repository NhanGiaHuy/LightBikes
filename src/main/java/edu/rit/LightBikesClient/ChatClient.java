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
 * ChatClient.java
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
public class ChatClient {

    // Attributes
    private BufferedReader br;
    private JTextArea chat;
    private JTextField newMsg;
    private PrintWriter pw;
    private Socket sock;
    private String username;

    /**
     *
     */
    public ChatClient(JTextArea chat, JTextField newMsg) {
        username = "";
        this.chat = chat;
        chat.setEditable(false);
        this.newMsg = newMsg;

        newMsg.addActionListener(actionEvent -> {
            send(username + ": " + newMsg.getText());
            newMsg.setText("");
        });
    }

    /**
     * Initial connection to the chat server. This command is to be called by the menu bar item to initialize the
     * chat server connection. This method helps ensure that the client successfully connects to the chat server even
     * when it may not be running.
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
     *
     * @param msg
     */
    public void send(String msg) {
        pw.println(msg);
        pw.flush();
    }

    /**
     *
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

    class ReceiveMessages extends Thread {
        public void run() {
            while (true) {
                try {
                    chat.append(br.readLine() + "\n");
                } catch (IOException e) {
                    //tem.out.println("Client closed.");
                    break;
                }
            }
        }
    }
}