/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import edu.teddys.objects.box.items.Item;
import edu.teddys.objects.weapons.Weapon;

/**
 *
 * An implementation of the NetworkCommunicatorAPI.
 * 
 * @see NetworkCommunicatorAPI
 * @author cm
 */
public class NetworkCommunicatorSpidermonkey implements NetworkCommunicatorAPI {

  public String getPubKey(String pubKeyClient) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void notifyClients(NetworkMessage message) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public NetworkMessage join(Client client) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public NetworkMessage disconnect(Client client) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public NetworkMessage syncServerState(Client client) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void sendChecksum(String checkSumFiles) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void updateClient(Client client) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void fireWeapon(Client client, Weapon weapon) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void activateItem(Client client, Item item) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}
