/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageRequest;

/**
 * 
 * A message for the clients that a new map should be loaded.
 *
 * @author cm
 */
@Serializable
public class ReqMessageMapRequest extends NetworkMessageRequest {

  private String map = "";
  
  public ReqMessageMapRequest() {
    super();
  }

  public ReqMessageMapRequest(String map) {
    this();
    setMap(map);
  }

  public String getMap() {
    return map;
  }

  private  void setMap(String map) {
    this.map = map;
  }
}
