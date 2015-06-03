import java.util.*;
class MainCharacter {
	private int health=1000, ANGLE,sx = 30, sy = 70, sp=10;
	private String name;
	private int cweapon = 1;
	public double posx, posy;
	private final int NUMOFWEAPONS = 7;
	private int[] weapons = new int[NUMOFWEAPONS];
	public MainCharacter(String n, int px, int py){
		name = n;
		posx = px;
		posy = py;
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
	public double getDX(){
		return posx;
	}
	public double getDY(){
		return posy;
	}
	public void setX(double x){
		posx = x;
	}
	public void setY(double y){
		posy = y;
	}
	public void setHealth(int h){
		health=h;
	}
	public int getHealth(){
		return health;
	}
	public String getName(){
		return name;
	}
	public void setName(String n){
		name = n;
	}
	public int getLength(){
		return sx;
	}
	public int getWidth(){
		return sy;
	}
	public int getANGLE(){
		return ANGLE;
	}
	public void setAngle(int ang){
		ANGLE = ang;
	}
	public int getWeapon(){
		return cweapon;
	}
	public int getsx(){
		return sx;
	}
	public int getsy(){
		return sy;
	}
	public int calculateHealth(){
		return (int)(health/100*20);
	}
	public void addAmmo(int n){
		weapons[n]=100;
	}
}
