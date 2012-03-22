
package edu.teddys.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import edu.teddys.callables.AttachToNodeCallable;
import edu.teddys.states.Game;
import org.apache.commons.math.util.FastMath;

/**
 * When a Teddy dies, he vanishes with a puff of smoke and leaves a cloud of plush.
 *
 * @author skahl
 */
public class Explosion {
  
    Node mother;
    ParticleEmitter fire;
    ParticleEmitter smoke;
    Material matFire;
    Material matSmoke;
    
    AssetManager assetManager;
    
    
    
    public Explosion(float size) {
      assetManager = Game.getInstance().getAssetManager();
      
      mother = new Node("Explosion");

      int numSmoke = FastMath.round(8*size);
      int numFire = FastMath.round(15*size);

      fire = new ParticleEmitter("Fire", ParticleMesh.Type.Triangle, numFire);
      smoke = new ParticleEmitter("Smoke", ParticleMesh.Type.Triangle, numSmoke);

      matFire = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
      matFire.setTexture("Texture", assetManager.loadTexture("Textures/Effects/Smoke/Smoke.png"));
      
      matFire.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
      matFire.getAdditionalRenderState().setAlphaTest(true);
      
      matSmoke = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
      matSmoke.setTexture("Texture", assetManager.loadTexture("Textures/Effects/Smoke/Smoke.png"));
      
      matSmoke.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
      matSmoke.getAdditionalRenderState().setAlphaTest(true);

      fire.setMaterial(matFire);
      smoke.setMaterial(matSmoke);
      
      fire.setImagesX(15); fire.setImagesY(1);
      fire.setEndColor(new ColorRGBA(1f, 0.3f, 0.3f, 1f));   // light-red
      fire.setStartColor(new ColorRGBA(1f, 1f, 0.7f, 1f)); // yellow-ish
      fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0f, 2f, 0f));
      fire.setStartSize(0.1f);
      fire.setLowLife(0.1f);
      fire.setHighLife(0.2f);
      fire.setGravity(0,1,0);
      fire.setParticlesPerSec(0);
      fire.getParticleInfluencer().setVelocityVariation(1f);
      
      smoke.setImagesX(15); smoke.setImagesY(1);
      smoke.setStartColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 0.7f));
      smoke.setEndColor(new ColorRGBA(0.8f, 0.8f, 0.8f, 0.0f));
      smoke.getParticleInfluencer().setInitialVelocity(new Vector3f(0f, 2f, 0f));
      smoke.setStartSize(0.2f);
      smoke.setLowLife(0.1f);
      smoke.setHighLife(0.2f);
      smoke.setGravity(0,1,0);
      smoke.setParticlesPerSec(0);
      smoke.getParticleInfluencer().setVelocityVariation(1f);

      mother.attachChild(fire);
      mother.attachChild(smoke);
      
      Game.getInstance().getApp().enqueue(new AttachToNodeCallable(
              Game.getInstance().getRootNode(), mother));
    }
    
    /**
     * If you want to attach the effect somewhere, get this node.
     * 
     * @return Returns the node the effect is attached to.
     */
    public Node getNode() {
      return mother;
    }
    
    /**
     * In order to trigger the effect, call this method.
     */
    public void trigger(Vector3f location) {
      mother.setLocalTranslation(location);
      
      smoke.emitAllParticles();
      fire.emitAllParticles();
    }
}
