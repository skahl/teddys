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
  }

  public String getPubKey(String pubKeyClient) {
    //TODO encrypt the messages before broadcasting with given public key?
    return "";
  }

  public void send(NetworkMessage message) {
    networkClient.send(message);
  }

  public boolean join() {
    TeddyClient client = TeddyClient.getInstance();
    // Check for active connection
    if (networkClient != null) {
      if (networkClient.isConnected()) {
        // close connection
        disconnect(client);
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
      if(networkClient.isConnected()) {
        System.out.println("Client is connected. Setting the appropriate ID ...");
        //TODO use the client state listener?
      } else {
        System.out.println("Not fully connected yet.");
      }
      while(networkClient.getId() == -1) {
        try {
          //TODO use state listener instead of polling?
          Thread.sleep(1000);
        } catch (InterruptedException ex) {
          Logger.getLogger(NetworkCommunicatorSpidermonkeyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      // Get the ID from the server
      client.setId(networkClient.getId());
      System.out.println("Client ID is "+networkClient.getId());
      client.setJoinedServer(new Date());
      // Configure the client to receive messages
      networkClient.addMessageListener(new ClientListener());
      // Well done!
      System.out.println("Client is connected!");
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
