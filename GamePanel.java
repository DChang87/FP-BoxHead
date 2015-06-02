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
	private Image background = new ImageIcon("map.jpg").getImage();
	private Image boxSprite = new ImageIcon("box.png").getImage();
	private Image[][] charSprites = new Image[8][3];
	private Image[][] zombieSprites = new Image[8][8];
	private int spriteCounter = 0;
	private int[] weapondist = new int[30];
	private int rectsx = 30, rectsy = 70;
	
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
		updateweapon();
	}
	public void updateweapon(){
		//we keep track of how far the weapon can travel
		weapondist[1] = 400;
		weapondist[2] = 500;
		
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
		return (fx+10>=mcx && fx+10 <=mcx+BH.mc.getWidth() && fy+10>=mcy && fy+10<=mcy+BH.mc.getLength());
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
			//System.out.println("CHECKPAUSE");
			BH.state=BH.PAUSE;
			setFocusable(false);
    		BH.sm.requestFocus();
    		keys[KeyEvent.VK_P]=false;
		}
	}
	public void moveMC(){
		int speed = BH.mc.getspeed();
		//arent we moving the character around a section of the map
		//so our boundary thing rn doesnt work LOL
		//how do you do this HELP IDKKKK RIGHT NOW
		//SHOOTING OF BULLETS IS ALSO WEIRD LOL
		
		double nx=BH.mc.getX(), ny=BH.mc.getY();
		
		
		if (keys[KeyEvent.VK_LEFT] && keys[KeyEvent.VK_UP]){
			BH.mc.setAngle(225);
			nx = Math.max(BH.mc.getX()-speed/2*Math.sqrt(2),0);
			ny = Math.max(BH.mc.getY()-speed/2*Math.sqrt(2),0);
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_LEFT] && keys[KeyEvent.VK_DOWN]){
			BH.mc.setAngle(135);
			nx = Math.max(BH.mc.getX()-speed/2*Math.sqrt(2),0); 
			ny = Math.min(BH.mc.getY()+speed/2*Math.sqrt(2),640);
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_RIGHT] && keys[KeyEvent.VK_DOWN]){
			BH.mc.setAngle(45);
			nx = Math.min(BH.mc.getX()+speed/2*Math.sqrt(2),800); 
			ny = Math.min(BH.mc.getY()+speed/2*Math.sqrt(2),640); 
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_RIGHT] && keys[KeyEvent.VK_UP]){
			BH.mc.setAngle(315);
			nx = Math.min(BH.mc.getX()+speed/2*Math.sqrt(2),800); 
			ny = Math.max(BH.mc.getY()-speed/2*Math.sqrt(2),0); 
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_LEFT]){
			//maybe set the direction lol
			BH.mc.setAngle(180);
			nx = Math.max(0, BH.mc.getX()-speed);
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_RIGHT]){
			nx = Math.min(800-BH.mc.getWidth(), BH.mc.getX()+speed);
			BH.mc.setAngle(0);
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_UP]){
			ny = Math.max(0, BH.mc.getY()-speed);
			BH.mc.setAngle(270);
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_DOWN]){
			ny = Math.min(640-BH.mc.getLength(),BH.mc.getY()+speed);
			BH.mc.setAngle(90);
			spriteCounter++;
		}
		if (numbercollisions((int)nx, BH.mc.getY()) <= 1){
			BH.mc.setX(nx);
		}
		if (numbercollisions(BH.mc.getX(), (int)ny) <= 1){
			BH.mc.setY(ny);
		}
	}
	public void moveZombie(){
		for (int i=0; i< BH.allZombies.size(); i++){
			Zombie temp = BH.allZombies.get(i);
			double ManhatX = Math.abs(temp.getDX() - BH.mc.getDX()), ManhatY = Math.abs(temp.getDY() - BH.mc.getDY()), speed = temp.getspeed();
			double moveX = ManhatX/(ManhatX+ManhatY)*speed, moveY = ManhatY/(ManhatX+ManhatY)*speed,		nx, ny;
			temp.setAngle(Math.toDegrees(3.14159265358+Math.atan2(temp.getDY() - BH.mc.getDY(),temp.getDX() - BH.mc.getDX())));
			if (temp.getDX() <= BH.mc.getDX()){
				nx = temp.getDX()+moveX;
			}
			else{
				nx = temp.getDX()-moveX;
			}
			if (temp.getDY() <= BH.mc.getDY()){
				ny = temp.getY()+moveY;
			}
			else{
				ny = temp.getY()-moveY;
			}
			if (numbercollisions((int)nx, temp.getY()) <= 1){
				temp.setX(nx);
			}
			if (numbercollisions(temp.getX(), (int)ny) <= 1){
				temp.setY(ny);
			}
		}
	}
	public void moveDevil(){
		for (int i=0; i< BH.allDevils.size(); i++){
			Devil temp = BH.allDevils.get(i);
			double ManhatX = Math.abs(temp.getDX() - BH.mc.getDX()), ManhatY = Math.abs(temp.getDY() - BH.mc.getDY()), speed = temp.getspeed();
			double moveX = ManhatX/(ManhatX+ManhatY)*speed, moveY = ManhatY/(ManhatX+ManhatY)*speed,		nx, ny;
			temp.setAngle(Math.toDegrees(Math.PI+Math.atan2(temp.getDY() - BH.mc.getDY(),temp.getDX() - BH.mc.getDX())));
			temp.addTime(BH);
			if (temp.getDX() <= BH.mc.getDX()){
				nx = temp.getDX()+moveX;
			}
			else{
				nx = temp.getDX()-moveX;
			}
			if (temp.getDY() <= BH.mc.getDY()){
				ny = temp.getY()+moveY;
			}
			else{
				ny = temp.getY()-moveY;
			}
			if (numbercollisions((int)nx, temp.getY()) <= 1){
				temp.setX(nx);
			}
			if (numbercollisions(temp.getX(), (int)ny) <= 1){
				temp.setY(ny);
			}
		}
		
	}
	public int numbercollisions(int x, int y){
		//collisions should max 1 (itself)
		int ncollision = 0;
		if (rectcollision(x,y,BH.mc.getX(),BH.mc.getY())){
			ncollision++;
		}
		for (Zombie z : BH.allZombies){
			if (rectcollision(x,y,z.getX(),z.getY())){
				ncollision++;
			}
		}
		for (Devil d : BH.allDevils){
			if (rectcollision(x,y,d.getX(),d.getY())){
				ncollision++;
			}
		}
		return ncollision;
	}
	public boolean rectcollision(int x1, int y1, int x2, int y2){
		if (x1+rectsx < x2 || x1 > x2 + rectsx || y1 + rectsy < y2 || y1 > y2 + rectsy){
			return false;
		}
		return true;
	}
	
	public boolean enemyCollision(int x, int y){
		//this method is called to see if the character's bullets damage the enemies
		ArrayList<Zombie> toRemoveZ = new ArrayList<Zombie>();
		boolean flag = false;
		for (int i=0; i<BH.allZombies.size(); i++){
			if (BH.allZombies.get(i).getCollide(x,y)){
				flag = true;
				BH.allZombies.get(i).setHealth(BH.allZombies.get(i).getHealth() - 10);
				System.out.println("HIT");
			}
			if (BH.allZombies.get(i).getHealth() <= 0){
				System.out.println("DIE");
				toRemoveZ.add(BH.allZombies.get(i));
			}
		}

		for (Zombie zzh8829:toRemoveZ){
			//for every zombie that dies, add onto the score, remove the zombie and add a magical box to the screen
			BH.allBoxes.add(new MagicalBox(zzh8829.getX(),zzh8829.getY()));
			System.out.println("HI"+BH.allBoxes.size());
			BH.score+=200;
			BH.allZombies.remove(zzh8829);
		}
		ArrayList<Devil> toRemoveD = new ArrayList<Devil>();
		for (int i=0;i<BH.allDevils.size();i++){
			if (BH.allDevils.get(i).getCollide(x,y)){
				flag = true;
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
	public boolean checkOutside(int x,int y){
		return x<0||x>800||y<0||y>640;
	}
	public void checkBulletCollision(){
		//check if the bullets hit any enemies
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
	public void checkBulletDistance(){
		//checks how far the bullet travels and if it needs to be removed
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (PosPair a: BH.activeBullets){
			if ((a.getX()-a.getorigX())*(a.getX()-a.getorigX()) + (a.getY()-a.getorigY())*(a.getY()-a.getorigY()) > weapondist[a.getTYPE()]*weapondist[a.getTYPE()]){
				toRemove.add(a);	
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
			final double ANG = Math.toRadians(temp.getANGLE());
			double xx = temp.getDX(), yy = temp.getDY();
			BH.fireballs.get(i).setPos(xx+20*Math.cos(ANG),yy+20*Math.sin(ANG));
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
	public void checkMC(){
		for (Zombie a:BH.allZombies)
		{
			if (a.collideMC())
			{
				BH.mc.setHealth(BH.mc.getHealth()-10);
			}
		}
		for (Devil a:BH.allDevils)
		{
			if (a.collideMC())
			{
				BH.mc.setHealth(BH.mc.getHealth()-10);
			}
		}
	}
	
	public void paintComponent(Graphics g){
		Font Sfont = new Font("Calisto MT", Font.BOLD, 20);
		g.setFont(Sfont);
		g.drawImage(background, 0, 0, this);
		g.drawString(BH.mc.getName(), BH.mc.getX()-5, BH.mc.getY()-10); //maybe do the string formatting with this later if we have time
		//drawing the health bar
		//figure out the colouring of the bar ugh
		g.drawRect(BH.mc.getX()-5,BH.mc.getY()-3,BH.mc.calculateHealth(),5); //filling of the bar
		g.drawRect(BH.mc.getX()-5, BH.mc.getY()-3, 20, 5); //drawing the outline
		
		for (int i=0;i<BH.activeBullets.size();i++){
			//we need to get bulllet sprites
			///g.drawImage(BH.bulletSprites.get(BH.activeBullets.get(i).getTYPE()),BH.activeBullets.get(i).getX(),BH.activeBullets.get(i).getY(),this);
			g.drawOval(BH.activeBullets.get(i).getX(), BH.activeBullets.get(i).getY(), 20, 20);
			//we need to move the bullets...either call a function here or call it from the BoxHead class
		}
		for (int i=0;i<BH.fireballs.size();i++){
			g.drawOval(BH.fireballs.get(i).getX(), BH.fireballs.get(i).getY(), 20, 20);
		}
		for (int i=0;i<BH.allZombies.size();i++){
			Zombie a = BH.allZombies.get(i);
			g.drawRect(a.getX(),a.getY(),a.getsx(), a.getsy());
		}
		for (int i=0;i<BH.allDevils.size();i++){
			Devil a = BH.allDevils.get(i);
			g.drawRect(a.getX(),a.getY(),a.getsx(), a.getsy());
		}
		for (int i=0;i<BH.allBoxes.size();i++){
			MagicalBox b = BH.allBoxes.get(i);
			g.drawImage(boxSprite,b.getX(),b.getY(),this);
		}
		g.setColor(new Color (255,0,0));
		g.drawImage(charSprites[BH.mc.getANGLE()/45][spriteCounter%3],BH.mc.getX(),BH.mc.getY(),this);
		for (int i=0;i<BH.allZombies.size();i++){
			Zombie a = BH.allZombies.get(i);
			g.drawImage(zombieSprites[(a.getAngle()+22)%360/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		for (Devil a : BH.allDevils){
			g.drawImage(zombieSprites[(a.getAngle()+22)%360/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		//g.drawRect(BH.mc.getX(),BH.mc.getY(),BH.mc.getsx(),BH.mc.getsy());
	}
}
