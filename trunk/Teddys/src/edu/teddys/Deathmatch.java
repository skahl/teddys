/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys;

import com.jme3.network.serializing.Serializable;

/**
 * 
 * TODO There should be a maximum number of kills/deaths ...
 *
 * @author cm
 */
@Serializable
public class Deathmatch extends GameMode {

  @Override
  public String getName() {
    return "Deathmatch";
  }
  
}
