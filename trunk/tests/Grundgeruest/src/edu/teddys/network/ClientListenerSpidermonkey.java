/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * @author cm
 */
public class ClientListenerSpidermonkey implements MessageListener<Client> {
  public void messageReceived(Client source, Message message) {
    if(message instanceof NetworkMessageSpidermonkey) {
      NetworkMessageSpidermonkey msg = (NetworkMessageSpidermonkey)message;
      System.out.println(msg.getData().getMessage());
    }
  }
  
}
