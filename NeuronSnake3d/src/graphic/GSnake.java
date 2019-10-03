/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

/* ----  MY MODULES  ---- */
import bot.Snake;

import static common.Statics.DIRECTION;
import static common.Statics.C_WORLD_DIM_X;
import static common.Statics.C_GRAPHIC_OBJECT_INITIAL_POS;
import static common.Statics.C_GRAPHIC_OBJECT_MOVE_QUANTITY;
import static common.Statics.getCamera;
import static common.Statics.getInputManager;
/* ---- EXTERNAL MODULES ---- */
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.input.ChaseCamera;

import java.util.ArrayList;

/**
 *
 * @author steph
 */
public class GSnake extends Node {

  GArrow arrow;
  DIRECTION lastDirMove;
  /**
   * This is the snake head
   */
  GSphere head;
  /**
   * This is the snake body
   */
  ArrayList<GSphere> body;

  Snake snake;
  ChaseCamera chaseCam;
  
  /**
   * Create a new Graphical Snake poping in the middle of the world
   */
  public GSnake(Snake snake_) {
    snake = snake_;
    // initiate a new snake
    initGSnake();
  }

  /**
   * Create the snake commoned of one head and two body parts. The Snake is
   * positioned to the middle of the world
   */
  void initGSnake() {
    // Create the snake in the middle of the world
    body = new ArrayList<>();
    head = new GSphere(ColorRGBA.Green, "head", new Vector3f(C_GRAPHIC_OBJECT_INITIAL_POS));

    attachChild(head);
    // add the first body part
    Vector3f tail = head.getLocalTranslation();
    addBodyPart(tail.add(0, C_GRAPHIC_OBJECT_MOVE_QUANTITY, 0));
    feed();
    
    chaseCam = new ChaseCamera(getCamera(), head, getInputManager());
    chaseCam.setMinDistance(C_WORLD_DIM_X*2);
   
  }

  /**
   * This function returns the snake head
   *
   * @return The snake head
   */
  public GSphere getHead() {
    return head;
  }

  /**
   * Add a new body part to the snake when the snake is feed. The new body part
   * is positioned to the tail
   */
  public void feed() {
    Vector3f tail = body.get(body.size() - 1).getLocalTranslation();
    addBodyPart(tail.add(0, C_GRAPHIC_OBJECT_MOVE_QUANTITY, 0));
  }

  /**
   * Add a new body part to the snake at the specific position.
   *
   * @param pos The specific position where to add the new body part (tail)
   */
  void addBodyPart(Vector3f pos) {
    GSphere bodypart = new GSphere(ColorRGBA.Gray, "body", pos);
    body.add(bodypart);
    attachChild(bodypart);
    
  }
  
  void refresh()
  {
    // move the head position
    head.move(snake.head);
    
    if( body.size() != snake.body.size() )
    {
      GSphere bodypart = new GSphere(ColorRGBA.Gray, "body", snake.body.get(snake.body.size() - 1));
      body.add(bodypart);
      attachChild(bodypart);
      setTransformRefresh();
    }

    // move each body part by flowing the previous one
    for (int i = 0 ; i< snake.body.size() ; i++) {
      //GSphere bodypart; Vector3f b  : body; snake.body
      body.get(i).move(snake.body.get(i));
    }
  }
  
 /* public void die()
  {
    detachAllChildren();
  }*/
}
