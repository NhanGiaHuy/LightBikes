/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package edu.rit.LightBikesClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    /**
     *
     */
    public ChatClient(JTextArea chat, JTextField newMsg) {
        this.chat = chat;
        this.newMsg = newMsg;

        newMsg.addActionListener(actionEvent -> {
            send(newMsg.getText());
            newMsg.setText("");
        });
    }

    public void connect() {
        try {
            sock = new Socket("localhost", 6667);
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

            newMsg.setEnabled(true);

            ReceiveMessages recv = new ReceiveMessages();
            recv.start();
        } catch (UnknownHostException e) {
            chat.append("Cannot find server with specified IP and/or port. Is a firewall running?\n");
        } catch (IOException e) {
            chat.append("Unable to connect. Is the server running?\n");
        }
    }

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
                    System.out.println("Client closed.");
                    break;
                }
            }
        }
    }
}