/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.teddys.objects.box.items;

import com.jme3.math.Vector3f;
import edu.teddys.effects.ItemBoxSpawn;

/**
 *
 * @author cm
 */
public class Item {
  private String name;
  private Integer spawningRate;
  private ItemBoxSpawn itemBoxSpawn = new ItemBoxSpawn();
  
  public ItemBoxSpawn getItemBoxSpawn() {
    return itemBoxSpawn;
  }
  
  public void setSpawnPoint(Vector3f point) {
    itemBoxSpawn.setVector(point);
  }
  
  public Vector3f getSpawnPoint() {
    return itemBoxSpawn.getVector();
  }
  
  public void spawn() {
    itemBoxSpawn.trigger();
  }
}
