import java.util.*;
class MainCharacter {
	private int health, ANGLE,sx = 70, sy = 30;
	private String name;
	private int cweapon = 1000;
	public double posx, posy;
	private final int NUMOFWEAPONS = 5;
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
	public int getWeapon(){
		return cweapon;
	}
}
