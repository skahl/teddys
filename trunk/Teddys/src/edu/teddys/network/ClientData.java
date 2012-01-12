/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import edu.teddys.input.Position;
import edu.teddys.objects.Jetpack;

/**
 *
 * @author cm
 */
@Serializable
public class ClientData {
  /**
   * Autogenerated on connection with a server.
   */
  private Integer id = -1;
  /**
   * The current Health Points ranging from 0 to 100.
   */
  private Integer health = 100;
  /**
   * Displayed name.
   */
  private String name = "Player";
  /**
   * Position in the current map. 
   */
  private Vector3f position = new Vector3f();
  private Jetpack jetpack = new Jetpack();
  /**
   * Session-related information, that is the number of deaths, kills, rounds etc.
   */
  private SessionClientData session = new SessionClientData();
  /**
   * Team allocation if available.
   */
  private Integer team = -1;
  /**
   * 
   */
  private Boolean ready = false;
  
  public ClientData() {
    super();
  }

  public Integer getHealth() {
    return health;
  }

  public void setHealth(Integer health) {
    this.health = health;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Jetpack getJetpack() {
    return jetpack;
  }

  public void setJetpack(Jetpack jetpack) {
    this.jetpack = jetpack;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }

  public SessionClientData getSession() {
    return session;
  }

  public void setSession(SessionClientData session) {
    this.session = session;
  }

  public Integer getTeamID() {
    return team;
  }

  public void setTeam(Integer team) {
    this.team = team;
  }

  public Boolean getReady() {
    return ready;
  }

  public void setReady(Boolean ready) {
    this.ready = ready;
  }
  
}
