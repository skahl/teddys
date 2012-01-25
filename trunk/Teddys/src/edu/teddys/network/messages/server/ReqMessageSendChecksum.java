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
  private Integer destination;

  public ReqMessageSendChecksum() {
    super();
  }

  public ReqMessageSendChecksum(String token, List<String> files, Integer destination) {
    this(token, files);
    setDestination(destination);
  }

  private ReqMessageSendChecksum(String token, List<String> files) {
    this();
    if (token == null || token.isEmpty() || files == null || files.isEmpty()) {
      throw new InstantiationError("Some files must be specified to send a request!");
    }
    setFiles(files);
    setToken(token);
    //TODO change!!!
    setDestination(0);
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

  public Integer getDestination() {
    return destination;
  }

  private void setDestination(Integer destination) {
    this.destination = destination;
  }
}
