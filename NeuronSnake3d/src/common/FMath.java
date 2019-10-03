package common;

import java.util.Random;

public class FMath
{
  public static float random(float min, float max)
  {
    Random rand = new Random();
    return (rand.nextFloat() * ((max - min) + 1.0f)) + min;
  }

  public static float random(float max)
  {
    return random(0, max);
  }

  public static float randomGaussian()
  {
    Random rand = new Random();
    return (float) rand.nextGaussian();
  }

  public static float abs(float f)
  {
    return (f <= 0) ? 0 - f : f;
  }

  public static float min(float a, float b)
  { // this check for NaN, from JLS 15.21.1, saves a method call
    if (a != a)
      return a; // no need to check if b
    // is NaN; < will work correctly 
    // recall that -0.0 == 0.0, 
    // but [+-]0.0 - [+-]0.0 behaves special
    if (a == 0 && b == 0)
      return -(-a - b);
    return (a < b) ? a : b;
  }

  public static float max(float a, float b)
  { // this check for NaN, from JLS 15.21.1, saves a method call
    if (a != a)
      return a; 
    // no need to check if b is NaN; > will work correctly 
    // recall that -0.0 == 0.0, 
    // but [+-]0.0 - [+-]0.0 behaves special
    if (a == 0 && b == 0)
      return a - -b;
    return (a > b) ? a : b;
  }
  
  /*
  public static float manhattan_distance(float [] data_i, float [] data_j){
    float dist = 0;
    for(int dim = 0 ; dim < data_i.length; dim++) dist += Math.abs(data_i[dim]- data_j[dim]);
    return dist;
  }
  
  public static float euclidean_distance(Vector3f data_i, Vector3f data_j){
    data_i.distance(data_j)
    float dist = 0;
    for(int dim = 0 ; dim < data_i.length; dim++) dist += Math.pow(data_i[dim]- data_j[dim], 2);
    return (float) Math.sqrt(dist);
  }
  
  public static float tchebyshev_distance(float [] data_i, float [] data_j){
    float max =0 ;
    for(int dim = 0 ; dim < data_i.length; dim++) {
      float dist = Math.abs(data_i[dim]- data_j[dim]);
      if(dist > max) max = dist;
    }
    return max;
  }
  
*/
}
