/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network.messages.client;

import com.jme3.network.serializing.Serializable;
import edu.teddys.network.messages.NetworkMessageManipulation;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 *
 * @author cm
 */
@Serializable
public class ManControllerInput extends NetworkMessageManipulation {

  private LinkedList<Entry<String, Object>> input = new LinkedList<Entry<String, Object>>();
  
  public ManControllerInput() {
    super();
  }
  
  public ManControllerInput(LinkedList<Entry<String, Object>> list) {
    this();
    input = list;
  }

  public LinkedList<Entry<String, Object>> getInput() {
    return input;
  }
}
