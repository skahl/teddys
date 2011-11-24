/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.serializing.Serializer;
import edu.teddys.objects.box.items.Item;
import edu.teddys.objects.weapons.Weapon;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * An implementation of the NetworkCommunicatorAPI.
 * 
 * @see NetworkCommunicatorAPI
 * @author cm
 */
public class NetworkCommunicatorSpidermonkeyClient implements NetworkCommunicatorAPI {
  
  private com.jme3.network.Client networkClient;

  public String getPubKey(String pubKeyClient) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void notifyClients(NetworkMessage message) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public NetworkMessage join(Client client) {
    // Check for active connection
    if(networkClient != null) {
      if(networkClient.isConnected()) {
        return new NetworkMessage(NetworkMessageType.WARNING, "Client is already connected!");
      }
    }
    // Get the server settings
    String serverIP = client.getServerIP();
    Integer serverPort = NetworkSettings.SERVER_PORT;
    if(serverIP == null || serverPort == null) {
      return new NetworkMessage(NetworkMessageType.ERROR, "Please check your server settings!");
    }
    // Try to connect to the server
    try {
      networkClient = com.jme3.network.Network.connectToServer(serverIP, serverPort);
      networkClient.start();
      // Get the ID from the server
      client.setId(networkClient.getId());
      // Register the client for receiving messages.
//      networkClient.addMessageListener(new ClientListenerSpidermonkey(), NetworkMessageSpidermonkey.class);
      Serializer.registerClass(NetworkMessageSpidermonkey.class);
      // Well done!
      return new NetworkMessage(NetworkMessageType.ACCEPT, 
              String.format("Client is connected to server %s:%d.",
                serverIP, serverPort));
    } catch (IOException ex) {
      Logger.getLogger(NetworkCommunicatorSpidermonkeyClient.class.getName()).log(Level.SEVERE, null, ex);
      return new NetworkMessage(NetworkMessageType.ERROR, ex.getMessage());
    }
  }

  public NetworkMessage disconnect(Client client) {
    if(!networkClient.isConnected()) {
      return new NetworkMessage(NetworkMessageType.WARNING, "Client is already disconnected!");
    }
    networkClient.close();
    return new NetworkMessage(NetworkMessageType.DISCONNECT, "Client is now disconnected.");
  }

  public void sendChecksum(String checkSumFiles) {
    networkClient.send(new NetworkMessageSpidermonkey(new NetworkMessage(NetworkMessageType.REJECT, checkSumFiles)));
  }

  public void fireWeapon(Client client, Weapon weapon) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void activateItem(Client client, Item item) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  
}
