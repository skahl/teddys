package edu.teddys.objects;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import edu.teddys.effects.JetpackEffect;

/**
 *
 * @author skahl
 */
public class JetpackView {

  /**
   * The jetpack data object.
   */
  private Jetpack jetpack;
  private AssetManager assetManager;
  private Node mother;
  private Box box;
  private Geometry geo;
  private Material mat;
  /**
   * The jetpack effect object.
   */
  private JetpackEffect jetpackFx;

  public JetpackView(String name, ColorRGBA color, AssetManager assetManager) {
    mother = new Node(name);
    jetpackFx = new JetpackEffect("jetpack", assetManager);

    this.assetManager = assetManager;
    box = new Box(new Vector3f(0f, 0f, 0f), 1f, 1f, 1f);
    geo = new Geometry("boxGeo", box);

    mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", color);
    geo.setMaterial(mat);
    mother.attachChild(geo);

    mother.attachChild(jetpackFx.getNode());


  }

  public Jetpack getJetpack() {
    return jetpack;
  }

  public void setJetpack(Jetpack data) {
    jetpack = data;
  }

  public Node getNode() {
    return mother;
  }

  public void setEnabled(boolean enable) {
    jetpack.setEnabled(enable);
    jetpackFx.setEnabled(enable);
  }

  public boolean isEnabled() {
    return jetpack.isEnabled();
  }

  public void pause() {
    jetpackFx.pause();
  }

  public void unpause() {
    jetpackFx.unpause();
  }
}
