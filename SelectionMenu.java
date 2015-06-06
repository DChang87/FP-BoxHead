import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
public class SelectionMenu extends JPanel implements KeyListener{
	private static int mouseX,mouseY;
	private Image background = new ImageIcon("selectionBackground.jpg").getImage();
	private boolean down=false;
	BoxHead BH;
	private boolean[] keys;
	public SelectionMenu(BoxHead b){
		//load the images for the start button
		System.out.println("Selection menu");
		keys = new boolean[65535];
		BH=b;
		addKeyListener(this);
		setSize(800,640);
	}
    //public void addNotify() {
    	//super.addNotify();
    	//requestFocus();
    //}
    public void keyTyped(KeyEvent e){
		
	}
    public void keyPressed(KeyEvent e){
    	keys[e.getKeyCode()]=true;
    }
    public void keyReleased(KeyEvent e){
    	keys[e.getKeyCode()]=false;
    }
    public void checkUnPause(){
    	//System.out.println("checkunpause");
		if (keys[KeyEvent.VK_P] || down){
			BH.state=BH.GAME;
			System.out.println("checkunpause");
			BH.game.requestFocus();
			keys[KeyEvent.VK_P]=false;
		}
	}
    public void paintComponent(Graphics g){
    	//draw the background and the button (According to the situation)
    	g.drawImage(background,0,0,this);
    }
    
	    
}
