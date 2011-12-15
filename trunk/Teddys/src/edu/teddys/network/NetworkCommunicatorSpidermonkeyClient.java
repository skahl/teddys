/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import edu.teddys.BaseGame;
import edu.teddys.network.messages.NetworkMessage;
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
  
  private static NetworkCommunicatorSpidermonkeyClient instance = null;
  
  public static NetworkCommunicatorSpidermonkeyClient getInstance() {
    if(instance == null) {
      instance = new NetworkCommunicatorSpidermonkeyClient();
    }
    return instance;
  }

  private NetworkCommunicatorSpidermonkeyClient() {
  }
  
  public void addClientStateListener(ClientStateListener listener) {
    if(networkClient == null) {
      BaseGame.getLogger().severe("Could not add ClientStateListener because networkClient is null!");
      return;
    }
    networkClient.addClientStateListener(listener);
  }

  public String getPubKey(String pubKeyClient) {
    //TODO encrypt the messages before broadcasting with given public key?
    return "";
  }

  public void send(NetworkMessage message) {
    //TODO check if the client is in the list of active connections ...
    networkClient.send(message);
  }

  public boolean join() {
    TeddyClient client = TeddyClient.getInstance();
    // Check for active connection
    if (networkClient != null) {
      if (networkClient.isConnected()) {
        // close connection
        disconnect(client.getId());
      }
    }
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
      if(!networkClient.isConnected()) {
        System.out.println("Not fully connected yet.");
      }
      // Configure the client to receive messages
      networkClient.addMessageListener(new ClientListener());
      return true;
    } catch (IOException ex) {
      Logger.getLogger(NetworkCommunicatorSpidermonkeyClient.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
  }

  public void disconnect(Integer clientID) {
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
