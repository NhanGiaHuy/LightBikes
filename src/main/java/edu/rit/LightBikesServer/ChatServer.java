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
 * ChatServer.java <p>
 *
 * Assignment: Final Project <p>
 * Class: Rochester Institute of Technology, ISTE-121.01, 2155 <p>
 * Professor: Michael Floeser <p>
 *
 * Creates a runnable chat server to process and push chat messages sent from
 * connected clients. <p>
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

    /**
     * Creates a new <code>ChatServer</code> instance.
     * @param  chat The <code>JTextArea</code> to append chat messages to.
     */
    public ChatServer(JTextArea chat) {
        this.chat = chat;
    }

    /**
     * Starts the chat server and listens for client connections. The server runs
     * until the thread is interrupted, creating a separate threaded
     * <code>ConnectedClient</code> instance for each client that connects.
     */
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
            chat.append("Error creating server socket\n");
        }

        // Create the socket and wait for a client to connect; once connected, run on its own thread
        try {
            while (true) {
                chat.append("Waiting for a client.\n");
                Socket sock = srvSock.accept();
                chat.append("Client found:\n" + sock + "\n");
                clients.add(new ConnectedClient(sock, chat));
                clients.get(clientCounter).start();
                clientCounter++;
            }
        } catch (IOException e) {
            chat.append("Error with server socket connections\n");
            try {
                srvSock.close();
            } catch (IOException e1) {
                chat.append("Error closing server socket\n");
            }
        }

    }

    /**
     *	Threaded inner class to handle each client connected to the main
     *	<code>ChatServer</code>.
     */
    class ConnectedClient extends Thread {

        /**
         * Reads messages from clients
         */
        private BufferedReader br = null;

        /**
         * Sends messages to clients
         */
        private PrintWriter pw = null;

        /**
         * Connection to client
         */
        private Socket sock;

        /**
         * Message to send to client
         */
        private String msg = "";

        /**
         * Displays chat on server GUI
         */
        private JTextArea chat;

        /**
         * Creates a new <code>ConnectedClient</code> and opens in/out buffers
         * in order to send/receive data from the client.
         * @param sock The socket the client is connected to.
         * @param chat The <code>JTextArea</code> to output chat messages to.
         */
        public ConnectedClient(Socket sock, JTextArea chat) {
            this.sock = sock;
            this.chat = chat;
            try {
                br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            } catch (IOException e) {
                chat.append("IOException BufferedReader/Print Writer\n");
            }
        }

        /**
         * Sends a message to the client associated with this
         * <code>ConnectedClient</code> object.
         *
         * @param msg The message to be sent.
         */
        public void send(String msg) {
            pw.println(msg);
            pw.flush();
        }

        /**
         * Writes a message out to the server GUI window.
         * @param msg The message to be written.
         */
        public void addToChatWindow(String msg) {
            chat.append(msg + "\n");
        }

        /**
         * Sends a message to all clients connected to the <code>ChatServer</code>.
         *
         * @param msg The message to send.
         */
        public void sendToAll(String msg) {
            for (ConnectedClient cc : clients) {
                cc.send(msg);
            }
        }

        /**
         * Runs a listener to read messages from the associated client, forwarding
         * them and printing them out as appropriate. As <code>ConnectedClient</code>
         * is threaded, it is not recommended to call this <code>run</code> method
         * directly, instead using the <code>Thread.start</code> method.
         */
        @Override
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
                    chat.append("OptionalDataException occurred.\n");
                } catch (IOException e) {
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
                chat.append("IOException closing connection\n");
            }
        }
    }
}
