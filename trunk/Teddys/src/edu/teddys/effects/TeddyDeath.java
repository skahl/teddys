
package edu.teddys.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * When a Teddy dies, he vanishes with a puff of smoke and leaves a cloud of plush.
 *
 * @author skahl
 */
public class TeddyDeath {
  
    Node mother;
    ParticleEmitter plush;
    ParticleEmitter smoke;
    Material matPlush;
    Material matSmoke;
    
    AssetManager assetManager;
    
    
    
    public TeddyDeath(AssetManager assetManager) {
      mother = new Node("DeathNode");
      this.assetManager = assetManager;

      int numSmoke = 8;
      int numPlush = 10;

      plush = new ParticleEmitter("DeathPlush", ParticleMesh.Type.Triangle, numPlush);
      smoke = new ParticleEmitter("DeathSmoke", ParticleMesh.Type.Triangle, numSmoke);

      matPlush = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
      matPlush.setTexture("Texture", assetManager.loadTexture("Textures/Effects/plush/plush.png"));
      
      matPlush.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
      matPlush.getAdditionalRenderState().setAlphaTest(true);
      
      matSmoke = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
      matSmoke.setTexture("Texture", assetManager.loadTexture("Textures/Effects/Smoke/Smoke.png"));
      
      matSmoke.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
      matSmoke.getAdditionalRenderState().setAlphaTest(true);

      plush.setMaterial(matPlush);
      smoke.setMaterial(matSmoke);
      
      plush.setImagesX(1); plush.setImagesY(1);
      plush.setStartColor(ColorRGBA.White);
      plush.setEndColor(ColorRGBA.White);
      plush.getParticleInfluencer().setInitialVelocity(new Vector3f(0f, 4f, 0f));
      plush.setRotateSpeed(4f);
      plush.setStartSize(0.1f);
      plush.setEndSize(0.1f);
      plush.setLowLife(0.5f);
      plush.setHighLife(0.7f);
      plush.setGravity(0,6,0);
      plush.setParticlesPerSec(0);
      plush.getParticleInfluencer().setVelocityVariation(0.6f);
      
      smoke.setImagesX(15); smoke.setImagesY(1);
      smoke.setStartColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 0.7f));
      smoke.setEndColor(new ColorRGBA(0.8f, 0.8f, 0.8f, 0.0f));
      smoke.getParticleInfluencer().setInitialVelocity(new Vector3f(0f, 2f, 0f));
      smoke.setStartSize(1f);
      smoke.setLowLife(0.4f);
      smoke.setHighLife(0.5f);
      smoke.setGravity(0,1,0);
      smoke.setParticlesPerSec(0);
      smoke.getParticleInfluencer().setVelocityVariation(1f);

      mother.attachChild(plush);
      mother.attachChild(smoke);
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
    public void die() {
      smoke.emitAllParticles();
      plush.emitAllParticles();
    }
}
