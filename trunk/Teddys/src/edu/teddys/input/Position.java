/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.input;

import com.jme3.network.serializing.Serializable;

/**
 * 
 * The current position of a player. x and y must be positive values!
 *
 * @author cm
 */
@Serializable
public final class Position {

  private Integer x = 0;
  private Integer y = 0;
  
  public Position(int x, int y) {
    super();
    if(x < 0 || y < 0) {
      throw new InstantiationError("Position must be defined by positive values!");
    }
    setX(x);
    setY(y);
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
