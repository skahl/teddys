/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import edu.teddys.timer.SendPositionController;
import edu.teddys.network.messages.NetworkMessage;
import edu.teddys.objects.box.items.Item;
import edu.teddys.objects.weapons.Weapon;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cm
 */
public class TeddyClient implements NetworkCommunicatorAPI, ClientStateListener {
  
  /**
   * Field identifiers for the listeners. Info: Use this idents for the listeners!
   */
  public enum ListenerFields {
    health,currentItem,currentWeapon,isDead
  };
  /**
   * The player information, such as name, health etc.
   */
  private ClientData data = new ClientData();
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
   * The client can only handle one item which is bound to the user, so save it here.
   */
  private Item currentItem;
  /**
   * The list of achieved weapons. Initially, there's always the simple gun available.
   */
  private List<Weapon> weapons = new ArrayList<Weapon>();
  /**
   * Index in the weapon list for the current weapon.
   */
  private Integer currentWeapon;
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
  private Map<ListenerFields,List<AttributeListener>> listeners = new EnumMap<ListenerFields,List<AttributeListener>>(ListenerFields.class);
  
  private static TeddyClient instance = null;
  
  private TeddyClient() {
    super();
  }
  
  public static TeddyClient getInstance() {
    if(instance == null) {
      instance = new TeddyClient();
    }
    return instance;
  }
  
  public void registerListener(ListenerFields field, AttributeListener listener) {
    // additional check for "permitted" fields
    if(field == null) {
      return;
    }
    if(!listeners.containsKey(field)) {
      // create list for the field
      listeners.put(field, new ArrayList<AttributeListener>());
    }
    listeners.get(field).add(listener);
  }
  
  public boolean isCurrentConnection() {
    return currentConnection;
  }

  public void setCurrentConnection(boolean currentConnection) {
    this.currentConnection = currentConnection;
  }

  public Item getCurrentItem() {
    return currentItem;
  }

  public void setCurrentItem(Item currentItem) {
    this.currentItem = currentItem;
    for(AttributeListener listener : listeners.get(ListenerFields.currentItem)) {
      listener.attributeChanged(currentItem);
    }
  }

  public Integer getCurrentWeapon() {
    return currentWeapon;
  }

  public void setCurrentWeapon(Integer currentWeapon) {
    this.currentWeapon = currentWeapon;
    for(AttributeListener listener : listeners.get(ListenerFields.currentWeapon)) {
      listener.attributeChanged(currentWeapon);
    }
  }

  public Integer getHealth() {
    return getData().getHealth();
  }
  
  /**
   * 
   * Adds the specified damage value to the current teddy. Checks also if
   * the teddy is dead from now on and notifies all listeners of that fact 
   * if so.
   * 
   * @param damage Positive value as it is the damage to the teddy
   */
  public void addDamage(Integer damage) {
    Integer newHealth = getHealth()-damage;
    if(newHealth > 0) {
      setHealth(newHealth);
      return;
    }
    // You're dead, fag!
    for(AttributeListener listener : listeners.get(ListenerFields.isDead)) {
      listener.attributeChanged(true);
    }
  }

  public void setHealth(Integer health) {
    getData().setHealth(health);
    for(AttributeListener listener : listeners.get(ListenerFields.health)) {
      listener.attributeChanged(health);
    }
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
    return NetworkCommunicatorSpidermonkeyClient.getInstance().getPubKey(pubKeyClient);
  }

  public void send(NetworkMessage message) {
    NetworkCommunicatorSpidermonkeyClient.getInstance().send(message);
  }

  public boolean join() {
    boolean status = NetworkCommunicatorSpidermonkeyClient.getInstance().join();
    NetworkCommunicatorSpidermonkeyClient.getInstance().addClientStateListener(this);
    return status;
  }

  public void disconnect(Integer clientID) {
    NetworkCommunicatorSpidermonkeyClient.getInstance().disconnect(clientID);
  }
  
  public void disconnect() {
    disconnect(getData().getId());
  }

  public ClientData getData() {
    return data;
  }

  public void setData(ClientData data) {
    this.data = data;
  }
  
  public Integer getId() {
    return getData().getId();
  }
  
  public void setId(Integer id) {
    getData().setId(id);
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
    TeddyClient client = TeddyClient.getInstance();
    client.setId(c.getId());
    client.setJoinedServer(new Date());
  }

  public void clientDisconnected(Client c, DisconnectInfo info) {
    //TODO set game state
    SendPositionController.stopTimer();
  }
}
