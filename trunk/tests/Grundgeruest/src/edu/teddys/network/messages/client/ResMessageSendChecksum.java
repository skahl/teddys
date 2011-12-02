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

  private String checksum;

  public ResMessageSendChecksum(String checksum) {
    if (checksum == null) {
      throw new InstantiationError("Checksum not specified!");
    }
    setChecksum(checksum);
  }

  public String getChecksum() {
    return checksum;
  }

  private void setChecksum(String checksum) {
    this.checksum = checksum;
  }
}
