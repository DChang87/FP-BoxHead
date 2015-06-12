import java.util.*;
class Zombie {
	// Since ancient times, human have seeked companionship
	// but humans are weak, with short lifespans
	// Introducing the human creation that will last forever
	// The Zombie, created by Dr. Frankenstein
	// It is guaranteed to satisfy your needs, or your money back guaranteed
	// Come get one today for only $299.00 each month for the rest of your life
	// If you get one today, Frankenstein has even offered to lower it to $199.00
	// In exchange for your body for human experiments
	//                                        -Leo Feng
	// P.S. The Zombie even gives free hugs! Don't be shy.
	MainCharacter mc;
	private int Health = 20;
	private int sx = 34, sy = 47, sp = 2, dmg = 10;  //size x, size y, speed, damage
	private double posx, posy;
	private int spriteCounter=0; //counter to decide which sprite to blit
	private int ANG;
	public Zombie(int x, int y, int ang, MainCharacter mccc){
		posx = x;
		posy = y;
		ANG=ang;
		mc = mccc;
	}
	
	/////////////getter and setters/////////////////
	//position (int)
	public int getX(){return (int)posx;}
	public int getY(){return (int)posy;}
	//position(double)
	public double getDX(){return posx;}
	public double getDY(){return posy;}
	public void setX(double x){posx = x;}
	public void setY(double y){posy = y;}
	//centre position of the zombie (int)
	public int getcx(){return (int)(posx + sx/2);}
	public int getcy(){return (int)(posy + sy/2);}
	//centre position of the zombie (double)
	public double getcdx(){return (posx + sx/2);}
	public double getcdy(){return (posy + sy/2);}
	//size
	public int getsx(){return sx;}
	public int getsy(){return sy;}
	//angle
	public int getAngle(){return ANG;}
	public void setAngle(double n){ANG = (int)n;}
	//health
	public int getHealth(){return Health;}
	public void setHealth(int hp){Health = hp;}
	//damage, speed and angle
	public int getdmg(){return dmg;}
	public int getspeed(){return sp;}
	
	//Changing between sprites
	public void addToCounter(){spriteCounter++;}
	public int returnSpriteCounter(){return spriteCounter;}
	
	public boolean collideMC(){
		//see if the zombie is in contact with the MC
		if (posx > mc.getX() + mc.getsx()+3 || posx + sx+3 < mc.getX() || posy > mc.getY()+mc.getsy()+3 || posy + sy+3 < mc.getY())
			return false;// a little extra space for safety before collision
		return true;
	}
	
	public boolean getCollide(int x, int y){
		//if a point collides with the zombie
		return (x >= posx && x <= posx+sx && y >= posy && y <= posy+sy);
	}
}
