import java.util.*;
class Devil {
	private int Health = 100;
	private int angle; //angle is the angle in which the character is facing
	//-> is 0
	//<- is 180
	private final int FIREBALL=0;
	private int posx, posy;
	private int sx=30,sy=70;
	private int timeCount=0; //counter to see when to shoot a fireball
	private int sp=3;
	MainCharacter mc;
	private final int timeCountLim = 20;
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
	public int getspeed(){
		return sp;
	}
	public int getangle(){
		return angle;
	}
	public void setX(int x){
		posx = x;
	}
	public void setY(int y){
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
	public boolean collideMC(){
		if (posx > mc.getX() + mc.getsx() || posx + sx < mc.getX() || posy < mc.getY()+mc.getsy() || posy + sy < mc.getY())
			return false;
		return true;
	}
	public void addTime(BoxHead BH){
		//call this method during the main method with a timer to call it at regular intervals
		timeCount++;
		if (timeCount==timeCountLim){
			timeCount=0;
			BH.fireballs.add(new PosPair(posx,posy,angle,FIREBALL)); 
		}
	}
}
