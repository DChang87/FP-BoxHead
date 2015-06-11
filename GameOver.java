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
	private Image rup = new ImageIcon("restartup.jpg").getImage();
	private Image rhover = new ImageIcon("restarthover.jpg").getImage();
	private Image rdown = new ImageIcon("restartdown.jpg").getImage();
	private Image hsup = new ImageIcon("hsUp.jpg").getImage();
	private Image hshover = new ImageIcon("hpHover.jpg").getImage();
	private Image hsdown = new ImageIcon("hsDown.jpg").getImage();
	private int rx=350,ry=300,rLength=50,rWidth=100;
	private int hsx=350,hsy=500,hsLength=50,hsWidth=100;
	
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
    public boolean collide(int x,int y,int w, int l){
		return x<=mouseX&&x+w>=mouseX&&y<=mouseY&&y+l>=mouseY;
	}
    public void activateMouse(){
    	addMouseMotionListener(this);
		addMouseListener(this);
    }
    public void paintComponent(Graphics g){
    	//draw the background and the button (According to the situation)
    	g.drawImage(background,0,0,this);
    	if (collide(rx,ry,rWidth,rLength)&&Down){
    		g.drawImage(rdown,rx,ry,this);
    		BH.state=BH.GAME;
    		System.out.println("restart");
    		BH.game.restart();
    		BH.game.requestFocus();
    	}
    	else if (collide(rx,ry,rWidth,rLength)){
    		g.drawImage(rhover,rx,ry,this);
    	}
    	else{
    		g.drawImage(rup,rx,ry,this);
    	}
    	if (collide(hsx,hsy,hsWidth,hsLength)&&Down){
    		g.drawImage(hsdown,hsx,hsy,this);
    		BH.state=BH.HS;
    		BH.hs.requestFocus();
    		BH.hs.loadList();
    		BH.hs.addScore(BH.score);
    	}
    	else if(collide(hsx,hsy,hsWidth,hsLength)){
    		g.drawImage(hshover,rx,ry,this);
    	}
    	else{
    		g.drawImage(hsup,hsx,hsy,this);
    	}
    	g.drawString(BH.score+"", 390, 500);
    }
}
