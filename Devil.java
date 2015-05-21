import java.util.*;
class Devil {
	private int Health = 100;
	private int angle; //angle is the angle in which the character is facing
	//-> is 0
	//<- is 180
	private double posx, posy;
	private int timeCount=0; //counter to see when to shoot a fireball
	private final int timeCountLim = 20;
	public Devil(int x, int y, int ang){
		posx = x;
		posy = y;
		angle = ang;
	}
	public int getX(){
		return (int)posx;
	}
	public int getY(){
		return (int)posy;
	}
	public int getspeed(){
		return sp;
	}
	public int getangle({
		return angle;
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
	public boolean getCollide(int x, int y){
		return (x >= posx && x <= posx+sx && y >= posy && y <= posy+sy);
	}
	public int getsx(){
		return sx;
	}
	public int getsy(){
		return sy;
	}
	public void addTime(){
		//call this method during the main method with a timer to call it at regular intervals
		timeCount++;
		if (timeCount==timeCountLim){
			timeCount=0;
			BH.mc.fireballs.add(new PosPair(posx,posy,angle)); 
		}
	}
}
