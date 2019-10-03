/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import static common.Statics.getAssetManager;

/**
 *
 * @author steph
 */
public class GArrow extends Node {
  
  /**
   * Create an arrow.
   * @param node The node to attach this graphical element
   * @param name The name of this graphical element
   * @param color The color of this graphical element
   * @param direction The direction of this graphical element
   * @param length The length of this graphical element
   */
  GArrow(Node node, String name, ColorRGBA color, Vector3f direction, float length)
  {
    Arrow arrow = new Arrow(direction);
    arrow.setLineWidth(length); // make arrow thicker
    putShape(node, arrow, color, name);
  }

  /**
   * This function creates geomety and material of the arrow.
   * @param node The node to attach this graphical element
   * @param shape The shape of this graphical element
   * @param color The color of this graphical element
   * @param length The length of this graphical element
   */
  private void putShape(Node node, Mesh shape, ColorRGBA color, String name) 
  {
    Geometry geo_axis= new Geometry(name, shape); 
    Material material = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    material.getAdditionalRenderState().setWireframe(true);
    material.setColor("Color", color);
    geo_axis.setMaterial(material);
    node.attachChild(geo_axis);
  }
  
}
