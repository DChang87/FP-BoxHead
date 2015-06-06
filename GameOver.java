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
	
	BoxHead BH;
	public GameOver(BoxHead b){
		//load the images for the start button
		
		System.out.println("public gameover");
		BH=b;
		
		setSize(800,640);
		
	}
    //public void addNotify() {
//    	super.addNotify();
    	//requestFocus();
    //}
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
    public void enableTextEntry(){
    	System.out.println("weheres that text");
    	//BH.nametext.setVisible(true);
    	addMouseMotionListener(this);
		addMouseListener(this);
    	//BH.nametext.setText("yoolo");
    }
    public void paintComponent(Graphics g){
    	//draw the background and the button (According to the situation)
    	g.drawImage(background,0,0,this);
    	//System.out.println("paintComponent game over");
    	g.drawRect(350, 200, 100, 50);
    	if (down){
    		System.out.println("down game over");
    		BH.state=BH.GAME;
    		//setFocusable(false);
    		BH.game.requestFocus();
    	}
    }
}
