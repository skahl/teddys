
package edu.teddys.effects;

import com.jme3.bullet.control.GhostControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.callables.DetachFromNodeCallable;
import edu.teddys.objects.weapons.Rocket;
import edu.teddys.states.Game;

/**
 * Implements the RocketShot Effect. Triggering an unstoppable rocket. Smoke appears. 
 * 
 * @author skahl
 */
public class RocketShot extends GhostControl implements Effect {
  Rocket weapon;
  // control variables
  boolean canShoot;
  
  // Effect attributes
  Node mother;
  RocketParticle particle;
  Vector3f triggerVector;
  ParticleEmitter flameEffect;
  Explosion explosion;
  Material flames;
  ParticleCollisionBox partColBox;
  
  
  
  public RocketShot(Rocket weapon) {
    this.weapon = weapon;
    // init control variables
    canShoot = true;
    
    // init effect attributes
    mother = new Node("Teddy Rocket");
    particle = new RocketParticle(mother.getName());
    
    // init the explosion
    explosion = new Explosion(1f);
    partColBox = new ParticleCollisionBox(mother.getName(), weapon, particle);
    
    // init particle emitter for rocket flames effect
    flames = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
    flames.setTexture("Texture", Game.getInstance().getAssetManager().loadTexture("Textures/Effects/Smoke/Smoke.png"));
    flames.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    flames.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
    flames.getAdditionalRenderState().setAlphaTest(true);
    
    flameEffect = new ParticleEmitter("Flames", ParticleMesh.Type.Triangle, 10);
    flameEffect.setMaterial(flames);
    flameEffect.setImagesX(15); flameEffect.setImagesY(1);
    flameEffect.setEndColor(new ColorRGBA(1f, 0.5f, 0.5f, 1f));   // red
    flameEffect.setStartColor(new ColorRGBA(1f, 1f, 1f, 1f)); // yellow-ish
    flameEffect.getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_X);
    flameEffect.setStartSize(0.2f);
    flameEffect.setEndSize(0.02f);
    flameEffect.setGravity(0,0,0);
    flameEffect.setLowLife(0.2f);
    flameEffect.setHighLife(0.3f);
    flameEffect.getParticleInfluencer().setVelocityVariation(0.1f);  
    flameEffect.setParticlesPerSec(0);
    
    partColBox.getNode().attachChild(flameEffect);
    
    this.setCollisionShape(partColBox.getCollisionShape());
    
    partColBox.getNode().addControl(this);
  }
  
  @Override
  public void update(float tpf) {
    super.update(tpf);
    
    if(partColBox.collided()) {
      if(partColBox.collidedPlayer()) {
        
      }
      
      reset();
    } else {
      
      // linear force to apply
      Vector3f force = triggerVector;
      // apply velocity
      force = force.mult(particle.getVelocity());
      
      force = force.mult(tpf);
      
      partColBox.getNode().move(force);
    }
  }

  
  @Override
  public void trigger() {
    
    if(canShoot) {
      canShoot = false;
      
      
      // attach the collision cube to the root node, not the player node!
      if(!partColBox.getNode().hasAncestor(Game.getInstance().getRootNode())) {
        Game.getInstance().getApp().enqueue(new AttachToNodeCallable(
              Game.getInstance().getRootNode(), partColBox.getNode()));
      }
      
      Vector3f loc = mother.getWorldTranslation();
      loc.addLocal(particle.getVector());
      
      partColBox.getNode().setLocalTranslation(loc);
      
      // make visible
      partColBox.getNode().setCullHint(CullHint.Inherit);
      
      // store the vector at trigger time, so that the rocket can fly uncontrolled
      triggerVector = new Vector3f(particle.getVector());
      
      // rotate the partColBox, so that the particle looks good
      partColBox.getNode().rotateUpTo(particle.getVector());
      partColBox.getNode().rotate(0f, 0f, -FastMath.HALF_PI);
      
      // activate the flameEffect
      flameEffect.getParticleInfluencer().setInitialVelocity(triggerVector.negate());
      flameEffect.setParticlesPerSec(10);
      
      setEnabled(true);
    }
  }
  
  @Override
  public void reset() {
    if(!canShoot) {
      
      canShoot = true;
      setEnabled(false);
      
      // position the explosion on the root node and explode
      explosion.trigger(getPhysicsLocation());
      
      // deactivate the flameEffect
      flameEffect.setParticlesPerSec(0);

      // remove the cube from the field
      partColBox.getNode().setCullHint(CullHint.Always);
      
      
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
