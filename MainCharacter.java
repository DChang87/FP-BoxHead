import java.util.*;
class MainCharacter {
	public final int full_health=1000;
	private int health=full_health, ANGLE,sx = 30, sy = 70, sp=10;
	
	private String name;
	private int cweapon = 1;
	//cweapon:
	/*
	 * pistol = 1
	 * uzi = 2
	 * pistol = 3
	 */
	public double posx, posy;
	private final int NUMOFWEAPONS = 7;
	private final int UZI = 2,SHOTGUN =3, BARREL=4,GRENADE=5,FAKEWALLS=6;
	private int[] weapons = new int[NUMOFWEAPONS];
	private int[] maxAmmo = new int[NUMOFWEAPONS]; //this stores all the max ammo values but it changes as the character receives upgrades
	private int[] ORIGINALmaxAmmo = new int[NUMOFWEAPONS]; //this stores the original max ammo values
	//this stores all the weapons available to the character
	private int[] weaponsp = new int[NUMOFWEAPONS];
	private int[] ORIGINALweaponsp = new int[NUMOFWEAPONS];
	private int[] weapondmg = new int[NUMOFWEAPONS];
	private int[] ORIGINALweapondmg = new int[NUMOFWEAPONS];
	
	public MainCharacter(String n, int px, int py){
		name = n;
		posx = px;
		posy = py;
		loadORMaxAmmo();
		loadORWeaponSpeed();
		loadORWeaponDmg();
		for (int i=0;i<NUMOFWEAPONS;i++){
			weapons[i]=0;
			//all weapons have default at 0
			//pistol is unlimited
		}
		
		//for (int i=0; i!=7; ++i)//temporary
			//weapons[i] = 100;
	}
	public void loadORWeaponDmg(){
		for (int i=0;i<NUMOFWEAPONS;i++){
			ORIGINALweaponsp[i]=0;
			//just in case
		}
		ORIGINALweapondmg[1]=10;
		loadWeaponDmg();
	}
	public void loadWeaponDmg(){
		for (int i=0;i<NUMOFWEAPONS;i++){
			weapondmg[i]=ORIGINALweapondmg[i];
		}
	}
	public int getAtWeaponSpeed(int weapon){
		return weaponsp[weapon];
	}
	public void loadORWeaponSpeed(){
		for (int i=0;i<NUMOFWEAPONS;i++){
			ORIGINALweaponsp[i]=0;
			//just in case
		}
		ORIGINALweaponsp[1]=5; //pistol
		ORIGINALweaponsp[2]=10; //uzi
		ORIGINALweaponsp[3]=10;//pistol
		loadWeaponSpeed();
	}
	public void loadWeaponSpeed(){
		for (int i=0;i<NUMOFWEAPONS;i++){
			weaponsp[i]=ORIGINALweaponsp[i];
		}
	}
	public void loadORMaxAmmo(){
		for (int i=0;i<NUMOFWEAPONS;i++){
			ORIGINALweaponsp[i]=0;
			//just in case
		}
		ORIGINALmaxAmmo[UZI]=100;
		ORIGINALmaxAmmo[SHOTGUN]=20;
		ORIGINALmaxAmmo[BARREL]=10;
		ORIGINALmaxAmmo[FAKEWALLS]=5;
		ORIGINALmaxAmmo[GRENADE]=5;
		loadMaxAmmo();
	}
	public void loadMaxAmmo(){
		for (int i=0;i<ORIGINALmaxAmmo.length;i++){
			maxAmmo[i]=ORIGINALmaxAmmo[i];
		}
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
	public int getcx(){
		return (int)posx + sx/2;
	}
	public int getcy(){
		return (int)posy + sy/2;
	}
	public int calculateHealth(){
		return (int)(health*30/1000.0);
	}
	public void addAmmo(int n){
		weapons[n]=maxAmmo[n];
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
