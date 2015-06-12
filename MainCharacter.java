import java.util.*;
class MainCharacter {
	//The character you'll be playing as :)
	//He's a very dangerous child and capable of things beyond belief
	
	//A young never aging young boy was raised in rural Japan
	//Ness is courageous and the envy of many children
	//He posseses strong psychic abilities
	//In the past he was known for his abilities to use Slingshots as weapons
	//but as time passed and technology improved, he found that guns were much more convenient
	//Along with power comes great responsibility.
	//Ness was not capable of that and ended up becoming evil and a big bully to young children
	//The Zombies and Devils depicted in the game are the children he bullied after
	//Having experienced the effects of the apocalypse.
	
	public final int full_health=1000;
	private int health=full_health, ANGLE,sx = 37, sy = 43, sp=10;
	
	private int cweapon = 1;
	public double posx, posy;
	private final int NUMOFWEAPONS = 9;
	private final int PISTOL=1,UZI = 2,SHOTGUN =3, BARREL=4,GRENADE=5,BARRICADE=6, SENTRY=7; // weapon numbers

	private int[] ORIGINALmaxAmmo = new int[NUMOFWEAPONS]; //this stores the original max ammo values
	private int[] maxAmmo = new int[NUMOFWEAPONS]; //this stores all the max ammo values but it changes as the character receives upgrades
	
	//this stores all the current weapon ammo available to the character
	private int[] cweaponAmmo = new int[NUMOFWEAPONS];
	
	//weapon speed
	private int[] ORIGINALweaponsp = new int[NUMOFWEAPONS];
	private int[] weaponsp = new int[NUMOFWEAPONS];
	
	//weapon damage
	private int[] ORIGINALweapondmg = new int[NUMOFWEAPONS];
	private int[] weapondmg = new int[NUMOFWEAPONS];
	//weapon distance
	private int[] ORIGINALweapondist= new int[NUMOFWEAPONS];
	private int[] weapondist = new int[NUMOFWEAPONS];
	
	//shotgun width
	private int shotgunWide = 0;
	//Upgrade, consecutive shots
	private boolean[] consecutiveShoot = new boolean[NUMOFWEAPONS];
	
	public MainCharacter(int px, int py){
		posx = px;
		posy = py;
		loadORMaxAmmo();//done
		loadORWeaponSpeed();//done
		loadORdist();//done
		loadORdmg(); //done
		///loadCAmmo();
		loadConsecutiveShoot();
		//for (int i=0; i!=8; ++i)//temporary
			//cweaponAmmo[i] = 100;
		
	}
	//how wide the shotgun shot is
	public int getSGW(){
		return shotgunWide;
	}
	//Sets the width of shotgun
	public void setSGW(int n){
		shotgunWide = n;
	}
	/////////////DAMAGE//////////////////
	public void loadORdmg(){
		ORIGINALweapondmg[PISTOL]=5;
		ORIGINALweapondmg[UZI]=15;
		ORIGINALweapondmg[SHOTGUN]=8;
		//barrel,grenade has its damage saved in its class
		ORIGINALweapondmg[SENTRY]=5;
		loaddmg();
	}
	public void loaddmg(){
		for (int i=0;i<NUMOFWEAPONS;i++){
			weapondmg[i] = ORIGINALweapondmg[i];
		}
	}
	public void setDmg(int weapon,int dmg){
		weapondmg[weapon]=dmg;
	}
	public int getdmg(int type){
		return weapondmg[type];
	}
	
	/////////////DISTANCE//////////////////
	public void loadORdist(){
		for (int i=0; i<NUMOFWEAPONS; ++i){
			ORIGINALweapondist[i] = 250;
		}
		ORIGINALweapondist[2] = 350;
		ORIGINALweapondist[3]=200;
		loaddist();
	}
	public void loaddist(){
		for (int i=0;i<NUMOFWEAPONS;i++){
			weapondist[i] = ORIGINALweapondist[i];
		}
	}
	public void setDist(int weapon,int dist){
		weapondist[weapon]=dist;
	}
	public int getMaxDist(int a){
		return weapondist[a];
	}
	
	
	/////////////////SPEED/////////////////
	public void loadORWeaponSpeed(){
		ORIGINALweaponsp[1]=20; //pistol
		ORIGINALweaponsp[2]=30; //uzi
		ORIGINALweaponsp[3]=30;//shotgun
		loadWeaponSpeed();
	}
	public void loadWeaponSpeed(){
		for (int i=0;i<ORIGINALweaponsp.length;i++){
			weaponsp[i]=ORIGINALweaponsp[i];
		}
	}
	public int getAtWeaponSpeed(int weapon){
		return weaponsp[weapon];
	}
	public void setWeaponSpeed(int weapon,int speed){
		weaponsp[weapon]=speed;
	}
	
	
	/////////MAX ammo//////////////
	public void loadORMaxAmmo(){
		ORIGINALmaxAmmo[UZI]=50;
		ORIGINALmaxAmmo[SHOTGUN]=20;
		ORIGINALmaxAmmo[BARREL]=10;
		ORIGINALmaxAmmo[BARRICADE]=5;
		ORIGINALmaxAmmo[GRENADE]=5;
		ORIGINALmaxAmmo[BARRICADE]=5;
		ORIGINALmaxAmmo[SENTRY]=5;
		loadMaxAmmo();
	}
	public void loadMaxAmmo(){
		for (int i=0;i<ORIGINALmaxAmmo.length;i++){
			maxAmmo[i]=ORIGINALmaxAmmo[i];
		}
	}
	public void setMaxAmmo(int weapon,int max){
		maxAmmo[weapon]=max;
	}
	public int getMaxAmmo(int weapon){
		return ORIGINALmaxAmmo[weapon];
	}
	//////////AMMO//////////
	public void addAmmo(int n){
		System.out.println(n+" "+getMaxAmmo(n)+"ADAMMO");
		cweaponAmmo[n]=getMaxAmmo(n);
		System.out.println(cweaponAmmo[n]+"CWEAPONAMMO");
	}
	public int getAmmo(int n){
		return cweaponAmmo[n];
	}
	public void useAmmo(int n){
		cweaponAmmo[n]-=1;
		if (cweaponAmmo[n]==0){
			cweapon=1;
		}
	}
	public void loadCAmmo(){
		//why does this method even exist
		//load current ammo
		for (int i=0;i<NUMOFWEAPONS;i++){
			cweaponAmmo[i]=10;
			//all cweaponAmmo have default at 0
			//pistol is unlimited
		}
	}
	public void unloadCAmmo(){ //restart, no ammo :(
		for (int i=0;i<NUMOFWEAPONS;i++){
			cweaponAmmo[i]=0;
		}
	}
	/////////CONSECUTIVE SHOOT////////////
	public void loadConsecutiveShoot(){
		for (int i=0;i<NUMOFWEAPONS;i++){
			consecutiveShoot[i]=false;
		}
	}
	public void setConsecutiveShoot(int weapon){
		consecutiveShoot[weapon]=true;
	}
	public boolean getConsecutiveShoot(int weapon){
		return consecutiveShoot[weapon];
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
	public int calculateHealth(){
		return (int)(health*30/1000.0);
	}
	
}
