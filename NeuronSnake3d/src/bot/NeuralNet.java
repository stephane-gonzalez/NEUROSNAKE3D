package bot;

/* ----  MY MODULES  ---- */
import com.jme3.math.Vector3f;
import static common.Statics.*;
import graphic.GNeuralNet;

/* ---- EXTERNAL MODULES ---- */

public class NeuralNet
{
  /** The number of input nodes */
  public int iNodes;
  /** The number of hidden nodes */
  public int hNodes;
  /** The number of output nodes */
  public int oNodes;
  /** The number of hidden layers */
  public int hLayers;
  
  GNeuralNet graphNeuralaNet;
    
  /**
   * The neural matrix weights (represent the inter-connection between neuron
   * layers)
   */
  public Matrix[] weights;

 /**
   * Create a new neural network based on default topology.
   */
  public NeuralNet(Matrix[] weights_)
  {
    initNeuralNet(C_NEURALNET_INPUT_NODES, C_NEURALNET_HIDDEN_NODES, C_NEURALNET_OUTPUT_NODES, C_NEURALNET_HIDDEN_LAYERS);
    load(weights_);
  }
  
  /**
   * Create a new neural network based on default topology.
   */
  public NeuralNet()
  {
    initNeuralNet(C_NEURALNET_INPUT_NODES, C_NEURALNET_HIDDEN_NODES, C_NEURALNET_OUTPUT_NODES, C_NEURALNET_HIDDEN_LAYERS);
  }

  /**
   * Initialize a neural network with the specified topology. And full fill each
   * neuronal weights matrix with a random value.
   */
  void initNeuralNet(final int iNodes_, final int hNodes_, final int oNodes_, final int hLayers_)
  {
    iNodes = iNodes_;
    hNodes = hNodes_;
    oNodes = oNodes_;
    hLayers = hLayers_;

    // Create the network layers
    weights = new Matrix[hLayers + 2];
    // Create the first layer inter-mapping
    // {input nodes -> intermediate nodes} [hNodes x iNodes + 1] [16 x 24+1]
    weights[0] = new Matrix(hNodes, iNodes + 1);
    for (int i = 1; i < (hLayers + 1); i++)
    {
      // Create the intermediate layers inter-mapping
      // {intermediate nodes -> intermediate nodes} [hNodes x hNodes + 1] [16 x 16+1]
      weights[i] = new Matrix(hNodes, hNodes + 1);
    }
    // Create the last layers inter-mapping
    // {intermediate nodes -> output nodes} [oNodes x hNodes + 1] [4 x 16+1]
    weights[weights.length - 1] = new Matrix(oNodes, hNodes + 1);

    // Fill matrix content with random content
    for (Matrix w : weights)
      w.randomize();


  }

  /**
   * Perform neuronal mutation. Only a part of the neuronal network is updated
   * 
   * @param mutationRate The mutation rate
   * @return None
   */
  void mutate(float mutationRate)
  {
    for (Matrix w : weights)
      w.mutate(mutationRate);
  }

  /**
   * Perform neuronal computation.
   * 
   * @param inputsArr The input array used to compute the bias of the first layer
   * @return return the activated output layer (result of the decision)
   */
  float[] output(float[] inputsArr)
  {
    // Create a matrix Nx1 based on the input array
    Matrix inputs = Matrix.singleColumnMatrixFromArray(inputsArr);

    // Create a matrix N+1x1 based on the input array
    // with the last value set to 1
    // This new matrix is the bias matrix
    Matrix curr_bias = inputs.addBias();

    // apply the bias on for each hidden layer
    for (int i = 0; i < hLayers; i++)
    {
      // apply the bias on neurons of the current hidden layer
      Matrix hidden_ip = weights[i].dot(curr_bias);
      // activate the neurons current hidden layer
      Matrix hidden_op = hidden_ip.activate();
      // perform new bias calculation based on the output of the current hidden layer
      curr_bias = hidden_op.addBias();
    }

    // apply bias on the last layer
    Matrix output_ip = weights[weights.length - 1].dot(curr_bias);
    // activate the neurons of the output layer
    Matrix output = output_ip.activate();

    // return the activated output layer (result of the decision)
    return output.toArray();
  }

  /**
   * Perform crossover function on the neural network (Breeds)
   * Hybrid 2 neural network randomly to attempt to find even better candidates
   * @param partner the partner neural network
   * @return The child
   */
  NeuralNet crossover(NeuralNet partner)
  {
    NeuralNet child = new NeuralNet();
    for (int i = 0; i < weights.length; i++)
    {
      child.weights[i] = weights[i].crossover(partner.weights[i]);
    }
    return child;
  }

  /**
   * Clone a neural network and create a new child (the champion)
   * @return The clone
   */
  public NeuralNet clone()
  {
    NeuralNet clone = new NeuralNet();
    for (int i = 0; i < weights.length; i++)
    {
      clone.weights[i] = weights[i].clone();
    }

    return clone;
  }

  /**
   * Load a neuronal network from a network layers Matrix array
   * @param [] weight_ the network layers Matrix array
   * @return None
   */
  void load(Matrix[] weights_)
  {
    for(int layer=0; layer < weights_.length; layer++)
      for(int row =0 ; row < weights[layer].rows; row++)
        System.arraycopy(weights_[layer].matrix[row], 0, weights[layer].matrix[row], 0, weights[layer].cols);
  }

  /**
   * Clones a neuronal network as Matrix array
   * @param None
   * @return the neuronal network as Matrix array
   */
  Matrix[] pull()
  {
    return weights.clone();
  }

}