
public class Barrel {
	final int dmg = 100;
	private int Health = 100, sx = 20, sy = 50, rectsx = 34, rectsy = 47;
	public double posX, posY;
	Barrel(int x, int y){
		posX = x;
		posY = y;
	}
	public int getX(){
		return (int)posX;
	}
	public int getY(){
		return (int)posY;
	}
	public void setX(double x){
		posX = x;
	}
	public void setY(double y){
		posY = y;
	}
	public int getHealth(){
		return Health;
	}
	public void setHealth(int hp){
		Health = hp;
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
