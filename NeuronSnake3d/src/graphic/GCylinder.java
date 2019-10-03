/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import static common.Statics.C_GRAPHIC_3D_AXIS_LENGHT;
import static common.Statics.C_GRAPHIC_DISPLAY_3D_AXIS;
import static common.Statics.C_GRAPHIC_OBJECT_SIZE;
import static common.Statics.getAssetManager;

/**
 *
 * @author steph
 */
public class GCylinder extends GObject {

  Cylinder cylinder;
  
  /**
   * Create a graphical Cylinder object.
   *
   * @param color The color of the object
   * @param name The name of the object
   * @param pos The position of the object
   */
  public GCylinder(ColorRGBA color, String name, Vector3f pos) {
    initGCylinder(color, name);
    move(pos.x, pos.y, pos.z);
  }

  /**
   * Create a graphical Cylinder object.
   *
   * @param color The color of the object
   * @param name The name of the object
   */
  public GCylinder(ColorRGBA color, String name) {
    initGCylinder(color, name);
    move(0, 0, 0);
  }

  /**
   * This funtion initialise a graphical dome object.
   *
   * @param color The color of the object
   * @param name The name of the object
   */
  void initGCylinder(ColorRGBA color, String name) {
   // dome = new Dome( 2 , 100, C_GRAPHIC_OBJECT_SIZE * 2);
    
    cylinder = new Cylinder(2, 25, C_GRAPHIC_OBJECT_SIZE, C_GRAPHIC_OBJECT_SIZE * 5, C_GRAPHIC_OBJECT_SIZE *40, true, false); // Cone
    
    Geometry geometry = new Geometry("View " + name, cylinder);
    Material material = new Material(getAssetManager(),
      "Common/MatDefs/Misc/Unshaded.j3md");
    material.setColor("Color", color);
    geometry.setMaterial(material);
    attachChild(geometry);

    if (C_GRAPHIC_DISPLAY_3D_AXIS) {
      attachCoordinateAxes(C_GRAPHIC_3D_AXIS_LENGHT);
    }
  }
  
   /**
   * This funtion moves graphical dome object.
   *
   * @param pos The position of the object
   */
  @Override
  public Spatial move(float x, float y, float z)
  {
    // Update the graphical position
    setLocalTranslation(new Vector3f(x, y, z));
    setTransformRefresh();
    return this;
  }
  
    
   /**
   * This funtion moves graphical dome object.
   *
   * @param pos The position of the object
   * @return Spatial Object
   */
  @Override
  public Spatial move(Vector3f pos)
  {
    // Update the graphical position
    setLocalTranslation(new Vector3f(pos));
    // Update the collision bounds
    setTransformRefresh();
    return this;
  }
  
   /**
   * This funtion checks if an object collides with me
   *
   * @param with The position of the object agaisnt collision
   * @return True if the objects collides
   */
  public boolean isCollide(Vector3f with)
  {
    boolean collides = false;

    BoundingSphere thisObj = new BoundingSphere(C_GRAPHIC_OBJECT_SIZE - 0.1f, this.getLocalTranslation());
    BoundingSphere withObj = new BoundingSphere(C_GRAPHIC_OBJECT_SIZE - 0.1f, with);
    
    CollisionResults results = new CollisionResults();
    int numCollisions = thisObj.collideWith(withObj, results);

    if (numCollisions > 0) {
      collides = true;
    }
        
    return collides;
  }

}

