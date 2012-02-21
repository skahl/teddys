/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.math.Vector2f;
import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;

/**
 *
 * @author cm
 */
@Serializable
public class ManMessageTriggerWeapon extends NetworkMessageManipulation {

  private Vector2f crosshair = new Vector2f();
  
  public ManMessageTriggerWeapon() {
    super();
  }

  public ManMessageTriggerWeapon(Vector2f crosshair) {
    this();
    if (crosshair == null) {
      throw new InstantiationError("Mouse position not specified!");
    }
    setCrosshair(crosshair);
  }

  public Vector2f getCrosshair() {
    return crosshair;
  }

  private void setCrosshair(Vector2f crosshair) {
    this.crosshair = crosshair;
  }
}
