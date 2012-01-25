
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
    
    public TeddyVisual(Node node, AssetManager assetManager) {
        isRunning = false;
        runLeft = false;
        

        quad = new Quad(0.6f, 0.79f); //Box(0.3f, 0.3f, 0.01f);
        geo = new Geometry(node.getName(), quad);
        geo.setLocalTranslation(new Vector3f(-0.3f,-0.3f, 0.0f));
        
        Texture blueStand = assetManager.loadTexture("Textures/teddy_blue_baerenpistole_stand.png");
        
        standing = assetManager.loadMaterial("Materials/teddyStand/teddyStand.j3m");
        standing.setTexture("TexMap", blueStand);
        standing.setInt("SelectedTile", 3);
        standing.setInt("MaxTiles", 7);
        standing.setBoolean("Mirrored", false);
        standing.getAdditionalRenderState().setAlphaTest(true);
        
        Texture blueRun = assetManager.loadTexture("Textures/teddy_blue_baerenpistole_run.png");
        
        running = assetManager.loadMaterial("Materials/teddyRun/teddyRun.j3m");
        running.setTexture("TexMap", blueRun);
        running.setInt("SelectedTile", 3);
        running.setInt("MaxTilesX", 7);
        running.setInt("MaxTilesY", 4);
        running.setFloat("Speed", 8.0f);
        running.setBoolean("Reverse", false);
        running.getAdditionalRenderState().setAlphaTest(true);
        
        
        geo.setMaterial(standing);
        
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
    
    public void runLeft() {
        if(!isRunning) {
            isRunning = true;
            geo.setMaterial(running);
        }
    
        if(!runLeft) {
            runLeft = true;
            running.setBoolean("Mirrored", runLeft);
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
        }
    }
    
    public void stand() {
        if(isRunning) {
            isRunning = false;
            geo.setMaterial(standing);
        }
    }
    
    public void viewDir(int dir) {
        lookDir = dir;
        
        
    }
}
