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
   * Get the public key from the server for encryption reasons.
   * 
   * @param pubKeyClient  The key from the client so that the public key
   * from the server can be encrypted.
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
   * Join a new client to the list of users. 
   * 
   * @param client  The client to be joined to the list of users.
   * @return Response from the server if the session could be opened or not.
   */
  public NetworkMessage join(Client client);
  /**
   * 
   * Destroy the active session for the specified user.
   * 
   * @param client  The client that is disconnected from the server.
   * @return Response from the server if the session could be destroyed or not.
   */
  public NetworkMessage disconnect(Client client);
  /**
   * 
   * Send the checksum of local game files for security reasons. This should
   * be called when the server requests the current checksums.
   * 
   * @param checkSumFiles Checksum of all local game files. Must be signed with
   * the server's randomly generated key!
   */
  public void sendChecksum(String checkSumFiles);
  /**
   * 
   * Trigger a weapon activation for the user so that the server 
   * calculates the immanent properties such as damage, spread etc.
   * 
   * Afterwards, the server notifies all clients for the results.
   * 
   * @param client  The client that activates the specified weapon.
   * @param weapon The weapon that is activated. Checked if valid by the server!
   */
  public void fireWeapon(Client client, Weapon weapon);
  /**
   * 
   * Trigger an item activation. Notify the clients for the results if necessary.
   * 
   * @param client  The client that activates the specified item.
   * @param item  The item to be activated. Checked if valid by the server!
   */
  public void activateItem(Client client, Item item);
}
