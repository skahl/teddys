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

  private String name = "Grampen";
  /**
   * Default value is a dummy.
   */
  private String color = Color.BLACK.toString();
  private List<Integer> players = new ArrayList<Integer>();

  public Team() {
  }

  public Team(String teamName) {
    this();
    setName(teamName);
  }
  
  public Team(Color color, String teamName) {
    this(teamName);
    setColor(color.toString());
  }

  public String getColor() {
    return color;
  }

  public final void setColor(String color) {
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public final void setName(String name) {
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
  
  public void removePlayer(Integer clientID) {
    getPlayers().remove(clientID);
  }
}
