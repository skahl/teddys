/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageActivateItem extends NetworkMessageManipulation {

  private Integer affectedClient;
  private String itemName;
  
  public ManMessageActivateItem() {
    super();
  }

  public ManMessageActivateItem(Integer client, String item) {
    this();
    if (client == null || client == 0 || item == null) {
      throw new InstantiationError("Client or item must be specified!");
    }
    setAffectedClient(client);
    setItemName(item);
  }

  public Integer getAffectedClient() {
    return affectedClient;
  }

  private void setAffectedClient(Integer affectedClient) {
    this.affectedClient = affectedClient;
  }

  public String getItemName() {
    return itemName;
  }

  private void setItemName(String itemName) {
    this.itemName = itemName;
  }
}
