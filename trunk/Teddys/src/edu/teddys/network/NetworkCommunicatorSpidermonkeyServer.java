/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.ConnectionListener;
import edu.teddys.MegaLogger;
import edu.teddys.network.messages.NetworkMessage;
import edu.teddys.timer.ServerTimer;
import java.io.IOException;

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
    try {
      setUpServer();
    } catch (IOException ex) {
      MegaLogger.getLogger().fatal(new Throwable("The server could not be started!", ex));
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
      networkServer.addConnectionListener(server);
      MegaLogger.getLogger().debug("The Spidermonkey server has started successfully.");
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
      MegaLogger.getLogger().debug("Spidermonkey server closed.");
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
    //TODO check if a recipient field is available?
    message.setLocalTimestamp(NetworkMessage.getSystemTimestamp());
    message.setServerTimestamp(ServerTimer.getServerTimestamp());
    networkServer.broadcast(message);
  }

  /*
   * TODO use the client state listener for this purpose?
   */
  public boolean join() {
    throw new UnsupportedOperationException("Not necessary.");
  }

  public void disconnect(Integer clientID) {
    throw new UnsupportedOperationException("Not necessary.");
  }
}
