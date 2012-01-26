/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import edu.teddys.input.SimpleTriple;
import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;
import java.util.LinkedList;

/**
 *
 * @author cm
 */
@Serializable
public class ManControllerInput extends NetworkMessageManipulation {

  private LinkedList<SimpleTriple> input = new LinkedList<SimpleTriple>();
  
  public ManControllerInput() {
    super();
  }
  
  public ManControllerInput(LinkedList<SimpleTriple> list) {
    this();
    if(list == null) {
      // Always return a list
      return;
    }
    input = list;
  }

  public LinkedList<SimpleTriple> getInput() {
    return input;
  }
}
