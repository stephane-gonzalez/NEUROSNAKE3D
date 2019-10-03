/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import bot.Food;
import bot.Matrix;
import bot.NeuralNet;
import bot.Snake;
import com.jme3.math.Vector3f;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author steph
 */
public class Save {

  static Element createNeuralNetNode(Document document, final int iNodes, final int hNodes, final int hLayers, final int oNodes) throws ParserConfigurationException {

    // root element
    Element neuralNet = document.createElement("NeuralNet");

    // set attributes
    Attr attr = document.createAttribute("iNodes");
    attr.setValue(String.valueOf(iNodes));
    neuralNet.setAttributeNode(attr);

    attr = document.createAttribute("hNodes");
    attr.setValue(String.valueOf(hNodes));
    neuralNet.setAttributeNode(attr);

    attr = document.createAttribute("hLayers");
    attr.setValue(String.valueOf(hLayers));
    neuralNet.setAttributeNode(attr);

    attr = document.createAttribute("oNodes");
    attr.setValue(String.valueOf(oNodes));
    neuralNet.setAttributeNode(attr);

    document.appendChild(neuralNet);
    return neuralNet;
  }

  static Element createSnakeNode(Document document, final int gen, final int score, final int lifeTime, final int lifeLeft, final float sysTime) throws ParserConfigurationException {

    // root element
    Element snake = document.createElement("Snake");
    // set attributes
    Attr attr = document.createAttribute("gen");
    attr.setValue(String.valueOf(gen));
    snake.setAttributeNode(attr);

    attr = document.createAttribute("score");
    attr.setValue(String.valueOf(score));
    snake.setAttributeNode(attr);

    attr = document.createAttribute("lifeLeft");
    attr.setValue(String.valueOf(lifeLeft));
    snake.setAttributeNode(attr);

    attr = document.createAttribute("lifeTime");
    attr.setValue(String.valueOf(lifeTime));
    snake.setAttributeNode(attr);

    attr = document.createAttribute("sysTime");
    attr.setValue(String.valueOf(sysTime));
    snake.setAttributeNode(attr);

    
    return snake;
  }

  static Element createBrainNode(Document document, Element root, Matrix weights[]) throws ParserConfigurationException {

    // root element
    Element brain = document.createElement("Brain");
    root.appendChild(brain);

    int layerno = 0;
    for (Matrix matrix : weights) {
      Element layer = createLayerNode(document, brain, matrix, layerno++);
    }
    
    return brain;
  }
  
  static Element createLayerNode(Document document, Element root, final Matrix matrix, final int layerno) throws ParserConfigurationException {

    // root element
    Element layer = document.createElement("Layer");
    root.appendChild(layer);

    // set attributes
    Attr attr = document.createAttribute("no");
    attr.setValue(String.valueOf(layerno));
    layer.setAttributeNode(attr);

    attr = document.createAttribute("rows");
    attr.setValue(String.valueOf(matrix.rows));
    layer.setAttributeNode(attr);
    
    attr = document.createAttribute("cols");
    attr.setValue(String.valueOf(matrix.cols));
    layer.setAttributeNode(attr);
          
    for (int i = 0; i < matrix.rows; i++) {
      for (int j = 0; j < matrix.cols; j++) {
        // root element
        Element weight = document.createElement("Weight");
        root.appendChild(layer);

        // set attributes
        attr = document.createAttribute("i");
        attr.setValue(String.valueOf(i));
        weight.setAttributeNode(attr);

        // set attributes
        attr = document.createAttribute("j");
        attr.setValue(String.valueOf(j));
        weight.setAttributeNode(attr);

        weight.appendChild(document.createTextNode(String.valueOf(matrix.matrix[i][j])));
        layer.appendChild(weight);
      }
    }
    return layer;
  }
  
  
  static Element createFoodNode(Document document, Element root, ArrayList<Food> foodList) throws ParserConfigurationException {

    // root element
    Element xmlfoodList = document.createElement("Food");
    root.appendChild(xmlfoodList);

    int foodno = 0;
    for (Food food : foodList) {
      Element pos = document.createElement("Pos");
      xmlfoodList.appendChild(pos);

      // set attributes
      Attr attr = document.createAttribute("no");
      attr.setValue(String.valueOf(foodno++));
      pos.setAttributeNode(attr);

      attr = document.createAttribute("posx");
      attr.setValue(String.valueOf(food.getPos().x));
      pos.setAttributeNode(attr);

      attr = document.createAttribute("posy");
      attr.setValue(String.valueOf(food.getPos().y));
      pos.setAttributeNode(attr);

      attr = document.createAttribute("posz");
      attr.setValue(String.valueOf(food.getPos().z));
      pos.setAttributeNode(attr);

    }
    
    return xmlfoodList;
  }
  
  public static void save(final String pathName, final int gen, Snake snake) {
    try {
      
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();

      Element xmlneuralNet = createNeuralNetNode(document, snake.brain.iNodes, snake.brain.hNodes, snake.brain.hLayers, snake.brain.oNodes);
      Element xmlsnake = createSnakeNode(document, gen, snake.score, snake.lifeTime, snake.lifeLeft, snake.sysTime);
      xmlneuralNet.appendChild(xmlsnake);
      
      createBrainNode(document, xmlsnake, snake.brain.weights);
      createFoodNode(document, xmlsnake, snake.foodList);
      
      // create the xml file
      //transform the DOM Object to an XML File
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(new File(pathName + "save-" + gen + ".xml"));

      // If you use
      // StreamResult result = new StreamResult(System.out);
      // the output will be pushed to the standard output ...
      // You can use that for debugging 
      transformer.transform(domSource, streamResult);

      System.out.println("Done creating XML File");

    } catch (ParserConfigurationException | TransformerException pce) {
    }
  }

  public class Loaded
  {
    public NeuralNet neuralNet;
    public int gen;
    public int lifeLeft;
    public int lifeTime;
    public int score;
    public float sysTime;
    public ArrayList<Food> foodList;
      
    public Loaded(NeuralNet neuralNet_, ArrayList<Food> foodList_, int gen_, int lifeLeft_, int lifeTime_, int score_, float sysTime_)
    {
      neuralNet = neuralNet_;
      gen = gen_;
      lifeLeft = lifeLeft_;
      lifeTime = lifeTime_;
      score = score_;
      sysTime = sysTime_;
      foodList = foodList_;
    }
  };
  
  public Loaded loadLast(final String savePath) {
      
    File dir = new File(savePath);
    File[] files = dir.listFiles();

    Arrays.sort(files, new Comparator<File>() {
        public int compare(File f1, File f2) {
            return Long.valueOf(f2.lastModified()).compareTo(
                    f1.lastModified());
        }
    });
     
     return load(files[0].getAbsolutePath());
  }
  
  public Loaded load(final String fileName) {

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    try {
      
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final Document document = builder.parse(new File(fileName));

      // Print prologue
      System.out.println("*************PROLOGUE************");
      System.out.println("version : " + document.getXmlVersion());
      System.out.println("encodage : " + document.getXmlEncoding());
      System.out.println("standalone : " + document.getXmlStandalone());


      final Element xmlNeuralNet = document.getDocumentElement();
      final int hLayers = Integer.parseInt(xmlNeuralNet.getAttribute("hLayers"));
      final int hNodes = Integer.parseInt(xmlNeuralNet.getAttribute("hNodes"));
      final int oNodes = Integer.parseInt(xmlNeuralNet.getAttribute("oNodes"));
      final int iNodes = Integer.parseInt(xmlNeuralNet.getAttribute("iNodes"));
          
      System.out.println("\n*************NEURALNET************");   
      System.out.println("iNodes " + iNodes);
      System.out.println("hLayers " + hLayers);
      System.out.println("hNodes " + hNodes);
      System.out.println("oNodes " + oNodes);

      final Element xmlSnake = (Element) xmlNeuralNet.getElementsByTagName("Snake").item(0);
      final int gen = Integer.parseInt(xmlSnake.getAttribute("gen"));
      final int lifeLeft = Integer.parseInt(xmlSnake.getAttribute("lifeLeft"));
      final int lifeTime = Integer.parseInt(xmlSnake.getAttribute("lifeTime"));
      final int score = Integer.parseInt(xmlSnake.getAttribute("score"));
      final float sysTime = Float.parseFloat(xmlSnake.getAttribute("sysTime"));
      
      System.out.println("\n*************SNAKE************");   
      System.out.println("gen " + gen);
      System.out.println("lifeLeft " + lifeLeft);
      System.out.println("lifeTime " + lifeTime);
      System.out.println("score " + score);
      System.out.println("sysTime " + sysTime);
      
      final Element xmlBrain = (Element) xmlSnake.getElementsByTagName("Brain").item(0);
      
      final NodeList layerNodes = xmlBrain.getChildNodes();
      final int nbLayerNodes = layerNodes.getLength();

      Matrix matrix[] = new Matrix [hLayers + 2];
      
      for (int layerno = 0; layerno < nbLayerNodes; layerno++) 
      {
        if(layerNodes.item(layerno).getNodeType() == Node.ELEMENT_NODE) 
        {
          final Element xmllayer = (Element) layerNodes.item(layerno);
          final int no = Integer.parseInt(xmllayer.getAttribute("no"));
          final int rows = Integer.parseInt(xmllayer.getAttribute("rows"));
          final int cols = Integer.parseInt(xmllayer.getAttribute("cols"));
          
          final NodeList weightNodes = xmllayer.getChildNodes();
          final int nbWeightNodes = weightNodes.getLength();
          matrix[no] = new Matrix(rows, cols);
          
          for (int weightno = 0; weightno < nbWeightNodes; weightno++) 
          {
            if(weightNodes.item(weightno).getNodeType() == Node.ELEMENT_NODE) 
            {
              final Element xmlweight = (Element) weightNodes.item(weightno);
              final int i = Integer.parseInt(xmlweight.getAttribute("i"));
              final int j = Integer.parseInt(xmlweight.getAttribute("j"));
              final float value = Float.parseFloat(xmlweight.getTextContent());
              
              matrix[no].set(i,j,value);
            }
          }
        }
      }
      
      ArrayList<Food> foodList = new ArrayList<>();
      
      final Element xmlFood = (Element) xmlSnake.getElementsByTagName("Food").item(0);
      final NodeList foodNodes = xmlFood.getChildNodes();
      final int nbFoodNodes = foodNodes.getLength();
      
      for (int foodno = 0; foodno < nbFoodNodes; foodno++) 
      {
        if(foodNodes.item(foodno).getNodeType() == Node.ELEMENT_NODE) 
        {
          final Element xmlFoodPos = (Element) foodNodes.item(foodno);
          final int no = Integer.parseInt(xmlFoodPos.getAttribute("no"));

          final float posx = Float.parseFloat(xmlFoodPos.getAttribute("posx"));
          final float posy = Float.parseFloat(xmlFoodPos.getAttribute("posy"));
          final float posz = Float.parseFloat(xmlFoodPos.getAttribute("posz"));
          
          Food food = new Food(new Vector3f (posx, posy, posz));
          foodList.add(food);
        }
      }

      System.out.println( "Loaded ..." );
      return new Loaded(new NeuralNet(matrix), foodList, gen, lifeLeft, lifeTime, score, sysTime);
    } catch (final ParserConfigurationException | SAXException | IOException e) {
    }
    
    return null;
  }

}
