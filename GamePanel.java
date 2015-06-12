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

// Paul Krishnamurthy is a noob


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
	private Image boxSprite = new ImageIcon("box.png").getImage();
	private Image[] fireballSprites = new Image[8];
	private Image[] bulletSprites = new Image[8];
	private Image grenadeSprite = new ImageIcon("grenade.png").getImage();
	private Image grenadeExploded = new ImageIcon("grenadeExplode.png").getImage();
	private Image barrelSprite = new ImageIcon("barrel.png").getImage();
	private Image barrelExplosion = new ImageIcon("barrelExplosion.png").getImage();
	private Image barricadeSprite = new ImageIcon("barricade.png").getImage();
	
	//backgrounds
	private BufferedImage mask_background;
	private Image background = new ImageIcon("forestmap2.jpg").getImage();
	
	private int[] weapondist = new int[30];//should not exist
	
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
	private final int SOMENUMBER=15; //constant to refill shootCounDown
	
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
		updateweapon();
		getWeaponNames();	
		
		loadAudio();
		loadSprites();
		loadMask();
		currentLevel=1;
		ZombiesThisLevel = getZombiesThisLevel();
		DevilsThisLevel = getDevilsThisLevel();
		generateEnemy();
	}

	public void loadMask(){
		try{
			File file= new File("mask_map3.jpg");
			mask_background = ImageIO.read(file);
		}
		catch (IOException ex){}
	}
	public void loadSprites(){
		for (int i=0;i<8;i++){
			for (int k=0;k<3;k++){
				charSprites[i][k]=new ImageIcon("guy0"+i+k+".png").getImage();
			}
		}
		for (int i=0;i<8;i++){
			for (int k=0;k<8;k++){
				zombieSprites[i][k]=new ImageIcon("zombie"+i+k+".png").getImage();
				devilSprites[i][k]=new ImageIcon("devil"+i+k+".png").getImage();
			}
		}
		for (int i=0;i<8;i++){
			bulletSprites[i]=new ImageIcon("pistol"+i+".png").getImage();
			fireballSprites[i]=new ImageIcon("fireball"+i+".png").getImage();
		}
		
	}
	public void loadAudio(){
		audio[1]=Applet.newAudioClip(getClass().getResource("pistol.wav"));
		audio[2]=Applet.newAudioClip(getClass().getResource("uzi.wav"));
		audio[3]=Applet.newAudioClip(getClass().getResource("shotgun.wav"));
		audio[SENTRY]=Applet.newAudioClip(getClass().getResource("sentrygun_qq.wav"));
		audio[GRENADE] = Applet.newAudioClip(getClass().getResource("grenadethrow.wav"));
	}
	public void getWeaponNames(){
		weaponNames[1]="PISTOL";
		weaponNames[2]="UZI";
		weaponNames[3]="SHOTGUN";
		weaponNames[4]="BARREL";
		weaponNames[5]="GRENADE";
		weaponNames[6]="BARRICADE";
		weaponNames[7]="SENTRY";
	}
	public void fullUpgradeCountDown(){
		UpgradeStringCountDown=100;
	}
	public void UpgradeCountDown(){
		if (UpgradeStringCountDown!=0){
			UpgradeStringCountDown--;
		}
		else{
			printUpgradeString="";
		}
		
	}
	public void setUpgradeString(String n){
		printUpgradeString=n;
	}
	public void setBoxString(String n){
		boxString=n;
		boxCountDown=100;
	}
	public void updateweapon(){
		//we keep track of how far the weapon can travel
		weapondist[1] = 400;
		weapondist[2] = 500;
		for (int i=3; i!=7; ++i)
			weapondist[i] = 600;
	}
	public void restart(){
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
		ZombiesThisLevel=getZombiesThisLevel();
		DevilsThisLevel=getDevilsThisLevel();
		ZombiesDead=0;
		DevilsDead=0;
		allZombies.clear();
		allDevils.clear();
		allGrenades.clear();
		explodedGrenade.clear();
		fireballs.clear();
		activeBullets.clear();
		BH.score=0;
		allBarricades.clear();
		allBarrels.clear();
		allExplosions.clear();
		allSentries.clear();
		allBoxes.clear();
		BH.mc.setX(100);
		BH.mc.setY(400);
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
		BH.mc.setSGW(0);
		BH.mc.loadConsecutiveShoot();
		consecutiveKills=0;
		nextUpgrade=0;
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
	public boolean checkFireballCollision(int fx,int fy,int mcx,int mcy){
		//check if the fireball is on the character
		return (fx+10>=mcx && fx+10 <=mcx+BH.mc.getWidth() && fy+10>=mcy && fy+10<=mcy+BH.mc.getLength());
	}
	public boolean objectPlacementCollision(int objx,int objy,int objsx,int objsy){
		boolean flag=false;
		for (Zombie zombie: allZombies){
			if (rectcollision(objx,objy,objsx,objsy,zombie.getX(),zombie.getY(),zombie.getsx(),zombie.getsy())){
				flag=true;
			}
		}
		for(Devil devil:allDevils){
			if (rectcollision(objx,objy,objsx,objsy,devil.getX(),devil.getY(),devil.getsx(),devil.getsy())){
				flag=true;
			}
		}
		for(Barrel barrel:allBarrels){
			if (rectcollision(objx,objy,objsx,objsy,barrel.getX(),barrel.getY(),barrel.getsx(),barrel.getsy())){
				flag=true;
			}
		}
		for (Barricade barricade: allBarricades){
			if (rectcollision(objx,objy,objsx,objsy,barricade.getX(),barricade.getY(),barricade.getsx(),barricade.getsy())){
				flag=true;
			}
		}
		for (SentryGun sentry: allSentries){
			if (rectcollision(objx,objy,objsx,objsy,sentry.getX(),sentry.getY(),sentrysx,sentrysy)){
				flag=true;
			}
		}
		return flag;
	}
	public void MCshoot(){ //Main character doing action
		if (keys[KeyEvent.VK_SPACE]){
			int mx = BH.mc.getcx(), my = BH.mc.getcy();//get center position
			//if the user shoots, add a bullet into the arraylist keeping track of flying bullets
			if (BH.mc.getWeapon()==BARRICADE){
				if (!lastSpaceStat){
					int nx = (int) (mx + 45*Math.cos(Math.toRadians(BH.mc.getANGLE())));
					int ny = (int) (my + 45*Math.sin(Math.toRadians(BH.mc.getANGLE())));
					if(!objectPlacementCollision(nx-barricadesx/2,ny-barricadesy/2,barricadesx,barricadesy)){
						allBarricades.add(new Barricade(nx-barricadesx/2,ny-barricadesy/2));//If the place is empty
						BH.mc.useAmmo(BARRICADE);
					}		
				}
			}
			else if (BH.mc.getWeapon()==BARREL){
				if (!lastSpaceStat){
					int nx = (int) (mx + 30*Math.cos(Math.toRadians(BH.mc.getANGLE())));
					int ny = (int) (my + 55*Math.sin(Math.toRadians(BH.mc.getANGLE())));
					if(!objectPlacementCollision(nx-barrelsx/2,ny-barrelsy/2,barrelsx,barrelsy)){
						allBarrels.add(new Barrel(nx - barrelsx/2, ny - barrelsy/2));//If the place is empty
						BH.mc.useAmmo(BARREL);
					}	
				}
			}
			else if (BH.mc.getWeapon()==GRENADE){
				if (!lastSpaceStat){
					int nx = (int) (mx + 60*Math.cos(Math.toRadians(BH.mc.getANGLE())));
					int ny = (int) (my + 60*Math.sin(Math.toRadians(BH.mc.getANGLE())));
					allGrenades.add(new Grenade(nx,ny,BH));
					BH.mc.useAmmo(GRENADE);
					activateAudio(GRENADE);
				}
			}
			else if (BH.mc.getWeapon()==SENTRY){
				if (!lastSpaceStat){
					int nx = (int) (mx + 37*Math.cos(Math.toRadians(BH.mc.getANGLE())));
					int ny = (int) (my + 37*Math.sin(Math.toRadians(BH.mc.getANGLE())));
					if(!objectPlacementCollision(nx-sentrysx/2,ny-sentrysy/2,sentrysx,sentrysy)){//If the space is empty
						allSentries.add(new SentryGun(nx-sentrysx/2,ny-sentrysy/2));//Man's best friend
						BH.mc.useAmmo(SENTRY);
					}
				}
			}
			else if (BH.mc.getWeapon()==SHOTGUN){
				if ((BH.mc.getConsecutiveShoot(SHOTGUN) &&shootCountDown==0)|| lastSpaceStat==false){
					//if it can shoot consecutively. if not, it cannot shoot consecutively and the last space stat must be false
					if (BH.mc.getSGW()==0){
						activeBullets.add(new PosPair(mx,my,BH.mc.getANGLE(),BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()+5)%360,BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()-5+360)%360,BH.mc.getWeapon()));
					}
					else if (BH.mc.getSGW()==1){
						activeBullets.add(new PosPair(mx,my,BH.mc.getANGLE(),BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()+10)%360,BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()-10+360)%360,BH.mc.getWeapon()));
						//wide shot
					}
					else{
						activeBullets.add(new PosPair(mx,my,BH.mc.getANGLE(),BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()+20)%360,BH.mc.getWeapon()));
						activeBullets.add(new PosPair(mx,my,(BH.mc.getANGLE()-20+360)%360,BH.mc.getWeapon()));
						//wider shot
					}
					shootCountDown=SOMENUMBER;
					activateAudio(BH.mc.getWeapon());
					BH.mc.useAmmo(BH.mc.getWeapon());
				}
			}
			else if (BH.mc.getWeapon()==1){
				//If it can be shot consecutively otherwise last space stat must be false
				if ((BH.mc.getConsecutiveShoot(PISTOL)&&shootCountDown==0)|| lastSpaceStat==false){
					activeBullets.add(new PosPair(mx,my,BH.mc.getANGLE(),BH.mc.getWeapon()));
					BH.mc.useAmmo(BH.mc.getWeapon());
					activateAudio(BH.mc.getWeapon());
					shootCountDown=SOMENUMBER;
				}
			}
			else{
				if (shootCountDown==0){
					shootCountDown=SOMENUMBER;
					activeBullets.add(new PosPair(mx,my,BH.mc.getANGLE(),BH.mc.getWeapon()));
					BH.mc.useAmmo(BH.mc.getWeapon());
					activateAudio(BH.mc.getWeapon());
				}
				
			}
			if (shootCountDown>0){
				shootCountDown--;
			}
		}
	}
	public void activateAudio(int weapon){
		audio[weapon].play();
	}
	public int getNextUpgrade(){
		return nextUpgrade;
	}
	public void checkPause(){
		//display another pause screen
		if (keys[KeyEvent.VK_P]){
			BH.state=BH.PAUSE;
			//setFocusable(false);
    		BH.sm.requestFocus();
    		keys[KeyEvent.VK_P]=false;
		}
	}
	public void moveMC(){
		int speed = BH.mc.getspeed();		
		double nx=BH.mc.getX(), ny=BH.mc.getY();
		//new x position, 		new y position
		
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
	
	public void moveZombie(){//move zombie
		for (int i=0; i< allZombies.size(); i++){
			Zombie temp = allZombies.get(i);
			double ManhatX = Math.abs(temp.getDX() - BH.mc.getDX()), ManhatY = Math.abs(temp.getDY() - BH.mc.getDY()), speed = temp.getspeed();
			//manhatx, x difference								manhaty, y difference
			double moveX = ManhatX/(ManhatX+ManhatY)*speed, moveY = ManhatY/(ManhatX+ManhatY)*speed,		nx, ny;
			//amount the zombie will move
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
	public void moveDevil(){//Refer to comments above :)
		for (int i=0; i< allDevils.size(); i++){
			Devil temp = allDevils.get(i);
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
	public int numbercollisions(int x, int y){//If pixel does not collide with rects
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
		if (x1+rectsx1 < x2 || x1 > x2 + rectsx2 || y1 + rectsy1 < y2 || y1 > y2 + rectsy2){//proof by contracdiction
			return false;
		}
		return true;
	}
	
	public void fullCountDown(){consecutiveCountDown=225;}
	public void CountDown(){
		if (consecutiveCountDown>0){
			consecutiveCountDown--;
			if (consecutiveCountDown==0 && consecutiveKills>0){
				consecutiveKills--;
				if (consecutiveKills>0){
					fullCountDown();
				}
			}
		}
	}
	public void checkDeath(){
		if(BH.mc.getHealth()<=0){
			BH.state=BH.OVER;
			BH.go.activateMouse();
			//setFocusable(false);
			BH.go.requestFocus();
		}
	}
	public boolean enemyCollision(int x, int y){
		//this method is called to see if the character's bullets damage the enemies
		boolean flag = false;
		for (Zombie z : allZombies){
			if (z.getCollide(x,y)){
				flag = true;
				z.setHealth(z.getHealth() - BH.mc.getdmg(BH.mc.getWeapon()));
			}
		}

		for (Devil d : allDevils){
			if (d.getCollide(x,y)){
				flag = true;
				d.setHealth(d.getHealth() - BH.mc.getdmg(BH.mc.getWeapon()));
			}
		}

		return flag;
	}
	public void addConsecutive(){
		consecutiveKills++;
		if (consecutiveKills==BH.ug.allUpgradesNum[nextUpgrade]){
			BH.ug.getUpgrade(BH.ug.allUpgradesNum[nextUpgrade++]);
		}
	}
	public boolean checkOutside(int x,int y){
		return x<0||x>800||y<0||y>640;
	}
	public void checkBulletCollision(){
		//check if the bullets hit any enemies
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (PosPair temp : activeBullets){
			if (enemyCollision(temp.getX(),temp.getY())){
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
	public void checkBulletDistance(){
		//checks how far the bullet travels and if it needs to be removed
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (PosPair a: activeBullets){
			if (dist(a.getorigX(),a.getorigY(),a.getX(),a.getY()) > weapondist[a.getTYPE()]){
				toRemove.add(a);	
			}
		}
		for (PosPair pair:toRemove){
			activeBullets.remove(pair);
		}
		
	}
	
	public void addZombieCounter(){
		for (int i=0;i<allZombies.size();i++){
			allZombies.get(i).addToCounter();
		}
	}
	public void addDevilCounter(){
		for (int i=0;i<allDevils.size();i++){
			allDevils.get(i).addToCounter();
		}
	}
	public void moveBullets(){
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (PosPair temp : activeBullets)
		{
			final double ANG = Math.toRadians(temp.getANGLE());
			double xx = temp.getDX(), yy = temp.getDY();
			final int sp =BH.mc.getAtWeaponSpeed(temp.getType());
			double nx = xx+sp*Math.cos(ANG), ny = yy+sp*Math.sin(ANG);
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
	public boolean checkOutsideMap(int x, int y){
		if (x >= 2000 || y >= 2000 || x < 0 || y < 0)
			return true;
		return false;
	}
	public void moveFireballs()
	{//The OP devil weapon
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (PosPair temp : fireballs)
		{
			final double ANG = Math.toRadians(temp.getANGLE());
			double xx = temp.getDX(), yy = temp.getDY();
			temp.setPos(xx+20*Math.cos(ANG),yy+20*Math.sin(ANG));
			if (checkFireballCollision(temp.getX(),temp.getY(),BH.mc.getX(),BH.mc.getY())){
				//checks if MC gets hit
				BH.mc.setHealth(BH.mc.getHealth()-temp.getdmg());
				toRemove.add(temp);
			}
			else if (checkOutsideMap(temp.getX(),temp.getY())|| !validMove(temp.getX(),temp.getY())){
				toRemove.add(temp);
			}
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
					bar.setHealth(0);
					toRemove.add(temp);
				}
			}
		}
		for (PosPair pair:toRemove){
			fireballs.remove(pair);
		}
	}
	public void checkMC(){//Check if MC is getting beat up
		for (Zombie a:allZombies)
		{
			if (a.collideMC())
			{
				BH.mc.setHealth(Math.max(BH.mc.getHealth()-a.getdmg(),0));
			}
		}
		for (Devil a:allDevils)
		{
			if (a.collideMC())
			{
				BH.mc.setHealth(Math.max(BH.mc.getHealth()-a.getdmg(),0));
			}
		}
		checkDeath();
	}
	public void switchWeapon(){
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
	public void generateEnemy(){
		if (ZombiesThisLevel>0){//generating enemies at specific locations (rects)
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
		for (int i=0; i!=10; ++i){
			int x1 = x + (int)(Math.random()*sx);//random position in the rect
			int y1 = y + (int)(Math.random()*sy);
			if (numbercollisions(x1,y1) == 0){
				ZombiesThisLevel--;
				allZombies.add(new Zombie(x1,y1,0,BH.mc));
				return;
			}
		}
	}
	public void addDevil(int x, int y, int sx, int sy){
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
	
	public void checkBoxCollision(){
		ArrayList<MagicalBox> toRemove = new ArrayList<MagicalBox>();
		for (int i=0;i<allBoxes.size();i++){
			MagicalBox box = allBoxes.get(i);
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
		if (item==HEALTH){
			System.out.println("HEALTH");
			BH.mc.setHealth(Math.min(BH.mc.getHealth()+500,1000));
		}
		else{
			BH.mc.addAmmo(item);
		}
	}
	public void moveMap()
	{//the shifting of the map
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
	
	public void updateposition(){//after shifting, move all the objects as well
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
	
	public boolean validMove(int x, int y){//If the pixel is valid in the mask
		if (mapx+x >=2000 || mapy+y >= 2000){
			return false;
		}
		if (mapx+x>=0&& mapy+y>=0){
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
	public int getZombiesThisLevel(){//# of zombies for the level
		return (currentLevel+2)*20;
	}
	public int getDevilsThisLevel(){
		return (currentLevel+2)*15;
	}
	public void checkLevelOver(){//TBH If you can beat level 1 you're already beast
		if (ZombiesDead == getZombiesThisLevel() && DevilsDead == getDevilsThisLevel()){
			//this level is over
			levelUp();
		}
	}
	public void levelUp(){
		BH.enemyGenerationTimer = new Timer(spawnsp-currentLevel*300, BH);
		currentLevel++;
		ZombiesDead=0;
		DevilsDead=0;
		ZombiesThisLevel=getZombiesThisLevel();
		DevilsThisLevel=getDevilsThisLevel();
		displayLevelCounter=200;
	}
	public void checkObjects(){
		checkSentry();
		checkBarricades();
		checkBarrels();
		checkExplosions();
		checkDead();
	}
	private void checkExplosions(){//counts how long the explosions lasts
		ArrayList<Explosion> toRemove = new ArrayList<Explosion>();
		for (Explosion b : allExplosions){
			b.incrementtime();
			if (b.getctime() >= 10){
				toRemove.add(b);
			}
		}
		for (Explosion b : toRemove){
			allExplosions.remove(b);
		}
		
	}
	private void checkSentry(){//if sentry is dead or out of ammo
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
	
	private void checkBarricades(){//if barricade has been destroyed
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

	private void checkBarrels(){
		ArrayList<Barrel> toRemove = new ArrayList<Barrel>();
		for (int x=1; x!=4; ++x){
			for (Barrel b1 : allBarrels){//Chain explosion
				if (b1.getHealth() <= 0){
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
			if (b.getHealth() <= 0){
				toRemove.add(b);
				barrelExplodeAudio.play();
			}
		}
		
		
		for (Barrel b : toRemove){
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
	public void checkDead(){
		//this method is called to see if the character's bullets damage the enemies
		ArrayList<Zombie> toRemoveZ = new ArrayList<Zombie>();
		for (Zombie z : allZombies){
			if (z.getHealth() <= 0){
				toRemoveZ.add(z);
			}
		}

		for (Zombie zzh8829:toRemoveZ){
			//Every once in a while a magical unicorn drops by after a zombie dies
			//And lays a magicalbox
			if ((int)(Math.random()*3)==1){
				allBoxes.add(new MagicalBox(zzh8829.getX(),zzh8829.getY(),BH));
			}
			BH.score+=200+consecutiveKills*100;
			ZombiesDead++;
			allZombies.remove(zzh8829);
			fullCountDown();
			addConsecutive();
		}
		ArrayList<Devil> toRemoveD = new ArrayList<Devil>();
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
	public void shootSentry(){//Sentries shoot at closest enemy
		for (SentryGun sentry : allSentries){
			int x = sentry.getcx(), y = sentry.getcy();
			Zombie zClosest = new Zombie(0,0,0,BH.mc);
			Devil dClosest = new Devil(0,0,0,BH.mc);
			double zd = 1000000000, dd = 1000000000;
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
	public void countdownGrenade(){
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
	public boolean circleRectangleCollision(int cx,int cy, int cr, int rx,int ry,int rl,int rw){
		boolean flag=false;
		if(rx<=cx && cx<=rx+rw && ry<=cy && cy<=ry+rl){
			flag=true;
		}
		else if (dist(rx,ry,cx,cy)<=cr){
			flag=true;
		}
		else if (dist(rx,ry+rl,cx,cy)<=cr){
			flag=true;
		}
		else if (dist(rx+rw,ry,cx,cy)<=cr){
			flag=true;
		}
		else if (dist(rx+rw,ry+rl,cx,cy)<=cr){
			flag=true;
		}
		return flag;
	}
	public void GrenadeExplode(Grenade grenade){
		for (Devil devil: allDevils){
			if (circleRectangleCollision(grenade.getX(),grenade.getY(),grenade.getdmgrange(),devil.getX(),devil.getY(),devil.getsx(),devil.getsy())){
				System.out.println("devil = dead"+dist(devil.getX(),devil.getY(),grenade.getX(),grenade.getY()));
				devil.setHealth(devil.getHealth()-grenade.getdmg());
			}
		}
		for (Zombie zombie: allZombies){
			if (circleRectangleCollision(grenade.getX(),grenade.getY(),grenade.getdmgrange(),zombie.getX(),zombie.getY(),zombie.getsx(),zombie.getsy())){
				System.out.println("zombie = dead"+dist(zombie.getX(),zombie.getY(),grenade.getX(),grenade.getY()));
				zombie.setHealth(zombie.getHealth()-grenade.getdmg());
			}
		}
		if (circleRectangleCollision(grenade.getX(),grenade.getY(),grenade.getdmgrange(),BH.mc.getX(),BH.mc.getY(),BH.mc.getsx(),BH.mc.getsy())){
			System.out.println("character = dead"+dist(BH.mc.getX(),BH.mc.getY(),grenade.getX(),grenade.getY()));
			BH.mc.setHealth(BH.mc.getHealth()-grenade.getdmg());
		}
		for (Barrel bar : allBarrels){
			int x = bar.getX(), y = bar.getY(), sx = bar.getsx(), sy = bar.getsy();
			if (circleRectangleCollision(grenade.getX(),grenade.getY(),grenade.getdmgrange(),x,y,sx,sy)){
				bar.setHealth(0);
			}
		}
		
	}
	public void PostGrenadeExplosion(){
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
	public double dist(int x1, int y1,int x2,int y2){
		return Math.pow(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2),0.5);
	}
	public void checkMagicalBox(){
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
		if (boxCountDown!=0){
			boxCountDown--;
		}
		else{
			boxString="";
		}
	}
	public void paintComponent(Graphics g){
		
		g.drawImage(background, -mapx, -mapy, this);
		
		//System.out.println(BH.mc.getWeapon()+"WEAPON AMMO"+BH.mc.getAmmo(BH.mc.getWeapon()));
		//drawing the health bar
		//figure out the colouring of the bar ugh
		g.setColor(Color.green);
		g.fillRect(BH.mc.getX()-5,BH.mc.getY()-3,BH.mc.calculateHealth(),5); //filling of the bar
		g.setColor(Color.black);
		g.drawRect(BH.mc.getX()-5, BH.mc.getY()-3, 30, 5); //drawing the outline
		
		for (PosPair pp : activeBullets){
			//we need to get bulllet sprites
			g.drawImage(bulletSprites[pp.getANGLE()%360/45],pp.getX(),pp.getY(),this);
			///g.drawImage(BH.bulletSprites.get(BH.activeBullets.get(i).getTYPE()),BH.activeBullets.get(i).getX(),BH.activeBullets.get(i).getY(),this);
			//g.drawOval(pp.getX(), pp.getY(), 20, 20);
		}
		for (PosPair pp : fireballs){
			g.drawImage(fireballSprites[pp.getANGLE()%360/45],pp.getX(),pp.getY(),this);
			//g.drawOval(pp.getX(), pp.getY(), 20, 20);
		}
		for (Zombie a : allZombies){
			g.drawRect(a.getX(),a.getY(),a.getsx(), a.getsy());
		}
		for (Devil a : allDevils){
			g.drawRect(a.getX(),a.getY(),a.getsx(), a.getsy());
		}
		for (MagicalBox b : allBoxes){
			b.countDown();
			g.drawImage(boxSprite,b.getX(),b.getY(),this);
		}
		checkMagicalBox();
		g.setColor(new Color (255,0,0));
		g.drawImage(charSprites[BH.mc.getANGLE()/45][spriteCounter%3],BH.mc.getX(),BH.mc.getY(),this);
		for (Zombie a : allZombies){
			g.drawImage(zombieSprites[(a.getAngle()+22)%360/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		for (Devil a : allDevils){
			g.drawImage(devilSprites[(a.getAngle()+22)%360/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		for (MagicalBox box: allBoxes){
			g.setColor(Color.BLUE);
			g.drawRect(box.getX()-5, box.getY()-5, 10, 10);
		}
		g.drawOval(BH.mc.getX(), BH.mc.getY(), 2, 2);
		
		for (Barricade bar : allBarricades){
			g.drawImage(barricadeSprite, bar.getX(), bar.getY(), this);
		}
		for (Barrel bar : allBarrels){
			g.drawImage(barrelSprite,bar.getX(),bar.getY(),this);
		}
		for (SentryGun bar : allSentries){
			g.drawRect(bar.getX(), bar.getY()-10, 20, 20);
			g.drawRect(bar.getX()+5, bar.getY()-5, 10, 10);
		}
		for (Grenade grenade: allGrenades){
			//-grenadeSprite.getHeight(this)/2,ny-grenadeSprite.getWidth(this)/2
			g.drawImage(grenadeSprite,grenade.getX()-grenadeSprite.getHeight(this)/2, grenade.getY()-grenadeSprite.getWidth(this)/2, this);
		}
		for (Grenade grenade: explodedGrenade){
			g.drawImage(grenadeExploded, grenade.getX()-grenadeExploded.getHeight(this)/2,grenade.getY()-grenadeExploded.getWidth(this)/2,this);
			g.drawOval(grenade.getX()-25, grenade.getY()-25, 50, 50);
		}
		for (Explosion exp : allExplosions){		
			g.drawImage(barrelExplosion,exp.getX()-exp.getrange()/2,exp.getY()-exp.getrange()/2,this);		
			g.drawOval(exp.getX()-exp.getrange()/2,exp.getY()-exp.getrange()/2,100,100);		
		}
		boxCount();
		
		g.setColor(Color.black);
		g.setFont(SMALLfont);
		g.drawString(weaponNames[BH.mc.getWeapon()]+" "+BH.mc.getAmmo(BH.mc.getWeapon()), BH.mc.getX()-5, BH.mc.getY()-10); //maybe do the string formatting with this later if we have time
		g.setFont(font);
		g.drawString(boxString,100,500+boxCountDown);
		g.drawString(printUpgradeString, 300, 500+UpgradeStringCountDown);
		if (displayLevelCounter>0){
			displayLevelCounter--;
			g.drawString("+-+-+-+ "+currentLevel+" +-+-+-+", 500, 500+displayLevelCounter/2);
		}
		g.setFont(LARGEfont);
		g.drawString(consecutiveKills+"",670,100);
		g.drawString(BH.score+"", 20, 100);
		g.setColor(new Color(225-consecutiveCountDown,225-consecutiveCountDown,225-consecutiveCountDown));
		g.drawString(consecutiveKills+"",670,100);
		lastSpaceStat=keys[KeyEvent.VK_SPACE];
		
	}
}
