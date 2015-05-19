import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements KeyListener{
	private BoxHead BH;
	private boolean[] keys; 
	private Image background = new ImageIcon("background.jpg").getImage();
	public GamePanel(BoxHead bh){
		BH=bh;
		keys = new boolean[65535];
		addKeyListener(this);
	}
	public void keyTyped(KeyEvent e){
		
	}
    public void keyPressed(KeyEvent e){
    	keys[e.getKeyCode()]=true;
    }
    public void keyReleased(KeyEvent e){
    	keys[e.getKeyCode()]=false;
    }
    public void addNotify(){
    	super.addNotify();requestFocus();
    	BH.start();
    }
	public ArrayList<PosPair> moveFireBalls(ArrayList<PosPair> fireballs){
		//move fireballs in the direction that they should be moving toward 
		for (int i=0;i<fireballs.size();i++){
			//fireballs.set(i, element) do something move the fireball
			//check if the fireall hit the mainchracter:
			if (checkCollision(fireballs.get(i).getX(),fireballs.get(i).getY(),BH.mc.getX(),BH.mc.getY())){
				//decrease health by 15 points per fireball
				BH.mc.setHealth(BH.mc.getHealth()-15);
			}
		}
		return fireballs;
	}
	public boolean checkCollision(int fx,int fy,int mcx,int mcy){
		//check if the fireball is on the character
		return (fx>=mcx && fx <=mcx+BH.mc.getWidth() && fy>=mcy && fy<=mcy+BH.mc.getLength());
	}
	public void MCshoot(){
		if (keys[KeyEvent.VK_SPACE]){
			//if the user shoots, add a bullet into the arraylist keeping track of flying bullets
			//BH.addBullet(new PosPair(BH.mc.getX(),BH.mc.getY(),BH.mc.getANGLE(),BH.getWeapon()));
			BH.activeBullets.add(new PosPair(BH.mc.getX(),BH.mc.getY(),BH.mc.getANGLE(),BH.mc.getWeapon()));
		}
	}
	public void moveMC(){
		if (keys[KeyEvent.VK_LEFT]){
			//maybe set the direction lol
			BH.mc.setX(Math.max(0, BH.mc.getX()-5));
		}
		else if (keys[KeyEvent.VK_RIGHT]){
			BH.mc.setX(Math.min(800-BH.mc.getWidth(), BH.mc.getX()+5));
		}
		else if (keys[KeyEvent.VK_UP]){
			BH.mc.setY(Math.max(0, BH.mc.getY()-5));
		}
		else if (keys[KeyEvent.VK_DOWN]){
			BH.mc.setY(Math.min(640-BH.mc.getLength(),BH.mc.getY()+5));
		}
	}
	public int calculateHealth(){
		//full health is at 100
		return (int)(BH.mc.getHealth()/100*20);
	}
	public boolean checkOutside(int x,int y){
		return x<0||x>800||y<0||y>640;
	}
	public void checkBulletCollision(){
		for (int i=0;i<BH.activeBullets.size();i++){
			if (checkCollision(BH.activeBullets.get(i).getX(),BH.activeBullets.get(i).getY(),BH.mc.getX(),BH.mc.getY())){
				BH.mc.setHealth(BH.mc.getHealth()-10); //change this to the actual value later;
				BH.activeBullets.remove(i);
			}
			else if (checkOutside(BH.activeBullets.get(i).getX(),BH.activeBullets.get(i).getY())){
				BH.activeBullets.remove(i);
			}
		}
	}
	public void moveBullets(){
		for (int i=0;i<BH.activeBullets.size();i++)
		{
			
		}
	}
	public void paintComponent(Graphics g){
		Font Sfont = new Font("Calisto MT", Font.BOLD, 30);
		g.setFont(Sfont);
		g.drawImage(background, 0, 0, this);
		g.drawString(BH.mc.getName(), BH.mc.getX()-5, BH.mc.getY()-10); //maybe do the string formatting with this later if we have time
		//drawing the health bar
		//figure out the colouring of the bar ugh
		//g.drawRect(BH.mc.getX()-5,BH.mc.getY()-3,calculateHealth(),5); //filling of the bar
		//g.drawRect(BH.mc.getX()-5, BH.mc.getY()-3, 20, 5); //drawing the outline
		for (int i=0;i<BH.activeBullets.size();i++){
			//we need to get bulllet sprites
			///g.drawImage(BH.bulletSprites.get(BH.activeBullets.get(i).getTYPE()),BH.activeBullets.get(i).getX(),BH.activeBullets.get(i).getY(),this);
			g.drawOval(BH.activeBullets.get(i).getX(), BH.activeBullets.get(i).getY(), 1, 1);
			//we need to move the bullets...either call a function here or call it from the BoxHead class
		}
		g.setColor(new Color (255,0,0));
		g.drawRect(BH.mc.getX(),BH.mc.getY(),30,70);
	}
}
