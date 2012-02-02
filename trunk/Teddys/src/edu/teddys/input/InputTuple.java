/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.input;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author cm
 */
@Serializable
public class InputTuple {

  private String type = "";
  private String key = "";
  private Object value = "";
  private float tpf = 0f;
  private float x = 0;
  private float y = 0;

  public InputTuple() {
  }

  public InputTuple(InputType type, String key, Object value, float tpf, float x, float y) {
    this.type = type.name();
    if (key != null) {
      this.key = key;
    }
    if (value != null) {
      this.value = value;
    }
    this.tpf = tpf;
    this.x = x;
    this.y = y;
  }

  public InputType getType() {
    return InputType.valueOf(type);
  }

  public void setType(InputType type) {
    this.type = type.name();
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public float getTpf() {
    return tpf;
  }

  public void setTpf(float tpf) {
    this.tpf = tpf;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  @Override
  public String toString() {
    String out = getClass().getSimpleName();
    out += String.format("[type=%s,key=%s,value=%s,tpf=%s]", 
            getType().name(), getKey(), getValue(), getTpf());
    return out;
  }
}
