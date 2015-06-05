import java.util.*;
class Devil {
	private int Health = 100;
	private int angle; //angle is the angle in which the character is facing
	//-> is 0
	//<- is 180
	private final int FIREBALL=0, dmg = 15;
	private double posx, posy;
	private int sx=34,sy=47;
	private int timeCount=0; //counter to see when to shoot a fireball
	private int sp=2, spriteCounter=0;
	MainCharacter mc;
	private final int timeCountLim = 100;
	public Devil(int x, int y, int ang, MainCharacter mccc){
		posx = x;
		posy = y;
		angle = ang;
		mc = mccc;
	}
	public int getX(){
		return (int)posx;
	}
	public int getY(){
		return (int)posy;
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
	public int getspeed(){
		return sp;
	}
	public int getAngle(){
		return angle;
	}
	public void addToCounter(){
		spriteCounter++;
	}
	public int returnSpriteCounter(){
		return spriteCounter;
	}
	public void setAngle(double n){
		angle = (int)n;
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
		if (posx > mc.getX() + mc.getsx()+3 || posx + sx+3 < mc.getX() || posy > mc.getY()+mc.getsy()+3 || posy + sy+3 < mc.getY()) // a little extra for collision
			return false;
		return true;
	}
	public boolean getCollide(int x, int y){
		return (x >= posx && x <= posx+sx && y >= posy && y <= posy+sy);
	}
	
	public int getsx(){
		return sx;
	}
	public int getsy(){
		return sy;
	}
	public void addTime(BoxHead BH){
		//call this method during the main method with a timer to call it at regular intervals
		timeCount++;
		//System.out.println(angle);
		//System.out.println(timeCount+ " " + timeCountLim);
		if (timeCount==timeCountLim){
			timeCount=0;
			BH.game.fireballs.add(new PosPair((int)posx,(int)posy,angle,FIREBALL)); 
		}
	}
}
