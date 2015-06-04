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
	private BoxHead BH;
	private boolean[] keys; 
	private Image background = new ImageIcon("forestmap.jpg").getImage();
	private Image boxSprite = new ImageIcon("box.png").getImage();
	
	private BufferedImage mask_background;
	
	private Image[][] charSprites = new Image[8][3];
	private Image[][] zombieSprites = new Image[8][8];
	private int spriteCounter = 0;
	private int[] weapondist = new int[30];
	private int rectsx = 30, rectsy = 70;
	final int HEALTH=0,PISTOL=1,UZI=2,PISTOLP=11,SHOTGUN=3,UZIP=21,BARREL = 4,UZIPP=22,GRENADE= 5, FAKEWALLS=6;
	private ArrayList<Zombie> allZombies = new ArrayList<Zombie>(); //this stores all of the zombies that are currently in the game
	private ArrayList<Devil> allDevils = new ArrayList<Devil>(); //this stores all of the devils that are currently running around in the game
	public ArrayList<PosPair> fireballs = new ArrayList<PosPair>(); //this stores all of the fireballs that are currently in the game
	//make an arraylist of active bullets that save the info about the bullet including the type of gun
	private ArrayList<PosPair> activeBullets = new ArrayList<PosPair>(); //private?
	private ArrayList<MagicalBox> allBoxes = new ArrayList<MagicalBox>();
	private ArrayList<Image> bulletSprites=new ArrayList<Image>();
	private int ZombiesThisLevel=10, DevilsThisLevel=3;

	private int mapx=0, mapy=0, mapsx = 2000, mapsy = 2000, bx1 = 100, bx2 = 670, by1 = 100, by2 = 510;
	private int screensx = 800, screensy = 640;
	
	private int shiftx = 0, shifty = 0;
	//map shift
	
	
	//add this
	private String[] weaponNames = new String[10];
	//private int gs1x=72,gs1y=1273,gs2x=905,gs2y=73,ang1=90,ang2=180; //generation spot 1, generation spot 2
	private int gs1x=100,gs1y=100,gs2x=500,gs2y=500,ang1=0,ang2=270;
	//stop this
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
		generateEnemy();
		updateweapon();
		weaponNames[1]="pistol";
		weaponNames[2]="UZI";
		weaponNames[3]="shotgun";
		
		try{
			File file= new File("map.jpg");
			mask_background = ImageIO.read(file);
		}
		catch (IOException ex){}
		
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
			activeBullets.add(new PosPair(BH.mc.getX(),BH.mc.getY(),BH.mc.getANGLE(),BH.mc.getWeapon()));
			BH.mc.useAmmo(BH.mc.getWeapon());
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
		int inx = (int)nx, iny = (int)ny;
		//there is something wrong with the pixels
		if (vMove(inx,iny) && numbercollisions(inx,iny) <= 1){
			BH.mc.setX(nx);
			BH.mc.setY(ny);
		}
	}
	public boolean vMove(int inx, int iny){
		if (validMove(inx,iny) && validMove(inx, iny+rectsy)){
			return true;
		}
		return false;
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
			if (numbercollisions((int)nx, temp.getY()) <= 1 && vMove((int)nx,temp.getY())){
				temp.setX(nx);
			}
			if (numbercollisions(temp.getX(), (int)ny) <= 1 && vMove(temp.getX(),(int)ny)){
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
			if (numbercollisions((int)nx, temp.getY()) <= 1 && vMove((int)nx,temp.getY())){
				temp.setX(nx);
			}
			if (numbercollisions(temp.getX(), (int)ny) <= 1 && vMove(temp.getX(),(int)ny)){
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
		for (int i=0; i<allZombies.size(); i++){
			if (allZombies.get(i).getCollide(x,y)){
				flag = true;
				allZombies.get(i).setHealth(allZombies.get(i).getHealth() - 10);
			}
			if (allZombies.get(i).getHealth() <= 0){
				toRemoveZ.add(allZombies.get(i));
			}
		}

		for (Zombie zzh8829:toRemoveZ){
			//for every zombie that dies, add onto the score, remove the zombie and add a magical box to the screen
			allBoxes.add(new MagicalBox(zzh8829.getX(),zzh8829.getY()));
			
			BH.score+=200;
			allZombies.remove(zzh8829);
		}
		ArrayList<Devil> toRemoveD = new ArrayList<Devil>();
		for (int i=0;i<allDevils.size();i++){
			if (allDevils.get(i).getCollide(x,y)){
				flag = true;
				allDevils.get(i).setHealth(allDevils.get(i).getHealth() - 10);
			}
			if (allDevils.get(i).getHealth() <= 0){
				toRemoveD.add(allDevils.get(i));
			}
		}
		for (Devil zzh8829:toRemoveD){
			BH.score+=200;
			allDevils.remove(zzh8829);
			allBoxes.add(new MagicalBox(zzh8829.getX(),zzh8829.getY()));
		}
		//remember to check the devils to
		System.out.println("Boxes"+allBoxes.size());
		return flag;
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
			activeBullets.get(i).setPos(xx+10*Math.cos(ANG),yy+10*Math.sin(ANG));
		}
	}
	public void moveFireballs()
	{
		ArrayList<PosPair> toRemove = new ArrayList<PosPair>();
		for (int i=0;i<fireballs.size();i++)
		{
			PosPair temp = fireballs.get(i);
			final double ANG = Math.toRadians(temp.getANGLE());
			double xx = temp.getDX(), yy = temp.getDY();
			fireballs.get(i).setPos(xx+20*Math.cos(ANG),yy+20*Math.sin(ANG));
			if (checkCollision(fireballs.get(i).getX(),fireballs.get(i).getY(),BH.mc.getX(),BH.mc.getY())){
				//decrease health by 15 points per fireball
				BH.mc.setHealth(BH.mc.getHealth()-15);
				toRemove.add(temp);
			}
			else if (checkOutside(fireballs.get(i).getX(),fireballs.get(i).getY())){
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
				BH.mc.setHealth(BH.mc.getHealth()-10);
			}
		}
		for (Devil a:allDevils)
		{
			if (a.collideMC())
			{
				BH.mc.setHealth(BH.mc.getHealth()-10);
			}
		}
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
	}
	//add this
	public void generateEnemy(){
		//level 1 (1-3)
		//level 2 (2-5)
		//level 3 (3-7)
		//level 4 (4-9)
		//level 5 (5-11)
		int maxZ=1,minZ=3,maxD=0,minD=1;
		int generateZ,generateD; //the number of zombies and devils generated at this time
		int pos1Z,pos2Z,pos1D,pos2D;
		if (ZombiesThisLevel>0){
			generateZ = (int)(Math.random()*(Math.min(ZombiesThisLevel-minZ, maxZ-minZ))+minZ);
			//randomly generate the number of Zombies needed based on the maximum Zombie generation allowed for this level and the the remaining allowance of zombie for the level
			//add on the minimum zombie generatoin allowed to ensure that enough zombies are generated
			ZombiesThisLevel-=generateZ;
			pos1Z = generateZ/2;
			pos2Z = generateZ-pos1Z;
			for (int i=0;i<pos1Z;i++){
				allZombies.add(new Zombie(gs1x+(int)(Math.random()*60-30),gs1y+(int)(Math.random()*60-30),ang1,BH.mc));
			}
			//the number of zombies and devils required for each spot
			for (int i=0;i<pos2Z;i++){
				allZombies.add(new Zombie(gs2x+(int)(Math.random()*60-30),gs2y+(int)(Math.random()*60-30),ang2,BH.mc));
			}

		}
		if (DevilsThisLevel>0){
			generateD = (int)(Math.random()*(Math.min(DevilsThisLevel-minD, maxD-minD))+minD);
			DevilsThisLevel-=generateD;
			pos1D = generateD/2;
			pos2D = generateD-pos1D;
			for (int i=0;i<pos1D;i++){
				allDevils.add(new Devil(gs1x+(int)(Math.random()*60-30),gs1y+(int)(Math.random()*60-30),ang1,BH.mc));
			}
			for (int i=0;i<pos2D;i++){
				allDevils.add(new Devil(gs2x+(int)(Math.random()*60-30),gs2y+(int)(Math.random()*60-30),ang2,BH.mc));
			}
		}		
	}
	
	public void checkBoxCollision(){
		//System.out.println("BOX"+allBoxes.size());
		ArrayList<MagicalBox> toRemove = new ArrayList<MagicalBox>();
		for (int i=0;i<allBoxes.size();i++){
			MagicalBox box = allBoxes.get(i);
			//if (x1+rectsx1 < x2 || x1 > x2 + rectsx2 || y1 + rectsy1 < y2 || y1 > y2 + rectsy2)
			//if (BH.mc.getX()+BH.mc.getWidth()<box.getX()||BH.mc.getX()>box.getX()+sizex||BH.mc.getY()+BH.mc.getLength()<box.getY()||BH.mc.getY()>box.getY()+sizey){
				//if they do collide
				//System.out.println("Collisionnnn"+BH.mc.getHealth());
				//addItem(box.generateItem());
				//toRemove.add(box);
			//}
			int x = box.getX(), y = box.getY(), mcx = BH.mc.getX(), mcy = BH.mc.getY();
			if (x + box.bsx < mcx || x > mcx + BH.mc.getsx() || y + box.bsy < mcy || y > mcy + BH.mc.getsy()){
				continue;
			}
			System.out.println("Collisionnnn"+BH.mc.getHealth());
			addItem(box.generateItem());
			toRemove.add(box);
		}
		for (MagicalBox box:toRemove){
			System.out.println("REMOVE");
			allBoxes.remove(box);
		}
		
	}
	public void addItem(int item){
		System.out.println("ITEM ADDED");
		if (item==HEALTH){
			BH.mc.setHealth(Math.min(BH.mc.getHealth()+500,1000));
		}
		else{
			BH.mc.addAmmo(item);
			System.out.println("AMMO ADDED");
		}
	}
	public void moveMap()
	{
		shiftx = shifty = 0;
		int mcx = BH.mc.getX(), mcy = BH.mc.getY(); //character position
		if (mcx < bx1 && mapx!=0){ //checks if beyond boundarym and if need of shifiting
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
	}
	
	public boolean validMove(int x, int y){
		int clr=  mask_background.getRGB(mapx+x,mapy+y);
		int  red   = (clr & 0x00ff0000) >> 16;
		int  green = (clr & 0x0000ff00) >> 8;
		int  blue  =  clr & 0x000000ff;
		System.out.println(red+" "+green+" "+blue);
		if (red == 255 && green == 255 && blue == 255){
			return true;
		}
		return false;
	}
	
	
	//stop	
	public void paintComponent(Graphics g){
		Font Sfont = new Font("Calisto MT", Font.BOLD, 20);
		g.setFont(Sfont);
		g.drawImage(background, -mapx, -mapy, this);
		g.drawString(weaponNames[BH.mc.getWeapon()], BH.mc.getX()-5, BH.mc.getY()-10); //maybe do the string formatting with this later if we have time
		//drawing the health bar
		//figure out the colouring of the bar ugh
		g.drawRect(BH.mc.getX()-5,BH.mc.getY()-3,BH.mc.calculateHealth(),5); //filling of the bar
		g.drawRect(BH.mc.getX()-5, BH.mc.getY()-3, 20, 5); //drawing the outline
		
		for (int i=0;i<activeBullets.size();i++){
			//we need to get bulllet sprites
			///g.drawImage(BH.bulletSprites.get(BH.activeBullets.get(i).getTYPE()),BH.activeBullets.get(i).getX(),BH.activeBullets.get(i).getY(),this);
			g.drawOval(activeBullets.get(i).getX(), activeBullets.get(i).getY(), 20, 20);
			//we need to move the bullets...either call a function here or call it from the BoxHead class
		}
		for (int i=0;i<fireballs.size();i++){
			g.drawOval(fireballs.get(i).getX(), fireballs.get(i).getY(), 20, 20);
		}
		for (int i=0;i<allZombies.size();i++){
			Zombie a = allZombies.get(i);
			g.drawRect(a.getX(),a.getY(),a.getsx(), a.getsy());
		}
		for (int i=0;i<allDevils.size();i++){
			Devil a = allDevils.get(i);
			g.drawRect(a.getX(),a.getY(),a.getsx(), a.getsy());
		}
		for (int i=0;i<allBoxes.size();i++){
			MagicalBox b = allBoxes.get(i);
			g.drawImage(boxSprite,b.getX(),b.getY(),this);
		}
		g.setColor(new Color (255,0,0));
		g.drawImage(charSprites[BH.mc.getANGLE()/45][spriteCounter%3],BH.mc.getX(),BH.mc.getY(),this);
		for (int i=0;i<allZombies.size();i++){
			Zombie a = allZombies.get(i);
			g.drawImage(zombieSprites[(a.getAngle()+22)%360/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		for (Devil a : allDevils){
			g.drawImage(zombieSprites[(a.getAngle()+22)%360/45][a.returnSpriteCounter()%8], a.getX(),a.getY(),this);
		}
		//g.drawRect(BH.mc.getX(),BH.mc.getY(),BH.mc.getsx(),BH.mc.getsy())
		for (MagicalBox box: allBoxes){
			g.setColor(Color.BLUE);
			//g.drawOval(box.getX(), box.getY(), 2, 2);
			g.drawRect(box.getX()-5, box.getY()-5, 10, 10);
		}
		g.drawOval(BH.mc.getX(), BH.mc.getY(), 2, 2);
	}
}
