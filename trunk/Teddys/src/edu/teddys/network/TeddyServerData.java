/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * The data class for the TeddyServer. Contains some data about the clients and
 * game-relevant data such as game mode.
 * 
 * @author cm
 */
@Serializable
public class TeddyServerData {
  /**
   * The date on which the server started.
   */
  private Date created = new Date();
  /**
   * The server name which is displayed on occasion.
   */
  private String name = "Honey Pot World";
  /**
   * The list of teams registered on the server.
   */
  private List<Team> teams = new ArrayList<Team>();
  /**
   * True if the server should be visible to other clients.
   */
  private boolean discoverable = false;
  /**
   * History of the positions of the clients.
   * 
   * TODO: This should be used for the collision detection because of the index 'tick'!
   */
  private TreeMap<Long, Map<Integer,Vector3f>> clientPositions = new TreeMap<Long, Map<Integer,Vector3f>>();
  
  public TeddyServerData() {
    super();
    //TODO Create a GameMode object which is not persisted
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
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

  public List<Team> getTeams() {
    return teams;
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }

  public TreeMap<Long, Map<Integer, Vector3f>> getClientPositions() {
    return clientPositions;
  }

  public synchronized void setClientPositions(TreeMap<Long, Map<Integer, Vector3f>> clientPositions) {
    this.clientPositions = clientPositions;
  }
}
