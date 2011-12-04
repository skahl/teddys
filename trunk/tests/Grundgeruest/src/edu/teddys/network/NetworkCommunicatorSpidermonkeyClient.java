/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Client;
import edu.teddys.network.messages.NetworkMessage;
import java.io.IOException;
import java.util.Date;
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
  
  private static NetworkCommunicatorSpidermonkeyClient instance = null;
  
  public static NetworkCommunicatorSpidermonkeyClient getInstance() {
    if(instance == null) {
      instance = new NetworkCommunicatorSpidermonkeyClient();
    }
    return instance;
  }

  private NetworkCommunicatorSpidermonkeyClient() {
    // The serializer must be called to know what class can be persisted.
//    Serializer.registerClass(NetworkMessage.class);
  }

  public String getPubKey(String pubKeyClient) {
    //TODO: Use a socket connection for the encryption part
    return "";
  }

  public void send(NetworkMessage message) {
    //TODO send a message request to the server in order to inform all users
    networkClient.send(message);
  }

  public boolean join() {
    // Check for active connection
    if (networkClient != null) {
      if (networkClient.isConnected()) {
//        throw new RuntimeException("TeddyClient is already connected!");
        return false;
      }
    }
    TeddyClient client = TeddyClient.getInstance();
    // Get the server settings
    String serverIP = client.getServerIP();
    Integer serverPort = NetworkSettings.SERVER_PORT;
    if (serverIP == null || serverPort == null) {
      //TODO dirty?
      throw new RuntimeException("Please check your server settings!");
    }
    // Try to connect to the server
    try {
      networkClient = com.jme3.network.Network.connectToServer(serverIP, serverPort);
      networkClient.start();
      if(networkClient.isConnected()) {
        System.out.println("Client is connected. Setting the appropriate ID ...");
      } else {
        System.out.println("Not fully connected.");
      }
      // Get the ID from the server
      client.setId(networkClient.getId());
      System.out.println("Client ID is "+networkClient.getId());
      client.setJoinedServer(new Date());
      // Configure the client to receive messages
      networkClient.addMessageListener(new ClientListener());
      // Well done!
      System.out.println("Client is connected!!");
      return true;
    } catch (IOException ex) {
      Logger.getLogger(NetworkCommunicatorSpidermonkeyClient.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
  }

  public void disconnect(TeddyClient client) {
    if (!networkClient.isConnected()) {
      return;
    }
    networkClient.close();
    System.out.println("Client is now disconnected!");
  }

  public Client getNetworkClient() {
    return networkClient;
  }
}
