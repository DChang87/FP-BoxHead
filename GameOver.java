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
	private boolean Down=false;
	private Image up = new ImageIcon("restartup.jpg").getImage();
	private Image hover = new ImageIcon("restarthover.jpg").getImage();
	private Image down = new ImageIcon("restartdown.jpg").getImage();
	private int bx=350,by=400,bLength=50,bWidth=100;
	
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
    	Down=false;
    }    
    public void mouseClicked(MouseEvent e){
    }  
    	 
    public void mousePressed(MouseEvent e){
    	System.out.println("mousePressed gameover");
		Down=true;
	}
    	
    // ---------- MouseMotionListener ------------------------------------------
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){
    	mouseX=e.getX();
    	mouseY = e.getY();
    }
    public boolean collide(){
		return bx<=mouseX&&bx+bWidth>=mouseX&&by<=mouseY&&by+bLength>=mouseY;
	}
    public void activateMouse(){
    	addMouseMotionListener(this);
		addMouseListener(this);
    }
    public void paintComponent(Graphics g){
    	//draw the background and the button (According to the situation)
    	g.drawImage(background,0,0,this);
    	//System.out.println("paintComponent game over");
    	if (collide()&&Down){
    		BH.state=BH.GAME;
    		System.out.println("restart");
    		BH.game.restart();
    		BH.game.requestFocus();
    	
    	}
    	else if (collide()){
    		g.drawImage(hover,bx,by,this);
    	}
    	else{
    		g.drawImage(up,bx,by,this);
    	}
    	g.drawString(BH.score+"", 390, 500);
    }
}
