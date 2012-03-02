/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import edu.teddys.GameSettings;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

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
  private String name = "Big Fat Server";
  /**
   * The list of teams registered on the server.
   */
  private List<Team> teams = new ArrayList<Team>();
  /*
   * //TODO integrate a singleton gameMode
   */
  /**
   * True if the server should be visible to other clients.
   */
  private boolean discoverable = false;
  /**
   * History of the clients' positions for the lag compensation.
   */
  private Map<Integer,LinkedBlockingQueue<Vector3f>> clientPositions = new HashMap<Integer,LinkedBlockingQueue<Vector3f>>();

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

  public Map<Integer, LinkedBlockingQueue<Vector3f>> getClientPositions() {
    return clientPositions;
  }

  public void setClientPositions(Map<Integer, LinkedBlockingQueue<Vector3f>> clientPositions) {
    this.clientPositions = clientPositions;
  }
  
  public void addClientPosition(Integer clientId, Vector3f pos) {
    if(!getClientPositions().containsKey(clientId)) {
      getClientPositions().put(clientId, new LinkedBlockingQueue<Vector3f>(GameSettings.MAX_SERVER_POS_CAPACITY));
    }
    getClientPositions().get(clientId).offer(pos);
  }
}
