/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import bot.Snake;
import static common.Statics.*;

/**
 *
 * @author steph
 */
public class GDisplay {
    
  Snake snake;
  GSnake gsnake;
  GFood gfood;
  GWorldBound bound;
  
  public GDisplay(Snake snake_)
  {
    snake = snake_;
    gsnake = new GSnake(snake);
    gfood = new GFood(snake.foodList.get(snake.food).getPos());

    getRootNode().attachChild(gsnake);
    getRootNode().attachChild(gfood);

    bound = new GWorldBound();
    getRootNode().attachChild(bound);
  }
    
  public void close() {
    getRootNode().detachAllChildren();
    gsnake.chaseCam.cleanupWithInput(getInputManager());
  }

  public void update() {
    if (!snake.dead) {
      gsnake.refresh();
      gfood.refresh(snake.foodList.get(snake.food).getPos());
    }
  }
}
