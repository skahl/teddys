/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

/**
 *
 * @author cm
 */
public class DeathTest implements AttributeListener<Boolean> {

  public void attributeChanged(Boolean value) {
    System.out.println("Your teddy is dead. Muahahahaha!");
  }
  
}
