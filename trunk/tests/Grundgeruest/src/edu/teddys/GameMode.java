/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

/**
 *
 * @author cm
 */
abstract public class GameMode {
  
  private Integer minutesToPlay = 10;
  private Integer rounds = 1;
  
  private GameModeEnum modeEnum;
  
  public GameModeEnum getEnum() {
    return modeEnum;
  }
  
  abstract public String getName();
  
}
