
package edu.teddys.objects.player;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import edu.teddys.MegaLogger;
import edu.teddys.effects.GunShot;
import edu.teddys.effects.JetpackEffect;
import edu.teddys.effects.ShotBaerenpistole;
import edu.teddys.input.ActionControllerEnum;
import edu.teddys.input.AnalogControllerEnum;

/**
 *
 * @author skahl
 */
public class TeddyVisual {
    
    // control attributes
    boolean isRunning;
    boolean runLeft;
    int lookDir;
    
    // visual attributes
    Quad quad;
    Geometry geo;
    Material standing;
    Material running;
    
    // effect attributes
    JetpackEffect jetpackFx;
    GunShot currentWeapon;
    
    
    public TeddyVisual(Node node, AssetManager assetManager) {
        // control init
        isRunning = false;
        runLeft = false;
        lookDir = 3; // default: look streight ahead!
        
        // effect init
        jetpackFx = new JetpackEffect(node.getName(), assetManager);
        jetpackFx.getNode().setLocalTranslation(-0.3f, -0.25f, 0.0f);
        node.attachChild(jetpackFx.getNode());
        
        // gun init
        currentWeapon = new ShotBaerenpistole(node.getName(), assetManager);
        currentWeapon.getNode().setLocalTranslation(0.2f, 0.1f, 0f);
        node.attachChild(currentWeapon.getNode());
        
        // quad and materials init
        quad = new Quad(0.6f, 0.79f); //Box(0.3f, 0.3f, 0.01f);
        geo = new Geometry(node.getName(), quad);
        geo.setLocalTranslation(new Vector3f(-0.3f,-0.3f, 0.0f));
        
        // texture and material for standing teddy
        Texture blueStand = assetManager.loadTexture("Textures/teddy_blue_baerenpistole_stand.png");
        
        standing = assetManager.loadMaterial("Materials/teddyStand/teddyStand.j3m");
        standing.getAdditionalRenderState().setAlphaTest(true);
        standing.setTexture("TexMap", blueStand);
        standing.setInt("SelectedTile", lookDir);
        standing.setInt("MaxTiles", 7);
        standing.setBoolean("Mirrored", false);
        
        // texture and material for running teddy (with animation)
        Texture blueRun = assetManager.loadTexture("Textures/teddy_blue_baerenpistole_run.png");
        
        running = assetManager.loadMaterial("Materials/teddyRun/teddyRun.j3m");
        running.getAdditionalRenderState().setAlphaTest(true);
        running.setTexture("TexMap", blueRun);
        running.setInt("SelectedTile", lookDir);
        running.setInt("MaxTilesX", 7);
        running.setInt("MaxTilesY", 4);
        running.setFloat("Speed", 8.0f);
        running.setBoolean("Mirrored", false);
        running.setBoolean("Reverse", false);
        
        
        geo.setMaterial(standing);
        
        // shadow and transparency settings
        geo.setShadowMode(ShadowMode.Cast);
        geo.setQueueBucket(Bucket.Transparent);
        
        node.attachChild(geo);
    }
    
    public Geometry getGeo() {
        return geo;
    }

    public Material getMat() {
        return geo.getMaterial();
    }
    
    public float getWidth() {
        return quad.getWidth();//.getXExtent();
    }
    
    public float getHeight() {
        return quad.getHeight();//.getYExtent();
    }

    public Quad getBox() {
        return quad;
    }
    
    public GunShot getWeapon() {
        return currentWeapon;
    }
    
    public void runLeft() {
        if(!isRunning) {
            isRunning = true;
            geo.setMaterial(running);
        }
    
        if(!runLeft) {
            runLeft = true;
            running.setBoolean("Mirrored", runLeft);
            
            // move jetpack
            jetpackFx.getNode().setLocalTranslation(0.30f, -0.25f, 0.0f);
            jetpackFx.switchVelocity();
        }
    }
    
    public void runRight() {
        if(!isRunning) {
            isRunning = true;
            geo.setMaterial(running);
        }
        
        if(runLeft) {
            runLeft = false;
            running.setBoolean("Mirrored", runLeft);
            
            // move jetpack
            jetpackFx.getNode().setLocalTranslation(-0.30f, -0.25f, 0.0f);
            jetpackFx.switchVelocity();
        }
    }
    
    public void stand() {
        if(isRunning) {
            isRunning = false;
            
            geo.setMaterial(standing);
            standing.setInt("SelectedTile", lookDir);
            standing.setBoolean("Mirrored", runLeft);
        }
    }
    
    public void viewDir(int dir) {
        lookDir = dir;
    }
    
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(AnalogControllerEnum.MOVE_LEFT.name())) {
            runLeft();
        } else if (name.equals(AnalogControllerEnum.MOVE_RIGHT.name())) {
            runRight();
        }
        
        if (name.equals(AnalogControllerEnum.WEAPON.name())) {
            currentWeapon.shoot();
        }

    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(ActionControllerEnum.JETPACK.name())) {
            if (isPressed) {
                jetpackFx.setEnabled(true);
            } else {
                jetpackFx.setEnabled(false);
            }
        }
    }
}
