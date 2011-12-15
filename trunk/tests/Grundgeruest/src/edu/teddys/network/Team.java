/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import java.awt.Color;
import java.util.List;

/**
 *
 * @author cm
 */
public class Team {
  private String name;
  private String id;
  private Color color;
  private List<Integer> players;

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
}
