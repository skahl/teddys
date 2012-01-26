/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import edu.teddys.MegaLogger;
import edu.teddys.network.messages.NetworkMessage;
import java.io.IOException;

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
    if (instance == null) {
      instance = new NetworkCommunicatorSpidermonkeyClient();
    }
    return instance;
  }

  private NetworkCommunicatorSpidermonkeyClient() {
  }

  protected Boolean isValidConnection() {
    if (networkClient == null) {
      return false;
    }
    //TODO add a delay for this check
    return networkClient.isConnected();
  }

  public void addClientStateListener(ClientStateListener listener) {
    if (networkClient == null) {
      MegaLogger.getLogger().error(new Throwable("Could not add ClientStateListener because networkClient is null!"));
      return;
    }
    networkClient.addClientStateListener(listener);
  }

  public String getPubKey(String pubKeyClient) {
    //TODO encrypt the messages before broadcasting with given public key?
    return "";
  }

  public void send(NetworkMessage message) {
    if (isValidConnection()) {
      networkClient.send(message);
    }
  }

  public boolean join() {
    TeddyClient client = TeddyClient.getInstance();
    // Check for active connection
    if (isValidConnection()) {
      // close the active connection
      disconnect(client.getData().getId());
    }
    // Get the server settings
    String serverIP = client.getServerIP();
    Integer serverPort = NetworkSettings.SERVER_PORT;
    if (serverIP == null || serverPort == null) {
      String msg = "Invalid server configuration! serverIP or serverPort is null. "
              + "Please check your network settings!";
      MegaLogger.getLogger().fatal(new Throwable(msg));
      throw new RuntimeException(msg);
    }
    // Try to connect to the server
    try {
      networkClient = com.jme3.network.Network.connectToServer(serverIP, serverPort);
      networkClient.start();
      // Configure the client to receive messages
      networkClient.addMessageListener(new ClientListener());
      MegaLogger.getLogger().debug("Client: Join request sent.");
      return true;
    } catch (IOException ex) {
      MegaLogger.getLogger().error(new Throwable("Client: Join request failed!", ex));
      return false;
    }
  }

  public void disconnect(Integer clientID) {
    if (!networkClient.isConnected()) {
      return;
    }
    networkClient.close();
    MegaLogger.getLogger().info("The client is disconnected now!");
  }

  public Client getNetworkClient() {
    return networkClient;
  }
}
