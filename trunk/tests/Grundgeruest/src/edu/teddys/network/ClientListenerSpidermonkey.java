/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * The listener for clients of our network implementation.
 * 
 * @author cm
 */
public class ClientListenerSpidermonkey implements MessageListener<com.jme3.network.Client> {
  public void messageReceived(com.jme3.network.Client source, Message message) {
    if(message instanceof NetworkMessageSpidermonkey) {
      NetworkMessageSpidermonkey msg = (NetworkMessageSpidermonkey)message;
      NetworkData data = msg.getData();
      if(data instanceof NetworkMessage) {
        System.out.println(((NetworkMessage)data).getMessage());
      }
    }
  }
}
