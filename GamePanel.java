import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class GamePanel extends JPanel implements KeyListener{
	private BoxHead BH;
	private boolean[] keys; 
	
	//Audio
	private AudioClip barrelExplodeAudio = Applet.newAudioClip(getClass().getResource("barrelExplosionAudio.wav")); 
	private AudioClip[] audio = new AudioClip[8];
	private AudioClip explodeGrenade =Applet.newAudioClip(getClass().getResource("grenadeExplode.wav"));
	
	//sprites
	private Image[][] charSprites = new Image[8][3];
	private Image[][] zombieSprites = new Image[8][8];
	private Image[][] devilSprites = new Image[8][8];
	private Image boxSprite = new ImageIcon("sprites/box.png").getImage();
	private Image[] fireballSprites = new Image[8];
	private Image[] bulletSprites = new Image[8];
	private Image grenadeSprite = new ImageIcon("sprites/grenade.png").getImage();
	private Image grenadeExploded = new ImageIcon("sprites/grenadeExplode.png").getImage();
	private Image barrelSprite = new ImageIcon("sprites/barrel.png").getImage();
	private Image barrelExplosion = new ImageIcon("sprites/barrelExplosion.png").getImage();
	private Image barricadeSprite = new ImageIcon("sprites/barricade.png").getImage();
	
	//backgrounds
	private BufferedImage mask_background;
	private Image background = new ImageIcon("sprites/forestmap2.jpg").getImage();
	
	//constants for the weapons/health
	private final int HEALTH=0,PISTOL=1,UZI=2,SHOTGUN=3,BARREL = 4,GRENADE= 5, BARRICADE=6,SENTRY=7; 
	
	//sizes of different objects/screens
	private int rectsx = 34, rectsy = 47; //size of all characters on screen (zombies/devils/MC)
	private int barricadesx = 60, barricadesy = 48;
	private int barrelsx = 27, barrelsy = 43;
	private int sentrysx = 20, sentrysy = 20;
	private int screensx = 800, screensy = 640; //screensize
	private int mapx=0, mapy=0, mapsx = 2000, mapsy = 2000; //the coordinates and size of the original map
	private int bx1 = 100, bx2 = 670, by1 = 100, by2 = 510; //the map boundary on the screen before the map is shifted
	private int shiftx = 0, shifty = 0; //The distance the map shifted
	
	
	//all objects on the map at any given time
	private ArrayList<Zombie> allZombies = new ArrayList<Zombie>();
	private ArrayList<Devil> allDevils = new ArrayList<Devil>();
	public ArrayList<PosPair> fireballs = new ArrayList<PosPair>(); //all fireballs in air
	private ArrayList<PosPair> activeBullets = new ArrayList<PosPair>(); //all bullets in air 
	private ArrayList<MagicalBox> allBoxes = new ArrayList<MagicalBox>();
	private ArrayList<Barricade> allBarricades = new ArrayList<Barricade>();
	private ArrayList<Barrel> allBarrels = new ArrayList<Barrel>();
	private ArrayList<Explosion> allExplosions = new ArrayList<Explosion>(); //all explosions taking place at the time (to blit the sprites)
	private ArrayList<SentryGun> allSentries = new ArrayList<SentryGun>();
	private ArrayList<Grenade> allGrenades = new ArrayList<Grenade>();
	private ArrayList<Grenade> explodedGrenade = new ArrayList<Grenade>();
	
	//consecutive kills + upgrades (consecutive kills lead to the upgrades)
	private int consecutiveKills=0; //the number of consecutive kills at any point of time
	private int consecutiveCountDown=0; //the counter in which after the countdown reaches 0, the consecutiveKills is reduced by 1
	//and countdown at full again so it can continue to reduce consecutiveKills
	private int nextUpgrade=0;//the position in the BH.ug.allUpgradesNum in which the next upgrade for the character will be
	private String printUpgradeString=""; //the name of the upgrade to display
	private int UpgradeStringCountDown=0; //countdown to display the string. when countdown is at 0, string is set at ""
	
	//box item name blitting
	private String boxString = ""; //the string to blit when the character obtains a box
	private int boxCountDown=0; //count down to blit the box
	
	//regulated shooting
	private int shootCountDown=0; //countdown to shoot bullets to ensure that the bullets are not shot consecutively like a machine gun
	//the character can only shoot when the countdown is at 0. once a bullet is shot, the counter is set back to full
	
	//font loading
	private Font SMALLfont = new Font("Impact",Font.PLAIN,15);
	private Font font = new Font("Impact", Font.PLAIN, 40);
	private Font LARGEfont = new Font("Impact", Font.PLAIN, 100);
	
	//counters
	private int spriteCounter = 0; //sprite counter for the MC so movement of MC is blitted properly
	private int displayLevelCounter=0; //this is the counter used to display the "+-+-+-+ Level 2 +-+-+-+"
	
	//other variables
	private boolean lastSpaceStat=false; //the status of space being pressed in the gampanel before the current one
	private String[] weaponNames = new String[8]; //stores the names of all the weapons
	private int ZombiesDead=0,DevilsDead=0; //the number of zombies dead at any point, accumulated for the current level
	private int ZombiesThisLevel, DevilsThisLevel; //the number of zombies/devils remaining in the level (alive, does not have to be spawned)
	private int currentLevel;	
	private int spawnsp = 2000; //spawn speed
	
	public GamePanel(BoxHead bh){
		BH=bh;
		keys = new boolean[65535];
		addKeyListener(this);
		getWeaponNames();	
		
		loadAudio();
		loadSprites();
		loadMask();
		currentLevel=1;
		ZombiesThisLevel = getZombiesThisLevel();
		DevilsThisLevel = getDevilsThisLevel();
		generateEnemy();
	}
	////////////////SETUP STUFF BEGIN///////////////
	public void loadMask(){
		//Load the mask needed for collision for the map
		try{
			File file= new File("mask_map3.jpg");
			mask_background = ImageIO.read(file);
		}
		catch (IOException ex){}
	}
	public void loadSprites(){
		for (int i=0;i<8;i++){
			for (int k=0;k<3;k++){
				charSprites[i][k]=new ImageIcon("sprites/guy0"+i+k+".png").getImage();
			}
		}
		for (int i=0;i<8;i++){
			for (int k=0;k<8;k++){
				zombieSprites[i][k]=new ImageIcon("sprites/zombie"+i+k+".png").getImage();
				devilSprites[i][k]=new ImageIcon("sprites/devil"+i+k+".png").getImage();
			}
		}
		for (int i=0;i<8;i++){
			bulletSprites[i]=new ImageIcon("sprites/pistol"+i+".png").getImage();
			fireballSprites[i]=new ImageIcon("sprites/fireball"+i+".png").getImage();
		}
		
	}
	public void loadAudio(){
		//load the audio sound effects
		audio[PISTOL]=Applet.newAudioClip(getClass().getResource("pistol.wav"));
		audio[UZI]=Applet.newAudioClip(getClass().getResource("uzi.wav"));
		audio[SHOTGUN]=Applet.newAudioClip(getClass().getResource("shotgun.wav"));
		audio[SENTRY]=Applet.newAudioClip(getClass().getResource("sentrygun_qq.wav"));
		audio[GRENADE] = Applet.newAudioClip(getClass().getResource("grenadethrow.wav"));
	}
	public void getWeaponNames(){
		//load the weapon names
		weaponNames[PISTOL]="PISTOL";
		weaponNames[UZI]="UZI";
		weaponNames[SHOTGUN]="SHOTGUN";
		weaponNames[BARREL]="BARREL";
		weaponNames[GRENADE]="GRENADE";
		weaponNames[BARRICADE]="BARRICADE";
		weaponNames[SENTRY]="SENTRY";
	}
	public void fullUpgradeCountDown(){
		//set the upgrade string count down at full so it can count down while printing the string to screen
		UpgradeStringCountDown=100;
	}
	public void UpgradeCountDown(){
		//count down on the counter if it is not zero
		//if it is zero then set printUpgradeString to "" (empty string) so nothing will blit on screeen
		if (UpgradeStringCountDown!=0){
			UpgradeStringCountDown--;
		}
		else{
			printUpgradeString="";
		}
		
	}
	public void setUpgradeString(String n){
		//set the printUpgradeString to blit on screen
		printUpgradeString=n;
	}
	public void setBoxString(String n){
		//set the boxString to blit on screen
		//set the boxCountDown on full
		//when count down is at 0, the boxString stops appearing on the screen
		boxString=n;
		boxCountDown=100;
	}
	
	//////////////methods for key/////////////
	public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){keys[e.getKeyCode()]=true;}
    public void keyReleased(KeyEvent e){keys[e.getKeyCode()]=false;}
    ////////////////////////////////////
    
    public void addNotify(){
    	super.addNotify();
    	requestFocus();
    	BH.start();
    }
	public void activateAudio(int weapon){
		//play the audio of the weapon given
		audio[weapon].play();
	}
	public int getNextUpgrade(){
		//getter for nextUpgrade
		return nextUpgrade;
	}
	public void checkPause(){
		//display another pause screen if the player presses P/p
		if (keys[KeyEvent.VK_P]){
			BH.state=BH.PAUSE;
    		BH.sm.requestFocus();
    		keys[KeyEvent.VK_P]=false; //set to false for future use
		}
	}
	/////////////SETUP STUFF END////////////////
	
	//MAIN CHARACTER BEGIN
	public void moveMC(){
		///move the character based on its speed and direction (angle)
		//yay for trigonometry 
		//add on to the sprite counter so the right sprite can blit on the screen
		int speed = BH.mc.getspeed();		
		double nx=BH.mc.getX(), ny=BH.mc.getY();
		//new x position, 		new y position
		
		///moving diagonally
		if (keys[KeyEvent.VK_LEFT] && keys[KeyEvent.VK_UP]){
			BH.mc.setAngle(225);
			nx = Math.max(BH.mc.getX()-speed/2*Math.sqrt(2),0);
			ny = Math.max(BH.mc.getY()-speed/2*Math.sqrt(2),0);
			//Doesn't go off screen
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_LEFT] && keys[KeyEvent.VK_DOWN]){
			BH.mc.setAngle(135);
			nx = Math.max(BH.mc.getX()-speed/2*Math.sqrt(2),0); 
			ny = Math.min(BH.mc.getY()+speed/2*Math.sqrt(2),640);
			//Doesn't go off screen
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_RIGHT] && keys[KeyEvent.VK_DOWN]){
			BH.mc.setAngle(45);
			nx = Math.min(BH.mc.getX()+speed/2*Math.sqrt(2),800); 
			ny = Math.min(BH.mc.getY()+speed/2*Math.sqrt(2),640); 
			//Doesn't go off screen
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_RIGHT] && keys[KeyEvent.VK_UP]){
			BH.mc.setAngle(315);
			nx = Math.min(BH.mc.getX()+speed/2*Math.sqrt(2),800); 
			ny = Math.max(BH.mc.getY()-speed/2*Math.sqrt(2),0); 
			//Doesn't go off screen
			spriteCounter++;
		}
		//moving up down left right (not diagonally)
		else if (keys[KeyEvent.VK_LEFT]){
			BH.mc.setAngle(180);
			nx = Math.max(0, BH.mc.getX()-speed);
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_RIGHT]){
			nx = Math.min(800-BH.mc.getWidth(), BH.mc.getX()+speed);
			//Doesn't go off screen
			BH.mc.setAngle(0);
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_UP]){
			ny = Math.max(0, BH.mc.getY()-speed);
			//Doesn't go off screen
			BH.mc.setAngle(270);
			spriteCounter++;
		}
		else if (keys[KeyEvent.VK_DOWN]){
			ny = Math.min(640-BH.mc.getLength(),BH.mc.getY()+speed);
			//Doesn't go off screen
			BH.mc.setAngle(90);
			spriteCounter++;
		}
		int inx = (int)nx, iny = (int)ny;
		//		If position is valid in the mask
		if (validMove(inx,BH.mc.getY()) && numbercollisions(inx,BH.mc.getY()) <= 1){
			BH.mc.setX(nx);
		}//note : co-ordinates are stored as doubles, allows mini movements
		if (validMove(BH.mc.getX(),iny) && numbercollisions(BH.mc.getX(),iny) <= 1){
			BH.mc.setY(ny);
		}
	}
	public void MCshoot(){ //Main character doing action
		if (keys[KeyEvent.VK_SPACE]){
			//shoot the weapons based on the angle and how far they are supposed to be shot to (at)
			//yay for trigs again
			
			int mx = BH.mc.getcx(), my = BH.mc.getcy();//get center position
			//if the user shoots, add a bullet into the arraylist keeping track of flying bullets
			
			//check if the weapon can be consecutively dropped:
			//if so, check if the counter is at 0 so the weapon can be fired, if not, it must wait until the counter is at 0
			//if it shoots, it sets the shoot countdown to full
			//if not, check if the last status of space bar was at false
			
			///consecutively shooting/dropping something:
			//holding space bar down and the bullet will shoot at a regular interval

			//non consecutive shooting/dropping
			//must press space bar multiple times to drop/shoot multiple times
			
			//reduce the ammo everytime a weapon is used
			//add the weapon to its arraylist so it can be blit and moved (if needed) properly
			//ex. add the barricade to allBarricades
			if (BH.mc.getWeapon()==BARRICADE){
				//barricade cannot be consecutively dropped
				if (!lastSpaceStat){
					int nx = (int) (mx + 45*Math.cos(Math.toRadians(BH.mc.getANGLE())));
					int ny = (int) (my + 45*Math.sin(Math.toRadians(BH.mc.getANGLE())));
					if(!objectPlacementCollision(nx-barricadesx/2,ny-barricadesy/2,barricadesx,barricadesy)){
						//If the spot is empty
						allBarricades.add(new Barricade(nx-barricadesx/2,ny-barricadesy/2));
						BH.mc.useAmmo(BARRICADE);
					}		
				}
			}
			else if (BH.mc.getWeapon()==BARREL){
				//barrel cannot be consecutively dropped
				if (!lastSpaceStat){
					int nx = (int) (mx + 30*Math.cos(Math.toRadians(BH.mc.getANGLE())));
					int ny = (int) (my + 55*Math.sin(Math.toRadians(BH.mc.getANGLE())));
					if(!objectPlacementCollision(nx-barrelsx/2,ny-barrelsy/2,barrelsx,barrelsy)){
						//If the spot is empty
						allBarrels.add(new Barrel(nx - barrelsx/2, ny - barrelsy/2));//If the place is empty
						BH.mc.useAmmo(BARREL);
					}	
				}
			}
			else if (BH.mc.getWeapon()==GRENADE){
				//grenade cannot be consecutively dropped
				if (!lastSpaceStat){
					int nx = (int) (mx + 60*Math.cos(Math.toRadians(BH.mc.getANGLE())));
					int ny = (int) (my + 60*Math.sin(Math.toRadians(BH.mc.getANGLE())));
					allGrenades.add(new Grenade(nx,ny,BH));
					BH.mc.useAmmo(GRENADE);
					activateAudio(GRENADE); //play the sound to drop a grenade
				}
			}
			else if (BH.mc.getWeapon()==SENTRY){
				//sentry cannot be consecutively dropped
				if (!lastSpaceStat){
					int nx = (int) (mx + 37*Math.cos(Math.toRadians(BH.mc.getANGLE())));
					int ny = (int) (my + 37*Math.sin(Math.toRadians(BH.mc.getANGLE())));
					if(!objectPlacementCollision(nx-sentrysx/2,ny-sentrysy/2,sentrysx,sentrysy)){
						//If the spot is empty
						allSentries.add(new SentryGun(nx-sentrysx/2,ny-sentrysy/2));//Man's best friend
						BH.mc.useAmmo(SENTRY);
					}
				}
			}
			else if (BH.mc.getWeapon()==SHOTGUN){
				if ((BH.mc.getConsecutiveShoot(SHOTGUN) &&shootCountDown==0)|| lastSpaceStat==false){
					//if it can shoot consecutively. if not, it cannot shoot consecutively and the last space stat must be false
					//check how wide the shotgun is to determine how far the bullets are apart
					if (BH.mc.getSGW()==0){
						activeBullets.add(new PosPair(mx,my,BH.mc.getANGLE(),BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()+5)%360,BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()-5+360)%360,BH.mc.getWeapon()));
					}
					else if (BH.mc.getSGW()==1){
						//wide shot
						activeBullets.add(new PosPair(mx,my,BH.mc.getANGLE(),BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()+10)%360,BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()-10+360)%360,BH.mc.getWeapon()));
					}
					else{
						//wider shot
						activeBullets.add(new PosPair(mx,my,BH.mc.getANGLE(),BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()+20)%360,BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()-20+360)%360,BH.mc.getWeapon()));
					}
					shootCountDown=15;
					activateAudio(BH.mc.getWeapon()); //play the sound effect
					BH.mc.useAmmo(BH.mc.getWeapon());
				}
			}
			else if (BH.mc.getWeapon()==PISTOL){
				//If it can be shot consecutively otherwise last space stat must be false
				if ((BH.mc.getConsecutiveShoot(PISTOL)&&shootCountDown==0)|| lastSpaceStat==false){
					activeBullets.add(new PosPair(mx,my,BH.mc.getANGLE(),BH.mc.getWeapon()));
					BH.mc.useAmmo(BH.mc.getWeapon());
					activateAudio(BH.mc.getWeapon());//play the sound effect
					shootCountDown=15;
				}
			}
			else{
				if (shootCountDown==0){
					shootCountDown=15;
					activeBullets.add(new PosPair(mx,my,BH.mc.getANGLE(),BH.mc.getWeapon()));
					BH.mc.useAmmo(BH.mc.getWeapon());
					activateAudio(BH.mc.getWeapon());//play the sound effect
				}
				
			}
			if (shootCountDown>0){
				//if shoot count down is not at 0, then reduce it
				shootCountDown--;
			}
		}
	}
	public void checkMC(){
		//Check if MC is getting beat up
		//check if it dies
		for (Zombie a:allZombies){
			if (a.collideMC()){
				BH.mc.setHealth(Math.max(BH.mc.getHealth()-a.getdmg(),0));
			}
		}
		for (Devil a:allDevils){
			if (a.collideMC()){
				BH.mc.setHealth(Math.max(BH.mc.getHealth()-a.getdmg(),0));
			}
		}
		checkDeath();
	}
	public void fullCountDown(){consecutiveCountDown=225;} //set counter to full
	public void CountDown(){
		//this counts down on the consecutive kills counter
		if (consecutiveCountDown>0){
			//reduce counter if it is greater than 0
			consecutiveCountDown--;
			if (consecutiveCountDown==0 && consecutiveKills>0){
				//reduce consecutive kills if there are still any after counter is at 0
				consecutiveKills--;
				if (consecutiveKills>0){
					// if there are still consecutive killss left, set couner to full
					fullCountDown();
				}
			}
		}
	}
	public void addConsecutive(){
		//this adds on to the consecutive kill
		//this checks if it is time to upgrade (based on the number of consecutive kills and the next upgrade)
		consecutiveKills++;
		if (nextUpgrade==26)
			return;
		if (consecutiveKills==BH.ug.allUpgradesNum[nextUpgrade]){
			BH.ug.getUpgrade(BH.ug.allUpgradesNum[nextUpgrade++]);
		}
	}
	public void switchWeapon(){
		//switches the weapon based on the number key pressed
		if (keys[KeyEvent.VK_1]){
			BH.mc.setWeapon(1);
		}
		else if (keys[KeyEvent.VK_2]){
			if (BH.mc.getAmmo(2)>0){
				BH.mc.setWeapon(2);
			}
		}
		else if (keys[KeyEvent.VK_3]){
			if (BH.mc.getAmmo(3)>0){
				BH.mc.setWeapon(3);
			}
		}
		else if (keys[KeyEvent.VK_4]){
			if (BH.mc.getAmmo(4)>0){
				BH.mc.setWeapon(4);
			}
		}
		else if (keys[KeyEvent.VK_5]){
			if (BH.mc.getAmmo(5)>0){
				BH.mc.setWeapon(5);
			}
		}
		else if (keys[KeyEvent.VK_6]){
			if (BH.mc.getAmmo(6)>0){
				BH.mc.setWeapon(6);
			}
		}
		else if (keys[KeyEvent.VK_7]){
			if (BH.mc.getAmmo(7)>0){
				BH.mc.setWeapon(7);
			}
		}
	}
	//MAIN CHARACTER END
	//GENERAL FUNCTIONS START
	public double dist(int x1, int y1,int x2,int y2){
		//returns the distance between two points
		return Math.pow(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2),0.5);
	}
	public boolean validMove(int x, int y){
		//check if the pixel is valid in the mask
		if (mapx+x >=2000 || mapy+y >= 2000){
			return false;
		}
		if (mapx+x>=0&& mapy+y>=0){
			//only return true if pixel is on the right colour
			int clr=  mask_background.getRGB(mapx+x,mapy+y);
			int  red   = (clr & 0x00ff0000) >> 16;
			int  green = (clr & 0x0000ff00) >> 8;
			int  blue  =  clr & 0x000000ff;
			if (red == 255 && green == 255 && blue == 255){
				return true;
			}
		}
		return false;
	}
	public int numbercollisions(int x, int y){
		//If pixel does not collide with any rects on screen
		//collisions should max 1 (itself)
		int ncollision = 0;
		if (rectcollision(x,y,rectsx,rectsy,BH.mc.getX(),BH.mc.getY(),rectsx,rectsy)){
			ncollision++;
		}
		for (Zombie z : allZombies){
			if (rectcollision(x,y,rectsx,rectsy,z.getX(),z.getY(),rectsx,rectsy)){
				ncollision++;
			}
		}
		for (Devil d : allDevils){
			if (rectcollision(x,y,rectsx,rectsy,d.getX(),d.getY(),rectsx,rectsy)){
				ncollision++;
			}
		}
		for (Barricade b : allBarricades){
			if (b.rectcollision(x,y)){
				ncollision++;
			}
		}
		for (SentryGun b : allSentries){
			if (b.rectcollision(x,y)){
				ncollision++;
			}
		}
		for (Barrel b : allBarrels){
			if (b.rectcollision(x,y)){
				ncollision++;
			}
		}
		return ncollision;
	}
	public boolean rectcollision(int x1, int y1,int rectsx1, int rectsy1, int x2, int y2,int rectsx2, int rectsy2){
		//this method checks the collision of two rectangles
		if (x1+rectsx1 < x2 || x1 > x2 + rectsx2 || y1 + rectsy1 < y2 || y1 > y2 + rectsy2){
			//proof by contradiction
			return false;
		}
		return true;
	}
	public boolean circleRectangleCollision(int cx,int cy, int cr, int rx,int ry,int rl,int rw){
		//this method checks the collision of a circle against a rectangle
		if(rx<=cx && cx<=rx+rw && ry<=cy && cy<=ry+rl){
			return true;
		}
		else if (dist(rx,ry,cx,cy)<=cr){
			return true;
		}
		else if (dist(rx,ry+rl,cx,cy)<=cr){
			return true;
		}
		else if (dist(rx+rw,ry,cx,cy)<=cr){
			return true;
		}
		else if (dist(rx+rw,ry+rl,cx,cy)<=cr){
			return true;
		}
		return false;
	}
	public boolean checkFireballCollision(int fx,int fy,int mcx,int mcy){
		//check if the fireball is on the character
		return (fx+10>=mcx && fx+10 <=mcx+BH.mc.getWidth() && fy+10>=mcy && fy+10<=mcy+BH.mc.getLength());
	}
	public boolean objectPlacementCollision(int objx,int objy,int objsx,int objsy){
		//this method is called to check if the user can place an object on the map
		//check if it would collide with any other object on the map
		for (Zombie zombie: allZombies){
			if (rectcollision(objx,objy,objsx,objsy,zombie.getX(),zombie.getY(),zombie.getsx(),zombie.getsy())){
				return true;
			}
		}
		for(Devil devil:allDevils){
			if (rectcollision(objx,objy,objsx,objsy,devil.getX(),devil.getY(),devil.getsx(),devil.getsy())){
				return true;
			}
		}
		for(Barrel barrel:allBarrels){
			if (rectcollision(objx,objy,objsx,objsy,barrel.getX(),barrel.getY(),barrel.getsx(),barrel.getsy())){
				return true;
			}
		}
		for (Barricade barricade: allBarricades){
			if (rectcollision(objx,objy,objsx,objsy,barricade.getX(),barricade.getY(),barricade.getsx(),barricade.getsy())){
				return true;
			}
		}
		for (SentryGun sentry: allSentries){
			if (rectcollision(objx,objy,objsx,objsy,sentry.getX(),sentry.getY(),sentrysx,sentrysy)){
				return true;
			}
		}
		return false;
	}
	//GENERAL FUNCTIONS END
	//LEVEL STUFF BEGIN
	public void checkLevelOver(){
		//TBH If you can beat level 1 you're already beast
		if (ZombiesDead == getZombiesThisLevel() && DevilsDead == getDevilsThisLevel()){
			//if the number of zombies/devils dead in the level is the same as the number of them that is supposed to exist in the level
			//this level is over
			levelUp();
		}
	}
	public void levelUp(){
		//preparing variables for a new level
		BH.enemyGenerationTimer = new Timer(spawnsp-currentLevel*300, BH);
		BH.enemyGenerationTimer.start();
		currentLevel++;
		ZombiesDead=0;
		DevilsDead=0;
		ZombiesThisLevel=getZombiesThisLevel();
		DevilsThisLevel=getDevilsThisLevel();
		displayLevelCounter=200;
	}
	public int getZombiesThisLevel(){
		//# of zombies for the level
		return (currentLevel+2)*20;
	}
	public int getDevilsThisLevel(){
		//# of devil for the level
		return (currentLevel+2)*15;
	}
	public void checkDeath(){
		//check MC's death
		//go to GameOver screen
		if(BH.mc.getHealth()<=0){
			BH.state=BH.OVER;
			BH.go.activateMouse();
			BH.go.requestFocus();
		}
	}
	public void restart(){
		//restart the game 
		//set everything back to default
		//clear everything
		spawnsp = 2000;
		BH.enemyGenerationTimer = new Timer(spawnsp, BH);
		BH.enemyGenerationTimer.start();
		BH.mc.unloadCAmmo();
		printUpgradeString="";
		consecutiveCountDown=0;
		BH.mc.setWeapon(1);
		BH.magicalBoxAllowance=1;
		currentLevel=1;
		shootCountDown=0;
		mapx=0;
		mapy=0;
		BH.mc.loadMaxAmmo();
		BH.mc.loadWeaponSpeed();
		BH.mc.loadConsecutiveShoot();
		BH.mc.setHealth(BH.mc.full_health);
		//enemies reset
		ZombiesThisLevel=getZombiesThisLevel();
		DevilsThisLevel=getDevilsThisLevel();
		BH.score=0;
		ZombiesDead=0;
		DevilsDead=0;
		//ListArrays reset
		allZombies.clear();
		allDevils.clear();
		allGrenades.clear();
		explodedGrenade.clear();
		fireballs.clear();
		activeBullets.clear();
		allBarricades.clear();
		allBarrels.clear();
		allExplosions.clear();
		allSentries.clear();
		allBoxes.clear();
		//Mc position reset
		BH.mc.setX(100);
		BH.mc.setY(400);
		//keys reset
		keys[KeyEvent.VK_SPACE]=false;
		keys[KeyEvent.VK_UP]=false;
		keys[KeyEvent.VK_DOWN]=false;
		keys[KeyEvent.VK_LEFT]=false;
		keys[KeyEvent.VK_RIGHT]=false;
		keys[KeyEvent.VK_1]=false;
		keys[KeyEvent.VK_2]=false;
		keys[KeyEvent.VK_3]=false;
		keys[KeyEvent.VK_4]=false;
		keys[KeyEvent.VK_5]=false;
		keys[KeyEvent.VK_6]=false;
		keys[KeyEvent.VK_7]=false;
		keys[KeyEvent.VK_8]=false;
		//shotgun reset
		BH.mc.setSGW(0);
		BH.mc.loadConsecutiveShoot();
		consecutiveKills=0;
		nextUpgrade=0;
	}
	//LEVEL STUFF END
	//CHECKING OBJECTS START
	public void checkObjects(){
		//General Check function
		checkSentry();
		checkBarricades();
		checkBarrels();
		checkExplosions();
		checkDead();
	}
	private void checkExplosions(){
		//counts how long the explosions lasts
		ArrayList<Explosion> toRemove = new ArrayList<Explosion>();
		for (Explosion b : allExplosions){
			b.incrementtime(); //add to the counter
			if (b.getctime() >= 10){
				//if the counter is at max, remove the explosion
				toRemove.add(b);
			}
		}
		for (Explosion b : toRemove){
			allExplosions.remove(b);
		}
		
	}
	public void checkDead(){
		//this method is called to see if the character's bullets damage the enemies
		ArrayList<Zombie> toRemoveZ = new ArrayList<Zombie>();
		for (Zombie z : allZombies){
			//check if a zombie dies
			if (z.getHealth() <= 0){
				toRemoveZ.add(z);
			}
		}

		for (Zombie zzh8829:toRemoveZ){
			//remove the dead zombies
			if ((int)(Math.random()*3)==1){
				//Every once in a while a magical unicorn drops by after a zombie dies
				//And lays a magicalbox
				allBoxes.add(new MagicalBox(zzh8829.getX(),zzh8829.getY(),BH));
			}
			BH.score+=200+consecutiveKills*100; //add the score and bonus for consecutive kills
			//add on to the consecutive kills and set the consecutive countdown to full
			//add on to the dead count and remove the zombie
			ZombiesDead++;
			allZombies.remove(zzh8829);
			fullCountDown();
			addConsecutive();
		}
		ArrayList<Devil> toRemoveD = new ArrayList<Devil>();
		//see comments for the zombies above
		for (Devil d : allDevils){
			if (d.getHealth() <= 0){
				toRemoveD.add(d);
			}
		}
		for (Devil zzh8829:toRemoveD){
			BH.score+=200+consecutiveKills*100;
			allDevils.remove(zzh8829);
			//All Devils lay boxes
			allBoxes.add(new MagicalBox(zzh8829.getX(),zzh8829.getY(),BH));
			fullCountDown();
			DevilsDead++;
			addConsecutive();
			
		}
	}
	private void checkBarricades(){
		//if barricade has been destroyed
		ArrayList<Barricade> toRemove = new ArrayList<Barricade>();
		for (Barricade b : allBarricades){
			if (b.getHealth() <= 0){
				toRemove.add(b);
			}
		}
		for (Barricade b : toRemove){
			allBarricades.remove(b);
		}
	}
	//CHECKING OBJECTS END
	//Checking position BEGIN
	public boolean checkOutside(int x,int y){
		//check if it is outside the screen
		return x<0||x>800||y<0||y>640;
	}
	public boolean checkOutsideMap(int x, int y){
		//check if it is outside the map
		if (x >= 2000 || y >= 2000 || x < 0 || y < 0)
			return true;
		return false;
	}
	//CHECK POSITION END
	
	//BULLET AND FIREBALLS MOVING BEGIN
	public void moveFireballs()
	{
		//The OP devil weapon
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (PosPair temp : fireballs)
		{
			//go through every single fireball in the air
			final double ANG = Math.toRadians(temp.getANGLE());
			double xx = temp.getDX(), yy = temp.getDY();
			//move fireball based on the angle and position...TRIGS!
			temp.setPos(xx+20*Math.cos(ANG),yy+20*Math.sin(ANG));
			//remove the fireball if it hits anythign
			if (checkFireballCollision(temp.getX(),temp.getY(),BH.mc.getX(),BH.mc.getY())){
				//checks if MC gets hit
				BH.mc.setHealth(BH.mc.getHealth()-temp.getdmg());
				toRemove.add(temp);
			}
			else if (checkOutsideMap(temp.getX(),temp.getY())|| !validMove(temp.getX(),temp.getY())){
				toRemove.add(temp);
			}
			//check if the fireball hits a barrel or barricade
			//if so, reduce the health/remove it
			for (Barrel bar : allBarrels){
				int x = bar.getX(), y = bar.getY(), sx = bar.getsx(), sy = bar.getsy(), px = temp.getX(), py = temp.getY();
				if (px >= x && px <= x + sx && py >= y && py <= y + sy){//barrels get blown up by one hit
					bar.setHealth(0);
					toRemove.add(temp);
				}
			}
			for (Barricade bar : allBarricades){
				int x = bar.getX(), y = bar.getY(), sx = bar.getsx(), sy = bar.getsy(), px = temp.getX(), py = temp.getY();
				if (px >= x && px <= x + sx && py >= y && py <= y + sy){//barricades get destroyed
					bar.setHealth(bar.getHealth()-temp.getdmg());
					toRemove.add(temp);
				}
			}
		}
		for (PosPair pair:toRemove){
			fireballs.remove(pair);
		}
	}
	public void moveBullets(){
		//move all the active bullets
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (PosPair temp : activeBullets)
		{
			final double ANG = Math.toRadians(temp.getANGLE());
			double xx = temp.getDX(), yy = temp.getDY();
			final int sp =BH.mc.getAtWeaponSpeed(temp.getType());
			//calculate how far to move based on the angle, speed and position of the bullet
			double nx = xx+sp*Math.cos(ANG), ny = yy+sp*Math.sin(ANG);
			//if it is a valid move then move it, if not then remove it
			if (validMove((int)nx,(int)ny)){
				temp.setPos(nx,ny);
			}
			else{
				toRemove.add(temp);
			}
		}
		for (PosPair temp : toRemove){
			activeBullets.remove(temp);
		}
	}
	public void checkBulletCollision(){
		//check if the bullets hit any enemies
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (PosPair temp : activeBullets){
			if (enemyBulletCollision(temp.getX(),temp.getY())){
				//REMOVE THE bullets
				toRemove.add(temp);
			}
			else if (checkOutside(temp.getX(),temp.getY())){
				toRemove.add(temp);
			}
			else if (!validMove(temp.getX(),temp.getY())){
				//If it hits a wall
				toRemove.add(temp);
			}
			for (Barrel bar : allBarrels){
				//if it hits a barrel, an explosion occurs
				int x = bar.getX(), y = bar.getY(), sx = bar.getsx(), sy = bar.getsy(), px = temp.getX(), py = temp.getY();
				if (px >= x && px <= x + sx && py >= y && py <= y + sy){
					bar.setHealth(0);
					toRemove.add(temp);
				}
			}
			
		}
		for (PosPair pair:toRemove){
			activeBullets.remove(pair);
		}
	}
	public boolean enemyBulletCollision(int x, int y){
		//this method is called to see if the character's bullets damage the enemies
		for (Zombie z : allZombies){
			if (z.getCollide(x,y)){
				z.setHealth(z.getHealth() - BH.mc.getdmg(BH.mc.getWeapon()));
				return true;
			}
		}

		for (Devil d : allDevils){
			if (d.getCollide(x,y)){
				d.setHealth(d.getHealth() - BH.mc.getdmg(BH.mc.getWeapon()));
				return true;
			}
		}

		return false;
	}
	public void checkBulletDistance(){
		//checks how far the bullet travels and if it needs to be removed
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (PosPair a: activeBullets){
			if (dist(a.getorigX(),a.getorigY(),a.getX(),a.getY()) > BH.mc.getMaxDist(a.getTYPE())){
				toRemove.add(a);	
			}
		}
		for (PosPair pair:toRemove){
			activeBullets.remove(pair);
		}
		
	}
	//BULLET AND FIREBALLS MOVING END
	
	//ENEMY MOVEMENT BEGIN
	public void moveZombie(){
		//move zombie AI
		for (Zombie temp : allZombies){
			//go through all zombies
			//Manhat = manhatten distance (Straight line distance)
			double ManhatX = Math.abs(temp.getDX() - BH.mc.getDX()), ManhatY = Math.abs(temp.getDY() - BH.mc.getDY()), speed = temp.getspeed();
			//manhatx, x difference								manhaty, y difference
			double moveX = ManhatX/(ManhatX+ManhatY)*speed, moveY = ManhatY/(ManhatX+ManhatY)*speed,		nx, ny;
			//amount the zombie will move
			//move the zombie toward the MC
			temp.setAngle(Math.toDegrees(3.14159265358+Math.atan2(temp.getDY() - BH.mc.getDY(),temp.getDX() - BH.mc.getDX())));
			if (temp.getDX() <= BH.mc.getDX()){
				nx = temp.getDX()+moveX;
			}
			else{
				nx = temp.getDX()-moveX;
			}
			if (temp.getDY() <= BH.mc.getDY()){
				ny = temp.getDY()+moveY;
			}
			else{
				ny = temp.getDY()-moveY;
			}
			for (Barricade b : allBarricades){//zombies destroy barricades
				if (b.rectcollision((int)nx,(int)ny)){
					b.setHealth(b.getHealth()-temp.getdmg());
				}
			}
			for (SentryGun b : allSentries){//also destroy sentryguns
				if (b.rectcollision((int)nx,(int)ny)){
					b.setHealth(b.getHealth()-temp.getdmg());
				}
			}
			//If movement is valid
			if (numbercollisions((int)nx, temp.getY()) <= 1 && validMove((int)nx,temp.getY())){
				temp.setX(nx);
			}
			if (numbercollisions(temp.getX(), (int)ny) <= 1 && validMove(temp.getX(),(int)ny)){
				temp.setY(ny);
			}//note: co-ordinates are stored as doubles to allow micro movements
		}
	}
	public void moveDevil(){
		//Refer to comments above :)
		for (Devil temp : allDevils){
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
			for (Barricade b : allBarricades){
				if (b.rectcollision((int)nx,(int)ny)){
					b.setHealth(b.getHealth()-temp.getdmg());
				}
			}
			for (SentryGun b : allSentries){
				if (b.rectcollision((int)nx,(int)ny)){
					b.setHealth(b.getHealth()-temp.getdmg());
				}
			}
			if (numbercollisions((int)nx, temp.getY()) <= 1 && validMove((int)nx,temp.getY())){
				temp.setX(nx);
			}
			if (numbercollisions(temp.getX(), (int)ny) <= 1 && validMove(temp.getX(),(int)ny)){
				temp.setY(ny);
			}
		}
		
	}
	//ENEMY MOVEMENT END
	//ENEMY SPRITE INCREMENT BEGIN
	public void addZombieCounter(){
		//add to the zombie counter to blit the sprites
		for (Zombie z : allZombies){
			z.addToCounter();
		}
	}
	public void addDevilCounter(){
		//add to the devil counter to blit the sprites
		for (Devil d : allDevils){
			d.addToCounter();
		}
	}
	//ENEMY SPRITE INCREMENT END
	//ENEMY GENERATION BEGIN
	public void generateEnemy(){
		//generating enemies at specific locations (rects)
		if (ZombiesThisLevel>0){
			addZombie(-mapx,-mapy,0,490);
			addZombie(-mapx,-mapy + 1040,0,520);
			addZombie(-mapx+1999,-mapy+1700,0,170);
			addZombie(-mapx+1625,-mapy+230,130,30);
			addZombie(-mapx+710,-mapy,320,0);
		}
		if (DevilsThisLevel>0){
			addDevil(-mapx,-mapy,0,490);
			addDevil(-mapx,-mapy+1040,0,520);
		}
	}
	public void addZombie(int x, int y, int sx, int sy){
		//generates a zombie
		for (int i=0; i!=10; ++i){
			//random position in the rect
			int x1 = x + (int)(Math.random()*sx);
			int y1 = y + (int)(Math.random()*sy);
			if (numbercollisions(x1,y1) == 0){
				ZombiesThisLevel--; //reduce the zombie count in ZombiesThisLevel since one is being generated
				allZombies.add(new Zombie(x1,y1,0,BH.mc)); //add it to the arraylist keeping track of all zombies on the screen
				return;
			}
		}
	}
	public void addDevil(int x, int y, int sx, int sy){
		//refer to comments above
		for (int i=0; i!=10; ++i){
			int x1 = x + (int)(Math.random()*sx);//random position in the rect
			int y1 = y + (int)(Math.random()*sy);
			if (numbercollisions(x1,y1) == 0){
				DevilsThisLevel--;
				allDevils.add(new Devil(x1,y1,0,BH.mc));
				return;
			}
		}
	}
	//ENEMY GENERATION END
	//MAP MOVEMENT BEGIN
	public void moveMap()
	{
		//the shifting of the map
		shiftx = shifty = 0;
		int mcx = BH.mc.getX(), mcy = BH.mc.getY(); //character position
		if (mcx < bx1 && mapx!=0){ //checks if beyond boundary and if need of shifiting
			mapx = mapx - (bx1 - mcx);//some sketchy math 
			shiftx =  - (bx1 - mcx);
			mcx = bx1;
		}
		else if (mcx > bx2 && mapx!=mapsx-screensx){ // checks if in need of shift
			mapx = mapx + (mcx - bx2);
			shiftx = (mcx - bx2);
			mcx = bx2;
		}
		if (mcy < by1 && mapy!=0){// checks if in need of shift
			mapy = mapy - (by1 - mcy);
			shifty =  - (by1 - mcy);
			mcy = by1;
		}
		else if (mcy > by2 && mapy!=mapsy-screensy){// checks if in need of shift
			mapy = mapy + (mcy - by2);
			shifty = (mcy - by2);
			mcy = by2;
		}
		BH.mc.setX(mcx);	//replace the coordinates with new ones
		BH.mc.setY(mcy);
		mapx = Math.max(0,Math.min(mapx,mapsx-screensx));
		mapy = Math.max(0,Math.min(mapy,mapsy-screensy));
	}
	public void updateposition(){
		//after shifting, move all the objects as well
		if (shiftx == 0 && shifty == 0){
			return;
		}
		for (Zombie a : allZombies){
			a.setX(a.getDX() - shiftx);
			a.setY(a.getDY() - shifty);
		}
		for (MagicalBox a : allBoxes){
			a.setX(a.getX()-shiftx);
			a.setY(a.getY()-shifty);
		}
		for (Devil a : allDevils){		
			a.setX(a.getDX()-shiftx);		
			a.setY(a.getDY()-shifty);		
		}
		for (Barricade a : allBarricades){
			a.setX(a.getX()-shiftx);
			a.setY(a.getY()-shifty);
		}
		for (Barrel a : allBarrels){
			a.setX(a.getX()-shiftx);
			a.setY(a.getY()-shifty);
		}
		for (SentryGun a : allSentries){
			a.setX(a.getX()-shiftx);
			a.setY(a.getY()-shifty);
		}
		for (Grenade g: allGrenades){
			g.setX(g.getX()-shiftx);
			g.setY(g.getY()-shifty);
		}
		for (Grenade g: explodedGrenade){
			g.setX(g.getX()-shiftx);
			g.setY(g.getY()-shifty);
		}
		for (Explosion exp : allExplosions){		
			exp.setX(exp.getX()-shiftx);		
			exp.setY(exp.getY()-shifty);		
		}
		for(PosPair f: fireballs){
			f.setPos(f.getX()-shiftx, f.getY()-shifty);
		}
		for (PosPair b: activeBullets){
			b.setPos(b.getX()-shiftx, b.getY()-shifty);
		}
	}
	//MAP MOVEMENT END
	//SENTRY STUFF BEGIN
	public void shootSentry(){
		//Sentries shoot at closest enemy
		for (SentryGun sentry : allSentries){
			int x = sentry.getcx(), y = sentry.getcy();
			Zombie zClosest = new Zombie(0,0,0,BH.mc);
			Devil dClosest = new Devil(0,0,0,BH.mc);
			double zd = 1000000000, dd = 1000000000;
			//find the closest zombie and devil
			for (Zombie z : allZombies){
				if (dist(x,y,z.getcx(),z.getcy()) <= zd){
					zd = dist(x,y,z.getcx(),z.getcy());
					zClosest = z;
				}
			}
			for (Devil d : allDevils){
				if (dist(x,y,d.getcx(),d.getcy()) <= dd){
					dd = dist(x,y,d.getcx(),d.getcy());
					dClosest = d;
				}
			}
			//Whether the zombie or devil is closer and if it's in range
			//shoot at the closer one
			if (zd < dd && zd <= sentry.getrange()*sentry.getrange()){
				int ang = (int)(Math.toDegrees(Math.PI+Math.atan2(y - zClosest.getcy(),x - zClosest.getDX())));
				activeBullets.add(new PosPair(x,y,ang,1));
				activateAudio(SENTRY);
				sentry.setammo(sentry.getammo()-1);
			}
			else if (dd <= sentry.getrange()*sentry.getrange()){
				int ang = (int)(Math.toDegrees(Math.PI+Math.atan2(y - dClosest.getDY(),x - dClosest.getDX())));
				activeBullets.add(new PosPair(x,y,ang,1));
				activateAudio(SENTRY);
				sentry.setammo(sentry.getammo()-1);
			}
		}
	}
	private void checkSentry(){
		//if sentry is dead or out of ammo, remove it
		ArrayList<SentryGun> toRemove = new ArrayList<SentryGun>();
		for (SentryGun b : allSentries){
			if (b.getHealth() <= 0 || b.getammo() <= 0){
				toRemove.add(b);
			}
		}
		for (SentryGun b : toRemove){
			allSentries.remove(b);
		}
	}
	//SENTRY STUFF END
	
	//BARREL STUFF BEGIN
	private void checkBarrels(){
		//check the explosion of the barrels
		ArrayList<Barrel> toRemove = new ArrayList<Barrel>();
		for (int x=1; x!=4; ++x){
			//chain explosion looping 3 times for missing cases
			for (Barrel b1 : allBarrels){
				//Chain explosion
				if (b1.getHealth() <= 0){
					//check the rest of the barrels and see if they are affected
					for (Barrel b2 : allBarrels){
						int x1 = b1.getcx(), x2 = b2.getcx(), y1 = b1.getcy(), y2 = b2.getcy();
						if (dist(x1,y1,x2,y2) <= b1.getrange()){
							b2.setHealth(0);
						}
					}
				}
			}
		}
		for (Barrel b : allBarrels){
			//remove all barrels that are at health 0
			//play the audio for explosion
			if (b.getHealth() <= 0){
				toRemove.add(b);
				barrelExplodeAudio.play();
			}
		}
		
		
		for (Barrel b : toRemove){
			//do the explosion thing to check for damage  the barrel
			//add to allExplosions to check for further updates and blit the explosion sprite
			Explosion eee=new Explosion(b.getcx(), b.getcy());
			allExplosions.add(eee);
			explosiondmg(eee);
			allBarrels.remove(b);
		}
	}
	public void explosiondmg(Explosion exp){
		//destroys everything in a radius
		int x = exp.getX(), y = exp.getY();
		
		for (Zombie temp : allZombies){
			int x2 = temp.getX(), y2 = temp.getY();
			if (dist(x,y,x2,y2) <= exp.getrange()){
				temp.setHealth(temp.getHealth() - exp.getdmg());
			}
		}
		for (Devil temp : allDevils){
			int x2 = temp.getX(), y2 = temp.getY();
			if (dist(x,y,x2,y2) <= exp.getrange()){
				temp.setHealth(temp.getHealth() - exp.getdmg());
			}
		}
		for (Barricade b : allBarricades){
			int x2 = b.getcx(), y2 = b.getcy();
			if (dist(x,y,x2,y2) <= exp.getrange()){
				b.setHealth(b.getHealth() - exp.getdmg());
			}
		}
		for (SentryGun b : allSentries){
			int x2 = b.getcx(), y2 = b.getcy();
			if (dist(x,y,x2,y2) <= exp.getrange()){
				b.setHealth(b.getHealth() - exp.getdmg());
			}
		}
		int x2 = BH.mc.getX(), y2 = BH.mc.getY();
		if (dist(x,y,x2,y2) <= exp.getrange()){
			BH.mc.setHealth(BH.mc.getHealth() - exp.getdmg());
		}
		
	}
	//BARREL STUFF END
	
	//GRENADE BEGIN
	public void countdownGrenade(){
		//counts down the grenade
		//explode it if it is at 0 (also play the audio and remove it)
		ArrayList<Grenade> toRemove = new ArrayList<Grenade>();
		for (Grenade grenade: allGrenades){
			grenade.countDown();
			if (grenade.getCounter()==0){
				explodeGrenade.play();
				GrenadeExplode(grenade);
				toRemove.add(grenade);
			}
		}
		for (Grenade grenade:toRemove){
			explodedGrenade.add(grenade);
			allGrenades.remove(grenade);
		}
	}
	public void GrenadeExplode(Grenade grenade){
		//explode the grenade and damage everything within the radius
		for (Devil devil: allDevils){
			if (circleRectangleCollision(grenade.getX(),grenade.getY(),grenade.getdmgrange(),devil.getX(),devil.getY(),devil.getsx(),devil.getsy())){
				devil.setHealth(devil.getHealth()-grenade.getdmg());
			}
		}
		for (Zombie zombie: allZombies){
			if (circleRectangleCollision(grenade.getX(),grenade.getY(),grenade.getdmgrange(),zombie.getX(),zombie.getY(),zombie.getsx(),zombie.getsy())){
				zombie.setHealth(zombie.getHealth()-grenade.getdmg());
			}
		}
		if (circleRectangleCollision(grenade.getX(),grenade.getY(),grenade.getdmgrange(),BH.mc.getX(),BH.mc.getY(),BH.mc.getsx(),BH.mc.getsy())){
			BH.mc.setHealth(BH.mc.getHealth()-grenade.getdmg());
		}
		for (Barrel bar : allBarrels){
			int x = bar.getX(), y = bar.getY(), sx = bar.getsx(), sy = bar.getsy();
			if (circleRectangleCollision(grenade.getX(),grenade.getY(),grenade.getdmgrange(),x,y,sx,sy)){
				//barrel is automatically at 0 since it is triggered by the explosion by the grenade
				bar.setHealth(0);
			}
		}
		
	}
	public void PostGrenadeExplosion(){
		//after the grenade explosion
		//look through explodedGrenade arraylist
		//count down and check if it is at 0
		//if it is then remove it
		//anything in the explodedGrenade arraylist is blitted on the screen with an explosion sprite
		ArrayList<Grenade> toRemove = new ArrayList<Grenade>();
		for (Grenade grenade: explodedGrenade){
			grenade.explodeCountDown();
			if (grenade.explodeCount==0){
				toRemove.add(grenade);
			}
		}
		for (Grenade grenade: toRemove){
			explodedGrenade.remove(grenade);
		}
	}
	//GRENADE END
	
	//MAGICAL BOX STUFF BEGIN
	public void checkMagicalBox(){
		//check if the box should appear any longer
		//if it is at 0 then remove the box, it's had its time to shine
		ArrayList<MagicalBox> toRemove = new ArrayList<MagicalBox>();
		for (MagicalBox b: allBoxes){
			if (b.getCounter()==0){
				toRemove.add(b);
			}
		}
		for (MagicalBox b:toRemove){
			allBoxes.remove(b);
		}
	}
	public void boxCount(){
		//reduce the box counter if it is not at 0
		if (boxCountDown!=0){
			boxCountDown--;
		}
		else{
			boxString="";
		}
	}
	public void checkBoxCollision(){
		//check if the box is being picked up
		//if it is, add the item to the character
		//and remove the box from the screen
		ArrayList<MagicalBox> toRemove = new ArrayList<MagicalBox>();
		for (MagicalBox box : allBoxes){
			int x = box.getX(), y = box.getY(), mcx = BH.mc.getX(), mcy = BH.mc.getY();
			if (x + box.getbsx() < mcx || x > mcx + BH.mc.getsx() || y + box.getbsy() < mcy || y > mcy + BH.mc.getsy()){
				continue;
			}
			addItem(box.generateItem());
			toRemove.add(box);
		}
		for (MagicalBox box:toRemove){
			allBoxes.remove(box);
		}
		
	}
	public void addItem(int item){
		//add the item from the box
		if (item==HEALTH){
			//add health to character
			BH.mc.setHealth(Math.min(BH.mc.getHealth()+500,1000));
		}
		else{
			//add ammo for that weapon
			BH.mc.addAmmo(item);
		}
	}
	//MAGICAL BOX STUFF END
	public void paintComponent(Graphics g){
		g.drawImage(background, -mapx, -mapy, this);
		
		//drawing the health bar
		g.setColor(Color.green);
		g.fillRect(BH.mc.getX()-5,BH.mc.getY()-3,BH.mc.calculateHealth(),5); //filling of the bar
		g.setColor(Color.black);
		g.drawRect(BH.mc.getX()-5, BH.mc.getY()-3, 30, 5); //drawing the outline

		///////////////////////////////////drawing objects on the screen///////////////////////////////////////
		g.drawImage(charSprites[BH.mc.getANGLE()/45][spriteCounter%3],BH.mc.getX(),BH.mc.getY(),this);		//draw character
		for (PosPair pp : activeBullets){
			g.drawImage(bulletSprites[pp.getANGLE()%360/45],pp.getX(),pp.getY(),this);
		}
		for (PosPair pp : fireballs){
			g.drawImage(fireballSprites[pp.getANGLE()%360/45],pp.getX(),pp.getY(),this);
		}
		for (MagicalBox b : allBoxes){
			b.countDown();
			g.drawImage(boxSprite,b.getX(),b.getY(),this);
		}
		for (Zombie a : allZombies){
			g.drawImage(zombieSprites[(a.getAngle()+22)%360/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		for (Devil a : allDevils){
			g.drawImage(devilSprites[(a.getAngle()+22)%360/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		for (Barricade bar : allBarricades){
			g.drawImage(barricadeSprite, bar.getX(), bar.getY(), this);
		}
		for (Barrel bar : allBarrels){
			g.drawImage(barrelSprite,bar.getX(),bar.getY(),this);
		}
		for (SentryGun bar : allSentries){
			g.setColor(Color.black);
			g.fillRect(bar.getX(), bar.getY()-10, 20, 20);
			g.setColor(Color.gray);
			g.fillRect(bar.getX()+5, bar.getY()-5, 10, 10);
		}
		for (Grenade grenade: allGrenades){
			g.drawImage(grenadeSprite,grenade.getX()-grenadeSprite.getHeight(this)/2, grenade.getY()-grenadeSprite.getWidth(this)/2, this);
		}
		for (Grenade grenade: explodedGrenade){
			g.drawImage(grenadeExploded, grenade.getX()-grenadeExploded.getHeight(this)/2,grenade.getY()-grenadeExploded.getWidth(this)/2,this);
		}
		for (Explosion exp : allExplosions){		
			g.drawImage(barrelExplosion,exp.getX()-exp.getrange()/2,exp.getY()-exp.getrange()/2,this);				
		}
		///////////////////////////////////////////////////////////////////////////////////////////
		
		///////////////////////////drawing the strings on the screen//////////////////////////
		g.setColor(Color.black);
		g.setFont(SMALLfont);
		///////drawing weapon name and ammo////////
		if (BH.mc.getWeapon()==1){
			//pistol is unlimited so no need to display ammo
			g.drawString(weaponNames[BH.mc.getWeapon()], BH.mc.getX()-5, BH.mc.getY()-10); //maybe do the string formatting with this later if we have time
		}
		else{
			g.drawString(weaponNames[BH.mc.getWeapon()]+" "+BH.mc.getAmmo(BH.mc.getWeapon()), BH.mc.getX()-5, BH.mc.getY()-10); //maybe do the string formatting with this later if we have time
		}
	
		g.setFont(font);
		//drawing the display for the item obtained through the box
		g.drawString(boxString,100,500+boxCountDown);
		
		//drawing the string of the upgrade obtained by the character
		g.drawString(printUpgradeString, 300, 500+UpgradeStringCountDown);
		
		//drawing the level display
		if (displayLevelCounter>0){
			displayLevelCounter--;
			g.drawString("+-+-+-+ "+currentLevel+" +-+-+-+", 500, 500+displayLevelCounter/2);
		}
		
		g.setFont(LARGEfont);
		//drawing the score
		g.drawString(BH.score+"", 20, 100);
		
		//draw consecutive kills
		//the color of the string changes based on how much time there is left until the next reduction of consecutive kill
		//consecutive kill is reduced if the character doesn't continue to kill more enemies within a period of time
		g.setColor(new Color(225-consecutiveCountDown,225-consecutiveCountDown,225-consecutiveCountDown));
		g.drawString(consecutiveKills+"",670,100);
		////////////////////////////////////////////////////////////////////////////////////
		
		lastSpaceStat=keys[KeyEvent.VK_SPACE];		//the status of space being pressed
		
		checkMagicalBox();		//check if it is time to remove the boxes
		boxCount(); //changing the counter on the boxes to see how much time the box has before it disappears
		
	}
}
