/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.serializing.Serializable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cm
 */
@Serializable
public class Team {
  private String name;
  private String id;
  /**
   * Default value is a dummy.
   */
  private Color color = Color.BLACK;
  private List<Integer> players = new ArrayList<Integer>();

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Integer> getPlayers() {
    return players;
  }

  public void setPlayers(List<Integer> players) {
    this.players = players;
  }
  
  public void addPlayer(Integer clientID) {
    getPlayers().add(clientID);
  }
}
