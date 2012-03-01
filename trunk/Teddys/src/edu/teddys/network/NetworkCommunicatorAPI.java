/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import edu.teddys.network.messages.NetworkMessage;

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
   * Send messages to the server. Depending on the type of the message,
   * other clients are notified.
   * 
   * @param message Some NetworkMessage
   */
  public void send(NetworkMessage message);
  /**
   * 
   * Join a new client to the list of users. 
   * 
   * @param serverIP The server which should be joined.
   * @param serverPort The server port on which the connection should be established.
   * 
   * @return Response from the server if the session could be opened or not.
   */
  public boolean join(String serverIP, Integer serverPort);
  /**
   * 
   * Destroy the active session for the specified user.
   * 
   * @param client  The client ID for the client to be disconnected. (Server implementation)
   */
  public void disconnect(Integer clientID);
}
