/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import edu.teddys.BaseGame;
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

  public void startServer() {
    if(data == null) {
      // Check if server data is available
      NetworkCommunicatorSpidermonkeyServer.getInstance().startServer(this);
      data = new TeddyServerData();
      data.setCreated(new Date());
      data.setDiscoverable(true);
    }
    getData().setCreated(new Date());
    getData().setDiscoverable(true);
  }

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
    // copy the list of connections because of the modification on calling close()
    List<HostedConnection> copyConnections = getData().getConnections();
    for (HostedConnection conn : copyConnections) {
      conn.close("Going down for maintenance NOW! ;)");
    }
    NetworkCommunicatorSpidermonkeyServer.getInstance().shutdownServer();
    // reset data
    data = null;
  }

  public String getPubKey(String pubKeyClient) {
    return NetworkCommunicatorSpidermonkeyServer.getInstance().getPubKey(pubKeyClient);
  }

  public void send(NetworkMessage message) {
    if (!isRunning()) {
      return;
    }
    if (!getData().isDiscoverable()) {
      System.err.println("TeddyServer not discoverable! Message not sent.");
    }
    if (getData().getConnections().isEmpty()) {
      //TODO Save or dismiss the message?
      System.out.println("Message could not be sent because no clients were connected!");
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
    if(getData() != null && !getData().getClients().isEmpty()) {
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
      System.err.println("TeddyServer is not running!");
      return;
    }
    getData().getClients().put(clientID, client);
    BaseGame.getLogger().log(Level.INFO, "setClientData() called from client {0}", clientID);
  }

  public void connectionAdded(Server server, HostedConnection conn) {
    if (!isRunning()) {
      System.err.println("connectionAdded() called, but no server data available!");
      return;
    }
    getData().getConnections().add(conn);
    String message = String.format(
            "New connection (%s) arrived! Client ID is %s",
            conn.getAddress(),
            conn.getId());
    NetworkMessageInfo info = new NetworkMessageInfo(message);
    send(info);
    System.out.println(message);
    //TODO send the real name of the server
    String serverMsg = String.format("Welcome on my server %s!", "Grunute");
    NetworkMessageInfo clientInfo = new NetworkMessageInfo(serverMsg);
    conn.send(clientInfo);
    ReqMessageSendClientData sendMsg = new ReqMessageSendClientData();
    conn.send(sendMsg);
  }

  public void connectionRemoved(Server server, HostedConnection conn) {
    
    if (!isRunning()) {
      System.err.println("connectionRemoved() called, but no server data available!");
      return;
    }
    if (getData().getConnections().contains(conn)) {

      // Search for the client data of the HostedConnection and remove it 
      // from list

      // acquire client data because of the team allocation
      ClientData client = getData().getClients().get(conn.getId());
      if (client != null) {
        // remove the player from the team list
        List<Team> teams = getData().getTeams();
        if (client.getTeamID() != null && teams != null) {
          try {
            Team team = teams.get(client.getTeamID());
            team.getPlayers().remove(conn.getId());
          } catch(ArrayIndexOutOfBoundsException ex) {
            //TODO ignore?
            BaseGame.getLogger().log(Level.INFO,
                    "No team with ID {0} could be found!", client.getTeamID());
          }
        }

        // remove the client data from server
        getData().getClients().remove(conn.getId());
      }

      String message = String.format(
              "Client %s committed suicide.",
              conn.getId());
      getData().getConnections().remove(conn);
      NetworkMessageInfo info = new NetworkMessageInfo(message);
      send(info);
      System.out.println(message);
      return;
    }
    System.err.println(
            String.format("Connection remove request failed from Address %s!",
            conn.getAddress()));
  }
}
