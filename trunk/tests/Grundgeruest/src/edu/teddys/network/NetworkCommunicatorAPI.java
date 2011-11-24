/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import edu.teddys.objects.box.items.Item;
import edu.teddys.objects.weapons.Weapon;

/**
 *
 * The Application Programming Interface for the client/server communication.
 * 
 * @author cm
 */
public interface NetworkCommunicatorAPI {
  /**
   * 
   * Acquire the public key from the server.
   * 
   * @param pubKeyClient
   * @return The public server key
   */
  public String getPubKey(String pubKeyClient);
  /**
   * 
   * Send update messages to the clients, for example the new Server object.
   * 
   * @param message Update request
   */
  public void notifyClients(NetworkMessage message);
  /**
   * 
   * 
   * 
   * @param client
   * @return 
   */
  public NetworkMessage join(Client client);
  public NetworkMessage disconnect(Client client);
  public void sendChecksum(String checkSumFiles);
//  public void updateClient(Client client);
  public void fireWeapon(Client client, Weapon weapon);
  public void activateItem(Client client, Item item);
}
