import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
public class StartScreen extends JPanel implements MouseMotionListener, MouseListener{
	private static int mouseX,mouseY;
	private Image background = new ImageIcon("sprites/tempbackground.jpg").getImage();
	private int bx=240,by=350,bLength=57,bWidth=338; //dimensions for the "START GAME" string
	private boolean Down=false; //if the mouse is down
	BoxHead BH;
	private Font font = new Font("Impact", Font.PLAIN, 70);
	public StartScreen(BoxHead b){
		BH=b;
		setSize(800,640);
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	public void mouseClicked(MouseEvent e) {}	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {Down=true;}
	public void mouseReleased(MouseEvent e) {Down=false;}
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	public boolean collide(){
		//if mouse collides with the rectangle containing the string
		return bx<=mouseX&&bx+bWidth>=mouseX&&by<=mouseY&&by+bLength>=mouseY;
	}
	public void paintComponent(Graphics g){
		g.setFont(font);
    	g.drawImage(background,0,0,this);
    	
    	//ny+nLength instead of ny so the String would fit perfectly in the rectangle for collision
    	if (collide()&&Down){
    		//pressed
    		g.setColor(Color.black);
    		g.drawString("START GAME", bx, by+bLength);
    		BH.state=BH.GAME;
    		BH.game.requestFocus();
    	}
    	else if (collide()){
    		//hovering
    		g.setColor(Color.gray);
    		g.drawString("START GAME", bx, by+bLength);
    	}
    	else{
    		g.setColor(Color.black);
    		g.drawString("START GAME", bx, by+bLength);
    	}
    }
	    
}
