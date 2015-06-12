public class SentryGun {
	// What would you take with you to a zombie apocalypse?
	// a gun? a grenade? a Bomb?
	// nono, introducing the brand new product
	// SentryGun 2000, a.k.a. Man's best friend
	// This product is sure to satisfy your needs
	// It will not only protect you from zombies
	// but it will cost you a pretty penny
	
	private double posX, posY;
	private int range = 1000, dmg = 10;
	private int sx = 20, sy = 20; //size of sentry gun
	private int Health = 1000, ang = 0, ammo = 20; //health, angle in which the gun is shooting, ammo remaining
	private int rectsx = 34, rectsy = 47; //size of the characters on screen
	SentryGun(int x, int y){
		posX = x;
		posY = y;
	}
	
	//getter setters
	//position
	public int getX(){return (int)posX;}
	public int getY(){return (int)posY;}
	public void setX(double x){posX = x;}
	public void setY(double y){posY = y;}
	//centre of sentry gun
	public int getcx(){return (int)(posX + sx/2);}
	public int getcy(){return (int)(posY + sy/2);}
	//health
	public int getHealth(){return Health;}
	public void setHealth(int hp){Health = hp;}
	//ammo
	public int getammo(){return ammo;}
	public void setammo(int a){ammo = a;}
	//angle
	public void setAngle(double angle){ang = (int)angle;}
	public int getAngle(){return ang;}
	//damage and range
	public int getrange(){return range;}
	public int getdmg(){return dmg;}

	//see if the sentrygun collides with a character at (x,y)
	public boolean rectcollision(int x, int y){
		if (posX > x + rectsx || posX + sx < x || posY > y + rectsy || posY + sy < y)
			return false;
		return true;
	}
}
