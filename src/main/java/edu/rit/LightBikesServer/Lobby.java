/*
 * Copyright (c) 2016 Felice Aprile, Justin W. Flory, Malcolm Jones, Timothy Endersby
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package edu.rit.LightBikesServer;

import java.util.Vector;

/**
 * Lobby.java
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

public class Lobby {

    private String lobbyName;
    private boolean locked = false;
    private Vector<Player> players = new Vector<Player>();

    public Lobby() {

    }

    public Lobby(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public boolean isGameMaster(String username) {
        return username.equals(players.get(0).getUsername());
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public String getGameMaster() {
        if (players.size() >= 1) {
            return players.get(0).getUsername();
        }
        else {
            return "";
        }
    }

    public Vector<Player> getPlayers() {
        return players;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Player getPlayer(String username) {
        return null;
    }

    public void startGame() {
        locked = true;
        pushToAll(makeCommandString("rsp-game-start", "true"));
    }

    public void pushToAll(String string) {
        for (Player player : players) {
            player.push(string);
        }
    }

    public String makeCommandString(String command, String value) {
        return command + ":" + value + ";";
    }

}
