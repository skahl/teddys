
package edu.teddys.effects;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.shape.Quad;
import edu.teddys.states.Game;

/**
 *
 * @author skahl
 */
public class HolyWaterParticle extends CustomParticle {
  Material mat;
  
  public HolyWaterParticle(String name) {
    super(name);
    
    this.setMesh(new Quad(0.6f, 0.6f));
    
    mat = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Red);
    this.setMaterial(mat);
    
    
  }
}
