import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
public class GameOver extends JPanel implements MouseMotionListener, MouseListener{
	private static int mouseX,mouseY;
	private Image background = new ImageIcon("gameOver.jpg").getImage();
	private boolean Down=false; //see if the mouse is down or not
	private int rx=270,ry=350,rLength=60,rWidth=243; //dimentions of Restart text
	private int hsx=210,hsy=420,hsLength=60,hsWidth=360; //dimentions of HighScore text
	private Font font = new Font("Impact", Font.PLAIN, 70);
	private Font LARGEfont = new Font("Impact", Font.PLAIN, 120);
	private int oldScoretoBlit=0; //the score to blit once the original variable is cleared to 0
	BoxHead BH;
	public GameOver(BoxHead b){
		BH=b;
		setSize(800,640);
	}
    // ------------ MouseListener ------------------------------------------
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {Down=false;}    
    public void mouseClicked(MouseEvent e){}  
    public void mousePressed(MouseEvent e){	Down=true;}
    	
    // ---------- MouseMotionListener ------------------------------------------
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){
    	mouseX=e.getX();
    	mouseY = e.getY();
    }
    public boolean collide(int x,int y,int w, int l){
    	//checks the collision of the mouse and a rectangle
		return x<=mouseX&&x+w>=mouseX&&y<=mouseY&&y+l>=mouseY;
	}
    public void activateMouse(){
    	//activate the two mouse listeners
    	addMouseMotionListener(this);
		addMouseListener(this);
    }
    public void paintComponent(Graphics g){
    	g.drawImage(background,0,0,this);
    	g.setFont(LARGEfont);
    	g.drawString("GAME OVER", 130, 130);
    	g.setColor(Color.black);
    	g.setFont(font);
    	if (oldScoretoBlit==0){
    		//either the score is actually 0 or the score has not been saved to oldScoretoBlit
    		oldScoretoBlit=BH.score;
    	}
    	
    	//in the following code, the String is drawn on the y-coordinate ny+nLength
    	//because nx,ny,nWidth,nLength is the rectangle for collision
    	//but the text is draw on top of the box at those four coordinates
    	//doing ny+nLength places the String right in the middle of the rectangle
    	if (collide(rx,ry,rWidth,rLength)&&Down){
    		//if Restart text is pressed
    		//change the state back
    		//clear oldScoretoBlit for next time
    		g.setColor(Color.black);
    		g.drawString("RESTART", rx, ry+rLength);
    		BH.state=BH.GAME;
    		BH.game.restart();
    		BH.game.requestFocus();
    		oldScoretoBlit=0;
    	}
    	else if (collide(rx,ry,rWidth,rLength)){
    		//Restart hovered
    		g.setColor(Color.gray);
    		g.drawString("RESTART", rx, ry+rLength);
    		
    	}
    	else{
    		g.setColor(Color.black);
    		g.drawString("RESTART",rx,ry+rLength);
    	}
    	if (collide(hsx,hsy,hsWidth,hsLength)&&Down){
    		//if HighScore is pressed
    		g.setColor(Color.black);
    		g.drawString("HIGH SCORES", hsx, hsy+hsLength);
    		BH.state=BH.HS;
    		BH.hs.requestFocus();
    		BH.hs.loadList();
    		BH.hs.addScore(BH.score);
    		BH.score=0;
    	}
    	else if(collide(hsx,hsy,hsWidth,hsLength)){
    		//if HighScore hovered
    		g.setColor(Color.GRAY);
    		g.drawString("HIGH SCORES", hsx, hsy+hsLength);
    	}
    	else{
    		g.setColor(Color.black);
    		g.drawString("HIGH SCORES", hsx, hsy+hsLength);
    	}
    	g.setColor(Color.black);
    	if(oldScoretoBlit==0){
    		//blit 3 zeroes instead of 1 since the next highest score is 200
    		g.drawString("000",325,550);
    	}
    	else{
    		g.drawString(oldScoretoBlit+"", 325, 550);
    	}
    }
}
