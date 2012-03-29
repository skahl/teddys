package edu.teddys.map;

import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import edu.teddys.MegaLogger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Represents the game configuration for starting the server that belongs to
 * one game map, containing the item and player spawn positions.
 * 
 * @author skahl
 */
public class GameMapConfig {
  private DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
  private Document document;
  private DocumentBuilder docBuilder;
  
  // Position boundaries, used for spawning players and items
  private ArrayList<PositionBoundary> positionBoundaries;
  // Background Color of the Scene
  private ColorRGBA backgroundColor;
  // Light List
  private LinkedList<Light> lightList;
  
  // parsing successfull boolean
  private boolean successfullyParsed = false;
  
  
  // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
  /**
   * PositionBoundary class
   * Stores two Vector3f, defining a space between two vectors.
   */
  public class PositionBoundary {
    Vector3f lB;
    Vector3f uB;
    
    public PositionBoundary(Vector3f lowerBound, Vector3f upperBound) {
      lB = lowerBound;
      uB = upperBound;
    }
    
    public Vector3f getLowerBound() {
      return lB;
    }
    
    public Vector3f getUpperBound() {
      return uB;
    }
    
    public void setLowerBound(Vector3f lB) {
      this.lB = lB;
    }
    
    public void setUpperBound(Vector3f uB) {
      this.uB = uB;
    }
  }

  // # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

  public GameMapConfig(String configPath) {
      try {
        File f = new File("assets/"+configPath+".xml");
        
        docBuilder = docFactory.newDocumentBuilder();
        document = docBuilder.parse(f);
        
        
        // parse position boundaries
        if(readPositionBoundaries()) {
          // parse background color
          if(readBackgroundColor()) {

            // parse lights
            if(readLights()) {
              successfullyParsed = true;
            }
          }
        }
          
        
        
      } catch (IOException e) {
        MegaLogger.getLogger().warn(configPath+".xml IOException: "+e.getMessage());
      } catch (ParserConfigurationException e) {
        MegaLogger.getLogger().warn(configPath+".xml could not be parsed: "+e.getMessage());
      } catch (SAXException e) {
        MegaLogger.getLogger().warn(configPath+".xml could not be parsed: "+e.getMessage());
      }
  }
  
  
  private boolean readPositionBoundaries() {
    positionBoundaries = new ArrayList<PositionBoundary>();
    
    NodeList spawnSpaces = document.getElementsByTagName("spawnSpace");
    for(int i=0; i<spawnSpaces.getLength(); i++) {
      Node el = spawnSpaces.item(i);
      NodeList nodes = el.getChildNodes();
      
      
      Vector3f lB = null;
      Vector3f uB = null;
      
      for(int o=0; o<nodes.getLength(); o++) {
        Node node = nodes.item(o);
        
        
        if(node.getNodeName().equals("lowerBound")) {
          lB = getVectorFromXML(node.getFirstChild().getNodeValue());
        } else if(node.getNodeName().equals("higherBound")) {
          uB = getVectorFromXML(node.getFirstChild().getNodeValue());
        }
      }
      
      if(lB != null && uB != null) {
        positionBoundaries.add(new PositionBoundary(lB, uB));
      }
    }
    
    if(positionBoundaries.size() > 0)
      return true;
    else
      return false;
  }
  
  private Vector3f getVectorFromXML(String vector) {
    String[] l = vector.split(",");
    
    Vector3f vec = new Vector3f(Float.valueOf(l[0]), Float.valueOf(l[1]), Float.valueOf(l[2]));
    
    return vec;
  }
  
  public ArrayList<PositionBoundary> getPositionBoundaries() {
    return positionBoundaries;
  }
  

  private boolean readBackgroundColor() {
    NodeList color = document.getElementsByTagName("backgroundColor");
    
    if(color.getLength() > 0) {
      Node node = color.item(0);
      Vector3f vec = getVectorFromXML(node.getFirstChild().getNodeValue());
      
      backgroundColor = new ColorRGBA(vec.x, vec.y, vec.z, 1.0f);
      
      return true;
    } else {
      return false;
    }
  }
  
  public ColorRGBA getBackgroundColor() {
    return backgroundColor;
  }
  
  private boolean readLights() {
    lightList = new LinkedList<Light>();
    
    NodeList lights = document.getElementsByTagName("light");
    
    for(int i=0; i<lights.getLength(); i++) {
      Node el = lights.item(i);
      NodeList nodes = el.getChildNodes();
      
      String name = null;
      String type = null;
      Vector3f position = null;
      ColorRGBA color = null;
      float radius = 0f;
      
      for(int o=0; o<nodes.getLength(); o++) {
        Node n = nodes.item(o);
        
        if(n.getNodeName().equals("type")) {
          type = n.getFirstChild().getNodeValue();
        } else if(n.getNodeName().contains("position")) {
          position = getVectorFromXML(n.getFirstChild().getNodeValue());
        } else if(n.getNodeName().equals("radius")) {
          radius = Float.valueOf(n.getFirstChild().getNodeValue());
        } else if(n.getNodeName().contains("color")) {
          Vector3f vec = getVectorFromXML(n.getFirstChild().getNodeValue());
          color = new ColorRGBA(vec.x, vec.y, vec.z, 1.0f);
        }
      }
      
      if(type.equals("Point")) {
        PointLight light = new PointLight();
        light.setPosition(position);
        light.setRadius(radius);
        light.setColor(color);
        lightList.add(light);
        
      } else if(type.equals("Directional")) {
        DirectionalLight light = new DirectionalLight();
        light.setDirection(position);
        light.setColor(color);
        lightList.add(light);
      }
    }
    
    if(lightList.size() > 0)
      return true;
    else
      return false;
    
  }
  
  public LinkedList<Light> getLights() {
    return lightList;
  }
  
  public boolean parsingSuccessfull() {
    return successfullyParsed;
  }
}
