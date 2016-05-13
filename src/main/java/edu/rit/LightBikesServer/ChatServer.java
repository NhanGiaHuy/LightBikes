/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package edu.rit.LightBikesServer;

import java.io.*;
import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * ChatServer.java
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
public class ChatServer implements Runnable {

    // Attributes
    private int clientCounter = 0;
    private ArrayList<ConnectedClient> clients;
    private JTextArea chat;

    public ChatServer(JTextArea chat) {
        this.chat = chat;
    }

    @Override
    public void run() {

        // Local variables
        ServerSocket srvSock = null;

        // Initialize attributes
        clients = new ArrayList<>();

        // Create the server socket
        try {
            srvSock = new ServerSocket(6667);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the socket and wait for a client to connect; once connected, run on its own thread
        try {
            while (true) {
<<<<<<< HEAD
                System.out.println("Waiting for a client.");

=======
                chatText.append("Waiting for a client.");
>>>>>>> a7f602137ce1961fa55c3fbec4be39188147e6e3
                Socket sock = srvSock.accept();
                System.out.println("Client found. " + sock);
                clients.add(new ConnectedClient(sock, chat));

                clients.get(clientCounter).start();
                clientCounter++;
            }
        } catch (IOException e) {
            try {
                srvSock.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    /**
     *
     */
    class ConnectedClient extends Thread {

        // Attributes
        private int count = 0;
        private BufferedReader br = null;
        private PrintWriter pw = null;
        private Socket sock;
        private String msg = "";
        private JTextArea chat;

        /**
         *
         *
         * @param sock
         */
        public ConnectedClient(Socket sock, JTextArea chat) {
            this.sock = sock;
            this.chat = chat;
            try {
                br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            } catch (IOException e) {
                System.out.println("An error occurred while opening the server socket.");
                e.printStackTrace();
            }
        }

        /**
         *
         *
         * @param msg
         */
        public void send(String msg) {
            pw.println(msg);
            pw.flush();
        }

        public void addToChatWindow(String msg) {
            chat.append(msg);
        }

        /**
         *
         *
         * @param msg
         */
        public void sendToAll(String msg) {
            for (ConnectedClient cc : clients) {
                cc.send(msg);
            }
        }

        /**
         *
         */
        public void run() {
            while (!msg.equalsIgnoreCase(null)) {
                try {

                    // Initialize attributes
                    br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

                    // Read from client
                    msg = br.readLine();
                    sendToAll(msg);
                    // Show message in the chat window
                    addToChatWindow(msg);

                } catch (OptionalDataException e) {
                    System.out.println("OptionalDataException occurred.");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            sendToAll("A user has disconnected.");
            clients.remove(this);

            // Close all readers, writers, sockets
            try {
                br.close();
                pw.close();
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
