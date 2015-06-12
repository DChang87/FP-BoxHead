import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
public class SelectionMenu extends JPanel implements KeyListener{
	private Image background = new ImageIcon("selectionBackground.jpg").getImage();
	BoxHead BH;
	private boolean[] keys;
	private Font font = new Font("Impact", Font.PLAIN, 35);
	public SelectionMenu(BoxHead b){
		keys = new boolean[65535];
		BH=b;
		addKeyListener(this);
		setSize(800,640);
	}
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){keys[e.getKeyCode()]=true;}
    public void keyReleased(KeyEvent e){keys[e.getKeyCode()]=false;}
    public void checkUnPause(){
    	//check if the user decided to return to the game
    	if (keys[KeyEvent.VK_P]){
			BH.state=BH.GAME;
			BH.game.requestFocus();
			keys[KeyEvent.VK_P]=false; //set to false so next time selectionMenu is needed, keys[KeyEvent.VK_P] would not be set at true and disrupt the program
		}
	}
    public void paintComponent(Graphics g){
    	g.setFont(font);
    	g.drawImage(background,0,0,this);
    	
    	//draw the names of all the available upgrades
    	//if they have been obtained, they are in green
    	//if not then they are in read
    	//half on one side half on the other side of the screen
    	for (int i=0;i<13;i++){
    		if (BH.game.getNextUpgrade()>i){
    			g.setColor(Color.green);
    		}
    		else{
    			g.setColor(Color.red);
    			
    		}
    		g.drawString(BH.ug.getUpgradeString(i), 30, 150+i*30);
    	}
    	for (int i=13;i<26;i++){
    		if (BH.game.getNextUpgrade()>i){
    			g.setColor(Color.green);
    		}
    		else{
    			g.setColor(Color.red);
    		}
    		g.drawString(BH.ug.getUpgradeString(i), 430, 150+(i-13)*30);
    	}
    }
    
	    
}
