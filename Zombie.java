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
	
	// P.S. The Zombie even gives free hugs! Don't be shy.
	MainCharacter mc;
	private int Health = 20;
	private int sx = 34, sy = 47, sp = 2, dmg = 10;
	private double posx, posy;
	private int spriteCounter=0;
	private int ANG;
	public Zombie(int x, int y, int ang, MainCharacter mccc){
		posx = x;
		posy = y;
		ANG=ang;
		mc = mccc;
	}
	public int getX(){
		return (int)posx;
	}
	public int getY(){
		return (int)posy;
	}
	public int getcx(){
		return (int)(posx + sx/2);
	}
	public int getcy(){
		return (int)(posy + sy/2);
	}
	public double getcdx(){
		return (posx + sx/2);
	}
	public double getcdy(){
		return (posy + sy/2);
	}
	public int getdmg(){
		return dmg;
	}
	public double getDX(){
		return posx;
	}
	public double getDY(){
		return posy;
	}
	public int getsx(){
		return sx;
	}
	public int getsy(){
		return sy;
	}
	public int getspeed(){
		return sp;
	}
	public int getAngle(){
		return ANG;
	}
	//Changing between sprites
	public void addToCounter(){
		spriteCounter++;
	}
	public int returnSpriteCounter(){
		return spriteCounter;
	}
	public void setAngle(double n){
		ANG = (int)n;
	}
	public void setX(double x){
		posx = x;
	}
	public void setY(double y){
		posy = y;
	}
	public void inContact(MainCharacter Leo){
		if (Leo.getX()==posx && Leo.getY()==posy){
			Leo.setHealth(Leo.getHealth()-5);
		}
	}
	public int getHealth(){
		return Health;
	}
	public void setHealth(int hp){
		Health = hp;
	}
	public boolean collideMC(){
		if (posx > mc.getX() + mc.getsx()+3 || posx + sx+3 < mc.getX() || posy > mc.getY()+mc.getsy()+3 || posy + sy+3 < mc.getY())
			return false;// a little extra space for safety before collision
		return true;
	}
	public boolean getCollide(int x, int y){//if a point collides with the zombie
		return (x >= posx && x <= posx+sx && y >= posy && y <= posy+sy);
	}
}
