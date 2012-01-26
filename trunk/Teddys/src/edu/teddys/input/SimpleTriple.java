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
public class SimpleTriple {

  private String type = "";
  private String key = "";
  private Object value = "";
  private float tpf = 0f;

  public SimpleTriple() {
  }

  public SimpleTriple(InputType type, String key, Object value, float tpf) {
    this.type = type.name();
    if (key != null) {
      this.key = key;
    }
    if (value != null) {
      this.value = value;
    }
    this.tpf = tpf;
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

  @Override
  public String toString() {
    String out = getClass().getSimpleName();
    out += String.format("[type=%s,key=%s,value=%s,tpf=%s]", 
            getType().name(), getKey(), getValue(), getTpf());
    return out;
  }
}
