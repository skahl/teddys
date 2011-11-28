/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.network;

import edu.teddys.objects.weapons.Weapon;

/**
 *
 * @author cm
 */
public class NetworkEvent extends NetworkData {

  private NetworkEventTypes type;
  private Client source;
  private Object object;

  public NetworkEvent(NetworkEventTypes type, Client client, Object weapon) {
    setType(type);
    setSource(client);
    setObject(weapon);
  }

  public NetworkEventTypes getType() {
    return type;
  }

  protected void setType(NetworkEventTypes type) {
    this.type = type;
  }

  public Object getObject() {
    return object;
  }

  protected void setObject(Object object) {
    this.object = object;
  }

  public Client getSource() {
    return source;
  }

  protected void setSource(Client source) {
    this.source = source;
  }
}
