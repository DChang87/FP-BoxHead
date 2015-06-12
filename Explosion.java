public class Explosion {
	// a big BOOM
	//massive damage, obliterating everything
	//INCLUDING the player
	//Be careful what you do with the barrels
	final private int sx = 100, sy = 100, rectsx = 34, rectsy = 47, dmg = 1000, range = 100;
	private double posX, posY;
	private int ctime = 0;//time the explosion pic lasts
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
	public void incrementtime(){
		ctime++;
	}
	public int getctime(){
		return ctime;
	}
	public int getrange(){
		return range;
	}
}
