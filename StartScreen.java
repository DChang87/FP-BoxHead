import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
public class StartScreen extends JPanel implements MouseMotionListener, MouseListener{
	private static int mouseX,mouseY;
	private Image background = new ImageIcon("tempbackground.jpg").getImage();
	private boolean down=false;
	BoxHead mainFrame;
	public StartScreen(BoxHead b){
		//load the images for the start button
		
		mainFrame=b;
		addMouseMotionListener(this);
		addMouseListener(this);
		setSize(800,640);
	}
    public void addNotify() {
    	super.addNotify();
    	requestFocus();
    }
    // ------------ MouseListener ------------------------------------------
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {
    	down=false;
    }    
    public void mouseClicked(MouseEvent e){
    }  
    	 
    public void mousePressed(MouseEvent e){
		down=true;
	}
    	
    // ---------- MouseMotionListener ------------------------------------------
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){
    	mouseX=e.getX();
    	mouseY = e.getY();
    }
    
    public static boolean collide(int x,int y,int width, int height){
    	//check if the mouse is colliding with the button
    	return x<=mouseX && mouseX<=x+width && y<=mouseY && mouseY<=y+height;
    }
    
    public void paintComponent(Graphics g){
    	//draw the background and the button (According to the situation)
    	g.drawImage(background,0,0,this);
    	if (down){
    		System.out.println("down");
    		mainFrame.state=mainFrame.GAME;
    		setFocusable(false);
    		mainFrame.game.requestFocus();
    		
    	}
    }
	    
}
