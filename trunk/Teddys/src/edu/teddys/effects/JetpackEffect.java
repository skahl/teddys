
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
 *
 * @author skahl
 */
public class JetpackEffect implements Effect {
    Node mother;
    ParticleEmitter pe;
    Material mat;
    AssetManager assetManager;
    String name;
    int particlesPerSec;
    Vector3f velocity;
    
    boolean enabled;
    
    public JetpackEffect(String name, AssetManager assetManager) {
        this.name = name;
        this.assetManager = assetManager;
        particlesPerSec = 30;
        enabled = false;
        velocity = new Vector3f(-1f, -4f, 0f);
        
        mother = new Node("node_"+name);
        pe = new ParticleEmitter("jetPack",
                ParticleMesh.Type.Triangle, 40);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        
        init();
    }
    
    private void init() {
        
        mat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/smoke/smoke.png"));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        mat.getAdditionalRenderState().setAlphaTest(true);
        
        pe.setMaterial(mat);
        pe.setImagesX(15); pe.setImagesY(1);
        pe.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        pe.setStartColor(new ColorRGBA(1f, 1f, 0.7f, 1f)); // yellow-ish
        pe.getParticleInfluencer().setInitialVelocity(velocity);
        pe.setStartSize(0.25f);
        pe.setEndSize(0.04f);
        pe.setGravity(0,0,0);
        pe.setLowLife(0.2f);
        pe.setHighLife(0.4f);
        pe.getParticleInfluencer().setVelocityVariation(0.1f);  
        pe.setParticlesPerSec(0);
        
        
        mother.attachChild(pe);
    }
    
    public boolean isTriggerable() {
        return enabled;
    }
    
    public void trigger() {
      setEnabled(true);
    }
    
    public void reset() {
      setEnabled(false);
    }
    
    public Vector3f getVector() {
      return velocity;
    }
    
    public void setVector(Vector3f vector) {
      velocity = vector;
    }
    
    private void setEnabled(boolean enable) {
        if(!enable) {
            pe.setParticlesPerSec(0);
            enabled = false;
        } else {
            pe.setParticlesPerSec(particlesPerSec);
            enabled = true;
        }
    }
    
    public Node getNode() {
        return mother;
    }
    
    public void switchVelocity() {
        velocity.x = velocity.x * -1f;
        pe.getParticleInfluencer().setInitialVelocity(velocity);
    }
    
}
