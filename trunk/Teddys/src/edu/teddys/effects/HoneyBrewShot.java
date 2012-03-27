
package edu.teddys.effects;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.objects.weapons.HoneyBrew;
import edu.teddys.states.Game;

/**
 * Implements the HoneyBrewShot Effect. Triggering a splash of deadly honey. 
 * Will create a pond of honey, deadly aswell.
 * 
 * @author skahl
 */
public class HoneyBrewShot extends RigidBodyControl implements Effect {
  HoneyBrew weapon;
  // control variables
  boolean canShoot;
  
  // Effect attributes
  Node mother;
  HoneyBrewParticle particle;
  Material honeyPart;
  ParticleEmitter honeyEffect;
//  ParticleEmmitter
  ParticleCollisionBox partColBox;
  
  
  public HoneyBrewShot(HoneyBrew weapon) {
    this.weapon = weapon;
    // init control variables
    canShoot = true;
    
    // init effect attributes
    mother = new Node("Honey Brew");
    particle = new HoneyBrewParticle(mother.getName());
    partColBox = new ParticleCollisionBox(mother.getName(), weapon, particle);
    
    // init particle emitter for honey brew shoot effect
    honeyPart = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
    honeyPart.setTexture("Texture", Game.getInstance().getAssetManager().loadTexture("Textures/Effects/honeyParticle.png"));
    honeyPart.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    honeyPart.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
    honeyPart.getAdditionalRenderState().setAlphaTest(true);
    
    honeyEffect = new ParticleEmitter("Honey", ParticleMesh.Type.Triangle, 10);
    honeyEffect.setMaterial(honeyPart);
    honeyEffect.setImagesX(1); honeyEffect.setImagesY(1);
    honeyEffect.getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_X);
    honeyEffect.setStartSize(0.2f);
    honeyEffect.setEndSize(0.02f);
    honeyEffect.setGravity(0,0,0);
    honeyEffect.setLowLife(0.2f);
    honeyEffect.setHighLife(0.3f);
    honeyEffect.getParticleInfluencer().setVelocityVariation(0.5f);  
    honeyEffect.setParticlesPerSec(0);
    
    mother.attachChild(honeyEffect);
    
    this.setCollisionShape(partColBox.getCollisionShape());
    
    setCcdMotionThreshold(partColBox.getCollisionShape().getHalfExtents().x);
    partColBox.getNode().addControl(this);
    
    setMass(1f);
    this.setDamping(0.9f, 0.9f);
  }
  
  @Override
  public void update(float tpf) {
    super.update(tpf);
    
    if(partColBox.collidedPlayer()) {
      reset();
    }
  }

  
  @Override
  public void trigger() {
        
    if(canShoot) {
      canShoot = false;
      
      // attach the collision cube to the root node, not the player node!
      Game.getInstance().getApp().enqueue(new AttachToNodeCallable(
              Game.getInstance().getRootNode(), partColBox.getNode()));
      
      Vector3f loc = mother.getWorldTranslation();
      Quaternion rot = mother.getLocalRotation();
      if(particle.getVector().x > 0f) {
        loc.addLocal(1.1f, 0.3f, 0f);
      } else {
        loc.addLocal(-1.1f, 0.3f, 0f);
      }
      setPhysicsLocation(loc);
      setPhysicsRotation(rot);
      
      
      // activate the honeyEffect
      honeyEffect.getParticleInfluencer().setInitialVelocity(particle.getVector());
      honeyEffect.setParticlesPerSec(10);
      
      setEnabled(true);
    }
  }
  
  @Override
  public void reset() {
    if(!canShoot) {
      
      if(partColBox.collidedPlayer()) {
        canShoot = true;
        setEnabled(false);

        // remove the cube from the field
        Game.getInstance().getApp().enqueue(new DetachFromNodeCallable(
                Game.getInstance().getRootNode(), partColBox.getNode()));
        
        // reset rigidBody physics
        
      }
      
      honeyEffect.setParticlesPerSec(0);
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
