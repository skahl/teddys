/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import edu.teddys.GameMode;
import edu.teddys.GameSettings;
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
@Serializable
public class TeddyServerData {
  private Date created = new Date();
  private String name = "TeddyServer";
  private List<Team> teams = new ArrayList<Team>();
  //TODO check if ok ......
  private List<HostedConnection> connections = new ArrayList<HostedConnection>();
  /**
   * Save the client data here ...
   */
  private Map<Integer,ClientData> clients = new HashMap<Integer,ClientData>();
  private Class<? extends GameMode> gameMode = GameSettings.DEFAULT_GAME_MODE;
  private boolean discoverable = false;

  public TeddyServerData() {
    super();
    //TODO Create a GameMode object which is not persisted
  }
  
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

  public Class<? extends GameMode> getGameMode() {
    return gameMode;
  }

  public void setGameMode(Class<? extends GameMode> gameMode) {
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

  public Map<Integer, ClientData> getClients() {
    return clients;
  }

  public void setClients(Map<Integer, ClientData> clients) {
    this.clients = clients;
  }

  public List<Team> getTeams() {
    return teams;
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }
}
