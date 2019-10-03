/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

/* ----  MY MODULES  ---- */

/* ---- EXTERNAL MODULES ---- */

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author steph
 */
public class GObject extends Node {
    
  /**
   * This function attaches 3 graphical axis coordinate to the object.
   * @param length The length of the arrows representing the coordinates.
   */
  public void attachCoordinateAxes(float length){
    new GArrow(this, "coordinate axis X", ColorRGBA.Red, Vector3f.UNIT_X, length);
    new GArrow(this, "coordinate axis Y", ColorRGBA.Green, Vector3f.UNIT_Y, length);
    new GArrow(this, "coordinate axis Z", ColorRGBA.Blue, Vector3f.UNIT_Z, length);
  }
   
}
