/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.serializing.Serializable;

/**
 * 
 * Session-related data storage. For example, this is used for statistics.
 *
 * @author cm
 */
@Serializable
public class SessionClientData {

  private Integer kills = 0;
  private Integer deaths = 0;
  private Integer wins = 0;
  private Integer losses = 0;
  private Integer rounds = 0;

  public SessionClientData() {
    super();
  }

  public Integer getDeaths() {
    return deaths;
  }

  public void setDeaths(Integer deaths) {
    this.deaths = deaths;
  }

  public Integer getKills() {
    return kills;
  }

  public void setKills(Integer kills) {
    this.kills = kills;
  }

  public Integer getLosses() {
    return losses;
  }

  public void setLosses(Integer losses) {
    this.losses = losses;
  }

  public Integer getRounds() {
    return rounds;
  }

  public void setRounds(Integer rounds) {
    this.rounds = rounds;
  }

  public Integer getWins() {
    return wins;
  }

  public void setWins(Integer wins) {
    this.wins = wins;
  }
  
  public void incDeaths() {
    deaths++;
  }
  
  public void incKills() {
    kills++;
  }
  
  public void incLosses() {
    losses++;
  }
  
  public void incWins() {
    wins++;
  }
  
  public void incRounds() {
    rounds++;
  }
}
