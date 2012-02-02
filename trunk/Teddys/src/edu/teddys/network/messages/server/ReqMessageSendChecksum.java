/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageRequest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cm
 */
@Serializable
public class ReqMessageSendChecksum extends NetworkMessageRequest {

  private String token = "";
  private List<String> files = new ArrayList<String>();

  public ReqMessageSendChecksum() {
    super();
  }

  public ReqMessageSendChecksum(String token, List<String> files) {
    this(new Integer[]{}, token, files);
  }
  
  public ReqMessageSendChecksum(Integer clientID, String token, List<String> files) {
    this(new Integer[]{clientID}, token, files);
  }
  
  public ReqMessageSendChecksum(Integer[] clientIDs, String token, List<String> files) {
    super(clientIDs);
    if (token == null || token.isEmpty() || files == null || files.isEmpty()) {
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
