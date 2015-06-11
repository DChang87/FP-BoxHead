import java.awt.Color;
import java.awt.Font;
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
	private int rx=270,ry=350,rLength=60,rWidth=243;
	private int hsx=210,hsy=420,hsLength=60,hsWidth=360;
	private Font font = new Font("Impact", Font.PLAIN, 70);
	private Font LARGEfont = new Font("Impact", Font.PLAIN, 120);
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
    	g.setFont(LARGEfont);
    	g.drawString("GAME OVER", 130, 130);
    	g.setColor(Color.black);
    	g.setFont(font);
    	
    	if (collide(rx,ry,rWidth,rLength)&&Down){
    		//g.drawImage(rdown,rx,ry,this);
    		g.setColor(Color.black);
    		g.drawString("RESTART", rx, ry+rLength);
    		BH.state=BH.GAME;
    		System.out.println("restart");
    		BH.game.restart();
    		BH.game.requestFocus();
    	}
    	else if (collide(rx,ry,rWidth,rLength)){
    		//g.drawImage(rhover,rx,ry,this);
    		g.setColor(Color.gray);
    		g.drawString("RESTART", rx, ry+rLength);
    		
    	}
    	else{
    		g.setColor(Color.black);
    		//g.drawImage(rup,rx,ry,this);
    		//g.drawRect(rx, ry, rWidth, rLength);
    		g.drawString("RESTART",rx,ry+rLength);
    	}
    	if (collide(hsx,hsy,hsWidth,hsLength)&&Down){
    		//g.drawImage(hsdown,hsx,hsy,this);
    		g.setColor(Color.black);
    		g.drawString("HIGH SCORES", hsx, hsy+hsLength);
    		BH.state=BH.HS;
    		BH.hs.requestFocus();
    		BH.hs.loadList();
    		BH.hs.addScore(BH.score);
    		BH.score=0;
    	}
    	else if(collide(hsx,hsy,hsWidth,hsLength)){
    		//g.drawImage(hshover,rx,ry,this);
    		g.setColor(Color.GRAY);
    		g.drawString("HIGH SCORES", hsx, hsy+hsLength);
    	}
    	else{
    		//g.drawImage(hsup,hsx,hsy,this);
    		g.setColor(Color.black);
    		g.drawString("HIGH SCORES", hsx, hsy+hsLength);
    		//g.drawRect(hsx,hsy,hsWidth,hsLength);
    	}
    	g.setColor(Color.black);
    	g.drawString(BH.score+"", 375, 550);
    }
}
