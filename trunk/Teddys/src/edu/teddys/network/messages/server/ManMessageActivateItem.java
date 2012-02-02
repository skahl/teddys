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

  private String itemName;
  
  public ManMessageActivateItem() {
    super();
  }

  public ManMessageActivateItem(Integer clientID, String item) {
    super(new Integer[]{clientID});
    if (clientID == null || clientID == 0 || item == null) {
      throw new InstantiationError("Client or item must be specified!");
    }
    setItemName(item);
  }

  public String getItemName() {
    return itemName;
  }

  private void setItemName(String itemName) {
    this.itemName = itemName;
  }
}
