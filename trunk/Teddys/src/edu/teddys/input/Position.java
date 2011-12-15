/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.input;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author cm
 */
@Serializable
public class Position {

  private Integer x = 0;
  private Integer y = 0;
  
  public Position() {
    super();
  }

  public Integer getX() {
    return x;
  }

  public void setX(Integer x) {
    this.x = x;
  }

  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }
}
