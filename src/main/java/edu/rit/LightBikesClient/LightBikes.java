/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package edu.rit.LightBikesClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LightBikes.java
 *
 * Assignment: Final Project
 * Class: Rochester Institute of Technology, ISTE-121.01, 2155
 * Professor: Michael Floeser
 *
 * This class is the main class for the LightBikes game created for our team's final project in ISTE-121. The game is
 * modeled after the TRON-esque light bikes in the movie and in the arcade. Players control a bike that leaves a
 * "wall of light" behind them as they travel across the game grid. They face an opponent who is also leaving a wall
 * of light behind them. If one player hits the wall of light, they lose and the game ends.
 *
 * This class is the main file and is executed as the game client.
 *
 * This class is responsible for the GUI
 *
 * @author Felice Aprile
 * @author Justin W. Flory
 * @author Malcolm Jones
 * @author Timothy Endersby
 * @version 2016.04.11.v1
 */
public class LightBikes extends JFrame {

    //These are test items, I'm not sure what we need here yet
    private JMenuItem jmiExit;
    private JMenuItem jmiConnect;
    private JMenuItem jmiAbout;
    private Grid gameGrid;

    /**
     * Starts the game
     * @param args Nothing
     */
    public static void main(String[] args){
        //Collect initial settings
        new LightBikes();//possibly send through size of game grid
    }

    /**
     * Builds the GUI
     * Starts game logic classes
     */
    public LightBikes(){
        //Connect to server/start game

        //Add JMenu bar
        JMenuBar menuBar = new JMenuBar();
            JMenu jmFile = new JMenu("File");
                jmiConnect = new JMenuItem("Connect");
            jmFile.add(jmiConnect);
                jmiExit = new JMenuItem("Exit");
            jmFile.add(jmiExit);
        menuBar.add(jmFile);
            JMenu jmHelp = new JMenu("Help");
                jmiAbout = new JMenuItem("About");
            jmHelp.add(jmiAbout);
        menuBar.add(jmHelp);
        add(menuBar, BorderLayout.NORTH);

        //Add chat window to east
        JPanel chatFrame = new JPanel();
        JTextArea chat = new JTextArea(25,60);
        JTextField msg = new JTextField(25);
        ChatClient chatClient = new ChatClient(chat, msg);

        add(chatFrame, BorderLayout.EAST);
        chatFrame.add(chat);
        chatFrame.add(msg);
        
        gameGrid = new Grid();
        add(gameGrid, BorderLayout.CENTER);
       

        //Action listener for Menu items
        ActionListener menuListener = new ActionListener() {
            public void	actionPerformed(ActionEvent ae){

                Object choice = ae.getSource();

                if(choice == jmiConnect){//Connect to server
                    chatClient.connect();//Chat server connection
                }else if(choice == jmiExit){
                    System.exit(0);
                }else if(choice == jmiAbout){
                    JOptionPane.showMessageDialog(null, "About");
                }
            }
        };

        jmiConnect.addActionListener(menuListener);
        jmiExit.addActionListener(menuListener);
        jmiAbout.addActionListener(menuListener);

        //Add game board to center
        //Controller cntr = new Controller(new Bike(5, 5));//This is for testing to see if the key lsitener works

        pack();
        setLocationRelativeTo(null);
        setTitle("Light Bikes");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        gameGrid.start();
    }
}