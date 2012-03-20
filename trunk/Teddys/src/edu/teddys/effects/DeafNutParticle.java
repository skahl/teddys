
package edu.teddys.effects;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.shape.Quad;
import edu.teddys.states.Game;

/**
 *
 * @author skahl
 */
public class DeafNutParticle extends CustomParticle {
  Material mat;
  
  public DeafNutParticle(String name) {
    super(name);
    
    this.velocity = 5f;
    
    this.setMesh(new Quad(0.2f, 0.2f));
    
    mat = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Red);
    this.setMaterial(mat);
    
  }
  
}
