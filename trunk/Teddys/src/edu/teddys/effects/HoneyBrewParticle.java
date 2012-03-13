
package edu.teddys.effects;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Quad;
import edu.teddys.states.Game;

/**
 *
 * @author skahl
 */
public class HoneyBrewParticle extends CustomParticle {
  Material mat;
  
  public HoneyBrewParticle(String name) {
    super(name);
    
    this.setMesh(new Quad(1f, 0.2f));
    
    mat = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Red);
    this.setMaterial(mat);
    
    // setup particle properties
    isFloating = true;
    initialVector = Vector3f.ZERO;
    gravityVector = Vector3f.ZERO;
    
    
  }
  
  protected void init(String name) {
    this.setName(name);
  }
  
}
