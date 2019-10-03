package mygame;


import bot.Population;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import common.Save;
import static common.Statics.C_PATH_SAVE;
import static common.Statics.C_WORLD_DIM_Z;



/**
 * Example 9 - How to make walls and floors solid. This collision code uses
 * Physics and a custom Action Listener.
 *
 * @author normen, with edits by Zathras
 */
public class Main extends SimpleApplication {
  // implements AnalogListener, ActionListener {

  static public AssetManager __assetManager;
  static public InputManager __inputManager;
  static public Camera __cam;
  static public Node __rootNode;

  // private Geometry teaGeom;
  Population pop;

  int highscore;
  long prevTime = 0;

  public static void main(String[] args) {
    Main app = new Main();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    __assetManager = getAssetManager();
    __inputManager = getInputManager();
    __cam = getCamera();
    __rootNode = getRootNode();

    // Disable the default first-person cam!
    flyCam.setEnabled(true);
    __cam.setLocation(new Vector3f(0,0,C_WORLD_DIM_Z*2));
    __cam.setAxes(new Quaternion().fromAngleAxis(+FastMath.HALF_PI, Vector3f.UNIT_X));
   
    Save.Loaded lo = new Save().loadLast(C_PATH_SAVE);
     
    if (lo != null)
      // create a population of snake
      pop = new Population(lo.gen, lo.neuralNet, lo.foodList, lo.score, lo.lifeTime);
    else
      pop = new Population();

  }

  @Override
  public void simpleUpdate(float tpf) {
    super.simpleUpdate(tpf);

    if (pop.done()) {
      highscore = pop.bestSnake.score;
      pop.calculateFitness();
      pop.naturalSelection();

    } else {
      pop.update();
      pop.show();
     
    }

  }

}
