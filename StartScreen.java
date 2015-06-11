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
	private Image background = new ImageIcon("tempbackground.jpg").getImage();
	private Image up = new ImageIcon("startup.jpg").getImage();
	private Image hover = new ImageIcon("starthover.jpg").getImage();
	private Image down = new ImageIcon("startdown.jpg").getImage();
	private int bx=220,by=350,bLength=55,bWidth=390;
	private boolean Down=false;
	BoxHead BH;
	private Font font = new Font("Century Gothic", Font.PLAIN, 70);
	private int counter=50;
	public StartScreen(BoxHead b){
		//load the images for the start button
		BH=b;
		setSize(800,640);
		addMouseMotionListener(this);
		addMouseListener(this);
	}
    //public void addNotify() {
    	//super.addNotify();
    	//requestFocus();
    //}
    
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
		return bx<=mouseX&&bx+bWidth>=mouseX&&by<=mouseY&&by+bLength>=mouseY;
	}
	public void paintComponent(Graphics g){
		g.setFont(font);
    	g.drawImage(background,0,0,this);
    	if (collide()&&Down){
    		//g.drawImage(down, bx, by, this);
    		g.setColor(Color.black);
    		g.drawString("Start Game", bx, by+bLength);
    		BH.state=BH.GAME;
    		BH.game.requestFocus();
    	}
    	else if (collide()){
    		//g.drawImage(hover,bx,by,this);
    		g.setColor(Color.gray);
    		g.drawString("Start Game", bx, by+bLength);
    	}
    	else{
    		//g.drawImage(up,bx,by,this);
    		//g.drawRect(bx, by, bWidth, bLength);
    		g.setColor(Color.black);
    		g.drawString("Start Game", bx, by+bLength);
    	}
    }
	    
}
