/* SimonPanel.java
 * Author: Matthew Smillie
 * Date Created: 7 March 2017
 * Date Last Edited: 17 March 2017
 * 
 * Creates a JPanel with clickable polygons to play the game Simon.
 * I learned how to use clickable polygons from Chihiro Jimbo (chijim) on Github: https://gist.github.com/chijim/7847738
 * 
 * The program utilizes a timer to update graphics according to events from the timer itself and a mouse listener. 
 * It utilizes a Queue ADT to store user input and generated numberss
 */

package simon;

//import relevant assets
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import queue.LinkedQueue;
import queue.QueueInterface;

public class SimonPanel extends JPanel implements MouseListener{
	
	//serialize
	private static final long serialVersionUID = 1L;
	
	//graphics variables
	private Polygon green;
	private Polygon red;
	private Polygon blue;
	private Polygon yellow;
	private Polygon start;
	private int buffer=40;
	private final int width = 490, height = 490;
	
	//colors
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color BLACK = new Color(0,0,0);
	
	public static final Color LIGHTRED = new Color(255,108,79);
	public static final Color RED = new Color(200,0,0);
	
	public static final Color LIGHTYELLOW = new Color(252, 255, 99);
	public static final Color YELLOW = new Color(200,200,0);
	
	public static final Color LIGHTGREEN = new Color(91,255,97);
	public static final Color GREEN = new Color(0,200,0);
	
	public static final Color LIGHTLIMEGREEN = new Color(149, 255, 84);
	public static final Color LIMEGREEN = new Color(100, 200, 0);
	
	public static final Color LIGHTBLUE = new Color(0, 148, 255);
	public static final Color BLUE = new Color(0, 0, 255);
	
	//Random, for number generation
	private Random rand = new Random();
	
	//since the main method utilizes a timer, a system of flags is used to activate different commands in the timer
	private boolean listenButtonPress;//when true, the user can click buttons, this is set as true when orderRelayed is true
	
	//when these are true, the paint component lights up the corresponding regions and then lowers them
	//these can be set true by the mouse listener and the flash method
	private boolean greenPress;
	private boolean redPress;
	private boolean bluePress;
	private boolean yellowPress;
	
	//when true, user input is processed. Set to true whenever a button is pressed. 
	//automatically set to false after input is processed
	private boolean isDirty;
	
	//when false, none of the code in main aside from repaint is run. set as true from the start button
	private boolean started;
	
	//when true, all queues are cleared and it sets itself to false
	//only used in the first timer cycle after started is set to true
	private boolean freshlyStarted;
	
	//when false, the timer will not exit the part of the code that relays the simon Pattern
	//orderRelayed is then set to true. It is again set to false after the user has completed their turn
	private boolean orderRelayed;
	
	//when false, a new part of the colorOrder will be created, and copied to temp queue
	//orderGenerated is then set to true. It is again set to false after the user has completed their turn
	private boolean orderGenerated;
	
	private boolean shouldRelay;//leaves a pause between relays. false means the step won't be relayed
	
	//Simon Data Types
	//each color on the Simon Panel corresponds to a number between 0 and 3
	private QueueInterface<Integer> colorOrder = new LinkedQueue<Integer>(); //unchanging memory
	private QueueInterface<Integer> userEntries = new LinkedQueue<Integer>(); //the store of userInputed variables
	private QueueInterface<Integer> tempQueue = new LinkedQueue<Integer>(); //used to store copies of colorOrder, for dequeuing
	
	/**
	 * constructor for SimonPanel
	 * creates the polygons that the buttons sit within, and adds a mouseListener
	 */
	public SimonPanel() {
		
		started = false;
		
		setPreferredSize(new Dimension(width, height));
		
		//the polygons don't precisely fit the buttons, but they at least contain them
		
		int[] greenX = {194, 229, 229, 140, 51,  16};
		int[] greenY = {16,  51,  140, 229, 229, 195};
		green = new Polygon(greenX, greenY, 6);
		
		int[] redX = {184, 250, 250, 339, 428, 463};
		int[] redY = {16,  51, 140, 229, 229, 195};
		red = new Polygon(redX, redY, 6);

		int[] blueX = {468, 428, 339, 250, 250, 272};
		int[] blueY = {297, 250, 250, 339, 428, 457};
		blue = new Polygon(blueX, blueY, 6);

		int[] yellowX = {205, 229, 229, 140, 51,  10 };
		int[] yellowY = {459, 428, 339, 250, 250, 256};
		yellow = new Polygon(yellowX, yellowY, 6);
		
		int[] startX = {255, 255, 278, 278};
		int[] startY = {267, 289, 289, 267};
		start = new Polygon(startX, startY, 4);
		
		addMouseListener(this);
	}//end public constructor simonPanel
	
	/**
	 * paintComponent
	 * @param Graphics g
	 * this is repainted every 500 milliseconds or so, and different flags can change what is drawn
	 */
 	public void paintComponent(Graphics g) {
 		//the circle in the back
		g.setColor(BLACK);
		g.fillOval(buffer, buffer, 400, 400);
		
		//four arcs of green, red, blue, and yellow
		int arcRad = 380;
		int arcBuffer = buffer + 10;
		
		//different color buttons. depending on flags being raised, the color they will be lighter or darker
		if(greenPress){
			g.setColor(LIGHTGREEN);
			
			greenPress = false;
		}else{
			g.setColor(GREEN);
		}
		g.fillArc(arcBuffer, arcBuffer, arcRad, arcRad, 90, 90);
		
		if(redPress){
			g.setColor(LIGHTRED);
			
			redPress = false;
		}else{
			g.setColor(RED);
		}
		g.fillArc(arcBuffer, arcBuffer, arcRad, arcRad, 0, 90);
		if(bluePress){
			g.setColor(LIGHTBLUE);
			
			bluePress = false;
		}else{
			g.setColor(BLUE);
		}
		g.fillArc(arcBuffer, arcBuffer, arcRad, arcRad, -90, 90);
		if(yellowPress){
			g.setColor(LIGHTYELLOW);
			
			yellowPress = false;
		}else{
			g.setColor(YELLOW);
		}
		g.fillArc(arcBuffer, arcBuffer, arcRad, arcRad, 180, 90);
		
		//a black circle in the middle and two rectangles to cover up where the arcs meet
		int innerCirclePosition = 140;
		int innerCircleDiameter = 200;
		g.setColor(BLACK);
		g.fillOval(innerCirclePosition, innerCirclePosition, innerCircleDiameter, innerCircleDiameter);
		g.fillRect(50, 230, 380, 20);
		g.fillRect(230, 50, 20, 380);
		
		//a button in the lower right middle, which lights up is a flag has been raised
		if(freshlyStarted){
			g.setColor(LIGHTLIMEGREEN);
		}else{
			g.setColor(LIMEGREEN);
		}
		g.fillOval(255, 267, 25, 25);
		
		//a score counter in the lower left middle
		g.setColor(WHITE);
		
		//to display the score when a game starts
		String panelString;
		if(colorOrder.getSize()!=0){
			panelString = String.format("%d", (colorOrder.getSize()-1));
		}else{//when a game hasn't started, display a "blank" score counter
			panelString = String.format("_ _");
		}
		g.drawString(panelString, 205, 285);
	}//end paintComponent
 	
 	/**
 	 * main
 	 * main runs a timer which continually refreshes the panel graphics
 	 */
	
	public static void main(String[] args) {
		JFrame frame = new JFrame ("Simon");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
				
		SimonPanel myPanel = new SimonPanel();
		frame.getContentPane().add(myPanel);
		
		frame.pack();
		frame.setVisible(true);
		
		Timer myTimer =new Timer();
		myTimer.scheduleAtFixedRate(new TimerTask()
		{
			public void run()
			{	
				if(myPanel.started){//if a player has begun a game, or a game is ongoing
					
					if(myPanel.freshlyStarted){//if it's a new game
						myPanel.userEntries.clear();
						myPanel.freshlyStarted = false;//clear any values from premature button presses
					}
								/* for debug purposes
								System.out.println("private boolean listenButtonPress: " + myPanel.listenButtonPress
										+ "\n          private boolean isDirty: " + myPanel.isDirty
										+ "\n     private boolean orderRelayed: " + myPanel.orderRelayed
										+ "\n   private boolean orderGenerated: " + myPanel.orderGenerated
										+ "\n                  colorOrder size: " + myPanel.colorOrder.getSize()
										+ "\n                   tempQueue size: " + myPanel.tempQueue.getSize()
										+ "\n                 userEntries size: " + myPanel.userEntries.getSize());*/
					
					if(myPanel.orderRelayed){ //if the entire color order has been presented to the user
						
						myPanel.listenButtonPress=true;//listen to button presses
						
						if(myPanel.isDirty){//when a button is pressed
							//check if it's the right one
							if(myPanel.userEntries.dequeue().equals(myPanel.tempQueue.dequeue())){
								//if it is the right one
								
								//check if there are anymore colors left to press
								if(myPanel.tempQueue.isEmpty()){ 
									//if there aren't any more, set up a timer pass through where the order is flashed
									
									myPanel.orderGenerated = false;
									myPanel.orderRelayed=false;
								}
								
								//user input has been processed
								myPanel.isDirty=false;
							}else{
								//end the game, set all relevant flags to false and clear data stores
								myPanel.userEntries.clear();
								myPanel.tempQueue.clear();
								myPanel.colorOrder.clear();
								myPanel.orderGenerated = false;
								myPanel.orderRelayed=false;
								myPanel.started = false;
							}
						}//end user input processing block
						
					}else{//if the pattern hasn't all been relayed, 
						//ensure button presses aren't listened to
						myPanel.listenButtonPress = false;

						//generate new number
						if(!myPanel.orderGenerated){
							int n = myPanel.rand.nextInt(4);
							myPanel.colorOrder.enqueue(n);
							myPanel.tempQueue = myPanel.colorOrder.copy();
							myPanel.orderGenerated=true; 
						}
						
						//until temp queue has been emptied, raise flags on every pass through
						if(!myPanel.tempQueue.isEmpty()){
							if(myPanel.shouldRelay){
								myPanel.flash(myPanel.tempQueue.dequeue());
								myPanel.shouldRelay=false;
							}else{//half the time it won't flash, and the user has to wait until the next pass through
								myPanel.shouldRelay=true;
							}
						}else{ //once it's emptied, exit this block, but not before copying colorOrder to tempQueue
							myPanel.tempQueue = myPanel.colorOrder.copy();
							myPanel.orderRelayed = true;
						}
					}//end pattern relaying block
					
				}else{
					myPanel.listenButtonPress=true; //while not started, listen for button pressess
				}
				//every timer pass through, the frame is repainted
				frame.repaint();
			}
		}, 10, 300);
		
	}//end paintComponent
	
	/**
	 * eventHandler mouseClicked
	 * @param MouseEvent e
	 * handle any button presses by raising flags and adding numbers to userData Queue
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		//nothing will happen if listenButtonPress isn't raised
		if(listenButtonPress){
			
			//each raises a flag, enqueues the appropriate number, and sets user input for processing
			if (green.contains(e.getPoint())){
					greenPress = true; 
					userEntries.enqueue(0);
					isDirty = true;
			}
			
			if (red.contains(e.getPoint())){
					redPress = true;
					userEntries.enqueue(1);
					isDirty = true;
			}
			
			if (blue.contains(e.getPoint())){
					bluePress = true;
					userEntries.enqueue(2);
					isDirty = true;
			}			
			
			if (yellow.contains(e.getPoint())){
					yellowPress = true;
					userEntries.enqueue(3);
					isDirty = true;
			}

			if (start.contains(e.getPoint())){
				//to prevent any illicit input when a game is started, the button only works if a game isn't currently ongoing
				if(!started){
					//set flags for a safe primary timer run
					started = true;
					freshlyStarted = true;
					orderGenerated=false;
					isDirty = false;
				}
			}

		}
	}//end mouseHandler method
	

	/**
	 * flash
	 * @param region
	 * this is an interpreter for numbers from the queue. a number is inputted and a flag is raised for the
	 * paintComponent to handle
	 */
	public void flash(int region) {
		if(region == 0){
			greenPress=true;
		}
		if(region == 1){
			redPress=true;
		}
		if(region == 2){
			bluePress=true;
		}
		if(region == 3){
			yellowPress=true;
		}
	}//end public method flash
	
	//setters and getters for isDirty
	public boolean getIsDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	//obsolete method
	/*public void relayOrder(){
		int n = rand.nextInt(4);
		//generate new number
		colorOrder.enqueue(n);
		
		//copy colorArray to tempQueue
		tempQueue = colorOrder.copy();
		
		while(!tempQueue.isEmpty()){
			flash(tempQueue.dequeue());
		}
	}*/
}// end public class SimonPanel
