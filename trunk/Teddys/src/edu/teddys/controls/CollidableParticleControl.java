
package edu.teddys.controls;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
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
import edu.teddys.effects.CustomParticle;
import edu.teddys.objects.player.Player;
import edu.teddys.states.Game;
import java.util.ArrayList;

/**
 * Controls an array of collidable particles, checks for collisions and triggers effect.
 * 
 * @author sk
 */
public class CollidableParticleControl extends AbstractControl {
  Node mother;
  Geometry collideCube;
  boolean cubePerParticle;
  
  ArrayList<CustomParticle> particles;
  ArrayList<Player> collidedPlayerList;
  
  boolean collided = false;
  boolean collidedPlayer = false;
  
  public CollidableParticleControl(Node mother, ArrayList<CustomParticle> particles, boolean cubePerParticle) {
    this.mother = mother;
    this.particles = particles;
    this.cubePerParticle = cubePerParticle;
    
    if(cubePerParticle) {
      for(CustomParticle p : particles) {
        Node node = new Node(mother.getName());
        Box invBox = new Box(p.getWorldScale().x, p.getWorldScale().y, 0.5f);
        Geometry colCube = new Geometry(node.getName()+"geo", invBox);
        Material red = new Material(Game.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        red.setColor("Color", ColorRGBA.Red);
        colCube.setMaterial(red);
        colCube.setCullHint(Spatial.CullHint.Always); //make invisible
        node.attachChild(p);
        node.attachChild(colCube);
      }
    } else {
      // TODO: 
    }
    
    
    
    collidedPlayerList = new ArrayList<Player>();
  }

  @Override
  protected void controlUpdate(float tpf) {
    if(isEnabled()) {
      // update particle movements
      // rotate if rotationSpeed != 0f -> so that it can be bigger or smaller than 0
      for(CustomParticle particle : particles) {
        if(particle.getRotateSpeed() != 0f) {
          particle.getParent().rotate(0f, particle.getRotateSpeed()*tpf, 0f);
        }

        if(!particle.isFloating()) {
          if(!collided) {
            // if not floating or on ground
            // apply gravity and initial vector, until cube hits the ground and particle.y>=cube.y
            Vector3f vec = particle.getGravityVector().add(particle.getInitialVector());

            Vector3f wouldBeTrans = particle.getWorldTranslation().add(vec);

            particle.getParent().setLocalTranslation(wouldBeTrans); // TODO: local translation?
          }
        } else {
          // apply sinusoidal with given amplitude
          // move while timeToHalt > 0
          Vector3f vec = particle.getInitialVector().mult(particle.getTimeToHalt()-tpf);

          vec.y = (float) (vec.y * Math.sin(tpf) * particle.getFloatingSinusAmplitude());

          particle.getParent().setLocalTranslation(vec);
        } 

        // Check for collisions:
        CollisionResults cr = new CollisionResults();

        //particle.collideWith(Game.getInstance().getRootNode(), cr);

        if(cr.size() > 0) {
          collided = true; // had some collision

          for(CollisionResult c : cr) {
            String name = c.getGeometry().getName();

            if(name.contains("player")) { // had a collision with a player!
              collidedPlayerList.add(Player.getPlayerByNode(name));
              collidedPlayer = true;
            }
          }
        }
      }
    }
  }

  @Override
  protected void controlRender(RenderManager rm, ViewPort vp) {
  }

  public Control cloneForSpatial(Spatial spatial) {
    CollidableParticleControl control = new CollidableParticleControl(mother, particles, cubePerParticle);

    return control;
  }
  
  public boolean didCollide() {
    return collided;
  }
  
  public boolean didCollidePlayer() {
    return collidedPlayer;
  }
  
}
