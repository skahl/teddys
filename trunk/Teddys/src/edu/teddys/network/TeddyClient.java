/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import edu.teddys.MegaLogger;
import edu.teddys.network.messages.NetworkMessage;
import edu.teddys.objects.player.Player;
import edu.teddys.objects.weapons.Weapon;
import edu.teddys.states.AppStateSwitcher;
import edu.teddys.timer.ClientTimer;
import edu.teddys.timer.ServerTimer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cm
 */
public class TeddyClient implements NetworkCommunicatorAPI, ClientStateListener {

//  /**
//   * The player information, such as name, health etc.
//   */
//  private ClientData data = new ClientData();
  /**
   * Server ip the client uses for the gameplay.
   */
  private String serverIP = NetworkSettings.DEFAULT_SERVER;
  /**
   * Public key of the server for the encryption.
   */
  private String pubKeyServer;
  /**
   * Indicates whether a server connection is active.
   */
  private boolean currentConnection = false;
  /**
   * Information when the client has connected to the server.
   */
  private Date joinedServer;
  /**
   * The list of achieved weapons. Initially, there's always the simple gun available.
   */
  private List<Weapon> weapons = new ArrayList<Weapon>();
  /**
   * The last 10 pings to the server. This is used for statistical and game-dependent
   * issues. For example, the server balancing or anti-lagging.
   */
  private List<Integer> lastPings = new ArrayList<Integer>();
  /**
   * Listeners for the modification of fields. For example, an attribute 
   * listener HealthListener could watch the changes of health since it 
   * is notified on calls of setHealth().
   */
  private static TeddyClient instance = null;
  private NetworkCommunicatorSpidermonkeyClient spidermonkeyClient = new NetworkCommunicatorSpidermonkeyClient();

  private TeddyClient() {
    super();
  }

  public static TeddyClient getInstance() {
    if (instance == null) {
      instance = new TeddyClient();
    }
    return instance;
  }

  public boolean isCurrentConnection() {
    return currentConnection;
  }

  public Date getJoinedServer() {
    return joinedServer;
  }

  public void setJoinedServer(Date joinedServer) {
    this.joinedServer = joinedServer;
  }

  public List<Integer> getLastPings() {
    return lastPings;
  }

  public void setLastPings(List<Integer> lastPings) {
    this.lastPings = lastPings;
  }

  public String getPubKeyServer() {
    return pubKeyServer;
  }

  public void setPubKeyServer(String pubKeyServer) {
    this.pubKeyServer = pubKeyServer;
  }

  public String getServerIP() {
    return serverIP;
  }

  public void setServerIP(String serverIP) {
    this.serverIP = serverIP;
  }

  public List<Weapon> getWeapons() {
    return weapons;
  }

  public void setWeapons(List<Weapon> weapons) {
    this.weapons = weapons;
  }

  public String getPubKey(String pubKeyClient) {
    return spidermonkeyClient.getPubKey(pubKeyClient);
  }

  public void send(NetworkMessage message) {
    spidermonkeyClient.send(message);
  }

  public boolean join(String serverIP, Integer serverPort) {
    return spidermonkeyClient.join(serverIP, serverPort);
  }

  public void disconnect(Integer clientID) {
    spidermonkeyClient.disconnect(clientID);
  }

  public void disconnect() {
    disconnect(Player.LOCAL_PLAYER);
  }

  /**
   * 
   * Method to produce a dummy enemy.
   * 
   * @return A new teddy client for development issues.
   */
  @Override
  public TeddyClient clone() {
    return new TeddyClient();
  }

  public void clientConnected(Client c) {
    
    Player.setLocalPlayerId(c.getId());
    TeddyClient.getInstance().setJoinedServer(new Date());
    MegaLogger.getLogger().debug("Client joined the server at "
            + TeddyClient.getInstance().getJoinedServer().toLocaleString());
    currentConnection = true;
  }

  public void clientDisconnected(Client c, DisconnectInfo info) {
    // Note: The client ID for the local player is kept until a new connection
    // has been established.
    ClientTimer.stopTimer();
    ServerTimer.stopTimer();
    MegaLogger.getLogger().warn("Client has been disconnected from the server");
    AppStateSwitcher.getInstance().activateState(AppStateSwitcher.AppStateEnum.MENU);
    currentConnection = false;
  }
}
