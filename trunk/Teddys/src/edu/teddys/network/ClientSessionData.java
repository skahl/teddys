/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.serializing.Serializable;
import edu.teddys.MegaLogger;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Session-related data storage. For example, this is used for statistics.
 *
 * @author cm
 */
@Serializable
public class ClientSessionData {

  private Integer kills = 0;
  private Integer deaths = 0;
  private Integer wins = 0;
  private Integer losses = 0;
  private Integer rounds = 0;
  transient Integer playerId = -1;
  transient List<SessionDataListener> listeners = new ArrayList<SessionDataListener>();

  public ClientSessionData() {
    super();
  }
  
  public void registerListener(SessionDataListener listener) {
    listeners.add(listener);
  }

  public Integer getDeaths() {
    return deaths;
  }

  public void setDeaths(Integer deaths) {
    newValue(SessionDataFieldsEnum.deaths, this.deaths, deaths);
    this.deaths = deaths;
  }

  public Integer getKills() {
    return kills;
  }

  public void setKills(Integer kills) {
    newValue(SessionDataFieldsEnum.kills, this.kills, kills);
    this.kills = kills;
  }

  public Integer getLosses() {
    return losses;
  }

  public void setLosses(Integer losses) {
    newValue(SessionDataFieldsEnum.losses, this.losses, losses);
    this.losses = losses;
  }

  public Integer getRounds() {
    return rounds;
  }

  public void setRounds(Integer rounds) {
    newValue(SessionDataFieldsEnum.rounds, this.rounds, rounds);
    this.rounds = rounds;
  }

  public Integer getWins() {
    return wins;
  }

  public void setWins(Integer wins) {
    newValue(SessionDataFieldsEnum.wins, this.wins, wins);
    this.wins = wins;
  }
  
  public void incDeaths() {
    setDeaths(getDeaths()+1);
  }
  
  public void incKills() {
    setKills(getKills()+1);
  }
  
  public void incLosses() {
    setLosses(getLosses()+1);
  }
  
  public void incWins() {
    setWins(getWins()+1);
  }
  
  public void incRounds() {
    setRounds(getRounds()+1);
  }

  private void newValue(SessionDataFieldsEnum name, Integer oldVal, Integer newVal) {
    for(SessionDataListener listener : listeners) {
      listener.valueChanged(playerId, name, oldVal, newVal);
    }
  }
}
