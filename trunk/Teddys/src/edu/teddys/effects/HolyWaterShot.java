
package edu.teddys.effects;

import com.jme3.bullet.control.GhostControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.objects.weapons.HolyWater;
import edu.teddys.states.Game;


/**
 * Implements the HolyWaterShot Effect. Triggering a splash of holy water. Water disappears on impact. 
 * 
 * @author skahl
 */
public class HolyWaterShot extends GhostControl implements Effect {
  HolyWater weapon;
  // control variables
  boolean canShoot;
  
  // Effect attributes
  Node mother;
  HolyWaterParticle particle;
  
  Material holyPart;
  ParticleEmitter holyEffect;
//  ParticleEmmitter
  ParticleCollisionBox partColBox;
  
  
  public HolyWaterShot(HolyWater weapon) {
    this.weapon = weapon;
    // init control variables
    canShoot = true;
    
    // init effect attributes
    mother = new Node("Holy Water");
    particle = new HolyWaterParticle(mother.getName());
    partColBox = new ParticleCollisionBox(mother.getName(), weapon, particle);
    
    
    // init particle emitter for holy water shoot effect
    holyPart = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
    holyPart.setTexture("Texture", Game.getInstance().getAssetManager().loadTexture("Textures/Effects/holywaterParticle.png"));
    holyPart.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    holyPart.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
    holyPart.getAdditionalRenderState().setAlphaTest(true);
    
    holyEffect = new ParticleEmitter("HolyWater", ParticleMesh.Type.Triangle, 10);
    holyEffect.setMaterial(holyPart);
    holyEffect.setImagesX(1); holyEffect.setImagesY(1);
    holyEffect.getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_X);
    holyEffect.setStartSize(0.2f);
    holyEffect.setEndSize(0.02f);
    holyEffect.setGravity(0,0,0);
    holyEffect.setLowLife(0.2f);
    holyEffect.setHighLife(0.3f);
    holyEffect.getParticleInfluencer().setVelocityVariation(0.5f);  
    holyEffect.setParticlesPerSec(0);
    
    mother.attachChild(holyEffect);
    
    
    this.setCollisionShape(partColBox.getCollisionShape());
    
    partColBox.hideParticle(true);
    
    partColBox.getNode().addControl(this);
  }
  
  @Override
  public void trigger() {
    
    
    if(canShoot) {
      canShoot = false;
      
      Game.getInstance().getApp().enqueue(new AttachToNodeCallable(mother, partColBox.getNode()));
      
      if(particle.getVector().x > 0f) {
        partColBox.getNode().setLocalTranslation(0.8f, 0.1f, 0f);
      } else {
        partColBox.getNode().setLocalTranslation(-0.8f, 0.1f, 0f);
      }
      
      
      // activate the holyEffect
      holyEffect.getParticleInfluencer().setInitialVelocity(particle.getVector());
      holyEffect.setParticlesPerSec(10);
      
      setEnabled(true);
    }
  }
  
  @Override
  public void reset() {
    if(!canShoot) {
      canShoot = true;
      
      setEnabled(false);
      
      Game.getInstance().getApp().enqueue(new DetachFromNodeCallable(mother, partColBox.getNode()));
    }
  }
  
  @Override
  public void setEnabled(boolean en) { 
    super.setEnabled(en);
    
    // enable collision listening
    partColBox.setEnabled(en);
  }
  
  @Override
  public boolean isEnabled() {
    return super.isEnabled();
  }

  public Node getNode() {
    return mother;
  }

  public Vector3f getVector() {
    return particle.getVector();
  }

  public void setVector(Vector3f vector) {
    particle.setVector(vector);
  }

  public boolean isTriggerable() {
    return canShoot;
  }
}
