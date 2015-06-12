import java.util.*;
public class Devil {
	//more advanced than Zombies. can shoot fireballs that do massive damage
	
	private int Health = 100;
	private int angle; //angle is the angle in which the character is facing
	//-> is 0
	//<- is 180
	private final int FIREBALL=0, dmg = 15;
	private double posx, posy;
	private int sx=34,sy=47; //size of devils
	private int timeCount=0; //counter to see when to shoot a fireball
	private int sp=2, spriteCounter=0;//sp is speed
	MainCharacter mc;
	private final int timeCountLim = 100;//when reaches shoots fireball
	public Devil(int x, int y, int ang, MainCharacter mccc){
		posx = x;
		posy = y;
		angle = ang;
		mc = mccc;
	}
	
	/////////getter/setters////////////////
	//position of the devil (int)
	public int getX(){return (int)posx;}
	public int getY(){return (int)posy;}
	//position of the devil (double)
	public double getDX(){return posx;}
	public double getDY(){return posy;}
	//centre position of the devil (int)
	public int getcx(){return (int)(posx + sx/2);}
	public int getcy(){return (int)(posy+sy/2);}
	//centre position of the devil (double)
	public double getcdx(){return (posx + sx/2);}
	public double getcdy(){return (posy + sy/2);}
	//other general properties of the devil
	public int getdmg(){return dmg;}
	public int getspeed(){return sp;}
	
	public int getAngle(){return angle;}
	public void setAngle(double n){angle = (int)n;}
	public int getHealth(){return Health;}
	public void setHealth(int hp){Health = hp;}
	public void setX(double x){posx = x;}
	public void setY(double y){posy = y;}
	public int getsx(){return sx;}
	public int getsy(){return sy;}
	
	//sprite counter
	public void addToCounter(){spriteCounter++;}
	public int returnSpriteCounter(){return spriteCounter;}
	
	public boolean collideMC(){
		if (posx > mc.getX() + mc.getsx()+3 || posx + sx+3 < mc.getX() || posy > mc.getY()+mc.getsy()+3 || posy + sy+3 < mc.getY()) // a little extra for collision
			return false;
		return true;
	}
	public boolean getCollide(int x, int y){
		return (x >= posx && x <= posx+sx && y >= posy && y <= posy+sy);
	}//collision with a point for the devil
	
	
	public void addTime(BoxHead BH){
		//call this method during the main method with a timer to call it at regular intervals
		timeCount++;
		if (timeCount==timeCountLim){
			//shoot fireball when time as at the limit
			timeCount=0;
			BH.game.fireballs.add(new PosPair((int)posx,(int)posy,angle,FIREBALL)); 
		}
	}
}
