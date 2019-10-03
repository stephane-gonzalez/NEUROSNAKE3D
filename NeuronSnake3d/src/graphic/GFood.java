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

/**
 *
 * @author steph
 */
public class GFood extends GSphere{

   /**
   * Create a food at a specific position ( used for food cloning).
   * 
   * @param pos 
   */
  public GFood (Vector3f pos)
  {
    super(ColorRGBA.Red, "Food", pos);
    refresh(pos);
  }
  
  /**
   * Clone a food Object.
   * 
   * @return GFood Object
   */
  public GFood clone()
  { 
    return new GFood(getLocalTranslation());
  }

  /**
   * Refresh the food Object position.
   * @param pos The food position
   */
  public void refresh(Vector3f pos)
  {
    move(pos);
  }
  
  /**
   * This function graphically delete the food item
   */
 /* public void eat()
  {
    getRootNode().detachChild(this);
  }*/
}
