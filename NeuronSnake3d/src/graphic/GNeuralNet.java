/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import bot.Snake;
import com.jme3.input.ChaseCamera;

import static common.Statics.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author steph
 */
public class GNeuralNet extends JPanel {

  static final int C_CIRCLE_SIZE = 40;

  JFrame frame;

  Snake snake;
  GSnake gsnake;
  GFood gfood;
  GWorldBound bound;

  public GNeuralNet(Snake snake_) {

    snake = snake_;
    JFrame.setDefaultLookAndFeelDecorated(true);
    frame = new JFrame("NeuralNet Activity");
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.setBackground(Color.white);
    frame.setSize(C_CIRCLE_SIZE * 2 * snake.vision.length + C_CIRCLE_SIZE * 2, C_NEURALNET_NB_LAYERS * C_CIRCLE_SIZE * 2 + C_CIRCLE_SIZE + C_CIRCLE_SIZE / 2);
    frame.add(this);
    frame.setVisible(true);

    gsnake = new GSnake(snake);
    gfood = new GFood(snake.foodList.get(snake.food).getPos());

    getRootNode().attachChild(gsnake);
    getRootNode().attachChild(gfood);

    bound = new GWorldBound();
    getRootNode().attachChild(bound);

  }

  public void drawCircle(Graphics graph, int posx, int posy, Color color, String text) {
    graph.setColor(color);
    graph.fillOval(posx, posy, C_CIRCLE_SIZE, C_CIRCLE_SIZE);
    graph.setColor(Color.black);
    graph.drawString(text, posx, posy + (int) (C_CIRCLE_SIZE * 1.5f));
  }

  @Override
  public void paintComponent(Graphics graph) {
    
      int posx = C_CIRCLE_SIZE;
      int posy = C_CIRCLE_SIZE;

      for (int neuron = 0; neuron < snake.vision.length; neuron++) {
        Color color = (snake.vision[neuron] > 0) ? Color.red : Color.blue;
        drawCircle(graph, posx, posy, color, String.format("%2f", snake.vision[neuron]));
        posx += C_CIRCLE_SIZE * 2;
      }
      posy += C_CIRCLE_SIZE * 2;

      posx = (snake.vision.length - snake.decision.length) * C_CIRCLE_SIZE + C_CIRCLE_SIZE;
      for (int neuron = 0; neuron < snake.decision.length; neuron++) {
        Color color = (neuron == snake.dirMove.getValue()) ? Color.red : Color.blue;
        drawCircle(graph, posx, posy, color, DIRECTION.valueOf(neuron).getLabel());
        posx += C_CIRCLE_SIZE * 2;
      }
      posy += C_CIRCLE_SIZE * 2;

      posx = C_CIRCLE_SIZE;

      graph.drawString("Food ", posx, posy += C_CIRCLE_SIZE);
      graph.drawString("posx " + snake.foodList.get(snake.food).getPos().x, posx += C_CIRCLE_SIZE * 2, posy);
      graph.drawString("posy " + snake.foodList.get(snake.food).getPos().y, posx += C_CIRCLE_SIZE * 2, posy);
      graph.drawString("posz " + snake.foodList.get(snake.food).getPos().z, posx += C_CIRCLE_SIZE * 2, posy);

      posx = C_CIRCLE_SIZE;
      graph.drawString("Head ", posx, posy += C_CIRCLE_SIZE);
      graph.drawString("posx " + snake.head.x, posx += C_CIRCLE_SIZE * 2, posy);
      graph.drawString("posy " + snake.head.y, posx += C_CIRCLE_SIZE * 2, posy);
      graph.drawString("posz " + snake.head.z, posx += C_CIRCLE_SIZE * 2, posy);

      posx = C_CIRCLE_SIZE;
      graph.drawString("Distance " + snake.head.distance(snake.foodList.get(snake.food).getPos()), posx, posy += C_CIRCLE_SIZE);
      graph.drawString("Score " + snake.score, posx += C_CIRCLE_SIZE * 2, posy);
      graph.drawString("timeLife " + snake.lifeTime, posx += C_CIRCLE_SIZE * 2, posy);

  }

  public void close() {
    getRootNode().detachAllChildren();
    gsnake.chaseCam.cleanupWithInput(getInputManager());
    frame.setVisible(false); //you can't see me!
    frame.dispose(); //Destroy the JFrame object
  }

  public void update() {
    frame.repaint();

    if (!snake.dead) {
      gsnake.refresh();
      gfood.refresh(snake.foodList.get(snake.food).getPos());
    }
  }

}
