/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageRequest;

/**
 *
 * @author cm
 */
@Serializable
public class ReqMessageRelocateServer extends NetworkMessageRequest {

  private Integer destination;
  
  public ReqMessageRelocateServer() {
    super();
  }

  public ReqMessageRelocateServer(Integer destination) {
    this();
    if(destination == null) {
      throw new InstantiationError("Destination for the server relocation "
              + "must be specified (Client ID is missing)!");
    }
    setDestination(destination);
  }

  public Integer getDestination() {
    return destination;
  }

  private void setDestination(Integer destination) {
    this.destination = destination;
  }
}
