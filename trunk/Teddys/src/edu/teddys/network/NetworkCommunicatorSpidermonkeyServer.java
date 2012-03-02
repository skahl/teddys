/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.messages.NetworkMessage;
import java.io.IOException;
import java.util.Collection;

/**
 *
 * An implementation of the NetworkCommunicatorAPI.
 * 
 * @see NetworkCommunicatorAPI
 * @author cm
 */
public class NetworkCommunicatorSpidermonkeyServer implements NetworkCommunicatorAPI {

  private com.jme3.network.Server networkServer;

  public NetworkCommunicatorSpidermonkeyServer(Integer serverPort) {
    try {
      setUpServer(serverPort);
    } catch (IOException ex) {
      MegaLogger.getLogger().fatal(new Throwable(
              "The server could not be started (is the port "
              + serverPort + " already used?)", ex));
    }
  }

  /**
   * 
   * Start the SpiderMonkey server. If the server is already running, just return.
   * 
   * @param server The TeddyServer instance. Necessary to set a ConnectionListener
   *  that is informed of all connection-related stuff.
   * @see ConnectionListener
   */
  public void startServer(TeddyServer server, Integer serverPort) {
    if (isRunning()) {
      return;
    }
    networkServer.start();
    networkServer.addConnectionListener(server);
    MegaLogger.getLogger().debug("The Spidermonkey server has started successfully.");
  }
  
  /**
   * 
   * Check if the server is running.
   * 
   * @return True if networkServer is not null and it is running, else false.
   */
  boolean isRunning() {
    return (networkServer != null && networkServer.isRunning());
  }

  /**
   * 
   * Shut down the server if it is currently running.
   * 
   */
  public void shutdownServer() {
    if (networkServer == null || !networkServer.isRunning()) {
      return;
    }
    networkServer.close();
    MegaLogger.getLogger().debug("Spidermonkey server closed.");
  }

  /**
   * 
   * Create a new SpiderMonkey server on the specified port.
   * @see NetworkSettings
   * 
   * Additionally, add a listener for network messages.
   * @see ServerListener
   * 
   * @throws IOException Exception which is thrown on network errors.
   */
  private void setUpServer(Integer serverPort) throws IOException {
    // Get the server settings
    networkServer = com.jme3.network.Network.createServer(serverPort);
    // Configure the server to receive messages
    networkServer.addMessageListener(new ServerListener());
  }

  public String getPubKey(String pubKeyClient) {
    //TODO: Use a socket connection for the encryption part
    return "";
  }

  public void send(NetworkMessage message) {
    if (!isRunning()) {
      return;
    }
    if (GameSettings.NETWORK_SERVER_LAG_DELAY != 0) {
      try {
        Thread.sleep(GameSettings.NETWORK_SERVER_LAG_DELAY);
      } catch (InterruptedException ex) {
        MegaLogger.getLogger().debug(new Throwable("Lag delay sleep has been interrupted!", ex));
      }
    }
    if (message.getRecipients() == null || message.getRecipients().length < 1) {
      // Don't filter, send a broadcast
      networkServer.broadcast(message);
    }
    // Send a message to the appropriate clients
    for (Integer clientID : message.getRecipients()) {
      try {
        networkServer.getConnection(clientID).send(message);
      } catch (NullPointerException ex) {
        MegaLogger.getLogger().warn("Specified client ID does not exist: " 
                + clientID + ". Message: " + message);
      }
    }
  }

  public boolean join(String serverIP, Integer serverPort) {
    throw new UnsupportedOperationException("Not necessary.");
  }

  public void disconnect(Integer clientID) {
    throw new UnsupportedOperationException("Not necessary.");
  }

  public Collection<HostedConnection> getConnections() {
    if (!isRunning()) {
      MegaLogger.getLogger().debug("networkServer has not yet started, can't get the connections.");
      return null;
    }
    return networkServer.getConnections();
  }
}
