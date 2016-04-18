/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import javax.swing.*;
import java.awt.*;

/**
 * Grid.java
 *
 * Assignment: Final Project
 * Class: Rochester Institute of Technology, ISTE-121.01, 2155
 * Professor: Michael Floeser
 *
 * This class is used as the grid GUI that the game is played on. The bike objects will move across the grid as they
 * leave the wall of light.
 *
 * @author Felice Aprile
 * @author Justin W. Flory
 * @author Malcolm Jones
 * @author Timothy Endersby
 * @version 2016.04.11.v1
 */
public class Grid extends JPanel{

   final int WIDTH = 500;
   final int HEIGHT = 500;
   private int i = 0;

   public Grid(){
      setPreferredSize(new Dimension(WIDTH+1, HEIGHT+1));//Plus one to assure the edge line is shown
   }
   
   public void paintComponent(Graphics g){
      super.paintComponent(g);
      //EDGES
      g.drawLine(0, 0, 0, HEIGHT);//left side
      g.drawLine(WIDTH, 0, WIDTH, HEIGHT);//right side
      g.drawLine(0, 0, WIDTH, 0);//top
      g.drawLine(0, HEIGHT, WIDTH, HEIGHT);//bottom
      
      //Draw snakes on screen here
   }
}