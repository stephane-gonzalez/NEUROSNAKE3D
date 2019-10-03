/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import static common.Statics.C_WORLD_DIM_X;
import static common.Statics.C_WORLD_DIM_Y;
import static common.Statics.getAssetManager;
import static common.Statics.getRootNode;


/**
 *
 * @author steph
 */
public class GWorldBound extends GObject {
  
   /**
   * This funtion initialise a graphical sphere object.
   *
   * @param pos The color of the object
   * @param name The name of the object
   */
  GWorldBound() {
    
    // Load a floor model
    Material mat_ground = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    mat_ground.setTexture("ColorMap", getAssetManager().loadTexture("Interface/Logo/Monkey.jpg"));
    Geometry ground = new Geometry("ground", new Quad(C_WORLD_DIM_X, C_WORLD_DIM_Y));

    ground.setLocalTranslation(0, 0, 0);
    ground.setMaterial(mat_ground);
    getRootNode().attachChild(ground);
     
  }
}
