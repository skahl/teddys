/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import edu.teddys.BaseGame;
import edu.teddys.MegaLogger;
import edu.teddys.network.messages.NetworkMessage;
import edu.teddys.network.messages.NetworkMessageInfo;
import edu.teddys.network.messages.server.ReqMessageSendClientData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author cm
 */
public class TeddyServer implements NetworkCommunicatorAPI, ConnectionListener {

  /**
   * Autogenerated on demand.
   */
  private TeddyServerData data;
  private static TeddyServer instance;

  public static TeddyServer getInstance() {
    if (instance == null) {
      instance = new TeddyServer();
    }
    return instance;
  }

  /**
   * 
   * Start the server. Refresh the server data regarding creation timestamp
   * and discoverability in every case, even when the server is already
   * running.
   * 
   */
  public void startServer() {
    NetworkCommunicatorSpidermonkeyServer.getInstance().startServer(this);
    if (!isRunning()) {
      data = new TeddyServerData();
    }
    getData().setCreated(new Date());
    getData().setDiscoverable(true);
    MegaLogger.debug("New server started.");
  }

  /**
   * 
   * Check if the server is currently running. That is when getData() returns
   * a non-null value.
   * 
   * @return true if the server is running, else false.
   */
  protected boolean isRunning() {
    if (data != null) {
      return true;
    }
    return false;
  }

  /**
   * This function safely stops the server.
   */
  public void stopServer() {
    if (!isRunning()) {
      return;
    }
    for (int i = 0; i < getData().getConnections().size(); i++) {
      getData().getConnections().get(i).close("Going down for maintenance NOW! ;)");
    }

    NetworkCommunicatorSpidermonkeyServer.getInstance().shutdownServer();
    // reset data
    data = null;
    MegaLogger.debug("Server stopped.");
  }

  public String getPubKey(String pubKeyClient) {
    return NetworkCommunicatorSpidermonkeyServer.getInstance().getPubKey(pubKeyClient);
  }

  public void send(NetworkMessage message) {
    if (!isRunning()) {
      return;
    }
    if (!getData().isDiscoverable()) {
      MegaLogger.warn("TeddyServer not discoverable! Message not sent.");
    }
    if (getData().getConnections().isEmpty()) {
      //TODO Save or dismiss the message?
      MegaLogger.debug("Message could not be sent because no clients were connected!");
      return;
    }
    NetworkCommunicatorSpidermonkeyServer.getInstance().send(message);
  }

  public boolean join() {
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
    HostedConnection conn = getData().getConnections().get(client);
    if (conn == null) {
      return;
    }
    conn.close(reason);
    MegaLogger.debug(String.format("The specified client (%d) has been disconnected yet.", client));
  }

  public void disconnect(Integer client) {
    disconnect(client, null);
  }

  protected TeddyServerData getData() {
    return data;
  }

  public void setData(TeddyServerData data) {
    this.data = data;
  }

  public List<Integer> getClientIDs() {
    if (getData() != null && !getData().getClients().isEmpty()) {
      return new ArrayList<Integer>(getData().getClients().keySet());
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
    return getData().getClients().get(id);
  }

  /**
   * Adds client data to the current list. Existing data will be overwritten!
   */
  public void setClientData(Integer clientID, ClientData client) {
    if (!isRunning()) {
      MegaLogger.error(new Throwable("TeddyServer is not running! Could not set client data!"));
      return;
    }
    getData().getClients().put(clientID, client);
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
      MegaLogger.error(new Throwable("connectionAdded() called, but the server is not running actually (or not discoverable)!"));
      return;
    }
    getData().getConnections().add(conn);

    // Send a status text
    String message = String.format(
            "New connection (%s) arrived! Client ID is %s",
            conn.getAddress(),
            conn.getId());
    NetworkMessageInfo info = new NetworkMessageInfo(message);
    send(info);
    MegaLogger.info(message);

    String serverMsg = String.format("Welcome to %s!", NetworkSettings.ServerName);
    NetworkMessageInfo clientInfo = new NetworkMessageInfo(serverMsg);
    conn.send(clientInfo);
    ReqMessageSendClientData sendMsg = new ReqMessageSendClientData();
    conn.send(sendMsg);
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
      MegaLogger.error(new Throwable("connectionRemoved() called, but the server is not running (or not discoverable)!"));
      return;
    }

    if (conn == null) {
      // can be true if the server has been shutdown in the meantime.
      return;
    }

    if (!getData().getConnections().contains(conn)) {
      MegaLogger.warn(
              String.format("Connection remove request (Client ID: %d) failed from address %s!",
              conn.getId(), conn.getAddress()));
      return;
    }

    // Now search the client data of the HostedConnection and remove it 
    // from list

    // acquire client data because of the team allocation
    ClientData client = getData().getClients().get(conn.getId());
    if (client != null) {
      // remove the player from the team list
      if (client.getTeamID() != null) {
        try {
          getData().getTeams().get(client.getTeamID()).removePlayer(client.getId());
        } catch (ArrayIndexOutOfBoundsException ex) {
          //TODO ignore?
          MegaLogger.warn(
                  new Throwable(
                  String.format("No team with ID %d could be found!", 
                  client.getTeamID()), ex)
                  );
        }
      }

      // remove the client data from server
      getData().getClients().remove(client.getId());
    }

    String message = String.format(
            "Client %s disconnected.",
            conn.getId());
    getData().getConnections().remove(conn);
    NetworkMessageInfo info = new NetworkMessageInfo(message);
    send(info);
    MegaLogger.info(message);
  }
}
