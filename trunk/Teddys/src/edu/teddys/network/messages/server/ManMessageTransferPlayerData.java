/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.TeddyServerData;
import edu.teddys.network.messages.NetworkMessageManipulation;
import edu.teddys.objects.player.Player;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageTransferPlayerData extends NetworkMessageManipulation {

  private Map<Integer, Player> data = new TreeMap<Integer, Player>();

  public ManMessageTransferPlayerData() {
    super();
  }

  public ManMessageTransferPlayerData(Map<Integer, Player> data) {
    this();
    setData(data);
  }

  public Map<Integer, Player> getData() {
    return data;
  }

  private void setData(Map<Integer, Player> data) {
    this.data = data;
  }
}
