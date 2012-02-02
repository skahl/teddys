/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import edu.teddys.GameSettings;
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
  /**
   * A counter variable for isValidConnection().
   */
  private int conCheck = 0;

  public static NetworkCommunicatorSpidermonkeyClient getInstance() {
    if (instance == null) {
      instance = new NetworkCommunicatorSpidermonkeyClient();
    }
    return instance;
  }

  private NetworkCommunicatorSpidermonkeyClient() {
  }

  /**
   * 
   * Checks if networkClient is not null. If so, check every n-th time (see
   * GameSettings.NETWORK_CLIENT_CON_CHECK) whether the connection is fully
   * established.
   * 
   * @return True if a connection to a server is established, else false.
   */
  protected Boolean isValidConnection() {
    if (networkClient == null) {
      return false;
    }
    if(conCheck % GameSettings.NETWORK_CLIENT_CON_CHECK == 0) {
      return networkClient.isConnected();
    }
    return true;
  }

  /**
   * 
   * Listenes for a client state change.
   * 
   * @param listener  The client listener.
   */
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

  /**
   * 
   * Checks at first if there's a valid connection. If so, consider the sleep
   * value in GameSettings.NETWORK_CLIENT_LAG_DELAY to test the lag
   * compensation. After it, send the message.
   * 
   * @param message The message to be transmitted.
   */
  public void send(NetworkMessage message) {
    if (isValidConnection()) {
      if(GameSettings.NETWORK_CLIENT_LAG_DELAY != 0) {
        try {
          Thread.sleep(GameSettings.NETWORK_CLIENT_LAG_DELAY);
        } catch (InterruptedException ex) {
          MegaLogger.getLogger().debug(new Throwable("Lag delay sleep has been interrupted!", ex));
        }
      }
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
