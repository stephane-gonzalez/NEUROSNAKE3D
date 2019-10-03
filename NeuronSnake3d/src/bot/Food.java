/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;


/* ----  MY MODULES  ---- */
import static common.FMath.*;
import static common.Statics.*;

/* ---- EXTERNAL MODULES ---- */
import com.jme3.math.Vector3f;


/**
 *
 * @author steph
 */
public class Food {
  
  final Vector3f pos;
  
  /**
   * Create a food at a random position witin the world dimention.
   */
  public Food()
  {    
    float posx = (float) Math.floor(random(C_GRAPHIC_OBJECT_MOVE_QUANTITY, C_WORLD_DIM_X - C_GRAPHIC_OBJECT_MOVE_QUANTITY));
    float posy = (float) Math.floor(random(C_GRAPHIC_OBJECT_MOVE_QUANTITY, C_WORLD_DIM_Y - C_GRAPHIC_OBJECT_MOVE_QUANTITY));
    float posz = (float) Math.floor(random(C_GRAPHIC_OBJECT_MOVE_QUANTITY, C_WORLD_DIM_Z - C_GRAPHIC_OBJECT_MOVE_QUANTITY));
    
    pos = new Vector3f(posx, posy, posz);
  }
  
  
  /**
   * Create a food at a specific position.
   */
  public Food(Vector3f pos_)
  {        
    pos = new Vector3f(pos_);
  }
  
  /**
   * Returns the food position.
   * @return pos The food position.
   */
  public Vector3f getPos() 
  { 
    return pos; 
  }
  
  /**
   * Returns the clone of the food.
   * @return Food Object (Cloned).
   */
  @Override
  public Food clone() 
  { 
    return new Food(pos);
  }
  
  /**
   * Check if an object collides with the food.
   * @param object The object position
   * @return True if collides.
   */
  public boolean isCollide( Vector3f object )
  {
    return (object.distance(pos) < (C_GRAPHIC_OBJECT_SIZE *4) );
  }
  
}
