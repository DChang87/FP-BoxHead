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

// Paul Krishnamurthy is a noob


public class GamePanel extends JPanel implements KeyListener{
	private AudioClip[] audio = new AudioClip[6];
	private BoxHead BH;
	private boolean[] keys; 
	private Image background = new ImageIcon("forestmap2.jpg").getImage();
	private Image boxSprite = new ImageIcon("box.png").getImage();
	
	private BufferedImage mask_background;
	
	private Image[][] charSprites = new Image[8][3];
	private Image[][] zombieSprites = new Image[8][8];
	private int spriteCounter = 0;
	private int[] weapondist = new int[30];
	private int[] weaponsp = new int[30];
	private int[] ORIGINALweaponsp = new int[30];
	private int rectsx = 34, rectsy = 47, barricadesx = 20, barricadesy = 20, barrelsx = 20, barrelsy = 50;
	final int HEALTH=0,PISTOL=1,UZI=2,PISTOLP=11,SHOTGUN=3,UZIP=21,BARREL = 4,UZIPP=22,GRENADE= 5, FAKEWALLS=6;
	private ArrayList<Zombie> allZombies = new ArrayList<Zombie>(); //this stores all of the zombies that are currently in the game
	private ArrayList<Devil> allDevils = new ArrayList<Devil>(); //this stores all of the devils that are currently running around in the game
	public ArrayList<PosPair> fireballs = new ArrayList<PosPair>(); //this stores all of the fireballs that are currently in the game
	//make an arraylist of active bullets that save the info about the bullet including the type of gun
	private ArrayList<PosPair> activeBullets = new ArrayList<PosPair>(); //private?
	private ArrayList<MagicalBox> allBoxes = new ArrayList<MagicalBox>();
	private ArrayList<Image> bulletSprites = new ArrayList<Image>();
	private ArrayList<Barricade> allBarricades = new ArrayList<Barricade>();
	private ArrayList<Barrel> allBarrels = new ArrayList<Barrel>();
	private ArrayList<Explosion> allExplosions = new ArrayList<Explosion>();
	private int ZombiesThisLevel, DevilsThisLevel;

	//add this
	private int displayLevelCounter=0; //this is the counter used to display the "+-+-+-+ Level 2 +-+-+-+"
	private int currentLevel=0;
	
	
	private int mapx=0, mapy=0, mapsx = 2000, mapsy = 2000, bx1 = 100, bx2 = 670, by1 = 100, by2 = 510;
	private int screensx = 800, screensy = 640;
	
	private int shiftx = 0, shifty = 0;
	//map shift
	private int consecutiveKills=0;
	private int consecutiveCountDown=0;
	int nextUpgrade=0;

	private String[] weaponNames = new String[10];
	private int ang1=90,ang2=180; //generation spot 1, generation spot 2
	//private int gs1x=100,gs1y=100,gs2x=500,gs2y=500,ang1=0,ang2=270;
	
	///add
	private int ZombiesDead=0,DevilsDead=0;
	//stop
	
	
	public GamePanel(BoxHead bh){
		BH=bh;
		keys = new boolean[65535];
		addKeyListener(this);
		updateweapon();
		getWeaponNames();	
		
		loadAudio();
		loadSprites();
		loadMask();
		loadORWeaponSpeed();
		currentLevel=1;
		ZombiesThisLevel = getZombiesThisLevel();
		DevilsThisLevel = getDevilsThisLevel();
		generateEnemy();
	}
	public void loadORWeaponSpeed(){
		ORIGINALweaponsp[1]=5; //pistol
		ORIGINALweaponsp[2]=10; //uzi
		ORIGINALweaponsp[3]=10;//pistol
		loadWeaponSpeed();
	}
	public void loadWeaponSpeed(){
		for (int i=0;i<ORIGINALweaponsp.length;i++){
			weaponsp[i]=ORIGINALweaponsp[i];
		}
	}
	public void loadMask(){
		try{
			File file= new File("mask_map2.jpg");
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
			}
		}
	}
	public void loadAudio(){
		audio[1]=Applet.newAudioClip(getClass().getResource("pistol.wav"));
		//audio[2]=Applet.newAudioClip(getClass().getResource("uzi.wav"));
		//audio[3]=Applet.newAudioClip(getClass().getResource("shotgunS.wav"));
	}
	public void getWeaponNames(){
		weaponNames[1]="PISTOL";
		weaponNames[2]="UZI";
		weaponNames[3]="SHOTGUN";
		weaponNames[4]="BARREL";
		weaponNames[5]="GRENADE";
		weaponNames[6]="BARRICADE";
	}
	
	public void updateweapon(){
		//we keep track of how far the weapon can travel
		weapondist[1] = 400;
		weapondist[2] = 500;
		for (int i=3; i!=7; ++i)
			weapondist[i] = 600;
	}
	public void restart(){
		currentLevel=1;
		BH.mc.setHealth(BH.mc.full_health);
		ZombiesThisLevel=getZombiesThisLevel();
		DevilsThisLevel=getDevilsThisLevel();
		ZombiesDead=0;
		DevilsDead=0;
		allZombies.clear();
		allDevils.clear();
		fireballs.clear();
		activeBullets.clear();
		allBarricades.clear();
		allBarrels.clear();
		allExplosions.clear();
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
			if (BH.mc.getWeapon()==6){
				int nx = (int) (BH.mc.getcx() + 37*Math.cos(Math.toRadians(BH.mc.getANGLE())));
				int ny = (int) (BH.mc.getcy() + 37*Math.sin(Math.toRadians(BH.mc.getANGLE())));
				allBarricades.add(new Barricade(nx-barricadesx/2,ny-barricadesy/2));
			}
			else if (BH.mc.getWeapon()==4){
				int nx = (int) (BH.mc.getcx() + 60*Math.cos(Math.toRadians(BH.mc.getANGLE())));
				int ny = (int) (BH.mc.getcy() + 60*Math.sin(Math.toRadians(BH.mc.getANGLE())));
				allBarrels.add(new Barrel(nx - barrelsx/2, ny - barrelsy/2));
			}
			else{
				activeBullets.add(new PosPair(BH.mc.getX(),BH.mc.getY(),BH.mc.getANGLE(),BH.mc.getWeapon()));
				BH.mc.useAmmo(BH.mc.getWeapon());
				activateAudio(BH.mc.getWeapon());
			}
		}
	}
	public void activateAudio(int weapon){
		audio[weapon].play();
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
		int inx = (int)nx, iny = (int)ny;
		//there is something wrong with the pixels
		if (validMove(inx,BH.mc.getY()) && numbercollisions(inx,BH.mc.getY()) <= 1){
			BH.mc.setX(nx);
		}
		if (validMove(BH.mc.getX(),iny) && numbercollisions(BH.mc.getX(),iny) <= 1){
			BH.mc.setY(ny);
		}
	}	
	
	public void moveZombie(){
		for (int i=0; i< allZombies.size(); i++){
			Zombie temp = allZombies.get(i);
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
			for (Barricade b : allBarricades){
				if (b.rectcollision((int)nx,(int)ny)){
					b.setHealth(b.getHealth()-temp.getdmg());
				}
			}
			for (Barrel b : allBarrels){
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
	public void moveDevil(){
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
			for (Barrel b : allBarrels){
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
	public int numbercollisions(int x, int y){
		//collisions should max 1 (itself)
		int ncollision = 0;
		if (rectcollision(x,y,BH.mc.getX(),BH.mc.getY())){
			ncollision++;
		}
		for (Zombie z : allZombies){
			if (rectcollision(x,y,z.getX(),z.getY())){
				ncollision++;
			}
		}
		for (Devil d : allDevils){
			if (rectcollision(x,y,d.getX(),d.getY())){
				ncollision++;
			}
		}
		for (Barricade b : allBarricades){
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
	public boolean rectcollision(int x1, int y1, int x2, int y2){
		if (x1+rectsx < x2 || x1 > x2 + rectsx || y1 + rectsy < y2 || y1 > y2 + rectsy){
			return false;
		}
		return true;
	}
	
	public void fullCountDown(){consecutiveCountDown=250;}
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
		ArrayList<Zombie> toRemoveZ = new ArrayList<Zombie>();
		boolean flag = false;
		for (Zombie z : allZombies){
			if (z.getCollide(x,y)){
				flag = true;
				z.setHealth(z.getHealth() - 10);
			}
			if (z.getHealth() <= 0){
				toRemoveZ.add(z);
			}
		}

		for (Zombie zzh8829:toRemoveZ){
			//for every zombie that dies, add onto the score, remove the zombie and add a magical box to the screen
			if ((int)(Math.random()*3)==1){
				allBoxes.add(new MagicalBox(zzh8829.getX(),zzh8829.getY()));
			}
			BH.score+=200+consecutiveKills*100;
			ZombiesDead++;
			allZombies.remove(zzh8829);
			fullCountDown();
			addConsecutive();

			
		}
		ArrayList<Devil> toRemoveD = new ArrayList<Devil>();
		for (Devil d : allDevils){
			if (d.getCollide(x,y)){
				flag = true;
				d.setHealth(d.getHealth() - 10);
			}
			if (d.getHealth() <= 0){
				toRemoveD.add(d);
			}
		}
		for (Devil zzh8829:toRemoveD){
			BH.score+=200+consecutiveKills*100;
			allDevils.remove(zzh8829);
			allBoxes.add(new MagicalBox(zzh8829.getX(),zzh8829.getY()));
			fullCountDown();
			DevilsDead++;
			addConsecutive();
			
		}
		return flag;
	}
	public void addConsecutive(){
		consecutiveKills++;
		if (consecutiveKills==BH.ug.allUpgradesNum[nextUpgrade]){
			BH.ug.getUpgrade(nextUpgrade++);		
			
		}
	}
	public boolean checkOutside(int x,int y){
		return x<0||x>800||y<0||y>640;
	}
	public void checkBulletCollision(){
		//check if the bullets hit any enemies
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (int i=0;i<activeBullets.size();i++){
			if (enemyCollision(activeBullets.get(i).getX(),activeBullets.get(i).getY())){
				//REMOVE THE ENEMIES
				toRemove.add(activeBullets.get(i));
			}
			else if (checkOutside(activeBullets.get(i).getX(),activeBullets.get(i).getY())){
				toRemove.add(activeBullets.get(i));
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
			if ((a.getX()-a.getorigX())*(a.getX()-a.getorigX()) + (a.getY()-a.getorigY())*(a.getY()-a.getorigY()) > weapondist[a.getTYPE()]*weapondist[a.getTYPE()]){
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
	public void moveBullets(){
		for (int i=0;i<activeBullets.size();i++)
		{
			PosPair temp = activeBullets.get(i);
			final double ANG = Math.toRadians(temp.getANGLE());
			double xx = temp.getDX(), yy = temp.getDY();
			activeBullets.get(i).setPos(xx+weaponsp[BH.mc.getWeapon()]*Math.cos(ANG),yy+weaponsp[BH.mc.getWeapon()]*Math.sin(ANG));
		}
	}
	public void moveFireballs()
	{
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (PosPair temp : fireballs)
		{
			final double ANG = Math.toRadians(temp.getANGLE());
			double xx = temp.getDX(), yy = temp.getDY();
			temp.setPos(xx+20*Math.cos(ANG),yy+20*Math.sin(ANG));
			if (checkCollision(temp.getX(),temp.getY(),BH.mc.getX(),BH.mc.getY())){
				//decrease health by 15 points per fireball
				BH.mc.setHealth(BH.mc.getHealth()-temp.getdmg());
				toRemove.add(temp);
			}
			else if (checkOutside(temp.getX(),temp.getY())){
				toRemove.add(temp);
			}
		}
		for (PosPair pair:toRemove){
			fireballs.remove(pair);
		}
	}
	public void checkMC(){
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
	}
	public void generateEnemy(){
		//level 1 (1-4)
		//level 2 (2-6)
		//level 3 (3-8)
		//level 4 (4-11)
		//level 5 (5-13)
		int maxZ=1,minZ=1,maxD=1,minD=1;
		int generateZ,generateD; //the number of zombies and devils generated at this time
		
		int posZ,posD;
		if (ZombiesThisLevel>0){
			int n = (int)(Math.random()*(Math.min(ZombiesThisLevel-minZ, maxZ-minZ)));
			generateZ = (int)(Math.random()*(Math.min(ZombiesThisLevel-minZ, maxZ-minZ)))+minZ;
			//randomly generate the number of Zombies needed based on the maximum Zombie generation allowed for this level and the the remaining allowance of zombie for the level
			//add on the minimum zombie generatoin allowed to ensure that enough zombies are generated
			ZombiesThisLevel-=generateZ;
			posZ = (int)(Math.random()*2);
			int y;
			if (posZ==0){
				for (int i=0;i<generateZ;i++){
					y = 1279+(int)(Math.random()*182)*((int)(Math.random()*2)-1);
					allZombies.add(new Zombie(-mapx,-mapy+y,ang1,BH.mc));
				}
			}
			else{
				//the number of zombies and devils required for each spot
				for (int i=0;i<generateZ;i++){
					y = (int)(Math.random()*490);
					allZombies.add(new Zombie(-mapx,-mapy+y,ang2,BH.mc));
				}
			}
			

		}
		if (DevilsThisLevel>0){
			generateD = (int)(Math.random()*(Math.min(DevilsThisLevel-minD, maxD-minD)))+minD;
			DevilsThisLevel-=generateD;
			posD = (int)(Math.random()*2);
			if (posD==0){
				for (int i=0;i<generateD;i++){
					allDevils.add(new Devil(-mapx,-mapy+1279+(int)(Math.random()*182)*((int)(Math.random()*2)-1),ang1,BH.mc));
				}
			}
			else{
				for (int i=0;i<generateD;i++){
					allDevils.add(new Devil(-mapx,-mapy+(int)(Math.random()*490),ang2,BH.mc));
				}
			}
			
		}
	}
	
	public void checkBoxCollision(){
		ArrayList<MagicalBox> toRemove = new ArrayList<MagicalBox>();
		for (int i=0;i<allBoxes.size();i++){
			MagicalBox box = allBoxes.get(i);
			int x = box.getX(), y = box.getY(), mcx = BH.mc.getX(), mcy = BH.mc.getY();
			if (x + box.bsx < mcx || x > mcx + BH.mc.getsx() || y + box.bsy < mcy || y > mcy + BH.mc.getsy()){
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
			BH.mc.setHealth(Math.min(BH.mc.getHealth()+500,1000));
		}
		else{
			BH.mc.addAmmo(item);
		}
	}
	public void moveMap()
	{
		shiftx = shifty = 0;
		int mcx = BH.mc.getX(), mcy = BH.mc.getY(); //character position
		if (mcx < bx1 && mapx!=0){ //checks if beyond boundary and if need of shifiting
			mapx = mapx - (bx1 - mcx);
			shiftx =  - (bx1 - mcx);
			mcx = bx1;
		}
		else if (mcx > bx2 && mapx!=mapsx-screensx){
			mapx = mapx + (mcx - bx2);
			shiftx = (mcx - bx2);
			mcx = bx2;
		}
		if (mcy < by1 && mapy!=0){
			mapy = mapy - (by1 - mcy);
			shifty =  - (by1 - mcy);
			mcy = by1;
		}
		else if (mcy > by2 && mapy!=mapsy-screensy){
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
			a.setX(a.getX()-shiftx);
			a.setY(a.getY()-shifty);
		}
		for (Barricade a : allBarricades){
			a.setX(a.getX()-shiftx);
			a.setY(a.getY()-shifty);
		}
		for (Barrel a : allBarrels){
			a.setX(a.getX()-shiftx);
			a.setY(a.getY()-shifty);
		}
	}
	
	public boolean validMove(int x, int y){
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
	public int getZombiesThisLevel(){
		return currentLevel*10-5;
	}
	public int getDevilsThisLevel(){
		return (currentLevel-1)*3+2;
	}
	public void checkLevelOver(){
		if (ZombiesDead == getZombiesThisLevel() && DevilsDead == getDevilsThisLevel()){
			//this level is over
			levelUp();
		}
	}
	public void levelUp(){
		currentLevel++;
		ZombiesDead=0;
		DevilsDead=0;
		ZombiesThisLevel=getZombiesThisLevel();
		DevilsThisLevel=getDevilsThisLevel();
		displayLevelCounter=1000;
	}
	public void checkBarricades(){
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
	
	public void checkBarrels(){
		ArrayList<Barrel> toRemove = new ArrayList<Barrel>();
		for (Barrel b : allBarrels){
			if (b.getHealth() <= 0){
				toRemove.add(b);
			}
		}
		for (Barrel b : toRemove){
			Explosion eee=new Explosion(b.getX(), b.getY());
			allExplosions.add(eee);
			explosiondmg(eee);
			allBarrels.remove(b);
		}
	}
	public void explosiondmg(Explosion exp){
		int x = exp.getX(), y = exp.getY();
		
		for (Zombie temp : allZombies){
			int x2 = temp.getX(), y2 = temp.getY();
			if ((x2-x)*(x2-x) + (y2-y)*(y2-y) <= exp.getrange()*exp.getrange()){
				temp.setHealth(temp.getHealth() - exp.getdmg());
				System.out.println("WTF");
			}
		}
		for (Devil temp : allDevils){
			int x2 = temp.getX(), y2 = temp.getY();
			if ((x2-x)*(x2-x) + (y2-y)*(y2-y) <= exp.getrange()*exp.getrange()){
				temp.setHealth(temp.getHealth() - exp.getdmg());
				System.out.println("WTF");
			}
		}
		int x2 = BH.mc.getX(), y2 = BH.mc.getY();;
		if ((x2-x)*(x2-x) + (y2-y)*(y2-y) <= exp.getrange()*exp.getrange()){
			BH.mc.setHealth(BH.mc.getHealth() - exp.getdmg());
			System.out.println("WTF");
		}
		
	}
	public void checkExplosions(){
		ArrayList<Explosion> toRemove = new ArrayList<Explosion>();
		for (Explosion b : allExplosions){
			if (b.getspritenum() == 5){
				toRemove.add(b);
			}
		}
		for (Explosion b : toRemove){
			allExplosions.remove(b);
		}
		
	}
	public void removeDead(){
		//this method is called to see if the character's bullets damage the enemies
		ArrayList<Zombie> toRemoveZ = new ArrayList<Zombie>();
		for (Zombie z : allZombies){
			if (z.getHealth() <= 0){
				toRemoveZ.add(z);
			}
		}

		for (Zombie zzh8829:toRemoveZ){
			//for every zombie that dies, add onto the score, remove the zombie and add a magical box to the screen
			if ((int)(Math.random()*3)==1){
				allBoxes.add(new MagicalBox(zzh8829.getX(),zzh8829.getY()));
			}
			BH.score+=200+consecutiveKills*100;
			ZombiesDead++;
			allZombies.remove(zzh8829);
			fullCountDown();
			consecutiveKills++;
			
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
			allBoxes.add(new MagicalBox(zzh8829.getX(),zzh8829.getY()));
			fullCountDown();
			DevilsDead++;
			consecutiveKills++;
			
		}
	}
	
	public void paintComponent(Graphics g){
		Font Sfont = new Font("Calisto MT", Font.BOLD, 20);
		g.setFont(Sfont);
		g.drawImage(background, -mapx, -mapy, this);
		g.drawString(weaponNames[BH.mc.getWeapon()], BH.mc.getX()-5, BH.mc.getY()-10); //maybe do the string formatting with this later if we have time
		//drawing the health bar
		//figure out the colouring of the bar ugh
		g.drawRect(BH.mc.getX()-5,BH.mc.getY()-3,BH.mc.calculateHealth(),5); //filling of the bar
		g.drawRect(BH.mc.getX()-5, BH.mc.getY()-3, 20, 5); //drawing the outline
		
		for (PosPair pp : activeBullets){
			//we need to get bulllet sprites
			///g.drawImage(BH.bulletSprites.get(BH.activeBullets.get(i).getTYPE()),BH.activeBullets.get(i).getX(),BH.activeBullets.get(i).getY(),this);
			g.drawOval(pp.getX(), pp.getY(), 20, 20);
		}
		for (PosPair pp : fireballs){
			g.drawOval(pp.getX(), pp.getY(), 20, 20);
		}
		for (Zombie a : allZombies){
			g.drawRect(a.getX(),a.getY(),a.getsx(), a.getsy());
		}
		for (Devil a : allDevils){
			g.drawRect(a.getX(),a.getY(),a.getsx(), a.getsy());
		}
		for (MagicalBox b : allBoxes){
			g.drawImage(boxSprite,b.getX(),b.getY(),this);
		}
		g.setColor(new Color (255,0,0));
		g.drawImage(charSprites[BH.mc.getANGLE()/45][spriteCounter%3],BH.mc.getX(),BH.mc.getY(),this);
		for (Zombie a : allZombies){
			g.drawImage(zombieSprites[(a.getAngle()+22)%360/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		for (Devil a : allDevils){
			g.drawImage(zombieSprites[(a.getAngle()+22)%360/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		for (MagicalBox box: allBoxes){
			g.setColor(Color.BLUE);
			g.drawRect(box.getX()-5, box.getY()-5, 10, 10);
		}
		g.drawOval(BH.mc.getX(), BH.mc.getY(), 2, 2);
		if (displayLevelCounter>0){
			displayLevelCounter--;
			g.drawString("+-+-+-+ "+currentLevel+" +-+-+-+", 400, 600);
		}
		for (Barricade bar : allBarricades){
			g.drawRect(bar.getX(), bar.getY()-10, 20, 20);
		}
		for (Barrel bar : allBarrels){
			g.drawRect(bar.getX(), bar.getY()-10, 20, 50);
		}
		g.drawString(consecutiveKills+" "+consecutiveCountDown,100,600);
	}
}
