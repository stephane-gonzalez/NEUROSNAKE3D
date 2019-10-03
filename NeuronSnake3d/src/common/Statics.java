/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/* ----  MY MODULES  ---- */
import static mygame.Main.__assetManager;
import static mygame.Main.__inputManager;
import static mygame.Main.__rootNode;
import static mygame.Main.__cam;

/* ---- EXTERNAL MODULES ---- */
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author steph
 */
public class Statics {

  public static final String C_PATH_SAVE = "./src/ressources/";
  
  /**
   * This is the replay mode definition
   */
  enum REPLAY_MODE {
    E_REPLAY_CURRENT_BEST,
    E_REPLAY_ALL_CURRENT_GEN,
    E_REPLAY_ALL_BEST;
  };

  public static final float C_SNAKE_MUTATION_RATE = 0.05f;

  public static final REPLAY_MODE C_REPLAY_MODE = REPLAY_MODE.E_REPLAY_CURRENT_BEST;

  /**
   * World Area definition
   */
  public static final float C_WORLD_DIM_X = 25f;
  public static final float C_WORLD_DIM_Y = 25f;
  public static final float C_WORLD_DIM_Z = 25f;

  /**
   * Sensitive dimention (point of intrest)
   */
  public enum SNAKE_SENSITIVE {
    E_SNAKE_SENSITIVE_FOOD,
    E_SNAKE_SENSITIVE_BODY,
    E_SNAKE_SENSITIVE_WALL,
    E_SNAKE_SENSITIVE_NB_DIM;
  };

  /**
   * Sensitive dimention (point of intrest)
   */
  public enum LOCATION_VECTOR {
    E_LOCATION_VECTOR_ANGLE_X_PLUS(1),
    E_LOCATION_VECTOR_ANGLE_Y_PLUS(2),
    E_LOCATION_VECTOR_ANGLE_Z_PLUS(3),
    
    E_LOCATION_VECTOR_ANGLE_X_MINUS(1),
    E_LOCATION_VECTOR_ANGLE_Y_MINUS(2),
    E_LOCATION_VECTOR_ANGLE_Z_MINUS(3),
    
    E_LOCATION_VECTOR_ANGLE_A_PLUS(1), // α is the angle between u and the x-axis
    E_LOCATION_VECTOR_ANGLE_B_PLUS(2), // β is the angle between u and the y-axis
    E_LOCATION_VECTOR_ANGLE_G_PLUS(3), // γ is the angle between u and the z-axis
    
    E_LOCATION_VECTOR_ANGLE_A_MINUS(1), // α is the angle between u and the x-axis
    E_LOCATION_VECTOR_ANGLE_B_MINUS(2), // β is the angle between u and the y-axis
    E_LOCATION_VECTOR_ANGLE_G_MINUS(3), // γ is the angle between u and the z-axis
    
    E_LOCATION_VECTOR_NB_DIM(4);

    final int __value;

    LOCATION_VECTOR(int value_) {
      __value = value_;
    }

    public int value() {
      return __value;
    }

  };

  /**
   * Snake movement direction
   */
  public enum DIRECTION {
    E_DIRECTION_UP(0, "UP"),
    E_DIRECTION_DOWN(1, "DOWN"),
    E_DIRECTION_FORWARD(2, "FORWARD"),
    E_DIRECTION_BACKWARD(3, "BACKWARD"),
    E_DIRECTION_LEFT(4, "LEFT"),
    E_DIRECTION_RIGHT(5, "RIGHT"),
    E_DIRECTION_NB_DIM(6, "NB_DIM");

    final int __value;
    final String __label;
    private static final Map __map = new HashMap<>();

    static {
      for (DIRECTION pageType : DIRECTION.values()) {
        __map.put(pageType.__value, pageType);
      }
    }

    DIRECTION(int value_, String label_) {
      __value = value_;
      __label = label_;
    }
        
    public static DIRECTION valueOf(int pageType) {
      return (DIRECTION) __map.get(pageType);
    }

    public int getValue() {
      return __value;
    }



    public int value() {
      return __value;
    }
    
    public String getLabel() {
      return __label;
    }

    

  };

  /**
   * Network topology ( default configuration )
   */
  public static final int C_NEURALNET_INPUT_NODES = 3;
  public static final int C_NEURALNET_OUTPUT_NODES = 6;
  public static final int C_NEURALNET_HIDDEN_NODES = 18;
  public static final int C_NEURALNET_HIDDEN_LAYERS = 2;
  // The number of neuron layers (+2 because of the input and output layers)
  public static final int C_NEURALNET_NB_LAYERS = 2 + C_NEURALNET_HIDDEN_LAYERS;

  /**
   * The default snake population number per generation
   */
  public static final int C_SNAKE_POPULATION = 10_000;

  /**
   * Object size
   */
  public static final float C_GRAPHIC_OBJECT_SIZE = 0.5f;
  public static final float C_GRAPHIC_OBJECT_MOVE_QUANTITY = C_GRAPHIC_OBJECT_SIZE * 2;
  public static final Vector3f C_GRAPHIC_OBJECT_INITIAL_POS = new Vector3f(C_WORLD_DIM_X / 2, C_WORLD_DIM_Y / 2, C_WORLD_DIM_Z / 2);

  /**
   * ****** Debug *********
   */
  /**
   * Display local 3D axis
   */
  public static final boolean C_GRAPHIC_DISPLAY_3D_AXIS = true;
  /**
   * Display 3D axis length
   */
  public static final float C_GRAPHIC_3D_AXIS_LENGHT = C_GRAPHIC_OBJECT_SIZE * 5.0f;

  public static AssetManager getAssetManager() {
    return __assetManager;
  }

  public static InputManager getInputManager() {
    return __inputManager;
  }

  public static Camera getCamera() {
    return __cam;
  }

  public static Node getRootNode() {
    return __rootNode;
  }

}
