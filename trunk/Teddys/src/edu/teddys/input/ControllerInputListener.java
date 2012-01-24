package edu.teddys.input;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author besient
 */
public class ControllerInputListener implements RawInputListener {

  private List<Integer> keys = new ArrayList<Integer>();
  private Map<Vector2f,Integer> mouse = new HashMap<Vector2f,Integer>();
  

//  public ControllerInputListener(Spatial player, CollisionShape collisionShape, float stepHeight) {
//    for(Field keyField : KeyInput.class.getDeclaredFields()) {
//      
//    }
//  }
  public ControllerInputListener() {
    
  }

  /**
   * 
   * Frees the list afterwards.
   * 
   * @return A copy of the keys list.
   */
  public List<Integer> getKeys() {
    List<Integer> out = new ArrayList<Integer>(keys);
    keys.clear();
    return out;
  }

  public void setKeys(List<Integer> keys) {
    this.keys = keys;
  }

  /**
   * Frees the map afterwards.
   * 
   * @return A copy of the map.
   */
  public Map<Vector2f, Integer> getMouse() {
    Map<Vector2f, Integer> out = new HashMap<Vector2f, Integer>(mouse);
    mouse.clear();
    return out;
  }

  public void setMouse(Map<Vector2f, Integer> mouse) {
    this.mouse = mouse;
  }

  public void beginInput() {
  }

  public void endInput() {
  }

  public void onJoyAxisEvent(JoyAxisEvent evt) {
  }

  public void onJoyButtonEvent(JoyButtonEvent evt) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void onMouseMotionEvent(MouseMotionEvent evt) {
  }

  public void onMouseButtonEvent(MouseButtonEvent evt) {
    mouse.put(new Vector2f((float)evt.getX(), (float)evt.getY()), evt.getButtonIndex());
  }

  public void onKeyEvent(KeyInputEvent evt) {
    keys.add(evt.getKeyCode());
  }

  public void onTouchEvent(TouchEvent evt) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}