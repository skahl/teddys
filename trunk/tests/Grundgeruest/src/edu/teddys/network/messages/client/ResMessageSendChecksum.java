/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageResponse;

/**
 *
 * @author cm
 */
@Serializable
public class ResMessageSendChecksum extends NetworkMessageResponse {

  private String token;
  private String checksum;

  public ResMessageSendChecksum(String token, String checksum) {
    if (checksum == null || token == null) {
      throw new InstantiationError("Checksum or token not specified!");
    }
    setChecksum(checksum);
    setToken(token);
  }

  public String getChecksum() {
    return checksum;
  }

  private void setChecksum(String checksum) {
    this.checksum = checksum;
  }

  public String getToken() {
    return token;
  }

  private void setToken(String token) {
    this.token = token;
  }
}
