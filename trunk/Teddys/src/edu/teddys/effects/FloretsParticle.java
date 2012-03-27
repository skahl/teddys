
package edu.teddys.effects;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.scene.shape.Quad;
import edu.teddys.states.Game;

/**
 *
 * @author skahl
 */
public class FloretsParticle extends CustomParticle {
  Material mat;
  
  public FloretsParticle(String name) {
    super(name);
    
    this.setMesh(new Quad(0.3f, 0.6f));
    
    mat = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
    mat.setTexture("Texture", Game.getInstance().getAssetManager().loadTexture("Textures/Effects/flowerParticle.png"));
    mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
    mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    this.setMaterial(mat);
    
  }
  
}
