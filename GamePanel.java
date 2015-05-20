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
    	super.addNotify();
    	requestFocus();
    	BH.start();
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
			BH.mc.setAngle(180);
			BH.mc.setX(Math.max(0, BH.mc.getX()-5));
		}
		else if (keys[KeyEvent.VK_RIGHT]){
			BH.mc.setX(Math.min(800-BH.mc.getWidth(), BH.mc.getX()+5));
			BH.mc.setAngle(0);
		}
		else if (keys[KeyEvent.VK_UP]){
			BH.mc.setY(Math.max(0, BH.mc.getY()-5));
			BH.mc.setAngle(270);
		}
		else if (keys[KeyEvent.VK_DOWN]){
			BH.mc.setY(Math.min(640-BH.mc.getLength(),BH.mc.getY()+5));
			BH.mc.setAngle(90);
		}
	}
	public boolean enemyCollision(int x, int y){
		ArrayList<Zombie> toRemove = new ArrayList<Zombie>();
		boolean flag = false;
		for (int i=0; i<BH.allZombies.size(); i++){
			if (BH.allZombies.get(i).getCollide(x,y)){
				flag = true;
				BH.allZombies.get(i).setHealth(BH.allZombies.get(i).getHealth() - 10);
			}
			if (BH.allZombies.get(i).getHealth() <= 0){
				toRemove.add(BH.allZombies.get(i));
			}
		}
		for (Zombie zzh8829:toRemove){
			BH.allZombies.remove(zzh8829);
		}
		return flag;
	}
	public int calculateHealth(){
		//full health is at 100
		return (int)(BH.mc.getHealth()/100*20);
	}
	public boolean checkOutside(int x,int y){
		return x<0||x>800||y<0||y>640;
	}
	public void checkBulletCollision(){
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (int i=0;i<BH.activeBullets.size();i++){
			if (enemyCollision(BH.activeBullets.get(i).getX(),BH.activeBullets.get(i).getY())){
				toRemove.add(BH.activeBullets.get(i));
			}
			else if (checkOutside(BH.activeBullets.get(i).getX(),BH.activeBullets.get(i).getY())){
				BH.activeBullets.remove(i);
				toRemove.add(BH.activeBullets.get(i));
			}
		}
		for (PosPair pair:toRemove){
			BH.activeBullets.remove(pair);
		}
	}
	public void moveBullets(){
		for (int i=0;i<BH.activeBullets.size();i++)
		{
			PosPair temp = BH.activeBullets.get(i);
			final double ANG = Math.toRadians(temp.getANGLE());
			double xx = temp.getDX(), yy = temp.getDY();
			BH.activeBullets.get(i).setPos(xx+10*Math.cos(ANG),yy+10*Math.sin(ANG));
		}
	}
	public void moveFireballs()
	{
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (int i=0;i<BH.fireballs.size();i++)
		{
			PosPair temp = BH.fireballs.get(i);
			final int ANG = temp.getANGLE();
			double xx = temp.getDX(), yy = temp.getDY();
			BH.fireballs.get(i).setPos(xx+10*Math.cos(ANG),yy+10*Math.sin(ANG));
			if (checkCollision(BH.fireballs.get(i).getX(),BH.fireballs.get(i).getY(),BH.mc.getX(),BH.mc.getY())){
				//decrease health by 15 points per fireball
				BH.mc.setHealth(BH.mc.getHealth()-15);
				toRemove.add(temp);
			}
			else if (checkOutside(BH.fireballs.get(i).getX(),BH.fireballs.get(i).getY())){
				toRemove.add(temp);
			}
		}
		for (PosPair pair:toRemove){
			BH.fireballs.remove(pair);
		}
	}
	public void paintComponent(Graphics g){
		Font Sfont = new Font("Calisto MT", Font.BOLD, 20);
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
			g.drawOval(BH.activeBullets.get(i).getX(), BH.activeBullets.get(i).getY(), 20, 20);
			//we need to move the bullets...either call a function here or call it from the BoxHead class
		}
		for (int i=0;i<BH.fireballs.size();i++){
			g.drawOval(BH.fireballs.get(i).getX(), BH.fireballs.get(i).getY(), 50, 50);
		}
		for (int i=0;i<BH.allZombies.size();i++){
			Zombie a = BH.allZombies.get(i);
			g.drawRect(a.getX(),a.getY(),a.getsx(), a.getsy());
		}
		
		g.setColor(new Color (255,0,0));
		g.drawRect(BH.mc.getX(),BH.mc.getY(),BH.mc.getsx(),BH.mc.getsy());
	}
}
