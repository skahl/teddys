/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.HostedConnection;
import edu.teddys.GameMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private List<HostedConnection> connections = new ArrayList<HostedConnection>();
  private Map<Integer,TeddyClient> clients = new HashMap<Integer,TeddyClient>();
  private GameMode gameMode;
  private boolean discoverable = false;

  public List<HostedConnection> getConnections() {
    return connections;
  }

  public void setConnections(List<HostedConnection> connections) {
    this.connections = connections;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public GameMode getGameMode() {
    return gameMode;
  }

  public void setGameMode(GameMode gameMode) {
    this.gameMode = gameMode;
  }

  public boolean isDiscoverable() {
    return discoverable;
  }

  public void setDiscoverable(boolean isDiscoverable) {
    this.discoverable = isDiscoverable;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<Integer, TeddyClient> getClients() {
    return clients;
  }

  public void setClients(Map<Integer, TeddyClient> clients) {
    this.clients = clients;
  }

  public List<Team> getTeams() {
    return teams;
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }
}
