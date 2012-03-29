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

  private String levelName = "";
  private String levelPath = "";
  
  public ReqMessageMapRequest() {
    super();
  }

  public ReqMessageMapRequest(Integer clientID, String levelName, String levelPath) {
    super(new Integer[]{clientID});
    this.levelName = levelName;
    this.levelPath = levelPath;
  }

  public String getLevelName() {
    return levelName;
  }

  public void setLevelName(String levelName) {
    this.levelName = levelName;
  }

  public String getLevelPath() {
    return levelPath;
  }

  public void setLevelPath(String levelPath) {
    this.levelPath = levelPath;
  }
}
