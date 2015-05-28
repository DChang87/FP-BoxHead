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
	private Image[][] charSprites = new Image[8][3];
	private Image[][] zombieSprites = new Image[8][8];
	private int spriteCounter = 0;
	public GamePanel(BoxHead bh){
		BH=bh;
		keys = new boolean[65535];
		addKeyListener(this);
		for (int i=0;i<8;i++){
			for (int k=0;k<3;k++){
				charSprites[i][k]=new ImageIcon("guy0"+i+k+".png").getImage();
			}
		}
		for (int i=0;i<8;i++){
			for (int k=0;k<8;k++){
				zombieSprites[i][k]=new ImageIcon("zombie"+i+k+".png").getImage();
			}
		}
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
	public void checkPause(){
		//display another pause screen
		if (keys[KeyEvent.VK_P]){
			BH.state=BH.PAUSE;
		}
	}
	public void checkUnPause(){
		if (keys[KeyEvent.VK_P]){
			BH.state=BH.GAME;
		}
	}
	public void moveMC(){
		int speed = BH.mc.getspeed();
		//arent we moving the character around a section of the map
		//so our boundary thing rn doesnt work LOL
		//how do you do this HELP
		//SHOOTING OF BULLETS IS ALSO WEIRD LOL
		if (keys[KeyEvent.VK_LEFT] && keys[KeyEvent.VK_UP]){
			BH.mc.setAngle(225);
			BH.mc.setX(Math.max(BH.mc.getX()-speed/2*Math.sqrt(2),0)); //MOVE DIAGONALLY LEFT & UP
			BH.mc.setY(Math.max(BH.mc.getY()-speed/2*Math.sqrt(2),0)); //MOVE DIAGONALLY LEFT & UP
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_LEFT] && keys[KeyEvent.VK_DOWN]){
			BH.mc.setAngle(135);
			BH.mc.setX(Math.max(BH.mc.getX()-speed/2*Math.sqrt(2),0)); 
			BH.mc.setY(Math.min(BH.mc.getY()+speed/2*Math.sqrt(2),640));
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_RIGHT] && keys[KeyEvent.VK_DOWN]){
			BH.mc.setAngle(45);
			BH.mc.setX(Math.min(BH.mc.getX()+speed/2*Math.sqrt(2),800)); 
			BH.mc.setY(Math.min(BH.mc.getY()+speed/2*Math.sqrt(2),640)); 
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_RIGHT] && keys[KeyEvent.VK_UP]){
			BH.mc.setAngle(315);
			BH.mc.setX(Math.min(BH.mc.getX()+speed/2*Math.sqrt(2),800)); 
			BH.mc.setY(Math.max(BH.mc.getY()-speed/2*Math.sqrt(2),0)); 
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_LEFT]){
			//maybe set the direction lol
			BH.mc.setAngle(180);
			BH.mc.setX(Math.max(0, BH.mc.getX()-speed));
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_RIGHT]){
			BH.mc.setX(Math.min(800-BH.mc.getWidth(), BH.mc.getX()+speed));
			BH.mc.setAngle(0);
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_UP]){
			BH.mc.setY(Math.max(0, BH.mc.getY()-speed));
			BH.mc.setAngle(270);
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_DOWN]){
			BH.mc.setY(Math.min(640-BH.mc.getLength(),BH.mc.getY()+speed));
			BH.mc.setAngle(90);
			spriteCounter++;
		}
	}
	public void moveEnemy(){
		for (int i=0; i< BH.allZombies.size(); i++){
			Zombie temp = BH.allZombies.get(i);
			double ManhatX = Math.abs(temp.getX() - BH.mc.getX()), ManhatY = Math.abs(temp.getY() - BH.mc.getY()), speed = temp.getspeed();
			double moveX = ManhatX/(ManhatX+ManhatY)*speed, moveY = ManhatY/(ManhatX+ManhatY)*speed;
			if (temp.getX() <= BH.mc.getX()){
				temp.setX(temp.getX()+moveX);
			}
			else{
				temp.setX(temp.getX()-moveX);
			}
			if (temp.getY() <= BH.mc.getY()){
				temp.setY(temp.getY()+moveY);
			}
			else{
				temp.setY(temp.getY()-moveY);
			}
			
		}
	}
	
	
	public boolean enemyCollision(int x, int y){
		ArrayList<Zombie> toRemoveZ = new ArrayList<Zombie>();
		boolean flag = false;
		for (int i=0; i<BH.allZombies.size(); i++){
			if (BH.allZombies.get(i).getCollide(x,y)){
				flag = true;
				System.out.println("COLLIDE");
				BH.allZombies.get(i).setHealth(BH.allZombies.get(i).getHealth() - 10);
			}
			if (BH.allZombies.get(i).getHealth() <= 0){
				toRemoveZ.add(BH.allZombies.get(i));
			}
		}

		for (Zombie zzh8829:toRemoveZ){
			BH.score+=200;
			BH.allZombies.remove(zzh8829);
		}
		ArrayList<Devil> toRemoveD = new ArrayList<Devil>();
		for (int i=0;i<BH.allDevils.size();i++){
			if (BH.allDevils.get(i).getCollide(x,y)){
				flag = true;
				System.out.println("COLLIDE DEVILS");
				BH.allDevils.get(i).setHealth(BH.allDevils.get(i).getHealth() - 10);
			}
			if (BH.allDevils.get(i).getHealth() <= 0){
				toRemoveD.add(BH.allDevils.get(i));
			}
		}
		for (Devil zzh8829:toRemoveD){
			BH.score+=200;
			BH.allDevils.remove(zzh8829);
		}
		//remember to check the devils to
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
				//REMOVE THE ENEMIES
				toRemove.add(BH.activeBullets.get(i));
			}
			else if (checkOutside(BH.activeBullets.get(i).getX(),BH.activeBullets.get(i).getY())){
				toRemove.add(BH.activeBullets.get(i));
			}
		}
		for (PosPair pair:toRemove){
			BH.activeBullets.remove(pair);
		}
	}
	public void addZombieCounter(){
		for (int i=0;i<BH.allZombies.size();i++){
			BH.allZombies.get(i).addToCounter();
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
		g.drawRect(BH.mc.getX()-5,BH.mc.getY()-3,calculateHealth(),5); //filling of the bar
		g.drawRect(BH.mc.getX()-5, BH.mc.getY()-3, 20, 5); //drawing the outline
		
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
			System.out.println(a.getX() + " " + a.getY());
		}
		
		g.setColor(new Color (255,0,0));
		g.drawImage(charSprites[BH.mc.getANGLE()/45][spriteCounter%3],BH.mc.getX(),BH.mc.getY(),this);
		for (int i=0;i<BH.allZombies.size();i++){
			Zombie a = BH.allZombies.get(i);
			g.drawImage(zombieSprites[a.getANG()/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		//g.drawRect(BH.mc.getX(),BH.mc.getY(),BH.mc.getsx(),BH.mc.getsy());
	}
}
