
public class SentryGun {
	private double posX, posY;
	private int range = 100, dmg = 10, sx = 20, sy = 20, Health = 1000, rectsx = 37, rectsy = 43;
	SentryGun(int x, int y){
		posX = x;
		posY = y;
	}
	public int getX(){
		return (int)posX;
	}
	public int getY(){
		return (int)posY;
	}
	public int getHealth(){
		return Health;
	}
	public void setHealth(int hp){
		Health = hp;
	}
	public void setX(double x){
		posX = x;
	}
	public void setY(double y){
		posY = y;
	}
	public int getrange(){
		return range;
	}
	public int getdmg(){
		return dmg;
	}
	public boolean rectcollision(int x, int y){
		if (posX > x + rectsx || posX + sx < x || posY > y + rectsy || posY + sy < y)
			return false;
		return true;
	}
}
