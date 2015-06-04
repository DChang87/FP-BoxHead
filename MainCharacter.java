import java.util.*;
class MainCharacter {
	private int health=1000, ANGLE,sx = 30, sy = 70, sp=10;
	private String name;
	private int cweapon = 1;
	public double posx, posy;
	private final int NUMOFWEAPONS = 7;
	private int[] weapons = new int[NUMOFWEAPONS];
	//this stores all the weapons available to the character
	public MainCharacter(String n, int px, int py){
		name = n;
		posx = px;
		posy = py;
		for (int i=0;i<NUMOFWEAPONS;i++){
			weapons[i]=0;
			//all weapons have default at 0
			//pistol is unlimited
		}
		for (int i=0; i!=7; ++i)//temporary
			weapons[i] = 100;
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
	public void setWeapon(int w){
		cweapon=w;
	}
	public int getsx(){
		return sx;
	}
	public int getsy(){
		return sy;
	}
	public int calculateHealth(){
		return (int)(health/1000*20);
	}
	public void addAmmo(int n){
		weapons[n]=100;
	}
	public int getAmmo(int n){
		return weapons[n];
	}
	public void useAmmo(int n){
		weapons[n]-=1;
		if (weapons[n]==0){
			cweapon=1;
		}
	}
}
