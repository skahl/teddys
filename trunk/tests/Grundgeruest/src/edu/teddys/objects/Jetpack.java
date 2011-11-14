
package edu.teddys.objects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author skahl
 */
public class Jetpack {
    
    private AssetManager assetManager;
    private Node mother;
    private Box box;
    private Geometry geo;
    private Material mat;
    private ParticleEmitter pe;
    
    public Jetpack(String name, ColorRGBA color, AssetManager assetManager) {
        mother = new Node(name);
        
        this.assetManager = assetManager;
        box = new Box(new Vector3f(0f, 0f, 0f), 1f, 1f, 1f);
        geo = new Geometry("boxGeo", box);
        
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geo.setMaterial(mat);
        mother.attachChild(geo);
        
        pe = new ParticleEmitter("jetPack",
                ParticleMesh.Type.Triangle, 30);
        Material pmat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        pmat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/Explosion/smoketrail.png"));
        pe.setMaterial(pmat);
        pe.setImagesX(1); pe.setImagesY(3);
        pe.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        pe.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        pe.getParticleInfluencer().setInitialVelocity(new Vector3f(0,2,0));
        pe.setStartSize(1.5f);
        pe.setEndSize(0.1f);
        pe.setGravity(0,0,0);
        pe.setLowLife(0.5f);
        pe.setHighLife(3f);
        pe.getParticleInfluencer().setVelocityVariation(0.3f);
        
        
        mother.attachChild(pe);
    }
    
    public Node getNode() {
        return mother;
    }
    
    public ParticleEmitter getParticleEmitter() {
        return pe;
    }
        
}
