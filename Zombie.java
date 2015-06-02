import java.util.*;
class Zombie {
	MainCharacter mc;
	private int Health = 20;
	private int sx = 30, sy = 70, sp = 2;
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
		return ANG;
	}
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
			return false;
		//System.out.println("AWRAF");
		
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
}
