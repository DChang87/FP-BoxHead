import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
public class HighScore extends JPanel implements KeyListener{
	BoxHead BH;
	private Image background = new ImageIcon("sprites/hsBackground.jpg").getImage();
	private ArrayList<Integer> list= new ArrayList<Integer>();
	private boolean[] keys;
	private Font font = new Font("Impact", Font.PLAIN, 40);
	
	public HighScore(BoxHead b){
		keys = new boolean[65535];
		addKeyListener(this);
		BH=b;
		setSize(800,640);
	}
	public void loadList(){
		//read the input file and add them to the list
		Scanner infile = null;
		list.clear(); //clear the list in case it contained numbers before
		try{
			infile = new Scanner(new File("high_score.txt"));
			for (int i=0;i<10;i++){
				list.add(Integer.parseInt(infile.nextLine()));
			}
		}
		catch(IOException ex){}
		
	}   
    public void addScore(int score){
    	//this method adds the score from the game that was just played
    	//sorts the list and determines the new top 10 scores
    	//then this list is recorded in the output file
    	list.add(score);
    	ArrayList<Integer> toSort = new ArrayList<Integer>();
    	for (int i=0;i<list.size();i++){
    		toSort.add(list.get(i));
    	}
    	list.clear(); //empty list to store the new top 10 scores
    	Collections.sort(toSort);
    	for (int i=toSort.size()-1;i>=toSort.size()-10;i--){
    		//add the top 10 scores back to list
    		list.add(toSort.get(i));
    	}
    	writeToFile();
    }
    public void writeToFile(){
    	//write to file the top 10 scores 
    	Writer writer = null;
    	try{
    		FileWriter outfile = new FileWriter(new File("high_score.txt"));
    		for (int i=0;i<10;i++){
        		outfile.write(list.get(i)+"\n");
        	}
    		outfile.close();
    	}
    	catch(IOException ex){}
    }
    public void keyPressed(KeyEvent e) {keys[e.getKeyCode()]=true;}
	public void keyReleased(KeyEvent e) {keys[e.getKeyCode()]=false;}
	public void keyTyped(KeyEvent arg0) {}
    
    public void paintComponent(Graphics g){
    	g.setFont(font);
    	g.drawImage(background,0,0,this);
    	if (keys[KeyEvent.VK_SPACE]){
    		//if the user presses space, go back to gameOver screen
    		list.clear(); //clears the list for next time use
    		BH.state=BH.OVER; //change the state to gameOver
    		BH.go.requestFocus();
    		keys[KeyEvent.VK_SPACE]=false; //set it as false so it is false for the next time the user goes to highScore screen
    	}
    	for (int i=0;i<list.size();i++){
    		g.drawString(i+".            "+list.get(i)+"", 300, 180+i*40);
    		//because who counts starting from 1 anyways
    		
    	}
    }	
}
