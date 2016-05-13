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
import java.awt.event.*;

/**
* LightBikes.java <p>
*
* Assignment: Final Project <p>
* Class: Rochester Institute of Technology, ISTE-121.01, 2155 <p>
* Professor: Michael Floeser <p>
*
* This class is the main class for the LightBikes game created for our team's
* final project in ISTE-121. The game is modeled after the TRON-esque light
* bikes in the movie and in the arcade. Players control a bike that leaves a
* "wall of light" behind them as they travel across the game grid. They face
* an opponent who is also leaving a wall of light behind them. If one player
* hits the wall of light, they lose and the game ends.
* <p>
*
* This class is the main file and is executed as the game client. <p>
*
* This class is responsible for the GUI
*
* @author Felice Aprile
* @author Justin W. Flory
* @author Malcolm Jones
* @author Timothy Endersby
* @version 2016.04.11.v1
*/
public class LightBikes extends JFrame implements KeyListener, MouseListener {

    //These are test items, I'm not sure what we need here yet
    private JMenuItem jmiExit;
    private JMenuItem jmiConnect;
    private JMenuItem jmiAbout;
    private Grid gameGrid;
    private String hostname;
    private String username;

    /**
    * Starts the game
    * @param args Nothing
    */
    public static void main(String[] args){
        new LightBikes();
    }

    /**
    * Builds the GUI
    * Starts game logic classes
    */
    public LightBikes(){

        //Add JMenu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu jmFile = new JMenu("File");
        jmiExit = new JMenuItem("Exit");
        jmFile.add(jmiExit);
        menuBar.add(jmFile);
        JMenu jmHelp = new JMenu("Help");
        jmiAbout = new JMenuItem("About");
        jmHelp.add(jmiAbout);
        menuBar.add(jmHelp);
        add(menuBar, BorderLayout.NORTH);

        //Add chat window to east
        JPanel chatFrame = new JPanel(new BorderLayout());
        JTextArea chat = new JTextArea(25,60);
        JTextField msg = new JTextField(25);
        ChatClient chatClient = new ChatClient(chat, msg);

        add(chatFrame, BorderLayout.EAST);
        chatFrame.add(chat, BorderLayout.CENTER);
        chatFrame.add(msg, BorderLayout.SOUTH);

        //Create Game grid and add to center
        gameGrid = new Grid();
        add(gameGrid, BorderLayout.CENTER);

        //Action listener for Menu items
        ActionListener menuListener = new ActionListener() {
            public void	actionPerformed(ActionEvent ae){

                Object choice = ae.getSource();

                if(choice == jmiExit){
                    System.exit(0);
                }else if(choice == jmiAbout){
                    JOptionPane.showMessageDialog(null, "About");
                }
            }
        };

        jmiExit.addActionListener(menuListener);
        jmiAbout.addActionListener(menuListener);

        pack();
        setLocationRelativeTo(null);
        setTitle("Light Bikes");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        //Get connection info
        hostname = JOptionPane.showInputDialog(null, "Enter the server hostname:");
        username = JOptionPane.showInputDialog(null, "Enter your desired username:");
        gameGrid.connect(hostname, username);
        gameGrid.setFocusable(true);
        gameGrid.addKeyListener(this);
        gameGrid.addMouseListener(this);
        gameGrid.requestFocus();
        chatClient.connect(hostname, username);
    }

    /**
    * Key listeners to listen for user input to control the bike.
    */
    @Override
    public void keyPressed(KeyEvent ke){

        if(ke.getKeyCode() == KeyEvent.VK_RIGHT){
            gameGrid.turnEast();
        }
        else if(ke.getKeyCode() == KeyEvent.VK_LEFT) {
            gameGrid.turnWest();
        }
        else if(ke.getKeyCode() == KeyEvent.VK_UP) {
            gameGrid.turnNorth();
        }
        else if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
            gameGrid.turnSouth();
        }
    }

    /**
    * Returns the focus of the GUI to the grid.
    */
    @Override
    public void mouseEntered(MouseEvent e) {
        gameGrid.requestFocus();
    }

    /**
    * Unused.
    * @param ke KeyEvent
    */
    @Override
    public void keyReleased(KeyEvent ke){

    }

    /**
    * Unused.
    * @param ke KeyEvent
    */
    @Override
    public void keyTyped(KeyEvent ke){

    }

    /**
    * Unused.
    * @param ke KeyEvent
    */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Unused.
     * @param e MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Unused.
     * @param e MouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Unused.
     * @param e MouseEvent
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
