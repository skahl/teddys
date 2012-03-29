/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

/**
 * 
 * Listener for value changes in the SessionData
 *
 * @author cm
 */
public interface SessionDataListener {
  public void valueChanged(Integer playerId, SessionDataFieldsEnum fieldName, Integer oldValue, Integer newValue);
}
