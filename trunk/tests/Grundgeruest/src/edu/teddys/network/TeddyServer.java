/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import edu.teddys.network.messages.NetworkMessage;

/**
 *
 * @author cm
 */
public class TeddyServer implements NetworkCommunicatorAPI {
  
  /**
   * Autogenerated on demand.
   */
  private String id;
  private TeddyServerData data;
  
  private static TeddyServer instance;
  
  public static TeddyServer getInstance() {
    if(instance == null) {
      instance = new TeddyServer();
    }
    return instance;
  }
  
  public void startServer() {
    NetworkCommunicatorSpidermonkeyServer.getInstance().startServer();
    data = new TeddyServerData();
  }
  
  public void stopServer() {
    NetworkCommunicatorSpidermonkeyServer.getInstance().shutdownServer();
    // reset data
    data = null;
  }
  
  public String getPubKey(String pubKeyClient) {
    return NetworkCommunicatorSpidermonkeyServer.getInstance().getPubKey(pubKeyClient);
  }

  public void send(NetworkMessage message) {
    NetworkCommunicatorSpidermonkeyServer.getInstance().send(message);
  }

  public boolean join() {
    //TODO check if necessary?
    boolean joined = NetworkCommunicatorSpidermonkeyServer.getInstance().join();
    if(joined) {
      //TODO add client to game data
      
    }
    return joined;
  }

  public void disconnect(TeddyClient client) {
    NetworkCommunicatorSpidermonkeyServer.getInstance().disconnect(client);
    //TODO remove client from team
  }

  public TeddyServerData getData() {
    return data;
  }

  public void setData(TeddyServerData data) {
    this.data = data;
  }

  public String getId() {
    return id;
  }

  private void setId(String id) {
    this.id = id;
  }
  
}
