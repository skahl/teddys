/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

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
    if(instance == null) {
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
      System.err.println("Error while trying to start the server!");
    }
  }
  
  public void startServer(TeddyServer server) {
    if(!networkServer.isRunning()) {
      networkServer.start();
      System.out.println("Server started!");
      networkServer.addConnectionListener(server);
    }
  }
  
  public void shutdownServer() {
    if(networkServer.isRunning()) {
      networkServer.close();
      System.out.println("Server closed.");
    }
  }
  
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
//    // Check for active connection
//    if(networkServer != null) {
//      if(!networkServer.isRunning()) {
//        return new NetworkMessage(NetworkMessageType.ERROR, "Server is not running!");
//      }
//    }
//    // Try to add the client to the list of clients
//    try {
//      //TODO add
//      networkServer.getConnections().add(client.get);
//      // Well done!
//      return new NetworkMessage(NetworkMessageType.ACCEPT, 
//              String.format("New client is connected to server %d.",
//                serverPort));
//    } catch (IOException ex) {
//      Logger.getLogger(NetworkCommunicatorSpidermonkeyServer.class.getName()).log(Level.SEVERE, null, ex);
//      return new NetworkMessage(NetworkMessageType.ERROR, ex.getMessage());
//    }
    return false;
  }

  public void disconnect(TeddyClient client) {
    if(!networkServer.isRunning()) {
      System.err.println("Server is not running. Request discarded.");
    }
    //TODO
    System.out.println("Client should be disconnected!");
  }

  
}
