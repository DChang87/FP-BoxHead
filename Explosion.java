public class Explosion {
	final private int sx = 100, sy = 100, rectsx = 34, rectsy = 47, dmg = 1000;
	private double posX, posY;
	private int spritenum = 0;
	Explosion(int x, int y){
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
	public int getsx(){
		return sx;
	}
	public int getsy(){
		return sy;
	}
	public int getdmg(){
		return dmg;
	}
	public void incrementsprite(){
		spritenum++;
	}
	public int getspritenum(){
		return spritenum;
	}
}
