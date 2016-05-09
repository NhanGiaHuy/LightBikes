/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package edu.rit.LightBikesServer;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.Scanner;


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
public class GameServer extends Application {
    private static final int PORT = 8888;
    private Vector<Lobby> lobbies = new Vector<Lobby>();

    public static void main (String[] args) {
        launch(args);
        new GameServer();
    }

    public GameServer() {
        ServerSocket ss = null;
        Socket s = null;
        try {
            ss = new ServerSocket(PORT);
            System.out.println("Waiting for client connections...");
            while (true) {
                s = ss.accept();
                System.out.println("Caught one - " + s);
                //Player temp = new Player(s);
                new Thread(new Player(s, lobbies)).start();
            }
        }
        catch (IOException ioe) {
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //redo JavaFX here
    }
}
