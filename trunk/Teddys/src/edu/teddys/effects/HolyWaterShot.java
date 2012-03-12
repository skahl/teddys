
package edu.teddys.effects;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.controls.CollidableParticleControl;
import edu.teddys.states.Game;
import java.util.ArrayList;

/**
 * Implements the HolyWaterShot Effect. Triggering a splash of holy water. Water disappears on impact. 
 * 
 * @author skahl
 */
public class HolyWaterShot implements Effect {
  Node node;
  Material mat;
  ArrayList<CustomParticle> particles;
  CollidableParticleControl control;
  Vector3f vector = new Vector3f(1f, 0f, 0f);
  boolean triggerable = false;
  int numParticles = 8;
  
  public HolyWaterShot() {
    node = new Node("holyWaterGun"); // player names???
    particles = new ArrayList<CustomParticle>();
    
    mat = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
    mat.setTexture("Texture", Game.getInstance().getAssetManager().loadTexture("Textures/Effects/plush/plush.png"));

    mat.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
    mat.getAdditionalRenderState().setAlphaTest(true);
    
    
    for(int i=0; i<numParticles; i++) {
      
      particles.add(new HolyWaterParticle(mat));
    }
    
    control = new CollidableParticleControl(node, particles, true);
    node.addControl(control);
    control.setEnabled(false);
    
    Game.getInstance().getRootNode().attachChild(node);
  }
  

  public Node getNode() {
    return node;
  }

  public Vector3f getVector() {
    return vector;
  }

  public void setVector(Vector3f vector) {
    this.vector = vector;
  }

  public boolean isTriggerable() {
    return triggerable;
  }

  public void trigger() {
    if(triggerable) {
      //shoot
      control.setEnabled(true);
      
      triggerable = false;
    }
  }

  public void reset() {
    control.setEnabled(false);
    triggerable = true;
  }
  
}
