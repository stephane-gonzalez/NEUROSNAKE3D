package bot;

/* ----  MY MODULES  ---- */

import common.Save;
import static common.Statics.*;
import graphic.GDisplay;
import graphic.GNeuralNet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

 

class SortFitness implements Comparator<Snake> 
{ 
  // Used for sorting in ascending order of 
  // roll number 
  public int compare(Snake a, Snake b) 
  { 
      return (int) b.fitness - (int) a.fitness; 
  } 
} 

/* ---- EXTERNAL MODULES ---- */
public class Population {

  /**
   * The snake population
   */
  Snake[] snakes;
  /**
   * The best snake
   */
  public Snake bestSnake;
  /**
   * The score of the best snake
   */
  int bestSnakeScore = 0;
  /**
   * The generation number
   */
  int gen = 0;
  /**
   * The counter of same best snake in row in order to trig a bigger mutation
   */
  int samebest = 0;
  /**
   * The fitness score of the best snake
   */
  float bestFitness = 0;
  /**
   * The fitness sum score of the population
   */
  float fitnessSum = 0;
  /**
   * The current mutation rate
   */
  float mutationRate = C_SNAKE_MUTATION_RATE;

   GNeuralNet neuralFrame;
   
   GDisplay snakeFrame;
   
   /**
   * Create a population with a default snake population number per generation
   *
   * @return Population Object
   */
  public Population(final int gen_, final NeuralNet neuralNet, final ArrayList<Food> foodList, final int scrore, final int lifeTime) {
   
    // create the snake population
    snakes = new Snake[C_SNAKE_POPULATION];
    
     // record the first one as the best one (initial condition)
    initBestSnake(neuralNet, foodList, scrore, lifeTime);

    bestSnake.dead = false;
    
    snakes[0] = new Snake(foodList, bestSnake.brain ) ;
    for (int i = 1; i < 1000; i++) {
      Snake child = new Snake(foodList, bestSnake.brain ) ;
      child.mutate();
      snakes[i] = child;
    }
      
    // generation
    for (int i = 1000; i < 1500; i++) {
      Snake child = selectParent(0, 999).crossover(selectParent(0, 999));
      snakes[i] = child;
    }
    
    for (int i = 1500; i < (snakes.length - 500)/2; i++) {
      Snake child = selectParent(0, 999).crossover(new Snake(foodList));
      snakes[i] = child;
    }
    
    for (int i = (snakes.length - 500)/2; i < snakes.length; i++) {
      Snake child = selectParent(0, 999).crossover(new Snake(foodList));
      child.mutate();
      snakes[i] = child;
    }
    
    
    gen = gen_;
    
    snakeFrame = new GDisplay(bestSnake);
   // neuralFrame = new GNeuralNet(bestSnake);
    
  }
  
  
  /**
   * Create a population with a default snake population number per generation
   *
   * @return Population Object
   */
  public Population() {
    ArrayList<Food> foodList = new ArrayList<>();
    foodList.add(new Food()); 
    initPopulation(C_SNAKE_POPULATION, foodList);
    initBestSnake(snakes[0].brain, foodList, 0, 0);
    
    snakeFrame = new GDisplay(bestSnake);
 //   neuralFrame = new GNeuralNet(bestSnake);
  }

  /**
   * Create a population of the given size
   *
   * @param size The number of creatures
   * @return Population Object
   */
  public Population(int size) {
    ArrayList<Food> foodList = new ArrayList<>();
    foodList.add(new Food()); 
    initPopulation(size, foodList);
    initBestSnake(snakes[0].brain, foodList, 0, 0);
    
    snakeFrame = new GDisplay(bestSnake);
  //  neuralFrame = new GNeuralNet(bestSnake);
  }

  /**
   * Initialize a population of the given size
   *
   * @param size The number of creatures
   */
  void initPopulation(int size, ArrayList<Food> foodList) {
    // create the snake population
    snakes = new Snake[size];
    for (int i = 0; i < snakes.length; i++) {
      snakes[i] = new Snake(foodList);
    }
  }
  
  void initBestSnake(NeuralNet neuralNet, final ArrayList<Food> foodList, final int scrore, final int lifeTime) {
    // record the first one as the best one (initial condition)
    bestSnake = new Snake(foodList, neuralNet);
    bestSnake.score = scrore;
    bestSnake.lifeTime = lifeTime;
    bestSnake.calculateFitness();
    bestFitness = bestSnake.fitness;
  }

  /**
   * Checks if the snakes population is dead
   *
   * @return True if the snake population is dead
   */
  public boolean done() {
    for (Snake snake : snakes) {
      if (!snake.dead) {
        return false;
      }
    }
    if (!bestSnake.dead) {
      return false;
    }
    return true;
  }

  /**
   * Update all the snakes in the generation
   */
  public void update() {
    for (Snake snake : snakes) {
      if (!snake.dead) {
        snake.look();
        snake.think();
        snake.move();
      }
    }
  }

  void setBestSnake() { // set the best snake of the generation
    Arrays.sort(snakes, new SortFitness());
    gen ++;
    if (snakes[0].fitness > bestFitness) {
      //neuralFrame.close();
      snakeFrame.close();
      samebest = 0;
      
      Save.save(C_PATH_SAVE, gen, snakes[0]);
      bestSnakeScore = snakes[0].score;
      bestFitness = snakes[0].fitness;
      bestSnake = snakes[0].cloneForReplay();
      bestSnake.fitness = snakes[0].fitness;
      bestSnake.dead = false;
      //neuralFrame = new GNeuralNet(bestSnake);
      snakeFrame = new GDisplay(bestSnake);
      
      System.out.println("New Best snake " + 
        " mutation rate " + mutationRate + 
        " score " + snakes[0].score + 
        " lifeLeft " + snakes[0].lifeLeft + 
        " lifeTime " + snakes[0].lifeTime);
      
      mutationRate = C_SNAKE_MUTATION_RATE;
    } else {
      //bestSnake = bestSnake.cloneForReplay();
      samebest++;
     
      mutationRate = C_SNAKE_MUTATION_RATE * samebest;

      if (samebest == 5) {
        mutationRate = C_SNAKE_MUTATION_RATE * (5);
      }
      
      bestSnake = bestSnake.cloneForReplay();
      bestSnake.fitness = bestFitness;
      
    }
    
    System.out.println("Gen no " + gen + 
      " champion fitness " + bestSnake.fitness + 
      " best fitness " + snakes[0].fitness + 
      " challenger " + snakes[1].fitness);
  }

  Snake selectParent(int min, int max) { // selects a random number in range of the fitness sum and if a snake falls in
    int rand = new Random().nextInt(max-min) + min;
    return snakes[rand];
  }
  
  public void naturalSelection()  {
    Snake[] newSnakes = new Snake[snakes.length];

    setBestSnake();
    calculateFitnessSum();
    
    ArrayList<Food> foodList = bestSnake.foodList;
    
    newSnakes[0] = new Snake(foodList, bestSnake.brain ) ;
    for (int i = 1; i < 1000; i++) {
      Snake child = new Snake(foodList, snakes[i].brain ) ;
      child.mutate();
      newSnakes[i] = child;
    }
      
    // generation
    for (int i = 1000; i < 1500; i++) {
      Snake child = selectParent(0, 999).crossover(selectParent(0, 999));
      newSnakes[i] = child;
    }
    
    for (int i = 1500; i < (snakes.length - 500)/2; i++) {
      Snake child = selectParent(0, 999).crossover(new Snake(foodList));
      newSnakes[i] = child;
    }
    
    for (int i = (snakes.length - 500)/2; i < snakes.length; i++) {
      Snake child = selectParent(0, 999).crossover(new Snake(foodList));
      child.mutate();
      newSnakes[i] = child;
    }
    
    snakes = newSnakes.clone();
    
  }

  void mutate() {
    for (int i = 1; i < snakes.length; i++) { // start from 1 as to not override the best snake placed in index 0
      snakes[i].mutate();
    }
  }

  public void calculateFitness() { // calculate the fitnesses for each snake
    for (Snake snake : snakes) {
      snake.calculateFitness();
    }
  }

  public void calculateFitnessSum() { // calculate the sum of all the snakes fitnesses
    fitnessSum = 0;
    for (Snake snake : snakes) {
      fitnessSum += snake.fitness;
    }
  }

  public void show() {
    if (!bestSnake.dead) {
      bestSnake.show();
      snakeFrame.update();
      //neuralFrame.update();
    }
  }
  
}
