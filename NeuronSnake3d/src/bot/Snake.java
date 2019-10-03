package bot;

/* ----  MY MODULES  ---- */
import static common.Statics.C_NEURALNET_INPUT_NODES;
import static common.Statics.C_NEURALNET_OUTPUT_NODES;
import static common.Statics.C_SNAKE_MUTATION_RATE;

import static common.Statics.DIRECTION;
import static common.Statics.DIRECTION.*;

import static common.Statics.C_GRAPHIC_OBJECT_MOVE_QUANTITY;
import static common.Statics.C_GRAPHIC_OBJECT_SIZE;
import static common.Statics.C_WORLD_DIM_X;
import static common.Statics.C_WORLD_DIM_Y;
import static common.Statics.C_WORLD_DIM_Z;


/* ---- EXTERNAL MODULES ---- */
import static com.jme3.math.FastMath.PI;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;

public class Snake {

  /**
   * the snake score
   */
  public int score = 0;
  /**
   * amount of moves the snake can make before it dies
   */
  public int lifeLeft = 200;
  /**
   * amount of time the snake has been alive
   */
  public int lifeTime = 0;
  /**
   * the snake fitness score
   */
  public float fitness = 0;
  /**
   * the time sytem
   */
  public float sysTime = 0f;
  /**
   * the snake dead state
   */
  public boolean dead = false;
  /**
   * snake vision
   */
  public float[] vision;
  /**
   * snake /** snake vision
   */
  public float[] decision;
  /**
   * snake brain
   */
  public NeuralNet brain;
  /**
   * The movement direction
   */
  public DIRECTION dirMove;
  /**
   * This is the snake head
   */
  public Vector3f head;
  /**
   * This is the snake body
   */
  public ArrayList<Vector3f> body;
  /**
   * current food
   */
  public int food;
  /**
   * list of food positions (used to replay the best snake)
   */
  public ArrayList<Food> foodList;

  /**
   * Create a new Snake with a specified neural network topology.
   * @param foodlist_ The food list at snake creation
   * @param brain_ The brain of the snake at this creation
   * @return Snake Object
   */
  public Snake(ArrayList<Food> foodlist_, NeuralNet brain_) {
    // Create a new food list and it the first food
    initSnake(foodlist_, brain_); 
  }
    
  /**
   * Create a new Snake.
   * @param foodlist_ The food list at snake creation
   * @return Snake Object
   */
  public Snake(ArrayList<Food> foodlist_) {
    initSnake(foodlist_, new NeuralNet());
  }
  
  void initSnake(ArrayList<Food> foodlist_, NeuralNet brain_)
  {
      // Create a new food list and it the first food
    food = 0;
    foodList = foodlist_;
   
    // Intialise snake 
    brain = brain_.clone();
    vision = new float[C_NEURALNET_INPUT_NODES];
    decision = new float[C_NEURALNET_OUTPUT_NODES];
    dirMove = DIRECTION.E_DIRECTION_FORWARD; 
   
    
    head = new Vector3f(C_WORLD_DIM_X/2, C_WORLD_DIM_Y/2, C_WORLD_DIM_Z/2);
    body = new ArrayList<>();
    body.add(new Vector3f(C_WORLD_DIM_X/2, C_WORLD_DIM_Y/2 + C_GRAPHIC_OBJECT_SIZE*2, C_WORLD_DIM_Z/2));
    body.add(new Vector3f(C_WORLD_DIM_X/2, C_WORLD_DIM_Y/2 + C_GRAPHIC_OBJECT_SIZE*4, C_WORLD_DIM_Z/2));
    
  }
  
   /**
   * This function moves the snake. Each body part follows the body part
   * position front of it. The first body part follows the head etc ...
   *
   * @param step The step position
   */
  public void moveSnake(Vector3f step) {
    // store the head position
    Vector3f temp1pos = new Vector3f(head);
    // move the head position
    head.addLocal(step);
    Vector3f temp2pos;
    // move each body part by flowing the previous one
    for (int i = 0 ; i < body.size(); i++) {
      temp2pos = new Vector3f(body.get(i));
      body.get(i).set(temp1pos);
      temp1pos = temp2pos;
    }
    
  }
  
  /**
   * This function moves the snake according to the direction movement
   */
  public void moveTowards()
  {

    // apply physically the decision
    switch (dirMove) {
      case E_DIRECTION_UP:
        moveSnake(new Vector3f (0, 0, C_GRAPHIC_OBJECT_MOVE_QUANTITY));
        break;
      case E_DIRECTION_DOWN:
        moveSnake(new Vector3f (0, 0, -C_GRAPHIC_OBJECT_MOVE_QUANTITY));
        break;
      case E_DIRECTION_FORWARD:
        moveSnake(new Vector3f (0, C_GRAPHIC_OBJECT_MOVE_QUANTITY, 0));
        break;
      case E_DIRECTION_BACKWARD:
        moveSnake(new Vector3f (0, -C_GRAPHIC_OBJECT_MOVE_QUANTITY, 0));
        break;
      case E_DIRECTION_LEFT:
        moveSnake(new Vector3f (C_GRAPHIC_OBJECT_MOVE_QUANTITY, 0, 0));
        break;
      case E_DIRECTION_RIGHT:
        moveSnake(new Vector3f (-C_GRAPHIC_OBJECT_MOVE_QUANTITY, 0, 0));
        break;
    }

  }

    /**
   * Checks wall collision.
   *
   * @return true if the head of the object collides with the world bounds
   */
  public boolean wallCollide() {
    boolean collides = false;
    if ((head.x > C_WORLD_DIM_X || head.x < 0)
      || (head.y > C_WORLD_DIM_Y || head.y < 0)
      || (head.z > C_WORLD_DIM_Z || head.z < 0)) {
      collides = true;
    }
    return collides;
  }
  
   /**
   * Checks body collision.
   *
   * @return true if the head collides with the body
   */
  public boolean bodyCollide() {
    // Check if a body part colides with the head
    for (Vector3f bodypart : body) {
      if (bodypart.distance(head) < ( 2 * C_GRAPHIC_OBJECT_SIZE) ) {
        return true;
      } 
    }
    return false;
  }
  
  /**
   * Performs snake movement
   *
   * @return True is the snake dead during its movement phase or False otherwise
   * (still alive)
   */
  boolean move() {
    // move the snake only if it is not dead
    if (!dead) {
      // check if the player is not human

      lifeTime++;
      lifeLeft--;

      // move the snake
      moveTowards();

      // check if the snake collide a food, then eat it
      if (foodList.get(food).isCollide(head)) {
        eat();
      }

      // check if the snake collides the world limit
      if (wallCollide()) {
        dead = true;
      } else if (bodyCollide()) {
        dead = true;
      } else if (lifeLeft <= 0) {
        dead = true;
      }
    }
   
    if(dead) die(); 
    
    return dead;
  }

  /**
   * Performs eat food
   */
  void eat() {
    
    // increment the score
    score++;

    // reward the snake by giving him more life time
    // (check point reached)
    if (lifeLeft < ( C_WORLD_DIM_X *10) ) {
      if (lifeLeft > (C_WORLD_DIM_X*4)) {
        lifeLeft = (int) (C_WORLD_DIM_X *10);
      } else {
        lifeLeft += (2 * C_WORLD_DIM_X);
      }
    }

    // check if the replay is not activate
    if(food == foodList.size() -1)
      // create a new food
      foodList.add(new Food());
    food++;
    
    body.add(body.get(body.size()-1).add(new Vector3f(0,C_GRAPHIC_OBJECT_MOVE_QUANTITY,0)));
      
  }

  /**
   * Clone the snake for replay purpose with its environment (same found)
   *
   * @return Snake object (The best snake)
   */
  Snake cloneForReplay() {
    // clone a version of the snake that will be used for a replay
    Snake clone = new Snake(foodList, brain);
    return clone;
  }

  /**
   * Clone a snake
   * @return Snake object
   */
  public Snake clone() {
    return new Snake(foodList, brain);
  }
 
  /**
   * Create a new snake and cross it over it another snake
   *
   * @param parent the parent to genetic cross with the new random specimen
   * 
   * @return Snake object (The crossover snake)
   */
  Snake crossover(Snake parent) {
    Snake child = (Snake) this.clone();
    child.brain = brain.crossover(parent.brain);
    return child;
  }

  /**
   * Mutate the current snake by using a ratio
   */
  void mutate() {
    brain.mutate(C_SNAKE_MUTATION_RATE);
  }

  /**
   * Performs fitness calculation
   * <li>fitness = lifetime + score x 1000
   */
  void calculateFitness() {
    fitness = lifeTime + score * 1000f;
  }

  /**
   * Performs the sensitive part (sight) for a given direction
   *
   * Looks towards the given direction and build 3 informations
   * <li>0. active if the food has been found by looking at this direction
   * <li>1. active if the body has been found by looking at this direction
   * <li>2. the distance inverse between the snake and the wall ( risk
   * assessment, more the snake is far from the wall smaller is this result )
   *
   * @param direction the direction to look at
   * @return the information on the line sight
   */
  Vector3f lookInDirection() {
    Vector3f foodPos = foodList.get(food).getPos().subtract(new Vector3f(C_WORLD_DIM_X/2, C_WORLD_DIM_Y/2, C_WORLD_DIM_Z/2));
    Vector3f headPos = head.subtract(new Vector3f(C_WORLD_DIM_X/2, C_WORLD_DIM_Y/2, C_WORLD_DIM_Z/2));
    Vector3f dirView = foodPos.subtract(headPos);

    return new Vector3f((dirView.x / C_WORLD_DIM_X/2) > 0 ? 1f : -1f, (dirView.y /C_WORLD_DIM_Y/2) > 0 ? 1 : -1, (dirView.z /C_WORLD_DIM_Z/2) > 0 ? 1 : -1);
  }

  /**
   * Perform the sensitive part (sight) for all directions
   */
  void look() {
    Arrays.fill(vision, 0f);
    Vector3f dirView =  lookInDirection();
    // distance between food and head
    vision[0] = dirView.z;
    vision[1] = dirView.y;
    vision[2] = dirView.x;
  }

  /**
   * Think about what direction to move
   */
  void think() {
    decision = brain.output(vision);
    int maxIndex = 0;
    float max = 0;
    // get as final decision the higher score of the output neurons layer
    for (int i = 0; i < decision.length; i++) {
      if (decision[i] > max) {
        max = decision[i];
        maxIndex = i;
      }
    }
    
    dirMove = DIRECTION.valueOf(maxIndex);

  }

  long prevTime = 0;
  /**
   * This function graphically display the snake and the food
   */
  void show() {
    long curTime = System.currentTimeMillis();
    
    long deltaTime = curTime - prevTime;
    if(!dead && (deltaTime > 50))
    {
      prevTime = curTime;
      look();
      think();
      move();
    }
  }
  
  void die()
  {
    sysTime = System.currentTimeMillis(); 
    if(dead)
    {
   //   brain.close();
    }
  }

}
