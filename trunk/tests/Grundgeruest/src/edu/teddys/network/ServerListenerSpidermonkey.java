/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * Since clients are able to submit messages to the server, this
 * class is used to fetch these ones.
 *
 * @author cm
 */
public class ServerListenerSpidermonkey implements MessageListener<HostedConnection> {

  public void messageReceived(HostedConnection source, Message message) {
    if (message instanceof NetworkMessageSpidermonkey) {
      NetworkMessageSpidermonkey msg = (NetworkMessageSpidermonkey) message;
      NetworkData data = msg.getData();
      if (data instanceof NetworkEvent) {
        System.out.println("Event triggered! " + ((NetworkEvent)data).getSource().getName());
      } else if (data instanceof NetworkMessage) {
        System.out.println("Message received!" + ((NetworkMessage)data).getMessage());
      }
    }
  }
}
