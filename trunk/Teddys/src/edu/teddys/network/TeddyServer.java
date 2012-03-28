/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import edu.teddys.GameSettings;
import edu.teddys.MegaLogger;
import edu.teddys.network.messages.NetworkMessage;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.network.messages.server.ReqMessagePlayerDisconnect;
import edu.teddys.network.messages.server.ReqMessageSendClientData;
import edu.teddys.objects.player.Player;
import edu.teddys.states.Game;
import edu.teddys.timer.ChecksumManager;
import edu.teddys.timer.ServerTimer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cm
 */
public class TeddyServer implements NetworkCommunicatorAPI, ConnectionListener {

  /**
   * Autogenerated on demand.
   */
  private TeddyServerData data = new TeddyServerData();
  private static TeddyServer instance;
  
  private NetworkCommunicatorSpidermonkeyServer spidermonkeyServer = null;
  
  public static TeddyServer getInstance() {
    if (instance == null) {
      instance = new TeddyServer();
    }
    return instance;
  }
  
  private TeddyServer() {
    spidermonkeyServer = new NetworkCommunicatorSpidermonkeyServer(NetworkSettings.SERVER_PORT);
  }

  /**
   * 
   * Start the server.
   * 
   */
  public void startServer(Integer serverPort) {
    if (!isRunning()) {
      // Get a fresh data object
      data = new TeddyServerData();
      getData().setCreated(new Date());
    }
    spidermonkeyServer.startServer(this, serverPort);
    
    // Start the protection mechanisms
    if(GameSettings.ENABLE_CHECKSUM_CHECK) {
      ChecksumManager.startTimer();
    }
    MegaLogger.getLogger().debug("New server started.");
  }

  /**
   * 
   * Check if the server is currently running.
   * 
   * @return true if the server is running, else false.
   */
  public boolean isRunning() {
    return (spidermonkeyServer != null && spidermonkeyServer.isRunning());
  }

  /**
   * This function safely stops the server.
   */
  public void stopServer() {
    if (!isRunning()) {
      return;
    }
    // Start the protection mechanisms
    if(GameSettings.ENABLE_CHECKSUM_CHECK) {
      ChecksumManager.stopTimer();
    }
    ServerTimer.stopTimer();
    spidermonkeyServer.shutdownServer();

    MegaLogger.getLogger().debug("Server stopped.");
  }

  public String getPubKey(String pubKeyClient) {
    return spidermonkeyServer.getPubKey(pubKeyClient);
  }

  public void send(NetworkMessage message) {
    if (!getData().isDiscoverable()) {
      MegaLogger.getLogger().warn("TeddyServer not discoverable! Message not sent.");
    }
    if (!isRunning()) {
      return;
    }
    if(ServerTimer.isActive()) {
      message.setServerTimestamp(ServerTimer.getServerTimestamp());
    }
    spidermonkeyServer.send(message);
  }

  public boolean join(String serverIP, Integer serverPort) {
    // dummy value
    return false;
  }

  /**
   * 
   * Call this function instead of disconnect(Integer) if you want to send
   * a reason for disconnecting. This is, for example, if a client gets
   * kicked.
   * 
   * @param client  Client ID
   * @param reason Reason for the disconnect
   */
  public void disconnect(Integer client, String reason) {
    if (!isRunning()) {
      return;
    }
    //TODO close the connestion
//    conn.close(reason);
    MegaLogger.getLogger().debug(String.format("The specified client (%d) has been disconnected yet.", client));
  }

  public void disconnect(Integer client) {
    disconnect(client, null);
  }

  public TeddyServerData getData() {
    return data;
  }

  public void setData(TeddyServerData data) {
    this.data = data;
  }

  public List<Integer> getClientIDs() {
    if (getData() != null && !Player.getInstances().isEmpty()) {
      return new ArrayList<Integer>(Player.getInstances().keySet());
    }
    return new ArrayList<Integer>();
  }

  /**
   * 
   * Return the client data specified by the ID in the current server context.
   * Note: Does not check if the entry exists!
   * 
   * @param id  Client id
   * @return Client data, i. e. the name, health, ...
   */
  public ClientData getClientData(Integer id) {
    return Player.getInstance(id).getData();
  }

  /**
   * 
   * Called when a new client has joined the server.
   * 
   * @param server
   * @param conn The client information.
   */
  public void connectionAdded(Server server, HostedConnection conn) {
    if (!isRunning()) {
      MegaLogger.getLogger().error(new Throwable("connectionAdded() called, but the server is not running actually (or not discoverable)!"));
      return;
    }

    // Initialize a new player
    Player.getInstance(conn.getId());
    
    // Send a status text
    String message = String.format(
            "New player (%s) joined this server! Client ID is %s. Active players: %d",
            conn.getAddress(),
            conn.getId(),
            server.getConnections().size());
    MegaLogger.getLogger().info(message);
    
    // Send a message to all clients
    NetworkMessageInfo info = new NetworkMessageInfo(message);
    TeddyServer.getInstance().send(info);
    
    // Send a message to the new client
    String serverMsg = String.format("Welcome to %s!", getData().getName());
    NetworkMessageInfo clientInfo = new NetworkMessageInfo(conn.getId(), serverMsg);
    TeddyServer.getInstance().send(clientInfo);
    
    // Request the client data
    ReqMessageSendClientData sendMsg = new ReqMessageSendClientData(conn.getId());
    TeddyServer.getInstance().send(sendMsg);
  }

  /**
   * 
   * Called when a client has been disconnected.
   * 
   * @param server
   * @param conn The client information.
   */
  public void connectionRemoved(Server server, HostedConnection conn) {

    if (!isRunning()) {
      MegaLogger.getLogger().error(new Throwable("connectionRemoved() called, "
              + "but the server is not running (or not discoverable)!"));
      return;
    }
    
    if (conn == null) {
      // can be true if the server has been shutdown in the meantime.
      return;
    }

    Integer clientID = conn.getId();
    
    MegaLogger.getLogger().debug("Connection with ID " + clientID 
            + " removed! Active players: "+server.getConnections().size());

    // check if the player exists in the current game
    Game.getInstance().removePlayerFromWorld(Player.getInstance(clientID));

    // Now search the client data of the HostedConnection and remove it 
    // from list

    // acquire client data because of the team allocation
    if(getData() != null && !Player.getInstances().isEmpty()) {
      ClientData clientData = Player.getInstance(clientID).getData();
      if (clientData != null) {
        // remove the player from the team list
        if (clientData.getTeamID() != null) {
          try {
            getData().getTeams().get(clientData.getTeamID()).removePlayer(clientID);
          } catch (ArrayIndexOutOfBoundsException ex) {
            //TODO ignore?
            MegaLogger.getLogger().warn(
                    new Throwable(
                    String.format("No team with ID %d could be found!",
                    clientData.getTeamID()), ex));
          }
        }

        // remove the client data from server
        Player.removePlayer(clientID);
      }
    }
    
    ReqMessagePlayerDisconnect disconnectMsg = new ReqMessagePlayerDisconnect(clientID);
    TeddyServer.getInstance().send(disconnectMsg);

    String message = String.format(
            "Client %s disconnected.",
            clientID);
    NetworkMessageInfo info = new NetworkMessageInfo(message);
    send(info);
    MegaLogger.getLogger().info(message);
  }
  
  protected Collection<HostedConnection> getConnections() {
    return spidermonkeyServer.getConnections();
  }
}
