
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
public class HoneyBrewParticle extends CustomParticle {
  Material mat;
  
  public HoneyBrewParticle(String name) {
    super(name);
    
    this.setMesh(new Quad(1f, 0.2f));
    
    mat = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setTexture("ColorMap", Game.getInstance().getAssetManager().loadTexture("Textures/Effects/HoneyBrew.png"));
    mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    //mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
    this.setMaterial(mat);
    
  }
  
}
