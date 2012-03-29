
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
public class DeafNutParticle extends CustomParticle {
  Material mat;
  
  public DeafNutParticle(String name) {
    super(name);
    
    this.velocity = 10f;
    
    this.setMesh(new Quad(0.2f, 0.2f));
    
    mat = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setTexture("ColorMap", Game.getInstance().getAssetManager().loadTexture("Textures/Effects/deafnutParticle.png"));
    mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
    mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    mat.getAdditionalRenderState().setAlphaTest(true);
    this.setMaterial(mat);
    
  }
  
}
