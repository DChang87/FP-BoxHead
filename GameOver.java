import java.awt.Graphics;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
public class GameOver extends JPanel implements MouseMotionListener, MouseListener{
	private static int mouseX,mouseY;
	private Image background = new ImageIcon("gameOver.jpg").getImage();
	private boolean down=false;
	//public TextField nametext = new TextField("enter your name",20);
	BoxHead BH;
	public GameOver(BoxHead b){
		//load the images for the start button
		//nametext.setBounds(350, 200, 100, 50);
		System.out.println("public gameover");
		BH=b;
		addMouseMotionListener(this);
		addMouseListener(this);
		setSize(800,640);
		//nametext.setVisible(true);
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
    	System.out.println("mousePressed");
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
    	System.out.println("paintComponent game over");
    	if (down){
    		System.out.println("down game over");
    		BH.state=BH.GAME;
    		//setFocusable(false);
    		BH.game.requestFocus();
    		
    	}
    }
	    
}
