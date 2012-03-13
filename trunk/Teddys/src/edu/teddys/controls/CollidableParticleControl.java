
package edu.teddys.controls;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import edu.teddys.MegaLogger;
import edu.teddys.effects.CustomParticle;
import edu.teddys.states.Game;

/**
 * Controls an array of collidable particles, checks for collisions and triggers effect.
 * 
 * @author sk
 */
public class CollidableParticleControl extends AbstractControl {
  Node node;
  CustomParticle particle;
  Geometry colCube;
  String name;
  boolean enabled;
  boolean rigidBody;
  
  GeometryCollisionGhostControl colControl;
  
  public CollidableParticleControl(String name, CustomParticle particle) {
    this.particle = particle;
    this.name = name;
    enabled = false;
    this.rigidBody = rigidBody;
    
    node = new Node(name);
    Box invBox = new Box(particle.getQuadWidth()/2f, particle.getQuadHeight()/2f, 0.5f);
    colCube = new Geometry(name, invBox);
    Material red = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    red.setColor("Color", ColorRGBA.Blue);
    colCube.setMaterial(red);
    colCube.setCullHint(Spatial.CullHint.Always); //make invisible
    
    colCube.setLocalTranslation(particle.getQuadWidth()/2f, particle.getQuadHeight()/2f, 0f);
    
    colControl = new GeometryCollisionGhostControl(new BoxCollisionShape(
            new Vector3f(invBox.xExtent, invBox.yExtent, invBox.zExtent)));
    colCube.addControl(colControl);
    
    // attach particle and collision cube to node
    node.attachChild(particle);
    node.attachChild(colCube);
  }

  @Override
  protected void controlUpdate(float tpf) {
    
    // update particle movements
    // rotate if rotationSpeed != 0f -> so that it can be bigger or smaller than 0

    if(particle.getRotateSpeed() != 0f) {
      node.rotate(0f, particle.getRotateSpeed()*tpf, 0f);
    }

    if(!particle.isFloating()) {
      // if not floating or on ground
      // apply gravity and initial vector, until cube hits the ground and particle.y>=cube.y
      Vector3f vec = particle.getGravityVector().add(particle.getInitialVector());

      Vector3f wouldBeTrans = particle.getWorldTranslation().add(vec);

      node.setLocalTranslation(wouldBeTrans); // TODO: local translation?
    } else {
      // apply sinusoidal with given amplitude
      // move while timeToHalt > 0
      Vector3f vec = Vector3f.ZERO;
      
      if(particle.getTimeToHalt() > 0f) {
        vec = particle.getInitialVector().mult(particle.getTimeToHalt()-tpf);
      }
      
      if(particle.getFloatingSinusAmplitude() > 0f) {
        vec.y = (float) (vec.y * Math.sin(tpf) * particle.getFloatingSinusAmplitude());
      }
      
      node.setLocalTranslation(vec);
    } 
  }
  
  @Override
  public void setEnabled(boolean enable) {
    super.setEnabled(enable);
    
    if(!enabled && enable) {
      // enable collision listener and particle control
      enabled = true;
      colControl.setListen(true);
    } else if(enabled && !enable) {
      // disable collision listener and particle control
      enabled = false;
      colControl.setListen(false);
    }
  }

  @Override
  protected void controlRender(RenderManager rm, ViewPort vp) {
  }

  public Control cloneForSpatial(Spatial spatial) {
    CollidableParticleControl control = new CollidableParticleControl(name, particle);

    return control;
  }
  
  public GeometryCollisionGhostControl getGeometryCollisionControl() {
    return colControl;
  }

  public Node getNode() {
    return node;
  }

}
