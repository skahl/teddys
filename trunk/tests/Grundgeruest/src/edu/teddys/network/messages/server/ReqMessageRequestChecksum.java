/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageRequest;
import java.util.List;

/**
 *
 * @author cm
 */
@Serializable

public class ReqMessageRequestChecksum extends NetworkMessageRequest {

  private String token = null;
  private List<String> files = null;

  public ReqMessageRequestChecksum(String token, List<String> files) {
    if(token == null || token.isEmpty() || files == null || files.isEmpty()) {
      throw new InstantiationError("Some files must be specified to send a request!");
    }
    setFiles(files);
    setToken(token);
  }

  public List<String> getFiles() {
    return files;
  }

  private void setFiles(List<String> files) {
    this.files = files;
  }

  public String getToken() {
    return token;
  }

  private void setToken(String token) {
    this.token = token;
  }
}
