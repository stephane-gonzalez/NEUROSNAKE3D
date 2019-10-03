package bot;

/* ----  MY MODULES  ---- */
import static common.FMath.*;

/* ---- EXTERNAL MODULES ---- */

public class Matrix
{

  /** Matrix dimension */
  public int rows, cols;
  public float[][] matrix;

  /**
   * Create an empty Matrix with the specified dimensions.
   * 
   * @param row_  the number of rows
   * @param cols_ the number of columns
   * @return Matrix Object
   */
  public Matrix(int rows_, int cols_)
  {
    rows = rows_;
    cols = cols_;
    matrix = new float[rows][cols];
  }

  /**
   * Create a Matrix based on an existing matrix.
   * 
   * @param matrix_ the matrix
   * @return Matrix Object
   */
  public Matrix(float[][] matrix_)
  {
    matrix = matrix_.clone();
    rows = matrix.length;
    cols = matrix[0].length;
  }

  /**
   * Display the matrix content.
   * 
   * @param None
   * @return None
   */
  public void output()
  {
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        System.out.print(matrix[i][j] + " ");
      }
      System.out.println();
    }
    System.out.println();
  }

  /**
   * Perform dot operation A.B.
   * 
   * @param MatrixB_ the matrix B
   * @return Matrix the result A.B
   */
  public Matrix dot(Matrix MatrixB_)
  {
    Matrix result = new Matrix(rows, MatrixB_.cols);

    if (cols == MatrixB_.rows)
    {
      for (int i = 0; i < rows; i++)
      {
        for (int j = 0; j < MatrixB_.cols; j++)
        {
          float sum = 0;
          for (int k = 0; k < cols; k++)
          {
            sum += matrix[i][k] * MatrixB_.matrix[k][j];
          }
          result.matrix[i][j] = sum;
        }
      }
    }
    return result;
  }

  /**
   * Fill the matrix with randomized data.
   * 
   * @param None
   * @return None
   */
  public void randomize()
  {
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        matrix[i][j] = random(-1.0f, 1.0f);
      }
    }
  }

  /**
   * Create a matrix Nx1 from an existing array.
   * 
   * @param arr_ the array data to build the matrix
   * @return a new Matrix Nx1
   */
  static public Matrix singleColumnMatrixFromArray(float[] arr_)
  {
    Matrix n = new Matrix(arr_.length, 1);
    for (int i = 0; i < arr_.length; i++)
    {
      n.matrix[i][0] = arr_[i];
    }
    return n;
  }

  /**
   * Returns an array from a matrix organization.
   * 
   * @param None
   * @return a new array from a matrix organization
   */
  public float[] toArray()
  {
    float[] arr = new float[rows * cols];
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        arr[j + i * cols] = matrix[i][j];
      }
    }
    return arr;
  }

  /**
   * Create a matrix Nx1 based on the current object but add one extra line with
   * the value 1.
   * 
   * @param None
   * @return a new Matrix Nx1 based on the current object with an extra line with
   *         containing the value 1
   */
  public Matrix addBias()
  {
    Matrix n = new Matrix(rows + 1, 1);
    for (int i = 0; i < rows; i++)
    {
      n.matrix[i][0] = matrix[i][0];
    }
    n.matrix[rows][0] = 1;
    return n;
  }

  /**
   * Performs Rectified Linear Unit (ReLU) on the current matrix
   * Create a matrix NxM based on the current object but only values greater than
   * 0 are copied from the original matrix.
   * @see https://en.wikipedia.org/wiki/Rectifier_(neural_networks)
   * @param None
   * @return a new Matrix NxM based on the current object but only values greater
   *         than 0 are copied from the original matrix
   */
  public Matrix activate()
  {
    Matrix n = new Matrix(rows, cols);
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        n.matrix[i][j] = __relu(matrix[i][j]);
      }
    }
    return n;
  }

  /**
   * Rectified Linear Unit (ReLU) function
   * 
   * @see https://en.wikipedia.org/wiki/Rectifier_(neural_networks)
   * @param x_ the element value
   * @return the element value if it is greater than 0 otherwise returns 0
   */
  private float __relu(float x_)
  {
    return max(0, x_);
  }

  /**
   * Mutate function (Breeds)
   * Mutates some members randomly to attempt to find even better candidates
   * @see https://blog.coast.ai/lets-evolve-a-neural-network-with-a-genetic-algorithm-code-included-8809bece164
   * @param mutationRate_ the mutation rate
   * @return None
   */
  public void mutate(float mutationRate_)
  {
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        float rand = random(1);
        if (rand < mutationRate_)
        {
          matrix[i][j] += randomGaussian() / 5;

          if (matrix[i][j] > 1)
          {
            matrix[i][j] = 1;
          }
          if (matrix[i][j] < -1)
          {
            matrix[i][j] = -1;
          }
        }
      }
    }
  }

  /**
   * Crossover function (Breeds)
   * Hybrid 2 neuron layers randomly to attempt to find even better candidates
   * @see https://blog.coast.ai/lets-evolve-a-neural-network-with-a-genetic-algorithm-code-included-8809bece164
   * @param partner_ the neuron layer partner
   * @return The child
   */
  public Matrix crossover(Matrix partner_)
  {
    Matrix child = new Matrix(rows, cols);

    int randC = (int) Math.floor(random(cols));
    int randR = (int) Math.floor(random(rows));

    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        if ((i < randR) || (i == randR && j <= randC))
        {
          child.matrix[i][j] = matrix[i][j];
        } else
        {
          child.matrix[i][j] = partner_.matrix[i][j];
        }
      }
    }
    return child;
  }

  /**
   * Clone function (Breeds)
   * Clone a candidate and create a new child (the champion)
   * @see https://blog.coast.ai/lets-evolve-a-neural-network-with-a-genetic-algorithm-code-included-8809bece164
   * @param None
   * @return The clone
   */
  public Matrix clone()
  {
    Matrix clone = new Matrix(rows, cols);
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        clone.matrix[i][j] = matrix[i][j];
      }
    }
    return clone;
  }
  
    /**
   * Clone function (Breeds)
   * Clone a candidate and create a new child (the champion)
   * @see https://blog.coast.ai/lets-evolve-a-neural-network-with-a-genetic-algorithm-code-included-8809bece164
   * @param None
   * @return The clone
   */
  public void set( final int row, final int col, final float value )
  {
    matrix[row][col] = value;
  }
}