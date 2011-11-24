/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

/**
 *
 * @author cm
 */
public class NetworkMessage {

  private NetworkMessageType type;
  private String message;

  public NetworkMessage(NetworkMessageType type, String message) {
    setType(type);
    setMessage(message);
  }

  public NetworkMessageType getType() {
    return type;
  }

  private void setType(NetworkMessageType type) {
    this.type = type;
  }

  public String getMessage() {
    return "[" + type.name() + "] " + message;
  }

  private void setMessage(String message) {
    this.message = message;
  }
}
