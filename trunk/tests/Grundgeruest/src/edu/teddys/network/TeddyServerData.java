/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import edu.teddys.GameMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * The data class for the TeddyServer. Contains some data about the clients and
 * game-relevant data such as game mode.
 * 
 * @author cm
 */
public class TeddyServerData {
  private Date created;
  private String name;
  private List<Team> teams = new ArrayList<Team>();
  private GameMode gameMode;
  private boolean isDiscoverable = false;
}
