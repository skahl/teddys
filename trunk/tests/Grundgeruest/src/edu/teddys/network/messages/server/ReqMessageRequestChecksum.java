/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.NetworkSettings;
import edu.teddys.network.messages.NetworkMessageRequest;

/**
 *
 * @author cm
 */
@Serializable
public class ReqMessageRequestChecksum extends NetworkMessageRequest {

  private String[] files;

  public ReqMessageRequestChecksum(String[] files) {
    if(files == null || files.length == 0) {
      throw new InstantiationError("Some files must be specified to send a request!");
    }
    setFiles(files);
  }

  public String[] getFiles() {
    return files;
  }

  private void setFiles(String[] files) {
    this.files = files;
  }
}
