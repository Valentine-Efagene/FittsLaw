/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fittslaw;

/**
 *
 * @author valentyne
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import controlP5.*; 
import java.util.ArrayList;
import processing.core.*;
import processing.data.*; 

public class FittsLaw extends PApplet {
ControlP5 cp5;
final int NO_OF_TARGETS = 13;
ArrayList<Integer> configuration = new ArrayList<Integer>();
Table[][] tables;
Table table; 
int clicks = 0;
String user = "";
int wNum;
int dNum;
float[] D = {100, 200, 300};
float[] W = {30, 40, 50};
float d,  w;
int targetID = 0;
final float ANGULAR_DISPLACEMENT = 2 * PI / NO_OF_TARGETS;
float angle = 0;
int index;
int i = 0;
float radians = 0;
boolean target = false;
Circle[] c = new Circle[NO_OF_TARGETS];
int startTime;
PFont font;
PFont timeFont;
int config = 1;
int targetsRemaining = NO_OF_TARGETS;
int size = 9;
boolean started = false;

public void updateTable(Table t) {
  t = tables[wNum][dNum];
  TableRow newRow = t.addRow(); 
  newRow.setInt("S/N", t.lastRowIndex()); 
  newRow.setString("User", user); 
  newRow.setInt("MouseX", mouseX); 
  newRow.setInt("MouseY", mouseY); 
  newRow.setInt("Clicks", clicks); 
  newRow.setInt("Time (seconds)", (int)(millis() - startTime) / 1000); 
  newRow.setInt("Width", (int)W[wNum]); 
  newRow.setInt("Distance", (int)D[dNum]);
  newRow.setInt("Targets Remaining", NO_OF_TARGETS - countUsed());
}

public void mouseMoved(){
  if(started) {
    updateTable(table);
  }
}

public void mousePressed() {
  if(started){
    clicks++;
    updateTable(table);
  }
}

public void input(String theText) {
  user = theText;
  started = true;
}

public void setup(){
  
  
  PFont Cp5font = createFont("arial",20);
  cp5 = new ControlP5(this);
  cp5.addTextfield("input")
     .setPosition(20,100)
     .setSize(200,40)
     .setFont(Cp5font)
     .setFocus(true)
     .setColor(color(255,255,255))
     ;
     
  font = createFont("SansSerif", 70);
  timeFont = createFont("SansSerif", 40);
  
  tables = new Table[3][3];
  
  for(int i = 0; i < 3; i++){
    for(int j = 0; j < 3; j++){
      tables[i][j] = new Table();
      tables[i][j].addColumn("S/N"); 
      tables[i][j].addColumn("User"); 
      tables[i][j].addColumn("Width"); 
      tables[i][j].addColumn("Distance"); 
      tables[i][j].addColumn("Clicks");
      tables[i][j].addColumn("Time (seconds)"); 
      tables[i][j].addColumn("MouseX"); 
      tables[i][j].addColumn("MouseY"); 
      tables[i][j].addColumn("Targets Remaining");
    }
  }
  
  for(int i = 1; i <= 9; i++){
    configuration.add(i);
  }
  
  index = PApplet.parseInt( random( size) );
  reset( configuration.get( index ) );
}

public void draw(){
  if(user == ""){
    return;
  }else if(config == 1){
    started = true;
  }
  
  textFont(font);
  
  if(config == 10){
    started = false;
    background(200);
    fill(0);
    text("The End!", 200, 200);
    return;
  }else{
    fill(0);
    text(config, 200, 200);
  }
  
  pushMatrix();
  translate(width/2, height/2);
  
  if(i >= NO_OF_TARGETS){
    i = 0;
  }
  
  rotate(ANGULAR_DISPLACEMENT * i);
  
  if(c[i].isClicked()){
    targetsRemaining--;
    targetID = (i + 7) % NO_OF_TARGETS;
    
    while(c[targetID].isUsed()){
      targetID = (targetID + 1) % NO_OF_TARGETS;
    }
  }
  
  if (i == targetID ){
    target = true; 
  }else{
    target = false; 
  }
  
  c[i].draw(target);
  i++;
  
  popMatrix();
  
  if(countUsed() == NO_OF_TARGETS){
    saveTable(table, "/data/Fitts Law/" + user + "/" + "user_" + user + "_width_" + W[wNum] + "_height_" + D[dNum] + ".csv", "csv");
    
    if(configuration.isEmpty()){
      config++;
      return;
    }
    
    size = configuration.size();
    index = PApplet.parseInt( random( size) );
    reset( configuration.get(index) );
  }
}

public int countUsed(){
  int count = 0;
  
  for(Circle i : c){
    if(i.isUsed()){
      count++;
    }
  }
  
  return count;
}

public void reset(int con){
  clicks = 0;
  config++;
  targetsRemaining = 13;
  println("size " + size);
  println( "config " + configuration);
  println("index " + index);
  
  switch(con){
    case 1:
      background(200);
      wNum = 0;
      dNum = 0;
      break;
    
    case 2:
      background(200);
      wNum = 0;
      dNum = 1;
      break;
      
      case 3:
      background(200);
      wNum = 0;
      dNum = 2;
      break;
      
      case 4:
      background(200);
      wNum = 1;
      dNum = 0;
      break;
      
      case 5:
      background(200);
      wNum = 1;
      dNum = 1;
      break;
      
      case 6:
      background(200);
      wNum = 1;
      dNum = 2;
      break;
      
      case 7:
      background(200);
      wNum = 2;
      dNum = 0;
      break;
      
      case 8:
      background(200);
      wNum = 2;
      dNum = 1;
      break;
      
      case 9:
      background(200);
      wNum = 2;
      dNum = 2;
      break;
      
    default:
      break; 
  }
  
  w = W[wNum];
  d = D[dNum];
  table = tables[wNum][dNum];
      
  for(int i = 0; i < NO_OF_TARGETS; i++){
    c[i] = new Circle( 0.0f,  d, w );
  }
  
  startTime = millis();
  configuration.remove(index);
}

class Circle{
  float w, x,  y;
  boolean target;
  boolean clicked = false;
  boolean used = false;
  int fillColor;
  int red = color(255, 0, 0);
  int white = color(255, 255, 255);
  int grey = color(128, 128, 128);
  
  Circle( float x,  float y, float w){
    this.w = w;
    this.x = x; 
    this.y = y;
  }
  
  public void draw(boolean target){
    this.target = target;
    if (target){
      fillColor = red;
    } else{
      fillColor = white; 
      clicked = false;
    }
    
    if(used){
      fillColor = grey;
    }
    
    fill(fillColor);
    
    ellipse(x, y, w,  w);
    
    if(target && get(mouseX, mouseY) == color(255, 0, 0) && mousePressed){
      this.target = false;
      clicked = true;
      used = true;
      
    }
  }
  
  public boolean isClicked(){
    return clicked; 
  }
  
  public boolean isUsed(){
    return used;
  }
 
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "FittsLaw" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
