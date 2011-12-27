/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.ConnectionListener;
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
public class NetworkCommunicatorSpidermonkeyServer implements NetworkCommunicatorAPI {

  private com.jme3.network.Server networkServer;
  private static NetworkCommunicatorSpidermonkeyServer instance;

  public static NetworkCommunicatorSpidermonkeyServer getInstance() {
    if (instance == null) {
      instance = new NetworkCommunicatorSpidermonkeyServer();
    }
    return instance;
  }

  private NetworkCommunicatorSpidermonkeyServer() {
    // The serializer must be called to know what class can be persisted.
//    Serializer.registerClass(NetworkMessage.class);
    try {
      setUpServer();
    } catch (IOException ex) {
      Logger.getLogger(NetworkCommunicatorSpidermonkeyServer.class.getName()).log(Level.SEVERE, null, ex);
      BaseGame.getLogger().log(
              Level.SEVERE,
              "Error while trying to start the server: {0}",
              ex.getMessage());
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
  public void startServer(TeddyServer server) {
    if (!networkServer.isRunning()) {
      networkServer.start();
      BaseGame.getLogger().info("Server started!");
      networkServer.addConnectionListener(server);
    }
  }

  /**
   * 
   * Shut down the server if it is currently running.
   * 
   */
  public void shutdownServer() {
    if (networkServer.isRunning()) {
      networkServer.close();
      BaseGame.getLogger().info("Server closed!");
    }
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
  private void setUpServer() throws IOException {
    // Get the server settings
    Integer serverPort = NetworkSettings.SERVER_PORT;
    networkServer = com.jme3.network.Network.createServer(serverPort);
    // Configure the server to receive messages
    networkServer.addMessageListener(new ServerListener());
  }

  public String getPubKey(String pubKeyClient) {
    //TODO: Use a socket connection for the encryption part
    return "";
  }

  public void send(NetworkMessage message) {
    //TODO check if a recipient field is available

    networkServer.broadcast(message);
  }

  /*
   * TODO use the client state listener for this purpose?
   */
  public boolean join() {
    throw new UnsupportedOperationException("Handled in TeddyServer.");
  }

  public void disconnect(Integer clientID) {
    throw new UnsupportedOperationException("Handled in TeddyServer.");
  }
}
