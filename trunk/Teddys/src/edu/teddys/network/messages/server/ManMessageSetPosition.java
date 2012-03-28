/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.server;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cm
 */
@Serializable
public final class ManMessageSetPosition extends NetworkMessageManipulation {

  private Map<Integer, Vector3f> positions = new HashMap<Integer, Vector3f>();
  private Boolean fixed = false;

  public ManMessageSetPosition() {
    super();
  }

  public Map<Integer, Vector3f> getPositions() {
    return positions;
  }

  public void setPositions(Map<Integer, Vector3f> positions) {
    this.positions = positions;
  }
  
  public void setFixed(Boolean fixed) {
    this.fixed = fixed;
  }
  
  public Boolean isFixed() {
    return fixed;
  }
}
