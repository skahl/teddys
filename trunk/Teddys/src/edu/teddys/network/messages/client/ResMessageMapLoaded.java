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
public class ResMessageMapLoaded extends NetworkMessageResponse {
  
  private Integer[] affected = new Integer[]{};

  public ResMessageMapLoaded() {
    super();
  }
  
  /**
   * 
   * @param affected the recipient of the message
   */
  public ResMessageMapLoaded(Integer affected) {
    super();
    this.affected = new Integer[]{affected};
  }
  
  /**
   * @param recipient The recipient of the message.
   * @param affected The users who have loaded the map.
   */
  public ResMessageMapLoaded(Integer recipient, Integer[] affected) {
    super(new Integer[]{recipient});
    this.affected = affected;
  }
  
  /**
   * @param recipient The recipients of the message.
   * @param affected The user who has loaded the map.
   */
  public ResMessageMapLoaded(Integer[] recipients, Integer affected) {
    super(recipients);
    this.affected = new Integer[]{affected};
  }
  
  /**
   * @param recipient The recipients of the message.
   * @param affected The users who have loaded the map.
   */
  public ResMessageMapLoaded(Integer[] recipients, Integer[] affected) {
    super(recipients);
    this.affected = affected;
  }

  public Integer[] getAffected() {
    return affected;
  }

  public void setAffected(Integer[] affected) {
    this.affected = affected;
  }
}
