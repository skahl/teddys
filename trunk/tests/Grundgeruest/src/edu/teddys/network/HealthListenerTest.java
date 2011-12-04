/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

/**
 *
 * @author cm
 */
public class HealthListenerTest implements AttributeListener<Integer> {

  public void attributeChanged(Integer value) {
    System.out.println("Health changed to " + value + "!");
  }
}
